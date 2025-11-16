package com.project.companyanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * MemoResponse DTO
 *
 * 메모 조회/저장 성공 시 반환되는 응답 DTO
 *
 * 프론트엔드 요구사항:
 * - content: 메모 내용
 * - updatedAt: 마지막 수정일시
 * - message: 성공/실패 메시지 (저장 시)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoResponse {

    /**
     * 메모 내용
     *
     * 저장된 메모의 내용
     * null 또는 빈 문자열일 수 있습니다.
     */
    private String content;

    /**
     * 마지막 수정일시
     *
     * ISO 8601 형식으로 반환됩니다.
     * 예: "2024-11-16T15:30:00"
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * 응답 메시지
     *
     * 저장 성공 시 메시지 (선택적)
     * 예: "메모가 성공적으로 저장되었습니다"
     */
    private String message;
}
