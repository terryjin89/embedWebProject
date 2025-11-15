package com.project.companyanalyzer.service;

import com.project.companyanalyzer.dto.StockPriceApiResponse;
import com.project.companyanalyzer.dto.StockPriceDTO;
import com.project.companyanalyzer.dto.StockPriceItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 금융위원회 주식시세정보 API 서비스
 *
 * 금융위원회 공공데이터 API를 연동하여 주가 데이터를 조회합니다.
 * - API 호출 및 응답 파싱
 * - 기간별 데이터 조회 (30/60/90일)
 * - DTO 변환
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceApiService {

    private final RestTemplate restTemplate;

    @Value("${finance.api.url:https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo}")
    private String apiUrl;

    @Value("${finance.api.serviceKey:8676c1c0ad9eda160d90d5927a4dc21aace7f18de90365e4c993f72a66ce66b7}")
    private String serviceKey;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 주가 데이터 조회 (기간별)
     *
     * @param stockCode 종목코드 (6자리)
     * @param period 조회 기간 (30/60/90일)
     * @return 주가 데이터 리스트 (최신 순)
     */
    public List<StockPriceDTO> getStockPriceData(String stockCode, int period) {
        log.info("주가 데이터 조회 시작 - 종목코드: {}, 기간: {}일", stockCode, period);

        try {
            // 1. 날짜 범위 계산
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(period);

            String endBasDt = endDate.format(DATE_FORMATTER);
            String beginBasDt = startDate.format(DATE_FORMATTER);

            log.debug("조회 날짜 범위 - 시작: {}, 종료: {}", beginBasDt, endBasDt);

            // 2. API 호출
            StockPriceApiResponse response = callFinanceApi(stockCode, beginBasDt, endBasDt);

            // 3. 응답 검증
            if (response == null || response.getResponse() == null) {
                log.error("API 응답이 null입니다.");
                return Collections.emptyList();
            }

            if (!"00".equals(response.getResponse().getHeader().getResultCode())) {
                log.error("API 호출 실패 - 결과 코드: {}, 메시지: {}",
                        response.getResponse().getHeader().getResultCode(),
                        response.getResponse().getHeader().getResultMsg());
                return Collections.emptyList();
            }

            // 4. 데이터 추출 및 변환
            StockPriceApiResponse.Body body = response.getResponse().getBody();
            if (body == null || body.getItems() == null || body.getItems().getItem() == null) {
                log.warn("조회된 주가 데이터가 없습니다.");
                return Collections.emptyList();
            }

            List<StockPriceItem> items = body.getItems().getItem();
            List<StockPriceDTO> result = items.stream()
                    .map(StockPriceDTO::from)
                    .collect(Collectors.toList());

            log.info("주가 데이터 조회 완료 - 총 {}건", result.size());
            return result;

        } catch (Exception e) {
            log.error("주가 데이터 조회 실패 - 종목코드: {}, 기간: {}일", stockCode, period, e);
            throw new RuntimeException("주가 데이터 조회에 실패했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 최신 주가 데이터 조회 (1일치)
     *
     * @param stockCode 종목코드 (6자리)
     * @return 최신 주가 데이터
     */
    public StockPriceDTO getCurrentStockPrice(String stockCode) {
        log.info("최신 주가 데이터 조회 시작 - 종목코드: {}", stockCode);

        try {
            // 1. 오늘 날짜 계산
            LocalDate today = LocalDate.now();
            String todayStr = today.format(DATE_FORMATTER);

            // 2. API 호출 (최근 5일 데이터 조회 - 휴장일 고려)
            LocalDate startDate = today.minusDays(5);
            String startDateStr = startDate.format(DATE_FORMATTER);

            StockPriceApiResponse response = callFinanceApi(stockCode, startDateStr, todayStr);

            // 3. 응답 검증
            if (response == null || response.getResponse() == null) {
                log.error("API 응답이 null입니다.");
                return null;
            }

            if (!"00".equals(response.getResponse().getHeader().getResultCode())) {
                log.error("API 호출 실패 - 결과 코드: {}, 메시지: {}",
                        response.getResponse().getHeader().getResultCode(),
                        response.getResponse().getHeader().getResultMsg());
                return null;
            }

            // 4. 최신 데이터 추출
            StockPriceApiResponse.Body body = response.getResponse().getBody();
            if (body == null || body.getItems() == null || body.getItems().getItem() == null
                    || body.getItems().getItem().isEmpty()) {
                log.warn("조회된 주가 데이터가 없습니다.");
                return null;
            }

            // 가장 최신 데이터 반환 (첫 번째 아이템)
            StockPriceItem latestItem = body.getItems().getItem().get(0);
            StockPriceDTO result = StockPriceDTO.from(latestItem);

            log.info("최신 주가 데이터 조회 완료 - 날짜: {}", result.getBasDt());
            return result;

        } catch (Exception e) {
            log.error("최신 주가 데이터 조회 실패 - 종목코드: {}", stockCode, e);
            throw new RuntimeException("최신 주가 데이터 조회에 실패했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 금융위원회 API 호출
     *
     * @param stockCode 종목코드
     * @param beginBasDt 시작일자 (YYYYMMDD)
     * @param endBasDt 종료일자 (YYYYMMDD)
     * @return API 응답
     */
    private StockPriceApiResponse callFinanceApi(String stockCode, String beginBasDt, String endBasDt) {
        log.debug("Finance API 호출 - 종목코드: {}, 시작: {}, 종료: {}", stockCode, beginBasDt, endBasDt);

        // URL 빌드
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", "400")  // 최대 400개까지 조회
                .queryParam("pageNo", "1")
                .queryParam("resultType", "json")
                .queryParam("likeSrtnCd", stockCode)
                .queryParam("beginBasDt", beginBasDt)
                .queryParam("endBasDt", endBasDt)
                .build()
                .encode()
                .toUriString();

        log.debug("Request URL: {}", url);

        // API 호출
        StockPriceApiResponse response = restTemplate.getForObject(url, StockPriceApiResponse.class);

        log.debug("Finance API 응답 수신");
        return response;
    }

    /**
     * 날짜 범위 계산
     *
     * @param period 기간 (일)
     * @return [시작일자, 종료일자] (YYYYMMDD 형식)
     */
    public String[] calculateDateRange(int period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(period);

        return new String[]{
                startDate.format(DATE_FORMATTER),
                endDate.format(DATE_FORMATTER)
        };
    }
}
