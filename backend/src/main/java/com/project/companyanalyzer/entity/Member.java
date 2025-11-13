package com.project.companyanalyzer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Member 엔티티
 *
 * 사용자 정보를 저장하는 테이블
 *
 * 컬럼:
 * - userCode: 사용자 고유 식별자 (PK, UUID)
 * - email: 이메일 (unique)
 * - password: 비밀번호 (BCrypt 암호화)
 * - name: 사용자 이름
 * - createdAt: 생성 시간
 * - updatedAt: 수정 시간
 */
@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    /**
     * 사용자 코드 (Primary Key)
     *
     * UUID 형식의 고유 식별자
     * JWT 토큰의 subject로 사용됩니다.
     */
    @Id
    @Column(name = "user_code", length = 50, nullable = false, unique = true)
    private String userCode;

    /**
     * 이메일 주소
     *
     * 로그인 ID로 사용됩니다.
     * Unique 제약 조건이 있습니다.
     */
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    /**
     * 비밀번호
     *
     * BCrypt 해시 알고리즘으로 암호화되어 저장됩니다.
     * 평문 비밀번호는 저장하지 않습니다.
     */
    @Column(name = "password", length = 255, nullable = false)
    private String password;

    /**
     * 사용자 이름
     */
    @Column(name = "name", length = 50, nullable = false)
    private String name;

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
     * userCode 자동 생성
     *
     * UUID를 사용하여 고유한 사용자 코드를 생성합니다.
     * 엔티티가 persist되기 전에 자동으로 호출됩니다.
     */
    @PrePersist
    public void generateUserCode() {
        if (this.userCode == null || this.userCode.isEmpty()) {
            this.userCode = java.util.UUID.randomUUID().toString();
        }
    }
}
