package com.project.companyanalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 현재 주가 응답 DTO
 *
 * 최신 주가 데이터와 기업 정보를 함께 반환
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockCurrentResponse {

    /**
     * 기업 정보
     */
    private CompanyInfo company;

    /**
     * 현재 주가 데이터
     */
    private StockPriceDTO currentPrice;

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

        /**
         * 법인구분
         */
        private String corpCls;
    }
}
