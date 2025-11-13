2.DART 전자공시시스템
인증키 : d76b2823154aff2001264dd25f0cc7bf256c6c7b
요청 URL : https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON

요청변수(Requester Parameter) : 
1.crtfc_key[String(40)] - 인증키
2.corp_code[String(8)] - 고유번호
예시 : https://opendart.fss.or.kr/api/company.json?crtfc_key=d76b2823154aff2001264dd25f0cc7bf256c6c7b&corp_code=00126380

출력결과(Response Element) :
"status": 에러 및 정보 코드(예시아래참조),
"message": 에러 및 정보 메시지(예시아래참조),
"corp_code": 고유번호,
"corp_name": 정식명칭,
"corp_name_eng": 영문명칭,
"stock_name": 종목명(상장사) 또는 약식명칭(기타법인),
"stock_code": 상장회사인 경우 주식의 종목코드,
"ceo_nm": 대표자명,
"corp_cls": 법인구분,
"jurir_no": 법인등록번호,
"bizr_no": 사업자등록번호,
"adres": 주소,
"hm_url": 홈페이지,
"ir_url": IR홈페이지,
"phn_no": 전화번호,
"fax_no": 팩스번호,
"induty_code": 업종코드,
"est_dt": 설립일(YYYYMMDD),
"acc_mt": 결산월(MM)

예시
"status": "000",
"message": "정상",
"corp_code": "00126380",
"corp_name": "삼성전자(주)",
"corp_name_eng": "SAMSUNG ELECTRONICS CO,.LTD",
"stock_name": "삼성전자",
"stock_code": "005930",
"ceo_nm": "전영현",
"corp_cls": "Y",
"jurir_no": "1301110006246",
"bizr_no": "1248100998",
"adres": "경기도 수원시 영통구  삼성로 129 (매탄동)",
"hm_url": "www.samsung.com/sec",
"ir_url": "",
"phn_no": "02-2255-0114",
"fax_no": "031-200-7538",
"induty_code": "264",
"est_dt": "19690113",
"acc_mt": "12"
