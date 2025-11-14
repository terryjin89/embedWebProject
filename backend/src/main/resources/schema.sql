-- Member 테이블 생성
-- 사용자 정보를 저장하는 테이블

CREATE TABLE IF NOT EXISTS member (
    -- Primary Key
    user_code VARCHAR(50) NOT NULL PRIMARY KEY COMMENT '사용자 코드 (UUID)',

    -- 사용자 정보
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '이메일 (로그인 ID)',
    password VARCHAR(255) NOT NULL COMMENT '비밀번호 (BCrypt 암호화)',
    name VARCHAR(50) NOT NULL COMMENT '사용자 이름',

    -- 타임스탬프
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성 시간',
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정 시간',

    -- 인덱스
    INDEX idx_email (email) COMMENT '이메일 검색용 인덱스'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='회원 정보 테이블';

-- Company 테이블 생성
-- DART API에서 조회한 기업 정보를 저장하는 테이블

CREATE TABLE IF NOT EXISTS company (
    -- Primary Key
    corp_code VARCHAR(8) NOT NULL PRIMARY KEY COMMENT '고유번호 (DART API 제공)',

    -- 기업 기본 정보
    corp_name VARCHAR(200) NOT NULL COMMENT '정식명칭',
    corp_name_eng VARCHAR(200) COMMENT '영문명칭',
    stock_name VARCHAR(100) COMMENT '종목명 또는 약식명칭',
    stock_code VARCHAR(6) COMMENT '주식 종목코드 (상장사만 해당)',
    ceo_nm VARCHAR(100) COMMENT '대표자명',
    corp_cls VARCHAR(1) COMMENT '법인구분 (Y=상장, K=코넥스, N=비상장, E=기타)',

    -- 등록번호
    jurir_no VARCHAR(13) COMMENT '법인등록번호',
    bizr_no VARCHAR(10) COMMENT '사업자등록번호',

    -- 연락처 및 주소
    adres VARCHAR(500) COMMENT '주소',
    hm_url VARCHAR(200) COMMENT '홈페이지',
    ir_url VARCHAR(200) COMMENT 'IR홈페이지',
    phn_no VARCHAR(20) COMMENT '전화번호',
    fax_no VARCHAR(20) COMMENT '팩스번호',

    -- 업종 및 설립 정보
    induty_code VARCHAR(10) COMMENT '업종코드 (한국표준산업분류)',
    est_dt VARCHAR(8) COMMENT '설립일 (YYYYMMDD)',
    acc_mt VARCHAR(2) COMMENT '결산월 (MM)',

    -- 타임스탬프
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성 시간',
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정 시간',

    -- 인덱스
    INDEX idx_corp_name (corp_name) COMMENT '기업명 검색용 인덱스',
    INDEX idx_stock_code (stock_code) COMMENT '종목코드 검색용 인덱스',
    INDEX idx_induty_code (induty_code) COMMENT '업종코드 필터링용 인덱스'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='기업 정보 테이블';

-- Stock 테이블 생성
-- 사용자의 관심기업 정보를 저장하는 테이블

CREATE TABLE IF NOT EXISTS stock (
    -- Primary Key
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '관심기업 고유 ID',

    -- Foreign Keys
    user_code VARCHAR(50) NOT NULL COMMENT '사용자 코드 (FK -> member.user_code)',
    corp_code VARCHAR(8) NOT NULL COMMENT '기업 코드 (FK -> company.corp_code)',
    stock_code VARCHAR(6) NOT NULL COMMENT '주식 종목코드 (6자리)',

    -- 등록 정보
    registered_at DATETIME(6) NOT NULL COMMENT '관심기업 등록일시',

    -- 타임스탬프
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성 시간',
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정 시간',

    -- 제약 조건 (중복 등록 방지)
    CONSTRAINT uk_user_stock UNIQUE (user_code, stock_code) COMMENT '사용자당 동일 종목 중복 등록 방지',

    -- 외래키 제약 조건
    CONSTRAINT fk_stock_member FOREIGN KEY (user_code) REFERENCES member(user_code) ON DELETE CASCADE COMMENT '회원 삭제 시 관심기업도 삭제',
    CONSTRAINT fk_stock_company FOREIGN KEY (corp_code) REFERENCES company(corp_code) ON DELETE CASCADE COMMENT '기업 삭제 시 관심기업도 삭제',

    -- 인덱스
    INDEX idx_user_code (user_code) COMMENT '사용자별 관심기업 조회용 인덱스',
    INDEX idx_stock_code (stock_code) COMMENT '종목코드 검색용 인덱스',
    INDEX idx_corp_code (corp_code) COMMENT '기업코드 검색용 인덱스',
    INDEX idx_registered_at (registered_at DESC) COMMENT '등록일시 정렬용 인덱스'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='관심기업 테이블';
