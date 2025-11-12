package com.project.companyanalyzer.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * JwtTokenProvider 단위 테스트
 *
 * 테스트 항목:
 * 1. 토큰 생성 (createToken)
 * 2. 토큰 검증 (validateToken) - 유효한 토큰, 만료된 토큰, 잘못된 형식
 * 3. 사용자 코드 추출 (getUserCode)
 * 4. 토큰 만료 시간 검증
 * 5. 남은 유효 시간 계산
 */
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    private static final String TEST_SECRET = "test-secret-key-for-jwt-token-generation-must-be-at-least-256-bits-long";
    private static final long TEST_VALIDITY = 7200000; // 2 hours in milliseconds
    private static final String TEST_USER_CODE = "user123";

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(TEST_SECRET, TEST_VALIDITY);
    }

    @Test
    @DisplayName("JWT 토큰 생성 성공")
    void createToken_Success() {
        // When
        String token = jwtTokenProvider.createToken(TEST_USER_CODE);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT는 3개 부분으로 구성 (header.payload.signature)
    }

    @Test
    @DisplayName("유효한 JWT 토큰 검증 성공")
    void validateToken_ValidToken_ReturnsTrue() {
        // Given
        String token = jwtTokenProvider.createToken(TEST_USER_CODE);

        // When
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("잘못된 형식의 JWT 토큰 검증 실패")
    void validateToken_MalformedToken_ReturnsFalse() {
        // Given
        String malformedToken = "invalid.jwt.token";

        // When
        boolean isValid = jwtTokenProvider.validateToken(malformedToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("빈 JWT 토큰 검증 실패")
    void validateToken_EmptyToken_ReturnsFalse() {
        // When
        boolean isValid = jwtTokenProvider.validateToken("");

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("만료된 JWT 토큰 검증 실패")
    void validateToken_ExpiredToken_ReturnsFalse() {
        // Given: 만료 시간을 1밀리초로 설정하여 즉시 만료되는 토큰 생성
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider(TEST_SECRET, 1);
        String token = expiredTokenProvider.createToken(TEST_USER_CODE);

        // 토큰이 만료되도록 대기
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // When
        boolean isValid = expiredTokenProvider.validateToken(token);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("JWT 토큰에서 사용자 코드 추출 성공")
    void getUserCode_ValidToken_ReturnsUserCode() {
        // Given
        String token = jwtTokenProvider.createToken(TEST_USER_CODE);

        // When
        String extractedUserCode = jwtTokenProvider.getUserCode(token);

        // Then
        assertThat(extractedUserCode).isEqualTo(TEST_USER_CODE);
    }

    @Test
    @DisplayName("잘못된 JWT 토큰에서 사용자 코드 추출 실패")
    void getUserCode_MalformedToken_ThrowsException() {
        // Given
        String malformedToken = "invalid.jwt.token";

        // When & Then
        assertThatThrownBy(() -> jwtTokenProvider.getUserCode(malformedToken))
                .isInstanceOf(MalformedJwtException.class);
    }

    @Test
    @DisplayName("만료된 JWT 토큰에서 사용자 코드 추출 실패")
    void getUserCode_ExpiredToken_ThrowsException() {
        // Given: 만료 시간을 1밀리초로 설정
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider(TEST_SECRET, 1);
        String token = expiredTokenProvider.createToken(TEST_USER_CODE);

        // 토큰이 만료되도록 대기
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // When & Then
        assertThatThrownBy(() -> expiredTokenProvider.getUserCode(token))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("JWT 토큰 만료 시간 조회 성공")
    void getExpirationDate_ValidToken_ReturnsCorrectDate() {
        // Given
        String token = jwtTokenProvider.createToken(TEST_USER_CODE);
        long creationTime = System.currentTimeMillis();

        // When
        Date expirationDate = jwtTokenProvider.getExpirationDate(token);

        // Then
        // 토큰 만료 시간은 현재 시간 + TEST_VALIDITY와 거의 같아야 함 (±1초 오차 허용)
        long expectedExpiration = creationTime + TEST_VALIDITY;
        long actualExpiration = expirationDate.getTime();
        long timeDifference = Math.abs(actualExpiration - expectedExpiration);

        assertThat(timeDifference).isLessThan(1000); // 1초 이내 오차 허용
    }

    @Test
    @DisplayName("JWT 토큰의 남은 유효 시간 계산 성공")
    void getRemainingValidity_ValidToken_ReturnsPositiveValue() {
        // Given
        String token = jwtTokenProvider.createToken(TEST_USER_CODE);

        // When
        long remainingValidity = jwtTokenProvider.getRemainingValidity(token);

        // Then
        assertThat(remainingValidity).isPositive();
        assertThat(remainingValidity).isLessThanOrEqualTo(TEST_VALIDITY);
    }

    @Test
    @DisplayName("만료된 JWT 토큰의 남은 유효 시간은 0")
    void getRemainingValidity_ExpiredToken_ReturnsZero() {
        // Given: 만료 시간을 1밀리초로 설정
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider(TEST_SECRET, 1);
        String token = expiredTokenProvider.createToken(TEST_USER_CODE);

        // 토큰이 만료되도록 대기
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // When & Then: 만료된 토큰에서 getExpirationDate()를 호출하면 ExpiredJwtException 발생
        // getRemainingValidity()는 내부적으로 getExpirationDate()를 호출하므로 예외 발생
        assertThatThrownBy(() -> expiredTokenProvider.getRemainingValidity(token))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("서로 다른 사용자 코드로 생성된 토큰은 다른 사용자 코드를 반환")
    void createToken_DifferentUserCodes_ReturnDifferentTokens() {
        // Given
        String userCode1 = "user001";
        String userCode2 = "user002";

        // When
        String token1 = jwtTokenProvider.createToken(userCode1);
        String token2 = jwtTokenProvider.createToken(userCode2);

        // Then
        assertThat(token1).isNotEqualTo(token2);
        assertThat(jwtTokenProvider.getUserCode(token1)).isEqualTo(userCode1);
        assertThat(jwtTokenProvider.getUserCode(token2)).isEqualTo(userCode2);
    }

    @Test
    @DisplayName("잘못된 비밀키로 생성된 토큰은 검증 실패")
    void validateToken_WrongSecretKey_ReturnsFalse() {
        // Given: 다른 비밀키로 JwtTokenProvider 생성
        String wrongSecret = "wrong-secret-key-for-jwt-token-generation-must-be-at-least-256-bits-long";
        JwtTokenProvider wrongProvider = new JwtTokenProvider(wrongSecret, TEST_VALIDITY);
        String token = wrongProvider.createToken(TEST_USER_CODE);

        // When: 원래의 jwtTokenProvider로 검증
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Then
        assertThat(isValid).isFalse();
    }
}
