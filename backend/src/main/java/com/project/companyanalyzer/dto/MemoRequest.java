package com.project.companyanalyzer.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * MemoRequest DTO
 *
 * 메모 저장/수정 요청 DTO
 *
 * 프론트엔드 요구사항:
 * - content: 메모 내용 (최대 2000자)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoRequest {

    /**
     * 메모 내용
     *
     * 최대 2000자까지 허용됩니다.
     * null 또는 빈 문자열도 허용됩니다 (메모 삭제 시).
     */
    @Size(max = 2000, message = "메모는 최대 2000자까지 입력할 수 있습니다")
    private String content;
}
