# 기업분석 웹페이지 PRD - 개발 환경 및 배포

## 📋 Document Info
- **작성일**: 2025.11.16
- **대상**: 백엔드/프론트엔드/DevOps 개발자
- **버전**: 1.0

---

## 🎯 Overview

임베디드융합개발자 과정 학습 내용을 활용한 기업분석 웹 서비스의 개발 환경 구성 및 배포 전략을 정의합니다. 로컬 개발부터 프로덕션 배포까지 일관된 환경을 보장하고, 빠른 온보딩과 안정적인 배포를 목표로 합니다.

---

## 1️⃣ Docker-Compose 기반 테스트 환경

### 1.1 목적
- ✅ **빠른 팀원 온보딩**: 새로운 개발자가 30분 내 개발 환경 구축
- ✅ **환경 일관성**: "내 컴퓨터에선 되는데?" 이슈 제거
- ✅ **독립적 테스트**: DB, 백엔드, 프론트엔드 각각 또는 통합 테스트 가능

### 1.2 구성 컴포넌트

```yaml
# docker-compose.yml 구조

services:
  ├─ mysql          # MySQL 8.0.xx
  ├─ backend        # SpringBoot 3.x (JDK 17)
  └─ frontend       # React 18.x (Node.js 22.x)
```

### 1.3 상세 요구사항

#### 📦 MySQL Service
| 항목 | 내용 |
|------|------|
| 이미지 | `mysql:8.0` (최신 8.0.xx 버전) |
| 포트 | `3306:3306` |
| 볼륨 | `./mysql-data:/var/lib/mysql` (데이터 영속성) |
| 환경변수 | `MYSQL_ROOT_PASSWORD`, `MYSQL_DATABASE`, `MYSQL_USER`, `MYSQL_PASSWORD` |
| 초기화 | `./init-scripts:/docker-entrypoint-initdb.d` (스키마 자동 생성) |
| Health Check | `mysqladmin ping` 체크 (의존성 관리) |

#### 🚀 Backend Service
| 항목 | 내용 |
|------|------|
| 이미지 | `openjdk:17-slim` 또는 커스텀 빌드 |
| 빌드 | Dockerfile 사용 (멀티스테이지 빌드 권장) |
| 포트 | `8080:8080` |
| 의존성 | `depends_on: mysql` (health check 기반) |
| 환경변수 | DB 접속 정보, API 키 등 `.env` 파일로 관리 |
| 볼륨 | `./backend:/app` (hot-reload 지원) |

#### ⚛️ Frontend Service
| 항목 | 내용 |
|------|------|
| 이미지 | `node:22-alpine` |
| 포트 | `3000:3000` |
| 명령어 | `npm start` (개발 서버) |
| 볼륨 | `./frontend:/app`, `/app/node_modules` (캐시 유지) |
| 환경변수 | `REACT_APP_API_URL=http://localhost:8080` |

### 1.4 실행 시나리오

```bash
# 전체 환경 시작
$ docker-compose up -d

# 특정 서비스만 시작
$ docker-compose up mysql backend

# 로그 확인
$ docker-compose logs -f backend

# 전체 종료 및 데이터 삭제
$ docker-compose down -v
```

### 1.5 환경변수 관리

```bash
# .env 파일 (Git에 커밋 금지)
MYSQL_ROOT_PASSWORD=rootpass123
MYSQL_DATABASE=corp_analysis
MYSQL_USER=devuser
MYSQL_PASSWORD=devpass123

# API Keys
PUBLIC_DATA_API_KEY=your_key_here
DART_API_KEY=your_dart_key_here

# Spring Profile
SPRING_PROFILES_ACTIVE=local
```

### 1.6 성공 기준
- [ ] `docker-compose up` 한 번에 모든 서비스 정상 구동
- [ ] Frontend에서 Backend API 호출 성공 (`http://localhost:3000` → `http://localhost:8080/api`)
- [ ] MySQL 데이터 영속성 확인 (컨테이너 재시작 후에도 데이터 유지)
- [ ] 신규 개발자가 README 보고 30분 내 환경 구축 완료

---

## 2️⃣ JAR 파일 배포 전략

### 2.1 목적
- ✅ **단순성**: 복잡한 WAS 설정 없이 `java -jar` 로 실행
- ✅ **이식성**: 어떤 환경에서든 JDK 17만 있으면 실행 가능
- ✅ **경량화**: 내장 Tomcat으로 빠른 시작

### 2.2 빌드 프로세스

#### Maven 사용 시
```bash
# 프로젝트 루트에서
$ ./mvnw clean package -DskipTests

# 결과물
target/corp-analysis-1.0.0.jar
```

#### Gradle 사용 시
```bash
$ ./gradlew clean bootJar

# 결과물
build/libs/corp-analysis-1.0.0.jar
```

### 2.3 JAR 구성 요구사항

| 항목 | 내용 |
|------|------|
| 타입 | Executable JAR (Spring Boot Fatjar) |
| 내장 서버 | Tomcat (default) |
| 프로파일 | `dev`, `staging`, `prod` |
| 설정 외부화 | `application.yml` 외부 경로 지원 |
| 로깅 | SLF4J + Logback, 파일 로깅 활성화 |

### 2.4 실행 명세서

```bash
# 기본 실행 (dev 프로파일)
$ java -jar corp-analysis-1.0.0.jar

# 프로파일 지정 (프로덕션)
$ java -jar \
  -Dspring.profiles.active=prod \
  -Dserver.port=8080 \
  corp-analysis-1.0.0.jar

# 외부 설정 파일 사용
$ java -jar \
  -Dspring.config.location=/etc/corp-analysis/application.yml \
  corp-analysis-1.0.0.jar

# 메모리 설정 (프로덕션 권장)
$ java -Xms512m -Xmx1024m -jar corp-analysis-1.0.0.jar
```

### 2.5 배포 환경별 설정

#### 🧪 Development
```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/corp_analysis
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update

logging:
  level:
    root: INFO
    com.yourcompany: DEBUG
```

#### 🚀 Production
```yaml
# application-prod.yml
spring:
  datasource:
    url: jdbc:mysql://prod-db-host:3306/corp_analysis
    hikari:
      maximum-pool-size: 10
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate

logging:
  level:
    root: WARN
  file:
    name: /var/log/corp-analysis/app.log
```

### 2.6 Systemd 서비스 등록 (Linux 서버)

```ini
# /etc/systemd/system/corp-analysis.service

[Unit]
Description=Corporate Analysis Web Application
After=syslog.target network.target

[Service]
User=appuser
ExecStart=/usr/bin/java -jar /opt/corp-analysis/corp-analysis-1.0.0.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```bash
# 서비스 등록 및 시작
$ sudo systemctl enable corp-analysis
$ sudo systemctl start corp-analysis
$ sudo systemctl status corp-analysis
```

### 2.7 Health Check

```bash
# Actuator 엔드포인트 (의존성 추가 필요)
$ curl http://localhost:8080/actuator/health

# 응답 예시
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "diskSpace": { "status": "UP" }
  }
}
```

### 2.8 배포 체크리스트

**빌드 전**
- [ ] 테스트 통과 확인 (`mvn test`)
- [ ] 프로파일별 설정 파일 검증
- [ ] API 키 등 민감정보 환경변수화

**배포 시**
- [ ] JAR 파일 크기 확인 (비정상적으로 크지 않은지)
- [ ] 실행 권한 부여 (`chmod +x`)
- [ ] 로그 디렉토리 생성 및 권한 설정

**배포 후**
- [ ] Health Check 응답 확인
- [ ] DB 연결 정상 확인
- [ ] 외부 API (공공데이터, DART) 연동 테스트
- [ ] 로그 파일 생성 및 기록 확인

---

## 🔄 통합 워크플로우

```
[개발] 
↓
Docker Compose로 로컬 테스트
↓
[빌드]
↓
JAR 파일 생성 (CI/CD)
↓
[배포]
↓
서버에서 java -jar 실행
↓
[모니터링]
```

---

## 📌 참고사항

### 보안
- `.env` 파일은 `.gitignore`에 추가
- 프로덕션 DB 접속 정보는 환경변수 또는 외부 Config 서버 사용
- API 키는 절대 코드에 하드코딩 금지

### 성능
- JAR 실행 시 JVM 옵션 튜닝 권장 (`-Xms`, `-Xmx`)
- DB 커넥션 풀 설정 최적화

### 문서화
- `README.md`에 Docker Compose 실행 방법 명시
- 환경변수 템플릿 `.env.example` 제공

---

**작성자**: Product Manager  
**검토 필요**: Backend Lead, DevOps Engineer  
**다음 단계**: CI/CD 파이프라인 설계 (Jenkins/GitHub Actions)