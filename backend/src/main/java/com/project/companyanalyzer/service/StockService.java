package com.project.companyanalyzer.service;

import com.project.companyanalyzer.dto.CompanyDTO;
import com.project.companyanalyzer.dto.StockChartResponse;
import com.project.companyanalyzer.dto.StockCurrentResponse;
import com.project.companyanalyzer.dto.StockPriceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 주가 정보 비즈니스 로직 서비스
 *
 * FinanceApiService와 CompanyService를 조합하여
 * 주가 데이터와 기업 정보를 함께 제공합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final FinanceApiService financeApiService;
    private final CompanyService companyService;

    /**
     * 주가 차트 데이터 조회 (기업 정보 포함)
     *
     * @param stockCode 종목코드 (6자리)
     * @param period 조회 기간 (30/60/90일)
     * @return 주가 차트 데이터 + 기업 정보
     */
    public StockChartResponse getStockChart(String stockCode, int period) {
        log.info("[StockService] 주가 차트 조회 - stockCode: {}, period: {}일", stockCode, period);

        // 1. 기업 정보 조회
        CompanyDTO company = getCompanyInfo(stockCode);

        // 2. 주가 데이터 조회
        List<StockPriceDTO> priceData = financeApiService.getStockPriceData(stockCode, period);

        // 3. 데이터 검증
        if (priceData == null || priceData.isEmpty()) {
            log.warn("[StockService] 주가 데이터 없음 - stockCode: {}, period: {}일", stockCode, period);
            throw new RuntimeException("주가 데이터가 없습니다. 주말/공휴일에는 데이터가 제공되지 않습니다.");
        }

        // 4. 응답 DTO 구성
        StockChartResponse response = StockChartResponse.builder()
                .company(buildCompanyInfo(company))
                .priceData(priceData)
                .period(period)
                .dataCount(priceData.size())
                .build();

        log.info("[StockService] 주가 차트 조회 완료 - stockCode: {}, dataCount: {}",
                stockCode, priceData.size());

        return response;
    }

    /**
     * 현재 주가 정보 조회 (기업 정보 포함)
     *
     * @param stockCode 종목코드 (6자리)
     * @return 현재 주가 데이터 + 기업 정보
     */
    public StockCurrentResponse getCurrentStockPrice(String stockCode) {
        log.info("[StockService] 현재 주가 조회 - stockCode: {}", stockCode);

        // 1. 기업 정보 조회
        CompanyDTO company = getCompanyInfo(stockCode);

        // 2. 현재 주가 데이터 조회
        StockPriceDTO currentPrice = financeApiService.getCurrentStockPrice(stockCode);

        // 3. 데이터 검증
        if (currentPrice == null) {
            log.warn("[StockService] 현재 주가 데이터 없음 - stockCode: {}", stockCode);
            throw new RuntimeException("현재 주가 데이터가 없습니다. 주말/공휴일에는 데이터가 제공되지 않습니다.");
        }

        // 4. 응답 DTO 구성
        StockCurrentResponse response = StockCurrentResponse.builder()
                .company(buildCurrentCompanyInfo(company))
                .currentPrice(currentPrice)
                .build();

        log.info("[StockService] 현재 주가 조회 완료 - stockCode: {}, 기준일: {}",
                stockCode, currentPrice.getBasDt());

        return response;
    }

    /**
     * 기업 정보 조회
     *
     * @param stockCode 종목코드 (6자리)
     * @return 기업 정보 DTO
     */
    private CompanyDTO getCompanyInfo(String stockCode) {
        try {
            return companyService.getCompanyByStockCode(stockCode);
        } catch (RuntimeException e) {
            log.error("[StockService] 기업 정보 조회 실패 - stockCode: {}", stockCode, e);
            throw new RuntimeException("종목코드를 찾을 수 없습니다: " + stockCode);
        }
    }

    /**
     * CompanyDTO를 StockChartResponse.CompanyInfo로 변환
     *
     * @param company 기업 정보 DTO
     * @return 차트 응답용 기업 정보
     */
    private StockChartResponse.CompanyInfo buildCompanyInfo(CompanyDTO company) {
        return StockChartResponse.CompanyInfo.builder()
                .corpCode(company.getCorpCode())
                .corpName(company.getCorpName())
                .stockName(company.getStockName())
                .stockCode(company.getStockCode())
                .ceoNm(company.getCeoNm())
                .build();
    }

    /**
     * CompanyDTO를 StockCurrentResponse.CompanyInfo로 변환
     *
     * @param company 기업 정보 DTO
     * @return 현재가 응답용 기업 정보
     */
    private StockCurrentResponse.CompanyInfo buildCurrentCompanyInfo(CompanyDTO company) {
        return StockCurrentResponse.CompanyInfo.builder()
                .corpCode(company.getCorpCode())
                .corpName(company.getCorpName())
                .stockName(company.getStockName())
                .stockCode(company.getStockCode())
                .ceoNm(company.getCeoNm())
                .corpCls(company.getCorpCls())
                .build();
    }
}
