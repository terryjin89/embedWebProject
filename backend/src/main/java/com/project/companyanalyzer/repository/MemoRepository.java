package com.project.companyanalyzer.repository;

import com.project.companyanalyzer.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MemoRepository
 *
 * 메모(Memo) 엔티티에 대한 데이터베이스 작업을 처리하는 Repository
 *
 * 주요 기능:
 * - 사용자별 메모 조회
 * - 특정 종목에 대한 메모 조회/저장
 * - 메모 존재 여부 확인
 */
@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    /**
     * 사용자 코드와 종목코드로 메모 조회
     *
     * Member를 fetch join하여 N+1 문제를 방지합니다.
     * 사용자가 특정 종목에 대해 작성한 메모를 조회합니다.
     *
     * @param userCode  사용자 코드
     * @param stockCode 주식 종목코드
     * @return 메모 Optional
     */
    @Query("SELECT m FROM Memo m " +
           "JOIN FETCH m.member mem " +
           "WHERE mem.userCode = :userCode AND m.stockCode = :stockCode")
    Optional<Memo> findByUserCodeAndStockCode(
        @Param("userCode") String userCode,
        @Param("stockCode") String stockCode
    );

    /**
     * 사용자 코드로 전체 메모 목록 조회
     *
     * 사용자가 작성한 모든 메모를 조회합니다.
     * 수정일시(updatedAt) 내림차순으로 정렬합니다.
     *
     * @param userCode 사용자 코드
     * @return 사용자의 메모 목록
     */
    @Query("SELECT m FROM Memo m " +
           "JOIN FETCH m.member mem " +
           "WHERE mem.userCode = :userCode " +
           "ORDER BY m.updatedAt DESC")
    List<Memo> findByUserCodeWithMember(@Param("userCode") String userCode);

    /**
     * 사용자 코드와 종목코드로 메모 존재 여부 확인
     *
     * 메모가 이미 작성되어 있는지 확인할 때 사용합니다.
     * UNIQUE KEY 제약조건이 있지만, 애플리케이션 레벨에서도 체크합니다.
     *
     * @param userCode  사용자 코드
     * @param stockCode 주식 종목코드
     * @return 존재 여부 (true/false)
     */
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
           "FROM Memo m " +
           "JOIN m.member mem " +
           "WHERE mem.userCode = :userCode AND m.stockCode = :stockCode")
    boolean existsByUserCodeAndStockCode(
        @Param("userCode") String userCode,
        @Param("stockCode") String stockCode
    );

    /**
     * 사용자 코드와 종목코드로 메모 삭제
     *
     * 특정 종목에 대한 사용자의 메모를 삭제합니다.
     *
     * @param userCode  사용자 코드
     * @param stockCode 주식 종목코드
     * @return 삭제된 행의 수 (0 or 1)
     */
    @Modifying
    @Query("DELETE FROM Memo m " +
           "WHERE m.member.userCode = :userCode AND m.stockCode = :stockCode")
    int deleteByUserCodeAndStockCode(
        @Param("userCode") String userCode,
        @Param("stockCode") String stockCode
    );

    /**
     * 사용자별 메모 개수 조회
     *
     * 사용자가 작성한 메모의 총 개수를 조회합니다.
     *
     * @param userCode 사용자 코드
     * @return 메모 개수
     */
    @Query("SELECT COUNT(m) FROM Memo m " +
           "JOIN m.member mem " +
           "WHERE mem.userCode = :userCode")
    long countByUserCode(@Param("userCode") String userCode);
}
