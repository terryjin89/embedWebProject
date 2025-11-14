package com.project.companyanalyzer.repository;

import com.project.companyanalyzer.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Company Repository
 *
 * Spring Data JPA를 사용한 Company 엔티티 데이터 접근 계층
 *
 * 제공 메서드:
 * - findByCorpCode: 고유번호로 기업 조회
 * - findByStockCode: 종목코드로 기업 조회
 * - findByCorpNameContaining: 정식명칭으로 검색 (LIKE)
 * - findByStockNameContaining: 종목명으로 검색 (LIKE)
 * - findByIndutyCode: 업종코드로 필터링
 * - findByCorpCls: 법인구분으로 필터링
 * - searchCompanies: 복합 검색 (이름, 종목명, 업종, 법인구분)
 * - 페이지네이션: 모든 검색 메서드에서 Pageable 지원
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

    /**
     * 고유번호로 기업 조회
     *
     * @param corpCode 고유번호 (8자리)
     * @return Optional<Company> 기업 정보
     */
    Optional<Company> findByCorpCode(String corpCode);

    /**
     * 종목코드로 기업 조회
     *
     * @param stockCode 종목코드 (6자리)
     * @return Optional<Company> 기업 정보
     */
    Optional<Company> findByStockCode(String stockCode);

    /**
     * 정식명칭으로 검색 (LIKE 검색, 페이지네이션)
     *
     * @param corpName 검색할 기업명 (부분 검색)
     * @param pageable 페이지 정보 (page, size, sort)
     * @return Page<Company> 검색 결과 페이지
     */
    Page<Company> findByCorpNameContaining(String corpName, Pageable pageable);

    /**
     * 종목명으로 검색 (LIKE 검색, 페이지네이션)
     *
     * @param stockName 검색할 종목명 (부분 검색)
     * @param pageable 페이지 정보 (page, size, sort)
     * @return Page<Company> 검색 결과 페이지
     */
    Page<Company> findByStockNameContaining(String stockName, Pageable pageable);

    /**
     * 업종코드로 필터링 (페이지네이션)
     *
     * @param indutyCode 업종코드
     * @param pageable 페이지 정보 (page, size, sort)
     * @return Page<Company> 해당 업종의 기업 목록
     */
    Page<Company> findByIndutyCode(String indutyCode, Pageable pageable);

    /**
     * 법인구분으로 필터링 (페이지네이션)
     *
     * Y: 상장회사, E: 기타법인
     *
     * @param corpCls 법인구분 (Y, K, N, E)
     * @param pageable 페이지 정보 (page, size, sort)
     * @return Page<Company> 해당 법인구분의 기업 목록
     */
    Page<Company> findByCorpCls(String corpCls, Pageable pageable);

    /**
     * 상장사만 조회 (법인구분 Y, 페이지네이션)
     *
     * @param pageable 페이지 정보 (page, size, sort)
     * @return Page<Company> 상장 기업 목록
     */
    default Page<Company> findListedCompanies(Pageable pageable) {
        return findByCorpCls("Y", pageable);
    }

    /**
     * 업종코드와 법인구분으로 필터링 (페이지네이션)
     *
     * @param indutyCode 업종코드
     * @param corpCls 법인구분 (Y, K, N, E)
     * @param pageable 페이지 정보 (page, size, sort)
     * @return Page<Company> 필터링된 기업 목록
     */
    Page<Company> findByIndutyCodeAndCorpCls(String indutyCode, String corpCls, Pageable pageable);

    /**
     * 복합 검색 (이름, 종목명, 업종, 법인구분)
     *
     * 기업명 또는 종목명으로 검색하고, 업종코드와 법인구분으로 필터링합니다.
     * 모든 파라미터는 선택적(Optional)이며, null이 아닌 경우만 조건에 포함됩니다.
     *
     * @param keyword 검색 키워드 (corpName 또는 stockName에 포함되는 텍스트)
     * @param indutyCode 업종코드 (선택적)
     * @param corpCls 법인구분 (선택적)
     * @param pageable 페이지 정보 (page, size, sort)
     * @return Page<Company> 검색 결과 페이지
     */
    @Query("SELECT c FROM Company c " +
           "WHERE (:keyword IS NULL OR c.corpName LIKE %:keyword% OR c.stockName LIKE %:keyword%) " +
           "AND (:indutyCode IS NULL OR c.indutyCode = :indutyCode) " +
           "AND (:corpCls IS NULL OR c.corpCls = :corpCls)")
    Page<Company> searchCompanies(
        @Param("keyword") String keyword,
        @Param("indutyCode") String indutyCode,
        @Param("corpCls") String corpCls,
        Pageable pageable
    );

    /**
     * 기업명 또는 종목명으로 검색 (OR 조건, 페이지네이션)
     *
     * @param corpName 검색할 기업명 (부분 검색)
     * @param stockName 검색할 종목명 (부분 검색)
     * @param pageable 페이지 정보 (page, size, sort)
     * @return Page<Company> 검색 결과 페이지
     */
    Page<Company> findByCorpNameContainingOrStockNameContaining(
        String corpName,
        String stockName,
        Pageable pageable
    );

    /**
     * 업종코드로 기업 수 조회
     *
     * @param indutyCode 업종코드
     * @return 해당 업종의 기업 수
     */
    long countByIndutyCode(String indutyCode);

    /**
     * 법인구분으로 기업 수 조회
     *
     * @param corpCls 법인구분 (Y, K, N, E)
     * @return 해당 법인구분의 기업 수
     */
    long countByCorpCls(String corpCls);

    /**
     * 종목코드 존재 여부 확인
     *
     * @param stockCode 종목코드 (6자리)
     * @return true면 존재, false면 없음
     */
    boolean existsByStockCode(String stockCode);

    /**
     * 전체 기업 목록 조회 (페이지네이션)
     *
     * @param pageable 페이지 정보 (page, size, sort)
     * @return Page<Company> 전체 기업 목록
     */
    Page<Company> findAll(Pageable pageable);
}
