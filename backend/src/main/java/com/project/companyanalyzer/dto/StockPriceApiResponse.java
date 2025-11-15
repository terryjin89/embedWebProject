package com.project.companyanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 금융위원회 주식시세정보 API 응답 DTO
 *
 * API 전체 응답 구조를 매핑합니다.
 *
 * 응답 구조:
 * {
 *   "response": {
 *     "header": { ... },
 *     "body": {
 *       "items": {
 *         "item": [ ... ]
 *       }
 *     }
 *   }
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockPriceApiResponse {

    @JsonProperty("response")
    private ResponseWrapper response;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseWrapper {
        @JsonProperty("header")
        private Header header;

        @JsonProperty("body")
        private Body body;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Header {
        /**
         * 결과 코드
         * "00": 정상, 기타: 오류
         */
        @JsonProperty("resultCode")
        private String resultCode;

        /**
         * 결과 메시지
         * "NORMAL SERVICE.": 정상
         */
        @JsonProperty("resultMsg")
        private String resultMsg;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body {
        /**
         * 한 페이지 결과 수
         */
        @JsonProperty("numOfRows")
        private Integer numOfRows;

        /**
         * 페이지 번호
         */
        @JsonProperty("pageNo")
        private Integer pageNo;

        /**
         * 전체 결과 수
         */
        @JsonProperty("totalCount")
        private Integer totalCount;

        /**
         * 주가 데이터 목록
         */
        @JsonProperty("items")
        private Items items;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Items {
        @JsonProperty("item")
        private List<StockPriceItem> item;
    }
}
