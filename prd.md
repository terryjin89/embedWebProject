# 기업분석 웹페이지 PRD v2.0 - 전체 문서

## 📋 문서 개요

**프로젝트명**: 기업분석 웹페이지  
**버전**: 2.0  
**작성일**: 2025년 11월 11일  
**작성자**: Product Manager

이 문서는 임베디드융합개발자 과정 프로젝트를 위한 완전한 제품 요구사항 명세서(PRD)입니다.

---

## 📁 문서 구성

총 6개의 섹션으로 나누어진 상세 PRD 문서입니다. 순서대로 읽어주세요.

### Part 1: 프로젝트 개요 및 UI/UX 디자인 가이드
**파일**: `01_프로젝트개요_및_디자인가이드.md`

**주요 내용**:
- 프로젝트 목적 및 타겟 사용자
- 기술 스택
- 컬러 팔레트 및 타이포그래피
- 레이아웃 구조 (헤더, 푸터, 메인 페이지)
- 컴포넌트 디자인 스타일
- CSS 변수 정의
- 반응형 브레이크포인트

**참고**: 한국수출입은행, IBK기업은행 웹사이트 디자인을 참조한 전문적인 금융 서비스 UI

---

### Part 2: 인증 시스템 및 게시판 기능
**파일**: `02_인증_및_게시판기능.md`

**주요 내용**:
- 회원가입/로그인 (JWT 인증)
- 경제지표 게시판 (환율 정보 - 테이블형)
- 기업정보 게시판 (DART API 연동 - 테이블형)
- 관심기업 게시판 (테이블형)
- 관심기업 상세페이지 (탭 기반 통합 대시보드)
- UI 목업 및 상세 설계

---

### Part 3: 주가차트 및 뉴스검색 기능
**파일**: `03_주가차트_및_뉴스검색.md`

**주요 내용**:
- ⭐ **금융위원회 주식시세정보 API** 연동
- Recharts를 활용한 주가 차트 (Area Chart)
- 기간 선택 기능 (30일/60일/90일)
- 뉴스 검색 기능 (Naver 검색 API)
- 해시태그 기반 검색
- React 컴포넌트 구현 예시
- Backend API 연동 코드

---

### Part 4: 데이터베이스 및 API 명세
**파일**: `04_데이터베이스_및_API명세.md`

**주요 내용**:
- ERD 및 테이블 설계
  - Member (회원)
  - Company (기업)
  - Stock (관심기업)
  - Memo (메모) ⭐ NEW
- 전체 API 명세서
  - Authentication APIs
  - Economy APIs
  - Company APIs
  - Favorites APIs
  - Stock APIs
  - News APIs
- API 요청/응답 예시
- 에러 코드 정의

---

### Part 5: 프론트엔드 및 백엔드 구현 가이드
**파일**: `05_프론트엔드_백엔드_구현가이드.md`

**주요 내용**:
- React 프로젝트 구조
- Axios 설정 및 인터셉터
- AuthContext 구현
- 공통 컴포넌트 (Table, Button, Card 등)
- Spring Boot 프로젝트 구조
- Spring Security + JWT 설정
- 금융위원회 API 서비스 구현
- JPA Repository 설계
- 예외 처리 (GlobalExceptionHandler)
- Application 설정 (application.yml)

---

### Part 6: 배포, 테스트, 모니터링 및 부록
**파일**: `06_배포_운영_및_부록.md`

**주요 내용**:
- 비기능 요구사항 (성능, 보안, 사용성)
- 개발 일정 (10주)
- 성공 지표
- 위험 요소 및 대응 방안
- 배포 전략 (Vercel, AWS EC2)
- 테스트 전략 (단위/통합 테스트)
- 사용자 매뉴얼
- FAQ
- 향후 개발 계획 (Roadmap)
- 참고 자료
- 부록 (환경 설정, Git 전략, 코드 리뷰)

---

## 🎯 핵심 특징

### ⭐ 신규 추가 사항 (v2.0)

1. **UI/UX 디자인 가이드**
   - 한국수출입은행, IBK기업은행 참조 디자인
   - 전문적인 금융 서비스 컬러 팔레트
   - 상세한 컴포넌트 스타일 가이드

2. **금융위원회 주식시세정보 API 연동**
   - 공공데이터포털 API 활용
   - 30일/60일/90일 주가 차트
   - Recharts Area Chart 구현

3. **테이블형 레이아웃**
   - 경제지표 게시판
   - 기업정보 게시판
   - 관심기업 게시판
   - Sticky header, 호버 효과

4. **탭 기반 통합 대시보드**
   - 관심기업 상세페이지
   - 공시정보, 주가차트, 관련기사, 메모 통합

---

## 🛠 기술 스택

### Frontend
- React 18.x
- JavaScript ES2022
- Node.js 22.x
- Recharts (차트)
- Axios (HTTP 클라이언트)

### Backend
- Spring Boot 3.x
- JDK 17
- Spring Security + JWT
- Lombok
- MyBatis/Maven

### Database
- MySQL 8.0.xx

### External APIs
- DART 전자공시시스템
- 금융위원회 주식시세정보 API ⭐
- 한국수출입은행 환율 API
- Naver 검색 API

---

## 📖 읽는 순서

1. **01_프로젝트개요_및_디자인가이드.md** ← 프로젝트 이해 및 UI 디자인
2. **02_인증_및_게시판기능.md** ← 핵심 기능 이해
3. **03_주가차트_및_뉴스검색.md** ← 금융 데이터 연동
4. **04_데이터베이스_및_API명세.md** ← 데이터 구조 및 API 이해
5. **05_프론트엔드_백엔드_구현가이드.md** ← 실제 코드 구현
6. **06_배포_운영_및_부록.md** ← 배포 및 운영 가이드

---

## 📊 프로젝트 규모

- **총 페이지 수**: 약 150페이지 (6개 문서 합계)
- **API 엔드포인트**: 20개 이상
- **데이터베이스 테이블**: 4개
- **React 컴포넌트**: 30개 이상
- **개발 기간**: 10주

---

## 🚀 빠른 시작 가이드

### 1. 환경 설정
```bash
# 필수 설치
- JDK 17
- Node.js 22.x
- MySQL 8.0

# API 키 발급
- DART API: https://opendart.fss.or.kr/
- 금융위원회 API: https://www.data.go.kr/
- 한국수출입은행 API: https://www.koreaexim.go.kr/
```

### 2. 데이터베이스 생성
```sql
CREATE DATABASE company_analyzer 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

### 3. 백엔드 실행
```bash
cd backend
./mvnw spring-boot:run
```

### 4. 프론트엔드 실행
```bash
cd frontend
npm install
npm start
```

---

## 📞 문의 및 협업

- **프로젝트 관리**: Git Issue 트래커
- **코드 리뷰**: Pull Request 기반
- **커뮤니케이션**: Slack / Discord

---

## 📝 변경 이력

| 버전 | 날짜 | 변경 내용 |
|------|------|-----------|
| 1.0 | 2025-11-11 | 초기 PRD 작성 |
| 2.0 | 2025-11-11 | UI/UX 가이드 추가, 금융위원회 API 연동, 테이블형 레이아웃 |

---

## ✅ 체크리스트

개발 시작 전 확인사항:

- [ ] 모든 PRD 문서 읽기 완료
- [ ] 개발 환경 설정 완료
- [ ] API 키 발급 완료
- [ ] 데이터베이스 생성 완료
- [ ] Git 저장소 생성 완료
- [ ] 팀원과 역할 분담 완료

---

## 🎓 학습 자료

- Spring Boot: https://spring.io/guides
- React: https://react.dev/learn
- Recharts: https://recharts.org/en-US/examples
- MySQL: https://dev.mysql.com/doc/

---

**📌 이 PRD는 프로젝트의 성공적인 구현을 위한 완전한 가이드입니다.**

모든 섹션을 순서대로 읽고, 개발 과정에서 참고하세요!

**작성자**: Product Manager  
**버전**: 2.0  
**마지막 업데이트**: 2025년 11월 11일

# 기업분석 웹페이지 PRD v2.0 - Part 1

## 프로젝트 개요 및 UI/UX 디자인 가이드

**작성일**: 2025년 11월 11일  
**버전**: 2.0  
**작성자**: Product Manager

---

## 1. 프로젝트 개요

### 1.1 프로젝트 목적
임베디드융합개발자 과정에서 배운 내용을 실전 웹 프로젝트로 구현하여, 기업 관련 정보를 체계적으로 관리하고 분석할 수 있는 플랫폼 제공

### 1.2 타겟 사용자
- **Primary**: 기업 관련 정보를 관리하는 사업가
- **User Persona**: 투자 결정을 위해 기업 재무정보, 공시정보, 주가 동향을 통합적으로 모니터링하고자 하는 전문가

### 1.3 기술 스택
- **Frontend**: React 18.x, JavaScript ES2022, Node.js 22.x
- **Backend**: Spring Boot 3.x, JDK 17
- **Database**: MySQL 8.0.xx
- **Libraries**: Axios, Lombok, Recharts
- **Tools**: Postman, Git
- **External APIs**: 
  - 공공데이터포털 (https://www.data.go.kr)
  - DART 전자공시시스템 (https://opendart.fss.or.kr)
  - 한국수출입은행 환율 API
  - **금융위원회 주식시세정보 API** ⭐ NEW

---

## 2. UI/UX 디자인 가이드 ⭐ NEW

### 2.1 디자인 컨셉
참조 이미지(한국수출입은행, IBK기업은행)를 기반으로 한 **신뢰감 있고 전문적인 금융 서비스 디자인**

### 2.2 컬러 팔레트

```css
Primary Color: #0066CC (신뢰감 있는 블루)
Secondary Color: #00A3E0 (밝은 블루)
Accent Color: #FF6B35 (포인트 오렌지)
Background: #F5F7FA (밝은 그레이)
Text Primary: #2C3E50
Text Secondary: #7F8C8D
Success: #27AE60
Warning: #F39C12
Danger: #E74C3C
```

### 2.3 레이아웃 구조

#### 헤더 (Header)
```
┌─────────────────────────────────────────────────────────┐
│ [로고]  온행소식  업무안내  금융상품  정보공개  지속가능경영  고객센터 │
│                                      [검색] [EN] [로그인] │
└─────────────────────────────────────────────────────────┘
```

**구성 요소**:
- **로고**: 좌측 상단 배치 (클릭 시 메인페이지)
- **GNB 메뉴**: 중앙 정렬
  - 경제지표
  - 기업정보
  - 관심기업 (로그인 필수)
  - 주가차트
  - 기사검색
- **우측 유틸리티**:
  - 검색창 (아이콘 클릭 시 확장)
  - 언어 선택 (한국어/English)
  - 로그인/마이페이지

#### 메인 페이지 히어로 섹션
```
┌─────────────────────────────────────────────────────────┐
│                                                         │
│         We Export                                       │
│         Sustainable Growth                              │
│                                                         │
│         [부제목: 기업 분석의 새로운 기준]                     │
│                                                         │
│         [바로가기 버튼들]                                   │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

**디자인 특징**:
- **배경**: 그라디언트 또는 금융 관련 이미지 (예: 차트, 그래프)
- **타이포그래피**: 큰 굵은 폰트로 임팩트
- **CTA 버튼**: 
  - "기업정보 조회하기" (Primary)
  - "관심기업 관리" (Secondary)

#### 메인 페이지 콘텐츠 섹션
```
┌─────────────────────────────────────────────────────────┐
│  [카드 1: 실시간 환율]  [카드 2: 인기 기업]  [카드 3: 최신 공시] │
│                                                         │
│  주요 서비스                                              │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐                │
│  │ 경제지표  │  │ 기업정보  │  │ 주가분석  │                │
│  │ [이미지] │  │ [이미지] │  │ [이미지] │                │
│  │ [설명]   │  │ [설명]   │  │ [설명]   │                │
│  └─────────┘  └─────────┘  └─────────┘                │
└─────────────────────────────────────────────────────────┘
```

**카드 디자인**: 
- 흰 배경, 그림자 효과 (box-shadow: 0 2px 8px rgba(0,0,0,0.1))
- 호버 시 약간 상승 효과 (transform: translateY(-4px))
- 아이콘 + 제목 + 설명 + 링크 버튼

#### 푸터 (Footer)
```
┌─────────────────────────────────────────────────────────┐
│  회사소개  |  이용약관  |  개인정보처리방침  |  고객센터        │
│                                                         │
│  (주)기업분석플랫폼  |  사업자등록번호: XXX-XX-XXXXX        │
│  주소: 서울특별시...  |  고객센터: 1588-XXXX               │
│                                                         │
│  © 2025 Company Analyzer. All rights reserved.         │
│                                                         │
│  [GitHub] [Twitter] [LinkedIn]                         │
└─────────────────────────────────────────────────────────┘
```

### 2.4 컴포넌트 디자인 스타일

#### 버튼
```css
/* Primary Button */
background: #0066CC;
color: white;
border-radius: 8px;
padding: 12px 24px;
font-weight: 600;
transition: all 0.3s;

/* Hover */
background: #0052A3;
box-shadow: 0 4px 12px rgba(0, 102, 204, 0.3);

/* Secondary Button */
background: white;
color: #0066CC;
border: 2px solid #0066CC;
```

#### 입력 필드
```css
border: 1px solid #E0E6ED;
border-radius: 8px;
padding: 12px 16px;
font-size: 14px;

/* Focus */
border-color: #0066CC;
box-shadow: 0 0 0 3px rgba(0, 102, 204, 0.1);
```

#### 카드
```css
background: white;
border-radius: 12px;
padding: 24px;
box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
transition: all 0.3s;

/* Hover */
box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
transform: translateY(-4px);
```

### 2.5 타이포그래피
```
Heading 1: 32px, Bold, #2C3E50
Heading 2: 24px, SemiBold, #2C3E50
Heading 3: 20px, SemiBold, #2C3E50
Body: 16px, Regular, #2C3E50
Caption: 14px, Regular, #7F8C8D
Small: 12px, Regular, #7F8C8D

Font Family: 'Noto Sans KR', 'Roboto', sans-serif
```

### 2.6 간격 시스템 (Spacing)
```
4px, 8px, 12px, 16px, 24px, 32px, 48px, 64px
```

### 2.7 반응형 브레이크포인트
```
Mobile: < 768px
Tablet: 768px - 1024px
Desktop: > 1024px
```

### 2.8 CSS 변수 정의

**variables.css**:
```css
:root {
  /* Colors */
  --primary-color: #0066CC;
  --primary-hover: #0052A3;
  --secondary-color: #00A3E0;
  --accent-color: #FF6B35;
  
  --background-primary: #FFFFFF;
  --background-secondary: #F5F7FA;
  --background-tertiary: #FAFBFC;
  
  --text-primary: #2C3E50;
  --text-secondary: #7F8C8D;
  --text-tertiary: #BDC3C7;
  
  --border-color: #E0E6ED;
  --border-hover: #D0D7DE;
  
  --success-color: #27AE60;
  --warning-color: #F39C12;
  --danger-color: #E74C3C;
  --info-color: #3498DB;
  
  /* Typography */
  --font-family: 'Noto Sans KR', 'Roboto', sans-serif;
  --font-size-base: 16px;
  --font-size-small: 14px;
  --font-size-large: 18px;
  --font-size-h1: 32px;
  --font-size-h2: 24px;
  --font-size-h3: 20px;
  
  /* Spacing */
  --spacing-xs: 4px;
  --spacing-sm: 8px;
  --spacing-md: 16px;
  --spacing-lg: 24px;
  --spacing-xl: 32px;
  --spacing-xxl: 48px;
  
  /* Border Radius */
  --radius-sm: 4px;
  --radius-md: 8px;
  --radius-lg: 12px;
  --radius-xl: 16px;
  
  /* Shadows */
  --shadow-sm: 0 2px 4px rgba(0, 0, 0, 0.05);
  --shadow-md: 0 2px 8px rgba(0, 0, 0, 0.08);
  --shadow-lg: 0 4px 16px rgba(0, 0, 0, 0.12);
  
  /* Transitions */
  --transition-fast: 0.15s ease;
  --transition-base: 0.3s ease;
  --transition-slow: 0.5s ease;
}
```

---

# 기업분석 웹페이지 PRD v2.0 - Part 2

## 인증 시스템 및 게시판 기능

**작성일**: 2025년 11월 11일  
**버전**: 2.0  

---

## 3. 핵심 기능 명세

### 3.1 인증 시스템 (로그인/회원가입)

#### 3.1.1 회원가입

**Endpoint**: `POST /api/auth/signup`

**Request Body**:
```json
{
  "id": "string",
  "password": "string",
  "email": "string",
  "phNum": "string",
  "userCode": "string (auto-generated)"
}
```

**Validation**:
- ID: 4-20자, 영문/숫자 조합
- Password: 8자 이상, 영문/숫자/특수문자 포함
- Email: 이메일 형식 검증
- Phone: 010-XXXX-XXXX 형식

**Response**:
```json
{
  "success": true,
  "message": "회원가입 성공",
  "userCode": "string"
}
```

#### 3.1.2 로그인

**Endpoint**: `POST /api/auth/login`

**Request Body**:
```json
{
  "id": "string",
  "password": "string"
}
```

**Response**:
```json
{
  "success": true,
  "token": "JWT_TOKEN",
  "userCode": "string",
  "userName": "string"
}
```

**UI 디자인**:
- **레이아웃**: 중앙 정렬 카드형 (최대 너비 400px)
- **배경**: 그라디언트 배경 이미지
- **카드 스타일**: 
  - 흰 배경, 둥근 모서리 (border-radius: 16px)
  - 부드러운 그림자 효과
- **입력 필드**: 
  - 아이콘 포함 (사용자 아이콘, 자물쇠 아이콘)
  - 플레이스홀더: "아이디를 입력하세요", "비밀번호를 입력하세요"
- **버튼**: 
  - 로그인 버튼 (Primary, 전체 너비)
  - 회원가입 버튼 (Secondary, 전체 너비)
- **추가 링크**: "아이디/비밀번호 찾기"

**인증 방식**: JWT 토큰 기반 인증
- Access Token: 2시간 유효
- 로그인 성공 시 토큰을 localStorage에 저장
- 이후 모든 API 요청 시 Header에 토큰 포함: `Authorization: Bearer {token}`

**접근 제어**:
- 로그인 전: 메인페이지, 로그인페이지만 접근 가능
- 로그인 후: 모든 페이지 접근 가능

---

### 3.2 게시판 기능

#### 3.2.1 경제지표 게시판

**Page**: `/economy`

**Endpoint**: `GET /api/economy/exchange-rates`

**기능**:
- 한국수출입은행 환율 API 호출
- 주요 10개국 통화 환율 정보 표시 (USD, JPY, CNY, EUR, GBP 등)
- 전일 대비 변동률 표시

**Response**:
```json
{
  "data": [
    {
      "currency": "USD",
      "currencyName": "미국 달러",
      "exchangeRate": 1320.50,
      "changeRate": 0.5,
      "changeAmount": 6.50,
      "baseDate": "2025-11-11"
    }
  ]
}
```

**UI 구성**: ⭐ 테이블형 레이아웃
```
┌────────────────────────────────────────────────────────────┐
│ 경제지표 - 환율 정보                         [새로고침] [설정] │
├────────────────────────────────────────────────────────────┤
│ 통화코드 │ 통화명    │ 환율      │ 전일대비      │ 기준일    │
├────────────────────────────────────────────────────────────┤
│ USD     │ 미국 달러 │ 1,320.50 │ ▲6.50(0.5%)  │ 2025-11-11│
│ JPY     │ 일본 엔   │ 880.25   │ ▼2.15(-0.24%)│ 2025-11-11│
│ CNY     │ 중국 위안 │ 185.30   │ ▲0.80(0.43%) │ 2025-11-11│
└────────────────────────────────────────────────────────────┘
```

**테이블 스타일**:
- **헤더**: 
  - 배경색: #F5F7FA
  - 텍스트: Bold, #2C3E50
  - Sticky header (스크롤 시 고정)
- **행**:
  - 짝수 행: 흰 배경
  - 홀수 행: #FAFBFC 배경
  - 호버 시: #F0F4F8 배경
  - 클릭 시 상세페이지로 이동
- **전일대비 표시**:
  - 상승: 빨간색 (#E74C3C), ▲ 아이콘
  - 하락: 파란색 (#3498DB), ▼ 아이콘
  - 변동없음: 회색 (#95A5A6), - 표시

#### 3.2.2 경제지표 상세페이지

**Page**: `/economy/:currencyCode`

**Endpoint**: `GET /api/economy/exchange-rates/:currencyCode/detail`

**UI 구성**:
```
┌────────────────────────────────────────────────────────────┐
│ [뒤로가기] 미국 달러 (USD)                                  │
├────────────────────────────────────────────────────────────┤
│ 현재 환율                                                   │
│ ┌────────────────────────────────────────────────────────┐ │
│ │ 1,320.50원        ▲ 6.50 (+0.5%)                       │ │
│ │ 기준일: 2025-11-11                                      │ │
│ └────────────────────────────────────────────────────────┘ │
├────────────────────────────────────────────────────────────┤
│ [7일] [30일] [90일] [1년]                                   │
│                                                            │
│             [Recharts 차트 영역]                            │
│                                                            │
├────────────────────────────────────────────────────────────┤
│ 상세 정보                                                   │
│ 최고가: 1,325.00 │ 최저가: 1,315.00 │ 평균가: 1,320.25    │
└────────────────────────────────────────────────────────────┘
```

#### 3.2.3 기업정보 게시판 ⭐ 테이블형

**Page**: `/companies`

**Endpoint**: `GET /api/companies`

**Query Parameters**:
- `page`: 페이지 번호 (default: 1)
- `size`: 페이지 당 항목 수 (default: 20)
- `keyword`: 검색 키워드 (기업명)
- `industryCode`: 업종 코드 (필터링)

**Response**:
```json
{
  "content": [
    {
      "compCode": "00126380",
      "compName": "삼성전자",
      "compPthkm": "상장",
      "compAddr": "경기도 수원시...",
      "establishDate": "1969-01-13",
      "industryCode": "26",
      "stockCode": "005930"
    }
  ],
  "totalPages": 100,
  "totalElements": 2000,
  "currentPage": 1
}
```

**UI 구성**: 테이블형 레이아웃
```
┌────────────────────────────────────────────────────────────┐
│ 기업정보                        [검색창: 기업명 검색] [업종▼] │
├────────────────────────────────────────────────────────────┤
│ 기업명    │ 종목코드 │ 업종    │ 설립일     │ 상장여부 │ 관리  │
├────────────────────────────────────────────────────────────┤
│ 삼성전자  │ 005930  │ 전자    │ 1969-01-13 │ 상장    │ [⭐] │
│ SK하이닉스│ 000660  │ 전자    │ 1996-10-04 │ 상장    │ [☆] │
│ LG전자    │ 066570  │ 전자    │ 1958-01-05 │ 상장    │ [☆] │
├────────────────────────────────────────────────────────────┤
│                    [1] 2 3 4 5 ... 100                     │
└────────────────────────────────────────────────────────────┘
```

**테이블 스타일**:
- 헤더: 고정 (sticky header), 배경 #F5F7FA
- 행 높이: 48px
- 텍스트 정렬: 기업명(좌측), 숫자(우측), 나머지(중앙)
- 호버 효과: 배경색 변경

**관심기업 등록 버튼**:
- 아이콘: ⭐ (비활성) → ⭐ (활성, 노란색)
- 클릭 시 즉시 등록/해제

#### 3.2.4 기업정보 상세페이지

**Page**: `/companies/:compCode`

**Endpoint**: `GET /api/companies/:compCode/disclosures`

**UI 구성**:
```
┌────────────────────────────────────────────────────────────┐
│ [뒤로가기] 삼성전자 (005930)                     [관심기업 등록] │
├────────────────────────────────────────────────────────────┤
│ 기업 기본정보                                               │
│ ┌────────────────────────────────────────────────────────┐ │
│ │ 📊 기업명: 삼성전자주식회사                              │ │
│ │ 👤 대표자: 한종희                                       │ │
│ │ 📍 주소: 경기도 수원시 영통구 삼성로 129                 │ │
│ │ 📅 설립일: 1969년 1월 13일                              │ │
│ │ 🏭 업종: 전자부품 제조업                                │ │
│ └────────────────────────────────────────────────────────┘ │
├────────────────────────────────────────────────────────────┤
│ 최근 1년 공시목록                           [필터: 전체 ▼]    │
│ ┌────────────────────────────────────────────────────────┐ │
│ │ 공시명             │ 접수일자    │ 비고          │ 보기 │ │
│ ├────────────────────────────────────────────────────────┤ │
│ │ 분기보고서(2025.09)│ 2025-11-10 │ 정기공시       │ 📄  │ │
│ │ 주주총회소집공고    │ 2025-10-20 │ 주요사항보고    │ 📄  │ │
│ └────────────────────────────────────────────────────────┘ │
│                         [1] 2 3 4 5                        │
└────────────────────────────────────────────────────────────┘
```

#### 3.2.5 관심기업 게시판 ⭐ 테이블형

**Page**: `/favorites`

**Endpoint**: `GET /api/favorites`

**Header**: `Authorization: Bearer {token}` (로그인 필수)

**Response**:
```json
{
  "favorites": [
    {
      "stockCode": "005930",
      "compName": "삼성전자",
      "prePrice": 70000,
      "nowPrice": 71000,
      "changeRate": 1.43,
      "changeAmount": 1000,
      "preHighPrice": 72000,
      "preLowPrice": 69000,
      "addedDate": "2025-11-01"
    }
  ]
}
```

**UI 구성**: 테이블형 레이아웃
```
┌────────────────────────────────────────────────────────────┐
│ 관심기업 (5)                                 [새로고침] [정렬▼]│
├────────────────────────────────────────────────────────────┤
│ 기업명   │ 현재가     │ 전일대비        │ 등록일     │ 관리   │
├────────────────────────────────────────────────────────────┤
│ 삼성전자 │ 71,000원   │ ▲1,000(1.43%)  │ 2025-11-01 │[보기][삭제]│
│ SK하이닉스│ 120,500원 │ ▼500(-0.41%)   │ 2025-11-02 │[보기][삭제]│
└────────────────────────────────────────────────────────────┘
```

**관심기업 등록/삭제**:
- **등록**: `POST /api/favorites`
- **삭제**: `DELETE /api/favorites/:stockCode`

#### 3.2.6 관심기업 상세페이지 (통합 대시보드)

**Page**: `/favorites/:stockCode`

**UI 구성**: 탭 기반 레이아웃
```
┌────────────────────────────────────────────────────────────┐
│ [뒤로가기] 삼성전자 (005930)                               │
├────────────────────────────────────────────────────────────┤
│ 현재가: 71,000원  ▲1,000 (+1.43%)                          │
├────────────────────────────────────────────────────────────┤
│ [탭] 📋 공시정보 | 📈 주가차트 | 📰 관련기사 | 📝 메모      │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  [선택된 탭의 콘텐츠]                                        │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

**탭 1: 공시정보**
- 최근 1년 공시목록 (테이블형)

**탭 2: 주가차트**
- Recharts 차트
- 기간 선택: 30일/60일/90일

**탭 3: 관련기사**
- 카드형 리스트
- 썸네일 + 제목 + 출처 + 날짜

**탭 4: 메모**
- 텍스트 에디터
- 글자 수: XXX / 2000
- [저장] 버튼

**API Endpoints**:
- 공시정보: `GET /api/companies/:compCode/disclosures`
- 주가차트: `GET /api/stocks/:stockCode/chart`
- 관련기사: `GET /api/news/search?company={기업명}&hashtag=기사`
- 메모: `GET/POST /api/favorites/:stockCode/memo`

---

# 기업분석 웹페이지 PRD v2.0 - Part 3

## 주가차트 및 뉴스검색 기능

**작성일**: 2025년 11월 11일  
**버전**: 2.0  

---

## 3.3 주식차트 기능 ⭐ 금융위원회 API 사용

### 3.3.1 기업주가 페이지

**Page**: `/stocks/:stockCode`

**Endpoint**: `GET /api/stocks/:stockCode/chart`

**Query Parameters**:
- `period`: 30 | 60 | 90 (일 단위)

**데이터 소스**: ⭐ **금융위원회 주식시세정보 API**

**금융위원회 API 정보**:
- **Base URL**: `http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService`
- **엔드포인트**: `/getStockPriceInfo`
- **필수 파라미터**:
  - `serviceKey`: API 인증키
  - `numOfRows`: 조회 건수
  - `pageNo`: 페이지 번호
  - `resultType`: json
  - `beginBasDt`: 시작 기준일자 (YYYYMMDD)
  - `endBasDt`: 종료 기준일자 (YYYYMMDD)
  - `itmsNm`: 종목명

**API 응답 구조**:
```json
{
  "response": {
    "header": {
      "resultCode": "00",
      "resultMsg": "NORMAL SERVICE."
    },
    "body": {
      "numOfRows": 30,
      "pageNo": 1,
      "totalCount": 30,
      "items": {
        "item": [
          {
            "basDt": "20251111",
            "srtnCd": "005930",
            "isinCd": "KR7005930003",
            "itmsNm": "삼성전자",
            "mrktCtg": "KOSPI",
            "clpr": "71000",
            "vs": "1000",
            "fltRt": "1.43",
            "mkp": "70500",
            "hipr": "71500",
            "lopr": "70000",
            "trqu": "15000000",
            "trPrc": "1050000000000"
          }
        ]
      }
    }
  }
}
```

**금융위원회 API 응답 필드**:
- `basDt`: 기준일자
- `clpr`: 종가
- `vs`: 대비
- `fltRt`: 등락률
- `mkp`: 시가
- `hipr`: 고가
- `lopr`: 저가
- `trqu`: 거래량
- `trPrc`: 거래대금

**Backend Response**:
```json
{
  "stockCode": "005930",
  "compName": "삼성전자",
  "period": 30,
  "currentPrice": 71000,
  "changeAmount": 1000,
  "changeRate": 1.43,
  "chartData": [
    {
      "date": "2025-10-12",
      "basDt": "20251012",
      "clpr": 71000,
      "vs": 1000,
      "fltRt": 1.43,
      "mkp": 70500,
      "hipr": 71500,
      "lopr": 70000,
      "trqu": 15000000,
      "trPrc": 1050000000000
    }
  ]
}
```

**UI 구성**:
```
┌────────────────────────────────────────────────────────────┐
│ 삼성전자 (005930)                                           │
│ 71,000원  ▲1,000 (+1.43%)                                  │
├────────────────────────────────────────────────────────────┤
│ [30일] [60일] [90일]                                        │
├────────────────────────────────────────────────────────────┤
│                                                            │
│              [Recharts 차트 영역]                           │
│                                                            │
│              (Area Chart - 파란색 그라디언트)                │
│                                                            │
├────────────────────────────────────────────────────────────┤
│ 주가 상세 정보                                              │
│ ┌─────────────┬─────────────┬─────────────┬─────────────┐ │
│ │ 시가        │ 고가        │ 저가        │ 거래량      │ │
│ │ 70,500원    │ 71,500원    │ 70,000원    │ 15,000,000 │ │
│ └─────────────┴─────────────┴─────────────┴─────────────┘ │
└────────────────────────────────────────────────────────────┘
```

**Recharts 구현 예시**:
```javascript
import { 
  AreaChart, Area,
  XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer 
} from 'recharts';

const StockAreaChart = ({ data, period }) => {
  const formattedData = data.map(item => ({
    ...item,
    date: new Date(item.basDt).toLocaleDateString('ko-KR', { 
      month: 'short', 
      day: 'numeric' 
    }),
    close: parseInt(item.clpr)
  }));

  const CustomTooltip = ({ active, payload }) => {
    if (active && payload && payload.length) {
      const data = payload[0].payload;
      return (
        <div className="custom-tooltip">
          <p className="tooltip-date">{data.basDt}</p>
          <p className="tooltip-price">
            종가: {data.clpr.toLocaleString()}원
          </p>
          <p className="tooltip-change" 
             style={{ color: data.vs > 0 ? '#E74C3C' : '#3498DB' }}>
            전일대비: {data.vs > 0 ? '▲' : '▼'} 
            {Math.abs(data.vs).toLocaleString()}원 ({data.fltRt}%)
          </p>
          <p className="tooltip-volume">
            거래량: {parseInt(data.trqu).toLocaleString()}주
          </p>
        </div>
      );
    }
    return null;
  };

  return (
    <ResponsiveContainer width="100%" height={400}>
      <AreaChart data={formattedData} 
                 margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
        <defs>
          <linearGradient id="colorPrice" x1="0" y1="0" x2="0" y2="1">
            <stop offset="5%" stopColor="#0066CC" stopOpacity={0.8}/>
            <stop offset="95%" stopColor="#0066CC" stopOpacity={0.1}/>
          </linearGradient>
        </defs>
        <CartesianGrid strokeDasharray="3 3" stroke="#E0E6ED" />
        <XAxis 
          dataKey="date" 
          tick={{ fill: '#7F8C8D', fontSize: 12 }}
          axisLine={{ stroke: '#E0E6ED' }}
        />
        <YAxis 
          tick={{ fill: '#7F8C8D', fontSize: 12 }}
          tickFormatter={(value) => `${(value / 1000).toFixed(0)}K`}
          axisLine={{ stroke: '#E0E6ED' }}
        />
        <Tooltip content={<CustomTooltip />} />
        <Area 
          type="monotone" 
          dataKey="close" 
          stroke="#0066CC" 
          strokeWidth={2}
          fillOpacity={1} 
          fill="url(#colorPrice)" 
          name="종가"
          animationDuration={1000}
        />
      </AreaChart>
    </ResponsiveContainer>
  );
};
```

**백엔드 API 연동 예시**:
```java
@Service
public class StockService {
    
    @Value("${finance.api.key}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    
    public StockChartDto getStockChart(String stockCode, int period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(period);
        
        // 금융위원회 API 호출
        String url = String.format(
            "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo?serviceKey=%s&numOfRows=%d&pageNo=1&resultType=json&beginBasDt=%s&endBasDt=%s&itmsNm=%s",
            apiKey, 
            period, 
            startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
            endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
            getCompanyName(stockCode)
        );
        
        // API 응답 파싱 및 DTO 변환
        FinanceApiResponse response = restTemplate.getForObject(
            url, 
            FinanceApiResponse.class
        );
        
        return convertToStockChartDto(response, stockCode, period);
    }
    
    private String getCompanyName(String stockCode) {
        // Company 테이블에서 종목코드로 기업명 조회
        return companyRepository.findByStockCode(stockCode)
            .map(Company::getCompName)
            .orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));
    }
}
```

**주의사항**:
- 트래픽 제한: 일반 인증키 하루 1,000건
- 응답 시간: 평균 1-2초
- 주말/공휴일 데이터 없음 → 안내 메시지 표시
- 종목코드는 6자리 표준코드 사용

---

## 3.4 기업 기사 조회 기능

### 3.4.1 뉴스 검색 페이지

**Page**: `/news`

**Endpoint**: `GET /api/news/search`

**Query Parameters**:
- `company`: 기업명
- `hashtag`: 검색 키워드 (예: "기사", "실적", "투자")

**기능**:
- 해시태그를 활용한 뉴스 검색
- 검색어: `{기업명} #{해시태그}` (예: "삼성전자 #기사")

**구현 옵션**:
1. **Naver 검색 API** (추천)
   - Base URL: `https://openapi.naver.com/v1/search/news.json`
   - 장점: 무료, 한국 뉴스에 강함
   - 단점: 하루 25,000건 제한

2. Google Custom Search API
3. 직접 크롤링 (저작권 검토 필요)

**Response**:
```json
{
  "articles": [
    {
      "title": "삼성전자, 3분기 실적 발표",
      "source": "한국경제",
      "link": "https://...",
      "publishedDate": "2025-11-10",
      "snippet": "삼성전자가 3분기 실적을 발표했다...",
      "thumbnail": "https://..."
    }
  ]
}
```

**UI 구성**:
```
┌────────────────────────────────────────────────────────────┐
│ 기업 뉴스 검색                                              │
│ ┌────────────────────────────────────────────────────────┐ │
│ │ [기업명 입력: 삼성전자]                       [🔍 검색]  │ │
│ └────────────────────────────────────────────────────────┘ │
│ 해시태그: [#기사] [#실적] [#투자] [#신제품] [#인사]          │
├────────────────────────────────────────────────────────────┤
│ 검색 결과 (25건)                               [정렬: 최신순▼]│
│ ┌────────────────────────────────────────────────────────┐ │
│ │ [썸네일]  삼성전자, 3분기 실적 발표                      │ │
│ │           한국경제 | 2025-11-10                          │ │
│ │           삼성전자가 3분기 실적을 발표했다. 매출...       │ │
│ └────────────────────────────────────────────────────────┘ │
│ ┌────────────────────────────────────────────────────────┐ │
│ │ [썸네일]  SK하이닉스, AI 메모리 공급 확대                 │ │
│ │           전자신문 | 2025-11-09                          │ │
│ │           SK하이닉스가 AI용 고성능 메모리...             │ │
│ └────────────────────────────────────────────────────────┘ │
│                         [더보기]                            │
└────────────────────────────────────────────────────────────┘
```

**검색창**:
- 플레이스홀더: "기업명을 입력하세요"
- 자동완성 기능 (선택사항)
- 검색 버튼 또는 Enter 키

**해시태그 버튼**:
- 클릭 시 해당 해시태그로 자동 검색
- 선택된 버튼: Primary 색상 (#0066CC)
- 미선택 버튼: Secondary 색상

**검색 결과 카드**:
- **레이아웃**: 썸네일 (좌측, 120x120px) + 내용 (우측)
- **제목**: Bold, 18px, #2C3E50
- **출처 + 날짜**: 14px, #7F8C8D
- **요약**: 2-3줄, 말줄임표(...), 14px, #2C3E50
- **호버 효과**: 그림자 증가, 약간 상승
- **클릭**: 원문 새창 열기

**Naver 검색 API 연동 예시**:
```java
@Service
public class NewsService {
    
    @Value("${naver.client.id}")
    private String clientId;
    
    @Value("${naver.client.secret}")
    private String clientSecret;
    
    public NewsResponseDto searchNews(String company, String hashtag) {
        String query = company + " " + hashtag;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        
        String url = "https://openapi.naver.com/v1/search/news.json?query=" 
                     + URLEncoder.encode(query, StandardCharsets.UTF_8)
                     + "&display=10&sort=date";
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<NaverNewsResponse> response = restTemplate.exchange(
            url, 
            HttpMethod.GET, 
            entity, 
            NaverNewsResponse.class
        );
        
        return convertToNewsResponseDto(response.getBody());
    }
}
```

**React 컴포넌트 예시**:
```javascript
const NewsSearch = () => {
  const [company, setCompany] = useState('');
  const [hashtag, setHashtag] = useState('기사');
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(false);

  const handleSearch = async () => {
    setLoading(true);
    try {
      const response = await newsService.search(company, hashtag);
      setArticles(response.articles);
    } catch (error) {
      console.error('뉴스 검색 실패:', error);
      toast.error('뉴스 검색에 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="news-search">
      <div className="search-box">
        <input
          type="text"
          placeholder="기업명을 입력하세요"
          value={company}
          onChange={(e) => setCompany(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
        />
        <button onClick={handleSearch}>🔍 검색</button>
      </div>

      <div className="hashtag-buttons">
        {['기사', '실적', '투자', '신제품', '인사'].map(tag => (
          <button
            key={tag}
            className={hashtag === tag ? 'active' : ''}
            onClick={() => {
              setHashtag(tag);
              if (company) handleSearch();
            }}
          >
            #{tag}
          </button>
        ))}
      </div>

      {loading ? (
        <Loading />
      ) : (
        <div className="news-list">
          {articles.map((article, idx) => (
            <NewsCard key={idx} article={article} />
          ))}
        </div>
      )}
    </div>
  );
};

const NewsCard = ({ article }) => {
  return (
    <div 
      className="news-card"
      onClick={() => window.open(article.link, '_blank')}
    >
      <img 
        src={article.thumbnail || '/default-news.png'} 
        alt={article.title}
        className="news-thumbnail"
      />
      <div className="news-content">
        <h3 className="news-title">{article.title}</h3>
        <p className="news-meta">
          {article.source} | {article.publishedDate}
        </p>
        <p className="news-snippet">{article.snippet}</p>
      </div>
    </div>
  );
};
```

**CSS 스타일**:
```css
.news-search {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.search-box {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.search-box input {
  flex: 1;
  padding: 12px 16px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 16px;
}

.search-box button {
  padding: 12px 24px;
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  font-weight: 600;
}

.hashtag-buttons {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
}

.hashtag-buttons button {
  padding: 8px 16px;
  background: white;
  border: 2px solid var(--primary-color);
  color: var(--primary-color);
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.hashtag-buttons button.active {
  background: var(--primary-color);
  color: white;
}

.news-card {
  display: flex;
  gap: 16px;
  padding: 16px;
  background: white;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-md);
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 16px;
}

.news-card:hover {
  box-shadow: var(--shadow-lg);
  transform: translateY(-2px);
}

.news-thumbnail {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: var(--radius-md);
}

.news-content {
  flex: 1;
}

.news-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.news-meta {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 8px;
}

.news-snippet {
  font-size: 14px;
  color: var(--text-primary);
  line-height: 1.6;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
```

---

# 기업분석 웹페이지 PRD v2.0 - Part 4

## 데이터베이스 설계 및 API 명세서

**작성일**: 2025년 11월 11일  
**버전**: 2.0  

---

## 4. 데이터베이스 설계

### 4.1 ERD
제공된 DB 테이블 구조를 따름:
- **Member**: 회원 정보
- **Stock**: 주식 정보 (관심기업 등록 시 저장)
- **Company**: 기업 기본정보
- **Memo**: 메모 정보 (신규 추가)

### 4.2 테이블 명세

#### Member (회원)
```sql
CREATE TABLE Member (
  id VARCHAR(50) PRIMARY KEY COMMENT '사용자 ID',
  password VARCHAR(255) NOT NULL COMMENT '암호화된 비밀번호',
  email VARCHAR(100) NOT NULL UNIQUE COMMENT '이메일',
  phNum VARCHAR(20) COMMENT '전화번호',
  userCode VARCHAR(50) NOT NULL UNIQUE COMMENT '사용자 고유코드',
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
  updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
  INDEX idx_userCode (userCode),
  INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='회원 정보';
```

#### Stock (관심기업-주식 연결)
```sql
CREATE TABLE Stock (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '고유 ID',
  stockCode VARCHAR(20) NOT NULL COMMENT '종목코드',
  prePrice DECIMAL(10, 2) COMMENT '전일 종가',
  nowPrice DECIMAL(10, 2) COMMENT '현재가',
  preHighPrice DECIMAL(10, 2) COMMENT '전일 고가',
  preLowPrice DECIMAL(10, 2) COMMENT '전일 저가',
  userCode VARCHAR(50) NOT NULL COMMENT '사용자 코드',
  addedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  FOREIGN KEY (userCode) REFERENCES Member(userCode) ON DELETE CASCADE,
  FOREIGN KEY (stockCode) REFERENCES Company(stockCode) ON DELETE CASCADE,
  INDEX idx_userCode (userCode),
  INDEX idx_stockCode (stockCode),
  UNIQUE KEY unique_user_stock (userCode, stockCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='관심기업 정보';
```

#### Company (기업)
```sql
CREATE TABLE Company (
  compCode VARCHAR(20) PRIMARY KEY COMMENT '기업 고유코드',
  compName VARCHAR(200) NOT NULL COMMENT '기업명',
  compPthkm VARCHAR(50) COMMENT '상장구분',
  compAddr TEXT COMMENT '주소',
  establishDate DATE COMMENT '설립일',
  industryCode VARCHAR(10) COMMENT '업종코드',
  stockCode VARCHAR(20) UNIQUE COMMENT '종목코드',
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
  updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
  INDEX idx_compName (compName),
  INDEX idx_stockCode (stockCode),
  INDEX idx_industryCode (industryCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='기업 정보';
```

#### Memo (메모) ⭐ NEW
```sql
CREATE TABLE Memo (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '고유 ID',
  userCode VARCHAR(50) NOT NULL COMMENT '사용자 코드',
  stockCode VARCHAR(20) NOT NULL COMMENT '종목코드',
  memo TEXT COMMENT '메모 내용',
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
  updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
  FOREIGN KEY (userCode) REFERENCES Member(userCode) ON DELETE CASCADE,
  FOREIGN KEY (stockCode) REFERENCES Company(stockCode) ON DELETE CASCADE,
  UNIQUE KEY unique_user_stock_memo (userCode, stockCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='관심기업 메모';
```

### 4.3 데이터 소싱 전략

| 테이블 | 데이터 소스 | 저장 방식 |
|--------|------------|----------|
| Member | 회원가입 시 생성 | DB 저장 |
| Company | DART API 초기 데이터 수집 | DB 저장 (주기적 업데이트) |
| Stock | 사용자가 관심기업 등록 시 | DB 저장 |
| Memo | 사용자가 메모 작성 시 | DB 저장 |
| 환율 정보 | 한국수출입은행 API | API 실시간 호출 (DB 저장 안 함) |
| 공시 정보 | DART API | API 실시간 호출 (DB 저장 안 함) |
| 주가 정보 | 금융위원회 API | API 실시간 호출 (DB 저장 안 함) |
| 뉴스 정보 | Naver 검색 API | API 실시간 호출 (DB 저장 안 함) |

### 4.4 초기 데이터 스크립트

```sql
-- Company 테이블 초기 데이터 (예시)
INSERT INTO Company (compCode, compName, compPthkm, compAddr, establishDate, industryCode, stockCode) VALUES
('00126380', '삼성전자', '상장', '경기도 수원시 영통구 삼성로 129', '1969-01-13', '26', '005930'),
('00164779', 'SK하이닉스', '상장', '경기도 이천시 부발읍 아미리 산 136-1', '1996-10-04', '26', '000660'),
('00113570', 'LG전자', '상장', '서울특별시 영등포구 여의대로 128', '1958-01-05', '26', '066570'),
('00126186', 'NAVER', '상장', '경기도 성남시 분당구 정자일로 95', '1999-06-02', '63', '035420'),
('00164930', '카카오', '상장', '제주특별자치도 제주시 첨단로 242', '1995-02-16', '63', '035720');

-- 추가 기업 데이터는 DART API를 통해 동적으로 가져옴
```

---

## 5. API 명세서

### 5.1 Authentication APIs (인증)

#### 5.1.1 회원가입
```
POST /api/auth/signup
Content-Type: application/json

Request Body:
{
  "id": "testuser",
  "password": "Password123!",
  "email": "test@example.com",
  "phNum": "010-1234-5678"
}

Response (201 Created):
{
  "success": true,
  "message": "회원가입 성공",
  "userCode": "USER_20251111_001"
}

Error (400 Bad Request):
{
  "success": false,
  "code": "U002",
  "message": "이미 존재하는 사용자입니다"
}
```

#### 5.1.2 로그인
```
POST /api/auth/login
Content-Type: application/json

Request Body:
{
  "id": "testuser",
  "password": "Password123!"
}

Response (200 OK):
{
  "success": true,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userCode": "USER_20251111_001",
  "userName": "testuser"
}

Error (401 Unauthorized):
{
  "success": false,
  "code": "U003",
  "message": "비밀번호가 일치하지 않습니다"
}
```

#### 5.1.3 로그아웃
```
POST /api/auth/logout
Authorization: Bearer {token}

Response (200 OK):
{
  "success": true,
  "message": "로그아웃 성공"
}
```

#### 5.1.4 토큰 검증
```
GET /api/auth/verify
Authorization: Bearer {token}

Response (200 OK):
{
  "success": true,
  "userCode": "USER_20251111_001",
  "id": "testuser"
}
```

---

### 5.2 Economy APIs (경제지표)

#### 5.2.1 환율 목록 조회
```
GET /api/economy/exchange-rates

Response (200 OK):
{
  "data": [
    {
      "currency": "USD",
      "currencyName": "미국 달러",
      "exchangeRate": 1320.50,
      "changeRate": 0.5,
      "changeAmount": 6.50,
      "baseDate": "2025-11-11"
    },
    {
      "currency": "JPY",
      "currencyName": "일본 엔",
      "exchangeRate": 880.25,
      "changeRate": -0.24,
      "changeAmount": -2.15,
      "baseDate": "2025-11-11"
    }
  ]
}
```

#### 5.2.2 환율 상세 조회
```
GET /api/economy/exchange-rates/{currencyCode}/detail

Response (200 OK):
{
  "currency": "USD",
  "currencyName": "미국 달러",
  "currentRate": 1320.50,
  "changeRate": 0.5,
  "history": [
    {
      "date": "2025-11-11",
      "rate": 1320.50,
      "change": 6.50
    }
  ]
}
```

---

### 5.3 Company APIs (기업정보)

#### 5.3.1 기업 목록 조회
```
GET /api/companies?page=1&size=20&keyword=삼성&industryCode=26

Query Parameters:
- page: 페이지 번호 (default: 1)
- size: 페이지 크기 (default: 20, max: 100)
- keyword: 검색 키워드 (기업명)
- industryCode: 업종 코드

Response (200 OK):
{
  "content": [
    {
      "compCode": "00126380",
      "compName": "삼성전자",
      "compPthkm": "상장",
      "compAddr": "경기도 수원시...",
      "establishDate": "1969-01-13",
      "industryCode": "26",
      "stockCode": "005930"
    }
  ],
  "totalPages": 5,
  "totalElements": 100,
  "currentPage": 1,
  "pageSize": 20
}
```

#### 5.3.2 기업 상세 조회
```
GET /api/companies/{compCode}

Response (200 OK):
{
  "compCode": "00126380",
  "compName": "삼성전자주식회사",
  "stockCode": "005930",
  "compAddr": "경기도 수원시 영통구 삼성로 129",
  "establishDate": "1969-01-13",
  "ceoName": "한종희",
  "industryCode": "26",
  "industryName": "전자부품 제조업"
}
```

#### 5.3.3 기업 공시 목록 조회
```
GET /api/companies/{compCode}/disclosures?page=1&size=10

Response (200 OK):
{
  "companyInfo": {
    "compCode": "00126380",
    "compName": "삼성전자",
    "stockCode": "005930"
  },
  "disclosures": [
    {
      "rceptNo": "20251111000123",
      "reportNm": "분기보고서 (2025.09)",
      "rceptDt": "2025-11-10",
      "rmk": "정기공시",
      "url": "https://dart.fss.or.kr/..."
    }
  ],
  "totalCount": 50
}
```

---

### 5.4 Favorites APIs (관심기업)

#### 5.4.1 관심기업 목록 조회
```
GET /api/favorites
Authorization: Bearer {token}

Response (200 OK):
{
  "favorites": [
    {
      "stockCode": "005930",
      "compName": "삼성전자",
      "prePrice": 70000,
      "nowPrice": 71000,
      "changeRate": 1.43,
      "changeAmount": 1000,
      "preHighPrice": 72000,
      "preLowPrice": 69000,
      "addedDate": "2025-11-01T10:00:00"
    }
  ]
}
```

#### 5.4.2 관심기업 등록
```
POST /api/favorites
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "stockCode": "005930"
}

Response (201 Created):
{
  "success": true,
  "message": "관심기업 등록 성공"
}

Error (409 Conflict):
{
  "success": false,
  "message": "이미 등록된 관심기업입니다"
}
```

#### 5.4.3 관심기업 삭제
```
DELETE /api/favorites/{stockCode}
Authorization: Bearer {token}

Response (200 OK):
{
  "success": true,
  "message": "관심기업 삭제 성공"
}
```

#### 5.4.4 관심기업 상세 조회
```
GET /api/favorites/{stockCode}
Authorization: Bearer {token}

Response (200 OK):
{
  "stockCode": "005930",
  "compName": "삼성전자",
  "compCode": "00126380",
  "currentPrice": 71000,
  "changeAmount": 1000,
  "changeRate": 1.43,
  "addedDate": "2025-11-01T10:00:00"
}
```

#### 5.4.5 메모 조회
```
GET /api/favorites/{stockCode}/memo
Authorization: Bearer {token}

Response (200 OK):
{
  "memo": "2025년 4분기 실적 주목...",
  "updatedAt": "2025-11-11T15:30:00"
}
```

#### 5.4.6 메모 저장/수정
```
POST /api/favorites/{stockCode}/memo
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "memo": "2025년 4분기 실적 주목. AI 반도체 수요 증가 예상."
}

Response (200 OK):
{
  "success": true,
  "message": "메모 저장 성공",
  "updatedAt": "2025-11-11T15:30:00"
}
```

---

### 5.5 Stock APIs (주가)

#### 5.5.1 주가 차트 데이터 조회
```
GET /api/stocks/{stockCode}/chart?period=30

Query Parameters:
- period: 30 | 60 | 90 (일 단위)

Response (200 OK):
{
  "stockCode": "005930",
  "compName": "삼성전자",
  "period": 30,
  "currentPrice": 71000,
  "changeAmount": 1000,
  "changeRate": 1.43,
  "chartData": [
    {
      "date": "2025-10-12",
      "basDt": "20251012",
      "clpr": "71000",
      "vs": "1000",
      "fltRt": "1.43",
      "mkp": "70500",
      "hipr": "71500",
      "lopr": "70000",
      "trqu": "15000000"
    }
  ]
}
```

#### 5.5.2 현재 주가 정보 조회
```
GET /api/stocks/{stockCode}/current

Response (200 OK):
{
  "stockCode": "005930",
  "compName": "삼성전자",
  "currentPrice": 71000,
  "changeAmount": 1000,
  "changeRate": 1.43,
  "highPrice": 71500,
  "lowPrice": 70000,
  "volume": 15000000,
  "date": "2025-11-11"
}
```

---

### 5.6 News APIs (뉴스)

#### 5.6.1 뉴스 검색
```
GET /api/news/search?company=삼성전자&hashtag=기사&page=1&size=10

Query Parameters:
- company: 기업명 (required)
- hashtag: 해시태그 키워드
- page: 페이지 번호
- size: 페이지 크기

Response (200 OK):
{
  "articles": [
    {
      "title": "삼성전자, 3분기 실적 발표",
      "source": "한국경제",
      "link": "https://...",
      "publishedDate": "2025-11-10",
      "snippet": "삼성전자가...",
      "thumbnail": "https://..."
    }
  ],
  "totalCount": 25,
  "currentPage": 1
}
```

---

### 5.7 API 공통 사항

#### 5.7.1 인증 헤더
```
Authorization: Bearer {JWT_TOKEN}
```

#### 5.7.2 공통 에러 응답
```json
{
  "code": "ERROR_CODE",
  "message": "에러 메시지",
  "timestamp": "2025-11-11T10:00:00"
}
```

#### 5.7.3 HTTP 상태 코드
- 200: 성공
- 201: 생성 성공
- 400: 잘못된 요청
- 401: 인증 실패
- 403: 권한 없음
- 404: 리소스 없음
- 409: 충돌 (중복 등록 등)
- 500: 서버 오류

---

# 기업분석 웹페이지 PRD v2.0 - Part 5

## 프론트엔드 및 백엔드 구현 가이드

**작성일**: 2025년 11월 11일  
**버전**: 2.0  

---

## 8. 프론트엔드 구현 가이드

### 8.1 프로젝트 구조

```
company-analyzer-frontend/
├── public/
│   ├── index.html
│   └── favicon.ico
├── src/
│   ├── components/
│   │   ├── common/
│   │   │   ├── Header.jsx
│   │   │   ├── Footer.jsx
│   │   │   ├── Navbar.jsx
│   │   │   ├── Loading.jsx
│   │   │   ├── Button.jsx
│   │   │   ├── Input.jsx
│   │   │   ├── Card.jsx
│   │   │   └── Table.jsx
│   │   ├── auth/
│   │   │   ├── LoginForm.jsx
│   │   │   └── SignupForm.jsx
│   │   ├── company/
│   │   │   ├── CompanyTable.jsx
│   │   │   ├── CompanyRow.jsx
│   │   │   └── DisclosureTable.jsx
│   │   ├── favorites/
│   │   │   ├── FavoriteTable.jsx
│   │   │   ├── FavoriteDetailTabs.jsx
│   │   │   └── MemoEditor.jsx
│   │   ├── stock/
│   │   │   ├── StockAreaChart.jsx
│   │   │   ├── PeriodSelector.jsx
│   │   │   └── StockInfoCard.jsx
│   │   ├── economy/
│   │   │   ├── ExchangeRateTable.jsx
│   │   │   └── RateDetailChart.jsx
│   │   └── news/
│   │       ├── NewsSearch.jsx
│   │       ├── NewsCard.jsx
│   │       └── NewsList.jsx
│   ├── pages/
│   │   ├── MainPage.jsx
│   │   ├── LoginPage.jsx
│   │   ├── SignupPage.jsx
│   │   ├── EconomyPage.jsx
│   │   ├── EconomyDetailPage.jsx
│   │   ├── CompanyPage.jsx
│   │   ├── CompanyDetailPage.jsx
│   │   ├── FavoritesPage.jsx
│   │   ├── FavoriteDetailPage.jsx
│   │   ├── StockPage.jsx
│   │   └── NewsPage.jsx
│   ├── services/
│   │   ├── api.js
│   │   ├── authService.js
│   │   ├── companyService.js
│   │   ├── stockService.js
│   │   ├── economyService.js
│   │   ├── newsService.js
│   │   └── memoService.js
│   ├── contexts/
│   │   ├── AuthContext.jsx
│   │   └── FavoritesContext.jsx
│   ├── utils/
│   │   ├── auth.js
│   │   ├── formatters.js
│   │   └── constants.js
│   ├── styles/
│   │   ├── globals.css
│   │   ├── variables.css
│   │   └── components/
│   ├── hooks/
│   │   ├── useAuth.js
│   │   └── useFetch.js
│   ├── App.jsx
│   └── index.js
├── package.json
└── .env
```

### 8.2 Axios 설정

**src/services/api.js**:
```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

// 요청 인터셉터: 토큰 자동 추가
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터: 인증 오류 처리
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
```

### 8.3 AuthContext 구현

**src/contexts/AuthContext.jsx**:
```javascript
import React, { createContext, useState, useEffect, useContext } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      authService.verify(token)
        .then((userData) => {
          setUser(userData);
          setIsAuthenticated(true);
        })
        .catch(() => {
          localStorage.removeItem('token');
        })
        .finally(() => {
          setLoading(false);
        });
    } else {
      setLoading(false);
    }
  }, []);

  const login = async (id, password) => {
    const response = await authService.login(id, password);
    localStorage.setItem('token', response.token);
    setUser({ id, userCode: response.userCode });
    setIsAuthenticated(true);
    return response;
  };

  const logout = () => {
    localStorage.removeItem('token');
    setUser(null);
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{ 
      user, 
      isAuthenticated, 
      loading, 
      login, 
      logout 
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};
```

### 8.4 공통 컴포넌트

**src/components/common/Table.jsx**:
```javascript
const Table = ({ columns, data, onRowClick }) => {
  return (
    <div className="table-container">
      <table className="table">
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col.key} style={{ textAlign: col.align || 'left' }}>
                {col.label}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((row, idx) => (
            <tr 
              key={idx} 
              onClick={() => onRowClick && onRowClick(row)}
              className="table-row"
            >
              {columns.map((col) => (
                <td key={col.key} style={{ textAlign: col.align || 'left' }}>
                  {col.render ? col.render(row[col.key], row) : row[col.key]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Table;
```

---

## 9. 백엔드 구현 가이드

### 9.1 프로젝트 구조

```
company-analyzer-backend/
├── src/main/java/com/company/analyzer/
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   ├── WebConfig.java
│   │   └── RestTemplateConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── CompanyController.java
│   │   ├── FavoritesController.java
│   │   ├── StockController.java
│   │   ├── EconomyController.java
│   │   ├── NewsController.java
│   │   └── MemoController.java
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── CompanyService.java
│   │   ├── FavoritesService.java
│   │   ├── StockService.java
│   │   ├── EconomyService.java
│   │   ├── NewsService.java
│   │   ├── MemoService.java
│   │   ├── DartApiService.java
│   │   ├── FinanceApiService.java
│   │   └── ExchangeRateService.java
│   ├── repository/
│   │   ├── MemberRepository.java
│   │   ├── CompanyRepository.java
│   │   ├── StockRepository.java
│   │   └── MemoRepository.java
│   ├── entity/
│   │   ├── Member.java
│   │   ├── Company.java
│   │   ├── Stock.java
│   │   └── Memo.java
│   ├── dto/
│   │   ├── request/
│   │   └── response/
│   ├── security/
│   │   ├── JwtTokenProvider.java
│   │   └── JwtAuthenticationFilter.java
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       ├── CustomException.java
│       └── ErrorCode.java
├── src/main/resources/
│   ├── application.yml
│   ├── application-dev.yml
│   └── application-prod.yml
└── pom.xml
```

### 9.2 Spring Security 설정

**SecurityConfig.java**:
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/**", 
                    "/api/companies/**", 
                    "/api/economy/**", 
                    "/api/stocks/**", 
                    "/api/news/**"
                ).permitAll()
                .requestMatchers("/api/favorites/**").authenticated()
                .anyRequest().authenticated()
            )
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider), 
                UsernamePasswordAuthenticationFilter.class
            );
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 9.3 금융위원회 API 서비스

**FinanceApiService.java**:
```java
@Service
@Slf4j
public class FinanceApiService {
    
    @Value("${finance.api.key}")
    private String apiKey;
    
    @Value("${finance.api.url}")
    private String apiUrl;
    
    private final RestTemplate restTemplate;
    private final CompanyRepository companyRepository;
    
    public StockChartDto getStockPriceInfo(String stockCode, LocalDate startDate, LocalDate endDate) {
        try {
            String url = buildApiUrl(stockCode, startDate, endDate);
            log.info("금융위원회 API 호출: {}", url);
            
            ResponseEntity<FinanceApiResponse> response = restTemplate.getForEntity(
                url, 
                FinanceApiResponse.class
            );
            
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new CustomException(ErrorCode.EXTERNAL_API_ERROR);
            }
            
            FinanceApiResponse apiResponse = response.getBody();
            
            if (apiResponse == null || !"00".equals(apiResponse.getResponse().getHeader().getResultCode())) {
                throw new CustomException(ErrorCode.EXTERNAL_API_ERROR);
            }
            
            return convertToStockChartDto(apiResponse, stockCode);
            
        } catch (Exception e) {
            log.error("금융위원회 API 호출 중 오류 발생", e);
            throw new CustomException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }
    
    private String buildApiUrl(String stockCode, LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String companyName = getCompanyName(stockCode);
        
        return UriComponentsBuilder.fromHttpUrl(apiUrl)
            .queryParam("serviceKey", apiKey)
            .queryParam("numOfRows", ChronoUnit.DAYS.between(startDate, endDate) + 1)
            .queryParam("pageNo", 1)
            .queryParam("resultType", "json")
            .queryParam("beginBasDt", startDate.format(formatter))
            .queryParam("endBasDt", endDate.format(formatter))
            .queryParam("itmsNm", companyName)
            .build()
            .encode()
            .toUriString();
    }
    
    private String getCompanyName(String stockCode) {
        return companyRepository.findByStockCode(stockCode)
            .map(Company::getCompName)
            .orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));
    }
}
```

### 9.4 JPA Repository

**CompanyRepository.java**:
```java
@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    
    Optional<Company> findByStockCode(String stockCode);
    
    Page<Company> findByCompNameContaining(String keyword, Pageable pageable);
    
    Page<Company> findByIndustryCode(String industryCode, Pageable pageable);
    
    @Query("SELECT c FROM Company c WHERE c.compName LIKE %:keyword% AND c.industryCode = :industryCode")
    Page<Company> searchByKeywordAndIndustry(
        @Param("keyword") String keyword,
        @Param("industryCode") String industryCode,
        Pageable pageable
    );
}
```

**StockRepository.java**:
```java
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    
    List<Stock> findByUserCode(String userCode);
    
    @Query("SELECT s FROM Stock s JOIN FETCH s.company WHERE s.userCode = :userCode")
    List<Stock> findByUserCodeWithCompany(@Param("userCode") String userCode);
    
    Optional<Stock> findByUserCodeAndStockCode(String userCode, String stockCode);
    
    boolean existsByUserCodeAndStockCode(String userCode, String stockCode);
    
    void deleteByUserCodeAndStockCode(String userCode, String stockCode);
}
```

### 9.5 예외 처리

**ErrorCode.java**:
```java
@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 공통
    INTERNAL_SERVER_ERROR(500, "C001", "서버 내부 오류가 발생했습니다"),
    INVALID_INPUT_VALUE(400, "C002", "잘못된 입력값입니다"),
    
    // 인증
    UNAUTHORIZED(401, "A001", "인증이 필요합니다"),
    TOKEN_EXPIRED(401, "A002", "토큰이 만료되었습니다"),
    INVALID_TOKEN(401, "A003", "유효하지 않은 토큰입니다"),
    
    // 회원
    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다"),
    DUPLICATE_USER(409, "U002", "이미 존재하는 사용자입니다"),
    INVALID_PASSWORD(400, "U003", "비밀번호가 일치하지 않습니다"),
    
    // 데이터
    DATA_NOT_FOUND(404, "D001", "데이터를 찾을 수 없습니다"),
    
    // 외부 API
    EXTERNAL_API_ERROR(500, "E001", "외부 API 호출 중 오류가 발생했습니다"),
    EXTERNAL_API_TIMEOUT(504, "E002", "외부 API 응답 시간이 초과되었습니다");
    
    private final int status;
    private final String code;
    private final String message;
}
```

**GlobalExceptionHandler.java**:
```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("CustomException: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(errorCode));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception: ", e);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
```

### 9.6 Application 설정

**application.yml**:
```yaml
spring:
  application:
    name: company-analyzer
  
  datasource:
    url: jdbc:mysql://localhost:3306/company_analyzer?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}
    driver-class-name: com.mysql.cj.jdbc.Driver
    
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect

# API Keys
dart:
  api:
    key: ${DART_API_KEY}
    url: https://opendart.fss.or.kr/api

finance:
  api:
    key: ${FINANCE_API_KEY}
    url: http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo

exim:
  api:
    key: ${EXIM_API_KEY}
    url: https://www.koreaexim.go.kr/site/program/financial/exchangeJSON

naver:
  client:
    id: ${NAVER_CLIENT_ID}
    secret: ${NAVER_CLIENT_SECRET}

# JWT
jwt:
  secret: ${JWT_SECRET:your-secret-key-at-least-256-bits-long}
  expiration: 7200000 # 2 hours

# Logging
logging:
  level:
    com.company.analyzer: DEBUG
    org.springframework.web: INFO
```

---

# 기업분석 웹페이지 PRD v2.0 - Part 6

## 배포, 테스트, 모니터링 및 부록

**작성일**: 2025년 11월 11일  
**버전**: 2.0  

---

## 10. 비기능 요구사항

### 10.1 성능
- API 응답 시간: 평균 1초 이내
- 페이지 로딩 시간: 3초 이내
- 외부 API 호출 실패 시 Retry 로직 구현 (최대 3회)
- 자주 조회되는 데이터는 Redis 캐싱 고려 (추후 확장)

### 10.2 보안
- 비밀번호는 BCrypt로 암호화 저장
- JWT 토큰 유효기간: 2시간
- CORS 설정: React 개발 서버(localhost:3000)만 허용
- SQL Injection 방지: Prepared Statement 사용
- XSS 방지: 입력값 검증 및 이스케이프

### 10.3 사용성
- 반응형 웹 디자인 (모바일 대응)
- 로딩 중 스피너 표시
- 에러 발생 시 사용자 친화적 메시지 표시
- 빈 데이터 시 안내 메시지 표시

### 10.4 유지보수성
- API 문서: Swagger/OpenAPI 자동 생성
- 로깅: Logback으로 에러 및 주요 이벤트 기록
- 코드 컨벤션: Google Java Style Guide, Airbnb JavaScript Style Guide
- Git 커밋 메시지: Conventional Commits 규칙 준수

---

## 11. 개발 일정

| Week | Task | Details |
|------|------|---------|
| 1 | 환경 설정 및 DB 구축 | Spring Boot 프로젝트 생성, React 프로젝트 생성, MySQL 테이블 생성, UI 디자인 시스템 구축 |
| 2 | 인증 시스템 개발 | JWT 설정, 회원가입/로그인 API 및 UI, AuthContext 구현 |
| 3 | 경제지표 기능 개발 | 환율 API 연동, 테이블형 게시판 및 상세페이지, 차트 구현 |
| 4 | 기업정보 기능 개발 | DART API 연동, 테이블형 게시판 및 상세페이지 |
| 5 | 관심기업 기능 개발 | CRUD API, 테이블형 게시판 및 탭 기반 상세페이지, 메모 기능 |
| 6 | 주가차트 기능 개발 | 금융위원회 API 연동, Recharts 차트 구현, 기간 선택 기능 |
| 7 | 뉴스 검색 기능 개발 | 뉴스 API/크롤링, 검색 UI, 카드형 리스트 |
| 8 | UI/UX 개선 | 참조 이미지 기반 디자인 적용, 반응형 레이아웃, 애니메이션 |
| 9 | 테스트 및 버그 수정 | 통합 테스트, 사용자 시나리오 테스트, 성능 최적화 |
| 10 | 배포 및 문서화 | 프로덕션 빌드, 서버 배포, README 작성, API 문서화 |

---

## 12. 성공 지표

- 회원가입 및 로그인 성공률: 95% 이상
- API 응답 성공률: 99% 이상
- 페이지 에러율: 1% 미만
- 사용자 피드백: 만족도 4.0/5.0 이상
- 페이지 로딩 시간: 평균 2초 이하

---

## 13. 위험 요소 및 대응

| 위험 | 영향도 | 대응 방안 |
|------|--------|-----------|
| 외부 API 서비스 중단 | 높음 | Fallback 메시지 표시, 캐싱된 데이터 사용, 대체 API 준비 |
| 금융위원회 API 무료 한도 초과 | 중간 | 요청 횟수 제한, 캐싱 적극 활용, 유료 API 전환 검토 |
| CORS 에러 | 낮음 | Spring Security CORS 설정 점검 |
| JWT 토큰 탈취 | 중간 | HTTPS 사용, 짧은 유효기간 설정, Refresh Token 도입 검토 |
| 주말/공휴일 주가 데이터 없음 | 낮음 | 주말/공휴일 안내 메시지 표시, 마지막 거래일 데이터 제공 |

---

## 14. 배포 전략

### 14.1 프론트엔드 배포

**배포 옵션**:
1. **Vercel** (추천) - 무료, 자동 배포, HTTPS 기본 제공
2. **Netlify** - 무료, CI/CD 지원
3. **AWS S3 + CloudFront** - 안정적, 확장 가능

**환경변수 설정**:
```bash
# .env.production
REACT_APP_API_URL=https://api.yourdomain.com
REACT_APP_ENV=production
```

### 14.2 백엔드 배포

**배포 옵션**:
1. **AWS EC2** (추천) - Ubuntu 22.04 LTS, Java 17, MySQL 8.0
2. **Heroku** - 쉬운 배포
3. **Docker + AWS ECS** - 컨테이너 기반

**배포 스크립트 (deploy.sh)**:
```bash
#!/bin/bash
git pull origin main
./mvnw clean package -DskipTests
pkill -f 'java -jar'
nohup java -jar -Dspring.profiles.active=prod target/company-analyzer.jar > app.log 2>&1 &
echo "Deployment completed!"
```

---

## 15. 테스트 전략

### 15.1 단위 테스트

**JUnit 5 + Mockito**:
```java
@ExtendWith(MockitoExtension.class)
class StockServiceTest {
    
    @Mock
    private StockRepository stockRepository;
    
    @Mock
    private FinanceApiService financeApiService;
    
    @InjectMocks
    private StockService stockService;
    
    @Test
    @DisplayName("주가 차트 조회 성공")
    void getStockChart_Success() {
        // Given
        String stockCode = "005930";
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();
        
        StockChartDto mockData = StockChartDto.builder()
            .stockCode(stockCode)
            .compName("삼성전자")
            .build();
        
        when(financeApiService.getStockPriceInfo(stockCode, startDate, endDate))
            .thenReturn(mockData);
        
        // When
        StockChartDto result = stockService.getStockChart(stockCode, 30);
        
        // Then
        assertNotNull(result);
        assertEquals("005930", result.getStockCode());
        assertEquals("삼성전자", result.getCompName());
    }
}
```

### 15.2 통합 테스트

**Spring Boot Test**:
```java
@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @DisplayName("기업 목록 조회 API 테스트")
    void getCompanies_Success() throws Exception {
        mockMvc.perform(get("/api/companies")
                .param("page", "1")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.totalPages").exists());
    }
}
```

---

## 16. 사용자 매뉴얼

### 16.1 회원가입 및 로그인
1. 메인 페이지 우측 상단 [로그인] 클릭
2. [회원가입] 버튼 클릭
3. 아이디, 비밀번호, 이메일, 전화번호 입력
4. [회원가입] 버튼 클릭하여 완료
5. 로그인 페이지에서 아이디/비밀번호 입력 후 로그인

### 16.2 기업 정보 조회
1. 상단 메뉴에서 [기업정보] 클릭
2. 검색창에 기업명 또는 종목코드 입력
3. 검색 결과 테이블에서 원하는 기업 클릭
4. 기업 상세 페이지에서 공시 정보 확인

### 16.3 관심기업 등록 및 관리
1. 기업정보 게시판에서 ⭐ 버튼 클릭하여 등록
2. 상단 메뉴에서 [관심기업] 클릭
3. 등록된 관심기업 목록 확인
4. 기업명 클릭 시 상세 페이지 이동
5. 상세 페이지에서 공시정보, 주가차트, 관련기사, 메모 확인

---

## 17. FAQ

### Q1. 주가 데이터가 표시되지 않아요.
**A**: 금융위원회 API는 주말/공휴일에는 데이터를 제공하지 않습니다. 평일에 다시 시도해주세요.

### Q2. 관심기업이 등록되지 않아요.
**A**: 로그인이 필요한 기능입니다. 로그인 후 다시 시도해주세요.

### Q3. API 호출 오류가 발생해요.
**A**: 외부 API 서비스가 일시적으로 중단되었을 수 있습니다. 잠시 후 다시 시도해주세요.

### Q4. 비밀번호를 잊어버렸어요.
**A**: 현재 비밀번호 재설정 기능은 지원하지 않습니다. 관리자에게 문의해주세요.

### Q5. 모바일에서도 사용할 수 있나요?
**A**: 네, 반응형 디자인으로 모바일/태블릿에서도 사용 가능합니다.

---

## 18. 향후 개발 계획 (Roadmap)

### Phase 2 (3개월 후)
- [ ] 비밀번호 재설정 기능
- [ ] 이메일 인증
- [ ] 알림 기능 (특정 기업 공시 등록 시 알림)
- [ ] 포트폴리오 관리 기능
- [ ] 기업 비교 기능

### Phase 3 (6개월 후)
- [ ] AI 기반 투자 추천
- [ ] 실시간 주가 알림
- [ ] 커뮤니티 기능 (게시판, 댓글)
- [ ] 모바일 앱 출시 (React Native)
- [ ] 프리미엄 구독 서비스

---

## 19. 참고 자료

### 19.1 공식 문서
- Spring Boot: https://spring.io/projects/spring-boot
- React: https://react.dev/
- Recharts: https://recharts.org/
- MySQL: https://dev.mysql.com/doc/

### 19.2 API 문서
- DART 전자공시: https://opendart.fss.or.kr/guide/main.do
- 금융위원회 API: https://www.data.go.kr/
- 한국수출입은행: https://www.koreaexim.go.kr/

### 19.3 디자인 참고
- Material-UI: https://mui.com/
- Ant Design: https://ant.design/
- 참조 이미지: 한국수출입은행, IBK기업은행 웹사이트

---

## 20. 변경 이력

| 버전 | 날짜 | 변경 내용 | 작성자 |
|------|------|-----------|--------|
| 1.0 | 2025-11-11 | 초기 PRD 작성 | PM |
| 2.0 | 2025-11-11 | UI/UX 디자인 가이드 추가, 금융위원회 API 연동, 테이블형 레이아웃 적용 | PM |

---

## 21. 승인 및 서명

**프로젝트 매니저**: ____________________  날짜: ___________

**개발 리드**: ____________________  날짜: ___________

**디자이너**: ____________________  날짜: ___________

---

## 22. 부록

### 부록 A: 환경 설정 체크리스트

**개발 환경**:
- [ ] JDK 17 설치
- [ ] Node.js 22.x 설치
- [ ] MySQL 8.0 설치
- [ ] Git 설치
- [ ] IDE 설정 (IntelliJ IDEA, VS Code)

**API 키 발급**:
- [ ] DART API 키 발급
- [ ] 금융위원회 API 키 발급
- [ ] 한국수출입은행 API 키 발급
- [ ] Naver 검색 API 키 발급 (선택)

**프로젝트 설정**:
- [ ] application.yml 환경변수 설정
- [ ] .env 파일 생성 (프론트엔드)
- [ ] CORS 설정 확인
- [ ] 데이터베이스 연결 테스트

### 부록 B: Git 브랜치 전략

```
main (프로덕션)
  ↑
develop (개발)
  ↑
feature/login (기능 개발)
feature/company-list
feature/stock-chart
...
```

**브랜치 명명 규칙**:
- `feature/기능명`: 새로운 기능 개발
- `bugfix/버그명`: 버그 수정
- `hotfix/긴급수정`: 프로덕션 긴급 수정

### 부록 C: 코드 리뷰 체크리스트

- [ ] 코드 컨벤션 준수
- [ ] 주석 작성 (복잡한 로직)
- [ ] 예외 처리
- [ ] 테스트 코드 작성
- [ ] API 문서 업데이트
- [ ] 성능 최적화
- [ ] 보안 검토

### 부록 D: 주요 API 엔드포인트 요약

| 기능 | Method | Endpoint | 인증 |
|------|--------|----------|------|
| 회원가입 | POST | /api/auth/signup | X |
| 로그인 | POST | /api/auth/login | X |
| 환율 목록 | GET | /api/economy/exchange-rates | X |
| 기업 목록 | GET | /api/companies | X |
| 기업 상세 | GET | /api/companies/:compCode | X |
| 관심기업 목록 | GET | /api/favorites | O |
| 관심기업 등록 | POST | /api/favorites | O |
| 주가 차트 | GET | /api/stocks/:stockCode/chart | X |
| 뉴스 검색 | GET | /api/news/search | X |
| 메모 저장 | POST | /api/favorites/:stockCode/memo | O |

---

## 📌 PRD 문서 완료

이 문서는 기업분석 웹페이지 프로젝트의 제품 요구사항 명세서(PRD) v2.0입니다.

**전체 문서 구성**:
1. 프로젝트 개요 및 UI/UX 디자인 가이드
2. 인증 시스템 및 게시판 기능
3. 주가차트 및 뉴스검색 기능
4. 데이터베이스 및 API 명세
5. 프론트엔드 및 백엔드 구현 가이드
6. 배포, 테스트, 모니터링 및 부록

**문의 및 협업**:
- 프로젝트 관리: Git Issue 트래커 활용
- 일일 스탠드업: 진행 상황 공유 (매일 오전 10시)
- 코드 리뷰: Pull Request 기반 (최소 1명 승인 필요)
- 커뮤니케이션: Slack 또는 Discord

---

**작성일**: 2025년 11월 11일  
**최종 수정일**: 2025년 11월 11일  
**버전**: 2.0  
**작성자**: Product Manager

**이 PRD는 프로젝트의 성공적인 구현을 위한 포괄적인 가이드입니다.**