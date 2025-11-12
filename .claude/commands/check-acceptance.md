# Acceptance Criteria 체크 커맨드

Jira 티켓의 모든 Acceptance Criteria가 구현되었는지 확인하는 명령어입니다.

## 프로세스

1. **현재 티켓 식별**
   - 현재 브랜치 이름에서 Jira 티켓 ID 추출
   - 예시: `feature/EMBED-101-login` → `EMBED-101`

2. **티켓 상세 정보 가져오기**
   - Atlassian MCP를 사용하여 티켓 정보 조회
   - Acceptance Criteria 체크리스트 추출

3. **구현 검토**
   - 각 Acceptance Criterion별로:
     - 요구사항 표시
     - 구현 완료 여부 확인
     - 필요시 관련 코드/파일 확인
     - 완료 또는 미완료로 표시

4. **리포트 생성**
   - 완료 상태 표시
   - 완료된 항목 목록
   - 미완료 항목 목록 (있는 경우)
   - 다음 작업 권장사항

5. **결정 지점**
   - 모두 완료: `/test-feature` 실행 제안
   - 미완료: 남은 작업 목록 제시

## 출력 형식

```
🎫 EMBED-XXX Acceptance Criteria 확인 중

📋 Acceptance Criteria 검토:

✅ [조건 1] - 구현 완료
   └─ 확인 위치: src/components/Login.jsx

✅ [조건 2] - 구현 완료
   └─ 확인 위치: src/services/authService.js

⚠️  [조건 3] - 미구현
   └─ 누락: 네트워크 오류 에러 핸들링

📊 진행률: 2/3 (66%)

💡 다음 단계:
- 남은 조건 완료: 에러 핸들링
- 완료 후 /test-feature 실행하여 구현 검증
```

## 검증 방법

- **코드 검사**: 관련 파일/함수 존재 여부 확인
- **파일 구조**: 컴포넌트/서비스 조직 확인
- **사용자 확인**: 사용자에게 구현 세부사항 확인 요청

## 다음 단계

- 모든 조건 충족: `/test-feature` 실행
- 미완료: 개발 계속 진행 후 이 명령어 재실행
