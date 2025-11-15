package com.project.companyanalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 주가 차트 응답 DTO
 *
 * 주가 데이터와 기업 정보를 함께 반환
 * 프론트엔드 StockAreaChart 컴포넌트와 연동
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockChartResponse {

    /**
     * 기업 정보
     */
    private CompanyInfo company;

    /**
     * 주가 데이터 리스트 (최신 순)
     */
    private List<StockPriceDTO> priceData;

    /**
     * 조회 기간 (일)
     */
    private Integer period;

    /**
     * 데이터 건수
     */
    private Integer dataCount;

    /**
     * 기업 정보 내부 클래스
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyInfo {
        /**
         * 고유번호 (8자리)
         */
        private String corpCode;

        /**
         * 정식명칭
         */
        private String corpName;

        /**
         * 종목명
         */
        private String stockName;

        /**
         * 종목코드 (6자리)
         */
        private String stockCode;

        /**
         * 대표자명
         */
        private String ceoNm;
    }
}
