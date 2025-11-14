package com.project.companyanalyzer.repository;

import com.project.companyanalyzer.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * StockRepository
 *
 * 관심기업(Stock) 엔티티에 대한 데이터베이스 작업을 처리하는 Repository
 *
 * 주요 기능:
 * - 사용자별 관심기업 목록 조회
 * - 관심기업 등록/삭제
 * - 중복 등록 방지 (UNIQUE KEY 제약조건)
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    /**
     * 사용자 코드로 관심기업 목록 조회
     *
     * Member와 Company를 fetch join하여 N+1 문제를 방지합니다.
     * 등록일시(registeredAt) 내림차순으로 정렬합니다.
     *
     * @param userCode 사용자 코드
     * @return 사용자의 관심기업 목록
     */
    @Query("SELECT s FROM Stock s " +
           "JOIN FETCH s.member m " +
           "JOIN FETCH s.company c " +
           "WHERE m.userCode = :userCode " +
           "ORDER BY s.registeredAt DESC")
    List<Stock> findByUserCodeWithMemberAndCompany(@Param("userCode") String userCode);

    /**
     * 사용자 코드와 종목코드로 관심기업 조회
     *
     * 특정 사용자가 특정 종목을 관심기업으로 등록했는지 확인할 때 사용합니다.
     *
     * @param userCode  사용자 코드
     * @param stockCode 주식 종목코드
     * @return 관심기업 Optional
     */
    @Query("SELECT s FROM Stock s " +
           "JOIN FETCH s.member m " +
           "JOIN FETCH s.company c " +
           "WHERE m.userCode = :userCode AND s.stockCode = :stockCode")
    Optional<Stock> findByUserCodeAndStockCode(
        @Param("userCode") String userCode,
        @Param("stockCode") String stockCode
    );

    /**
     * 사용자 코드와 종목코드로 관심기업 존재 여부 확인
     *
     * 중복 등록을 방지하기 위해 사용합니다.
     * UNIQUE KEY 제약조건이 있지만, 애플리케이션 레벨에서도 체크합니다.
     *
     * @param userCode  사용자 코드
     * @param stockCode 주식 종목코드
     * @return 존재 여부 (true/false)
     */
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
           "FROM Stock s " +
           "JOIN s.member m " +
           "WHERE m.userCode = :userCode AND s.stockCode = :stockCode")
    boolean existsByUserCodeAndStockCode(
        @Param("userCode") String userCode,
        @Param("stockCode") String stockCode
    );

    /**
     * 사용자 코드와 종목코드로 관심기업 삭제
     *
     * 관심기업 삭제 기능에서 사용합니다.
     *
     * @param userCode  사용자 코드
     * @param stockCode 주식 종목코드
     * @return 삭제된 행의 수 (0 or 1)
     */
    @Query("DELETE FROM Stock s " +
           "WHERE s.member.userCode = :userCode AND s.stockCode = :stockCode")
    int deleteByUserCodeAndStockCode(
        @Param("userCode") String userCode,
        @Param("stockCode") String stockCode
    );

    /**
     * 사용자별 관심기업 개수 조회
     *
     * 사용자가 등록한 관심기업의 총 개수를 조회합니다.
     *
     * @param userCode 사용자 코드
     * @return 관심기업 개수
     */
    @Query("SELECT COUNT(s) FROM Stock s " +
           "JOIN s.member m " +
           "WHERE m.userCode = :userCode")
    long countByUserCode(@Param("userCode") String userCode);

    /**
     * 종목코드로 관심기업 등록 사용자 수 조회
     *
     * 특정 종목이 몇 명의 사용자에게 관심기업으로 등록되었는지 조회합니다.
     * (선택적 기능 - 향후 인기 종목 분석 등에 활용 가능)
     *
     * @param stockCode 주식 종목코드
     * @return 해당 종목을 관심기업으로 등록한 사용자 수
     */
    @Query("SELECT COUNT(DISTINCT s.member.userCode) FROM Stock s " +
           "WHERE s.stockCode = :stockCode")
    long countUsersByStockCode(@Param("stockCode") String stockCode);
}
