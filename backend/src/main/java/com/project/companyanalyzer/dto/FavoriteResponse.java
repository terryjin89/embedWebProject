package com.project.companyanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * FavoriteResponse DTO
 *
 * 관심기업 목록 조회 시 반환되는 응답 DTO
 *
 * 프론트엔드 요구사항:
 * - id: 관심기업 ID
 * - stockCode: 종목코드 (6자리)
 * - corpCode: 기업 코드 (8자리)
 * - companyName: 기업명
 * - stockName: 주식명
 * - registeredAt: 등록일시
 *
 * 주가 정보는 프론트엔드가 직접 금융위원회 API를 호출하여 조회합니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteResponse {

    /**
     * 관심기업 ID
     */
    private Long id;

    /**
     * 종목코드 (6자리)
     * 예: "005930" (삼성전자)
     */
    private String stockCode;

    /**
     * 기업 코드 (8자리)
     * DART API의 고유번호
     * 예: "00126380" (삼성전자)
     */
    private String corpCode;

    /**
     * 기업명
     * 예: "삼성전자(주)"
     */
    private String companyName;

    /**
     * 주식명
     * 예: "삼성전자"
     */
    private String stockName;

    /**
     * 관심기업 등록일시
     * ISO 8601 형식으로 반환됩니다.
     * 예: "2024-11-01T10:30:00"
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime registeredAt;
}
