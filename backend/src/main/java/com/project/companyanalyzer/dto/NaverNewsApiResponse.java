package com.project.companyanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Naver 뉴스 검색 API 응답 DTO
 * API 원본 응답 구조를 그대로 매핑
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaverNewsApiResponse {

    /**
     * 검색 결과를 생성한 시간
     */
    @JsonProperty("lastBuildDate")
    private String lastBuildDate;

    /**
     * 총 검색 결과 개수
     */
    private Integer total;

    /**
     * 검색 시작 위치 (1부터 시작)
     */
    private Integer start;

    /**
     * 한 번에 표시할 검색 결과 개수 (기본값: 10, 최대: 100)
     */
    private Integer display;

    /**
     * 검색된 뉴스 기사 목록
     */
    private List<NewsItemDTO> items;
}
