package com.project.companyanalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 프론트엔드로 전달할 환율 정보 DTO
 * 전일대비 계산 결과 포함
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDTO {

    /**
     * 통화 코드 (예: USD, JPY, EUR)
     */
    private String curUnit;

    /**
     * 국가/통화명 (예: 미국 달러, 일본 옌)
     */
    private String curNm;

    /**
     * 매매 기준율 (현재)
     */
    private BigDecimal dealBasR;

    /**
     * 전신환(송금) 받으실때 환율
     */
    private BigDecimal ttb;

    /**
     * 전신환(송금) 보내실때 환율
     */
    private BigDecimal tts;

    /**
     * 전일 대비 변동액
     * 양수: 상승, 음수: 하락
     */
    private BigDecimal changeAmount;

    /**
     * 전일 대비 변동률 (%)
     * 양수: 상승, 음수: 하락
     */
    private BigDecimal changeRate;

    /**
     * 변동 방향
     * "UP": 상승, "DOWN": 하락, "SAME": 동일
     */
    private String changeDirection;

    /**
     * 조회 날짜 (YYYYMMDD)
     */
    private String searchDate;
}
