package com.project.companyanalyzer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Memo 엔티티
 *
 * 사용자가 관심기업에 대해 작성한 메모 정보를 저장하는 테이블
 *
 * 컬럼:
 * - id: 고유 식별자 (PK, Auto Increment)
 * - userCode: 사용자 코드 (FK - Member)
 * - stockCode: 주식 종목코드 (6자리)
 * - content: 메모 내용 (최대 2000자)
 * - createdAt: 생성 시간
 * - updatedAt: 수정 시간
 *
 * 제약조건:
 * - UNIQUE KEY (userCode, stockCode): 사용자당 동일 종목의 메모 하나만 존재
 * - Foreign Key: userCode -> Member.userCode
 */
@Entity
@Table(
    name = "memo",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_stock_memo",
            columnNames = {"user_code", "stock_code"}
        )
    },
    indexes = {
        @Index(name = "idx_memo_user_code", columnList = "user_code"),
        @Index(name = "idx_memo_stock_code", columnList = "stock_code")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Memo {

    /**
     * 메모 ID (Primary Key)
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
     * 메모를 작성한 사용자를 식별합니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_code",
        referencedColumnName = "user_code",
        foreignKey = @ForeignKey(name = "fk_memo_member"),
        nullable = false
    )
    private Member member;

    /**
     * 주식 종목코드
     *
     * 6자리 주식 종목코드
     * 메모가 작성된 기업의 종목코드입니다.
     */
    @Column(name = "stock_code", length = 6, nullable = false)
    private String stockCode;

    /**
     * 메모 내용
     *
     * 사용자가 작성한 메모 텍스트
     * 최대 2000자까지 저장 가능합니다.
     */
    @Column(name = "content", length = 2000, nullable = true)
    private String content;

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
     * 편의 메서드: userCode 반환
     *
     * Member 엔티티의 userCode를 반환합니다.
     */
    public String getUserCode() {
        return member != null ? member.getUserCode() : null;
    }
}
