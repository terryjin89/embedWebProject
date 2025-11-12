# 기업분석 웹페이지 - Frontend

React 18.x 기반의 기업분석 웹 애플리케이션 프론트엔드입니다.

## 🛠️ 기술 스택

- **Framework**: React 18.x
- **Build Tool**: Vite
- **Routing**: React Router DOM v6
- **HTTP Client**: Axios
- **Charts**: Recharts
- **Code Quality**: ESLint, Prettier

## 📁 프로젝트 구조

```
frontend/
├── src/
│   ├── components/     # 재사용 가능한 UI 컴포넌트
│   ├── pages/          # 페이지 컴포넌트
│   ├── services/       # API 호출 로직
│   ├── utils/          # 유틸리티 함수
│   ├── App.jsx         # 메인 App 컴포넌트
│   └── main.jsx        # 애플리케이션 진입점
├── public/             # 정적 파일
├── .env.example        # 환경 변수 예시
└── package.json        # 의존성 관리
```

## 🚀 시작하기

### 1. 의존성 설치

```bash
npm install
```

### 2. 환경 변수 설정

`.env.example` 파일을 복사하여 `.env` 파일을 생성하고 필요한 값을 설정합니다:

```bash
cp .env.example .env
```

```.env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_DART_API_KEY=your_dart_api_key
VITE_NAVER_CLIENT_ID=your_naver_client_id
VITE_NAVER_CLIENT_SECRET=your_naver_client_secret
```

### 3. 개발 서버 실행

```bash
npm run dev
```

개발 서버가 실행되면 브라우저에서 http://localhost:5173 으로 접속합니다.

## 📝 주요 스크립트

| 명령어 | 설명 |
|--------|------|
| `npm run dev` | 개발 서버 실행 |
| `npm run build` | 프로덕션 빌드 |
| `npm run preview` | 빌드 결과 미리보기 |
| `npm run lint` | ESLint 검사 |

## 🔧 개발 가이드

### 컴포넌트 작성

재사용 가능한 컴포넌트는 `src/components/` 디렉토리에 작성합니다:

```jsx
// src/components/Button.jsx
export const Button = ({ children, onClick }) => {
  return (
    <button onClick={onClick} className="btn">
      {children}
    </button>
  );
};
```

### API 호출

API 호출 로직은 `src/services/` 디렉토리에 작성합니다:

```javascript
// src/services/api.js
import axios from 'axios';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const getCompanyInfo = async (companyId) => {
  const response = await apiClient.get(`/companies/${companyId}`);
  return response.data;
};
```

### 환경 변수 사용

Vite에서는 `import.meta.env`를 통해 환경 변수에 접근합니다:

```javascript
const apiUrl = import.meta.env.VITE_API_BASE_URL;
```

## 🎨 코드 스타일

프로젝트는 ESLint와 Prettier를 사용하여 일관된 코드 스타일을 유지합니다.

```bash
# ESLint 검사
npm run lint

# Prettier 포맷팅 (IDE 플러그인 사용 권장)
```

## 🐳 Docker 빌드

```bash
# Dockerfile을 이용한 빌드 (프로젝트 루트에서 실행)
docker build -t company-analysis-frontend -f frontend/Dockerfile .
```

## 📦 빌드 및 배포

### 프로덕션 빌드

```bash
npm run build
```

빌드된 파일은 `dist/` 디렉토리에 생성됩니다.

### 빌드 결과 미리보기

```bash
npm run preview
```

## 🔗 관련 링크

- [React 공식 문서](https://react.dev/)
- [Vite 공식 문서](https://vitejs.dev/)
- [React Router 문서](https://reactrouter.com/)
- [Axios 문서](https://axios-http.com/)
- [Recharts 문서](https://recharts.org/)

## 📋 작업 내역

- ✅ Vite를 이용한 React 프로젝트 생성
- ✅ 프로젝트 디렉토리 구조 설정 (components, pages, services, utils)
- ✅ 필수 라이브러리 설치 (axios, recharts, react-router-dom)
- ✅ 환경 변수 설정 (.env, .env.example)
- ✅ ESLint/Prettier 설정

## 🤝 기여

이 프로젝트는 임베디드융합개발자 과정의 일환으로 진행되고 있습니다.
