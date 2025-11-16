# Company Analyzer Database 설계

## 개요

Company Analyzer 프로젝트의 MySQL 데이터베이스 스키마 설계 문서입니다.

## 데이터베이스 정보

- **데이터베이스명**: `company_analyzer`
- **문자셋**: `utf8mb4` (이모지 및 다국어 지원)
- **Collation**: `utf8mb4_unicode_ci`
- **엔진**: InnoDB

## ERD (Entity Relationship Diagram)

```
┌──────────────┐
│   Member     │
│──────────────│
│ user_code PK │─┐
│ email        │ │
│ password     │ │
│ name         │ │
│ created_at   │ │
│ updated_at   │ │
└──────────────┘ │
                 │
                 │ 1:N
                 │
┌──────────────┐ │      ┌──────────────┐
│   Company    │ │      │    Stock     │
│──────────────│ │      │──────────────│
│ corp_code PK │─┼─────<│ id PK        │
│ corp_name    │ │      │ user_code FK │>──┘
│ stock_code   │ │      │ stock_code   │
│ stock_name   │ │      │ corp_code FK │
│ ...          │ │      │ registered_at│
│ created_at   │ │      │ created_at   │
│ updated_at   │ │      │ updated_at   │
└──────────────┘ │      └──────────────┘
                 │
                 │      ┌──────────────┐
                 │      │    Memo      │
                 │      │──────────────│
                 │      │ id PK        │
                 └─────<│ user_code FK │
                        │ stock_code   │
                        │ content      │
                        │ created_at   │
                        │ updated_at   │
                        └──────────────┘
```

## 테이블 구조

### 1. member (사용자 정보)

사용자 계정 정보를 저장하는 테이블

| 컬럼명 | 데이터 타입 | 제약조건 | 설명 |
|--------|------------|---------|------|
| user_code | VARCHAR(50) | PK, NOT NULL | 사용자 코드 (UUID) |
| email | VARCHAR(100) | UNIQUE, NOT NULL | 이메일 (로그인 ID) |
| password | VARCHAR(255) | NOT NULL | 비밀번호 (BCrypt 암호화) |
| name | VARCHAR(50) | NOT NULL | 사용자 이름 |
| created_at | DATETIME | NOT NULL | 생성 시간 |
| updated_at | DATETIME | NOT NULL | 수정 시간 |

**인덱스**:
- `PRIMARY KEY`: user_code
- `UNIQUE KEY`: email
- `INDEX`: email

**특징**:
- user_code는 UUID 형식으로 자동 생성 (Java @PrePersist)
- password는 BCrypt 해시 알고리즘으로 암호화
- JWT 토큰의 subject로 user_code 사용

---

### 2. company (기업 정보)

DART API에서 조회한 기업 정보를 저장하는 테이블

| 컬럼명 | 데이터 타입 | 제약조건 | 설명 |
|--------|------------|---------|------|
| corp_code | VARCHAR(8) | PK, NOT NULL | 고유번호 (DART API) |
| corp_name | VARCHAR(200) | NOT NULL | 정식명칭 |
| corp_name_eng | VARCHAR(200) | NULL | 영문명칭 |
| stock_name | VARCHAR(100) | NULL | 종목명 또는 약식명칭 |
| stock_code | VARCHAR(6) | NULL | 주식 종목코드 (상장사만) |
| ceo_nm | VARCHAR(100) | NULL | 대표자명 |
| corp_cls | VARCHAR(1) | NULL | 법인구분 (Y/K/N/E) |
| jurir_no | VARCHAR(13) | NULL | 법인등록번호 |
| bizr_no | VARCHAR(10) | NULL | 사업자등록번호 |
| adres | VARCHAR(500) | NULL | 주소 |
| hm_url | VARCHAR(200) | NULL | 홈페이지 |
| ir_url | VARCHAR(200) | NULL | IR홈페이지 |
| phn_no | VARCHAR(20) | NULL | 전화번호 |
| fax_no | VARCHAR(20) | NULL | 팩스번호 |
| induty_code | VARCHAR(10) | NULL | 업종코드 (KSIC) |
| est_dt | VARCHAR(8) | NULL | 설립일 (YYYYMMDD) |
| acc_mt | VARCHAR(2) | NULL | 결산월 (MM) |
| created_at | DATETIME | NOT NULL | 생성 시간 |
| updated_at | DATETIME | NOT NULL | 수정 시간 |

**인덱스**:
- `PRIMARY KEY`: corp_code
- `INDEX`: corp_name
- `INDEX`: stock_code
- `INDEX`: induty_code

**법인구분 (corp_cls)**:
- `Y`: 유가증권시장, 코스닥, 코넥스 상장회사
- `K`: 콘텍스 상장회사
- `N`: 비상장회사
- `E`: 기타법인

---

### 3. stock (관심기업)

사용자의 관심기업 목록을 저장하는 테이블

| 컬럼명 | 데이터 타입 | 제약조건 | 설명 |
|--------|------------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 관심기업 ID |
| user_code | VARCHAR(50) | FK, NOT NULL | 사용자 코드 |
| stock_code | VARCHAR(6) | NOT NULL | 주식 종목코드 |
| corp_code | VARCHAR(8) | FK, NOT NULL | 기업 코드 |
| registered_at | DATETIME | NOT NULL | 관심기업 등록일시 |
| created_at | DATETIME | NOT NULL | 생성 시간 |
| updated_at | DATETIME | NOT NULL | 수정 시간 |

**인덱스**:
- `PRIMARY KEY`: id
- `UNIQUE KEY`: (user_code, stock_code) - 사용자당 동일 종목 중복 방지
- `INDEX`: user_code
- `INDEX`: stock_code
- `INDEX`: corp_code

**외래키**:
- `fk_stock_member`: user_code → member.user_code (ON DELETE CASCADE)
- `fk_stock_company`: corp_code → company.corp_code (ON DELETE CASCADE)

**특징**:
- 사용자는 동일한 종목을 중복 등록할 수 없음 (UNIQUE 제약)
- 사용자 삭제 시 관련 관심기업도 함께 삭제 (CASCADE)

---

### 4. memo (메모)

사용자가 관심기업에 대해 작성한 메모를 저장하는 테이블

| 컬럼명 | 데이터 타입 | 제약조건 | 설명 |
|--------|------------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 메모 ID |
| user_code | VARCHAR(50) | FK, NOT NULL | 사용자 코드 |
| stock_code | VARCHAR(6) | NOT NULL | 주식 종목코드 |
| content | VARCHAR(2000) | NULL | 메모 내용 (최대 2000자) |
| created_at | DATETIME | NOT NULL | 생성 시간 |
| updated_at | DATETIME | NOT NULL | 수정 시간 |

**인덱스**:
- `PRIMARY KEY`: id
- `UNIQUE KEY`: (user_code, stock_code) - 사용자당 종목별 메모 하나만
- `INDEX`: user_code
- `INDEX`: stock_code

**외래키**:
- `fk_memo_member`: user_code → member.user_code (ON DELETE CASCADE)

**특징**:
- 사용자는 종목당 하나의 메모만 작성 가능 (UNIQUE 제약)
- 메모 내용은 최대 2000자까지 저장 가능
- 사용자 삭제 시 관련 메모도 함께 삭제 (CASCADE)

---

## 데이터베이스 설치 및 설정

### 1. MySQL 데이터베이스 생성

```bash
# MySQL 접속
mysql -u root -p

# 스키마 실행
source C:/Github/embedWebProject/database/schema.sql

# 또는
mysql -u root -p < C:/Github/embedWebProject/database/schema.sql
```

### 2. Spring Boot 설정 (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/company_analyzer?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: root
    password: your_password_here
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: validate  # 운영: validate, 개발: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect
```

### 3. 환경 변수 설정 (선택사항)

```bash
# Windows
set DB_USERNAME=root
set DB_PASSWORD=your_password

# Linux/Mac
export DB_USERNAME=root
export DB_PASSWORD=your_password
```

---

## 테스트 데이터

테스트용 샘플 데이터는 `sample_data.sql` 파일을 참조하세요.

```bash
mysql -u root -p company_analyzer < database/sample_data.sql
```

---

## 주요 쿼리 예제

### 사용자 등록
```sql
INSERT INTO member (user_code, email, password, name)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'user@example.com', '$2a$10$...', '홍길동');
```

### 관심기업 등록
```sql
INSERT INTO stock (user_code, stock_code, corp_code, registered_at)
VALUES ('550e8400-e29b-41d4-a716-446655440000', '005930', '00126380', NOW());
```

### 사용자별 관심기업 조회
```sql
SELECT s.*, c.corp_name, c.stock_name
FROM stock s
INNER JOIN company c ON s.corp_code = c.corp_code
WHERE s.user_code = '550e8400-e29b-41d4-a716-446655440000'
ORDER BY s.registered_at DESC;
```

### 메모 저장/수정
```sql
INSERT INTO memo (user_code, stock_code, content)
VALUES ('550e8400-e29b-41d4-a716-446655440000', '005930', '삼성전자 투자 검토 중')
ON DUPLICATE KEY UPDATE content = VALUES(content), updated_at = NOW();
```

---

## 데이터베이스 백업 및 복구

### 백업
```bash
mysqldump -u root -p company_analyzer > backup_$(date +%Y%m%d).sql
```

### 복구
```bash
mysql -u root -p company_analyzer < backup_20250116.sql
```

---

## 참고사항

1. **문자셋**: utf8mb4를 사용하여 이모지 및 다국어 지원
2. **외래키**: CASCADE 옵션으로 데이터 무결성 보장
3. **인덱스**: 자주 조회되는 컬럼에 인덱스 설정 (성능 최적화)
4. **보안**:
   - 비밀번호는 BCrypt로 암호화
   - JWT 토큰 기반 인증
   - user_code는 UUID로 노출 방지

---

## 관련 파일

- `schema.sql` - 데이터베이스 스키마 DDL
- `sample_data.sql` - 테스트용 샘플 데이터
- `README.md` - 데이터베이스 설계 문서 (현재 파일)
