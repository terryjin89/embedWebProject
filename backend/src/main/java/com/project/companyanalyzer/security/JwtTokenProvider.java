package com.project.companyanalyzer.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증을 담당하는 유틸리티 클래스
 *
 * 주요 기능:
 * - JWT 토큰 생성 (createToken)
 * - JWT 토큰 검증 (validateToken)
 * - 토큰에서 사용자 정보 추출 (getUserCode)
 *
 * 토큰 만료 시간: 2시간 (application.yml에서 설정)
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long tokenValidityInMilliseconds;

    /**
     * JwtTokenProvider 생성자
     *
     * @param secret JWT 서명에 사용할 비밀 키 (application.yml의 jwt.secret)
     * @param tokenValidity 토큰 유효 시간 (밀리초 단위, application.yml의 jwt.expiration)
     */
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long tokenValidity) {

        // JJWT 0.12.x 권장사항: SecretKey 객체 사용
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.tokenValidityInMilliseconds = tokenValidity;

        log.info("JwtTokenProvider initialized with token validity: {} ms ({} hours)",
                tokenValidity, tokenValidity / 1000 / 60 / 60);
    }

    /**
     * JWT 토큰 생성
     *
     * 토큰 구조:
     * - Subject: 사용자 코드 (userCode)
     * - IssuedAt: 토큰 발급 시간
     * - Expiration: 토큰 만료 시간 (발급 시간 + tokenValidityInMilliseconds)
     * - SignWith: HMAC-SHA 알고리즘을 사용한 서명
     *
     * @param userCode 사용자 고유 식별자 (Member 엔티티의 PK)
     * @return 생성된 JWT 토큰 문자열
     */
    public String createToken(String userCode) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

        String token = Jwts.builder()
                .subject(userCode)                    // 사용자 코드를 subject로 설정
                .issuedAt(now)                         // 토큰 발급 시간
                .expiration(validity)                  // 토큰 만료 시간
                .signWith(secretKey)                   // SecretKey로 서명
                .compact();

        log.debug("JWT token created for userCode: {}, expires at: {}", userCode, validity);
        return token;
    }

    /**
     * JWT 토큰 검증
     *
     * 검증 항목:
     * - 토큰 서명 유효성 (SignatureException)
     * - 토큰 만료 여부 (ExpiredJwtException)
     * - 토큰 형식 유효성 (MalformedJwtException)
     * - 지원되는 JWT인지 확인 (UnsupportedJwtException)
     * - Claims가 비어있지 않은지 확인 (IllegalArgumentException)
     *
     * @param token 검증할 JWT 토큰 문자열
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)              // SecretKey로 서명 검증
                    .build()
                    .parseSignedClaims(token);          // Claims 파싱 및 검증

            log.debug("JWT token validation successful");
            return true;

        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token format: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    /**
     * JWT 토큰에서 사용자 코드 추출
     *
     * validateToken()이 이미 호출되어 토큰이 검증된 상태에서 사용하는 것을 권장
     *
     * @param token JWT 토큰 문자열
     * @return 토큰의 subject에 저장된 사용자 코드 (userCode)
     * @throws ExpiredJwtException 토큰이 만료된 경우
     * @throws MalformedJwtException 토큰 형식이 잘못된 경우
     */
    public String getUserCode(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String userCode = claims.getSubject();
        log.debug("Extracted userCode from JWT: {}", userCode);

        return userCode;
    }

    /**
     * JWT 토큰의 만료 시간 조회
     *
     * @param token JWT 토큰 문자열
     * @return 토큰 만료 시간 (Date 객체)
     */
    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getExpiration();
    }

    /**
     * JWT 토큰의 남은 유효 시간 조회 (밀리초)
     *
     * @param token JWT 토큰 문자열
     * @return 토큰 만료까지 남은 시간 (밀리초), 이미 만료된 경우 0
     */
    public long getRemainingValidity(String token) {
        Date expiration = getExpirationDate(token);
        long now = System.currentTimeMillis();
        long expirationTime = expiration.getTime();

        return Math.max(0, expirationTime - now);
    }
}
