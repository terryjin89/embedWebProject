package com.project.companyanalyzer.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터
 *
 * HTTP 요청에서 JWT 토큰을 추출하고 검증하여 Spring Security 인증을 설정합니다.
 *
 * 동작 순서:
 * 1. Authorization 헤더에서 Bearer 토큰 추출
 * 2. JwtTokenProvider를 사용하여 토큰 검증
 * 3. 토큰에서 사용자 코드 추출
 * 4. UserDetailsService를 통해 사용자 정보 로드
 * 5. Spring Security Context에 인증 정보 설정
 *
 * OncePerRequestFilter를 상속하여 요청당 한 번만 실행되도록 보장합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * JWT 인증 필터의 핵심 로직
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 다음 필터 체인
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // 1. Authorization 헤더에서 JWT 토큰 추출
            String jwt = extractJwtFromRequest(request);

            // 2. 토큰이 존재하고 유효한 경우 인증 처리
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {

                // 3. 토큰에서 사용자 코드 추출
                String userCode = jwtTokenProvider.getUserCode(jwt);

                // 4. UserDetailsService를 통해 사용자 정보 로드
                UserDetails userDetails = userDetailsService.loadUserByUsername(userCode);

                // 5. Spring Security 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // 6. 요청 세부 정보 설정 (IP 주소, 세션 ID 등)
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 7. SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("JWT authentication successful for user: {}", userCode);
            } else {
                log.debug("JWT token is empty or invalid, skipping authentication");
            }

        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
            // 인증 실패 시 SecurityContext를 비워서 인증되지 않은 상태로 유지
            SecurityContextHolder.clearContext();
        }

        // 8. 다음 필터 체인 실행
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청에서 JWT 토큰 추출
     *
     * Authorization 헤더에서 "Bearer " 접두사를 제거하고 토큰 문자열을 반환합니다.
     *
     * 예시:
     * - Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
     * - 반환값: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
     *
     * @param request HTTP 요청
     * @return JWT 토큰 문자열, 토큰이 없으면 null
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Bearer 토큰 형식 확인 및 추출
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7); // "Bearer " 제거
            log.debug("JWT token extracted from Authorization header");
            return token;
        }

        log.debug("No JWT token found in Authorization header");
        return null;
    }

    /**
     * 특정 요청에 대해 필터를 건너뛸지 결정
     *
     * 공개 엔드포인트(로그인, 회원가입 등)는 JWT 검증을 건너뛰도록 설정할 수 있습니다.
     * 현재는 모든 요청에 대해 필터를 실행하며, SecurityConfig에서 경로별 권한을 제어합니다.
     *
     * @param request HTTP 요청
     * @return true면 필터 건너뛰기, false면 필터 실행
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Swagger 및 API 문서 경로는 필터 건너뛰기
        return path.startsWith("/api-docs") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.equals("/health");
    }
}
