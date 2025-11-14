package com.project.companyanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DART 공시 목록 개별 아이템 DTO
 *
 * 공시 목록 API 응답의 list 배열 내 개별 아이템
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DartDisclosureItem {

    /**
     * 고유번호 (8자리)
     */
    @JsonProperty("corp_code")
    private String corpCode;

    /**
     * 법인명
     */
    @JsonProperty("corp_name")
    private String corpName;

    /**
     * 종목코드 (6자리)
     */
    @JsonProperty("stock_code")
    private String stockCode;

    /**
     * 법인구분
     * Y(유가), K(코스닥), N(코넥스), E(기타)
     */
    @JsonProperty("corp_cls")
    private String corpCls;

    /**
     * 보고서명
     */
    @JsonProperty("report_nm")
    private String reportNm;

    /**
     * 접수번호 (14자리)
     * DART 원문 링크에 사용
     */
    @JsonProperty("rcept_no")
    private String rceptNo;

    /**
     * 공시 제출인명
     */
    @JsonProperty("flr_nm")
    private String flrNm;

    /**
     * 접수일자 (YYYYMMDD)
     */
    @JsonProperty("rcept_dt")
    private String rceptDt;

    /**
     * 비고
     * 정정 공시인 경우 "유"
     */
    @JsonProperty("rm")
    private String rm;
}
