-- ============================================================
-- Company Analyzer Database Schema
-- ============================================================
-- 데이터베이스: company_analyzer
-- 문자셋: utf8mb4 (이모지 및 다국어 지원)
-- Collation: utf8mb4_unicode_ci
-- ============================================================

-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS company_analyzer
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE company_analyzer;

-- ============================================================
-- 1. Member 테이블 (사용자 정보)
-- ============================================================
-- 설명: 사용자 계정 정보를 저장하는 테이블
-- PK: user_code (UUID 형식)
-- ============================================================

CREATE TABLE IF NOT EXISTS member (
    user_code VARCHAR(50) NOT NULL COMMENT '사용자 코드 (UUID)',
    email VARCHAR(100) NOT NULL COMMENT '이메일 (로그인 ID)',
    password VARCHAR(255) NOT NULL COMMENT '비밀번호 (BCrypt 암호화)',
    name VARCHAR(50) NOT NULL COMMENT '사용자 이름',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',

    PRIMARY KEY (user_code),
    UNIQUE KEY uk_email (email),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사용자 정보 테이블';


-- ============================================================
-- 2. Company 테이블 (기업 정보)
-- ============================================================
-- 설명: DART API에서 조회한 기업 정보를 저장하는 테이블
-- PK: corp_code (DART API 8자리 고유번호)
-- ============================================================

CREATE TABLE IF NOT EXISTS company (
    corp_code VARCHAR(8) NOT NULL COMMENT '고유번호 (DART API)',
    corp_name VARCHAR(200) NOT NULL COMMENT '정식명칭',
    corp_name_eng VARCHAR(200) NULL COMMENT '영문명칭',
    stock_name VARCHAR(100) NULL COMMENT '종목명 또는 약식명칭',
    stock_code VARCHAR(6) NULL COMMENT '주식 종목코드 (6자리, 상장사만)',
    ceo_nm VARCHAR(100) NULL COMMENT '대표자명',
    corp_cls VARCHAR(1) NULL COMMENT '법인구분 (Y=상장, K=코넥스, N=비상장, E=기타)',
    jurir_no VARCHAR(13) NULL COMMENT '법인등록번호',
    bizr_no VARCHAR(10) NULL COMMENT '사업자등록번호',
    adres VARCHAR(500) NULL COMMENT '주소',
    hm_url VARCHAR(200) NULL COMMENT '홈페이지',
    ir_url VARCHAR(200) NULL COMMENT 'IR홈페이지',
    phn_no VARCHAR(20) NULL COMMENT '전화번호',
    fax_no VARCHAR(20) NULL COMMENT '팩스번호',
    induty_code VARCHAR(10) NULL COMMENT '업종코드 (KSIC)',
    est_dt VARCHAR(8) NULL COMMENT '설립일 (YYYYMMDD)',
    acc_mt VARCHAR(2) NULL COMMENT '결산월 (MM)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',

    PRIMARY KEY (corp_code),
    INDEX idx_corp_name (corp_name),
    INDEX idx_stock_code (stock_code),
    INDEX idx_induty_code (induty_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='기업 정보 테이블 (DART API)';


-- ============================================================
-- 3. Stock 테이블 (관심기업)
-- ============================================================
-- 설명: 사용자의 관심기업 목록을 저장하는 테이블
-- PK: id (Auto Increment)
-- FK: user_code -> member.user_code
-- FK: corp_code -> company.corp_code
-- ============================================================

CREATE TABLE IF NOT EXISTS stock (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '관심기업 ID',
    user_code VARCHAR(50) NOT NULL COMMENT '사용자 코드 (FK)',
    stock_code VARCHAR(6) NOT NULL COMMENT '주식 종목코드',
    corp_code VARCHAR(8) NOT NULL COMMENT '기업 코드 (FK)',
    registered_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '관심기업 등록일시',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',

    PRIMARY KEY (id),
    UNIQUE KEY uk_user_stock (user_code, stock_code) COMMENT '사용자당 동일 종목 중복 방지',
    INDEX idx_user_code (user_code),
    INDEX idx_stock_code (stock_code),
    INDEX idx_corp_code (corp_code),

    CONSTRAINT fk_stock_member FOREIGN KEY (user_code)
        REFERENCES member(user_code) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_stock_company FOREIGN KEY (corp_code)
        REFERENCES company(corp_code) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='관심기업 테이블';


-- ============================================================
-- 4. Memo 테이블 (메모)
-- ============================================================
-- 설명: 사용자가 관심기업에 대해 작성한 메모를 저장하는 테이블
-- PK: id (Auto Increment)
-- FK: user_code -> member.user_code
-- ============================================================

CREATE TABLE IF NOT EXISTS memo (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '메모 ID',
    user_code VARCHAR(50) NOT NULL COMMENT '사용자 코드 (FK)',
    stock_code VARCHAR(6) NOT NULL COMMENT '주식 종목코드',
    content VARCHAR(2000) NULL COMMENT '메모 내용 (최대 2000자)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',

    PRIMARY KEY (id),
    UNIQUE KEY uk_user_stock_memo (user_code, stock_code) COMMENT '사용자당 종목별 메모 하나만',
    INDEX idx_memo_user_code (user_code),
    INDEX idx_memo_stock_code (stock_code),

    CONSTRAINT fk_memo_member FOREIGN KEY (user_code)
        REFERENCES member(user_code) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='메모 테이블';


-- ============================================================
-- 테이블 생성 완료
-- ============================================================
-- 총 4개 테이블:
-- 1. member    - 사용자 정보
-- 2. company   - 기업 정보 (DART API)
-- 3. stock     - 관심기업
-- 4. memo      - 메모
-- ============================================================
