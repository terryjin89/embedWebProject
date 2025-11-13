package com.project.companyanalyzer.controller;

import com.project.companyanalyzer.dto.AuthResponse;
import com.project.companyanalyzer.dto.LoginRequest;
import com.project.companyanalyzer.dto.SignupRequest;
import com.project.companyanalyzer.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 인증 컨트롤러
 *
 * 회원가입, 로그인, 로그아웃, 토큰 검증 등 인증 관련 API를 제공합니다.
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     *
     * 새로운 사용자를 등록하고 JWT 토큰을 발급합니다.
     *
     * @param request 회원가입 요청 DTO (이메일, 비밀번호, 이름)
     * @return AuthResponse (JWT 토큰 + 사용자 정보)
     */
    @PostMapping("/signup")
    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록하고 JWT 토큰을 발급합니다. 이메일 중복 시 에러를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "이미 사용 중인 이메일"),
            @ApiResponse(responseCode = "422", description = "유효하지 않은 입력값")
    })
    public ResponseEntity<AuthResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        log.info("POST /api/auth/signup - email: {}", request.getEmail());

        try {
            AuthResponse response = authService.signup(request);
            log.info("Signup successful - userCode: {}", response.getUserCode());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            log.warn("Signup failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 로그인
     *
     * 이메일과 비밀번호로 로그인하고 JWT 토큰을 발급합니다.
     *
     * @param request 로그인 요청 DTO (이메일, 비밀번호)
     * @return AuthResponse (JWT 토큰 + 사용자 정보)
     */
    @PostMapping("/login")
    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호로 로그인하고 JWT 토큰을 발급합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "이메일 또는 비밀번호 불일치"),
            @ApiResponse(responseCode = "422", description = "유효하지 않은 입력값")
    })
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        log.info("POST /api/auth/login - email: {}", request.getEmail());

        try {
            AuthResponse response = authService.login(request);
            log.info("Login successful - userCode: {}", response.getUserCode());
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            log.warn("Login failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 로그아웃
     *
     * JWT는 Stateless이므로 서버에서 별도의 로그아웃 처리가 불필요합니다.
     * 클라이언트에서 토큰을 삭제하는 것으로 로그아웃이 처리됩니다.
     *
     * @return 로그아웃 성공 메시지
     */
    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃",
            description = "로그아웃 처리를 수행합니다. JWT는 Stateless이므로 클라이언트에서 토큰을 삭제하면 됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    public ResponseEntity<Map<String, String>> logout() {
        log.info("POST /api/auth/logout");

        authService.logout();

        Map<String, String> response = new HashMap<>();
        response.put("message", "로그아웃되었습니다.");
        response.put("description", "클라이언트에서 토큰을 삭제해주세요.");

        return ResponseEntity.ok(response);
    }

    /**
     * 토큰 검증
     *
     * JWT 토큰의 유효성을 검증하고 사용자 정보를 반환합니다.
     *
     * @param token JWT 토큰 (Authorization 헤더의 "Bearer " 제거 후 전달)
     * @return AuthResponse (사용자 정보만, 토큰은 null)
     */
    @GetMapping("/verify")
    @Operation(
            summary = "토큰 검증",
            description = "JWT 토큰의 유효성을 검증하고 사용자 정보를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 검증 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")
    })
    public ResponseEntity<AuthResponse> verifyToken(
            @Parameter(description = "JWT 토큰 (Bearer 제외)", required = true)
            @RequestHeader("Authorization") String authHeader
    ) {
        log.info("GET /api/auth/verify");

        try {
            // "Bearer " 접두사 제거
            String token = authHeader.replace("Bearer ", "");

            AuthResponse response = authService.verifyToken(token);
            log.info("Token verification successful - userCode: {}", response.getUserCode());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("Token verification failed: {}", e.getMessage());
            throw e;
        }
    }
}
