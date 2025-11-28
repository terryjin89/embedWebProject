package com.project.companyanalyzer.config;

import com.project.companyanalyzer.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Spring Security 설정 클래스
 *
 * JWT 기반 인증을 사용하는 Stateless 보안 설정:
 * - CSRF 비활성화 (JWT 사용으로 불필요)
 * - CORS 설정 적용
 * - Stateless 세션 정책
 * - JWT 인증 필터 체인 연결
 * - 경로별 인증/비인증 접근 제어
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Spring Security FilterChain 설정
     *
     * 보안 필터 체인 순서:
     * 1. CORS 필터
     * 2. JwtAuthenticationFilter (커스텀)
     * 3. UsernamePasswordAuthenticationFilter (기본)
     * 4. 기타 Spring Security 필터들
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화: JWT 사용으로 CSRF 토큰 불필요
                .csrf(AbstractHttpConfigurer::disable)

                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Stateless 세션 정책: 세션 사용 안 함 (JWT 기반 인증)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 경로별 인증/비인증 접근 제어
                .authorizeHttpRequests(auth -> auth
                        // 공개 엔드포인트: 인증 없이 접근 가능
                        .requestMatchers(
                                "/",                    // 루트 경로
                                "/api-docs/**",         // API 문서
                                "/swagger-ui/**",       // Swagger UI
                                "/swagger-ui.html",     // Swagger UI HTML
                                "/v3/api-docs/**",      // OpenAPI 3.0 문서
                                "/api/auth/**",         // 인증 관련 API (로그인, 회원가입)
                                "/api/companies/**",    // 기업 정보 API (공개) - SCRUM-8
                                "/companies/**",        // 기업 정보 API (공개) - SCRUM-8
                                "/stocks/**",           // 주가 API (공개)
                                "/api/exchange-rates/**", // 환율 API (공개)
                                "/api/exchange-rates", // 환율 API (공개)
                                "/api/news/**",         // 뉴스 검색 API (공개) - SCRUM-12
                                "/health",              // 헬스 체크
                                "/actuator/**"          // Actuator 엔드포인트 (모니터링)
                        ).permitAll()

                        // 그 외 모든 요청: 인증 필요
                        .anyRequest().authenticated()
                )

                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    /**
     * BCrypt 비밀번호 인코더 빈 등록
     *
     * 비밀번호를 BCrypt 해시 알고리즘으로 암호화합니다.
     * 회원가입 시 비밀번호 저장 및 로그인 시 비밀번호 검증에 사용됩니다.
     *
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
