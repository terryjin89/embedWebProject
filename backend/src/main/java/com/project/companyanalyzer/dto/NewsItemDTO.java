package com.project.companyanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 뉴스 아이템 DTO
 * Naver 검색 API 응답의 개별 뉴스 아이템
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsItemDTO {

    /**
     * 뉴스 기사의 제목 (HTML 태그 포함)
     */
    private String title;

    /**
     * 뉴스 기사의 네이버 뉴스 URL
     */
    private String link;

    /**
     * 뉴스 기사의 원본 URL
     */
    @JsonProperty("originallink")
    private String originallink;

    /**
     * 뉴스 기사의 내용 요약 (HTML 태그 포함)
     */
    private String description;

    /**
     * 뉴스 기사가 네이버에 제공된 시간 (RFC 2822 형식)
     * 예: Thu, 15 Nov 2025 10:30:00 +0900
     */
    @JsonProperty("pubDate")
    private String pubDate;
}
