package com.project.companyanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DART 공시 목록 API 응답 DTO
 *
 * DART API의 /list.json 엔드포인트 응답을 매핑
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DartDisclosureResponse {

    /**
     * 에러 및 정보 코드
     * "000": 정상
     * "010": 등록되지 않은 키
     * "011": 사용할 수 없는 키
     * "013": 조회된 데이타가 없습니다
     */
    private String status;

    /**
     * 에러 및 정보 메시지
     */
    private String message;

    /**
     * 페이지 번호
     */
    @JsonProperty("page_no")
    private Integer pageNo;

    /**
     * 페이지별 건수
     */
    @JsonProperty("page_count")
    private Integer pageCount;

    /**
     * 총 건수
     */
    @JsonProperty("total_count")
    private Integer totalCount;

    /**
     * 총 페이지 수
     */
    @JsonProperty("total_page")
    private Integer totalPage;

    /**
     * 공시 목록
     */
    private List<DartDisclosureItem> list;

    /**
     * API 호출이 성공했는지 확인
     * @return 성공 여부
     */
    public boolean isSuccess() {
        return "000".equals(status);
    }
}
