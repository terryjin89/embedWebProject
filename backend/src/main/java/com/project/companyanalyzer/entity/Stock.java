package com.project.companyanalyzer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Stock 엔티티
 *
 * 사용자의 관심기업 정보를 저장하는 테이블
 *
 * 컬럼:
 * - id: 고유 식별자 (PK, Auto Increment)
 * - userCode: 사용자 코드 (FK - Member)
 * - stockCode: 주식 종목코드 (6자리)
 * - corpCode: 기업 코드 (FK - Company)
 * - registeredAt: 관심기업 등록일시
 * - createdAt: 생성 시간
 * - updatedAt: 수정 시간
 *
 * 제약조건:
 * - UNIQUE KEY (userCode, stockCode): 사용자당 동일 종목 중복 등록 방지
 * - Foreign Key: userCode -> Member.userCode
 * - Foreign Key: corpCode -> Company.corpCode
 */
@Entity
@Table(
    name = "stock",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_stock",
            columnNames = {"user_code", "stock_code"}
        )
    },
    indexes = {
        @Index(name = "idx_user_code", columnList = "user_code"),
        @Index(name = "idx_stock_code", columnList = "stock_code"),
        @Index(name = "idx_corp_code", columnList = "corp_code")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    /**
     * 관심기업 ID (Primary Key)
     *
     * Auto Increment로 자동 생성되는 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 사용자 코드
     *
     * Member 엔티티를 참조하는 외래키
     * 관심기업을 등록한 사용자를 식별합니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_code",
        referencedColumnName = "user_code",
        foreignKey = @ForeignKey(name = "fk_stock_member"),
        nullable = false
    )
    private Member member;

    /**
     * 주식 종목코드
     *
     * 6자리 주식 종목코드
     * Company 엔티티의 stockCode 컬럼 값과 연결됩니다.
     */
    @Column(name = "stock_code", length = 6, nullable = false)
    private String stockCode;

    /**
     * 기업 코드
     *
     * Company 엔티티를 참조하는 외래키
     * DART API의 8자리 고유번호입니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "corp_code",
        referencedColumnName = "corp_code",
        foreignKey = @ForeignKey(name = "fk_stock_company"),
        nullable = false
    )
    private Company company;

    /**
     * 관심기업 등록일시
     *
     * 사용자가 해당 기업을 관심기업으로 등록한 일시
     */
    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

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

    /**
     * registeredAt 자동 설정
     *
     * 엔티티가 persist되기 전에 자동으로 호출됩니다.
     * registeredAt이 설정되지 않은 경우 현재 시간으로 설정합니다.
     */
    @PrePersist
    public void setDefaultRegisteredAt() {
        if (this.registeredAt == null) {
            this.registeredAt = LocalDateTime.now();
        }
    }

    /**
     * 편의 메서드: userCode 반환
     *
     * Member 엔티티의 userCode를 반환합니다.
     */
    public String getUserCode() {
        return member != null ? member.getUserCode() : null;
    }

    /**
     * 편의 메서드: corpCode 반환
     *
     * Company 엔티티의 corpCode를 반환합니다.
     */
    public String getCorpCode() {
        return company != null ? company.getCorpCode() : null;
    }

    /**
     * 편의 메서드: companyName 반환
     *
     * Company 엔티티의 corpName을 반환합니다.
     */
    public String getCompanyName() {
        return company != null ? company.getCorpName() : null;
    }

    /**
     * 편의 메서드: stockName 반환
     *
     * Company 엔티티의 stockName을 반환합니다.
     */
    public String getStockName() {
        return company != null ? company.getStockName() : null;
    }
}
