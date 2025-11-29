# SCRUM-9: 관심기업 게시판 기능 구현 문서

## 📋 목차

1. [개요](#개요)
2. [구현 기술 스택](#구현-기술-스택)
3. [프로젝트 구조](#프로젝트-구조)
4. [핵심 기능](#핵심-기능)
5. [데이터베이스 설계](#데이터베이스-설계)
6. [API 명세](#api-명세)
7. [프론트엔드 구현](#프론트엔드-구현)
8. [백엔드 구현](#백엔드-구현)
9. [인증 및 보안](#인증-및-보안)
10. [테스트](#테스트)
11. [코드 추적 가이드](#코드-추적-가이드)

---

## 개요

### 목적
사용자가 관심 있는 기업을 등록하고, 주가 정보를 실시간으로 모니터링할 수 있는 관심기업 게시판 기능 구현

### 주요 기능
- **관심기업 목록 조회**: 로그인한 사용자의 관심기업 목록 표시
- **관심기업 등록**: 기업정보 페이지에서 관심기업 추가
- **관심기업 삭제**: 관심기업 목록에서 제거
- **주가 정보 실시간 표시**: 금융위원회 API를 통한 주가 데이터 표시
  - 현재가, 전일대비 변동금액, 변동률
  - 상승(▲ 빨간색) / 하락(▼ 파란색) 표시
- **JWT 인증**: Bearer 토큰 기반 사용자 인증
- **반응형 UI**: 모든 디바이스에서 최적화된 사용자 경험

### 티켓 정보
- **Jira 티켓**: SCRUM-9
- **브랜치**: `feature/SCRUM-9-interest-companies`
- **개발 기간**: 2025-11-11 ~ 2025-11-29
- **Subtasks** (4개 모두 완료):
  - SCRUM-27: [FE] 관심기업 테이블 컴포넌트 구현
  - SCRUM-28: [FE] 인증 가드 및 리다이렉션 구현
  - SCRUM-29: [BE] Stock 엔티티 및 Repository 구현
  - SCRUM-30: [BE] 관심기업 API 엔드포인트 구현

---

## 구현 기술 스택

### 프론트엔드
- **React 19.2.0**: UI 컴포넌트 라이브러리
- **Vite 7.2.2**: 빌드 도구 및 개발 서버
- **Axios 1.13.2**: HTTP 클라이언트 (JWT 인터셉터 포함)
- **React Router DOM v7**: SPA 라우팅 (ProtectedRoute 구현)
- **CSS3**: 스타일링 (모듈 CSS 사용)

### 백엔드
- **Spring Boot 3.3.x**: 웹 애플리케이션 프레임워크
- **Spring Security**: JWT 기반 인증 및 권한 관리
- **Spring Data JPA**: ORM 및 데이터 접근 계층
- **Hibernate**: JPA 구현체
- **MySQL Connector**: 데이터베이스 드라이버
- **Lombok**: 보일러플레이트 코드 제거
- **Swagger/OpenAPI 3**: API 문서화

### 외부 API
- **금융위원회 주식시세정보 API**: 실시간 주가 조회
- **DART API**: 기업 정보 조회 (CompanyService 연동)

### 데이터베이스
- **MySQL 8.0**: 관계형 데이터베이스
- **Character Set**: utf8mb4 (한글 및 이모지 지원)
- **Collation**: utf8mb4_unicode_ci

### 인프라
- **Docker Compose**: 컨테이너 오케스트레이션
- **Docker**: MySQL, Backend, Frontend 컨테이너화

### 개발 도구
- **Playwright MCP**: E2E 테스트 및 브라우저 자동화
- **Git**: 버전 관리
- **GitHub CLI**: PR 생성 및 브랜치 관리

---

## 프로젝트 구조

```
embedWebProject/
├── frontend/
│   └── src/
│       ├── components/
│       │   └── FavoriteTable.jsx         # 관심기업 테이블 컴포넌트
│       ├── pages/
│       │   └── FavoritesPage.jsx         # 관심기업 페이지
│       ├── services/
│       │   ├── axiosInstance.js          # Axios 인터셉터 (JWT)
│       │   ├── authService.js            # 인증 서비스 (토큰 관리)
│       │   └── favoritesService.js       # 관심기업 API 서비스
│       ├── hooks/
│       │   └── useAuth.js                # 인증 커스텀 훅
│       └── App.jsx                       # 라우팅 설정 (ProtectedRoute)
├── backend/
│   └── src/main/java/com/project/companyanalyzer/
│       ├── controller/
│       │   └── FavoritesController.java  # 관심기업 REST API 엔드포인트
│       ├── service/
│       │   └── FavoritesService.java     # 관심기업 비즈니스 로직
│       ├── repository/
│       │   └── StockRepository.java      # Stock 엔티티 데이터 접근 계층
│       ├── entity/
│       │   └── Stock.java                # 관심기업 JPA 엔티티
│       └── dto/
│           ├── AddFavoriteRequest.java   # 관심기업 등록 요청 DTO
│           ├── FavoriteResponse.java     # 관심기업 응답 DTO
│           └── DeleteFavoriteResponse.java # 관심기업 삭제 응답 DTO
└── readme/
    └── interestCoFunction.md             # 본 문서
```

---

## 핵심 기능

### 1. 관심기업 목록 조회

#### 기능 설명
- 로그인한 사용자의 관심기업 목록을 조회
- 등록일시(registeredAt) 내림차순으로 정렬
- 각 기업의 주가 정보를 금융위원회 API로 실시간 조회

#### 주요 프로세스
```
사용자 요청 → JWT 인증 확인 → DB에서 관심기업 목록 조회
  → 각 종목코드로 금융위원회 API 호출 → 주가 정보 표시
```

#### 코드 위치
- **프론트엔드**: `frontend/src/components/FavoriteTable.jsx:64-79` (fetchFavorites 함수)
- **백엔드**: `backend/src/main/java/com/project/companyanalyzer/controller/FavoritesController.java:58-79` (getFavorites 메서드)
- **서비스**: `backend/src/main/java/com/project/companyanalyzer/service/FavoritesService.java:54-66`

### 2. 관심기업 등록

#### 기능 설명
- 기업정보 페이지에서 특정 기업을 관심기업으로 등록
- 중복 등록 방지 (UNIQUE KEY 제약조건)
- DB에 없는 기업은 DART API를 통해 조회 후 자동 저장
- 비상장 기업도 등록 가능 (stockCode = null)

#### 주요 프로세스
```
사용자 등록 요청 → JWT 인증 확인 → 중복 체크 (corpCode 기반)
  → Company 엔티티 조회/생성 (DART API) → Stock 엔티티 생성 → DB 저장
```

#### 코드 위치
- **프론트엔드**: `frontend/src/components/CompanyTable.jsx` (handleAddToFavorites 함수)
- **백엔드**: `backend/src/main/java/com/project/companyanalyzer/controller/FavoritesController.java:90-122` (addFavorite 메서드)
- **서비스**: `backend/src/main/java/com/project/companyanalyzer/service/FavoritesService.java:80-128`

### 3. 관심기업 삭제

#### 기능 설명
- 사용자가 관심기업 목록에서 특정 기업 제거
- 확인 다이얼로그 표시 후 삭제 진행

#### 주요 프로세스
```
사용자 삭제 요청 → 확인 다이얼로그 표시 → JWT 인증 확인
  → DB에서 해당 Stock 엔티티 삭제 → 목록 새로고침
```

#### 코드 위치
- **프론트엔드**: `frontend/src/components/FavoriteTable.jsx:81-99` (handleRemove 함수)
- **백엔드**: `backend/src/main/java/com/project/companyanalyzer/controller/FavoritesController.java:132-162` (deleteFavorite 메서드)
- **서비스**: `backend/src/main/java/com/project/companyanalyzer/service/FavoritesService.java:203-217`

### 4. 주가 정보 실시간 표시

#### 기능 설명
- 금융위원회 주식시세정보 API를 통해 실시간 주가 조회
- 현재가, 전일대비 변동금액, 변동률 표시
- 상승(▲ 빨간색) / 하락(▼ 파란색) 시각적 표시

#### 주요 프로세스
```
관심기업 목록 조회 → 각 종목코드에 대해 금융위원회 API 호출
  → 주가 데이터 파싱 → 변동률 계산 → UI 렌더링
```

#### 코드 위치
- **프론트엔드**: `frontend/src/components/FavoriteTable.jsx:101-157` (fetchStockPrices 함수)
- **금융위원회 API 엔드포인트**: `/uapi/domestic-stock/v1/quotations/inquire-price`

### 5. JWT 인증 및 보호된 라우트

#### 기능 설명
- JWT Bearer 토큰 기반 인증
- 인증되지 않은 사용자는 로그인 페이지로 리다이렉트
- Axios 인터셉터를 통한 자동 토큰 첨부

#### 주요 프로세스
```
사용자 페이지 접근 → ProtectedRoute 체크 → 토큰 존재 확인
  → 없으면 로그인 페이지로 리다이렉트 → 있으면 페이지 렌더링
```

#### 코드 위치
- **프론트엔드**:
  - `frontend/src/App.jsx` (ProtectedRoute 컴포넌트)
  - `frontend/src/services/axiosInstance.js:18-26` (요청 인터셉터)
  - `frontend/src/services/authService.js:1-24` (토큰 관리)
- **백엔드**: `backend/src/main/java/com/project/companyanalyzer/controller/FavoritesController.java:173-190` (getUserCodeFromAuthentication 메서드)

---

## 데이터베이스 설계

### Stock 테이블 (관심기업)

```sql
CREATE TABLE stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_code VARCHAR(255) NOT NULL,
    stock_code VARCHAR(6) NULL,           -- 비상장 기업은 NULL 가능
    corp_code VARCHAR(8) NOT NULL,
    registered_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,

    -- 제약조건
    CONSTRAINT uk_user_corp UNIQUE (user_code, corp_code),
    CONSTRAINT fk_stock_member FOREIGN KEY (user_code) REFERENCES member(user_code),
    CONSTRAINT fk_stock_company FOREIGN KEY (corp_code) REFERENCES company(corp_code),

    -- 인덱스
    INDEX idx_user_code (user_code),
    INDEX idx_stock_code (stock_code),
    INDEX idx_corp_code (corp_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 컬럼 설명

| 컬럼명 | 타입 | 설명 | 제약조건 |
|--------|------|------|----------|
| id | BIGINT | 관심기업 고유 식별자 | PK, Auto Increment |
| user_code | VARCHAR(255) | 사용자 코드 | NOT NULL, FK (Member) |
| stock_code | VARCHAR(6) | 주식 종목코드 (6자리) | NULL 가능 (비상장 기업) |
| corp_code | VARCHAR(8) | DART 기업 코드 (8자리) | NOT NULL, FK (Company) |
| registered_at | DATETIME | 관심기업 등록일시 | NOT NULL |
| created_at | DATETIME | 레코드 생성 시간 | NOT NULL, @CreationTimestamp |
| updated_at | DATETIME | 레코드 수정 시간 | NOT NULL, @UpdateTimestamp |

### 제약조건 설명

1. **UNIQUE KEY (user_code, corp_code)**
   - 사용자당 동일 기업 중복 등록 방지
   - 코드 위치: `backend/src/main/java/com/project/companyanalyzer/entity/Stock.java:32-36`

2. **Foreign Key (user_code → Member.userCode)**
   - 관심기업을 등록한 사용자 참조
   - 코드 위치: `backend/src/main/java/com/project/companyanalyzer/entity/Stock.java:67-74`

3. **Foreign Key (corp_code → Company.corpCode)**
   - 관심기업의 기업 정보 참조
   - 코드 위치: `backend/src/main/java/com/project/companyanalyzer/entity/Stock.java:91-98`

### 인덱스 설명

1. **idx_user_code**: 사용자별 관심기업 조회 성능 최적화
2. **idx_stock_code**: 종목코드 기반 조회 성능 최적화
3. **idx_corp_code**: 기업 코드 기반 조회 성능 최적화

### ERD 관계

```
Member (1) ────< (N) Stock (N) >──── (1) Company
         user_code           corp_code
```

---

## API 명세

### 1. 관심기업 목록 조회

#### 요청
```http
GET /api/favorites
Authorization: Bearer {JWT_TOKEN}
```

#### 응답 (200 OK)
```json
[
  {
    "id": 1,
    "stockCode": "005930",
    "corpCode": "00126380",
    "companyName": "삼성전자",
    "stockName": "삼성전자보통주",
    "registeredAt": "2025-11-29T15:30:00"
  }
]
```

#### 에러 응답
- **401 Unauthorized**: JWT 토큰이 없거나 유효하지 않음

#### 코드 위치
- **컨트롤러**: `backend/src/main/java/com/project/companyanalyzer/controller/FavoritesController.java:58-79`
- **서비스**: `backend/src/main/java/com/project/companyanalyzer/service/FavoritesService.java:54-66`
- **리포지토리**: `backend/src/main/java/com/project/companyanalyzer/repository/StockRepository.java:35-40`

---

### 2. 관심기업 등록

#### 요청
```http
POST /api/favorites
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json

{
  "stockCode": "005930",
  "corpCode": "00126380"
}
```

#### 요청 DTO 검증

| 필드 | 타입 | 필수 | 제약조건 | 설명 |
|------|------|------|----------|------|
| stockCode | String | 선택 | 숫자 6자리 또는 빈 문자열 | 종목코드 (비상장 기업은 빈 문자열) |
| corpCode | String | 필수 | 숫자 8자리 | DART 기업 코드 |

- **코드 위치**: `backend/src/main/java/com/project/companyanalyzer/dto/AddFavoriteRequest.java:22-40`

#### 응답 (201 Created)
```json
{
  "id": 1,
  "stockCode": "005930",
  "corpCode": "00126380",
  "companyName": "삼성전자",
  "stockName": "삼성전자보통주",
  "registeredAt": "2025-11-29T15:30:00"
}
```

#### 에러 응답
- **400 Bad Request**: 이미 등록된 관심기업
- **401 Unauthorized**: JWT 토큰이 없거나 유효하지 않음
- **422 Unprocessable Entity**: 유효하지 않은 입력값

#### 코드 위치
- **컨트롤러**: `backend/src/main/java/com/project/companyanalyzer/controller/FavoritesController.java:90-122`
- **서비스**: `backend/src/main/java/com/project/companyanalyzer/service/FavoritesService.java:80-128`

---

### 3. 관심기업 삭제

#### 요청
```http
DELETE /api/favorites/{stockCode}
Authorization: Bearer {JWT_TOKEN}
```

#### 경로 파라미터
- **stockCode**: 종목코드 (6자리)
  - 예: `005930` (삼성전자)

#### 응답 (200 OK)
```json
{
  "success": true,
  "message": "관심기업이 삭제되었습니다."
}
```

#### 에러 응답
- **401 Unauthorized**: JWT 토큰이 없거나 유효하지 않음
- **404 Not Found**: 삭제할 관심기업을 찾을 수 없음
```json
{
  "success": false,
  "message": "삭제할 관심기업을 찾을 수 없습니다."
}
```

#### 코드 위치
- **컨트롤러**: `backend/src/main/java/com/project/companyanalyzer/controller/FavoritesController.java:132-162`
- **서비스**: `backend/src/main/java/com/project/companyanalyzer/service/FavoritesService.java:203-217`
- **리포지토리**: `backend/src/main/java/com/project/companyanalyzer/repository/StockRepository.java:107-113`

---

## 프론트엔드 구현

### 1. FavoritesPage 컴포넌트

#### 역할
- 관심기업 페이지의 메인 컨테이너
- 네비게이션 바, 페이지 헤더, FavoriteTable 컴포넌트 렌더링
- 사용자 인증 상태 확인 및 로그아웃 기능

#### 코드 위치
`frontend/src/pages/FavoritesPage.jsx`

#### 주요 기능
```javascript
/**
 * 로그아웃 핸들러
 * - AuthContext의 logout 함수 호출
 * - 홈 페이지로 리다이렉트
 */
const handleLogout = () => {
  logout();
  window.location.href = '/';
};
```

---

### 2. FavoriteTable 컴포넌트

#### 역할
- 관심기업 목록 테이블 렌더링
- 주가 정보 실시간 조회 및 표시
- 관심기업 삭제 기능

#### 코드 위치
`frontend/src/components/FavoriteTable.jsx`

#### 주요 상태 관리
```javascript
const [favorites, setFavorites] = useState([]);       // 관심기업 목록
const [stockPrices, setStockPrices] = useState({});   // 주가 정보 (종목코드: 주가)
const [loading, setLoading] = useState(true);         // 로딩 상태
const [error, setError] = useState(null);             // 에러 상태
```

#### 주요 함수

##### 1) fetchFavorites - 관심기업 목록 조회
```javascript
/**
 * 관심기업 목록 조회
 * - favoritesService.getFavorites() API 호출
 * - JWT 토큰이 자동으로 Axios 인터셉터를 통해 첨부됨
 */
const fetchFavorites = async () => {
  try {
    const data = await favoritesService.getFavorites();
    setFavorites(data);
    // 주가 정보 조회
    if (data.length > 0) {
      fetchStockPrices(data);
    }
  } catch (err) {
    // 401 에러 시 로그인 페이지로 리다이렉트
    if (err.response?.status === 401) {
      navigate('/');
    }
  }
};
```
- **코드 위치**: `frontend/src/components/FavoriteTable.jsx:64-79`

##### 2) handleRemove - 관심기업 삭제
```javascript
/**
 * 관심기업 삭제
 * - 확인 다이얼로그 표시
 * - favoritesService.removeFavorite(stockCode) API 호출
 * - 성공 시 목록 새로고침
 */
const handleRemove = async (stockCode, companyName) => {
  if (!window.confirm(`${companyName}을(를) 관심기업에서 삭제하시겠습니까?`)) {
    return;
  }
  try {
    await favoritesService.removeFavorite(stockCode);
    fetchFavorites(); // 목록 새로고침
  } catch (err) {
    alert('삭제 중 오류가 발생했습니다.');
  }
};
```
- **코드 위치**: `frontend/src/components/FavoriteTable.jsx:81-99`

##### 3) fetchStockPrices - 주가 정보 조회
```javascript
/**
 * 주가 정보 실시간 조회
 * - 금융위원회 API를 각 종목코드에 대해 호출
 * - Promise.all을 사용하여 병렬 처리
 * - 주가 데이터 파싱 및 상태 업데이트
 */
const fetchStockPrices = async (favoriteList) => {
  const prices = {};

  const promises = favoriteList
    .filter(fav => fav.stockCode) // 종목코드가 있는 경우만
    .map(async (fav) => {
      try {
        const response = await axios.get(
          `/uapi/domestic-stock/v1/quotations/inquire-price`,
          {
            params: { FID_INPUT_ISCD: fav.stockCode },
            headers: {
              'appkey': import.meta.env.VITE_FINANCIAL_API_KEY,
              'appsecret': import.meta.env.VITE_FINANCIAL_SECRET_KEY,
              'tr_id': 'FHKST01010100'
            }
          }
        );

        if (response.data?.output) {
          prices[fav.stockCode] = response.data.output;
        }
      } catch (err) {
        console.error(`주가 조회 실패: ${fav.stockCode}`, err);
      }
    });

  await Promise.all(promises);
  setStockPrices(prices);
};
```
- **코드 위치**: `frontend/src/components/FavoriteTable.jsx:101-157`

---

### 3. favoritesService - API 서비스 레이어

#### 역할
- Axios를 사용한 관심기업 API 호출
- 백엔드 엔드포인트와 통신

#### 코드 위치
`frontend/src/services/favoritesService.js`

#### 주요 함수
```javascript
/**
 * 관심기업 목록 조회
 */
export const getFavorites = async () => {
  const response = await axiosInstance.get('/favorites');
  return response.data;
};

/**
 * 관심기업 등록
 */
export const addFavorite = async (stockCode, corpCode) => {
  const response = await axiosInstance.post('/favorites', {
    stockCode,
    corpCode
  });
  return response.data;
};

/**
 * 관심기업 삭제
 */
export const removeFavorite = async (stockCode) => {
  const response = await axiosInstance.delete(`/favorites/${stockCode}`);
  return response.data;
};
```

---

### 4. Axios 인터셉터 (JWT 토큰 자동 첨부)

#### 역할
- 모든 API 요청에 JWT 토큰 자동 첨부
- 401 에러 시 자동으로 로그인 페이지로 리다이렉트

#### 코드 위치
`frontend/src/services/axiosInstance.js:18-26`

#### 구현 내용
```javascript
/**
 * 요청 인터셉터
 * - localStorage에서 JWT 토큰 읽기
 * - Authorization 헤더에 Bearer 토큰 첨부
 */
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

/**
 * 응답 인터셉터
 * - 401 에러 시 토큰 제거 및 로그인 페이지로 리다이렉트
 */
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/';
    }
    return Promise.reject(error);
  }
);
```

---

### 5. ProtectedRoute 컴포넌트

#### 역할
- 인증되지 않은 사용자의 접근 차단
- 로그인 페이지로 자동 리다이렉트

#### 코드 위치
`frontend/src/App.jsx`

#### 구현 내용
```javascript
/**
 * ProtectedRoute 컴포넌트
 * - useAuth 훅을 사용하여 사용자 인증 상태 확인
 * - 인증되지 않은 경우 로그인 페이지로 리다이렉트
 */
function ProtectedRoute({ children }) {
  const { user } = useAuth();

  if (!user) {
    return <Navigate to="/" replace />;
  }

  return children;
}

// 라우팅 설정
<Route
  path="/favorites"
  element={
    <ProtectedRoute>
      <FavoritesPage />
    </ProtectedRoute>
  }
/>
```

---

## 백엔드 구현

### 1. FavoritesController - REST API 컨트롤러

#### 역할
- 관심기업 CRUD API 엔드포인트 제공
- JWT 인증 확인 (Spring Security)
- 요청 검증 및 응답 처리

#### 코드 위치
`backend/src/main/java/com/project/companyanalyzer/controller/FavoritesController.java`

#### 주요 메서드

##### 1) getFavorites - 관심기업 목록 조회
```java
/**
 * 관심기업 목록 조회
 *
 * @return 관심기업 목록 (FavoriteResponse DTO 리스트)
 */
@GetMapping
@Operation(summary = "관심기업 목록 조회")
public ResponseEntity<List<FavoriteResponse>> getFavorites() {
    // JWT 토큰에서 사용자 코드 추출
    String userCode = getUserCodeFromAuthentication();

    log.info("GET /api/favorites - userCode: {}", userCode);

    // 관심기업 목록 조회
    List<FavoriteResponse> favorites = favoritesService.getFavorites(userCode);

    return ResponseEntity.ok(favorites);
}
```
- **라인**: 58-79

##### 2) addFavorite - 관심기업 등록
```java
/**
 * 관심기업 등록
 *
 * @param request 관심기업 등록 요청 (stockCode, corpCode)
 * @return 등록된 관심기업 정보 (FavoriteResponse DTO)
 */
@PostMapping
@Operation(summary = "관심기업 등록")
public ResponseEntity<FavoriteResponse> addFavorite(
    @Valid @RequestBody AddFavoriteRequest request
) {
    String userCode = getUserCodeFromAuthentication();

    try {
        FavoriteResponse response = favoritesService.addFavorite(userCode, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (IllegalArgumentException e) {
        log.warn("관심기업 등록 실패 - error: {}", e.getMessage());
        throw e;
    }
}
```
- **라인**: 90-122

##### 3) deleteFavorite - 관심기업 삭제
```java
/**
 * 관심기업 삭제
 *
 * @param stockCode 종목코드 (6자리)
 * @return 삭제 결과 (DeleteFavoriteResponse DTO)
 */
@DeleteMapping("/{stockCode}")
@Operation(summary = "관심기업 삭제")
public ResponseEntity<DeleteFavoriteResponse> deleteFavorite(
    @PathVariable String stockCode
) {
    String userCode = getUserCodeFromAuthentication();

    DeleteFavoriteResponse response = favoritesService.deleteFavorite(userCode, stockCode);

    if (response.isSuccess()) {
        return ResponseEntity.ok(response);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
```
- **라인**: 132-162

##### 4) getUserCodeFromAuthentication - 사용자 코드 추출
```java
/**
 * SecurityContext에서 인증된 사용자의 userCode 추출
 *
 * JWT 인증 필터가 SecurityContext에 설정한 인증 정보에서 사용자 코드를 가져옵니다.
 *
 * @return 사용자 코드 (userCode)
 * @throws IllegalStateException 인증되지 않은 경우
 */
private String getUserCodeFromAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
        throw new IllegalStateException("인증되지 않은 사용자입니다.");
    }

    Object principal = authentication.getPrincipal();

    if (principal instanceof UserDetails) {
        UserDetails userDetails = (UserDetails) principal;
        return userDetails.getUsername(); // username = userCode
    }

    throw new IllegalStateException("인증 정보를 가져올 수 없습니다.");
}
```
- **라인**: 173-190

---

### 2. FavoritesService - 비즈니스 로직

#### 역할
- 관심기업 관련 비즈니스 로직 처리
- DB 트랜잭션 관리
- Company 엔티티 조회/생성 (DART API 연동)

#### 코드 위치
`backend/src/main/java/com/project/companyanalyzer/service/FavoritesService.java`

#### 주요 메서드

##### 1) getFavorites - 관심기업 목록 조회
```java
/**
 * 관심기업 목록 조회
 *
 * @param userCode 사용자 코드
 * @return 관심기업 목록 (FavoriteResponse DTO 리스트)
 */
public List<FavoriteResponse> getFavorites(String userCode) {
    log.info("관심기업 목록 조회 - userCode: {}", userCode);

    // 사용자별 관심기업 목록 조회 (Member, Company fetch join)
    List<Stock> stocks = stockRepository.findByUserCodeWithMemberAndCompany(userCode);

    // Stock 엔티티를 FavoriteResponse DTO로 변환
    return stocks.stream()
        .map(this::convertToFavoriteResponse)
        .collect(Collectors.toList());
}
```
- **라인**: 54-66

##### 2) addFavorite - 관심기업 등록
```java
/**
 * 관심기업 등록
 *
 * @param userCode 사용자 코드
 * @param request 관심기업 등록 요청 (stockCode, corpCode)
 * @return 등록된 관심기업 정보 (FavoriteResponse DTO)
 * @throws IllegalArgumentException 중복 등록 또는 유효하지 않은 요청 시
 */
@Transactional
public FavoriteResponse addFavorite(String userCode, AddFavoriteRequest request) {
    // 1. 중복 등록 체크 (corpCode 기반)
    Optional<Stock> existingStock = stockRepository.findByUserCodeAndCorpCode(
        userCode, request.getCorpCode());
    if (existingStock.isPresent()) {
        throw new IllegalArgumentException("이미 등록된 관심기업입니다.");
    }

    // 2. Member 엔티티 조회
    Member member = memberRepository.findByUserCode(userCode)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    // 3. Company 엔티티 조회 또는 생성 (DART API)
    Company company = getOrCreateCompany(request.getCorpCode());

    // 4. stockCode 검증 (비상장 기업 고려)
    String requestStockCode = (request.getStockCode() == null || request.getStockCode().trim().isEmpty())
        ? null : request.getStockCode();
    String companyStockCode = company.getStockCode();

    if (!isStockCodeMatching(requestStockCode, companyStockCode)) {
        throw new IllegalArgumentException("종목코드가 기업 정보와 일치하지 않습니다.");
    }

    // 5. Stock 엔티티 생성 및 저장
    Stock stock = Stock.builder()
        .member(member)
        .stockCode(requestStockCode)
        .company(company)
        .build();

    Stock savedStock = stockRepository.save(stock);

    // 6. FavoriteResponse DTO로 변환하여 반환
    return convertToFavoriteResponse(savedStock);
}
```
- **라인**: 80-128

##### 3) getOrCreateCompany - Company 엔티티 조회/생성
```java
/**
 * Company 엔티티 조회 또는 생성
 * DB에 없으면 DART API를 통해 조회하고 저장합니다.
 *
 * @param corpCode 기업 코드
 * @return Company 엔티티
 */
private Company getOrCreateCompany(String corpCode) {
    // DB에서 먼저 조회
    Optional<Company> companyOpt = companyRepository.findByCorpCode(corpCode);
    if (companyOpt.isPresent()) {
        return companyOpt.get();
    }

    // DB에 없으면 DART API를 통해 조회
    log.info("DB에 없음, DART API를 통해 기업 정보 조회 - corpCode: {}", corpCode);
    CompanyDTO companyDTO = companyService.getCompanyByCorpCode(corpCode);

    // CompanyDTO를 Company 엔티티로 변환하여 저장
    Company company = Company.builder()
        .corpCode(companyDTO.getCorpCode())
        .corpName(companyDTO.getCorpName())
        // ... (기타 필드 설정)
        .build();

    Company savedCompany = companyRepository.save(company);
    log.info("DART API에서 조회한 기업 정보 저장 완료 - corpCode: {}", savedCompany.getCorpCode());

    return savedCompany;
}
```
- **라인**: 137-175

##### 4) deleteFavorite - 관심기업 삭제
```java
/**
 * 관심기업 삭제
 *
 * @param userCode 사용자 코드
 * @param stockCode 종목코드
 * @return 삭제 결과 (DeleteFavoriteResponse DTO)
 */
@Transactional
public DeleteFavoriteResponse deleteFavorite(String userCode, String stockCode) {
    log.info("관심기업 삭제 시도 - userCode: {}, stockCode: {}", userCode, stockCode);

    // 관심기업 삭제 (삭제된 행의 수 반환)
    int deletedCount = stockRepository.deleteByUserCodeAndStockCode(userCode, stockCode);

    if (deletedCount > 0) {
        return DeleteFavoriteResponse.success("관심기업이 삭제되었습니다.");
    } else {
        return DeleteFavoriteResponse.failure("삭제할 관심기업을 찾을 수 없습니다.");
    }
}
```
- **라인**: 203-217

---

### 3. StockRepository - 데이터 접근 계층

#### 역할
- Stock 엔티티에 대한 CRUD 작업
- 커스텀 쿼리 메서드 제공 (JPQL)
- N+1 문제 방지 (fetch join)

#### 코드 위치
`backend/src/main/java/com/project/companyanalyzer/repository/StockRepository.java`

#### 주요 메서드

##### 1) findByUserCodeWithMemberAndCompany - 관심기업 목록 조회 (fetch join)
```java
/**
 * 사용자 코드로 관심기업 목록 조회
 *
 * Member와 Company를 fetch join하여 N+1 문제를 방지합니다.
 * 등록일시(registeredAt) 내림차순으로 정렬합니다.
 *
 * @param userCode 사용자 코드
 * @return 사용자의 관심기업 목록
 */
@Query("SELECT s FROM Stock s " +
       "JOIN FETCH s.member m " +
       "JOIN FETCH s.company c " +
       "WHERE m.userCode = :userCode " +
       "ORDER BY s.registeredAt DESC")
List<Stock> findByUserCodeWithMemberAndCompany(@Param("userCode") String userCode);
```
- **라인**: 35-40

##### 2) findByUserCodeAndCorpCode - 중복 체크용 조회
```java
/**
 * 사용자 코드와 기업 코드로 관심기업 조회
 *
 * 특정 사용자가 특정 기업을 관심기업으로 등록했는지 확인할 때 사용합니다.
 *
 * @param userCode 사용자 코드
 * @param corpCode 기업 코드
 * @return 관심기업 Optional
 */
@Query("SELECT s FROM Stock s " +
       "JOIN FETCH s.member m " +
       "JOIN FETCH s.company c " +
       "WHERE m.userCode = :userCode AND c.corpCode = :corpCode")
Optional<Stock> findByUserCodeAndCorpCode(
    @Param("userCode") String userCode,
    @Param("corpCode") String corpCode
);
```
- **라인**: 70-77

##### 3) deleteByUserCodeAndStockCode - 관심기업 삭제
```java
/**
 * 사용자 코드와 종목코드로 관심기업 삭제
 *
 * @param userCode  사용자 코드
 * @param stockCode 주식 종목코드
 * @return 삭제된 행의 수 (0 or 1)
 */
@Modifying
@Query("DELETE FROM Stock s " +
       "WHERE s.member.userCode = :userCode AND s.stockCode = :stockCode")
int deleteByUserCodeAndStockCode(
    @Param("userCode") String userCode,
    @Param("stockCode") String stockCode
);
```
- **라인**: 107-113

---

### 4. Stock 엔티티

#### 역할
- 관심기업 데이터 모델
- JPA 엔티티 매핑
- Member, Company 엔티티와의 관계 설정

#### 코드 위치
`backend/src/main/java/com/project/companyanalyzer/entity/Stock.java`

#### 주요 어노테이션 및 설정

##### 1) @Entity 및 테이블 설정
```java
@Entity
@Table(
    name = "stock",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_corp",
            columnNames = {"user_code", "corp_code"}  // 중복 등록 방지
        )
    },
    indexes = {
        @Index(name = "idx_user_code", columnList = "user_code"),
        @Index(name = "idx_stock_code", columnList = "stock_code"),
        @Index(name = "idx_corp_code", columnList = "corp_code")
    }
)
```
- **라인**: 29-43

##### 2) Member 관계 설정 (ManyToOne)
```java
/**
 * 사용자 코드
 *
 * Member 엔티티를 참조하는 외래키
 */
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(
    name = "user_code",
    referencedColumnName = "user_code",
    foreignKey = @ForeignKey(name = "fk_stock_member"),
    nullable = false
)
private Member member;
```
- **라인**: 67-74

##### 3) Company 관계 설정 (ManyToOne)
```java
/**
 * 기업 코드
 *
 * Company 엔티티를 참조하는 외래키
 */
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(
    name = "corp_code",
    referencedColumnName = "corp_code",
    foreignKey = @ForeignKey(name = "fk_stock_company"),
    nullable = false
)
private Company company;
```
- **라인**: 91-98

##### 4) @PrePersist - registeredAt 자동 설정
```java
/**
 * registeredAt 자동 설정
 *
 * 엔티티가 persist되기 전에 자동으로 호출됩니다.
 */
@PrePersist
public void setDefaultRegisteredAt() {
    if (this.registeredAt == null) {
        this.registeredAt = LocalDateTime.now();
    }
}
```
- **라인**: 132-137

##### 5) 편의 메서드
```java
/**
 * 편의 메서드: userCode 반환
 */
public String getUserCode() {
    return member != null ? member.getUserCode() : null;
}

/**
 * 편의 메서드: corpCode 반환
 */
public String getCorpCode() {
    return company != null ? company.getCorpCode() : null;
}

/**
 * 편의 메서드: companyName 반환
 */
public String getCompanyName() {
    return company != null ? company.getCorpName() : null;
}
```
- **라인**: 144-164

---

## 인증 및 보안

### 1. JWT 기반 인증 시스템

#### 아키텍처
```
사용자 로그인 → JWT 토큰 발급 → localStorage 저장
  → 이후 모든 API 요청 시 Authorization 헤더에 토큰 첨부
  → 백엔드에서 토큰 검증 → SecurityContext에 인증 정보 설정
```

#### 프론트엔드 토큰 관리
- **코드 위치**: `frontend/src/services/authService.js`
```javascript
/**
 * JWT 토큰 저장
 */
export const setToken = (token) => {
  localStorage.setItem('token', token);
};

/**
 * JWT 토큰 조회
 */
export const getToken = () => {
  return localStorage.getItem('token');
};

/**
 * JWT 토큰 제거 (로그아웃)
 */
export const removeToken = () => {
  localStorage.removeItem('token');
};
```

#### Axios 인터셉터 - 토큰 자동 첨부
- **코드 위치**: `frontend/src/services/axiosInstance.js:18-26`
```javascript
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);
```

#### 백엔드 토큰 검증
- **Spring Security 필터 체인**이 JWT 토큰을 검증
- 검증 성공 시 `SecurityContext`에 인증 정보 설정
- **코드 위치**: `backend/src/main/java/com/project/companyanalyzer/controller/FavoritesController.java:173-190`
```java
private String getUserCodeFromAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
        throw new IllegalStateException("인증되지 않은 사용자입니다.");
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof UserDetails) {
        UserDetails userDetails = (UserDetails) principal;
        return userDetails.getUsername(); // username = userCode
    }

    throw new IllegalStateException("인증 정보를 가져올 수 없습니다.");
}
```

### 2. ProtectedRoute - 접근 제어

#### 역할
- 인증되지 않은 사용자의 페이지 접근 차단
- 로그인 페이지로 자동 리다이렉트

#### 코드 위치
`frontend/src/App.jsx`

```javascript
function ProtectedRoute({ children }) {
  const { user } = useAuth();

  if (!user) {
    return <Navigate to="/" replace />;
  }

  return children;
}
```

#### 라우팅 설정
```javascript
<Route
  path="/favorites"
  element={
    <ProtectedRoute>
      <FavoritesPage />
    </ProtectedRoute>
  }
/>
```

### 3. Spring Security 설정

#### @SecurityRequirement 어노테이션
- Swagger API 문서에서 JWT 인증 필요 표시
- **코드 위치**: `backend/src/main/java/com/project/companyanalyzer/controller/FavoritesController.java:43`
```java
@SecurityRequirement(name = "bearerAuth")
public class FavoritesController {
    // ...
}
```

---

## 테스트

### 1. 프론트엔드 E2E 테스트 (Playwright)

#### 테스트 시나리오
1. **인증 체크**
   - 로그인하지 않은 상태에서 `/favorites` 접근
   - 로그인 페이지로 리다이렉트 확인

2. **관심기업 목록 조회**
   - 로그인 후 `/favorites` 접근
   - 관심기업 테이블 렌더링 확인
   - 주가 정보 표시 확인

3. **관심기업 삭제**
   - 삭제 버튼 클릭
   - 확인 다이얼로그 표시 확인
   - 삭제 후 목록 새로고침 확인

#### Playwright 테스트 도구 사용
```javascript
// Playwright MCP를 통한 브라우저 자동화
await browser_navigate({ url: 'http://localhost:5173/favorites' });
await browser_snapshot(); // 페이지 스냅샷 확인
await browser_click({ element: '삭제 버튼', ref: 'button[data-testid="delete-btn"]' });
```

### 2. 백엔드 API 테스트

#### Postman/cURL을 통한 API 테스트

##### 1) 관심기업 목록 조회
```bash
curl -X GET http://localhost:8080/api/favorites \
  -H "Authorization: Bearer {JWT_TOKEN}"
```

**예상 응답** (200 OK):
```json
[
  {
    "id": 1,
    "stockCode": "005930",
    "corpCode": "00126380",
    "companyName": "삼성전자",
    "stockName": "삼성전자보통주",
    "registeredAt": "2025-11-29T15:30:00"
  }
]
```

##### 2) 관심기업 등록
```bash
curl -X POST http://localhost:8080/api/favorites \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "stockCode": "005930",
    "corpCode": "00126380"
  }'
```

**예상 응답** (201 Created):
```json
{
  "id": 1,
  "stockCode": "005930",
  "corpCode": "00126380",
  "companyName": "삼성전자",
  "stockName": "삼성전자보통주",
  "registeredAt": "2025-11-29T15:30:00"
}
```

##### 3) 관심기업 삭제
```bash
curl -X DELETE http://localhost:8080/api/favorites/005930 \
  -H "Authorization: Bearer {JWT_TOKEN}"
```

**예상 응답** (200 OK):
```json
{
  "success": true,
  "message": "관심기업이 삭제되었습니다."
}
```

### 3. 테스트 체크리스트

#### 인증 확인
- [x] 로그인하지 않은 상태로 `/favorites` 접근 시 로그인 페이지로 리다이렉트
- [x] JWT 토큰 없이 API 호출 시 401 에러 응답
- [x] 유효하지 않은 JWT 토큰으로 API 호출 시 401 에러 응답

#### 관심기업 목록 조회
- [x] 로그인한 상태로 관심기업 페이지 접근
- [x] 관심기업 목록이 테이블 형태로 표시
- [x] 각 기업별로 기업명, 현재가, 전일대비, 등록일이 표시
- [x] 등록된 관심기업이 없으면 "등록된 관심기업이 없습니다" 메시지 표시

#### 주가 정보 실시간 표시
- [x] 현재가가 표시됨
- [x] 전일 대비 변동금액과 변동률이 표시됨
- [x] 상승 시 ▲ 빨간색, 하락 시 ▼ 파란색으로 표시

#### 관심기업 삭제
- [x] 삭제 버튼 클릭 시 확인 다이얼로그 표시
- [x] 확인 시 관심기업 삭제됨
- [x] 목록이 즉시 업데이트됨

#### 관심기업 등록
- [x] 기업정보 페이지에서 관심기업 추가 가능
- [x] 중복 등록 시 에러 메시지 표시
- [x] DB에 없는 기업은 DART API를 통해 자동 저장
- [x] 비상장 기업도 등록 가능

---

## 코드 추적 가이드

### 프론트엔드 코드 추적

#### 1. 관심기업 목록 조회 플로우
```
FavoritesPage.jsx (페이지 진입)
  ↓
FavoriteTable.jsx:64-79 (fetchFavorites 함수)
  ↓
favoritesService.js:getFavorites (API 호출)
  ↓
axiosInstance.js:18-26 (JWT 토큰 자동 첨부)
  ↓
Backend API: GET /api/favorites
  ↓
FavoriteTable.jsx:101-157 (fetchStockPrices 함수)
  ↓
금융위원회 API 호출 (주가 정보 조회)
  ↓
FavoriteTable.jsx (UI 렌더링)
```

#### 2. 관심기업 삭제 플로우
```
FavoriteTable.jsx:81-99 (handleRemove 함수)
  ↓
window.confirm (확인 다이얼로그)
  ↓
favoritesService.js:removeFavorite (API 호출)
  ↓
Backend API: DELETE /api/favorites/{stockCode}
  ↓
fetchFavorites (목록 새로고침)
```

#### 3. JWT 인증 플로우
```
사용자 로그인 성공
  ↓
authService.js:setToken (토큰 저장)
  ↓
localStorage.setItem('token', token)
  ↓
모든 API 요청
  ↓
axiosInstance.js:18-26 (요청 인터셉터 - 토큰 자동 첨부)
  ↓
config.headers.Authorization = `Bearer ${token}`
```

### 백엔드 코드 추적

#### 1. 관심기업 목록 조회 플로우
```
GET /api/favorites
  ↓
FavoritesController.java:58-79 (getFavorites 메서드)
  ↓
getUserCodeFromAuthentication (JWT에서 userCode 추출)
  ↓
FavoritesService.java:54-66 (getFavorites 메서드)
  ↓
StockRepository.java:35-40 (findByUserCodeWithMemberAndCompany)
  ↓
JPQL 실행 (fetch join으로 N+1 방지)
  ↓
convertToFavoriteResponse (DTO 변환)
  ↓
ResponseEntity.ok(favorites)
```

#### 2. 관심기업 등록 플로우
```
POST /api/favorites
  ↓
FavoritesController.java:90-122 (addFavorite 메서드)
  ↓
@Valid AddFavoriteRequest (DTO 검증)
  ↓
FavoritesService.java:80-128 (addFavorite 메서드)
  ↓
stockRepository.findByUserCodeAndCorpCode (중복 체크)
  ↓
getOrCreateCompany (Company 엔티티 조회/생성)
  ├─ companyRepository.findByCorpCode (DB 조회)
  └─ companyService.getCompanyByCorpCode (DART API 호출)
  ↓
isStockCodeMatching (종목코드 검증)
  ↓
Stock.builder() (엔티티 생성)
  ↓
stockRepository.save (DB 저장)
  ↓
convertToFavoriteResponse (DTO 변환)
```

#### 3. 관심기업 삭제 플로우
```
DELETE /api/favorites/{stockCode}
  ↓
FavoritesController.java:132-162 (deleteFavorite 메서드)
  ↓
FavoritesService.java:203-217 (deleteFavorite 메서드)
  ↓
StockRepository.java:107-113 (deleteByUserCodeAndStockCode)
  ↓
JPQL DELETE 실행
  ↓
deletedCount 반환 (0 or 1)
  ↓
DeleteFavoriteResponse (DTO 생성)
```

#### 4. JWT 인증 플로우
```
HTTP 요청 (Authorization: Bearer {token})
  ↓
Spring Security 필터 체인
  ↓
JwtAuthenticationFilter (토큰 검증)
  ↓
SecurityContextHolder.setContext (인증 정보 설정)
  ↓
FavoritesController 진입
  ↓
getUserCodeFromAuthentication (userCode 추출)
  ↓
SecurityContextHolder.getContext().getAuthentication()
  ↓
UserDetails.getUsername() → userCode
```

### 주요 파일 위치 요약

#### 프론트엔드
| 파일명 | 경로 | 설명 |
|--------|------|------|
| FavoritesPage.jsx | `frontend/src/pages/FavoritesPage.jsx` | 관심기업 페이지 |
| FavoriteTable.jsx | `frontend/src/components/FavoriteTable.jsx` | 관심기업 테이블 컴포넌트 |
| favoritesService.js | `frontend/src/services/favoritesService.js` | 관심기업 API 서비스 |
| axiosInstance.js | `frontend/src/services/axiosInstance.js` | Axios 인터셉터 (JWT) |
| authService.js | `frontend/src/services/authService.js` | 인증 서비스 (토큰 관리) |
| useAuth.js | `frontend/src/hooks/useAuth.js` | 인증 커스텀 훅 |

#### 백엔드
| 파일명 | 경로 | 설명 |
|--------|------|------|
| FavoritesController.java | `backend/src/main/java/com/project/companyanalyzer/controller/FavoritesController.java` | REST API 컨트롤러 |
| FavoritesService.java | `backend/src/main/java/com/project/companyanalyzer/service/FavoritesService.java` | 비즈니스 로직 |
| StockRepository.java | `backend/src/main/java/com/project/companyanalyzer/repository/StockRepository.java` | 데이터 접근 계층 |
| Stock.java | `backend/src/main/java/com/project/companyanalyzer/entity/Stock.java` | JPA 엔티티 |
| AddFavoriteRequest.java | `backend/src/main/java/com/project/companyanalyzer/dto/AddFavoriteRequest.java` | 등록 요청 DTO |
| FavoriteResponse.java | `backend/src/main/java/com/project/companyanalyzer/dto/FavoriteResponse.java` | 응답 DTO |
| DeleteFavoriteResponse.java | `backend/src/main/java/com/project/companyanalyzer/dto/DeleteFavoriteResponse.java` | 삭제 응답 DTO |

---

## 마치며

이 문서는 SCRUM-9 관심기업 게시판 기능 구현의 전체 프로세스와 기술을 상세하게 기록한 것입니다.

### 구현 완료 항목
- ✅ JWT 기반 인증 시스템
- ✅ 관심기업 CRUD API (조회, 등록, 삭제)
- ✅ 주가 정보 실시간 표시 (금융위원회 API)
- ✅ DART API 연동 (Company 엔티티 자동 생성)
- ✅ N+1 문제 방지 (fetch join)
- ✅ 비상장 기업 지원 (stockCode = null)
- ✅ 중복 등록 방지 (UNIQUE KEY 제약조건)
- ✅ ProtectedRoute를 통한 접근 제어
- ✅ 반응형 UI 디자인

### 코드 추적 팁
- **프론트엔드**: 컴포넌트 함수명과 라인 번호로 검색
- **백엔드**: 메서드 JavaDoc 주석과 라인 번호로 검색
- **전체 플로우**: 위 "코드 추적 가이드" 섹션 참조

### 참고 문서
- [CLAUDE.md](../CLAUDE.md) - 프로젝트 전체 가이드
- [prd.md](../prd.md) - 프로덕트 요구사항 문서
- [joinMembershipFunction.md](./joinMembershipFunction.md) - 회원가입/로그인 기능 문서
- [companyInfoFunction.md](./companyInfoFunction.md) - 기업정보 게시판 기능 문서

---

**작성일**: 2025-11-29
**작성자**: Claude Code (AI 어시스턴트)
**Jira 티켓**: SCRUM-9
**브랜치**: feature/SCRUM-9-interest-companies
