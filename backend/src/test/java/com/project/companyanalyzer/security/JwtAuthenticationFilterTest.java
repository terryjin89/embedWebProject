package com.project.companyanalyzer.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * JwtAuthenticationFilter 단위 테스트
 *
 * 테스트 시나리오:
 * 1. 유효한 JWT 토큰으로 인증 성공
 * 2. 유효하지 않은 JWT 토큰으로 인증 실패
 * 3. Authorization 헤더가 없는 경우
 * 4. Bearer 형식이 아닌 토큰
 */
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String TEST_USER_CODE = "user123";
    private static final String VALID_TOKEN = "valid.jwt.token";
    private static final String BEARER_TOKEN = "Bearer " + VALID_TOKEN;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("유효한 JWT 토큰으로 인증 성공")
    void doFilterInternal_ValidToken_SetsAuthentication() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn(BEARER_TOKEN);
        when(jwtTokenProvider.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtTokenProvider.getUserCode(VALID_TOKEN)).thenReturn(TEST_USER_CODE);

        UserDetails userDetails = User.builder()
                .username(TEST_USER_CODE)
                .password("")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
        when(userDetailsService.loadUserByUsername(TEST_USER_CODE)).thenReturn(userDetails);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName())
                .isEqualTo(TEST_USER_CODE);
        assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
                .isTrue();

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("유효하지 않은 JWT 토큰으로 인증 실패")
    void doFilterInternal_InvalidToken_DoesNotSetAuthentication() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn(BEARER_TOKEN);
        when(jwtTokenProvider.validateToken(VALID_TOKEN)).thenReturn(false);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Authorization 헤더가 없는 경우 인증 생략")
    void doFilterInternal_NoAuthorizationHeader_DoesNotSetAuthentication() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(jwtTokenProvider, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Bearer 형식이 아닌 토큰으로 인증 실패")
    void doFilterInternal_NonBearerToken_DoesNotSetAuthentication() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Basic some_token");

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(jwtTokenProvider, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("빈 Authorization 헤더로 인증 실패")
    void doFilterInternal_EmptyAuthorizationHeader_DoesNotSetAuthentication() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn("");

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(jwtTokenProvider, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("JWT 검증 중 예외 발생 시 SecurityContext 비우기")
    void doFilterInternal_ExceptionDuringValidation_ClearsSecurityContext() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn(BEARER_TOKEN);
        when(jwtTokenProvider.validateToken(VALID_TOKEN)).thenThrow(new RuntimeException("Token validation error"));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
