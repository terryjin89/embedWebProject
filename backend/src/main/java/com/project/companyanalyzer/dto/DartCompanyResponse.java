package com.project.companyanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DART 기업 정보 API 응답 DTO
 *
 * DART API의 /company.json 엔드포인트 응답을 매핑
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DartCompanyResponse {

    /**
     * 에러 및 정보 코드
     * "000": 정상
     * "010": 등록되지 않은 키
     * "011": 사용할 수 없는 키
     * "013": 조회된 데이타가 없습니다
     */
    private String status;

    /**
     * 에러 및 정보 메시지
     */
    private String message;

    /**
     * 고유번호 (8자리)
     */
    @JsonProperty("corp_code")
    private String corpCode;

    /**
     * 정식명칭
     */
    @JsonProperty("corp_name")
    private String corpName;

    /**
     * 영문명칭
     */
    @JsonProperty("corp_name_eng")
    private String corpNameEng;

    /**
     * 종목명(상장사) 또는 약식명칭(기타법인)
     */
    @JsonProperty("stock_name")
    private String stockName;

    /**
     * 상장회사인 경우 주식의 종목코드
     */
    @JsonProperty("stock_code")
    private String stockCode;

    /**
     * 대표자명
     */
    @JsonProperty("ceo_nm")
    private String ceoNm;

    /**
     * 법인구분
     * Y(유가), K(코스닥), N(코넥스), E(기타)
     */
    @JsonProperty("corp_cls")
    private String corpCls;

    /**
     * 법인등록번호
     */
    @JsonProperty("jurir_no")
    private String jurirNo;

    /**
     * 사업자등록번호
     */
    @JsonProperty("bizr_no")
    private String bizrNo;

    /**
     * 주소
     */
    @JsonProperty("adres")
    private String adres;

    /**
     * 홈페이지
     */
    @JsonProperty("hm_url")
    private String hmUrl;

    /**
     * IR 홈페이지
     */
    @JsonProperty("ir_url")
    private String irUrl;

    /**
     * 전화번호
     */
    @JsonProperty("phn_no")
    private String phnNo;

    /**
     * 팩스번호
     */
    @JsonProperty("fax_no")
    private String faxNo;

    /**
     * 업종코드
     */
    @JsonProperty("induty_code")
    private String indutyCode;

    /**
     * 설립일 (YYYYMMDD)
     */
    @JsonProperty("est_dt")
    private String estDt;

    /**
     * 결산월 (MM)
     */
    @JsonProperty("acc_mt")
    private String accMt;

    /**
     * API 호출이 성공했는지 확인
     * @return 성공 여부
     */
    public boolean isSuccess() {
        return "000".equals(status);
    }
}
