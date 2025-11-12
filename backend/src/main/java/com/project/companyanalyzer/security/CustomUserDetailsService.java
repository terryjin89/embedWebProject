package com.project.companyanalyzer.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Spring Security UserDetailsService 구현체
 *
 * JWT 토큰에서 추출한 사용자 코드로 사용자 정보를 로드합니다.
 *
 * 현재는 임시 구현으로, 실제 Member 엔티티와 MemberRepository 구현 후
 * 데이터베이스에서 사용자 정보를 조회하도록 업데이트해야 합니다.
 *
 * TODO: MemberRepository를 사용하여 실제 데이터베이스 조회 구현
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // TODO: MemberRepository 주입 및 사용
    // private final MemberRepository memberRepository;

    /**
     * 사용자 코드로 사용자 정보 로드
     *
     * @param userCode 사용자 코드 (Member 엔티티의 PK)
     * @return UserDetails 객체
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
     */
    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        log.debug("Loading user by userCode: {}", userCode);

        // TODO: 실제 구현 시 데이터베이스에서 사용자 조회
        /*
        Member member = memberRepository.findByUserCode(userCode)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with userCode: " + userCode
                ));

        return User.builder()
                .username(member.getUserCode())
                .password(member.getPassword())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_USER")
                ))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
        */

        // 임시 구현: Mock 사용자 반환
        log.warn("Using mock user implementation - replace with actual database lookup");

        return User.builder()
                .username(userCode)
                .password("") // JWT 인증에서는 비밀번호 불필요
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_USER")
                ))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
