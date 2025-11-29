package com.project.companyanalyzer.service;

import com.project.companyanalyzer.dto.AddFavoriteRequest;
import com.project.companyanalyzer.dto.CompanyDTO;
import com.project.companyanalyzer.dto.DeleteFavoriteResponse;
import com.project.companyanalyzer.dto.FavoriteResponse;
import com.project.companyanalyzer.entity.Company;
import com.project.companyanalyzer.entity.Member;
import com.project.companyanalyzer.entity.Stock;
import com.project.companyanalyzer.repository.CompanyRepository;
import com.project.companyanalyzer.repository.MemberRepository;
import com.project.companyanalyzer.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * FavoritesService
 *
 * 관심기업 비즈니스 로직을 처리하는 서비스 클래스
 *
 * 주요 기능:
 * - 사용자별 관심기업 목록 조회
 * - 관심기업 등록 (중복 체크)
 * - 관심기업 삭제
 *
 * 주가 정보는 프론트엔드가 직접 금융위원회 API를 호출하여 조회합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FavoritesService {

    private final StockRepository stockRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;

    /**
     * 관심기업 목록 조회
     *
     * 사용자 코드를 기반으로 등록된 관심기업 목록을 조회합니다.
     * 등록일시(registeredAt) 내림차순으로 정렬됩니다.
     *
     * @param userCode 사용자 코드
     * @return 관심기업 목록 (FavoriteResponse DTO 리스트)
     */
    public List<FavoriteResponse> getFavorites(String userCode) {
        log.info("관심기업 목록 조회 - userCode: {}", userCode);

        // 사용자별 관심기업 목록 조회 (Member, Company fetch join)
        List<Stock> stocks = stockRepository.findByUserCodeWithMemberAndCompany(userCode);

        log.info("조회된 관심기업 수: {}", stocks.size());

        // Stock 엔티티를 FavoriteResponse DTO로 변환
        return stocks.stream()
            .map(this::convertToFavoriteResponse)
            .collect(Collectors.toList());
    }

    /**
     * 관심기업 등록
     *
     * 사용자가 특정 기업을 관심기업으로 등록합니다.
     * DB에 없는 기업은 DART API를 통해 조회 후 저장합니다.
     * 비상장 기업(종목코드 없음)도 등록 가능합니다.
     *
     * @param userCode 사용자 코드
     * @param request 관심기업 등록 요청 (stockCode, corpCode)
     * @return 등록된 관심기업 정보 (FavoriteResponse DTO)
     * @throws IllegalArgumentException 중복 등록 또는 유효하지 않은 요청 시 예외 발생
     */
    @Transactional
    public FavoriteResponse addFavorite(String userCode, AddFavoriteRequest request) {
        log.info("관심기업 등록 시도 - userCode: {}, stockCode: {}, corpCode: {}",
            userCode, request.getStockCode(), request.getCorpCode());

        // 1. 중복 등록 체크 (corpCode 기반)
        Optional<Stock> existingStock = stockRepository.findByUserCodeAndCorpCode(userCode, request.getCorpCode());
        if (existingStock.isPresent()) {
            log.warn("이미 등록된 관심기업 - userCode: {}, corpCode: {}", userCode, request.getCorpCode());
            throw new IllegalArgumentException("이미 등록된 관심기업입니다.");
        }

        // 2. Member 엔티티 조회
        Member member = memberRepository.findByUserCode(userCode)
            .orElseThrow(() -> {
                log.error("사용자를 찾을 수 없음 - userCode: {}", userCode);
                return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
            });

        // 3. Company 엔티티 조회 또는 생성
        Company company = getOrCreateCompany(request.getCorpCode());

        // 4. stockCode 검증 (비상장 기업 고려)
        String requestStockCode = (request.getStockCode() == null || request.getStockCode().trim().isEmpty())
            ? null : request.getStockCode();
        String companyStockCode = company.getStockCode();

        // 둘 다 null이거나 같은 값이어야 함
        if (!isStockCodeMatching(requestStockCode, companyStockCode)) {
            log.error("종목코드 불일치 - corpCode: {}, requestStockCode: {}, companyStockCode: {}",
                request.getCorpCode(), requestStockCode, companyStockCode);
            throw new IllegalArgumentException("종목코드가 기업 정보와 일치하지 않습니다.");
        }

        // 5. Stock 엔티티 생성 및 저장
        Stock stock = Stock.builder()
            .member(member)
            .stockCode(requestStockCode)
            .company(company)
            .build();

        Stock savedStock = stockRepository.save(stock);

        log.info("관심기업 등록 완료 - id: {}, userCode: {}, corpCode: {}, stockCode: {}",
            savedStock.getId(), userCode, request.getCorpCode(), requestStockCode);

        // 6. FavoriteResponse DTO로 변환하여 반환
        return convertToFavoriteResponse(savedStock);
    }

    /**
     * Company 엔티티 조회 또는 생성
     * DB에 없으면 DART API를 통해 조회하고 저장합니다.
     *
     * @param corpCode 기업 코드
     * @return Company 엔티티
     */
    private Company getOrCreateCompany(String corpCode) {
        // DB에서 먼저 조회
        Optional<Company> companyOpt = companyRepository.findByCorpCode(corpCode);
        if (companyOpt.isPresent()) {
            log.debug("DB에서 기업 정보 조회 완료 - corpCode: {}", corpCode);
            return companyOpt.get();
        }

        // DB에 없으면 DART API를 통해 조회
        log.info("DB에 없음, DART API를 통해 기업 정보 조회 - corpCode: {}", corpCode);
        CompanyDTO companyDTO = companyService.getCompanyByCorpCode(corpCode);

        // CompanyDTO를 Company 엔티티로 변환하여 저장
        Company company = Company.builder()
            .corpCode(companyDTO.getCorpCode())
            .corpName(companyDTO.getCorpName())
            .corpNameEng(companyDTO.getCorpNameEng())
            .stockName(companyDTO.getStockName())
            .stockCode(companyDTO.getStockCode())
            .ceoNm(companyDTO.getCeoNm())
            .corpCls(companyDTO.getCorpCls())
            .jurirNo(companyDTO.getJurirNo())
            .bizrNo(companyDTO.getBizrNo())
            .adres(companyDTO.getAdres())
            .hmUrl(companyDTO.getHmUrl())
            .irUrl(companyDTO.getIrUrl())
            .phnNo(companyDTO.getPhnNo())
            .faxNo(companyDTO.getFaxNo())
            .indutyCode(companyDTO.getIndutyCode())
            .estDt(companyDTO.getEstDt())
            .accMt(companyDTO.getAccMt())
            .build();

        Company savedCompany = companyRepository.save(company);
        log.info("DART API에서 조회한 기업 정보 저장 완료 - corpCode: {}, corpName: {}",
            savedCompany.getCorpCode(), savedCompany.getCorpName());

        return savedCompany;
    }

    /**
     * 종목코드 일치 여부 확인
     * null 값 고려 (비상장 기업)
     */
    private boolean isStockCodeMatching(String requestStockCode, String companyStockCode) {
        // 둘 다 null인 경우 (비상장 기업)
        if (requestStockCode == null && companyStockCode == null) {
            return true;
        }
        // 하나만 null인 경우
        if (requestStockCode == null || companyStockCode == null) {
            return false;
        }
        // 둘 다 값이 있는 경우
        return requestStockCode.equals(companyStockCode);
    }

    /**
     * 관심기업 삭제
     *
     * 사용자가 특정 기업을 관심기업에서 삭제합니다.
     *
     * @param userCode 사용자 코드
     * @param stockCode 종목코드
     * @return 삭제 결과 (DeleteFavoriteResponse DTO)
     */
    @Transactional
    public DeleteFavoriteResponse deleteFavorite(String userCode, String stockCode) {
        log.info("관심기업 삭제 시도 - userCode: {}, stockCode: {}", userCode, stockCode);

        // 관심기업 삭제 (삭제된 행의 수 반환)
        int deletedCount = stockRepository.deleteByUserCodeAndStockCode(userCode, stockCode);

        if (deletedCount > 0) {
            log.info("관심기업 삭제 완료 - userCode: {}, stockCode: {}", userCode, stockCode);
            return DeleteFavoriteResponse.success("관심기업이 삭제되었습니다.");
        } else {
            log.warn("삭제할 관심기업을 찾을 수 없음 - userCode: {}, stockCode: {}", userCode, stockCode);
            return DeleteFavoriteResponse.failure("삭제할 관심기업을 찾을 수 없습니다.");
        }
    }

    /**
     * Stock 엔티티를 FavoriteResponse DTO로 변환
     *
     * @param stock Stock 엔티티
     * @return FavoriteResponse DTO
     */
    private FavoriteResponse convertToFavoriteResponse(Stock stock) {
        return FavoriteResponse.builder()
            .id(stock.getId())
            .stockCode(stock.getStockCode())
            .corpCode(stock.getCorpCode())
            .companyName(stock.getCompanyName())
            .stockName(stock.getStockName())
            .registeredAt(stock.getRegisteredAt())
            .build();
    }

    /**
     * 관심기업 개수 조회
     *
     * 사용자가 등록한 관심기업의 총 개수를 조회합니다.
     * (선택적 기능 - 향후 확장 가능)
     *
     * @param userCode 사용자 코드
     * @return 관심기업 개수
     */
    public long getFavoritesCount(String userCode) {
        return stockRepository.countByUserCode(userCode);
    }
}
