package com.project.companyanalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 차트용 과거 환율 데이터 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalRateDTO {
    /**
     * 날짜 (YYYY-MM-DD 형식)
     */
    private String date;

    /**
     * 해당 날짜의 매매 기준율
     */
    private BigDecimal rate;
}
