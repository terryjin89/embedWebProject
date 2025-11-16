package com.project.companyanalyzer.service;

import com.project.companyanalyzer.dto.NaverNewsApiResponse;
import com.project.companyanalyzer.dto.NewsSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Naver 검색 API 연동 서비스
 *
 * Naver Search API를 통해 뉴스 검색 기능을 제공하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final RestTemplate restTemplate;

    @Value("${naver.api.url}")
    private String apiUrl;

    @Value("${naver.api.clientId}")
    private String clientId;

    @Value("${naver.api.clientSecret}")
    private String clientSecret;

    /**
     * 뉴스 검색
     *
     * @param query 검색어 (기업명 + 해시태그)
     * @param display 한 번에 표시할 검색 결과 개수 (기본값: 10, 최대: 100)
     * @param start 검색 시작 위치 (기본값: 1, 최대: 1000)
     * @param sort 정렬 옵션 (sim: 관련도순, date: 최신순, 기본값: date)
     * @return 뉴스 검색 결과
     */
    public NewsSearchResponse searchNews(String query, Integer display, Integer start, String sort) {
        log.info("[Naver] 뉴스 검색 - query: {}, display: {}, start: {}, sort: {}",
            query, display, start, sort);

        // 검색어 검증
        if (query == null || query.trim().isEmpty()) {
            log.error("[Naver] 검색어가 비어있습니다");
            throw new IllegalArgumentException("검색어를 입력해주세요");
        }

        try {
            // 검색어 URL 인코딩
            String encodedQuery = encodeQuery(query);

            // API URL 생성
            String url = buildSearchApiUrl(encodedQuery, display, start, sort);
            log.debug("[Naver] API 호출 URL: {}", url);

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // API 호출
            ResponseEntity<NaverNewsApiResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                NaverNewsApiResponse.class
            );

            NaverNewsApiResponse naverResponse = response.getBody();

            if (naverResponse != null) {
                log.info("[Naver] 뉴스 검색 성공 - 총 {}건, 표시 {}건",
                    naverResponse.getTotal(), naverResponse.getDisplay());

                // NaverNewsApiResponse를 NewsSearchResponse로 변환
                return NewsSearchResponse.builder()
                    .lastBuildDate(naverResponse.getLastBuildDate())
                    .total(naverResponse.getTotal())
                    .start(naverResponse.getStart())
                    .display(naverResponse.getDisplay())
                    .items(naverResponse.getItems())
                    .build();
            } else {
                log.error("[Naver] API 응답이 null입니다");
                throw new RuntimeException("뉴스 검색 결과가 없습니다");
            }

        } catch (HttpClientErrorException e) {
            log.error("[Naver] 클라이언트 오류 - status: {}, message: {}",
                e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Naver API 클라이언트 오류: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            log.error("[Naver] 서버 오류 - status: {}, message: {}",
                e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Naver API 서버 오류: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("[Naver] 뉴스 검색 중 오류 발생 - query: {}", query, e);
            throw new RuntimeException("뉴스 검색 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 검색어 URL 인코딩
     *
     * @param query 검색어
     * @return URL 인코딩된 검색어
     */
    private String encodeQuery(String query) {
        try {
            return URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("[Naver] 검색어 인코딩 실패 - query: {}", query, e);
            throw new RuntimeException("검색어 인코딩 실패", e);
        }
    }

    /**
     * 뉴스 검색 API URL 생성
     *
     * @param encodedQuery URL 인코딩된 검색어
     * @param display 한 번에 표시할 검색 결과 개수
     * @param start 검색 시작 위치
     * @param sort 정렬 옵션
     * @return API URL
     */
    private String buildSearchApiUrl(String encodedQuery, Integer display, Integer start, String sort) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("query", encodedQuery)
                .queryParam("display", display != null ? display : 10)
                .queryParam("start", start != null ? start : 1);

        // 정렬 옵션이 있으면 추가 (sim: 관련도순, date: 최신순)
        if (sort != null && !sort.isEmpty()) {
            builder.queryParam("sort", sort);
        } else {
            builder.queryParam("sort", "date"); // 기본값: 최신순
        }

        return builder.build(false).toUriString();
    }
}
