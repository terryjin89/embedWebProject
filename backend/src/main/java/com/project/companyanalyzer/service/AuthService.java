package com.project.companyanalyzer.service;

import com.project.companyanalyzer.dto.AuthResponse;
import com.project.companyanalyzer.dto.LoginRequest;
import com.project.companyanalyzer.dto.SignupRequest;
import com.project.companyanalyzer.entity.Member;
import com.project.companyanalyzer.repository.MemberRepository;
import com.project.companyanalyzer.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 서비스
 *
 * 회원가입, 로그인, 로그아웃 등 인증 관련 비즈니스 로직을 처리합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     *
     * 1. 이메일 중복 체크
     * 2. 비밀번호 BCrypt 암호화
     * 3. userCode 자동 생성 (UUID)
     * 4. Member 엔티티 저장
     * 5. JWT 토큰 생성 및 반환
     *
     * @param request 회원가입 요청 DTO
     * @return AuthResponse (JWT 토큰 + 사용자 정보)
     * @throws IllegalArgumentException 이미 사용 중인 이메일인 경우
     */
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        log.info("Signup attempt for email: {}", request.getEmail());

        // 1. 이메일 중복 체크
        if (memberRepository.existsByEmail(request.getEmail())) {
            log.warn("Signup failed: Email already exists - {}", request.getEmail());
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + request.getEmail());
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. Member 엔티티 생성 (userCode는 @PrePersist에서 자동 생성)
        Member member = Member.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .build();

        // 4. Member 저장
        Member savedMember = memberRepository.save(member);
        log.info("Signup successful: userCode={}, email={}", savedMember.getUserCode(), savedMember.getEmail());

        // 5. JWT 토큰 생성
        String token = jwtTokenProvider.createToken(savedMember.getUserCode());

        // 6. AuthResponse 반환
        return AuthResponse.of(
                token,
                savedMember.getUserCode(),
                savedMember.getEmail(),
                savedMember.getName()
        );
    }

    /**
     * 로그인
     *
     * 1. 이메일로 회원 조회
     * 2. 비밀번호 검증
     * 3. JWT 토큰 생성 및 반환
     *
     * @param request 로그인 요청 DTO
     * @return AuthResponse (JWT 토큰 + 사용자 정보)
     * @throws BadCredentialsException 이메일 또는 비밀번호가 일치하지 않는 경우
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // 1. 이메일로 회원 조회
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: Email not found - {}", request.getEmail());
                    return new BadCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다.");
                });

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            log.warn("Login failed: Invalid password for email - {}", request.getEmail());
            throw new BadCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        log.info("Login successful: userCode={}, email={}", member.getUserCode(), member.getEmail());

        // 3. JWT 토큰 생성
        String token = jwtTokenProvider.createToken(member.getUserCode());

        // 4. AuthResponse 반환
        return AuthResponse.of(
                token,
                member.getUserCode(),
                member.getEmail(),
                member.getName()
        );
    }

    /**
     * 토큰 검증
     *
     * JWT 토큰의 유효성을 검증하고 사용자 정보를 반환합니다.
     *
     * @param token JWT 토큰
     * @return AuthResponse (사용자 정보만, 토큰은 null)
     * @throws IllegalArgumentException 유효하지 않은 토큰인 경우
     */
    @Transactional(readOnly = true)
    public AuthResponse verifyToken(String token) {
        log.debug("Token verification attempt");

        // 1. 토큰 검증
        if (!jwtTokenProvider.validateToken(token)) {
            log.warn("Token verification failed: Invalid token");
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        // 2. 토큰에서 userCode 추출
        String userCode = jwtTokenProvider.getUserCode(token);

        // 3. userCode로 회원 조회
        Member member = memberRepository.findByUserCode(userCode)
                .orElseThrow(() -> {
                    log.warn("Token verification failed: User not found for userCode - {}", userCode);
                    return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
                });

        log.debug("Token verification successful: userCode={}", userCode);

        // 4. AuthResponse 반환 (토큰은 null, 사용자 정보만 반환)
        return AuthResponse.of(
                null,
                member.getUserCode(),
                member.getEmail(),
                member.getName()
        );
    }

    /**
     * 로그아웃
     *
     * JWT는 Stateless이므로 서버에서 별도의 로그아웃 처리가 불필요합니다.
     * 클라이언트에서 토큰을 삭제하는 것으로 로그아웃이 처리됩니다.
     *
     * 향후 토큰 블랙리스트 기능을 추가할 경우 이 메서드를 확장할 수 있습니다.
     */
    public void logout() {
        log.info("Logout called (JWT is stateless, no server-side action required)");
        // JWT는 Stateless이므로 서버에서 별도 처리 없음
        // 클라이언트에서 localStorage의 토큰 삭제로 로그아웃 처리
    }
}
