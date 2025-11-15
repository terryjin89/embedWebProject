package com.project.companyanalyzer.dto;

import lombok.*;

/**
 * DeleteFavoriteResponse DTO
 *
 * 관심기업 삭제 응답 DTO
 *
 * 프론트엔드 요구사항:
 * - success: 성공 여부
 * - message: 응답 메시지
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteFavoriteResponse {

    /**
     * 성공 여부
     */
    private boolean success;

    /**
     * 응답 메시지
     */
    private String message;

    /**
     * 성공 응답 생성
     */
    public static DeleteFavoriteResponse success(String message) {
        return DeleteFavoriteResponse.builder()
            .success(true)
            .message(message)
            .build();
    }

    /**
     * 실패 응답 생성
     */
    public static DeleteFavoriteResponse failure(String message) {
        return DeleteFavoriteResponse.builder()
            .success(false)
            .message(message)
            .build();
    }
}
