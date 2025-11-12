# 기업분석 웹페이지 (Company Analyzer)

임베디드융합개발자 과정 프로젝트 - 기업 분석 웹 애플리케이션

## 프로젝트 개요

재무 데이터, 공시 정보, 주가, 뉴스 등 기업 관련 정보를 관리하고 분석할 수 있는 웹 플랫폼입니다.

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

## 시작하기

### 사전 요구사항

- Docker Desktop 설치
- Docker Compose 설치
- Git 설치

### 환경 설정

1. 저장소 클론

```bash
git clone <repository-url>
cd embedProject
```

2. 환경 변수 설정

```bash
# .env.example을 .env로 복사
cp .env.example .env

# .env 파일을 열어 실제 API 키 입력
```

필수 환경 변수:
- `DART_API_KEY`: DART API 키 (https://opendart.fss.or.kr/)
- `NAVER_CLIENT_ID`: Naver API 클라이언트 ID
- `NAVER_CLIENT_SECRET`: Naver API 클라이언트 시크릿
- 데이터베이스 정보 (기본값 사용 가능)

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
