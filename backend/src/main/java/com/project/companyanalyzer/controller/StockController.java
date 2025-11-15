package com.project.companyanalyzer.controller;

import com.project.companyanalyzer.dto.StockChartResponse;
import com.project.companyanalyzer.dto.StockCurrentResponse;
import com.project.companyanalyzer.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 주가 정보 컨트롤러
 *
 * 금융위원회 주식시세정보 API 연동을 통한 주가 데이터 제공
 * 기업 정보와 함께 주가 데이터를 반환합니다.
 */
@Slf4j
@Tag(name = "주가 API", description = "주가 정보 조회 API")
@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    /**
     * 주가 차트 데이터 조회
     *
     * @param stockCode 종목코드 (6자리)
     * @param period 조회 기간 (30/60/90일, 기본값: 30)
     * @return 주가 차트 데이터 + 기업 정보
     */
    @Operation(
            summary = "주가 차트 데이터 조회",
            description = "금융위원회 API를 통해 종목의 주가 차트 데이터를 조회합니다. " +
                    "기간은 30일, 60일, 90일 중 선택 가능하며, 기업 정보도 함께 반환됩니다."
    )
    @GetMapping("/{stockCode}/chart")
    public ResponseEntity<StockChartResponse> getStockChart(
            @Parameter(description = "종목코드 (6자리)", example = "005930", required = true)
            @PathVariable String stockCode,
            @Parameter(description = "조회 기간 (30/60/90일)", example = "30")
            @RequestParam(defaultValue = "30") int period
    ) {
        log.info("[StockController] 주가 차트 조회 요청 - 종목코드: {}, 기간: {}일", stockCode, period);

        // 기간 검증 (30, 60, 90만 허용)
        if (period != 30 && period != 60 && period != 90) {
            log.warn("[StockController] 잘못된 기간 파라미터: {} - 기본값 30일 사용", period);
            period = 30;
        }

        try {
            StockChartResponse response = stockService.getStockChart(stockCode, period);

            log.info("[StockController] 주가 차트 조회 완료 - 종목코드: {}, 기간: {}일, 데이터: {}건",
                    stockCode, period, response.getDataCount());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            log.error("[StockController] 주가 차트 조회 실패 - 종목코드: {}, 기간: {}일",
                    stockCode, period, e);
            throw e;
        }
    }

    /**
     * 최신 주가 정보 조회
     *
     * @param stockCode 종목코드 (6자리)
     * @return 최신 주가 데이터 + 기업 정보
     */
    @Operation(
            summary = "최신 주가 정보 조회",
            description = "종목의 가장 최신 주가 정보를 조회합니다. 기업 정보도 함께 반환됩니다."
    )
    @GetMapping("/{stockCode}/current")
    public ResponseEntity<StockCurrentResponse> getCurrentStockPrice(
            @Parameter(description = "종목코드 (6자리)", example = "005930", required = true)
            @PathVariable String stockCode
    ) {
        log.info("[StockController] 최신 주가 조회 요청 - 종목코드: {}", stockCode);

        try {
            StockCurrentResponse response = stockService.getCurrentStockPrice(stockCode);

            log.info("[StockController] 최신 주가 조회 완료 - 종목코드: {}, 날짜: {}",
                    stockCode, response.getCurrentPrice().getBasDt());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            log.error("[StockController] 최신 주가 조회 실패 - 종목코드: {}", stockCode, e);
            throw e;
        }
    }
}
