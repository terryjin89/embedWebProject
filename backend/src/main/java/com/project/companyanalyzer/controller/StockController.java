package com.project.companyanalyzer.controller;

import com.project.companyanalyzer.dto.StockPriceDTO;
import com.project.companyanalyzer.service.FinanceApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 주가 정보 컨트롤러
 *
 * 금융위원회 주식시세정보 API 연동을 통한 주가 데이터 제공
 */
@Slf4j
@Tag(name = "주가 API", description = "주가 정보 조회 API")
@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final FinanceApiService financeApiService;

    /**
     * 주가 차트 데이터 조회
     *
     * @param stockCode 종목코드 (6자리)
     * @param period 조회 기간 (30/60/90일, 기본값: 30)
     * @return 주가 데이터 리스트 (최신 순)
     */
    @Operation(
            summary = "주가 차트 데이터 조회",
            description = "금융위원회 API를 통해 종목의 주가 차트 데이터를 조회합니다. " +
                    "기간은 30일, 60일, 90일 중 선택 가능합니다."
    )
    @GetMapping("/{stockCode}/chart")
    public ResponseEntity<List<StockPriceDTO>> getStockChart(
            @Parameter(description = "종목코드 (6자리)", example = "005930", required = true)
            @PathVariable String stockCode,
            @Parameter(description = "조회 기간 (30/60/90일)", example = "30")
            @RequestParam(defaultValue = "30") int period
    ) {
        log.info("주가 차트 조회 요청 - 종목코드: {}, 기간: {}일", stockCode, period);

        // 기간 검증 (30, 60, 90만 허용)
        if (period != 30 && period != 60 && period != 90) {
            log.warn("잘못된 기간 파라미터: {} - 기본값 30일 사용", period);
            period = 30;
        }

        List<StockPriceDTO> stockPrices = financeApiService.getStockPriceData(stockCode, period);

        log.info("주가 차트 조회 완료 - 종목코드: {}, 기간: {}일, 결과: {}건",
                stockCode, period, stockPrices.size());

        return ResponseEntity.ok(stockPrices);
    }

    /**
     * 최신 주가 정보 조회
     *
     * @param stockCode 종목코드 (6자리)
     * @return 최신 주가 데이터
     */
    @Operation(
            summary = "최신 주가 정보 조회",
            description = "종목의 가장 최신 주가 정보를 조회합니다."
    )
    @GetMapping("/{stockCode}/current")
    public ResponseEntity<StockPriceDTO> getCurrentStockPrice(
            @Parameter(description = "종목코드 (6자리)", example = "005930", required = true)
            @PathVariable String stockCode
    ) {
        log.info("최신 주가 조회 요청 - 종목코드: {}", stockCode);

        StockPriceDTO stockPrice = financeApiService.getCurrentStockPrice(stockCode);

        if (stockPrice == null) {
            log.warn("최신 주가 데이터가 없습니다 - 종목코드: {}", stockCode);
            return ResponseEntity.noContent().build();
        }

        log.info("최신 주가 조회 완료 - 종목코드: {}, 날짜: {}",
                stockCode, stockPrice.getBasDt());

        return ResponseEntity.ok(stockPrice);
    }
}
