package com.project.companyanalyzer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Company 엔티티
 *
 * DART API에서 조회한 기업 정보를 저장하는 테이블
 *
 * 컬럼:
 * - corpCode: 고유번호 (PK, DART API에서 제공)
 * - corpName: 정식명칭
 * - corpNameEng: 영문명칭
 * - stockName: 종목명 또는 약식명칭
 * - stockCode: 주식 종목코드 (상장사만 해당)
 * - ceoNm: 대표자명
 * - corpCls: 법인구분 (Y=상장, E=기타)
 * - jurirNo: 법인등록번호
 * - bizrNo: 사업자등록번호
 * - adres: 주소
 * - hmUrl: 홈페이지
 * - irUrl: IR홈페이지
 * - phnNo: 전화번호
 * - faxNo: 팩스번호
 * - indutyCode: 업종코드
 * - estDt: 설립일 (YYYYMMDD)
 * - accMt: 결산월 (MM)
 * - createdAt: 생성 시간
 * - updatedAt: 수정 시간
 */
@Entity
@Table(
    name = "company",
    indexes = {
        @Index(name = "idx_corp_name", columnList = "corp_name"),
        @Index(name = "idx_stock_code", columnList = "stock_code"),
        @Index(name = "idx_induty_code", columnList = "induty_code")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    /**
     * 고유번호 (Primary Key)
     *
     * DART API에서 제공하는 8자리 고유번호
     * 기업을 식별하는 유일한 키입니다.
     */
    @Id
    @Column(name = "corp_code", length = 8, nullable = false)
    private String corpCode;

    /**
     * 정식명칭
     *
     * 기업의 공식 법인명
     */
    @Column(name = "corp_name", length = 200, nullable = false)
    private String corpName;

    /**
     * 영문명칭
     *
     * 기업의 영문 법인명
     */
    @Column(name = "corp_name_eng", length = 200)
    private String corpNameEng;

    /**
     * 종목명 또는 약식명칭
     *
     * 상장사의 경우 주식 종목명
     * 기타법인의 경우 약식명칭
     */
    @Column(name = "stock_name", length = 100)
    private String stockName;

    /**
     * 주식 종목코드
     *
     * 상장사만 해당하며, 6자리 종목코드
     * 비상장 기업의 경우 빈 문자열 또는 null
     */
    @Column(name = "stock_code", length = 6)
    private String stockCode;

    /**
     * 대표자명
     *
     * 기업의 대표이사 또는 각자대표이사
     */
    @Column(name = "ceo_nm", length = 100)
    private String ceoNm;

    /**
     * 법인구분
     *
     * Y: 유가증권시장, 코스닥, 코넥스 상장회사
     * K: 콘텍스 상장회사
     * N: 비상장회사
     * E: 기타법인
     */
    @Column(name = "corp_cls", length = 1)
    private String corpCls;

    /**
     * 법인등록번호
     *
     * 13자리 법인등록번호
     */
    @Column(name = "jurir_no", length = 13)
    private String jurirNo;

    /**
     * 사업자등록번호
     *
     * 10자리 사업자등록번호
     */
    @Column(name = "bizr_no", length = 10)
    private String bizrNo;

    /**
     * 주소
     *
     * 기업의 본사 주소
     */
    @Column(name = "adres", length = 500)
    private String adres;

    /**
     * 홈페이지
     *
     * 기업 홈페이지 URL
     */
    @Column(name = "hm_url", length = 200)
    private String hmUrl;

    /**
     * IR홈페이지
     *
     * 투자자 관계(IR) 홈페이지 URL
     */
    @Column(name = "ir_url", length = 200)
    private String irUrl;

    /**
     * 전화번호
     *
     * 기업 대표 전화번호
     */
    @Column(name = "phn_no", length = 20)
    private String phnNo;

    /**
     * 팩스번호
     *
     * 기업 팩스번호
     */
    @Column(name = "fax_no", length = 20)
    private String faxNo;

    /**
     * 업종코드
     *
     * 한국표준산업분류(KSIC) 코드
     */
    @Column(name = "induty_code", length = 10)
    private String indutyCode;

    /**
     * 설립일
     *
     * YYYYMMDD 형식의 설립일자
     */
    @Column(name = "est_dt", length = 8)
    private String estDt;

    /**
     * 결산월
     *
     * MM 형식의 결산월 (01~12)
     */
    @Column(name = "acc_mt", length = 2)
    private String accMt;

    /**
     * 생성 시간
     *
     * 레코드 생성 시 자동으로 설정됩니다.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 수정 시간
     *
     * 레코드 수정 시 자동으로 업데이트됩니다.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
