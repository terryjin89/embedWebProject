package com.project.companyanalyzer.controller;

import com.project.companyanalyzer.dto.MemoRequest;
import com.project.companyanalyzer.dto.MemoResponse;
import com.project.companyanalyzer.service.MemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 메모 API 컨트롤러
 *
 * 관심기업에 대한 사용자 메모 조회 및 저장 기능을 제공합니다.
 * 모든 엔드포인트는 JWT 인증이 필요합니다.
 *
 * 엔드포인트:
 * - GET /api/favorites/{stockCode}/memo - 메모 조회 (인증 필수)
 * - POST /api/favorites/{stockCode}/memo - 메모 저장/수정 (인증 필수)
 *
 * 권한 검증: 본인의 메모만 조회/수정 가능합니다.
 */
@Slf4j
@RestController
@RequestMapping("/favorites/{stockCode}/memo")
@RequiredArgsConstructor
@Tag(name = "Memo", description = "메모 API (인증 필수)")
@SecurityRequirement(name = "bearerAuth")
public class MemoController {

    private final MemoService memoService;

    /**
     * 메모 조회
     *
     * 인증된 사용자가 특정 종목에 대해 작성한 메모를 조회합니다.
     * 메모가 없으면 빈 메모가 반환됩니다.
     *
     * @param stockCode 종목코드 (6자리)
     * @return 메모 정보 (MemoResponse DTO)
     */
    @GetMapping
    @Operation(
        summary = "메모 조회",
        description = "특정 종목에 대한 사용자의 메모를 조회합니다. 메모가 없으면 빈 메모가 반환됩니다. JWT 토큰이 필요합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공 (메모가 있거나 없어도 200 반환)"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public ResponseEntity<MemoResponse> getMemo(
        @Parameter(description = "종목코드 (6자리)", example = "005930")
        @PathVariable String stockCode
    ) {
        // JWT 토큰에서 사용자 코드 추출
        String userCode = getUserCodeFromAuthentication();

        log.info("GET /api/favorites/{}/memo - userCode: {}", stockCode, userCode);

        // 메모 조회
        MemoResponse response = memoService.getMemo(userCode, stockCode);

        log.info("메모 조회 완료 - userCode: {}, stockCode: {}, 메모 있음: {}",
            userCode, stockCode, response.getContent() != null && !response.getContent().isEmpty());

        return ResponseEntity.ok(response);
    }

    /**
     * 메모 저장/수정
     *
     * 인증된 사용자가 특정 종목에 대한 메모를 저장하거나 수정합니다.
     * - 메모가 없으면 새로 생성됩니다.
     * - 메모가 있으면 내용이 업데이트됩니다.
     *
     * @param stockCode 종목코드 (6자리)
     * @param request 메모 저장 요청 (content - 최대 2000자)
     * @return 저장된 메모 정보 (MemoResponse DTO)
     */
    @PostMapping
    @Operation(
        summary = "메모 저장/수정",
        description = "특정 종목에 대한 메모를 저장하거나 수정합니다. 메모가 없으면 생성되고, 있으면 업데이트됩니다. JWT 토큰이 필요합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "저장 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (글자수 초과 등)"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @ApiResponse(responseCode = "422", description = "유효하지 않은 입력값")
    })
    public ResponseEntity<MemoResponse> saveMemo(
        @Parameter(description = "종목코드 (6자리)", example = "005930")
        @PathVariable String stockCode,
        @Valid @RequestBody MemoRequest request
    ) {
        // JWT 토큰에서 사용자 코드 추출
        String userCode = getUserCodeFromAuthentication();

        log.info("POST /api/favorites/{}/memo - userCode: {}, 메모 길이: {}",
            stockCode, userCode, request.getContent() != null ? request.getContent().length() : 0);

        try {
            // 메모 저장/수정
            MemoResponse response = memoService.saveMemo(userCode, stockCode, request);

            log.info("메모 저장 완료 - userCode: {}, stockCode: {}", userCode, stockCode);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("메모 저장 실패 - userCode: {}, error: {}", userCode, e.getMessage());
            throw e;
        }
    }

    /**
     * SecurityContext에서 인증된 사용자의 userCode 추출
     *
     * JWT 인증 필터가 SecurityContext에 설정한 인증 정보에서 사용자 코드를 가져옵니다.
     * UserDetails의 username이 userCode입니다.
     *
     * @return 사용자 코드 (userCode)
     * @throws IllegalStateException 인증되지 않은 경우 예외 발생
     */
    private String getUserCodeFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("인증되지 않은 요청");
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return userDetails.getUsername(); // username = userCode
        }

        log.error("예상하지 못한 Principal 타입: {}", principal.getClass().getName());
        throw new IllegalStateException("인증 정보를 가져올 수 없습니다.");
    }
}
