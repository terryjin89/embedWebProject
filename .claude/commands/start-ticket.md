# 티켓 시작 커맨드

Atlassian MCP 워크플로우를 따라 Jira 티켓 작업을 시작하는 명령어입니다.

## 프로세스

1. **Git 상태 확인**
   - `git status && git branch` 실행하여 현재 상태 확인
   - `gh auth status` 실행하여 GitHub CLI 인증 확인

2. **Jira 티켓 조회**
   - Atlassian MCP를 사용하여 embed-project의 사용 가능한 티켓 검색
   - 티켓 목록을 키, 요약, 상태와 함께 표시
   - 여러 티켓이 있는 경우 사용자에게 작업할 티켓 선택 요청

3. **티켓 상세 정보 표시**
   - 티켓 설명 표시
   - Acceptance Criteria 체크리스트 표시
   - 추가 컨텍스트 표시

4. **Feature 브랜치 생성**
   - 브랜치 이름 생성: `feature/[JIRA-ID]-[간단한-설명]`
   - 예시: `feature/EMBED-101-login-component`
   - 실행: `git checkout -b feature/[JIRA-ID]-[설명]`

5. **설정 확인**
   - `git branch`로 브랜치 생성 확인
   - 수행할 작업 요약
   - 개발 시작 준비 완료

## 출력 형식

```
🎫 Jira 티켓 작업 시작

📋 티켓 상세:
- 키: EMBED-XXX
- 요약: [티켓 요약]
- 상태: [현재 상태]

✅ Acceptance Criteria:
[ ] 조건 1
[ ] 조건 2
[ ] 조건 3

🌿 브랜치 생성됨: feature/EMBED-XXX-설명

✨ 개발 시작 준비 완료!
```

## 다음 단계

이 명령어 실행 후:
- Acceptance Criteria를 따라 구현 시작
- 설명적인 메시지로 점진적으로 커밋
- 커밋 메시지에 🎫 EMBED-XXX 포함
- 완료 시 `/check-acceptance` 실행하여 요구사항 확인
