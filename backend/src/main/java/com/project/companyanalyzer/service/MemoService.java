package com.project.companyanalyzer.service;

import com.project.companyanalyzer.dto.MemoRequest;
import com.project.companyanalyzer.dto.MemoResponse;
import com.project.companyanalyzer.entity.Memo;
import com.project.companyanalyzer.entity.Member;
import com.project.companyanalyzer.repository.MemberRepository;
import com.project.companyanalyzer.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * MemoService
 *
 * 메모 비즈니스 로직을 처리하는 서비스 클래스
 *
 * 주요 기능:
 * - 사용자별 메모 조회 (종목코드별)
 * - 메모 저장/수정
 * - 권한 검증 (본인 메모만 조회/수정)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemoService {

    private final MemoRepository memoRepository;
    private final MemberRepository memberRepository;

    /**
     * 메모 조회
     *
     * 사용자 코드와 종목코드를 기반으로 저장된 메모를 조회합니다.
     * 메모가 없으면 빈 메모 응답을 반환합니다.
     *
     * @param userCode 사용자 코드
     * @param stockCode 종목코드 (6자리)
     * @return 메모 응답 (MemoResponse DTO)
     */
    public MemoResponse getMemo(String userCode, String stockCode) {
        log.info("메모 조회 - userCode: {}, stockCode: {}", userCode, stockCode);

        // 메모 조회
        Optional<Memo> memoOptional = memoRepository.findByUserCodeAndStockCode(userCode, stockCode);

        if (memoOptional.isPresent()) {
            Memo memo = memoOptional.get();
            log.info("메모 조회 완료 - userCode: {}, stockCode: {}, 메모 길이: {}",
                userCode, stockCode, memo.getContent() != null ? memo.getContent().length() : 0);

            return MemoResponse.builder()
                .content(memo.getContent())
                .updatedAt(memo.getUpdatedAt())
                .build();
        } else {
            log.info("메모 없음 - userCode: {}, stockCode: {}", userCode, stockCode);

            // 메모가 없으면 빈 메모 응답 반환
            return MemoResponse.builder()
                .content("")
                .updatedAt(null)
                .build();
        }
    }

    /**
     * 메모 저장/수정
     *
     * 사용자가 특정 종목에 대한 메모를 저장하거나 수정합니다.
     * - 메모가 없으면 새로 생성
     * - 메모가 있으면 내용 업데이트
     *
     * @param userCode 사용자 코드
     * @param stockCode 종목코드 (6자리)
     * @param request 메모 저장 요청 (content)
     * @return 저장된 메모 정보 (MemoResponse DTO)
     * @throws IllegalArgumentException 사용자를 찾을 수 없는 경우 예외 발생
     */
    @Transactional
    public MemoResponse saveMemo(String userCode, String stockCode, MemoRequest request) {
        log.info("메모 저장 시도 - userCode: {}, stockCode: {}, 메모 길이: {}",
            userCode, stockCode, request.getContent() != null ? request.getContent().length() : 0);

        // 1. 기존 메모 조회
        Optional<Memo> memoOptional = memoRepository.findByUserCodeAndStockCode(userCode, stockCode);

        Memo memo;

        if (memoOptional.isPresent()) {
            // 2-1. 메모가 있으면 내용 업데이트
            memo = memoOptional.get();
            memo.setContent(request.getContent());

            log.info("기존 메모 업데이트 - userCode: {}, stockCode: {}", userCode, stockCode);
        } else {
            // 2-2. 메모가 없으면 새로 생성
            // Member 엔티티 조회
            Member member = memberRepository.findByUserCode(userCode)
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없음 - userCode: {}", userCode);
                    return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
                });

            // Memo 엔티티 생성
            memo = Memo.builder()
                .member(member)
                .stockCode(stockCode)
                .content(request.getContent())
                .build();

            log.info("새 메모 생성 - userCode: {}, stockCode: {}", userCode, stockCode);
        }

        // 3. 메모 저장
        Memo savedMemo = memoRepository.save(memo);

        log.info("메모 저장 완료 - id: {}, userCode: {}, stockCode: {}",
            savedMemo.getId(), userCode, stockCode);

        // 4. MemoResponse DTO로 변환하여 반환
        return MemoResponse.builder()
            .content(savedMemo.getContent())
            .updatedAt(savedMemo.getUpdatedAt())
            .message("메모가 성공적으로 저장되었습니다")
            .build();
    }
}
