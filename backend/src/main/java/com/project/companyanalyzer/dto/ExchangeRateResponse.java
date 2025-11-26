package com.project.companyanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 한국수출입은행 환율 API 응답 DTO
 * API 원본 응답 구조를 그대로 매핑
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {

    /**
     * 조회 결과 코드
     * 1: 성공, 2: DATA코드 오류, 3: 인증코드 오류, 4: 일일제한횟수 마감
     */
    //private Integer result;

    /**
     * 통화 코드 (예: USD, JPY, EUR)
     */
    @JsonProperty("cur_unit")
    private String curUnit;

    /**
     * 전신환(송금) 받으실때 환율
     */
    @JsonProperty("ttb")
    private String ttb;

    /**
     * 전신환(송금) 보내실때 환율
     */
    @JsonProperty("tts")
    private String tts;

    /**
     * 매매 기준율
     */
    @JsonProperty("deal_bas_r")
    private String dealBasR;

    /**
     * 장부가격
     */
    @JsonProperty("bkpr")
    private String bkpr;

    /**
     * 년환가료율
     */
    @JsonProperty("yy_efee_r")
    private String yyEfeeR;

    /**
     * 10일환가료율
     */
    @JsonProperty("ten_dd_efee_r")
    private String tenDdEfeeR;

    /**
     * 서울외국환중개 장부가격
     */
    @JsonProperty("kftc_bkpr")
    private String kftcBkpr;

    /**
     * 서울외국환중개 매매기준율
     */
    @JsonProperty("kftc_deal_bas_r")
    private String kftcDealBasR;

    /**
     * 국가/통화명 (예: 미국 달러, 일본 옌)
     */
    @JsonProperty("cur_nm")
    private String curNm;
}
