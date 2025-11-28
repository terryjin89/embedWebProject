package com.project.companyanalyzer.controller;

import com.project.companyanalyzer.dto.CompanyDTO;
import com.project.companyanalyzer.dto.CompanyListResponse;
import com.project.companyanalyzer.dto.DartDisclosureResponse;
import com.project.companyanalyzer.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 기업 정보 API 컨트롤러
 *
 * 📝 문서 참고: readme/companyInfoFunction.md - "API 명세" 섹션 (226-293라인)
 *
 * 기업 목록 조회, 검색, 필터링, 상세 조회, 공시 정보 조회를 제공합니다.
 *
 * 엔드포인트:
 * - GET /api/companies - 기업 목록 조회 (검색, 필터링, 페이지네이션)
 * - GET /api/companies/{corpCode} - 기업 상세 조회 (데이터베이스)
 * - GET /api/companies/{corpCode}/disclosures - 기업 공시 목록 조회 (DART API 프록시)
 *
 * @see readme/companyInfoFunction.md
 */
@Slf4j
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Company", description = "기업 정보 API")
public class CompanyController {

    private final CompanyService companyService;

    /**
     * 기업 목록 조회
     *
     * 전체 기업 목록을 조회하거나 검색 조건에 따라 필터링된 목록을 조회합니다.
     *
     * @param keyword 검색 키워드 (기업명 또는 종목명, 선택)
     * @param indutyCode 업종코드 (선택)
     * @param corpCls 법인구분 (Y/K/N/E, 선택)
     * @param page 페이지 번호 (0부터 시작, 기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param sort 정렬 기준 (기본값: corpCode,asc)
     * @return 기업 목록 및 페이지 정보
     */
    @GetMapping
    @Operation(summary = "기업 목록 조회", description = "전체 기업 목록 또는 검색/필터링된 기업 목록을 조회합니다.")
    public ResponseEntity<CompanyListResponse> getCompanies(
            @Parameter(description = "검색 키워드 (기업명 또는 종목명)")
            @RequestParam(required = false) String keyword,

            @Parameter(description = "업종코드")
            @RequestParam(required = false) String indutyCode,

            @Parameter(description = "법인구분 (Y=유가증권, K=코스닥, N=코넥스, E=기타)")
            @RequestParam(required = false) String corpCls,

            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "정렬 기준 (예: corpName,asc)")
            @RequestParam(defaultValue = "corpCode,asc") String[] sort
    ) {
        log.info("[CompanyController] GET /companies - keyword: {}, indutyCode: {}, corpCls: {}, page: {}, size: {}",
                keyword, indutyCode, corpCls, page, size);

        // Sort 객체 생성
        Sort sortObj = createSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        CompanyListResponse response;

        // 검색 조건이 있으면 검색, 없으면 전체 조회
        if (keyword != null || indutyCode != null || corpCls != null) {
            response = companyService.searchCompanies(keyword, indutyCode, corpCls, pageable);
        } else {
            response = companyService.getAllCompanies(pageable);
        }

        log.debug("[CompanyController] 응답 - 총 {}건, 현재 페이지: {}/{}",
                response.getTotalElements(), response.getCurrentPage() + 1, response.getTotalPages());

        return ResponseEntity.ok(response);
    }

    /**
     * 기업 상세 조회 (고유번호)
     *
     * 고유번호(corpCode)로 기업 상세 정보를 조회합니다.
     *
     * @param corpCode 고유번호 (8자리)
     * @return 기업 상세 정보
     */
    @GetMapping("/{corpCode}")
    @Operation(summary = "기업 상세 조회", description = "고유번호로 기업 상세 정보를 조회합니다.")
    public ResponseEntity<CompanyDTO> getCompanyByCorpCode(
            @Parameter(description = "고유번호 (8자리)", required = true)
            @PathVariable String corpCode
    ) {
        log.info("[CompanyController] GET /companies/{} - 기업 상세 조회", corpCode);

        CompanyDTO company = companyService.getCompanyByCorpCode(corpCode);

        log.debug("[CompanyController] 응답 - {}", company.getCorpName());

        return ResponseEntity.ok(company);
    }

    /**
     * 기업 공시 목록 조회
     *
     * DART API를 통해 해당 기업의 공시 목록을 조회합니다.
     *
     * @param corpCode 고유번호 (8자리)
     * @param bgnDe 시작일 (YYYYMMDD, 기본값: 최근 30일)
     * @param endDe 종료일 (YYYYMMDD, 기본값: 오늘)
     * @param pblntfTy 공시유형 (A~J, 선택)
     * @param pageNo 페이지 번호 (기본값: 1)
     * @param pageCount 페이지당 건수 (기본값: 10)
     * @return 공시 목록
     */
    @GetMapping("/{corpCode}/disclosures")
    @Operation(summary = "기업 공시 목록 조회", description = "DART API를 통해 기업의 공시 목록을 조회합니다.")
    public ResponseEntity<DartDisclosureResponse> getCompanyDisclosures(
            @Parameter(description = "고유번호 (8자리)", required = true)
            @PathVariable String corpCode,

            @Parameter(description = "시작일 (YYYYMMDD)")
            @RequestParam(required = false) String bgnDe,

            @Parameter(description = "종료일 (YYYYMMDD)")
            @RequestParam(required = false) String endDe,

            @Parameter(description = "공시유형 (A=정기공시, B=주요사항보고, C=발행공시, D=지분공시, E=기타공시, F=외부감사관련, G=펀드공시, H=자산유동화, I=거래소공시, J=공정위공시)")
            @RequestParam(required = false) String pblntfTy,

            @Parameter(description = "페이지 번호")
            @RequestParam(defaultValue = "1") Integer pageNo,

            @Parameter(description = "페이지당 건수")
            @RequestParam(defaultValue = "10") Integer pageCount
    ) {
        log.info("[CompanyController] GET /companies/{}/disclosures - 공시 목록 조회", corpCode);
        log.debug("[CompanyController] 검색 조건 - 기간: {}~{}, 공시유형: {}, page: {}, size: {}",
                bgnDe, endDe, pblntfTy, pageNo, pageCount);

        // 날짜 기본값 설정 (최근 30일)
        if (bgnDe == null || endDe == null) {
            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.LocalDate startDate = today.minusDays(30);

            if (endDe == null) {
                endDe = today.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            }
            if (bgnDe == null) {
                bgnDe = startDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            }
        }

        DartDisclosureResponse response = companyService.getCompanyDisclosures(
                corpCode, bgnDe, endDe, pblntfTy, pageNo, pageCount);

        log.debug("[CompanyController] 응답 - 총 {}건, 페이지: {}/{}",
                response.getTotalCount(), response.getPageNo(), response.getTotalPage());

        return ResponseEntity.ok(response);
    }

    /**
     * Sort 객체 생성
     *
     * @param sort 정렬 조건 배열 (예: ["corpName", "asc"])
     * @return Sort 객체
     */
    private Sort createSort(String[] sort) {
        if (sort == null || sort.length == 0) {
            return Sort.by(Sort.Direction.ASC, "corpCode");
        }

        String property = sort[0];
        Sort.Direction direction = sort.length > 1 && "desc".equalsIgnoreCase(sort[1])
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(direction, property);
    }
}
