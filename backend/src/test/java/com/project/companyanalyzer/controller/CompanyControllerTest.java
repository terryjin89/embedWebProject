package com.project.companyanalyzer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.companyanalyzer.dto.CompanyDTO;
import com.project.companyanalyzer.dto.CompanyListResponse;
import com.project.companyanalyzer.dto.DartDisclosureResponse;
import com.project.companyanalyzer.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CompanyController 테스트
 *
 * @WebMvcTest를 사용한 슬라이스 테스트
 * - MockMvc를 통한 HTTP 요청/응답 검증
 * - Service 계층은 Mock 처리
 */
@WebMvcTest(
        controllers = CompanyController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        com.project.companyanalyzer.config.SecurityConfig.class,
                        com.project.companyanalyzer.security.JwtAuthenticationFilter.class
                }
        )
)
@AutoConfigureMockMvc(addFilters = false)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompanyService companyService;

    private CompanyDTO samsungDTO;
    private CompanyListResponse companyListResponse;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        samsungDTO = CompanyDTO.builder()
                .corpCode("00126380")
                .corpName("삼성전자(주)")
                .corpNameEng("SAMSUNG ELECTRONICS CO,.LTD")
                .stockName("삼성전자")
                .stockCode("005930")
                .ceoNm("전영현")
                .corpCls("Y")
                .corpClsName("유가증권시장")
                .indutyCode("264")
                .estDt("19690113")
                .estDtFormatted("1969-01-13")
                .accMt("12")
                .build();

        companyListResponse = CompanyListResponse.builder()
                .companies(Arrays.asList(samsungDTO))
                .currentPage(0)
                .pageSize(10)
                .totalElements(1L)
                .totalPages(1)
                .isFirst(true)
                .isLast(true)
                .hasNext(false)
                .hasPrevious(false)
                .build();
    }

    @Test
    @DisplayName("GET /companies - 전체 기업 목록 조회")
    void testGetAllCompanies() throws Exception {
        // given
        given(companyService.getAllCompanies(any(Pageable.class)))
                .willReturn(companyListResponse);

        // when & then
        mockMvc.perform(get("/companies")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.companies").isArray())
                .andExpect(jsonPath("$.companies[0].corpCode").value("00126380"))
                .andExpect(jsonPath("$.companies[0].corpName").value("삼성전자(주)"))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @DisplayName("GET /companies - 키워드 검색")
    void testSearchCompaniesByKeyword() throws Exception {
        // given
        given(companyService.searchCompanies(eq("삼성"), isNull(), isNull(), any(Pageable.class)))
                .willReturn(companyListResponse);

        // when & then
        mockMvc.perform(get("/companies")
                        .param("keyword", "삼성")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companies").isArray())
                .andExpect(jsonPath("$.companies[0].corpName").value("삼성전자(주)"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("GET /companies - 업종코드 필터링")
    void testSearchCompaniesByIndustry() throws Exception {
        // given
        given(companyService.searchCompanies(isNull(), eq("264"), isNull(), any(Pageable.class)))
                .willReturn(companyListResponse);

        // when & then
        mockMvc.perform(get("/companies")
                        .param("indutyCode", "264")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companies[0].indutyCode").value("264"));
    }

    @Test
    @DisplayName("GET /companies - 법인구분 필터링")
    void testSearchCompaniesByCorpCls() throws Exception {
        // given
        given(companyService.searchCompanies(isNull(), isNull(), eq("Y"), any(Pageable.class)))
                .willReturn(companyListResponse);

        // when & then
        mockMvc.perform(get("/companies")
                        .param("corpCls", "Y")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companies[0].corpCls").value("Y"))
                .andExpect(jsonPath("$.companies[0].corpClsName").value("유가증권시장"));
    }

    @Test
    @DisplayName("GET /companies - 복합 검색 (키워드 + 업종 + 법인구분)")
    void testSearchCompaniesWithMultipleFilters() throws Exception {
        // given
        given(companyService.searchCompanies(eq("삼성"), eq("264"), eq("Y"), any(Pageable.class)))
                .willReturn(companyListResponse);

        // when & then
        mockMvc.perform(get("/companies")
                        .param("keyword", "삼성")
                        .param("indutyCode", "264")
                        .param("corpCls", "Y")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companies[0].corpName").value("삼성전자(주)"))
                .andExpect(jsonPath("$.companies[0].indutyCode").value("264"))
                .andExpect(jsonPath("$.companies[0].corpCls").value("Y"));
    }

    @Test
    @DisplayName("GET /companies/{corpCode} - 기업 상세 조회")
    void testGetCompanyByCorpCode() throws Exception {
        // given
        given(companyService.getCompanyByCorpCode("00126380"))
                .willReturn(samsungDTO);

        // when & then
        mockMvc.perform(get("/companies/00126380")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.corpCode").value("00126380"))
                .andExpect(jsonPath("$.corpName").value("삼성전자(주)"))
                .andExpect(jsonPath("$.stockCode").value("005930"))
                .andExpect(jsonPath("$.corpClsName").value("유가증권시장"))
                .andExpect(jsonPath("$.estDtFormatted").value("1969-01-13"));
    }

    @Test
    @DisplayName("GET /companies/{corpCode} - 존재하지 않는 기업 조회")
    void testGetCompanyByCorpCode_NotFound() throws Exception {
        // given
        given(companyService.getCompanyByCorpCode("99999999"))
                .willThrow(new RuntimeException("기업 정보를 찾을 수 없습니다. corpCode: 99999999"));

        // when & then
        mockMvc.perform(get("/companies/99999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("GET /companies/{corpCode}/disclosures - 공시 목록 조회")
    void testGetCompanyDisclosures() throws Exception {
        // given
        DartDisclosureResponse disclosureResponse = DartDisclosureResponse.builder()
                .status("000")
                .message("정상")
                .totalCount(10)
                .totalPage(1)
                .pageNo(1)
                .pageCount(10)
                .list(Collections.emptyList())
                .build();

        given(companyService.getCompanyDisclosures(
                eq("00126380"), anyString(), anyString(), isNull(), eq(1), eq(10)))
                .willReturn(disclosureResponse);

        // when & then
        mockMvc.perform(get("/companies/00126380/disclosures")
                        .param("pageNo", "1")
                        .param("pageCount", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("000"))
                .andExpect(jsonPath("$.message").value("정상"))
                .andExpect(jsonPath("$.total_count").value(10))
                .andExpect(jsonPath("$.page_no").value(1));
    }

    @Test
    @DisplayName("GET /companies - 페이지네이션 검증")
    void testPagination() throws Exception {
        // given
        CompanyListResponse secondPageResponse = CompanyListResponse.builder()
                .companies(Arrays.asList(samsungDTO))
                .currentPage(1)
                .pageSize(5)
                .totalElements(20L)
                .totalPages(4)
                .isFirst(false)
                .isLast(false)
                .hasNext(true)
                .hasPrevious(true)
                .build();

        given(companyService.getAllCompanies(any(Pageable.class)))
                .willReturn(secondPageResponse);

        // when & then
        mockMvc.perform(get("/companies")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(1))
                .andExpect(jsonPath("$.pageSize").value(5))
                .andExpect(jsonPath("$.totalElements").value(20))
                .andExpect(jsonPath("$.totalPages").value(4))
                .andExpect(jsonPath("$.isFirst").value(false))
                .andExpect(jsonPath("$.isLast").value(false))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.hasPrevious").value(true));
    }
}
