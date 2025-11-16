-- ============================================================
-- Company Analyzer - Sample Data
-- ============================================================
-- 테스트용 샘플 데이터
-- 실제 운영 환경에서는 사용하지 마세요!
-- ============================================================

USE company_analyzer;

-- ============================================================
-- 1. Member 샘플 데이터
-- ============================================================
-- 비밀번호: 모두 "password123" (BCrypt 암호화)
-- BCrypt 해시: $2a$10$N9qo8uLOickgx2ZMRZoMye/jlW8A.0/HsZKJVZc3Z7kLqTzQH3T6S
-- ============================================================

INSERT INTO member (user_code, email, password, name, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'test1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/jlW8A.0/HsZKJVZc3Z7kLqTzQH3T6S', '홍길동', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440001', 'test2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/jlW8A.0/HsZKJVZc3Z7kLqTzQH3T6S', '김철수', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440002', 'test3@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/jlW8A.0/HsZKJVZc3Z7kLqTzQH3T6S', '이영희', NOW(), NOW());


-- ============================================================
-- 2. Company 샘플 데이터
-- ============================================================
-- 주요 상장사 정보 (DART API 기반)
-- ============================================================

INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code,
    ceo_nm, corp_cls, jurir_no, bizr_no, adres,
    hm_url, ir_url, phn_no, induty_code, est_dt, acc_mt,
    created_at, updated_at
) VALUES
-- 삼성전자
('00126380', '삼성전자주식회사', 'SAMSUNG ELECTRONICS CO., LTD.', '삼성전자', '005930',
 '한종희, 경계현', 'Y', '1301110006246', '1248100998', '경기도 수원시 영통구 삼성로 129 (매탄동)',
 'http://www.samsung.com/sec', 'http://www.samsung.com/sec/ir', '031-200-1114', '26410', '19690113', '12',
 NOW(), NOW()),

-- SK하이닉스
('00164779', '에스케이하이닉스 주식회사', 'SK hynix Inc.', 'SK하이닉스', '000660',
 '곽노정', 'Y', '1301110330762', '1248812617', '경기도 이천시 부발읍 경충대로 2091',
 'http://www.skhynix.com', 'http://www.skhynix.com/investor.jsp', '031-630-4114', '26121', '19831214', '12',
 NOW(), NOW()),

-- NAVER
('00138849', '네이버 주식회사', 'NAVER Corporation', 'NAVER', '035420',
 '최수연', 'Y', '1301111140190', '2208100767', '경기도 성남시 분당구 정자일로 95 (정자동)',
 'http://www.navercorp.com', 'http://ir.navercorp.com', '031-784-1114', '63991', '19990606', '12',
 NOW(), NOW()),

-- 카카오
('00183166', '주식회사 카카오', 'Kakao Corp.', '카카오', '035720',
 '홍은택', 'Y', '1101111044923', '1208181476', '경기도 성남시 분당구 판교역로 235 (삼평동)',
 'http://www.kakaocorp.com', 'http://ir.kakaocorp.com', '1577-3321', '63991', '19951216', '12',
 NOW(), NOW()),

-- LG전자
('00174832', '엘지전자 주식회사', 'LG Electronics Inc.', 'LG전자', '066570',
 '조주완, 배두용', 'Y', '1101110004004', '1078186361', '서울특별시 영등포구 여의대로 128 (여의도동)',
 'http://www.lge.co.kr', 'http://www.lge.co.kr/investor', '02-3777-1114', '26400', '19580928', '12',
 NOW(), NOW()),

-- 현대차
('00164365', '현대자동차 주식회사', 'Hyundai Motor Company', '현대차', '005380',
 '장재훈', 'Y', '1301110192217', '1248121576', '서울특별시 서초구 헌릉로 12 (양재동)',
 'http://www.hyundai.com', 'http://www.hyundai.com/kr/ko/ir', '02-3464-1114', '30120', '19671229', '12',
 NOW(), NOW());


-- ============================================================
-- 3. Stock 샘플 데이터 (관심기업)
-- ============================================================
-- 사용자별 관심기업 등록
-- ============================================================

INSERT INTO stock (user_code, stock_code, corp_code, registered_at, created_at, updated_at) VALUES
-- 홍길동의 관심기업
('550e8400-e29b-41d4-a716-446655440000', '005930', '00126380', NOW(), NOW(), NOW()), -- 삼성전자
('550e8400-e29b-41d4-a716-446655440000', '000660', '00164779', NOW(), NOW(), NOW()), -- SK하이닉스
('550e8400-e29b-41d4-a716-446655440000', '035420', '00138849', NOW(), NOW(), NOW()), -- NAVER

-- 김철수의 관심기업
('550e8400-e29b-41d4-a716-446655440001', '035720', '00183166', NOW(), NOW(), NOW()), -- 카카오
('550e8400-e29b-41d4-a716-446655440001', '066570', '00174832', NOW(), NOW(), NOW()), -- LG전자

-- 이영희의 관심기업
('550e8400-e29b-41d4-a716-446655440002', '005380', '00164365', NOW(), NOW(), NOW()), -- 현대차
('550e8400-e29b-41d4-a716-446655440002', '005930', '00126380', NOW(), NOW(), NOW()); -- 삼성전자


-- ============================================================
-- 4. Memo 샘플 데이터
-- ============================================================
-- 사용자가 관심기업에 작성한 메모
-- ============================================================

INSERT INTO memo (user_code, stock_code, content, created_at, updated_at) VALUES
-- 홍길동의 메모
('550e8400-e29b-41d4-a716-446655440000', '005930',
 '삼성전자 투자 검토 중
- 반도체 업황 회복 기대
- 갤럭시 S24 출시 예정
- 배당 수익률 2.5% 수준', NOW(), NOW()),

('550e8400-e29b-41d4-a716-446655440000', '000660',
 'SK하이닉스 - HBM3 시장 점유율 확대
D램 가격 반등 가능성', NOW(), NOW()),

-- 김철수의 메모
('550e8400-e29b-41d4-a716-446655440001', '035720',
 '카카오 - AI 사업 확장 중
카카오톡 광고 매출 증가세', NOW(), NOW()),

-- 이영희의 메모
('550e8400-e29b-41d4-a716-446655440002', '005380',
 '현대차 - 전기차 라인업 강화
미국 IRA 수혜주', NOW(), NOW());


-- ============================================================
-- 샘플 데이터 삽입 완료
-- ============================================================
-- 사용자: 3명
-- 기업: 6개 (삼성전자, SK하이닉스, NAVER, 카카오, LG전자, 현대차)
-- 관심기업: 7개
-- 메모: 4개
-- ============================================================

-- 데이터 확인 쿼리
SELECT '=== Member 데이터 ===' AS '';
SELECT user_code, email, name FROM member;

SELECT '=== Company 데이터 ===' AS '';
SELECT corp_code, corp_name, stock_code FROM company;

SELECT '=== Stock 데이터 ===' AS '';
SELECT s.id, m.name AS user_name, c.corp_name, s.stock_code
FROM stock s
JOIN member m ON s.user_code = m.user_code
JOIN company c ON s.corp_code = c.corp_code;

SELECT '=== Memo 데이터 ===' AS '';
SELECT m.id, mem.name AS user_name, m.stock_code, LEFT(m.content, 30) AS content_preview
FROM memo m
JOIN member mem ON m.user_code = mem.user_code;
