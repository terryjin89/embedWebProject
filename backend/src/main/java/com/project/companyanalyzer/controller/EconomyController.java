package com.project.companyanalyzer.controller;

import com.project.companyanalyzer.dto.ExchangeRateDTO;
import com.project.companyanalyzer.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 경제지표 컨트롤러
 *
 * 경제지표 게시판을 위한 환율 정보 API 제공
 */
@Slf4j
@Tag(name = "경제지표 API", description = "경제지표 게시판용 환율 정보 조회 API")
@RestController
@RequestMapping("/api/economy")
@RequiredArgsConstructor
public class EconomyController {

    private final ExchangeRateService exchangeRateService;

    /**
     * 환율 목록 조회
     *
     * @param searchDate 조회 날짜 (YYYYMMDD 형식, 선택사항)
     * @return 주요 10개국 환율 정보 (전일대비 포함)
     */
    @Operation(
            summary = "환율 목록 조회",
            description = "경제지표 게시판용 환율 정보 목록을 조회합니다. 주요 10개국 통화의 환율 정보와 전일대비 변동 정보를 제공합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (날짜 형식 오류)"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/exchange-rates")
    public ResponseEntity<List<ExchangeRateDTO>> getExchangeRates(
            @Parameter(description = "조회 날짜 (YYYYMMDD 형식)", example = "20251114")
            @RequestParam(required = false) String searchDate
    ) {
        log.info("[경제지표] 환율 목록 조회 요청 - searchDate: {}", searchDate);

        // searchDate가 없으면 오늘 날짜 사용
        if (searchDate == null || searchDate.trim().isEmpty()) {
            searchDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            log.debug("searchDate 미입력 - 오늘 날짜 사용: {}", searchDate);
        }

        // 날짜 형식 검증
        validateDateFormat(searchDate);

        List<ExchangeRateDTO> exchangeRates = exchangeRateService.getExchangeRates(searchDate);

        log.info("[경제지표] 환율 목록 조회 완료 - 결과: {}건", exchangeRates.size());
        return ResponseEntity.ok(exchangeRates);
    }

    /**
     * 특정 통화 환율 상세 정보 조회
     *
     * @param currencyCode 통화 코드 (예: USD, JPY, EUR)
     * @param searchDate 조회 날짜 (YYYYMMDD 형식, 선택사항)
     * @return 특정 통화 환율 상세 정보
     */
    @Operation(
            summary = "특정 통화 환율 상세 조회",
            description = "특정 통화의 환율 상세 정보를 조회합니다. 매매 기준율, 전신환 매도/매수율, 전일대비 변동 정보를 제공합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (날짜 형식 오류)"),
            @ApiResponse(responseCode = "404", description = "통화 코드를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/exchange-rates/{currencyCode}/detail")
    public ResponseEntity<ExchangeRateDTO> getExchangeRateDetail(
            @Parameter(description = "통화 코드", example = "USD")
            @PathVariable String currencyCode,
            @Parameter(description = "조회 날짜 (YYYYMMDD 형식)", example = "20251114")
            @RequestParam(required = false) String searchDate
    ) {
        log.info("[경제지표] 환율 상세 조회 요청 - currencyCode: {}, searchDate: {}", currencyCode, searchDate);

        // searchDate가 없으면 오늘 날짜 사용
        if (searchDate == null || searchDate.trim().isEmpty()) {
            searchDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        // 날짜 형식 검증
        validateDateFormat(searchDate);

        List<ExchangeRateDTO> exchangeRates = exchangeRateService.getExchangeRates(searchDate);

        // 특정 통화 필터링
        ExchangeRateDTO result = exchangeRates.stream()
                .filter(rate -> rate.getCurUnit().equalsIgnoreCase(currencyCode))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("[경제지표] 통화 코드를 찾을 수 없음 - currencyCode: {}", currencyCode);
                    return new IllegalArgumentException("통화 코드를 찾을 수 없습니다: " + currencyCode);
                });

        log.info("[경제지표] 환율 상세 조회 완료 - currencyCode: {}", currencyCode);
        return ResponseEntity.ok(result);
    }

    /**
     * 날짜 형식 검증 (YYYYMMDD)
     *
     * @param searchDate 조회 날짜
     * @throws IllegalArgumentException 날짜 형식이 잘못된 경우
     */
    private void validateDateFormat(String searchDate) {
        if (searchDate == null || !searchDate.matches("\\d{8}")) {
            throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다. YYYYMMDD 형식으로 입력해주세요. (예: 20251114)");
        }

        try {
            LocalDate.parse(searchDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 날짜입니다: " + searchDate);
        }
    }
}
