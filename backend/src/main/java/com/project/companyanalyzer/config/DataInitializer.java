package com.project.companyanalyzer.config;

import com.project.companyanalyzer.entity.Company;
import com.project.companyanalyzer.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 애플리케이션 시작 시 기업 데이터를 초기화하는 설정 클래스
 * SCRUM-8: 기업정보 게시판용 초기 데이터 자동 생성
 *
 * 📝 문서 참고: readme/companyInfoFunction.md
 *    - "한글 인코딩 문제 해결" 섹션 (488-551라인)
 *    - "DataInitializer.java" 섹션 (451-485라인)
 *
 * 한글 인코딩 문제 해결을 위해 SQL 파일 대신 Java CommandLineRunner 사용:
 * - SQL 파일은 Windows에서 생성 시 인코딩 문제 발생 가능
 * - Java 코드는 JVM의 UTF-8 인코딩을 사용하여 안전
 * - 애플리케이션 시작 시 자동으로 10개 주요 기업 데이터 삽입
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 애플리케이션 시작 시 실행되어 기업 데이터를 자동으로 삽입
     * SQL 파일의 인코딩 문제를 해결하기 위해 Java 코드로 직접 데이터 삽입
     */
    @Bean
    CommandLineRunner initDatabase(CompanyRepository companyRepository) {
        return args -> {
            // 데이터가 이미 존재하면 초기화하지 않음
            if (companyRepository.count() > 0) {
                log.info("기업 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
                return;
            }

            log.info("기업 데이터 초기화 시작...");

            // 1. 삼성전자(주)
            saveCompany(companyRepository, Company.builder()
                    .corpCode("00126380")
                    .corpName("삼성전자")
                    .corpNameEng("SAMSUNG ELECTRONICS CO,.LTD")
                    .stockName("삼성전자")
                    .stockCode("005930")
                    .ceoNm("전영현")
                    .corpCls("Y")
                    .jurirNo("1301110006246")
                    .bizrNo("1248100998")
                    .adres("경기도 수원시 영통구 삼성로 129 (매탄동)")
                    .hmUrl("www.samsung.com/sec")
                    .irUrl("")
                    .phnNo("02-2255-0114")
                    .faxNo("031-200-7538")
                    .indutyCode("264")
                    .estDt("19690113")
                    .accMt("12")
                    .build());

            // 2. SK하이닉스(주)
            saveCompany(companyRepository, Company.builder()
                    .corpCode("00164779")
                    .corpName("SK하이닉스")
                    .corpNameEng("SK hynix Inc.")
                    .stockName("SK하이닉스")
                    .stockCode("000660")
                    .ceoNm("곽노정")
                    .corpCls("Y")
                    .jurirNo("1344110001387")
                    .bizrNo("1268103725")
                    .adres("경기도 이천시 부발읍 경충대로 2091")
                    .hmUrl("www.skhynix.com")
                    .irUrl("")
                    .phnNo("031-630-4114")
                    .faxNo("031-645-8078")
                    .indutyCode("2612")
                    .estDt("19491015")
                    .accMt("12")
                    .build());

            // 3. HD현대중공업(주)
            saveCompany(companyRepository, Company.builder()
                    .corpCode("01390344")
                    .corpName("HD현대중공업")
                    .corpNameEng("HD HYUNDAI HEAVY INDUSTRIES CO.,LTD.")
                    .stockName("HD현대중공업")
                    .stockCode("329180")
                    .ceoNm("이상균, 노진율")
                    .corpCls("Y")
                    .jurirNo("2301110312741")
                    .bizrNo("2528701412")
                    .adres("울산광역시 동구 방어진순환도로 1000")
                    .hmUrl("www.hhi.co.kr")
                    .irUrl("")
                    .phnNo("052-202-2114")
                    .faxNo("052-202-3315")
                    .indutyCode("31113")
                    .estDt("20190603")
                    .accMt("12")
                    .build());

            // 4. 한화오션(주)
            saveCompany(companyRepository, Company.builder()
                    .corpCode("00111704")
                    .corpName("한화오션")
                    .corpNameEng("Hanwha Ocean Co., Ltd.")
                    .stockName("한화오션")
                    .stockCode("042660")
                    .ceoNm("김희철")
                    .corpCls("Y")
                    .jurirNo("1101112095837")
                    .bizrNo("1048157667")
                    .adres("경상남도 거제시 거제대로 3370 (아주동)")
                    .hmUrl("www.hanwhaocean.com/pub/main/index.do")
                    .irUrl("")
                    .phnNo("055-735-2114")
                    .faxNo("02-2129-0084")
                    .indutyCode("3111")
                    .estDt("20001023")
                    .accMt("12")
                    .build());

            // 5. 현대건설(주)
            saveCompany(companyRepository, Company.builder()
                    .corpCode("00164478")
                    .corpName("현대건설")
                    .corpNameEng("HYUNDAI ENGINEERING & CONSTRUCTION CO.,LTD")
                    .stockName("현대건설")
                    .stockCode("000720")
                    .ceoNm("이한우")
                    .corpCls("Y")
                    .jurirNo("1101110007909")
                    .bizrNo("1018116293")
                    .adres("서울특별시 종로구 율곡로 75")
                    .hmUrl("www.hdec.kr")
                    .irUrl("https://www.hdec.kr/kr/invest/irpt.aspx")
                    .phnNo("02-746-1114")
                    .faxNo("02-746-4846")
                    .indutyCode("41221")
                    .estDt("19500110")
                    .accMt("12")
                    .build());

            // 6. 삼성물산(주)
            saveCompany(companyRepository, Company.builder()
                    .corpCode("00149655")
                    .corpName("삼성물산")
                    .corpNameEng("SAMSUNG C&T CORPORATION")
                    .stockName("삼성물산")
                    .stockCode("028260")
                    .ceoNm("오세철")
                    .corpCls("Y")
                    .jurirNo("1301110006361")
                    .bizrNo("1068600606")
                    .adres("서울특별시 서초구 서초대로74길 11 (서초동)")
                    .hmUrl("www.samsungcnt.com")
                    .irUrl("")
                    .phnNo("02-2145-5114")
                    .faxNo("")
                    .indutyCode("46")
                    .estDt("19380101")
                    .accMt("12")
                    .build());

            // 7. 포스코(주)
            saveCompany(companyRepository, Company.builder()
                    .corpCode("01620971")
                    .corpName("포스코")
                    .corpNameEng("POSCO")
                    .stockName("포스코")
                    .stockCode("")
                    .ceoNm("이희근")
                    .corpCls("E")
                    .jurirNo("1717110171410")
                    .bizrNo("3018702315")
                    .adres("경상북도 포항시 남구 동해안로 6261")
                    .hmUrl("www.posco.co.kr")
                    .irUrl("")
                    .phnNo("054-220-0114")
                    .faxNo("054-220-6000")
                    .indutyCode("2411")
                    .estDt("20220302")
                    .accMt("12")
                    .build());

            // 8. NHN(주)
            saveCompany(companyRepository, Company.builder()
                    .corpCode("00983271")
                    .corpName("NHN")
                    .corpNameEng("NHN Corporation")
                    .stockName("NHN")
                    .stockCode("181710")
                    .ceoNm("정우진")
                    .corpCls("Y")
                    .jurirNo("1101111050178")
                    .bizrNo("2208137109")
                    .adres("경기도 성남시 분당구 대왕판교로645번길 16")
                    .hmUrl("www.nhn.com")
                    .irUrl("")
                    .phnNo("1544-6859")
                    .faxNo("")
                    .indutyCode("582")
                    .estDt("20130529")
                    .accMt("12")
                    .build());

            // 9. 카카오(주)
            saveCompany(companyRepository, Company.builder()
                    .corpCode("00258801")
                    .corpName("카카오")
                    .corpNameEng("Kakao Corp.")
                    .stockName("카카오")
                    .stockCode("035720")
                    .ceoNm("정신아")
                    .corpCls("Y")
                    .jurirNo("1301110183166")
                    .bizrNo("1208800767")
                    .adres("경기도 성남시 분당구 판교역로 235 (삼평동)")
                    .hmUrl("www.kakaocorp.com")
                    .irUrl("")
                    .phnNo("1577-3754")
                    .faxNo("")
                    .indutyCode("5820")
                    .estDt("19950216")
                    .accMt("12")
                    .build());

            // 10. 고려아연(주)
            saveCompany(companyRepository, Company.builder()
                    .corpCode("00102858")
                    .corpName("고려아연")
                    .corpNameEng("KOREA ZINC INC")
                    .stockName("고려아연")
                    .stockCode("010130")
                    .ceoNm("박기덕, 정태웅")
                    .corpCls("Y")
                    .jurirNo("1101110168404")
                    .bizrNo("2118111260")
                    .adres("서울특별시 종로구 종로 33 (청진동)")
                    .hmUrl("www.koreazinc.co.kr")
                    .irUrl("")
                    .phnNo("02-6947-2114")
                    .faxNo("02-549-8245")
                    .indutyCode("24213")
                    .estDt("19740801")
                    .accMt("12")
                    .build());

            log.info("기업 데이터 초기화 완료: 총 {} 개 기업", companyRepository.count());
        };
    }

    /**
     * 기업 데이터를 데이터베이스에 저장 (중복 체크 후 저장)
     */
    private void saveCompany(CompanyRepository repository, Company company) {
        if (!repository.existsByCorpCode(company.getCorpCode())) {
            repository.save(company);
            log.debug("기업 저장: {} ({})", company.getCorpName(), company.getCorpCode());
        }
    }
}
