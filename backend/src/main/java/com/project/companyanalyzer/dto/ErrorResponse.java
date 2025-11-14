package com.project.companyanalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 에러 응답 DTO
 *
 * API 에러 발생 시 클라이언트에게 전달할 표준 에러 응답 포맷
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * 에러 발생 시각
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * HTTP 상태 코드
     */
    private int status;

    /**
     * 에러 코드 (예: BAD_REQUEST, NOT_FOUND)
     */
    private String error;

    /**
     * 에러 메시지
     */
    private String message;

    /**
     * 요청 경로
     */
    private String path;

    /**
     * 에러 응답 생성 (간소화 버전)
     */
    public static ErrorResponse of(int status, String error, String message, String path) {
        return ErrorResponse.builder()
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }
}
