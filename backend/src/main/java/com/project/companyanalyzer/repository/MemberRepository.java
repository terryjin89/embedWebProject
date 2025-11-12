package com.project.companyanalyzer.repository;

import com.project.companyanalyzer.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Member Repository
 *
 * Spring Data JPA를 사용한 Member 엔티티 데이터 접근 계층
 *
 * 제공 메서드:
 * - findByUserCode: 사용자 코드로 회원 조회 (JWT 인증용)
 * - findByEmail: 이메일로 회원 조회 (로그인용)
 * - existsByEmail: 이메일 중복 체크 (회원가입용)
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    /**
     * 사용자 코드로 회원 조회
     *
     * JWT 토큰의 subject에서 추출한 userCode로 회원 정보를 조회합니다.
     * CustomUserDetailsService에서 사용됩니다.
     *
     * @param userCode 사용자 코드 (UUID)
     * @return Optional<Member> 회원 정보
     */
    Optional<Member> findByUserCode(String userCode);

    /**
     * 이메일로 회원 조회
     *
     * 로그인 시 이메일로 회원 정보를 조회합니다.
     *
     * @param email 이메일 주소
     * @return Optional<Member> 회원 정보
     */
    Optional<Member> findByEmail(String email);

    /**
     * 이메일 중복 체크
     *
     * 회원가입 시 이메일이 이미 사용 중인지 확인합니다.
     *
     * @param email 이메일 주소
     * @return true면 이미 존재, false면 사용 가능
     */
    boolean existsByEmail(String email);
}
