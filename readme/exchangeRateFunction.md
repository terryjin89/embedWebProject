# 환율 정보 기능 구현 가이드

> **SCRUM-7**: 경제지표 게시판 구현 (환율 정보)
> **작업 기간**: 2025-11-26
> **담당자**: terryjin89

---

## 📋 목차

1. [프로젝트 개요](#프로젝트-개요)
2. [기술 스택](#기술-스택)
3. [아키텍처 설계](#아키텍처-설계)
4. [구현 프로세스](#구현-프로세스)
5. [주요 구현 내용](#주요-구현-내용)
6. [테스트 전략](#테스트-전략)
7. [트러블슈팅](#트러블슈팅)
8. [코드 참조 가이드](#코드-참조-가이드)

---

## 프로젝트 개요

### 비즈니스 요구사항
사용자가 주요 통화의 환율 정보를 실시간으로 조회하고, 과거 환율 추이를 차트로 확인할 수 있는 기능을 구현합니다.

### 주요 기능
- ✅ **환율 목록 조회**: 주요 10개국 통화의 환율 정보 (USD, JPY, EUR, CNH, GBP, CHF, CAD, AUD, HKD, SGD)
- ✅ **전일대비 표시**: 전일 대비 변동액, 변동률, 변동 방향 (UP/DOWN/SAME)
- ✅ **환율 차트**: 선택한 통화의 과거 환율 추이 (7일/30일/90일/1년)
- ✅ **반응형 UI**: sticky 헤더, 호버 효과, 홀짝 행 색상

### 인수 조건 (Acceptance Criteria)
- [x] 경제지표 페이지에서 주요 10개국 환율 정보 표시
- [x] 한국수출입은행 API를 통한 실시간 데이터 조회
- [x] 전일대비 변동 계산 및 시각적 표시 (▲/▼/-)
- [x] 테이블 UI (sticky header, 홀짝 색상, 호버 효과)
- [x] 행 클릭 시 환율 차트 페이지 이동
- [x] 기간별 차트 표시 (7일/30일/90일/1년)

---

## 기술 스택

### 프론트엔드
| 기술 | 버전 | 용도 |
|------|------|------|
| **React** | 19.2.0 | UI 컴포넌트 라이브러리 |
| **Vite** | 7.2.2 | 빌드 도구 및 개발 서버 |
| **Axios** | 1.13.2 | HTTP 클라이언트 (백엔드 API 호출) |
| **Recharts** | 3.4.1 | 환율 차트 시각화 |
| **Playwright** | latest | E2E 테스트 프레임워크 |

### 백엔드
| 기술 | 버전 | 용도 |
|------|------|------|
| **Spring Boot** | 3.2.0 | 백엔드 프레임워크 |
| **JDK** | 17 | Java 런타임 |
| **RestTemplate** | - | 외부 API 호출 |
| **Jackson** | - | JSON 직렬화/역직렬화 |
| **Lombok** | - | 보일러플레이트 코드 제거 |
| **Swagger** | 3.x | API 문서화 |

### 인프라
| 기술 | 용도 |
|------|------|
| **Docker** | 컨테이너화 |
| **Docker Compose** | 멀티 컨테이너 오케스트레이션 |
| **MySQL** | 데이터베이스 (향후 환율 데이터 캐싱용) |

### 외부 API
| API | 용도 | 인증 |
|-----|------|------|
| **한국수출입은행 환율 API** | 환율 정보 조회 | API Key |

---

## 아키텍처 설계

### 시스템 아키텍처

```
┌─────────────────────────────────────────────────────────────┐
│                         사용자                               │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                  프론트엔드 (React + Vite)                    │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  App.jsx                                             │  │
│  │    ├─ ExchangeRateTable.jsx (환율 테이블)            │  │
│  │    └─ RateDetailChart.jsx (환율 차트)                │  │
│  └──────────────────────────────────────────────────────┘  │
│                              │                               │
│                              ▼                               │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  economyService.js (API 호출 로직)                    │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                    HTTP (Vite Proxy)
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                백엔드 (Spring Boot)                          │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  ExchangeRateController.java                         │  │
│  │    ├─ GET /api/exchange-rates                        │  │
│  │    ├─ GET /api/exchange-rates/{curUnit}              │  │
│  │    └─ GET /api/exchange-rates/{curUnit}/historical   │  │
│  └──────────────────────────────────────────────────────┘  │
│                              │                               │
│                              ▼                               │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  ExchangeRateService.java (비즈니스 로직)             │  │
│  │    ├─ 환율 데이터 조회                                │  │
│  │    ├─ 전일대비 계산                                   │  │
│  │    └─ 주요 10개국 통화 필터링                         │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                    HTTP (RestTemplate)
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│            한국수출입은행 환율 API (외부 API)                 │
│  https://oapi.koreaexim.go.kr/site/program/financial/...   │
└─────────────────────────────────────────────────────────────┘
```

### 데이터 플로우

#### 1. 환율 목록 조회
```
사용자
  └─> ExchangeRateTable 컴포넌트 로드
      └─> economyService.getExchangeRates(searchDate)
          └─> Vite Proxy (/api/exchange-rates)
              └─> ExchangeRateController.getExchangeRates()
                  └─> ExchangeRateService.getExchangeRates()
                      ├─> 한국수출입은행 API (현재 날짜)
                      ├─> 한국수출입은행 API (전일 날짜)
                      ├─> 전일대비 계산
                      └─> 주요 10개국 통화 필터링
                          └─> ExchangeRateDTO[] 반환
                              └─> 프론트엔드 렌더링
```

#### 2. 환율 차트 조회
```
사용자
  └─> 테이블 행 클릭 (예: USD)
      └─> RateDetailChart 컴포넌트 로드
          └─> economyService.getHistoricalRates(currencyCode, days)
              └─> Vite Proxy (/api/exchange-rates/USD/historical?days=30)
                  └─> ExchangeRateController.getHistoricalRates()
                      └─> ExchangeRateService.getHistoricalRates()
                          └─> 과거 N일간 한국수출입은행 API 호출
                              └─> HistoricalRateDTO[] 반환
                                  └─> Recharts LineChart 렌더링
```

---

## 구현 프로세스

### Phase 1: 백엔드 API 개발 (SCRUM-20, SCRUM-21)

#### 1.1 DTO 설계
**파일 위치**: `backend/src/main/java/com/project/companyanalyzer/dto/`

**ExchangeRateResponse.java** (한국수출입은행 API 응답 매핑)
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {
    @JsonProperty("cur_unit")    // 통화코드 (USD, JPY 등)
    private String curUnit;

    @JsonProperty("cur_nm")       // 통화명 (미국 달러, 일본 옌 등)
    private String curNm;

    @JsonProperty("deal_bas_r")   // 매매 기준율
    private String dealBasR;

    @JsonProperty("ttb")           // 전신환 받을때
    private String ttb;

    @JsonProperty("tts")           // 전신환 보낼때
    private String tts;
}
```

**ExchangeRateDTO.java** (프론트엔드 응답 DTO)
```java
@Data
@Builder
public class ExchangeRateDTO {
    private String curUnit;           // 통화코드
    private String curNm;             // 통화명
    private BigDecimal dealBasR;      // 매매 기준율
    private BigDecimal ttb;           // 전신환 받을때
    private BigDecimal tts;           // 전신환 보낼때
    private BigDecimal changeAmount;  // 전일대비 변동액
    private BigDecimal changeRate;    // 전일대비 변동률 (%)
    private String changeDirection;   // 변동 방향 (UP/DOWN/SAME)
    private String searchDate;        // 조회 날짜
}
```

**HistoricalRateDTO.java** (차트 데이터 DTO)
```java
@Data
@Builder
public class HistoricalRateDTO {
    private String date;         // 날짜 (YYYY-MM-DD)
    private BigDecimal rate;     // 환율
}
```

#### 1.2 Service 계층 구현
**파일 위치**: `backend/src/main/java/com/project/companyanalyzer/service/ExchangeRateService.java`

**핵심 로직**:
1. **환율 데이터 조회**
   - 한국수출입은행 API 호출
   - Mock 모드 지원 (개발 환경)

2. **전일대비 계산**
   ```java
   // 변동액 = 현재 환율 - 전일 환율
   changeAmount = currentDealBasR.subtract(previousDealBasR);

   // 변동률 = (변동액 / 전일 환율) * 100
   changeRate = changeAmount
       .divide(previousDealBasR, 4, RoundingMode.HALF_UP)
       .multiply(BigDecimal.valueOf(100))
       .setScale(2, RoundingMode.HALF_UP);

   // 변동 방향
   if (changeAmount > 0) → "UP"
   if (changeAmount < 0) → "DOWN"
   if (changeAmount = 0) → "SAME"
   ```

3. **주요 10개국 통화 필터링**
   ```java
   private static final Set<String> MAJOR_CURRENCIES = Set.of(
       "USD", "JPY(100)", "EUR", "CNH", "GBP",
       "CHF", "CAD", "AUD", "HKD", "SGD"
   );
   ```

4. **과거 환율 데이터 조회**
   - N일간 반복 API 호출
   - 데이터 누락 시 건너뛰기
   - 날짜 오름차순 정렬

#### 1.3 Controller 계층 구현
**파일 위치**: `backend/src/main/java/com/project/companyanalyzer/controller/ExchangeRateController.java`

**API 엔드포인트**:

| Method | Endpoint | 설명 | 파라미터 |
|--------|----------|------|----------|
| GET | `/api/exchange-rates` | 환율 목록 조회 | `searchDate` (YYYYMMDD, 선택) |
| GET | `/api/exchange-rates/{curUnit}` | 특정 통화 조회 | `curUnit` (통화코드) |
| GET | `/api/exchange-rates/{curUnit}/historical` | 과거 환율 조회 | `days` (기간, 기본값: 30) |

---

### Phase 2: 프론트엔드 개발 (SCRUM-18, SCRUM-19)

#### 2.1 API 서비스 구현
**파일 위치**: `frontend/src/services/economyService.js`

**주요 기능**:
```javascript
const economyService = {
  // 환율 목록 조회
  getExchangeRates: async (searchDate) => {
    const response = await economyAPI.get('', { params: { searchDate } });
    return response.data;
  },

  // 과거 환율 데이터 조회 (차트용)
  getHistoricalRates: async (currencyCode, days = 30) => {
    const response = await economyAPI.get(`/${currencyCode}/historical`, {
      params: { days }
    });
    return response.data.map(item => ({
      ...item,
      rate: Number(item.rate)  // BigDecimal → Number 변환
    }));
  }
};
```

#### 2.2 환율 테이블 컴포넌트
**파일 위치**: `frontend/src/components/ExchangeRateTable.jsx`

**주요 기능**:
- 환율 데이터 로드 및 표시
- 새로고침 버튼
- 행 클릭 이벤트 (차트 페이지 이동)
- 로딩/에러 상태 처리

**핵심 로직**:
```javascript
// 어제 날짜 계산 (한국수출입은행 API는 당일 데이터 미제공)
const yesterday = new Date();
yesterday.setDate(yesterday.getDate() - 1);
const searchDate = yesterday.toISOString().slice(0, 10).replace(/-/g, '');

// 환율 데이터 로드
const data = await economyService.getExchangeRates(searchDate);
setExchangeRates(data);
```

**UI 특징**:
- Sticky 헤더 (`position: sticky`)
- 홀짝 행 색상 구분
- 호버 효과
- 반응형 디자인

#### 2.3 환율 차트 컴포넌트
**파일 위치**: `frontend/src/components/RateDetailChart.jsx`

**주요 기능**:
- Recharts LineChart를 사용한 환율 추이 시각화
- 기간 선택 (7일/30일/90일/1년)
- 통계 정보 카드 (최고가/최저가/평균가)
- 커스텀 툴팁

**핵심 로직**:
```javascript
// 과거 환율 데이터 로드
const data = await economyService.getHistoricalRates(currencyCode, period);
setChartData(data);

// 통계 계산
const rates = data.map((item) => parseFloat(item.rate));
setStatistics({
  highest: Math.max(...rates).toFixed(2),
  lowest: Math.min(...rates).toFixed(2),
  average: (rates.reduce((a, b) => a + b, 0) / rates.length).toFixed(2),
});
```

**차트 설정**:
```jsx
<LineChart data={chartData} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
  <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
  <XAxis dataKey="date" angle={-45} textAnchor="end" />
  <YAxis
    tickFormatter={(value) => value.toLocaleString('ko-KR')}
    domain={[(dataMin) => Math.floor(dataMin * 0.99),
             (dataMax) => Math.ceil(dataMax * 1.01)]}
  />
  <Tooltip content={<CustomTooltip />} />
  <Line
    type="monotone"
    dataKey="rate"
    stroke="#1890ff"
    strokeWidth={2}
  />
</LineChart>
```

---

### Phase 3: Docker 환경 설정

#### 3.1 Vite 프록시 설정
**파일 위치**: `frontend/vite.config.js`

**문제**: Docker 컨테이너 내부에서는 `localhost`로 백엔드 접근 불가

**해결 방법**: 환경변수를 통한 동적 프록시 설정
```javascript
export default defineConfig({
  plugins: [react()],
  server: {
    host: true,  // Docker 외부 접근 허용
    watch: {
      usePolling: true,  // Docker HMR 활성화
    },
    proxy: {
      '/api': {
        // 환경변수로 프록시 타겟 설정
        target: process.env.VITE_PROXY_TARGET || 'http://backend:8080',
        changeOrigin: true,
        secure: false,
      },
    },
  },
})
```

#### 3.2 Docker Compose 설정
**파일 위치**: `docker-compose.yml`

```yaml
services:
  frontend:
    environment:
      # Docker 환경에서 백엔드 서비스명 사용
      VITE_PROXY_TARGET: http://backend:8080
    ports:
      - "5173:5173"
    depends_on:
      - backend
    networks:
      - app-network
```

---

### Phase 4: E2E 테스트 구현

#### 4.1 Playwright 테스트 작성
**파일 위치**: `tests/exchange-rate.spec.js`

**테스트 케이스**:

1. **환율 정보 목록이 표시되어야 함**
   - 환율 테이블 렌더링 확인
   - 테이블 헤더 검증
   ```javascript
   await expect(page.locator('.exchange-rate-container')).toBeVisible();
   await expect(page.locator('.table-title')).toHaveText('환율 정보');
   ```

2. **백엔드 API에서 환율 데이터를 가져와야 함**
   - API 응답 감지
   - 데이터 필드 검증
   ```javascript
   const responsePromise = page.waitForResponse(
     response => response.url().includes('/api/exchange-rates')
   );
   const data = await response.json();
   expect(data).toBeInstanceOf(Array);
   expect(firstItem).toHaveProperty('curUnit');
   ```

3. **전일대비 변동이 올바르게 표시되어야 함**
   - 전일대비 컬럼 확인
   - 변동 아이콘 검증 (▲/▼/-)

4. **새로고침 버튼이 작동해야 함**
   - 버튼 클릭 이벤트
   - API 재호출 확인

5. **로딩 상태가 표시되어야 함**
   - 로딩 메시지 확인
   - 로딩 완료 후 테이블 표시

6. **에러 상태가 올바르게 처리되어야 함**
   - API 실패 시뮬레이션
   - 에러 메시지 및 "다시 시도" 버튼 확인

**테스트 실행 결과**:
```
✓ 환율 정보 목록이 표시되어야 함 (1.2s)
✓ 백엔드 API에서 환율 데이터를 가져와야 함 (564ms)
✓ 전일대비 변동이 올바르게 표시되어야 함 (1.1s)
✓ 새로고침 버튼이 작동해야 함 (1.3s)
✓ 로딩 상태가 표시되어야 함 (770ms)
✓ 에러 상태가 올바르게 처리되어야 함 (1.2s)

6 passed (7.5s)
```

---

## 주요 구현 내용

### 1. 전일대비 계산 로직

**백엔드 구현** (`ExchangeRateService.java:175-211`):
```java
private ExchangeRateDTO convertToDTO(
    ExchangeRateResponse currentRate,
    ExchangeRateResponse previousRate,
    String searchDate
) {
    // 현재 매매 기준율 파싱
    BigDecimal currentDealBasR = parseBigDecimal(currentRate.getDealBasR());

    // 전일대비 계산
    BigDecimal changeAmount = BigDecimal.ZERO;
    BigDecimal changeRate = BigDecimal.ZERO;
    String changeDirection = "SAME";

    if (previousRate != null) {
        BigDecimal previousDealBasR = parseBigDecimal(previousRate.getDealBasR());

        // 변동액 = 현재 - 전일
        changeAmount = currentDealBasR.subtract(previousDealBasR);

        // 변동률 = (변동액 / 전일) * 100
        if (previousDealBasR.compareTo(BigDecimal.ZERO) > 0) {
            changeRate = changeAmount
                .divide(previousDealBasR, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
        }

        // 방향 결정
        int comparison = changeAmount.compareTo(BigDecimal.ZERO);
        if (comparison > 0) changeDirection = "UP";
        else if (comparison < 0) changeDirection = "DOWN";
    }

    return ExchangeRateDTO.builder()
        .curUnit(currentRate.getCurUnit())
        .curNm(currentRate.getCurNm())
        .dealBasR(currentDealBasR)
        .changeAmount(changeAmount)
        .changeRate(changeRate)
        .changeDirection(changeDirection)
        .build();
}
```

### 2. 환율 데이터 API 호출

**실제 API 모드** (`ExchangeRateService.java:132-145`):
```java
private List<ExchangeRateResponse> fetchFromApi(String searchDate) {
    log.debug("실제 API 호출 - 날짜: {}", searchDate);

    String url = String.format("%s?authkey=%s&searchdate=%s&data=AP01",
        apiUrl, authKey, searchDate);

    ExchangeRateResponse[] response = restTemplate.getForObject(
        url, ExchangeRateResponse[].class
    );

    if (response == null || response.length == 0) {
        throw new RuntimeException("API 응답이 비어있습니다.");
    }

    return Arrays.asList(response);
}
```

**Mock 모드** (`ExchangeRateService.java:150-170`):
```java
private List<ExchangeRateResponse> fetchMockData(String searchDate) throws IOException {
    log.debug("목업 데이터 조회 - 날짜: {}", searchDate);

    String mockFilePath = String.format("exchange-rate-mock-%s.json", searchDate);

    try {
        ClassPathResource resource = new ClassPathResource(mockFilePath);
        return objectMapper.readValue(
            resource.getInputStream(),
            new TypeReference<List<ExchangeRateResponse>>() {}
        );
    } catch (IOException e) {
        // 기본 목업 파일 사용
        ClassPathResource defaultResource =
            new ClassPathResource("exchange-rate-mock.json");
        return objectMapper.readValue(
            defaultResource.getInputStream(),
            new TypeReference<List<ExchangeRateResponse>>() {}
        );
    }
}
```

### 3. 과거 환율 데이터 조회 (차트용)

**백엔드 구현** (`ExchangeRateService.java:255-291`):
```java
public List<HistoricalRateDTO> getHistoricalRates(String currencyCode, int days) {
    log.info("과거 환율 조회 시작 - 통화: {}, 기간: {}일", currencyCode, days);

    List<HistoricalRateDTO> historicalRates = new ArrayList<>();
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // N일간 반복 조회
    for (int i = 0; i < days; i++) {
        LocalDate targetDate = today.minusDays(i);
        String searchDate = targetDate.format(formatter);

        try {
            List<ExchangeRateResponse> dailyRates = fetchExchangeRates(searchDate);

            // 특정 통화 필터링
            dailyRates.stream()
                .filter(rate -> currencyCode.equals(rate.getCurUnit()))
                .findFirst()
                .ifPresent(rate -> {
                    BigDecimal dealBasR = parseBigDecimal(rate.getDealBasR());
                    if (dealBasR.compareTo(BigDecimal.ZERO) > 0) {
                        historicalRates.add(HistoricalRateDTO.builder()
                            .date(targetDate.format(outputFormatter))
                            .rate(dealBasR)
                            .build());
                    }
                });
        } catch (Exception e) {
            log.warn("과거 환율 데이터 조회 실패 - 날짜: {}", searchDate);
            // 데이터 누락 시 건너뛰기
        }
    }

    // 날짜 오름차순 정렬
    historicalRates.sort(Comparator.comparing(HistoricalRateDTO::getDate));

    return historicalRates;
}
```

### 4. 프론트엔드 상태 관리

**환율 테이블** (`ExchangeRateTable.jsx:14-34`):
```javascript
const loadExchangeRates = async () => {
  setLoading(true);
  setError(null);

  try {
    // 어제 날짜 계산 (한국수출입은행 API는 당일 데이터 미제공)
    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    const searchDate = yesterday.toISOString().slice(0, 10).replace(/-/g, '');

    const data = await economyService.getExchangeRates(searchDate);
    setExchangeRates(data);
    setLastUpdated(new Date());
  } catch (err) {
    console.error('Failed to load exchange rates:', err);
    setError('환율 데이터를 불러오는데 실패했습니다.');
  } finally {
    setLoading(false);
  }
};
```

**환율 차트** (`RateDetailChart.jsx:35-58`):
```javascript
const loadChartData = async (period) => {
  setLoading(true);
  setError(null);

  try {
    const data = await economyService.getHistoricalRates(currencyCode, period);
    setChartData(data);

    // 통계 계산
    if (data.length > 0) {
      const rates = data.map((item) => parseFloat(item.rate));
      setStatistics({
        highest: Math.max(...rates).toFixed(2),
        lowest: Math.min(...rates).toFixed(2),
        average: (rates.reduce((a, b) => a + b, 0) / rates.length).toFixed(2),
      });
    }
  } catch (err) {
    console.error('Failed to load chart data:', err);
    setError('차트 데이터를 불러오는데 실패했습니다.');
  } finally {
    setLoading(false);
  }
};
```

---

## 테스트 전략

### 1. 백엔드 API 테스트

**수동 테스트 (cURL)**:
```bash
# 환율 목록 조회
curl -X GET "http://localhost:8080/api/exchange-rates?searchDate=20251125"

# 특정 통화 조회
curl -X GET "http://localhost:8080/api/exchange-rates/USD"

# 과거 환율 조회
curl -X GET "http://localhost:8080/api/exchange-rates/USD/historical?days=7"
```

**예상 응답**:
```json
[
  {
    "curUnit": "USD",
    "curNm": "미국 달러",
    "dealBasR": 1473.50,
    "ttb": 1458.76,
    "tts": 1488.23,
    "changeAmount": 1.50,
    "changeRate": 0.10,
    "changeDirection": "UP",
    "searchDate": "20251125"
  }
]
```

### 2. 프론트엔드-백엔드 통합 테스트

**Docker 환경**:
```bash
# 컨테이너 실행
docker-compose up -d

# 로그 확인
docker logs company-analyzer-backend
docker logs company-analyzer-frontend

# 프론트엔드 접속
http://localhost:5173
```

**테스트 시나리오**:
1. 환율 테이블 로드 확인
2. 10개 통화 데이터 표시 확인
3. USD 행 클릭 → 차트 페이지 이동
4. 차트 데이터 로드 확인
5. 기간 선택 버튼 (7일/30일/90일/1년) 동작 확인

### 3. E2E 테스트 (Playwright)

**테스트 실행**:
```bash
cd tests
npx playwright test exchange-rate.spec.js --reporter=list
```

**테스트 커버리지**:
- ✅ UI 렌더링 (테이블, 차트)
- ✅ API 호출 및 응답 검증
- ✅ 사용자 인터랙션 (클릭, 새로고침)
- ✅ 로딩/에러 상태 처리
- ✅ 데이터 변환 및 표시

---

## 트러블슈팅

### 문제 1: Docker 환경에서 프론트엔드-백엔드 통신 실패

**증상**:
- 프론트엔드에서 `/api/exchange-rates` 호출 시 500 에러
- 브라우저 콘솔: `Failed to load resource: the server responded with a status of 500`

**원인**:
Vite 프록시 설정이 `localhost:8080`으로 고정되어 있어 Docker 컨테이너 내부에서 백엔드 접근 불가

**해결**:
`vite.config.js`에서 환경변수를 통한 동적 프록시 설정
```javascript
proxy: {
  '/api': {
    target: process.env.VITE_PROXY_TARGET || 'http://backend:8080',
    changeOrigin: true,
    secure: false,
  },
}
```

`docker-compose.yml`에 환경변수 추가
```yaml
frontend:
  environment:
    VITE_PROXY_TARGET: http://backend:8080
```

### 문제 2: 환율 차트 렌더링 실패

**증상**:
- 테이블 행 클릭 시 "차트를 불러오는 중..." 상태에서 멈춤
- 콘솔 에러: `Cannot read property 'curUnit' of undefined`

**원인**:
`App.jsx`에서 백엔드 API 응답 필드명(camelCase)과 프론트엔드 코드(snake_case) 불일치
```javascript
// 잘못된 코드
<RateDetailChart
  currencyCode={selectedCurrency.cur_unit}  // ❌ undefined
  currencyName={selectedCurrency.cur_nm}     // ❌ undefined
/>
```

**해결**:
필드명을 camelCase로 통일
```javascript
// 수정된 코드
<RateDetailChart
  currencyCode={selectedCurrency.curUnit}  // ✅
  currencyName={selectedCurrency.curNm}    // ✅
/>
```

### 문제 3: Playwright 테스트 실패

**증상**:
- 테스트 1 실패: `strict mode violation: locator resolved to 2 elements`
- 테스트 2 실패: `Expected pattern: /[▲▼-]/ Received: "952.91"`

**원인**:
1. 테스트 선택자가 너무 광범위 (`th:has-text("통화")` → 2개 요소 매칭)
2. 전일대비 컬럼 인덱스 오류 (2번째 대신 5번째)

**해결**:
```javascript
// 문제 1 해결: 더 구체적인 선택자 사용
await expect(page.locator('th:has-text("통화코드")')).toBeVisible();
await expect(page.locator('th:has-text("매매기준율")')).toBeVisible();

// 문제 2 해결: 올바른 컬럼 인덱스 사용
const changeCell = page.locator('tbody tr').first().locator('td').nth(5);
```

---

## 코드 참조 가이드

### 백엔드 코드 구조

```
backend/src/main/java/com/project/companyanalyzer/
├── controller/
│   └── ExchangeRateController.java       # REST API 엔드포인트
│       ├── getExchangeRates()            # GET /api/exchange-rates
│       ├── getExchangeRate()             # GET /api/exchange-rates/{curUnit}
│       └── getHistoricalRates()          # GET /api/exchange-rates/{curUnit}/historical
│
├── service/
│   └── ExchangeRateService.java          # 비즈니스 로직
│       ├── getExchangeRates()            # 환율 조회 + 전일대비 계산
│       ├── fetchExchangeRates()          # 실제/목업 API 호출
│       ├── fetchFromApi()                # 한국수출입은행 API 호출
│       ├── fetchMockData()               # 목업 데이터 로드
│       ├── convertToDTO()                # Response → DTO 변환
│       ├── parseBigDecimal()             # 문자열 → BigDecimal 변환
│       ├── calculatePreviousDate()       # 전일 날짜 계산
│       └── getHistoricalRates()          # 과거 환율 조회
│
└── dto/
    ├── ExchangeRateResponse.java        # 외부 API 응답 DTO
    ├── ExchangeRateDTO.java              # 프론트엔드 응답 DTO
    └── HistoricalRateDTO.java            # 차트 데이터 DTO
```

### 프론트엔드 코드 구조

```
frontend/src/
├── App.jsx                                # 메인 애플리케이션
│   ├── handleCurrencySelect()            # 통화 선택 핸들러
│   ├── ExchangeRateTable 렌더링
│   └── RateDetailChart 렌더링
│
├── components/
│   ├── ExchangeRateTable.jsx             # 환율 테이블
│   │   ├── loadExchangeRates()           # 환율 데이터 로드
│   │   ├── handleRefresh()               # 새로고침 핸들러
│   │   ├── handleRowClick()              # 행 클릭 핸들러
│   │   └── renderChangeIndicator()       # 전일대비 아이콘 렌더링
│   │
│   ├── RateDetailChart.jsx               # 환율 차트
│   │   ├── loadChartData()               # 차트 데이터 로드
│   │   ├── handlePeriodChange()          # 기간 선택 핸들러
│   │   └── CustomTooltip                 # 커스텀 툴팁
│   │
│   ├── ExchangeRateTable.css             # 테이블 스타일
│   └── RateDetailChart.css               # 차트 스타일
│
└── services/
    └── economyService.js                  # API 호출 서비스
        ├── getExchangeRates()            # 환율 목록 조회
        └── getHistoricalRates()          # 과거 환율 조회
```

### 테스트 코드 구조

```
tests/
└── exchange-rate.spec.js                 # Playwright E2E 테스트
    ├── 환율 정보 목록이 표시되어야 함
    ├── 백엔드 API에서 환율 데이터를 가져와야 함
    ├── 전일대비 변동이 올바르게 표시되어야 함
    ├── 새로고침 버튼이 작동해야 함
    ├── 로딩 상태가 표시되어야 함
    └── 에러 상태가 올바르게 처리되어야 함
```

### 주요 파일 위치

| 파일 | 위치 | 용도 |
|------|------|------|
| ExchangeRateController.java | `backend/src/main/java/.../controller/` | REST API 엔드포인트 |
| ExchangeRateService.java | `backend/src/main/java/.../service/` | 비즈니스 로직 |
| ExchangeRateDTO.java | `backend/src/main/java/.../dto/` | 응답 DTO |
| ExchangeRateTable.jsx | `frontend/src/components/` | 환율 테이블 컴포넌트 |
| RateDetailChart.jsx | `frontend/src/components/` | 환율 차트 컴포넌트 |
| economyService.js | `frontend/src/services/` | API 호출 서비스 |
| vite.config.js | `frontend/` | Vite 설정 (프록시) |
| docker-compose.yml | 프로젝트 루트 | Docker 설정 |
| exchange-rate.spec.js | `tests/` | E2E 테스트 |

---

## 배운 점 & 개선 방향

### 배운 점
1. **Docker 환경에서의 네트워크 통신**
   - 컨테이너 간 통신 시 `localhost` 대신 서비스명 사용
   - 환경변수를 통한 동적 설정의 중요성

2. **필드명 일관성**
   - 백엔드-프론트엔드 간 필드명 통일 (camelCase)
   - DTO 설계 시 명명 규칙 사전 협의 필요

3. **E2E 테스트의 가치**
   - 통합 테스트를 통한 버그 조기 발견
   - 선택자 구체성의 중요성

4. **BigDecimal 정밀도**
   - 금융 데이터는 `double` 대신 `BigDecimal` 사용
   - 반올림 모드 명시 (`RoundingMode.HALF_UP`)

### 개선 방향
1. **캐싱 전략**
   - Redis를 활용한 환율 데이터 캐싱
   - API 호출 횟수 감소 (비용 절감)

2. **에러 핸들링**
   - 더 구체적인 에러 메시지
   - 재시도 로직 (Exponential Backoff)

3. **성능 최적화**
   - 과거 환율 조회 시 병렬 처리
   - 프론트엔드 메모이제이션 (`useMemo`, `useCallback`)

4. **접근성**
   - ARIA 레이블 추가
   - 키보드 네비게이션 지원

---

## 참고 자료

### API 문서
- [한국수출입은행 환율 API](https://www.koreaexim.go.kr/ir/HPHKIR020M01)

### 기술 문서
- [React 공식 문서](https://react.dev/)
- [Vite 공식 문서](https://vitejs.dev/)
- [Recharts 공식 문서](https://recharts.org/)
- [Playwright 공식 문서](https://playwright.dev/)
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)

### 프로젝트 문서
- [CLAUDE.md](../CLAUDE.md) - 프로젝트 전체 가이드
- [prd.md](../prd.md) - 제품 요구사항 명세서

---

**작성일**: 2025-11-26
**작성자**: terryjin89
**버전**: 1.0.0
