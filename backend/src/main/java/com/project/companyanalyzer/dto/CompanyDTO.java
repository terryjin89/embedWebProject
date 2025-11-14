package com.project.companyanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.companyanalyzer.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 기업 정보 응답 DTO
 *
 * 프론트엔드에 전달할 기업 정보
 * Company 엔티티를 클라이언트 친화적인 형태로 변환
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {

    /**
     * 고유번호 (8자리)
     */
    @JsonProperty("corpCode")
    private String corpCode;

    /**
     * 정식명칭
     */
    @JsonProperty("corpName")
    private String corpName;

    /**
     * 영문명칭
     */
    @JsonProperty("corpNameEng")
    private String corpNameEng;

    /**
     * 종목명 또는 약식명칭
     */
    @JsonProperty("stockName")
    private String stockName;

    /**
     * 주식 종목코드 (6자리)
     */
    @JsonProperty("stockCode")
    private String stockCode;

    /**
     * 대표자명
     */
    @JsonProperty("ceoNm")
    private String ceoNm;

    /**
     * 법인구분
     * Y: 상장, K: 코넥스, N: 비상장, E: 기타
     */
    @JsonProperty("corpCls")
    private String corpCls;

    /**
     * 법인구분 한글명
     */
    @JsonProperty("corpClsName")
    private String corpClsName;

    /**
     * 법인등록번호
     */
    @JsonProperty("jurirNo")
    private String jurirNo;

    /**
     * 사업자등록번호
     */
    @JsonProperty("bizrNo")
    private String bizrNo;

    /**
     * 주소
     */
    @JsonProperty("adres")
    private String adres;

    /**
     * 홈페이지
     */
    @JsonProperty("hmUrl")
    private String hmUrl;

    /**
     * IR홈페이지
     */
    @JsonProperty("irUrl")
    private String irUrl;

    /**
     * 전화번호
     */
    @JsonProperty("phnNo")
    private String phnNo;

    /**
     * 팩스번호
     */
    @JsonProperty("faxNo")
    private String faxNo;

    /**
     * 업종코드
     */
    @JsonProperty("indutyCode")
    private String indutyCode;

    /**
     * 설립일 (YYYYMMDD)
     */
    @JsonProperty("estDt")
    private String estDt;

    /**
     * 설립일 포맷팅 (YYYY-MM-DD)
     */
    @JsonProperty("estDtFormatted")
    private String estDtFormatted;

    /**
     * 결산월 (MM)
     */
    @JsonProperty("accMt")
    private String accMt;

    /**
     * 생성 시간
     */
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    /**
     * 수정 시간
     */
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    /**
     * Company 엔티티를 CompanyDTO로 변환
     *
     * @param company Company 엔티티
     * @return CompanyDTO
     */
    public static CompanyDTO fromEntity(Company company) {
        if (company == null) {
            return null;
        }

        return CompanyDTO.builder()
                .corpCode(company.getCorpCode())
                .corpName(company.getCorpName())
                .corpNameEng(company.getCorpNameEng())
                .stockName(company.getStockName())
                .stockCode(company.getStockCode())
                .ceoNm(company.getCeoNm())
                .corpCls(company.getCorpCls())
                .corpClsName(getCorpClsName(company.getCorpCls()))
                .jurirNo(company.getJurirNo())
                .bizrNo(company.getBizrNo())
                .adres(company.getAdres())
                .hmUrl(company.getHmUrl())
                .irUrl(company.getIrUrl())
                .phnNo(company.getPhnNo())
                .faxNo(company.getFaxNo())
                .indutyCode(company.getIndutyCode())
                .estDt(company.getEstDt())
                .estDtFormatted(formatEstDt(company.getEstDt()))
                .accMt(company.getAccMt())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    /**
     * 법인구분 코드를 한글명으로 변환
     *
     * @param corpCls 법인구분 코드
     * @return 한글명
     */
    private static String getCorpClsName(String corpCls) {
        if (corpCls == null) {
            return null;
        }

        switch (corpCls) {
            case "Y":
                return "유가증권시장";
            case "K":
                return "코스닥";
            case "N":
                return "코넥스";
            case "E":
                return "기타법인";
            default:
                return corpCls;
        }
    }

    /**
     * 설립일을 YYYY-MM-DD 형식으로 포맷팅
     *
     * @param estDt 설립일 (YYYYMMDD)
     * @return 포맷팅된 설립일 (YYYY-MM-DD)
     */
    private static String formatEstDt(String estDt) {
        if (estDt == null || estDt.length() != 8) {
            return estDt;
        }

        try {
            return estDt.substring(0, 4) + "-" + estDt.substring(4, 6) + "-" + estDt.substring(6, 8);
        } catch (Exception e) {
            return estDt;
        }
    }
}
