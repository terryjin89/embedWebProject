package com.project.companyanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 금융위원회 주식시세정보 API - 개별 주가 데이터 DTO
 *
 * API 응답의 item 구조를 매핑합니다.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockPriceItem {

    /**
     * 기준일자 (YYYYMMDD)
     */
    @JsonProperty("basDt")
    private String basDt;

    /**
     * 단축코드 (종목코드 6자리)
     */
    @JsonProperty("srtnCd")
    private String srtnCd;

    /**
     * ISIN 코드
     */
    @JsonProperty("isinCd")
    private String isinCd;

    /**
     * 종목명
     */
    @JsonProperty("itmsNm")
    private String itmsNm;

    /**
     * 시장구분 (KOSPI/KOSDAQ/KONEX)
     */
    @JsonProperty("mrktCtg")
    private String mrktCtg;

    /**
     * 종가 (정규시장 종료 시 최종가격)
     */
    @JsonProperty("clpr")
    private String clpr;

    /**
     * 대비 (전일 대비 등락)
     */
    @JsonProperty("vs")
    private String vs;

    /**
     * 등락률 (전일 대비 등락 비율)
     */
    @JsonProperty("fltRt")
    private String fltRt;

    /**
     * 시가 (정규시장 개시 후 최초가격)
     */
    @JsonProperty("mkp")
    private String mkp;

    /**
     * 고가 (하루 중 가격의 최고치)
     */
    @JsonProperty("hipr")
    private String hipr;

    /**
     * 저가 (하루 중 가격의 최저치)
     */
    @JsonProperty("lopr")
    private String lopr;

    /**
     * 거래량 (체결수량의 누적 합계)
     */
    @JsonProperty("trqu")
    private String trqu;

    /**
     * 거래대금 (거래건별 체결가격 * 체결수량의 누적 합계)
     */
    @JsonProperty("trPrc")
    private String trPrc;

    /**
     * 상장주식수
     */
    @JsonProperty("lstgStCnt")
    private String lstgStCnt;

    /**
     * 시가총액 (종가 * 상장주식수)
     */
    @JsonProperty("mrktTotAmt")
    private String mrktTotAmt;
}
