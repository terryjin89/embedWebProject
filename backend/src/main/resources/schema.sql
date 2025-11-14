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
