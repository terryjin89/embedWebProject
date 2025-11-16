package com.project.companyanalyzer.controller;

import com.project.companyanalyzer.dto.NewsSearchResponse;
import com.project.companyanalyzer.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 뉴스 검색 컨트롤러
 *
 * Naver 검색 API 연동을 통한 뉴스 검색 기능 제공
 * 프론트엔드와의 연결을 위한 REST API 엔드포인트
 */
@Slf4j
@Tag(name = "뉴스 API", description = "뉴스 검색 API")
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    /**
     * 뉴스 검색
     *
     * @param company 기업명 (필수)
     * @param hashtag 해시태그 (선택, 예: "실적", "투자", "신제품" 등)
     * @param page 페이지 번호 (기본값: 1, 1부터 시작)
     * @param size 페이지 크기 (기본값: 10, 최대: 100)
     * @param sort 정렬 옵션 (sim: 관련도순, date: 최신순, 기본값: date)
     * @return 뉴스 검색 결과
     */
    @Operation(
            summary = "뉴스 검색",
            description = "Naver 검색 API를 통해 기업 관련 뉴스를 검색합니다. " +
                    "기업명은 필수이며, 해시태그를 추가하여 더 구체적인 검색이 가능합니다. " +
                    "페이지네이션과 정렬 기능을 지원합니다."
    )
    @GetMapping("/search")
    public ResponseEntity<NewsSearchResponse> searchNews(
            @Parameter(description = "기업명 (필수)", example = "삼성전자", required = true)
            @RequestParam String company,
            @Parameter(description = "해시태그 (선택)", example = "반도체")
            @RequestParam(required = false) String hashtag,
            @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "페이지 크기 (최대 100)", example = "10")
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "정렬 옵션 (sim: 관련도순, date: 최신순)", example = "date")
            @RequestParam(defaultValue = "date") String sort
    ) {
        log.info("[NewsController] 뉴스 검색 요청 - company: {}, hashtag: {}, page: {}, size: {}, sort: {}",
                company, hashtag, page, size, sort);

        // 1. 기업명 검증
        if (company == null || company.trim().isEmpty()) {
            log.error("[NewsController] 기업명이 비어있습니다");
            throw new IllegalArgumentException("기업명을 입력해주세요");
        }

        // 2. 페이지 검증 (1 이상)
        if (page < 1) {
            log.warn("[NewsController] 잘못된 page 파라미터: {} - 기본값 1 사용", page);
            page = 1;
        }

        // 3. size 검증 (1 ~ 100)
        if (size < 1 || size > 100) {
            log.warn("[NewsController] 잘못된 size 파라미터: {} - 기본값 10 사용", size);
            size = 10;
        }

        // 4. sort 검증 (sim, date만 허용)
        if (!"sim".equals(sort) && !"date".equals(sort)) {
            log.warn("[NewsController] 잘못된 sort 파라미터: {} - 기본값 date 사용", sort);
            sort = "date";
        }

        // 5. 검색어 조합 (기업명 + 해시태그)
        String query = company;
        if (hashtag != null && !hashtag.trim().isEmpty()) {
            query = company + " " + hashtag.trim();
        }

        // 6. 페이지네이션 변환 (page, size → start, display)
        // Naver API는 start가 1부터 시작
        // page=1, size=10 → start=1, display=10
        // page=2, size=10 → start=11, display=10
        // page=3, size=10 → start=21, display=10
        Integer start = (page - 1) * size + 1;
        Integer display = size;

        // 7. Naver API 최대 검색 시작 위치 검증 (최대 1000)
        if (start > 1000) {
            log.warn("[NewsController] 검색 시작 위치가 1000을 초과합니다 - start: {}", start);
            throw new IllegalArgumentException("검색 결과는 최대 1000개까지만 조회할 수 있습니다");
        }

        try {
            // 8. NewsService 호출
            NewsSearchResponse response = newsService.searchNews(query, display, start, sort);

            log.info("[NewsController] 뉴스 검색 완료 - query: {}, 총 {}건, 표시 {}건, page: {}/{}",
                    query, response.getTotal(), response.getDisplay(), page,
                    (int) Math.ceil((double) response.getTotal() / size));

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.error("[NewsController] 잘못된 요청 - company: {}, hashtag: {}", company, hashtag, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("[NewsController] 뉴스 검색 실패 - company: {}, hashtag: {}", company, hashtag, e);
            throw e;
        }
    }
}
