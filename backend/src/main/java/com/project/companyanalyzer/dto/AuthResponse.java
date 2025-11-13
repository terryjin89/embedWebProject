package com.project.companyanalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 인증 응답 DTO
 *
 * 로그인/회원가입 성공 시 JWT 토큰과 사용자 정보를 반환합니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    /**
     * JWT 액세스 토큰
     *
     * Authorization 헤더에 "Bearer {token}" 형식으로 사용합니다.
     */
    private String token;

    /**
     * 토큰 타입
     *
     * 항상 "Bearer"입니다.
     */
    private String tokenType;

    /**
     * 사용자 코드 (UUID)
     */
    private String userCode;

    /**
     * 이메일 주소
     */
    private String email;

    /**
     * 사용자 이름
     */
    private String name;

    /**
     * 편의 메서드: Bearer 토큰 타입으로 AuthResponse 생성
     */
    public static AuthResponse of(String token, String userCode, String email, String name) {
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userCode(userCode)
                .email(email)
                .name(name)
                .build();
    }
}
