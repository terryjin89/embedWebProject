package com.project.companyanalyzer.controller;

import com.project.companyanalyzer.dto.ExchangeRateDTO;
import com.project.companyanalyzer.dto.HistoricalRateDTO;
import com.project.companyanalyzer.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 환율 정보 컨트롤러
 *
 * 한국수출입은행 환율 API 연동을 통한 환율 정보 제공
 */
@Slf4j
@Tag(name = "환율 API")
@RestController
@RequestMapping("/api/exchange-rates")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    /**
     * 환율 정보 조회
     *
     * @param searchDate 조회 날짜 (YYYYMMDD 형식, 선택사항 - 미입력 시 오늘 날짜)
     * @return 주요 10개국 환율 정보 (전일대비 포함)
     */
    @Operation(
            summary = "환율 정보 조회",
            description = "한국수출입은행 API를 통해 주요 10개국 환율 정보를 조회합니다. (USD, JPY, EUR, CNH, GBP, CHF, CAD, AUD, HKD, SGD)"
    )
    @GetMapping
    public ResponseEntity<List<ExchangeRateDTO>> getExchangeRates(
            @Parameter(description = "조회 날짜 (YYYYMMDD 형식)", example = "20251114")
            @RequestParam(required = false) String searchDate
    ) {
        log.info("환율 조회 요청 - searchDate: {}", searchDate);

        // searchDate가 없으면 오늘 날짜 사용
        if (searchDate == null || searchDate.trim().isEmpty()) {
            searchDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            log.debug("searchDate 미입력 - 오늘 날짜 사용: {}", searchDate);
        }

        List<ExchangeRateDTO> exchangeRates = exchangeRateService.getExchangeRates(searchDate);

        log.info("환율 조회 완료 - 결과: {}건", exchangeRates.size());
        return ResponseEntity.ok(exchangeRates);
    }

    /**
     * 특정 통화 환율 정보 조회
     *
     * @param curUnit 통화 코드 (예: USD, JPY, EUR)
     * @param searchDate 조회 날짜 (YYYYMMDD 형식, 선택사항)
     * @return 특정 통화 환율 정보
     */
    @Operation(
            summary = "특정 통화 환율 조회",
            description = "특정 통화의 환율 정보를 조회합니다."
    )
    @GetMapping("/{curUnit}")
    public ResponseEntity<ExchangeRateDTO> getExchangeRate(
            @Parameter(description = "통화 코드", example = "USD")
            @PathVariable String curUnit,
            @Parameter(description = "조회 날짜 (YYYYMMDD 형식)", example = "20251114")
            @RequestParam(required = false) String searchDate
    ) {
        log.info("특정 통화 환율 조회 요청 - curUnit: {}, searchDate: {}", curUnit, searchDate);

        // searchDate가 없으면 오늘 날짜 사용
        if (searchDate == null || searchDate.trim().isEmpty()) {
            searchDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        List<ExchangeRateDTO> exchangeRates = exchangeRateService.getExchangeRates(searchDate);

        // 특정 통화 필터링
        ExchangeRateDTO result = exchangeRates.stream()
                .filter(rate -> rate.getCurUnit().equals(curUnit))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("통화 코드를 찾을 수 없습니다: " + curUnit));

        log.info("특정 통화 환율 조회 완료 - curUnit: {}", curUnit);
        return ResponseEntity.ok(result);
    }

    /**
     * 특정 통화의 과거 환율 데이터 조회 (차트용)
     *
     * @param curUnit 통화 코드 (예: USD)
     * @param days    조회 기간 (일)
     * @return 과거 환율 데이터 리스트
     */
    @Operation(
            summary = "과거 환율 조회 (차트용)",
            description = "특정 통화의 과거 환율 정보를 기간(일)만큼 조회하여 차트용 데이터로 반환합니다."
    )
    @GetMapping("/{curUnit}/historical")
    public ResponseEntity<List<HistoricalRateDTO>> getHistoricalRates(
            @Parameter(description = "통화 코드", example = "USD")
            @PathVariable String curUnit,
            @Parameter(description = "조회 기간(일)", example = "30")
            @RequestParam(defaultValue = "30") int days
    ) {
        log.info("과거 환율 조회 요청 - curUnit: {}, days: {}", curUnit, days);

        List<HistoricalRateDTO> historicalRates = exchangeRateService.getHistoricalRates(curUnit, days);

        log.info("과거 환율 조회 완료 - curUnit: {}, 결과: {}건", curUnit, historicalRates.size());
        return ResponseEntity.ok(historicalRates);
    }
}
