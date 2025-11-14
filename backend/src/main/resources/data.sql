-- Company 테이블 초기 데이터
-- dart-company-mock.json 기반으로 작성된 샘플 기업 데이터

-- 삼성전자(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt
) VALUES (
    '00126380', '삼성전자(주)', 'SAMSUNG ELECTRONICS CO,.LTD', '삼성전자', '005930', '전영현', 'Y',
    '1301110006246', '1248100998', '경기도 수원시 영통구  삼성로 129 (매탄동)', 'www.samsung.com/sec', '', '02-2255-0114', '031-200-7538', '264', '19690113', '12'
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 에스케이하이닉스(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt
) VALUES (
    '00164779', '에스케이하이닉스(주)', 'SK hynix Inc.', 'SK하이닉스', '000660', '곽노정', 'Y',
    '1344110001387', '1268103725', '경기도 이천시  부발읍 경충대로 2091', 'www.skhynix.com', '', '031-630-4114', '031-645-8078', '2612', '19491015', '12'
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 현대건설(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt
) VALUES (
    '00164478', '현대건설(주)', 'HYUNDAI ENGINEERING & CONSTRUCTION CO.,LTD', '현대건설', '000720', '이한우', 'Y',
    '1101110007909', '1018116293', '서울특별시 종로구  율곡로 75', 'www.hdec.kr', 'https://www.hdec.kr/kr/invest/irpt.aspx', '02-746-1114', '02-746-4846', '41221', '19500110', '12'
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 에이치디씨현대산업개발(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt
) VALUES (
    '01310269', '에이치디씨현대산업개발(주)', 'HYUNDAI DEVELOPMENT COMPANY', 'HDC현대산업개발', '294870', '정경구, 조태제(각자 대표이사)', 'Y',
    '1101116740008', '3348600815', '서울특별시 용산구 한강대로23길 55 현대아이파크몰 9층', 'www.hdc-dvp.com', '', '02-2008-9114', '', '411', '20180502', '12'
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 에이치디현대중공업(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt
) VALUES (
    '01390344', '에이치디현대중공업(주)', 'HD HYUNDAI HEAVY INDUSTRIES CO.,LTD.', 'HD현대중공업', '329180', '이상균, 노진율 (각자 대표이사)', 'Y',
    '2301110312741', '2528701412', '울산광역시 동구 방어진순환도로 1000', 'www.hhi.co.kr', '', '052-202-2114', '052-202-3315', '31113', '20190603', '12'
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 한화오션(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt
) VALUES (
    '00111704', '한화오션(주)', 'Hanwha Ocean Co., Ltd.', '한화오션', '042660', '김희철', 'Y',
    '1101112095837', '1048157667', '경상남도 거제시 거제대로 3370 (아주동)', 'www.hanwhaocean.com/pub/main/index.do', '', '055-735-2114', '02-2129-0084', '3111', '20001023', '12'
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 한국항공우주산업(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt
) VALUES (
    '00309503', '한국항공우주산업(주)', 'KOREA AEROSPACE INDUSTRIES, LTD.', '한국항공우주', '047810', '차재병', 'Y',
    '1101111827570', '1108142397', '경상남도 사천시  사남면 공단1로 78 한국항공우주산업', 'www.koreaaero.com', '', '055-851-1000', '055-851-9049', '31311', '19991001', '12'
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- (주)엘지에너지솔루션
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt
) VALUES (
    '01515323', '(주)엘지에너지솔루션', 'LG ENERGY SOLUTION, LTD.', 'LG에너지솔루션', '373220', '김동명', 'Y',
    '1101117701356', '8518102050', '서울특별시 영등포구 여의대로 108, 타워 1', 'www.lgensol.com', 'https://www.lgensol.com/kr/earnings-announcement', '02-3777-1114', '', '28202', '20201201', '12'
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 주식회사 포스코
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt
) VALUES (
    '01620971', '주식회사 포스코', 'POSCO', '포스코', '', '이희근', 'E',
    '1717110171410', '3018702315', '경상북도 포항시 남구 동해안로 6261', 'www.posco.co.kr', '', '054-220-0114', '0562-220-6000', '2411', '20220302', '12'
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);

-- 고려아연(주)
INSERT INTO company (
    corp_code, corp_name, corp_name_eng, stock_name, stock_code, ceo_nm, corp_cls,
    jurir_no, bizr_no, adres, hm_url, ir_url, phn_no, fax_no, induty_code, est_dt, acc_mt
) VALUES (
    '00102858', '고려아연(주)', 'KOREA ZINC INC', '고려아연', '010130', '박기덕, 정태웅', 'Y',
    '1101110168404', '2118111260', '서울특별시 종로구 종로33 (청진동) -', 'www.koreazinc.co.kr', '', '02-6947-2114', '549-8245', '24213', '19740801', '12'
) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP(6);
