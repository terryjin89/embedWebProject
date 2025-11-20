# 기업분석 웹페이지 (Company Analyzer)

임베디드융합개발자 과정 프로젝트 - 기업 분석 웹 애플리케이션

## 프로젝트 개요
 KH정보교육원에서 배운 웹 프로그래밍 학습내용구현이 주 목적입니다. 프론트엔드는 React, 백엔드는 SpringBoot, 데이터베이스는 MYSQL로 구성되어 있습니다.
 재무 데이터, 공시 정보, 주가, 뉴스 등 기업 관련 정보를 관리하고 분석할 수 있는 웹 플랫폼입니다.
 개괄적인 구조는 외부 API와 데이터를 SpringBoot 프레임워크의 백엔드에서 처리하여 React 프레임워크로 구성된 프론트에서 브라우저의 요청을 받아 호출하면
 각종 정보를 전달하는 방식으로 구성되어 있습니다.
 - 요청 프로세스 - 브라우저(사용자, 액션) -> 프론트엔드(React,백엔드 API 호출) -> 백엔드(SpringBoot, 외부 API or DB 데이터 호출)
 - 응답 프로세스 - 백엔드(SpringBoot, 외부 API or DB 데이터 호출) -> 프론트엔드(React,백엔드 API 호출) -> 브라우저(사용자, 액션)
 - 전체 프로젝트 관리는 Jira를 통해 진행하였습니다.(https://embed-project.atlassian.net/browse/SCRUM-5)
 - 버전관리는 Git을 통해 진행하였습니다.
 - 작업순서는 Git 브랜치를 열고 단위별 작업 진행 후 테스트 결과 이상 없을 경우에 브랜치를 커밋, 머지하여 메인에 병합하는 순서로 진행하였습니다.
 - 테스트의 경우 프론트엔드는 E2E 테스트를 단위 테스트로 진행하고, 백엔드는 API 호출을 단위 테스트로 진행하였습니다.
 - 전체 테스트 및 배포는 docker-compose를 활용하여 3개의 컨테이너(MYSQL, SpringBoot, React) 올려 연결하여 테스트 및 배포 진행하였습니다.
 
### 주요 기능

- **경제지표 게시판**: 환율 데이터 조회
- **기업정보 게시판**: DART API 연동 기업 정보
- **관심기업 관리**: 사용자별 관심 기업 목록
- **주가 차트**: Recharts를 이용한 주가 시각화
- **관련 뉴스**: Naver Search API 연동 뉴스 검색

## 기술 스택

### Frontend
- React 19.2.0 + Vite 7.2.2
- React Router DOM v7
- Axios 1.13.2
- Recharts 3.4.1

### Backend
- Spring Boot 3.x + Gradle
- JDK 17
- MySQL 8.0

### Infrastructure
- Docker & Docker Compose
- MySQL 8.0

## 🚀 시작하기 (Getting Started)

이 섹션은 신규 개발자가 로컬 환경에서 프로젝트를 실행하기 위한 단계별 가이드를 제공합니다.

### ✅ 사전 요구사항 (Prerequisites)

프로젝트를 시작하기 전에 다음 소프트웨어가 설치되어 있어야 합니다.

-   [Git](https://git-scm.com/downloads)
-   [Docker Desktop](https://www.docker.com/products/docker-desktop/) (Docker Compose가 포함되어 있습니다)

### ⚙️ 환경 설정 (Environment Setup)

#### 1. 저장소 클론 (Clone Repository)

먼저, 프로젝트를 로컬 머신으로 복제합니다.

```bash
git clone https://github.com/terryjin89/embedWebProject.git
cd embedWebProject
```

#### 2. 환경 변수 파일 생성 (Create Environment File)

이 프로젝트는 민감한 정보(API 키, DB 비밀번호 등)를 `.env` 파일을 통해 관리합니다. Git에 포함된 `.env.example` 템플릿을 복사하여 `.env` 파일을 생성해야 합니다.

```bash
cp .env.example .env
```

`.env` 파일은 Git에 의해 추적되지 않으므로, 여기에 입력하는 민감한 정보는 외부에 노출되지 않습니다.

#### 3. API 키 발급 및 설정 (Get API Keys)

생성된 `.env` 파일을 열고, 아래 목록에 따라 각 API 키를 발급받아 해당 변수에 값을 채워넣어야 합니다.

-   `VITE_DART_API_KEY`
    -   **설명**: DART(전자공시시스템) 기업 정보 조회를 위한 API 키입니다.
    -   **발급처**: [DART 오픈 API 포털](https://opendart.fss.or.kr/)

-   `VITE_FINANCIAL_API_KEY`
    -   **설명**: 금융위원회 주식 시세 정보 조회를 위한 공공데이터포털 API 키입니다.
    -   **발급처**: [공공데이터포털](https://www.data.go.kr/) (금융위원회_주식시세정보 검색 후 활용신청)

-   `VITE_EXCHANGE_RATE_API_KEY`
    -   **설명**: 한국수출입은행 환율 정보 조회를 위한 API 키입니다.
    -   **발급처**: [한국수출입은행 경제통계시스템](https://www.koreaexim.go.kr/site/main/index.do)

-   `VITE_NAVER_CLIENT_ID` & `VITE_NAVER_CLIENT_SECRET`
    -   **설명**: 네이버 검색(뉴스) API 이용을 위한 클라이언트 ID와 시크릿입니다.
    -   **발급처**: [네이버 개발자 센터](https://developers.naver.com/)

#### 4. 데이터베이스 설정 (Database Configuration)

`.env` 파일 내의 데이터베이스 관련 변수는 로컬 Docker 환경을 위해 미리 설정되어 있습니다. 특별한 경우가 아니라면 기본값을 그대로 사용해도 무방합니다.

```
MYSQL_ROOT_PASSWORD=rootpass123
MYSQL_DATABASE=corp_analysis
MYSQL_USER=devuser
MYSQL_PASSWORD=devpass123
```

### Docker Compose로 실행

#### 전체 환경 실행 (권장)

```bash
# 전체 서비스 시작 (프론트엔드 + 백엔드 + MySQL)
docker-compose up

# 백그라운드에서 실행
docker-compose up -d

# 로그 확인
docker-compose logs -f

# 특정 서비스 로그만 보기
docker-compose logs -f frontend
docker-compose logs -f backend
docker-compose logs -f mysql
```

#### 특정 서비스만 실행

```bash
# 프론트엔드만 실행
docker-compose up frontend

# 백엔드 + MySQL만 실행
docker-compose up backend mysql
```

#### 서비스 중지 및 정리

```bash
# 서비스 중지
docker-compose stop

# 서비스 중지 및 컨테이너 삭제
docker-compose down

# 볼륨까지 완전히 삭제 (데이터베이스 초기화)
docker-compose down -v
```

#### 재시작 및 재빌드

```bash
# 이미지 재빌드 후 시작
docker-compose up --build

# 특정 서비스만 재빌드
docker-compose up --build frontend

# 전체 재시작
docker-compose restart

# 특정 서비스만 재시작
docker-compose restart backend
```

### 접속 URL

서비스 실행 후 다음 URL로 접속 가능합니다:

- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080
- **MySQL**: localhost:3306

### 개발 모드 특징

- **핫 리로딩**: 코드 변경 시 자동으로 반영됨
- **볼륨 마운트**: 로컬 파일 시스템과 컨테이너 동기화
- **데이터 영속성**: MySQL 데이터는 볼륨에 저장되어 컨테이너 재시작 후에도 유지

## 로컬 개발 (Docker 없이)

### Frontend 개발

```bash
cd frontend

# 의존성 설치
npm install

# 개발 서버 실행
npm run dev

# 프로덕션 빌드
npm run build
```

### Backend 개발

```bash
cd backend

# Gradle 빌드 및 실행
./gradlew bootRun

# 또는 Windows
gradlew.bat bootRun
```

## 문서

- [CLAUDE.md](./CLAUDE.md) - 프로젝트 상세 문서
- [PRD.md](./prd.md) - 제품 요구사항 명세서
- [Frontend README](./frontend/README.md) - 프론트엔드 가이드
- [Backend README](./backend/README.md) - 백엔드 가이드

## 트러블슈팅

### Docker 관련 문제

**문제: 포트가 이미 사용 중**
```bash
# 사용 중인 포트 확인 (Windows)
netstat -ano | findstr :3306
netstat -ano | findstr :8080
netstat -ano | findstr :5173

# 프로세스 종료
taskkill /PID <PID> /F
```

**문제: 컨테이너가 시작되지 않음**
```bash
# 로그 확인
docker-compose logs -f <service-name>

# 컨테이너 상태 확인
docker-compose ps

# 완전히 재시작
docker-compose down -v
docker-compose up --build
```

**문제: MySQL 연결 오류**
```bash
# MySQL 헬스체크 확인
docker-compose ps

# MySQL 로그 확인
docker-compose logs mysql

# MySQL 컨테이너 접속
docker-compose exec mysql mysql -u root -p
```

### 데이터베이스 초기화

```bash
# 모든 볼륨 삭제 (주의: 데이터 손실)
docker-compose down -v

# 재시작
docker-compose up
```

## 라이선스

This project is part of the Embedded Developer Course.

## 기여

프로젝트 기여는 환영합니다. Pull Request를 제출해주세요.
