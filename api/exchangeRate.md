1.한국수출입은행
국제환율
인증키 : DvYPnli3e0IjaMMxPsqthtkwFfkWt9IW
요청 URL : https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON

요청변수(Requester Parameter) : 
1.authkey[String] - 인증키
2.searchdate[String] - 검색요청날짜
3.data[String] - 검색요청타입 AP01 : 환율, AP02 : 대출금리, AP03 : 국제금리
예시 : https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=DvYPnli3e0IjaMMxPsqthtkwFfkWt9IW&searchdate=20251111&data=AP01

출력결과(Response Element) :
"변수명":[타입]변수설명
"result": [Integer] 조회 결과 1 : 성공, 2 : DATA코드 오류, 3 : 인증코드 오류, 4 : 일일제한횟수 마감,
"cur_unit": [String]통화코드,
"ttb": [String]전신환(송금)받으실때,
"tts": [String]전신환(송금)보내실때,
"deal_bas_r": [String]매매 기준율,
"bkpr": [String]장부가격,
"yy_efee_r": [String]년환가료율,
"ten_dd_efee_r": [String]10일환가료율,
"kftc_bkpr": [String]서울외국환중개장부가격,
"kftc_deal_bas_r": [String]서울외국환중개매매기준율,
"cur_nm": [String]국가/통화명