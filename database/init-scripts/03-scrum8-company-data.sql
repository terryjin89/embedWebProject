-- ============================================================
-- Company Analyzer - SCRUM-8 Company Data
-- ============================================================
-- 기업정보 게시판용 10개 주요 기업
-- CORPCODE.xml에서 추출한 실제 기업 정보
-- ============================================================

-- UTF-8 인코딩 설정 (한글 깨짐 방지)
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;
SET CHARACTER SET utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;

-- Note: DB name is controlled by docker-compose environment variables
-- USE statement removed to allow flexible database selection

-- 1. 삼성전자(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt,
    created_at, updated_at
) VALUES (
    '00126380', '삼성전자', 'SAMSUNG ELECTRONICS CO,.LTD', '삼성전자', '005930', '전영현', 'Y',
    '1301110006246', '1248100998', '경기도 수원시 영통구 삼성로 129 (매탄동)', 'www.samsung.com/sec', '', '02-2255-0114', '031-200-7538', '264', '19690113', '12',
    NOW(), NOW()
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 2. SK하이닉스(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt,
    created_at, updated_at
) VALUES (
    '00164779', 'SK하이닉스', 'SK hynix Inc.', 'SK하이닉스', '000660', '곽노정', 'Y',
    '1344110001387', '1268103725', '경기도 이천시 부발읍 경충대로 2091', 'www.skhynix.com', '', '031-630-4114', '031-645-8078', '2612', '19491015', '12',
    NOW(), NOW()
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 3. HD현대중공업(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt,
    created_at, updated_at
) VALUES (
    '01390344', 'HD현대중공업', 'HD HYUNDAI HEAVY INDUSTRIES CO.,LTD.', 'HD현대중공업', '329180', '이상균, 노진율', 'Y',
    '2301110312741', '2528701412', '울산광역시 동구 방어진순환도로 1000', 'www.hhi.co.kr', '', '052-202-2114', '052-202-3315', '31113', '20190603', '12',
    NOW(), NOW()
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 4. 한화오션(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt,
    created_at, updated_at
) VALUES (
    '00111704', '한화오션', 'Hanwha Ocean Co., Ltd.', '한화오션', '042660', '김희철', 'Y',
    '1101112095837', '1048157667', '경상남도 거제시 거제대로 3370 (아주동)', 'www.hanwhaocean.com/pub/main/index.do', '', '055-735-2114', '02-2129-0084', '3111', '20001023', '12',
    NOW(), NOW()
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 5. 현대건설(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt,
    created_at, updated_at
) VALUES (
    '00164478', '현대건설', 'HYUNDAI ENGINEERING & CONSTRUCTION CO.,LTD', '현대건설', '000720', '이한우', 'Y',
    '1101110007909', '1018116293', '서울특별시 종로구 율곡로 75', 'www.hdec.kr', 'https://www.hdec.kr/kr/invest/irpt.aspx', '02-746-1114', '02-746-4846', '41221', '19500110', '12',
    NOW(), NOW()
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 6. 삼성물산(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt,
    created_at, updated_at
) VALUES (
    '00149655', '삼성물산', 'SAMSUNG C&T CORPORATION', '삼성물산', '028260', '오세철', 'Y',
    '1301110006361', '1068600606', '서울특별시 서초구 서초대로74길 11 (서초동)', 'www.samsungcnt.com', '', '02-2145-5114', '', '46', '19380101', '12',
    NOW(), NOW()
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 7. 포스코(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt,
    created_at, updated_at
) VALUES (
    '01620971', '포스코', 'POSCO', '포스코', '', '이희근', 'E',
    '1717110171410', '3018702315', '경상북도 포항시 남구 동해안로 6261', 'www.posco.co.kr', '', '054-220-0114', '054-220-6000', '2411', '20220302', '12',
    NOW(), NOW()
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 8. NHN(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt,
    created_at, updated_at
) VALUES (
    '00983271', 'NHN', 'NHN Corporation', 'NHN', '181710', '정우진', 'Y',
    '1101111050178', '2208137109', '경기도 성남시 분당구 대왕판교로645번길 16', 'www.nhn.com', '', '1544-6859', '', '582', '20130529', '12',
    NOW(), NOW()
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 9. 카카오(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt,
    created_at, updated_at
) VALUES (
    '00258801', '카카오', 'Kakao Corp.', '카카오', '035720', '정신아', 'Y',
    '1301110183166', '1208800767', '경기도 성남시 분당구 판교역로 235 (삼평동)', 'www.kakaocorp.com', '', '1577-3754', '', '5820', '19950216', '12',
    NOW(), NOW()
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 10. 고려아연(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt,
    created_at, updated_at
) VALUES (
    '00102858', '고려아연', 'KOREA ZINC INC', '고려아연', '010130', '박기덕, 정태웅', 'Y',
    '1101110168404', '2118111260', '서울특별시 종로구 종로 33 (청진동)', 'www.koreazinc.co.kr', '', '02-6947-2114', '02-549-8245', '24213', '19740801', '12',
    NOW(), NOW()
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);
