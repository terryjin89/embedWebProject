package com.project.companyanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 뉴스 검색 응답 DTO
 * 프론트엔드로 전달되는 응답 형식
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsSearchResponse {

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
     * 한 번에 표시할 검색 결과 개수
     */
    private Integer display;

    /**
     * 검색된 뉴스 기사 목록
     */
    private List<NewsItemDTO> items;
}
