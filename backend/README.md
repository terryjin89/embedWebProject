# Company Analyzer Backend

Spring Boot 3.x 기반의 기업분석 웹 애플리케이션 백엔드입니다.

## 🛠️ 기술 스택

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Gradle 8.11.1
- **Database**: MySQL 8.0 (Production), H2 (Test)
- **Security**: Spring Security + JWT
- **API Documentation**: Swagger/OpenAPI 3.0
- **ORM**: Spring Data JPA + Hibernate

## 📁 프로젝트 구조

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/project/companyanalyzer/
│   │   │   ├── controller/      # REST API 엔드포인트
│   │   │   ├── service/          # 비즈니스 로직
│   │   │   ├── repository/       # 데이터 액세스 계층
│   │   │   ├── entity/           # JPA 엔티티
│   │   │   ├── dto/              # 데이터 전송 객체
│   │   │   ├── config/           # 설정 클래스 (CORS, Swagger, Security)
│   │   │   ├── security/         # 보안 관련 클래스
│   │   │   ├── exception/        # 예외 처리
│   │   │   ├── util/             # 유틸리티 클래스
│   │   │   └── CompanyAnalyzerApplication.java
│   │   └── resources/
│   │       ├── application.yml       # 기본 설정
│   │       ├── application-dev.yml   # 개발 환경 설정
│   │       └── application-test.yml  # 테스트 환경 설정
│   └── test/
│       └── java/com/project/companyanalyzer/
├── build.gradle          # Gradle 빌드 설정
├── settings.gradle       # Gradle 프로젝트 설정
├── gradlew              # Gradle Wrapper (Unix)
└── gradlew.bat          # Gradle Wrapper (Windows)
```

## 🚀 시작하기

### 필수 요구사항

- JDK 17 이상
- MySQL 8.0 (프로덕션 환경)

### 1. 데이터베이스 생성 (MySQL)

```sql
CREATE DATABASE company_analyzer
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

### 2. 환경 변수 설정

환경 변수 또는 application.yml에서 다음 값들을 설정합니다:

```yaml
spring:
  datasource:
    password: ${DB_PASSWORD:your_password}

jwt:
  secret: ${JWT_SECRET:your_jwt_secret_key}
```

### 3. 애플리케이션 실행

#### 개발 환경 (H2 데이터베이스)
```bash
./gradlew bootRun --args='--spring.profiles.active=test'
```

#### 프로덕션 환경 (MySQL 데이터베이스)
```bash
./gradlew bootRun
```

애플리케이션이 실행되면:
- **API Base URL**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Docs**: http://localhost:8080/api/api-docs
- **Health Check**: http://localhost:8080/api/health

## 📝 주요 스크립트

| 명령어 | 설명 |
|--------|------|
| `./gradlew build` | 프로젝트 빌드 |
| `./gradlew build -x test` | 테스트 제외하고 빌드 |
| `./gradlew bootRun` | 애플리케이션 실행 |
| `./gradlew test` | 테스트 실행 |
| `./gradlew clean` | 빌드 파일 정리 |

## 🔧 주요 의존성

### Spring Boot Starters
- `spring-boot-starter-web` - REST API 개발
- `spring-boot-starter-data-jpa` - JPA/Hibernate
- `spring-boot-starter-security` - 보안
- `spring-boot-starter-validation` - 유효성 검사

### Database
- `mysql-connector-j` - MySQL 드라이버
- `h2database` - 테스트용 인메모리 DB

### Security
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson` - JWT 인증

### Documentation
- `springdoc-openapi-starter-webmvc-ui` - Swagger/OpenAPI

### Development Tools
- `lombok` - 코드 간소화
- `spring-boot-devtools` - 개발 도구

## 🔐 보안 설정

### JWT 인증
- JWT 토큰 기반 인증 시스템
- 토큰 만료 시간: 24시간 (설정 가능)
- Bearer 토큰 방식

### CORS 설정
- 허용 Origin: `http://localhost:5173`, `http://localhost:3000`
- 허용 Methods: GET, POST, PUT, DELETE, OPTIONS
- 자격 증명 허용

### 공개 엔드포인트
- `/` - 루트
- `/api-docs/**` - API 문서
- `/swagger-ui/**` - Swagger UI
- `/auth/**` - 인증 관련
- `/health` - 헬스 체크

## 📊 API 문서

Swagger UI를 통해 모든 API 엔드포인트를 확인하고 테스트할 수 있습니다:

http://localhost:8080/api/swagger-ui.html

## 🧪 테스트

### 헬스 체크
```bash
curl http://localhost:8080/api/health
```

### API 문서 확인
```bash
curl http://localhost:8080/api/v3/api-docs
```

## 📦 프로덕션 빌드

```bash
# JAR 파일 생성
./gradlew build

# 생성된 JAR 파일 실행
java -jar build/libs/company-analyzer-0.0.1-SNAPSHOT.jar
```

## 🔍 로깅

로그 레벨 설정 (application.yml):
- Root: INFO
- Application: DEBUG
- Spring Web: DEBUG
- Hibernate SQL: DEBUG

## 📋 프로파일

### default (프로덕션)
- MySQL 데이터베이스
- 포트 8080
- DDL-Auto: update

### dev (개발)
- MySQL 데이터베이스
- DDL-Auto: create-drop
- 상세 로깅

### test (테스트)
- H2 인메모리 데이터베이스
- 포트 8081
- DDL-Auto: create-drop
- H2 콘솔 활성화: `/h2-console`

## 🤝 기여

이 프로젝트는 임베디드융합개발자 과정의 일환으로 진행되고 있습니다.

## 📄 라이선스

이 프로젝트는 교육 목적으로 제작되었습니다.
