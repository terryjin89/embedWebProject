package com.project.companyanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 기업 목록 페이지네이션 응답 DTO
 *
 * 프론트엔드에 기업 목록과 페이지 정보를 함께 전달
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyListResponse {

    /**
     * 기업 목록
     */
    @JsonProperty("companies")
    private List<CompanyDTO> companies;

    /**
     * 현재 페이지 번호 (0부터 시작)
     */
    @JsonProperty("currentPage")
    private int currentPage;

    /**
     * 페이지당 항목 수
     */
    @JsonProperty("pageSize")
    private int pageSize;

    /**
     * 전체 항목 수
     */
    @JsonProperty("totalElements")
    private long totalElements;

    /**
     * 전체 페이지 수
     */
    @JsonProperty("totalPages")
    private int totalPages;

    /**
     * 첫 페이지 여부
     */
    @JsonProperty("isFirst")
    private boolean isFirst;

    /**
     * 마지막 페이지 여부
     */
    @JsonProperty("isLast")
    private boolean isLast;

    /**
     * 다음 페이지 존재 여부
     */
    @JsonProperty("hasNext")
    private boolean hasNext;

    /**
     * 이전 페이지 존재 여부
     */
    @JsonProperty("hasPrevious")
    private boolean hasPrevious;

    /**
     * Spring Data Page 객체를 CompanyListResponse로 변환
     *
     * @param page Company Page 객체
     * @return CompanyListResponse
     */
    public static CompanyListResponse fromPage(Page<CompanyDTO> page) {
        return CompanyListResponse.builder()
                .companies(page.getContent())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
