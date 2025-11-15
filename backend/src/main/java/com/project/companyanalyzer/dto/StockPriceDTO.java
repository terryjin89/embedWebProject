package com.project.companyanalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 주가 정보 DTO
 *
 * 프론트엔드에 전달할 간소화된 주가 데이터 구조
 * StockPriceItem을 프론트엔드 친화적 형태로 변환
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockPriceDTO {

    /**
     * 기준일자 (YYYYMMDD)
     */
    private String basDt;

    /**
     * 단축코드 (종목코드 6자리)
     */
    private String srtnCd;

    /**
     * 종목명
     */
    private String itmsNm;

    /**
     * 종가 (정규시장 종료 시 최종가격)
     */
    private String clpr;

    /**
     * 대비 (전일 대비 등락)
     */
    private String vs;

    /**
     * 등락률 (전일 대비 등락 비율)
     */
    private String fltRt;

    /**
     * 시가 (정규시장 개시 후 최초가격)
     */
    private String mkp;

    /**
     * 고가 (하루 중 가격의 최고치)
     */
    private String hipr;

    /**
     * 저가 (하루 중 가격의 최저치)
     */
    private String lopr;

    /**
     * 거래량 (체결수량의 누적 합계)
     */
    private String trqu;

    /**
     * StockPriceItem을 StockPriceDTO로 변환
     *
     * @param item API 응답 아이템
     * @return 변환된 DTO
     */
    public static StockPriceDTO from(StockPriceItem item) {
        return StockPriceDTO.builder()
                .basDt(item.getBasDt())
                .srtnCd(item.getSrtnCd())
                .itmsNm(item.getItmsNm())
                .clpr(item.getClpr())
                .vs(item.getVs())
                .fltRt(item.getFltRt())
                .mkp(item.getMkp())
                .hipr(item.getHipr())
                .lopr(item.getLopr())
                .trqu(item.getTrqu())
                .build();
    }
}
