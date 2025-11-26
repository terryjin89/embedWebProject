package com.project.companyanalyzer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    @Value("${cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Origin
        config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));

        // Methods
        config.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));

        // Headers (공백 제거 + 모두 등록)
        config.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));

        // 중요: 브라우저 기본 헤더 포함
        config.addAllowedHeader("Origin");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("X-Requested-With");

        // 최신 브라우저 자동 헤더도 허용
        config.addAllowedHeader("sec-ch-ua");
        config.addAllowedHeader("sec-ch-ua-mobile");
        config.addAllowedHeader("sec-ch-ua-platform");

        config.setAllowCredentials(allowCredentials);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

