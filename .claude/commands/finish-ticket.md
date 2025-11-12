# 티켓 완료 커맨드

Pull Request를 생성하고 병합하며 Jira를 업데이트하여 티켓 워크플로우를 완료하는 명령어입니다.

## 프로세스

1. **사전 점검**
   - 모든 커밋이 완료되었는지 확인
   - `git status` 실행하여 커밋되지 않은 변경사항 확인
   - Acceptance Criteria 충족 확인
   - 테스트 통과 확인

2. **Feature 브랜치 푸시**
   - 실행: `git push -u origin feature/EMBED-XXX-설명`
   - 푸시 성공 확인

3. **Pull Request 생성**
   - GitHub CLI를 사용하여 PR 생성
   - 포함 내용:
     - Conventional commit 형식의 설명적 제목
     - 구현 요약
     - Jira 티켓 참조 (🎫 EMBED-XXX)
     - Acceptance Criteria 체크리스트 (모두 체크됨)
     - 테스트 결과 요약
   - 예시:
     ```bash
     gh pr create --title "feat(auth): 로그인 컴포넌트 구현" \
       --body "[생성된 PR 설명]"
     ```

4. **Pull Request 병합**
   - 개인 프로젝트용: squash로 직접 병합
   - 실행: `gh pr merge --squash --delete-branch`
   - 병합 성공 확인

5. **로컬 저장소 업데이트**
   - main으로 전환: `git checkout main`
   - 최신 변경사항 가져오기: `git pull origin main`
   - 정리: feature 브랜치 삭제 확인

6. **Jira 티켓 업데이트** (선택사항)
   - Atlassian MCP를 사용하여 티켓을 "Done"으로 전환
   - PR 링크와 완료 요약이 포함된 코멘트 추가

7. **완료 요약**
   - 완료된 작업 표시
   - 모든 단계 완료 확인
   - 다음 티켓 준비 완료

## 출력 형식

```
🎯 티켓 완료 중: EMBED-XXX

📤 브랜치 푸시 중...
✅ origin/feature/EMBED-XXX-login으로 푸시됨

📝 Pull Request 생성 중...
✅ PR #123 생성됨: feat(auth): 로그인 컴포넌트 구현
   URL: https://github.com/user/repo/pull/123

🔀 Pull Request 병합 중...
✅ PR 병합됨 및 브랜치 삭제됨

🔄 로컬 저장소 업데이트 중...
✅ main 브랜치로 전환됨
✅ 최신 변경사항 가져옴

🎫 Jira 티켓 업데이트 중...
✅ EMBED-XXX를 Done으로 전환함

✨ 티켓 완료!

📊 요약:
- 기능: 로그인 컴포넌트 구현
- 커밋: 5개
- 변경된 파일: 8개
- 테스트: 모두 통과
- PR: #123 (병합됨)

💡 다음 티켓 준비 완료! /start-ticket 실행하여 시작하세요.
```

## PR 설명 템플릿

```markdown
## 요약
[한국어로 구현 요약]

## Jira 티켓
🎫 EMBED-XXX

## Acceptance Criteria 체크리스트
- [x] 조건 1
- [x] 조건 2
- [x] 조건 3

## 구현 내용
- 컴포넌트 구현
- 서비스 연동
- 에러 핸들링

## 테스트 결과
- [x] Playwright E2E 테스트 통과
- [x] API 호출 테스트 통과
- [x] 에러 케이스 검증 완료

## 스크린샷
[해당되는 경우]
```

## 안전 점검

병합 전:
- ❓ 모든 Acceptance Criteria가 충족되었나요?
- ❓ 모든 테스트가 통과했나요?
- ❓ 커밋되지 않은 변경사항이 있나요?
- ❓ PR 설명이 완전한가요?

하나라도 "아니오"인 경우 → 중단하고 문제 해결

## 다음 단계

완료 후:
- `/start-ticket`으로 다음 티켓 시작
- 또는 휴식 - 수고하셨습니다! 🎉
