package com.project.companyanalyzer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 설정 클래스
 *
 * RestTemplate 빈을 등록하여 외부 API 호출에 사용합니다.
 * DartApiService 등에서 사용됩니다.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * RestTemplate 빈 등록
     *
     * @return RestTemplate 인스턴스
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
