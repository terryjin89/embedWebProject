package com.project.companyanalyzer.repository;

import com.project.companyanalyzer.entity.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CompanyRepository 테스트
 *
 * @DataJpaTest를 사용한 JPA Repository 단위 테스트
 * - H2 인메모리 데이터베이스 사용
 * - 각 테스트 메서드마다 트랜잭션 롤백
 */
@DataJpaTest
@ActiveProfiles("test")
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    private Company samsungElectronics;
    private Company skHynix;
    private Company hyundaiConstruction;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        companyRepository.deleteAll();

        // 삼성전자
        samsungElectronics = Company.builder()
                .corpCode("00126380")
                .corpName("삼성전자(주)")
                .corpNameEng("SAMSUNG ELECTRONICS CO,.LTD")
                .stockName("삼성전자")
                .stockCode("005930")
                .ceoNm("전영현")
                .corpCls("Y")
                .jurirNo("1301110006246")
                .bizrNo("1248100998")
                .adres("경기도 수원시 영통구 삼성로 129")
                .hmUrl("www.samsung.com/sec")
                .phnNo("02-2255-0114")
                .faxNo("031-200-7538")
                .indutyCode("264")
                .estDt("19690113")
                .accMt("12")
                .build();
        companyRepository.save(samsungElectronics);

        // SK하이닉스
        skHynix = Company.builder()
                .corpCode("00164779")
                .corpName("에스케이하이닉스(주)")
                .corpNameEng("SK hynix Inc.")
                .stockName("SK하이닉스")
                .stockCode("000660")
                .ceoNm("곽노정")
                .corpCls("Y")
                .indutyCode("2612")
                .estDt("19491015")
                .accMt("12")
                .build();
        companyRepository.save(skHynix);

        // 현대건설
        hyundaiConstruction = Company.builder()
                .corpCode("00164478")
                .corpName("현대건설(주)")
                .corpNameEng("HYUNDAI ENGINEERING & CONSTRUCTION CO.,LTD")
                .stockName("현대건설")
                .stockCode("000720")
                .ceoNm("이한우")
                .corpCls("Y")
                .indutyCode("41221")
                .estDt("19500110")
                .accMt("12")
                .build();
        companyRepository.save(hyundaiConstruction);
    }

    @Test
    @DisplayName("고유번호로 기업 조회")
    void testFindByCorpCode() {
        // when
        Optional<Company> found = companyRepository.findByCorpCode("00126380");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getCorpName()).isEqualTo("삼성전자(주)");
        assertThat(found.get().getStockCode()).isEqualTo("005930");
    }

    @Test
    @DisplayName("종목코드로 기업 조회")
    void testFindByStockCode() {
        // when
        Optional<Company> found = companyRepository.findByStockCode("000660");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getCorpName()).isEqualTo("에스케이하이닉스(주)");
    }

    @Test
    @DisplayName("정식명칭으로 검색 (LIKE)")
    void testFindByCorpNameContaining() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Company> result = companyRepository.findByCorpNameContaining("삼성", pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCorpName()).contains("삼성");
    }

    @Test
    @DisplayName("종목명으로 검색 (LIKE)")
    void testFindByStockNameContaining() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Company> result = companyRepository.findByStockNameContaining("SK", pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getStockName()).contains("SK");
    }

    @Test
    @DisplayName("업종코드로 필터링")
    void testFindByIndutyCode() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when - 업종코드 264 (전자부품, 컴퓨터, 영상, 음향 및 통신장비 제조업)
        Page<Company> result = companyRepository.findByIndutyCode("264", pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getIndutyCode()).isEqualTo("264");
    }

    @Test
    @DisplayName("법인구분으로 필터링 - 상장사만")
    void testFindByCorpCls() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when - 법인구분 Y (상장회사)
        Page<Company> result = companyRepository.findByCorpCls("Y", pageable);

        // then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent()).allMatch(c -> c.getCorpCls().equals("Y"));
    }

    @Test
    @DisplayName("상장사만 조회 (default 메서드)")
    void testFindListedCompanies() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Company> result = companyRepository.findListedCompanies(pageable);

        // then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent()).allMatch(c -> c.getCorpCls().equals("Y"));
    }

    @Test
    @DisplayName("업종코드와 법인구분으로 필터링")
    void testFindByIndutyCodeAndCorpCls() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when - 업종코드 264, 법인구분 Y
        Page<Company> result = companyRepository.findByIndutyCodeAndCorpCls("264", "Y", pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCorpName()).isEqualTo("삼성전자(주)");
    }

    @Test
    @DisplayName("복합 검색 - 키워드만")
    void testSearchCompanies_KeywordOnly() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when - "현대" 검색
        Page<Company> result = companyRepository.searchCompanies("현대", null, null, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCorpName()).contains("현대");
    }

    @Test
    @DisplayName("복합 검색 - 키워드 + 업종코드")
    void testSearchCompanies_KeywordAndIndustry() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when - "전자" 검색 + 업종코드 264
        Page<Company> result = companyRepository.searchCompanies("전자", "264", null, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCorpName()).contains("삼성전자");
    }

    @Test
    @DisplayName("복합 검색 - 모든 조건 null")
    void testSearchCompanies_AllNull() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when - 모든 조건 null (전체 조회)
        Page<Company> result = companyRepository.searchCompanies(null, null, null, pageable);

        // then
        assertThat(result.getContent()).hasSize(3);
    }

    @Test
    @DisplayName("기업명 또는 종목명으로 검색 (OR 조건)")
    void testFindByCorpNameOrStockName() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when - "삼성" 검색 (기업명 또는 종목명)
        Page<Company> result = companyRepository.findByCorpNameContainingOrStockNameContaining("삼성", "삼성", pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("업종코드로 기업 수 조회")
    void testCountByIndutyCode() {
        // when
        long count = companyRepository.countByIndutyCode("264");

        // then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("법인구분으로 기업 수 조회")
    void testCountByCorpCls() {
        // when
        long count = companyRepository.countByCorpCls("Y");

        // then
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("종목코드 존재 여부 확인")
    void testExistsByStockCode() {
        // when
        boolean exists = companyRepository.existsByStockCode("005930");
        boolean notExists = companyRepository.existsByStockCode("999999");

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("페이지네이션 - 첫 번째 페이지")
    void testPagination_FirstPage() {
        // given
        Pageable pageable = PageRequest.of(0, 2);

        // when
        Page<Company> result = companyRepository.findAll(pageable);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.isFirst()).isTrue();
    }

    @Test
    @DisplayName("페이지네이션 - 두 번째 페이지")
    void testPagination_SecondPage() {
        // given
        Pageable pageable = PageRequest.of(1, 2);

        // when
        Page<Company> result = companyRepository.findAll(pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.isLast()).isTrue();
    }
}
