공공데이터포털 : 금융위원회\_주식시세정보
주식시세
인증키 : 8676c1c0ad9eda160d90d5927a4dc21aace7f18de90365e4c993f72a66ce66b7
요청 URL : https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo

요청변수(Requester Parameter) :
1.serviceKey[String] - 인증키
2.numOfRows[String] - 한 페이지 결과 수
3.pageNo[String] - 페이지번호
4.resultType[String] - 결과형식
5.basDt[String] - 기준일자(YYYYMMDD)
6.beginBasDt[String] - 기준일자가 검색값보다 크거나 같은 데이터를 검색(YYYYMMDD)
7.endBasDt[String] - 기준일자가 검색값보다 작은 데이터를 검색(YYYYMMDD)
8.likeBasDt[String] - 기준일자값이 검색값을 포함하는 데이터를 검색(YYYYMMDD)
9.likeSrtnCd[String] - 단축코드가 검색값을 포함하는 데이터를 검색
10.isinCd[String] - 검색값과 ISIN코드이 일치하는 데이터를 검색
11.likeIsinCd[String] - ISIN코드가 검색값을 포함하는 데이터를 검색
12.itmsNm[String] - 검색값과 종목명이 일치하는 데이터를 검색
13.likeItmsNm[String] - 종목명이 검색값을 포함하는 데이터를 검색
14.mrktCls[String] - 검색값과 시장구분이 일치하는 데이터를 검색
15.beginVs[String] - 대비가 검색값보다 크거나 같은 데이터를 검색
16.endVs[String] - 대비가 검색값보다 작은 데이터를 검색
17.beginFltRt[String] - 등락률이 검색값보다 크거나 같은 데이터를 검색
18.endFltRt[String] - 등락률이 검색값보다 작은 데이터를 검색
19.beginTrqu[String] - 거래량이 검색값보다 크거나 같은 데이터를 검색
20.endTrqu[String] - 거래량이 검색값보다 작은 데이터를 검색
21.beginTrPrc[String] - 거래대금이 검색값보다 크거나 같은 데이터를 검색
22.endTrPrc[String] - 거래대금이 검색값보다 작은 데이터를 검색
23.beginLstgStCnt[String] - 상장주식수가 검색값보다 크거나 같은 데이터를 검색
24.endLstgStCnt[String] - 상장주식수가 검색값보다 작은 데이터를 검색
25.beginMrktTotAmt[String] - 시가총액이 검색값보다 크거나 같은 데이터를 검색
26.endMrktTotAmt[String] - 시가총액이 검색값보다 작은 데이터를 검색
예시 : https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo?serviceKey=8676c1c0ad9eda160d90d5927a4dc21aace7f18de90365e4c993f72a66ce66b7&numOfRows=400&pageNo=1&resultType=json&likeSrtnCd=005930&beginBasDt=20251011&endBasDt=20251112

출력결과(Response Element) :
"resultCode": 결과 코드 - API 호출 결과의 상태
"resultMsg": 결과메세지 - API 호출 결과의 상태
"numOfRows": 한페이지 결과 수
"pageNo": 페이지 번호
"totalCount": 전체 결과 수
"basDt": 기준일자
"srtnCd": 단축코드 - 종목 코드보다 짧으면서 유일성이 보장되는 코드(6자리)
"isinCd": ISIN코드 - 국제 채권 식별 번호. 유가증권(채권)의 국제인증 고유번호
"itmsNm": 종목명
"mrktCtg": 시장구분 - 주식의 시장 구분 (KOSPI/KOSDAQ/KONEX 중 1)
"clpr": 종가 - 정규시장의 매매시간 종료시까지 형성되는 최종가격
"vs": 대비 - 전일 대비 등락
"fltRt": 등락률 - 전일 대비 등락에 따른 비율
"mkp": 시가 - 정규시장의 매매시간 개시 후 형성되는 최초가격
"hipr": 고가 - 하루 중 가격의 최고치
"lopr": 저가 - 하루 중 가격의 최저치
"trqu": 거래량 - 체결수량의 누적 합계
"trPrc": 거래대금 - 거래건 별 체결가격 _ 체결수량의 누적 합계
"lstgStCnt": 상장주식수 - 종목의 상장주식수
"mrktTotAmt": 시가총액 - 종가 _ 상장주식수



예시
{
"response": {
"header": {
"resultCode": "00",
"resultMsg": "NORMAL SERVICE."
},
"body": {
"numOfRows": 400,
"pageNo": 1,
"totalCount": 22,
"items": {
"item": [
{
"basDt": "20251111",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "103500",
"vs": "2900",
"fltRt": "2.88",
"mkp": "103700",
"hipr": "106000",
"lopr": "102000",
"trqu": "27742542",
"trPrc": "2891284191696",
"lstgStCnt": "5919637922",
"mrktTotAmt": "612682524927000"
},
{
"basDt": "20251110",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "100600",
"vs": "2700",
"fltRt": "2.76",
"mkp": "98600",
"hipr": "101000",
"lopr": "97900",
"trqu": "23842327",
"trPrc": "2378589265170",
"lstgStCnt": "5919637922",
"mrktTotAmt": "595515574953200"
},
{
"basDt": "20251107",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "97900",
"vs": "-1300",
"fltRt": "-1.31",
"mkp": "96400",
"hipr": "100300",
"lopr": "96300",
"trqu": "22908083",
"trPrc": "2246165668512",
"lstgStCnt": "5919637922",
"mrktTotAmt": "579532552563800"
},
{
"basDt": "20251106",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "99200",
"vs": "-1400",
"fltRt": "-1.39",
"mkp": "103700",
"hipr": "103800",
"lopr": "98800",
"trqu": "28655689",
"trPrc": "2879395591412",
"lstgStCnt": "5919637922",
"mrktTotAmt": "587228081862400"
},
{
"basDt": "20251105",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "100600",
"vs": "-4300",
"fltRt": "-4.1",
"mkp": "101000",
"hipr": "102000",
"lopr": "96700",
"trqu": "44843020",
"trPrc": "4472837142000",
"lstgStCnt": "5919637922",
"mrktTotAmt": "595515574953200"
},
{
"basDt": "20251104",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "104900",
"vs": "-6200",
"fltRt": "-5.58",
"mkp": "111800",
"hipr": "112400",
"lopr": "104900",
"trqu": "30450281",
"trPrc": "3288827318350",
"lstgStCnt": "5919637922",
"mrktTotAmt": "620970018017800"
},
{
"basDt": "20251103",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "111100",
"vs": "3600",
"fltRt": "3.35",
"mkp": "106900",
"hipr": "111500",
"lopr": "106500",
"trqu": "30110066",
"trPrc": "3295567406580",
"lstgStCnt": "5919637922",
"mrktTotAmt": "657671773134200"
},
{
"basDt": "20251031",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "107500",
"vs": "3400",
"fltRt": "3.27",
"mkp": "105000",
"hipr": "108600",
"lopr": "103300",
"trqu": "54824564",
"trPrc": "5744254696496",
"lstgStCnt": "5919637922",
"mrktTotAmt": "636361076615000"
},
{
"basDt": "20251030",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "104100",
"vs": "3600",
"fltRt": "3.58",
"mkp": "102400",
"hipr": "105800",
"lopr": "102000",
"trqu": "34570017",
"trPrc": "3591285130855",
"lstgStCnt": "5919637922",
"mrktTotAmt": "616234307680200"
},
{
"basDt": "20251029",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "100500",
"vs": "1000",
"fltRt": "1.01",
"mkp": "100200",
"hipr": "101000",
"lopr": "99100",
"trqu": "20899788",
"trPrc": "2092397701550",
"lstgStCnt": "5919637922",
"mrktTotAmt": "594923611161000"
},
{
"basDt": "20251028",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "99500",
"vs": "-2500",
"fltRt": "-2.45",
"mkp": "100900",
"hipr": "101000",
"lopr": "99100",
"trqu": "20002282",
"trPrc": "1994943413477",
"lstgStCnt": "5919637922",
"mrktTotAmt": "589003973239000"
},
{
"basDt": "20251027",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "102000",
"vs": "3200",
"fltRt": "3.24",
"mkp": "101300",
"hipr": "102000",
"lopr": "100600",
"trqu": "22169970",
"trPrc": "2250615426550",
"lstgStCnt": "5919637922",
"mrktTotAmt": "603803068044000"
},
{
"basDt": "20251024",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "98800",
"vs": "2300",
"fltRt": "2.38",
"mkp": "97900",
"hipr": "99000",
"lopr": "97700",
"trqu": "18801925",
"trPrc": "1852510715919",
"lstgStCnt": "5919637922",
"mrktTotAmt": "584860226693600"
},
{
"basDt": "20251023",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "96500",
"vs": "-2100",
"fltRt": "-2.13",
"mkp": "96800",
"hipr": "98500",
"lopr": "96300",
"trqu": "18488581",
"trPrc": "1796781660790",
"lstgStCnt": "5919637922",
"mrktTotAmt": "571245059473000"
},
{
"basDt": "20251022",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "98600",
"vs": "1100",
"fltRt": "1.13",
"mkp": "97100",
"hipr": "98600",
"lopr": "95500",
"trqu": "15937611",
"trPrc": "1547962213088",
"lstgStCnt": "5919637922",
"mrktTotAmt": "583676299109200"
},
{
"basDt": "20251021",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "97500",
"vs": "-600",
"fltRt": "-.61",
"mkp": "98500",
"hipr": "99900",
"lopr": "97300",
"trqu": "22803830",
"trPrc": "2252645721374",
"lstgStCnt": "5919637922",
"mrktTotAmt": "577164697395000"
},
{
"basDt": "20251020",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "98100",
"vs": "200",
"fltRt": ".2",
"mkp": "97900",
"hipr": "98300",
"lopr": "96000",
"trqu": "17589756",
"trPrc": "1714999925050",
"lstgStCnt": "5919637922",
"mrktTotAmt": "580716480148200"
},
{
"basDt": "20251017",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "97900",
"vs": "200",
"fltRt": ".2",
"mkp": "97200",
"hipr": "99100",
"lopr": "96700",
"trqu": "22730809",
"trPrc": "2225132112442",
"lstgStCnt": "5919637922",
"mrktTotAmt": "579532552563800"
},
{
"basDt": "20251016",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "97700",
"vs": "2700",
"fltRt": "2.84",
"mkp": "95300",
"hipr": "97700",
"lopr": "95000",
"trqu": "28141060",
"trPrc": "2728362196400",
"lstgStCnt": "5919637922",
"mrktTotAmt": "578348624979400"
},
{
"basDt": "20251015",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "95000",
"vs": "3400",
"fltRt": "3.71",
"mkp": "92300",
"hipr": "95300",
"lopr": "92100",
"trqu": "21050111",
"trPrc": "1978832965007",
"lstgStCnt": "5919637922",
"mrktTotAmt": "562365602590000"
},
{
"basDt": "20251014",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "91600",
"vs": "-1700",
"fltRt": "-1.82",
"mkp": "95300",
"hipr": "96000",
"lopr": "90200",
"trqu": "35545235",
"trPrc": "3312011003250",
"lstgStCnt": "5919637922",
"mrktTotAmt": "542238833655200"
},
{
"basDt": "20251013",
"srtnCd": "005930",
"isinCd": "KR7005930003",
"itmsNm": "삼성전자",
"mrktCtg": "KOSPI",
"clpr": "93300",
"vs": "-1100",
"fltRt": "-1.17",
"mkp": "91300",
"hipr": "93400",
"lopr": "90700",
"trqu": "23883308",
"trPrc": "2197370665497",
"lstgStCnt": "5919637922",
"mrktTotAmt": "552302218122600"
}
]
}
}
}
}

참고사항
우리가 그릴 주식 차트는 30일 60일 90일 3가지만 그릴꺼다., 일반 게시판은 30일치만 보여주자자
1. 차트 그리기 위해서 모든 요청변수를 쓸 필요는 없다. 차트 작성시 필요 요청변수는
serviceKey[String] - 인증키
numOfRows[String] - 한 페이지 결과 수
pageNo[String] - 페이지번호
resultType[String] - 결과형식 - JSON으로 받자
likeSrtnCd[String] - 종목코드
beginBasDt[String] - 기준일자가 검색값보다 크거나 같은 데이터를 검색(YYYYMMDD)
endBasDt[String] - 기준일자가 검색값보다 작은 데이터를 검색(YYYYMMDD)
위 정도만 있으면 될 것 같다.
위 의견은 너무많은 요청변수는 쓸 필요 없고 꼭 필요한 요청변수만 쓰란 의미다.
2. 출력결과도 아래 정보만 있으면 차트 그릴때 문제가 없을 것 같다.
"itmsNm": 종목명
"srtnCd": 단축코드
"basDt": 기준일자
"mkp": 시가
"clpr": 종가
"hipr": 고가
"lopr": 저가
"trqu": 거래량