package com.project.companyanalyzer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * AddFavoriteRequest DTO
 *
 * 관심기업 등록 요청 DTO
 *
 * 요구사항:
 * - stockCode: 종목코드 (6자리, 필수)
 * - corpCode: 기업 코드 (8자리, 필수)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddFavoriteRequest {

    /**
     * 종목코드 (6자리, 비상장 기업의 경우 null 가능)
     * 예: "005930" (삼성전자), null (비상장 기업)
     */
    @Pattern(regexp = "^$|\\d{6}", message = "종목코드는 숫자 6자리여야 합니다. (비상장 기업은 빈 문자열 가능)")
    private String stockCode;

    /**
     * 기업 코드 (8자리)
     * DART API의 고유번호
     * 예: "00126380" (삼성전자)
     */
    @NotBlank(message = "기업 코드는 필수입니다.")
    @Size(min = 8, max = 8, message = "기업 코드는 8자리여야 합니다.")
    @Pattern(regexp = "\\d{8}", message = "기업 코드는 숫자 8자리여야 합니다.")
    private String corpCode;
}
