# Company Analyzer - 데이터베이스 설정 가이드

## 빠른 시작 (Quick Start)

### 1. MySQL 데이터베이스 생성

Windows 환경에서 MySQL에 접속하여 스키마를 실행합니다.

```bash
# MySQL 접속
mysql -u root -p

# 스키마 실행 (MySQL 내부에서)
source C:/Github/embedWebProject/database/schema.sql;

# 또는 외부에서 직접 실행
mysql -u root -p < C:/Github/embedWebProject/database/schema.sql
```

### 2. 샘플 데이터 삽입 (선택사항)

테스트를 위한 샘플 데이터를 삽입합니다.

```bash
mysql -u root -p company_analyzer < C:/Github/embedWebProject/database/sample_data.sql
```

**샘플 계정**:
- 이메일: `test1@example.com`
- 비밀번호: `password123`

### 3. Spring Boot 설정 파일 수정

`backend/src/main/resources/application.yml` 파일에서 데이터베이스 연결 정보를 설정합니다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/company_analyzer?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: root
    password: YOUR_MYSQL_PASSWORD  # 여기에 MySQL 비밀번호 입력
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: validate  # 스키마 검증만 수행 (자동 생성 안 함)
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect
```

### 4. 백엔드 서버 실행

```bash
cd backend
./gradlew bootRun
```

정상적으로 연결되면 다음과 같은 로그가 출력됩니다:

```
Hibernate: select @@version
HHH000400: Using dialect: org.hibernate.dialect.MySQL8Dialect
Started CompanyAnalyzerApplication in X.XXX seconds
```

---

## 데이터베이스 구조

### 테이블 요약

| 테이블명 | 설명 | PK | FK |
|---------|------|----|----|
| member | 사용자 정보 | user_code | - |
| company | 기업 정보 (DART API) | corp_code | - |
| stock | 관심기업 | id | user_code, corp_code |
| memo | 메모 | id | user_code |

### 관계도

```
Member (1) ──< (N) Stock (N) >── (1) Company
  │
  │ (1)
  │
  ↓
 (N) Memo
```

- 한 사용자(Member)는 여러 관심기업(Stock)을 가질 수 있음
- 한 기업(Company)은 여러 사용자의 관심기업(Stock)으로 등록될 수 있음
- 한 사용자(Member)는 여러 메모(Memo)를 작성할 수 있음

---

## 주요 제약조건

### 1. UNIQUE 제약

- **member.email**: 이메일 중복 방지
- **stock(user_code, stock_code)**: 사용자당 동일 종목 중복 등록 방지
- **memo(user_code, stock_code)**: 사용자당 종목별 메모 하나만

### 2. Foreign Key (CASCADE)

- **stock.user_code → member.user_code**: 사용자 삭제 시 관심기업도 삭제
- **stock.corp_code → company.corp_code**: 기업 삭제 시 관심기업도 삭제
- **memo.user_code → member.user_code**: 사용자 삭제 시 메모도 삭제

---

## 트러블슈팅

### 문제 1: MySQL 접속 오류

**에러**:
```
Access denied for user 'root'@'localhost' (using password: YES)
```

**해결**:
1. MySQL 비밀번호 확인
2. `application.yml`에서 `spring.datasource.password` 확인
3. MySQL 사용자 권한 확인:
   ```sql
   GRANT ALL PRIVILEGES ON company_analyzer.* TO 'root'@'localhost';
   FLUSH PRIVILEGES;
   ```

### 문제 2: 테이블이 이미 존재함

**에러**:
```
Table 'member' already exists
```

**해결**:
```sql
-- 기존 데이터베이스 삭제 후 재생성
DROP DATABASE IF EXISTS company_analyzer;
CREATE DATABASE company_analyzer CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 스키마 재실행
source C:/Github/embedWebProject/database/schema.sql;
```

### 문제 3: Hibernate ddl-auto 충돌

**에러**:
```
Schema validation failed
```

**해결**:
- `application.yml`에서 `spring.jpa.hibernate.ddl-auto`를 `validate`로 설정
- 스키마 파일(schema.sql)과 Entity 클래스가 일치하는지 확인

### 문제 4: 문자셋 오류 (이모지 저장 안 됨)

**에러**:
```
Incorrect string value: '\xF0\x9F...'
```

**해결**:
```sql
-- 데이터베이스 문자셋 확인
SHOW VARIABLES LIKE 'character_set%';

-- utf8mb4로 변경
ALTER DATABASE company_analyzer CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE memo CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

## 데이터 확인 쿼리

### 전체 테이블 확인
```sql
USE company_analyzer;
SHOW TABLES;
```

### 사용자 목록
```sql
SELECT user_code, email, name, created_at FROM member;
```

### 관심기업 목록 (JOIN)
```sql
SELECT
    m.name AS user_name,
    c.corp_name,
    s.stock_code,
    s.registered_at
FROM stock s
JOIN member m ON s.user_code = m.user_code
JOIN company c ON s.corp_code = c.corp_code
ORDER BY s.registered_at DESC;
```

### 메모 목록
```sql
SELECT
    m.name AS user_name,
    memo.stock_code,
    LEFT(memo.content, 50) AS content_preview,
    memo.updated_at
FROM memo
JOIN member m ON memo.user_code = m.user_code
ORDER BY memo.updated_at DESC;
```

---

## 백업 및 복구

### 백업
```bash
# 전체 데이터베이스 백업
mysqldump -u root -p company_analyzer > backup_$(date +%Y%m%d).sql

# 특정 테이블만 백업
mysqldump -u root -p company_analyzer member stock memo > user_data_backup.sql
```

### 복구
```bash
mysql -u root -p company_analyzer < backup_20250116.sql
```

---

## 성능 최적화

### 인덱스 사용 현황 확인
```sql
SHOW INDEX FROM stock;
SHOW INDEX FROM memo;
```

### 쿼리 실행 계획 확인
```sql
EXPLAIN SELECT * FROM stock WHERE user_code = '550e8400-e29b-41d4-a716-446655440000';
```

### 느린 쿼리 로그 활성화
```sql
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 1;  -- 1초 이상 걸리는 쿼리 로그
```

---

## 보안 고려사항

1. **비밀번호 암호화**: BCrypt 사용 (Spring Security)
2. **SQL Injection 방지**: JPA Prepared Statement 사용
3. **데이터베이스 사용자 권한 최소화**:
   ```sql
   -- 애플리케이션 전용 사용자 생성 (권장)
   CREATE USER 'app_user'@'localhost' IDENTIFIED BY 'secure_password';
   GRANT SELECT, INSERT, UPDATE, DELETE ON company_analyzer.* TO 'app_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

---

## 관련 파일

- `schema.sql` - 데이터베이스 스키마 DDL
- `sample_data.sql` - 테스트용 샘플 데이터
- `README.md` - 상세 설계 문서
- `DB_SETUP_GUIDE.md` - 설정 가이드 (현재 파일)

---

## 참고 자료

- [Spring Boot + MySQL 연동 가이드](https://spring.io/guides/gs/accessing-data-mysql/)
- [MySQL 8.0 공식 문서](https://dev.mysql.com/doc/refman/8.0/en/)
- [JPA Entity 설계 가이드](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
