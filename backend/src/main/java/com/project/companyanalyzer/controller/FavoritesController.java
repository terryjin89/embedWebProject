package com.project.companyanalyzer.controller;

import com.project.companyanalyzer.dto.AddFavoriteRequest;
import com.project.companyanalyzer.dto.DeleteFavoriteResponse;
import com.project.companyanalyzer.dto.FavoriteResponse;
import com.project.companyanalyzer.service.FavoritesService;
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

import java.util.List;

/**
 * 관심기업 API 컨트롤러
 *
 * 사용자의 관심기업 목록 조회, 등록, 삭제 기능을 제공합니다.
 * 모든 엔드포인트는 JWT 인증이 필요합니다.
 *
 * 엔드포인트:
 * - GET /api/favorites - 관심기업 목록 조회 (인증 필수)
 * - POST /api/favorites - 관심기업 등록 (인증 필수)
 * - DELETE /api/favorites/{stockCode} - 관심기업 삭제 (인증 필수)
 *
 * 주가 정보는 프론트엔드가 직접 금융위원회 API를 호출하여 조회합니다.
 */
@Slf4j
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorites")
@SecurityRequirement(name = "bearerAuth")
public class FavoritesController {

    private final FavoritesService favoritesService;

    /**
     * 관심기업 목록 조회
     *
     * 인증된 사용자의 관심기업 목록을 조회합니다.
     * 등록일시(registeredAt) 내림차순으로 정렬됩니다.
     *
     * 프론트엔드는 각 종목코드에 대해 금융위원회 API를 호출하여 주가 정보를 조회합니다.
     *
     * @return 관심기업 목록 (FavoriteResponse DTO 리스트)
     */
    @GetMapping
    @Operation(
        summary = "관심기업 목록 조회",
        description = "인증된 사용자의 관심기업 목록을 조회합니다. JWT 토큰이 필요합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public ResponseEntity<List<FavoriteResponse>> getFavorites() {
        // JWT 토큰에서 사용자 코드 추출
        String userCode = getUserCodeFromAuthentication();

        log.info("GET /api/favorites - userCode: {}", userCode);

        // 관심기업 목록 조회
        List<FavoriteResponse> favorites = favoritesService.getFavorites(userCode);

        log.info("관심기업 목록 조회 완료 - userCode: {}, count: {}", userCode, favorites.size());

        return ResponseEntity.ok(favorites);
    }

    /**
     * 관심기업 등록
     *
     * 인증된 사용자가 특정 기업을 관심기업으로 등록합니다.
     * 중복 등록을 방지하고, Company 엔티티 존재 여부를 확인합니다.
     *
     * @param request 관심기업 등록 요청 (stockCode, corpCode)
     * @return 등록된 관심기업 정보 (FavoriteResponse DTO)
     */
    @PostMapping
    @Operation(
        summary = "관심기업 등록",
        description = "특정 기업을 관심기업으로 등록합니다. 중복 등록이 방지됩니다. JWT 토큰이 필요합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "중복 등록 또는 잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @ApiResponse(responseCode = "422", description = "유효하지 않은 입력값")
    })
    public ResponseEntity<FavoriteResponse> addFavorite(
        @Valid @RequestBody AddFavoriteRequest request
    ) {
        // JWT 토큰에서 사용자 코드 추출
        String userCode = getUserCodeFromAuthentication();

        log.info("POST /api/favorites - userCode: {}, stockCode: {}, corpCode: {}",
            userCode, request.getStockCode(), request.getCorpCode());

        try {
            // 관심기업 등록
            FavoriteResponse response = favoritesService.addFavorite(userCode, request);

            log.info("관심기업 등록 완료 - userCode: {}, stockCode: {}", userCode, request.getStockCode());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            log.warn("관심기업 등록 실패 - userCode: {}, error: {}", userCode, e.getMessage());
            throw e;
        }
    }

    /**
     * 관심기업 삭제
     *
     * 인증된 사용자가 특정 기업을 관심기업에서 삭제합니다.
     *
     * @param stockCode 종목코드 (6자리)
     * @return 삭제 결과 (DeleteFavoriteResponse DTO)
     */
    @DeleteMapping("/{stockCode}")
    @Operation(
        summary = "관심기업 삭제",
        description = "특정 기업을 관심기업에서 삭제합니다. JWT 토큰이 필요합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @ApiResponse(responseCode = "404", description = "관심기업을 찾을 수 없음")
    })
    public ResponseEntity<DeleteFavoriteResponse> deleteFavorite(
        @Parameter(description = "종목코드 (6자리)", example = "005930")
        @PathVariable String stockCode
    ) {
        // JWT 토큰에서 사용자 코드 추출
        String userCode = getUserCodeFromAuthentication();

        log.info("DELETE /api/favorites/{} - userCode: {}", stockCode, userCode);

        // 관심기업 삭제
        DeleteFavoriteResponse response = favoritesService.deleteFavorite(userCode, stockCode);

        if (response.isSuccess()) {
            log.info("관심기업 삭제 완료 - userCode: {}, stockCode: {}", userCode, stockCode);
            return ResponseEntity.ok(response);
        } else {
            log.warn("관심기업 삭제 실패 - userCode: {}, stockCode: {}, message: {}",
                userCode, stockCode, response.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
