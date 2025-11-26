package com.project.companyanalyzer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.companyanalyzer.dto.ExchangeRateDTO;
import com.project.companyanalyzer.dto.ExchangeRateResponse;
import com.project.companyanalyzer.dto.HistoricalRateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 환율 정보 서비스
 *
 * 한국수출입은행 환율 API 연동 및 환율 데이터 처리
 * - API 호출 (실제/목업 모드)
 * - 주요 10개국 통화 필터링
 * - 전일대비 계산
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    @Value("${exchange.api.url:https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON}")
    private String apiUrl;

    @Value("${exchange.api.authkey:DvYPnli3e0IjaMMxPsqthtkwFfkWt9IW}")
    private String authKey;

    @Value("${exchange.api.mock-mode:true}")
    private boolean mockMode;

    /**
     * 주요 10개국 통화 코드
     */
    private static final Set<String> MAJOR_CURRENCIES = Set.of(
            "USD",      // 미국 달러
            "JPY(100)", // 일본 옌
            "EUR",      // 유로
            "CNH",      // 위안화
            "GBP",      // 영국 파운드
            "CHF",      // 스위스 프랑
            "CAD",      // 캐나다 달러
            "AUD",      // 호주 달러
            "HKD",      // 홍콩 달러
            "SGD"       // 싱가포르 달러
    );

    /**
     * 외부 노출용 주요 코드 변환 (예: JPY(100) → JPY)
     */
    private String normalizeCurrencyCode(String curUnit) {
        if (curUnit == null) return null;
        if (curUnit.startsWith("JPY")) return "JPY";
        return curUnit;
    }

    /**
     * 환율 정보 조회
     *
     * @param searchDate 조회 날짜 (YYYYMMDD)
     * @return 환율 정보 리스트 (전일대비 포함)
     */
    public List<ExchangeRateDTO> getExchangeRates(String searchDate) {
        log.info("환율 조회 시작 - 날짜: {}, 모드: {}", searchDate, mockMode ? "목업" : "실제 API");

        try {
            // 1. 현재 날짜 데이터 조회
            List<ExchangeRateResponse> currentRates = fetchExchangeRates(searchDate);

            // 2. 전일 날짜 계산
            String previousDate = calculatePreviousDate(searchDate);

            // 3. 전일 데이터 조회
            List<ExchangeRateResponse> previousRates = fetchExchangeRates(previousDate);

            // 4. 전일 데이터를 Map으로 변환 (빠른 조회를 위해)
            Map<String, ExchangeRateResponse> previousRateMap = previousRates.stream()
                    .collect(Collectors.toMap(
                            ExchangeRateResponse::getCurUnit,
                            rate -> rate,
                            (existing, replacement) -> existing
                    ));

            // 5. 주요 10개국 통화 필터링 및 DTO 변환 (전일대비 계산 포함)
            List<ExchangeRateDTO> result = currentRates.stream()
                    .filter(rate -> MAJOR_CURRENCIES.contains(rate.getCurUnit()))
                    .map(currentRate -> {
                        ExchangeRateResponse previousRate = previousRateMap.get(currentRate.getCurUnit());
                        return convertToDTO(currentRate, previousRate, searchDate);
                    })
                    .collect(Collectors.toList());

            log.info("환율 조회 완료 - 총 {}건", result.size());
            return result;

        } catch (Exception e) {
            log.error("환율 조회 실패", e);
            throw new RuntimeException("환율 정보 조회에 실패했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 환율 데이터 조회 (실제 API 또는 목업)
     */
    private List<ExchangeRateResponse> fetchExchangeRates(String searchDate) throws IOException {
        if (mockMode) {
            return fetchMockData(searchDate);
        } else {
            return fetchFromApi(searchDate);
        }
    }

    /**
     * 실제 API 호출
     */
    private List<ExchangeRateResponse> fetchFromApi(String searchDate) {
        log.debug("실제 API 호출 - 날짜: {}", searchDate);

        String url = String.format("%s?authkey=%s&searchdate=%s&data=AP01",
                apiUrl, authKey, searchDate);

        ExchangeRateResponse[] response = restTemplate.getForObject(url, ExchangeRateResponse[].class);

        if (response == null || response.length == 0) {
            throw new RuntimeException("API 응답이 비어있습니다.");
        }

        return Arrays.asList(response);
    }

    /**
     * 목업 데이터 조회
     */
    private List<ExchangeRateResponse> fetchMockData(String searchDate) throws IOException {
        log.debug("목업 데이터 조회 - 날짜: {}", searchDate);

        String mockFilePath = String.format("exchange-rate-mock-%s.json", searchDate);

        try {
            ClassPathResource resource = new ClassPathResource(mockFilePath);
            return objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<List<ExchangeRateResponse>>() {}
            );
        } catch (IOException e) {
            log.warn("목업 파일 없음: {} - 기본 목업 데이터 사용", mockFilePath);
            // 기본 목업 파일 사용
            ClassPathResource defaultResource = new ClassPathResource("exchange-rate-mock.json");
            return objectMapper.readValue(
                    defaultResource.getInputStream(),
                    new TypeReference<List<ExchangeRateResponse>>() {}
            );
        }
    }

    /**
     * ExchangeRateResponse를 ExchangeRateDTO로 변환 (전일대비 계산 포함)
     */
    private ExchangeRateDTO convertToDTO(
            ExchangeRateResponse currentRate,
            ExchangeRateResponse previousRate,
            String searchDate
    ) {
        // 현재 매매 기준율 파싱 (쉼표 제거)
        BigDecimal currentDealBasR = parseBigDecimal(currentRate.getDealBasR());
        BigDecimal currentTtb = parseBigDecimal(currentRate.getTtb());
        BigDecimal currentTts = parseBigDecimal(currentRate.getTts());

        // 전일대비 계산
        BigDecimal changeAmount = BigDecimal.ZERO;
        BigDecimal changeRate = BigDecimal.ZERO;
        String changeDirection = "SAME";

        if (previousRate != null) {
            BigDecimal previousDealBasR = parseBigDecimal(previousRate.getDealBasR());

            // 변동액 = 현재 환율 - 전일 환율
            changeAmount = currentDealBasR.subtract(previousDealBasR);

            // 변동률 = (변동액 / 전일 환율) * 100
            if (previousDealBasR.compareTo(BigDecimal.ZERO) > 0) {
                changeRate = changeAmount
                        .divide(previousDealBasR, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            // 변동 방향 결정
            int comparison = changeAmount.compareTo(BigDecimal.ZERO);
            if (comparison > 0) {
                changeDirection = "UP";
            } else if (comparison < 0) {
                changeDirection = "DOWN";
            }
        }

        return ExchangeRateDTO.builder()
                .curUnit(currentRate.getCurUnit())
                .curNm(currentRate.getCurNm())
                .dealBasR(currentDealBasR)
                .ttb(currentTtb)
                .tts(currentTts)
                .changeAmount(changeAmount)
                .changeRate(changeRate)
                .changeDirection(changeDirection)
                .searchDate(searchDate)
                .build();
    }

    /**
     * 문자열을 BigDecimal로 파싱 (쉼표 제거)
     */
    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        // 쉼표 제거 후 BigDecimal 변환
        String cleanValue = value.replace(",", "");
        return new BigDecimal(cleanValue);
    }

    /**
     * 전일 날짜 계산 (YYYYMMDD 형식)
     */
    private String calculatePreviousDate(String searchDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(searchDate, formatter);
        LocalDate previousDate = date.minusDays(1);
        return previousDate.format(formatter);
    }

    /**
     * 특정 통화의 과거 환율 데이터 조회 (차트용)
     *
     * @param currencyCode 통화 코드 (예: USD)
     * @param days         조회할 기간 (일)
     * @return 과거 환율 데이터 리스트
     */
    public List<HistoricalRateDTO> getHistoricalRates(String currencyCode, int days) {
        log.info("과거 환율 조회 시작 - 통화: {}, 기간: {}일", currencyCode, days);
        List<HistoricalRateDTO> historicalRates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < days; i++) {
            LocalDate targetDate = today.minusDays(i);
            String searchDate = targetDate.format(formatter);

            try {
                List<ExchangeRateResponse> dailyRates = fetchExchangeRates(searchDate);
                dailyRates.stream()
                        .filter(rate -> currencyCode.equals(rate.getCurUnit()))
                        .findFirst()
                        .ifPresent(rate -> {
                            BigDecimal dealBasR = parseBigDecimal(rate.getDealBasR());
                            if (dealBasR.compareTo(BigDecimal.ZERO) > 0) {
                                historicalRates.add(HistoricalRateDTO.builder()
                                        .date(targetDate.format(outputFormatter))
                                        .rate(dealBasR)
                                        .build());
                            }
                        });
            } catch (Exception e) {
                log.warn("과거 환율 데이터 조회 실패 - 날짜: {}, 통화: {}, 오류: {}", searchDate, currencyCode, e.getMessage());
                // 특정 날짜에 데이터가 없거나 오류가 발생해도 계속 진행
            }
        }

        // 날짜 오름차순으로 정렬
        historicalRates.sort(Comparator.comparing(HistoricalRateDTO::getDate));

        log.info("과거 환율 조회 완료 - 통화: {}, 결과: {}건", currencyCode, historicalRates.size());
        return historicalRates;
    }
}
