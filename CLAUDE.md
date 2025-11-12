# CLAUDE.md

이 파일은 Claude Code(claude.ai/code)가 이 저장소의 코드를 작업할 때 참고하는 가이드입니다.

## 프로젝트 개요

**기업분석 웹페이지** - 임베디드융합개발자 과정 프로젝트를 위한 기업 분석 웹 애플리케이션입니다. 이 플랫폼을 통해 사용자는 재무 데이터, 공시 정보, 주가, 뉴스 등 기업 관련 정보를 관리하고 분석할 수 있습니다.

**타겟 사용자**: 투자 결정을 위해 기업 재무 정보, 공시 데이터, 주가 동향을 모니터링해야 하는 비즈니스 전문가 및 투자자

## 기술 스택

### 프론트엔드 (React 18.x + Vite)
- **프레임워크**: React 19.2.0 with Vite 7.2.2
- **라우팅**: React Router DOM v7
- **HTTP 클라이언트**: Axios 1.13.2
- **차트**: Recharts 3.4.1 (주가 시각화용)
- **코드 품질**: ESLint 9.39.1 + Prettier 3.6.2

### 백엔드 (Spring Boot - 미구현)
- Spring Boot 3.x with JDK 17
- Spring Security + JWT 인증
- MyBatis/Maven
- MySQL 8.0.xx 데이터베이스

### 외부 API
- **DART**: 전자공시시스템 (Electronic Disclosure System)
- **금융위원회**: 주식시세정보 API (Stock market data)
- **한국수출입은행**: 환율 API (Exchange rate data)
- **Naver**: 검색 API (News search)

## 개발 명령어

### 프론트엔드 (React + Vite)
```bash
cd frontend

# 의존성 설치
npm install

# 개발 서버 실행 (http://localhost:5173)
npm run dev

# 프로덕션 빌드
npm run build

# 프로덕션 빌드 미리보기
npm run preview

# 린트 체크
npm run lint
```

### 백엔드 (Spring Boot - 구현 시)
```bash
cd backend

# Spring Boot 애플리케이션 실행
./mvnw spring-boot:run

# http://localhost:8080 에서 실행 예정
```

### 데이터베이스 설정
```sql
CREATE DATABASE company_analyzer
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

## 환경 설정

### 프론트엔드 환경 변수
`frontend/` 디렉토리에서 `.env.example`을 `.env`로 복사:

```bash
cd frontend
cp .env.example .env
```

필수 변수:
- `VITE_API_BASE_URL`: 백엔드 API URL (기본값: http://localhost:8080/api)
- `VITE_DART_API_KEY`: DART API 키 (https://opendart.fss.or.kr/ 에서 발급)
- `VITE_NAVER_CLIENT_ID`: Naver API 클라이언트 ID
- `VITE_NAVER_CLIENT_SECRET`: Naver API 클라이언트 시크릿
- `VITE_ENV`: 환경 (development/production)

**참고**: Vite에서는 `import.meta.env.VITE_*`를 사용하여 환경 변수에 접근합니다

## 프로젝트 구조

### 프론트엔드 디렉토리 구조
```
frontend/
├── src/
│   ├── components/     # 재사용 가능한 UI 컴포넌트 (미구현 - 구현 예정)
│   ├── pages/          # 페이지 컴포넌트 (미구현 - 구현 예정)
│   ├── services/       # API 호출 로직 (미구현 - 구현 예정)
│   ├── utils/          # 유틸리티 함수
│   ├── App.jsx         # 메인 App 컴포넌트 (현재 기본 Vite 템플릿)
│   └── main.jsx        # 애플리케이션 진입점
├── public/             # 정적 파일
├── .env                # 환경 변수 (커밋 제외)
└── .env.example        # 환경 변수 템플릿
```

### 구현할 주요 기능

**인증 시스템**:
- JWT 기반 사용자 등록 및 로그인
- 전역 인증 상태를 위한 AuthContext
- 토큰 관리를 위한 Axios 인터셉터

**게시판 기능** (모두 테이블 기반 레이아웃 사용):
1. **경제지표 게시판**: 경제 지표 (환율 데이터)
2. **기업정보 게시판**: 기업 정보 (DART API 연동)
3. **관심기업 게시판**: 관심 기업 목록
4. **관심기업 상세페이지**: 탭 기반 상세 대시보드:
   - 공시정보 (Disclosure info)
   - 주가차트 (Recharts Area Chart를 사용한 주가 차트)
   - 관련기사 (Naver Search API를 통한 관련 뉴스)
   - 메모 (사용자 메모)

**주가 차트 기능**:
- 금융위원회 주식시세정보 API 연동
- Recharts Area Chart를 사용한 시각화
- 기간 선택: 30일/60일/90일

**뉴스 검색**:
- Naver Search API 연동
- 해시태그 기반 검색 기능

### 백엔드 구조 (구현 예정)
```
backend/
├── src/main/java/
│   ├── controller/     # REST API 엔드포인트
│   ├── service/        # 비즈니스 로직
│   ├── repository/     # JPA/MyBatis 리포지토리
│   ├── entity/         # 데이터베이스 엔티티
│   ├── dto/            # Data Transfer Objects
│   ├── config/         # Spring Security, JWT 설정
│   └── exception/      # GlobalExceptionHandler
└── src/main/resources/
    └── application.yml # 설정 파일
```

### 데이터베이스 스키마 (구현 예정)
- **Member**: 사용자 계정
- **Company**: 기업 정보
- **Stock**: 사용자별 관심 기업
- **Memo**: 기업에 대한 사용자 메모

## 디자인 시스템

**참고 디자인**: 한국수출입은행(Korea Eximbank) 및 IBK기업은행(IBK) 웹사이트의 전문 금융 서비스 UI에서 영감을 받음

**컬러 팔레트**: primary, secondary, success, warning, danger, neutral 톤의 전문 금융 서비스 테마 (자세한 내용은 `prd.md` Part 1 참조)

**레이아웃 컴포넌트**:
- 네비게이션이 있는 헤더
- 회사 정보가 있는 푸터
- 반응형 그리드를 사용한 메인 콘텐츠 영역
- 고정 헤더와 호버 효과가 있는 테이블 기반 레이아웃
- 상세 보기를 위한 탭 기반 통합 대시보드

**반응형 브레이크포인트**: CSS 변수로 정의된 모바일, 태블릿, 데스크톱 설정

## 중요 구현 참고사항

### 현재 상태
- 프론트엔드는 **스캐폴딩만 완료** - 기본 Vite + React 템플릿만 존재
- 백엔드 디렉토리는 **아직 존재하지 않음**
- components, pages, services 디렉토리는 **비어 있음**
- 데이터베이스 **아직 생성되지 않음**

### 기능 구현 시 주의사항

**프론트엔드 개발**:
1. 디자인 시스템을 따라 `src/components/`에 재사용 가능한 컴포넌트 생성
2. React Router를 사용하여 `src/pages/`에 페이지 구현
3. Axios를 사용하여 `src/services/`에 API 서비스 모듈 생성
4. 전역 인증 상태를 위한 AuthContext 설정
5. JWT 토큰 처리를 위한 Axios 인터셉터 구성

**백엔드 개발**:
1. `backend/` 디렉토리에 Spring Boot 프로젝트 구조 생성
2. Spring Security + JWT 인증 구현
3. `prd.md` Part 4의 API 명세를 따라 REST API 엔드포인트 생성
4. 외부 API 연동 (DART, 금융위원회, 한국수출입은행, Naver)
5. MySQL 데이터베이스와 JPA/MyBatis 리포지토리 설정

**API 연동**:
- 모든 외부 API 키는 각 플랫폼에서 발급받아야 함
- 프론트엔드에 키를 노출하지 않도록 백엔드에서 외부 API 호출을 프록시해야 함
- 외부 API 호출에 대한 속도 제한 및 캐싱 구현

**보안 고려사항**:
- JWT 토큰 인증 사용 (세션 기반 아님)
- API 키는 백엔드 환경 변수에 저장, 프론트엔드에 절대 노출 금지
- 프론트엔드-백엔드 통신을 위한 CORS 설정 구현
- 프로덕션에서 HTTPS 사용

## 문서

**주요 문서**: `prd.md` (6개 파트에 걸쳐 150+ 페이지)
- Part 1: 프로젝트 개요 및 UI/UX 디자인 가이드
- Part 2: 인증 및 게시판 기능
- Part 3: 주가 차트 및 뉴스 검색
- Part 4: 데이터베이스 및 API 명세 (20+ 엔드포인트)
- Part 5: 프론트엔드 및 백엔드 구현 가이드
- Part 6: 배포, 테스트 및 모니터링

**프론트엔드 README**: `frontend/README.md`에 설정 지침 및 개발 가이드라인 포함

## 개발 일정

**기간**: 10주 (임베디드융합개발자 과정 프로젝트)

**주요 마일스톤**:
- 환경 설정 및 API 키 발급
- 데이터베이스 스키마 구현
- 인증 시스템
- 게시판 기능 (경제지표, 기업정보, 관심기업)
- 금융위원회 API와 주가 차트 연동
- Naver API를 사용한 뉴스 검색
- Vercel(프론트엔드) 및 AWS EC2(백엔드) 배포

## 코드 스타일

- **ESLint + Prettier**: 일관된 코드 포맷팅을 위해 설정됨
- **명명 규칙**:
  - React 컴포넌트: PascalCase (예: `Button.jsx`, `CompanyTable.jsx`)
  - 파일/디렉토리: 적절히 camelCase 또는 kebab-case 사용
  - API 서비스: 설명적인 이름 (예: `getCompanyInfo`, `fetchStockData`)
- **컴포넌트 구조**: React Hooks를 사용한 함수형 컴포넌트
- **상태 관리**: 전역 상태를 위한 React Context (AuthContext)

## Atlassian MCP 기반 작업 프로세스 (필수 준수)

모든 개발 작업은 **반드시** 다음 프로세스를 따라야 합니다:

### 1️⃣ 티켓 확인 및 작업 시작

```bash
# Atlassian MCP를 통해 Jira 프로젝트의 티켓 조회
# embed-project에서 진행할 티켓을 확인
```

**작업 내용**:
- Jira에서 할당된 티켓 확인
- 티켓의 Description, Acceptance Criteria 검토
- 작업 범위 및 요구사항 명확히 이해

### 2️⃣ Git 브랜치 생성 및 작업 진행

```bash
# 세션 시작 프로토콜
git status && git branch
gh auth status

# Jira 티켓 ID를 포함한 feature 브랜치 생성
git checkout -b feature/[JIRA-ID]-[간단한-설명]

# 예시: git checkout -b feature/EMBED-101-login-component
```

**작업 내용**:
- GitHub CLI 문법을 따라 브랜치 생성
- 점진적으로 커밋하며 개발 진행
- 커밋 메시지에 Jira 티켓 ID 포함 (`🎫 EMBED-XXX`)

### 3️⃣ Acceptance Criteria 체크리스트 확인

**작업 완료 후**:
- Jira 티켓의 **Acceptance Criteria** 항목별로 체크
- 모든 요구사항이 구현되었는지 확인
- 누락된 항목이 있으면 추가 작업 진행

**체크리스트 예시**:
- [ ] 로그인 폼 UI 구현 완료
- [ ] 폼 검증 로직 구현 완료
- [ ] API 연동 완료
- [ ] 에러 핸들링 구현 완료

### 4️⃣ 테스트 수행

**프론트엔드 테스트** (Playwright 활용):
```bash
# Playwright를 사용한 E2E 테스트 실행
# - 사용자 시나리오 테스트
# - UI 컴포넌트 동작 검증
# - 접근성 테스트
```

**백엔드 테스트** (API 호출):
```bash
# REST API 엔드포인트 테스트
# - Postman, curl, 또는 Axios를 통한 API 호출
# - 응답 코드 및 데이터 검증
# - 에러 케이스 테스트
```

**테스트 항목**:
- 정상 동작 케이스
- 에러 케이스 (유효하지 않은 입력, 네트워크 오류 등)
- 경계값 테스트
- 사용자 시나리오 플로우

### 5️⃣ Pull Request 생성 및 병합

**테스트 통과 후**:
```bash
# feature 브랜치 푸시
git push -u origin feature/EMBED-101-login-component

# GitHub CLI를 통한 PR 생성
gh pr create --title "feat(auth): 로그인 컴포넌트 구현" \
  --body "## 요약
[구현 내용 요약]

## Jira 티켓
🎫 EMBED-101

## Acceptance Criteria 체크리스트
- [x] 로그인 폼 UI 구현 완료
- [x] 폼 검증 로직 구현 완료
- [x] API 연동 완료
- [x] 에러 핸들링 구현 완료

## 테스트 결과
- [x] Playwright E2E 테스트 통과
- [x] API 호출 테스트 통과
- [x] 에러 케이스 검증 완료"

# PR 병합 (개인 프로젝트)
gh pr merge --squash --delete-branch

# 로컬 main 브랜치 업데이트
git checkout main
git pull origin main
```

**병합 후**:
- GitHub에 자동 업로드됨
- 로컬 및 원격 feature 브랜치 삭제
- Jira 티켓 상태를 "Done"으로 업데이트

### 작업 프로세스 준수 사항

**필수 체크리스트**:
- ✅ Jira 티켓 확인 후 작업 시작
- ✅ 브랜치 명명 규칙 준수 (`feature/[JIRA-ID]-[설명]`)
- ✅ Acceptance Criteria 모두 체크
- ✅ 테스트 수행 (프론트엔드: Playwright, 백엔드: API 호출)
- ✅ 테스트 통과 후 PR 생성 및 병합
- ✅ GitHub CLI를 활용한 워크플로우 준수

**금지 사항**:
- ❌ Jira 티켓 없이 작업 진행
- ❌ Acceptance Criteria 체크 생략
- ❌ 테스트 없이 PR 생성
- ❌ 테스트 실패 시 강제 병합
- ❌ 브랜치 명명 규칙 위반

## GitHub CLI를 활용한 Git 워크플로우

### GitHub CLI 설정

**설치**:
- Windows: `winget install --id GitHub.cli`
- macOS: `brew install gh`
- Linux: https://github.com/cli/cli#installation 참고

**인증**:
```bash
# GitHub 인증
gh auth login

# 인증 상태 확인
gh auth status
```

### 세션 시작 프로토콜
모든 개발 세션은 **반드시** 다음으로 시작해야 합니다:
```bash
git status && git branch
gh auth status  # GitHub CLI 인증 확인
```
이를 통해 변경 사항을 만들기 전에 현재 브랜치, 작업 트리 상태, GitHub 연결을 확인할 수 있습니다.

### Jira 기반 브랜치 전략

**브랜치 명명 규칙**:
```
feature/[JIRA-ID]-[간단한-설명]
bugfix/[JIRA-ID]-[간단한-설명]
hotfix/[JIRA-ID]-[간단한-설명]
```

**예시**:
- `feature/CORP-123-auth-system`
- `feature/CORP-124-stock-chart`
- `bugfix/CORP-125-login-validation`

### 개발 워크플로우 (GitHub CLI)

**1. Jira 티켓 작업 시작**:
```bash
# 현재 상태 확인
git status && git branch

# feature 브랜치 생성 및 체크아웃
git checkout -b feature/CORP-123-auth-system

# 올바른 브랜치에 있는지 확인
git branch

# 선택사항: Jira 티켓과 연결된 GitHub 이슈 생성
gh issue create --title "CORP-123: 인증 시스템 구현" \
  --body "Jira 티켓: CORP-123

  JWT 기반 인증 구현:
  - 로그인 컴포넌트
  - 회원가입 컴포넌트
  - AuthContext
  - Axios 인터셉터"
```

**2. 점진적 개발**:
```bash
# 코드 변경

# 스테이징 전에 변경사항 검토
git diff

# 특정 파일 스테이징
git add src/components/Login.jsx src/services/authService.js

# 설명적인 커밋 생성
git commit -m "feat(auth): JWT 로그인 플로우 구현

- 폼 검증이 있는 Login 컴포넌트 추가
- axios 인터셉터를 사용한 authService 생성
- AuthContext에서 JWT 토큰 관리 설정

🎫 CORP-123"
```

**3. 작업 계속** (여러 커밋 권장):
```bash
# 추가 변경...
git add src/components/Register.jsx

git commit -m "feat(auth): 사용자 등록 컴포넌트 추가

- 검증이 있는 등록 폼 구현
- 비밀번호 강도 표시기 추가
- 백엔드 등록 API 연결

🎫 CORP-123"
```

**4. 위험한 작업 전** (복원 지점 생성):
```bash
# 주요 리팩토링이나 위험한 변경 전에 커밋
git add .
git commit -m "chore: 인증 리팩토링 전 체크포인트

🎫 CORP-123"
```

**5. 푸시 및 Pull Request 생성**:
```bash
# feature 브랜치를 원격으로 푸시
git push -u origin feature/CORP-123-auth-system

# GitHub CLI를 사용하여 Pull Request 생성
gh pr create --title "feat(auth): JWT 인증 시스템 구현" \
  --body "## 요약
- 폼 검증이 있는 Login 컴포넌트 구현
- Axios 인터셉터를 사용한 authService 생성
- 전역 인증 상태를 위한 AuthContext 설정
- 사용자 등록 플로우 추가

## Jira 티켓
🎫 CORP-123

## 테스트 계획
- [ ] 로그인 폼 검증이 올바르게 작동
- [ ] JWT 토큰이 올바르게 저장 및 갱신됨
- [ ] 등록 플로우가 성공적으로 완료됨
- [ ] Axios 인터셉터가 401 오류를 처리

## 스크린샷
[해당되는 경우 스크린샷 추가]" \
  --assignee @me

# 또는 대화형 모드 사용
gh pr create
```

**6. Pull Request 검토 및 병합**:
```bash
# PR 상태 보기
gh pr status

# PR diff 보기
gh pr diff

# 팀원에게 검토 요청
gh pr review --approve

# 승인 후 PR 병합
gh pr merge --squash --delete-branch

# 또는 merge 커밋으로 병합
gh pr merge --merge --delete-branch

# 로컬 main 브랜치 업데이트
git checkout main
git pull origin main
```

### 커밋 메시지 형식

Jira 티켓 참조와 함께 conventional commits 따르기:

```
<type>(<scope>): <subject>

<body>

🎫 <JIRA-ID>
```

**타입**:
- `feat`: 새로운 기능
- `fix`: 버그 수정
- `refactor`: 코드 리팩토링
- `style`: 포맷팅, 스타일링
- `docs`: 문서 변경
- `test`: 테스트 추가
- `chore`: 유지보수 작업

**예시**:
```
feat(stock): Recharts를 사용한 주가 차트 구현

- 주가 데이터를 위한 금융위원회 API 연동
- 반응형 디자인의 AreaChart 컴포넌트 추가
- 기간 선택 구현 (30/60/90일)
- 로딩 상태 및 에러 처리 추가

🎫 CORP-124
```

### 유용한 GitHub CLI 명령어

**Pull Request 작업**:
```bash
# 모든 PR 목록
gh pr list

# 특정 PR 보기
gh pr view 123

# PR을 로컬에서 체크아웃
gh pr checkout 123

# PR 검토
gh pr review 123 --approve
gh pr review 123 --request-changes --body "수정 필요..."

# 병합하지 않고 PR 닫기
gh pr close 123

# PR 다시 열기
gh pr reopen 123
```

**이슈 작업**:
```bash
# 이슈 목록
gh issue list

# 이슈 생성
gh issue create --title "버그: 로그인 실패" --body "설명..."

# 이슈 보기
gh issue view 456

# 이슈 닫기
gh issue close 456

# PR에 이슈 연결
gh pr create --body "Fixes #456"
```

**저장소 작업**:
```bash
# 저장소 정보 보기
gh repo view

# 저장소 클론
gh repo clone owner/repo

# 저장소 포크
gh repo fork

# 브라우저에서 저장소 열기
gh browse
```

**워크플로우/액션**:
```bash
# 워크플로우 실행 목록
gh run list

# 특정 실행 보기
gh run view 789

# 실시간으로 실행 보기
gh run watch 789
```

### Git 안전 규칙

**절대 금지**:
- ❌ `main` 브랜치에서 직접 작업
- ❌ main에 강제 푸시 (`git push --force`)
- ❌ 민감한 데이터 커밋 (.env 파일, API 키)
- ❌ 커밋 전 `git diff` 검토 생략
- ❌ 모호한 커밋 메시지 사용 ("fix", "update", "changes")
- ❌ 검토 없이 PR 병합 (개인 프로젝트 제외)

**항상 준수**:
- ✅ 모든 작업에 대해 feature 브랜치 생성
- ✅ 세션 시작 시 `git status && git branch && gh auth status` 실행
- ✅ 스테이징 전에 `git diff`로 변경사항 검토
- ✅ Jira 티켓 ID가 포함된 설명적인 커밋 메시지 작성
- ✅ 점진적으로 커밋 (거대한 커밋 금지)
- ✅ 위험한 작업 전 복원 지점 생성
- ✅ Pull Request에 `gh pr create` 사용
- ✅ 병합 시 `--delete-branch` 플래그로 브랜치 삭제

### 브랜치 정리

GitHub CLI를 통한 병합 후:
```bash
# --delete-branch 플래그로 브랜치 자동 삭제
gh pr merge --squash --delete-branch

# 로컬 저장소 업데이트
git checkout main
git pull origin main

# 로컬 브랜치 확인
git branch -a

# 필요시 수동으로 로컬 브랜치 삭제
git branch -d feature/CORP-123-auth-system

# 원격 추적 브랜치 정리
git remote prune origin
```

### 복구 및 롤백

**마지막 커밋 취소 (변경사항 유지)**:
```bash
git reset --soft HEAD~1
```

**커밋되지 않은 변경사항 삭제**:
```bash
git restore <file>  # 특정 파일
git restore .       # 모든 파일
```

**커밋 히스토리 보기**:
```bash
git log --oneline --graph --all
gh pr list --state all  # 모든 PR 히스토리 보기
```

**병합된 PR 되돌리기**:
```bash
# PR 번호 찾기
gh pr list --state merged

# revert PR 생성
gh pr view 123 --json mergeCommit --jq .mergeCommit.oid | \
  xargs -I {} git revert {} --no-commit
git commit -m "revert: PR #123 취소"
gh pr create --title "revert: PR #123 취소"
```

### 개인 개발 vs 팀 협업

**개인 개발** (현재):
```bash
# 간소화된 워크플로우 - 필요시 직접 병합
git checkout main
git merge feature/CORP-123-auth-system --no-ff
git push origin main
git branch -d feature/CORP-123-auth-system
```

**팀 협업** (향후):
```bash
# 항상 PR 워크플로우 사용
gh pr create
# 검토 대기
gh pr merge --squash --delete-branch
```

## 테스트 전략 (구현 예정)

- **프론트엔드**: 컴포넌트 유닛 테스트, API 호출 통합 테스트
- **백엔드**: 서비스 유닛 테스트, REST 엔드포인트 통합 테스트
- **E2E**: 중요 플로우에 대한 사용자 여정 테스트