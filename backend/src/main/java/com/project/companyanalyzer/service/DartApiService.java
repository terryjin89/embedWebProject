package com.project.companyanalyzer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.companyanalyzer.dto.DartCompanyResponse;
import com.project.companyanalyzer.dto.DartDisclosureResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * DART 전자공시시스템 API 연동 서비스
 *
 * DART(Data Analysis, Retrieval and Transfer System) API를 통해
 * 기업 정보와 공시 정보를 조회하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DartApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${dart.api.url:https://opendart.fss.or.kr/api}")
    private String apiUrl;

    @Value("${dart.api.key:d76b2823154aff2001264dd25f0cc7bf256c6c7b}")
    private String apiKey;

    @Value("${dart.api.mock-mode:true}")
    private boolean mockMode;

    /**
     * 기업 상세 정보 조회
     *
     * @param corpCode 고유번호 (8자리)
     * @return 기업 정보
     */
    public DartCompanyResponse getCompanyInfo(String corpCode) {
        log.info("[DART] 기업 정보 조회 - corpCode: {}, mockMode: {}", corpCode, mockMode);

        if (mockMode) {
            log.debug("[DART] 목업 모드 - 목업 데이터 반환");
            return loadMockCompanyData();
        }

        try {
            String url = buildCompanyApiUrl(corpCode);
            log.debug("[DART] API 호출 URL: {}", url);

            DartCompanyResponse response = restTemplate.getForObject(url, DartCompanyResponse.class);

            if (response != null && response.isSuccess()) {
                log.info("[DART] 기업 정보 조회 성공 - {}", response.getCorpName());
                return response;
            } else {
                String errorMsg = response != null ? response.getMessage() : "Unknown error";
                log.error("[DART] API 호출 실패 - status: {}, message: {}",
                    response != null ? response.getStatus() : "null", errorMsg);
                throw new RuntimeException("DART API 오류: " + errorMsg);
            }
        } catch (Exception e) {
            log.error("[DART] 기업 정보 조회 중 오류 발생 - corpCode: {}", corpCode, e);
            throw new RuntimeException("기업 정보 조회 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 공시 목록 조회
     *
     * @param corpCode 고유번호 (8자리)
     * @param bgnDe 시작일 (YYYYMMDD)
     * @param endDe 종료일 (YYYYMMDD)
     * @param pblntfTy 공시유형 (A~J, 선택사항)
     * @param pageNo 페이지 번호
     * @param pageCount 페이지당 건수
     * @return 공시 목록
     */
    public DartDisclosureResponse getDisclosures(
            String corpCode,
            String bgnDe,
            String endDe,
            String pblntfTy,
            Integer pageNo,
            Integer pageCount
    ) {
        log.info("[DART] 공시 목록 조회 - corpCode: {}, 기간: {}~{}, mockMode: {}",
            corpCode, bgnDe, endDe, mockMode);

        if (mockMode) {
            log.debug("[DART] 목업 모드 - 목업 데이터 반환");
            return loadMockDisclosureData();
        }

        try {
            String url = buildDisclosureApiUrl(corpCode, bgnDe, endDe, pblntfTy, pageNo, pageCount);
            log.debug("[DART] API 호출 URL: {}", url);

            DartDisclosureResponse response = restTemplate.getForObject(url, DartDisclosureResponse.class);

            if (response != null && response.isSuccess()) {
                log.info("[DART] 공시 목록 조회 성공 - 총 {}건 ({}페이지)",
                    response.getTotalCount(), response.getTotalPage());
                return response;
            } else {
                String errorMsg = response != null ? response.getMessage() : "Unknown error";
                log.error("[DART] API 호출 실패 - status: {}, message: {}",
                    response != null ? response.getStatus() : "null", errorMsg);
                throw new RuntimeException("DART API 오류: " + errorMsg);
            }
        } catch (Exception e) {
            log.error("[DART] 공시 목록 조회 중 오류 발생 - corpCode: {}", corpCode, e);
            throw new RuntimeException("공시 목록 조회 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 기업 정보 API URL 생성
     */
    private String buildCompanyApiUrl(String corpCode) {
        return UriComponentsBuilder.fromHttpUrl(apiUrl + "/company.json")
                .queryParam("crtfc_key", apiKey)
                .queryParam("corp_code", corpCode)
                .toUriString();
    }

    /**
     * 공시 목록 API URL 생성
     */
    private String buildDisclosureApiUrl(
            String corpCode,
            String bgnDe,
            String endDe,
            String pblntfTy,
            Integer pageNo,
            Integer pageCount
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/list.json")
                .queryParam("crtfc_key", apiKey)
                .queryParam("corp_code", corpCode)
                .queryParam("bgn_de", bgnDe)
                .queryParam("end_de", endDe)
                .queryParam("page_no", pageNo != null ? pageNo : 1)
                .queryParam("page_count", pageCount != null ? pageCount : 10);

        // 공시유형이 있으면 추가
        if (pblntfTy != null && !pblntfTy.isEmpty()) {
            builder.queryParam("pblntf_ty", pblntfTy);
        }

        return builder.toUriString();
    }

    /**
     * 목업 기업 정보 데이터 로드
     */
    private DartCompanyResponse loadMockCompanyData() {
        try {
            ClassPathResource resource = new ClassPathResource("dart-company-mock.json");
            return objectMapper.readValue(resource.getInputStream(), DartCompanyResponse.class);
        } catch (IOException e) {
            log.error("[DART] 목업 데이터 로드 실패", e);
            throw new RuntimeException("목업 데이터 로드 실패", e);
        }
    }

    /**
     * 목업 공시 목록 데이터 로드
     */
    private DartDisclosureResponse loadMockDisclosureData() {
        try {
            ClassPathResource resource = new ClassPathResource("dart-disclosure-mock.json");
            return objectMapper.readValue(resource.getInputStream(), DartDisclosureResponse.class);
        } catch (IOException e) {
            log.error("[DART] 목업 데이터 로드 실패", e);
            throw new RuntimeException("목업 데이터 로드 실패", e);
        }
    }
}
