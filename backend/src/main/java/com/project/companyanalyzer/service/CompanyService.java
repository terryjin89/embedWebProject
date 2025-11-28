package com.project.companyanalyzer.service;

import com.project.companyanalyzer.dto.CompanyDTO;
import com.project.companyanalyzer.dto.CompanyListResponse;
import com.project.companyanalyzer.dto.DartCompanyResponse;
import com.project.companyanalyzer.dto.DartDisclosureResponse;
import com.project.companyanalyzer.entity.Company;
import com.project.companyanalyzer.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 기업 정보 서비스
 *
 * 📝 문서 참고: readme/companyInfoFunction.md - "백엔드 구현" 섹션 (400-430라인)
 *
 * 기업 목록 조회, 검색, 필터링 및 상세 정보 조회를 처리
 * CompanyRepository와 DartApiService를 조합하여 사용
 *
 * 주요 기능:
 * - 데이터베이스에서 기업 목록 조회 (페이지네이션, 검색, 필터링)
 * - Entity → DTO 변환
 * - DART API를 통한 실시간 공시 정보 조회
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final DartApiService dartApiService;

    /**
     * 전체 기업 목록 조회 (페이지네이션)
     *
     * @param pageable 페이지 정보
     * @return 기업 목록 페이지
     */
    public CompanyListResponse getAllCompanies(Pageable pageable) {
        log.info("[CompanyService] 전체 기업 목록 조회 - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Company> companyPage = companyRepository.findAll(pageable);
        Page<CompanyDTO> dtoPage = companyPage.map(CompanyDTO::fromEntity);

        log.debug("[CompanyService] 조회 결과 - 총 {}건, {}페이지",
                dtoPage.getTotalElements(), dtoPage.getTotalPages());

        return CompanyListResponse.fromPage(dtoPage);
    }

    /**
     * 기업 검색 (복합 조건)
     *
     * @param keyword 검색 키워드 (기업명 또는 종목명)
     * @param indutyCode 업종코드 (선택)
     * @param corpCls 법인구분 (선택)
     * @param pageable 페이지 정보
     * @return 검색 결과 페이지
     */
    public CompanyListResponse searchCompanies(
            String keyword,
            String indutyCode,
            String corpCls,
            Pageable pageable
    ) {
        log.info("[CompanyService] 기업 검색 - keyword: {}, indutyCode: {}, corpCls: {}",
                keyword, indutyCode, corpCls);

        Page<Company> companyPage = companyRepository.searchCompanies(
                keyword, indutyCode, corpCls, pageable);
        Page<CompanyDTO> dtoPage = companyPage.map(CompanyDTO::fromEntity);

        log.debug("[CompanyService] 검색 결과 - 총 {}건", dtoPage.getTotalElements());

        return CompanyListResponse.fromPage(dtoPage);
    }

    /**
     * 기업명으로 검색 (LIKE)
     *
     * @param corpName 기업명
     * @param pageable 페이지 정보
     * @return 검색 결과 페이지
     */
    public CompanyListResponse searchByCorpName(String corpName, Pageable pageable) {
        log.info("[CompanyService] 기업명 검색 - corpName: {}", corpName);

        Page<Company> companyPage = companyRepository.findByCorpNameContaining(corpName, pageable);
        Page<CompanyDTO> dtoPage = companyPage.map(CompanyDTO::fromEntity);

        return CompanyListResponse.fromPage(dtoPage);
    }

    /**
     * 종목명으로 검색 (LIKE)
     *
     * @param stockName 종목명
     * @param pageable 페이지 정보
     * @return 검색 결과 페이지
     */
    public CompanyListResponse searchByStockName(String stockName, Pageable pageable) {
        log.info("[CompanyService] 종목명 검색 - stockName: {}", stockName);

        Page<Company> companyPage = companyRepository.findByStockNameContaining(stockName, pageable);
        Page<CompanyDTO> dtoPage = companyPage.map(CompanyDTO::fromEntity);

        return CompanyListResponse.fromPage(dtoPage);
    }

    /**
     * 업종코드로 필터링
     *
     * @param indutyCode 업종코드
     * @param pageable 페이지 정보
     * @return 필터링 결과 페이지
     */
    public CompanyListResponse filterByIndutyCode(String indutyCode, Pageable pageable) {
        log.info("[CompanyService] 업종코드 필터링 - indutyCode: {}", indutyCode);

        Page<Company> companyPage = companyRepository.findByIndutyCode(indutyCode, pageable);
        Page<CompanyDTO> dtoPage = companyPage.map(CompanyDTO::fromEntity);

        return CompanyListResponse.fromPage(dtoPage);
    }

    /**
     * 법인구분으로 필터링
     *
     * @param corpCls 법인구분 (Y, K, N, E)
     * @param pageable 페이지 정보
     * @return 필터링 결과 페이지
     */
    public CompanyListResponse filterByCorpCls(String corpCls, Pageable pageable) {
        log.info("[CompanyService] 법인구분 필터링 - corpCls: {}", corpCls);

        Page<Company> companyPage = companyRepository.findByCorpCls(corpCls, pageable);
        Page<CompanyDTO> dtoPage = companyPage.map(CompanyDTO::fromEntity);

        return CompanyListResponse.fromPage(dtoPage);
    }

    /**
     * 상장사만 조회
     *
     * @param pageable 페이지 정보
     * @return 상장사 목록 페이지
     */
    public CompanyListResponse getListedCompanies(Pageable pageable) {
        log.info("[CompanyService] 상장사 목록 조회");

        Page<Company> companyPage = companyRepository.findListedCompanies(pageable);
        Page<CompanyDTO> dtoPage = companyPage.map(CompanyDTO::fromEntity);

        return CompanyListResponse.fromPage(dtoPage);
    }

    /**
     * 기업 상세 정보 조회 (고유번호)
     *
     * DB에 있으면 DB에서 조회하고, 없으면 DART API를 통해 실시간 조회
     *
     * @param corpCode 고유번호 (8자리)
     * @return 기업 상세 정보
     */
    public CompanyDTO getCompanyByCorpCode(String corpCode) {
        log.info("[CompanyService] 기업 상세 조회 - corpCode: {}", corpCode);

        // 1. DB에서 먼저 조회
        Optional<Company> companyOpt = companyRepository.findByCorpCode(corpCode);

        if (companyOpt.isPresent()) {
            log.debug("[CompanyService] DB에서 기업 정보 조회 완료 - {}", companyOpt.get().getCorpName());
            return CompanyDTO.fromEntity(companyOpt.get());
        }

        // 2. DB에 없으면 DART API 호출
        log.info("[CompanyService] DB에 없음, DART API 호출 - corpCode: {}", corpCode);
        DartCompanyResponse dartResponse = dartApiService.getCompanyInfo(corpCode);

        if (dartResponse == null || !dartResponse.isSuccess()) {
            log.error("[CompanyService] DART API에서도 기업 정보 없음 - corpCode: {}", corpCode);
            throw new RuntimeException("기업 정보를 찾을 수 없습니다. corpCode: " + corpCode);
        }

        // 3. DART 응답을 CompanyDTO로 변환
        log.info("[CompanyService] DART API에서 기업 정보 조회 완료 - {}", dartResponse.getCorpName());
        return CompanyDTO.fromDartResponse(dartResponse);
    }

    /**
     * 기업 상세 정보 조회 (종목코드)
     *
     * @param stockCode 종목코드 (6자리)
     * @return 기업 상세 정보
     */
    public CompanyDTO getCompanyByStockCode(String stockCode) {
        log.info("[CompanyService] 기업 상세 조회 - stockCode: {}", stockCode);

        Company company = companyRepository.findByStockCode(stockCode)
                .orElseThrow(() -> {
                    log.error("[CompanyService] 기업 정보 없음 - stockCode: {}", stockCode);
                    return new RuntimeException("기업 정보를 찾을 수 없습니다. stockCode: " + stockCode);
                });

        return CompanyDTO.fromEntity(company);
    }

    /**
     * 기업 공시 목록 조회
     *
     * DART API를 통해 해당 기업의 공시 목록을 조회합니다.
     * DB에 기업이 있든 없든 DART API로 직접 조회합니다.
     *
     * @param corpCode 고유번호 (8자리)
     * @param bgnDe 시작일 (YYYYMMDD)
     * @param endDe 종료일 (YYYYMMDD)
     * @param pblntfTy 공시유형 (선택)
     * @param pageNo 페이지 번호
     * @param pageCount 페이지당 건수
     * @return 공시 목록
     */
    public DartDisclosureResponse getCompanyDisclosures(
            String corpCode,
            String bgnDe,
            String endDe,
            String pblntfTy,
            Integer pageNo,
            Integer pageCount
    ) {
        log.info("[CompanyService] 기업 공시 목록 조회 - corpCode: {}, 기간: {}~{}",
                corpCode, bgnDe, endDe);

        // DART API 직접 호출 (DB 체크 제거)
        // DB에 없는 기업도 DART API로 공시 조회 가능
        return dartApiService.getDisclosures(corpCode, bgnDe, endDe, pblntfTy, pageNo, pageCount);
    }

    /**
     * 업종별 기업 수 조회
     *
     * @param indutyCode 업종코드
     * @return 기업 수
     */
    public long countByIndutyCode(String indutyCode) {
        return companyRepository.countByIndutyCode(indutyCode);
    }

    /**
     * 법인구분별 기업 수 조회
     *
     * @param corpCls 법인구분
     * @return 기업 수
     */
    public long countByCorpCls(String corpCls) {
        return companyRepository.countByCorpCls(corpCls);
    }
}
