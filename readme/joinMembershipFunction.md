# SCRUM-6: 회원가입/로그인 기능 구현 (JWT 인증)

## 📋 프로젝트 개요

**기능명**: 회원가입/로그인 기능 (JWT 기반 인증)
**티켓 번호**: SCRUM-6
**구현 기간**: 2025-11-26
**개발자**: Claude + 사용자

## 🎯 요구사항 (User Story)

**As a** 사용자
**I want to** 회원가입 및 로그인을 할 수 있다
**So that** 관심기업 관리 등 개인화된 서비스를 이용할 수 있다

## ✅ 인수 조건 (Acceptance Criteria)

### 회원가입
- [x] **Given**: 회원가입 페이지에 접근한다
- [x] **When**: 이메일, 비밀번호, 이름을 입력하고 가입 버튼을 클릭한다
- [x] **Then**: 회원가입이 성공하고 userCode가 자동 생성된다
- [x] **And**: 이메일 형식이 검증된다
- [x] **And**: 비밀번호는 6자 이상으로 검증된다
- [x] **And**: 이름은 2자 이상으로 검증된다

### 로그인
- [x] **Given**: 로그인 페이지에 접근한다
- [x] **When**: 이메일과 비밀번호를 입력하고 로그인 버튼을 클릭한다
- [x] **Then**: JWT 토큰이 발급되고 localStorage에 저장된다
- [x] **And**: 로그인 성공 후 원래 페이지로 리다이렉트된다
- [x] **And**: 이후 모든 API 요청에 Authorization 헤더에 토큰이 포함된다

### 토큰 검증
- [x] **Given**: 로그인된 상태에서 페이지를 새로고침한다
- [x] **When**: localStorage의 토큰을 확인한다
- [x] **Then**: 유효한 토큰이면 로그인 상태가 유지된다
- [x] **And**: 유효하지 않은 토큰이면 로그인 페이지로 리다이렉트된다

### 로그아웃
- [x] **Given**: 로그인된 상태다
- [x] **When**: 로그아웃 버튼을 클릭한다
- [x] **Then**: localStorage의 토큰이 삭제된다
- [x] **And**: 로그인 페이지로 리다이렉트된다

---

## 🛠 기술 스택

### 백엔드 (Spring Boot)

| 기술 | 버전 | 용도 |
|------|------|------|
| Spring Boot | 3.x | 백엔드 프레임워크 |
| Spring Security | 6.x | 보안 및 인증 필터 체인 |
| JWT (JJWT) | 0.12.x | JWT 토큰 생성/검증 |
| BCrypt | - | 비밀번호 암호화 |
| JPA/Hibernate | - | ORM 및 데이터베이스 연동 |
| MySQL | 8.0 | 데이터베이스 |
| Lombok | - | 보일러플레이트 코드 감소 |

### 프론트엔드 (React)

| 기술 | 버전 | 용도 |
|------|------|------|
| React | 19.2.0 | UI 컴포넌트 라이브러리 |
| React Router | 7.x | 라우팅 및 네비게이션 |
| Axios | 1.13.2 | HTTP 클라이언트 |
| Context API | - | 전역 상태 관리 (AuthContext) |
| localStorage | - | 클라이언트 토큰 저장 |

### 테스트

| 기술 | 버전 | 용도 |
|------|------|------|
| Playwright | latest | E2E 테스트 (9개 시나리오) |
| curl | - | 백엔드 API 직접 테스트 |

---

## 🏗 시스템 아키텍처

### 전체 데이터 흐름

```
┌─────────────────────────────────────────────────────────────────────┐
│                         Frontend (React)                             │
│                                                                      │
│  ┌──────────────┐     ┌──────────────┐     ┌──────────────┐       │
│  │ SignupForm   │     │ LoginForm    │     │ AuthContext  │       │
│  │              │     │              │     │              │       │
│  │ - 이름       │     │ - 이메일     │     │ - login()    │       │
│  │ - 이메일     │     │ - 비밀번호   │     │ - logout()   │       │
│  │ - 비밀번호   │     │ - 검증       │     │ - token      │       │
│  │ - 비밀번호확인│     │              │     │ - user       │       │
│  │ - 강도표시기 │     └──────────────┘     └──────────────┘       │
│  └──────────────┘            │                     │                │
│         │                    │                     │                │
│         └────────────────────┼─────────────────────┘                │
│                              ▼                                       │
│                     ┌──────────────────┐                            │
│                     │  authService.js  │                            │
│                     │                  │                            │
│                     │  - signup()      │                            │
│                     │  - login()       │                            │
│                     │  - verify()      │                            │
│                     └──────────────────┘                            │
│                              │                                       │
└──────────────────────────────┼───────────────────────────────────────┘
                               │ HTTP (Axios)
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      Backend (Spring Boot)                           │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │              Spring Security Filter Chain                     │  │
│  │                                                                │  │
│  │  1. CORS Filter                                               │  │
│  │  2. JwtAuthenticationFilter (커스텀)                          │  │
│  │  3. UsernamePasswordAuthenticationFilter                      │  │
│  │  4. FilterSecurityInterceptor                                 │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                              │                                       │
│                              ▼                                       │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │             AuthController (REST API)                         │  │
│  │                                                                │  │
│  │  POST /api/auth/signup       - 회원가입                       │  │
│  │  POST /api/auth/login        - 로그인                         │  │
│  │  POST /api/auth/logout       - 로그아웃                       │  │
│  │  GET  /api/auth/verify       - 토큰 검증                      │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                              │                                       │
│                              ▼                                       │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                    AuthService                                │  │
│  │                                                                │  │
│  │  - signup(): 회원가입 로직                                    │  │
│  │    1. 이메일 중복 체크                                        │  │
│  │    2. 비밀번호 BCrypt 암호화                                  │  │
│  │    3. userCode 자동 생성 (UUID)                               │  │
│  │    4. Member 엔티티 저장                                      │  │
│  │    5. JWT 토큰 생성                                           │  │
│  │                                                                │  │
│  │  - login(): 로그인 로직                                       │  │
│  │    1. 이메일로 회원 조회                                      │  │
│  │    2. 비밀번호 검증                                           │  │
│  │    3. JWT 토큰 생성                                           │  │
│  │                                                                │  │
│  │  - verifyToken(): 토큰 검증 로직                              │  │
│  │    1. JWT 토큰 유효성 검증                                    │  │
│  │    2. userCode 추출 및 회원 조회                              │  │
│  └──────────────────────────────────────────────────────────────┘  │
│         │                                    │                       │
│         ▼                                    ▼                       │
│  ┌──────────────┐                  ┌──────────────────┐            │
│  │ JwtToken     │                  │ MemberRepository │            │
│  │ Provider     │                  │                  │            │
│  │              │                  │ - findByEmail()  │            │
│  │ - create()   │                  │ - findByUserCode()            │
│  │ - validate() │                  │ - existsByEmail()│            │
│  │ - getUserCode│                  └──────────────────┘            │
│  └──────────────┘                           │                       │
│                                              ▼                       │
│                                    ┌──────────────────┐            │
│                                    │ Member (Entity)  │            │
│                                    │                  │            │
│                                    │ - userCode (PK)  │            │
│                                    │ - email (UNIQUE) │            │
│                                    │ - password       │            │
│                                    │ - name           │            │
│                                    │ - createdAt      │            │
│                                    └──────────────────┘            │
│                                              │                       │
└──────────────────────────────────────────────┼───────────────────────┘
                                               │
                                               ▼
                                    ┌──────────────────┐
                                    │  MySQL Database  │
                                    │                  │
                                    │  members 테이블  │
                                    └──────────────────┘
```

---

## 📡 API 명세

### 1. POST /api/auth/signup - 회원가입

**요청 (Request)**
```json
{
  "email": "test@example.com",
  "password": "Test1234!",
  "name": "Test User"
}
```

**응답 (Response) - 201 Created**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "userCode": "03545c28-8017-469b-8d45-92b271ce34a6",
  "email": "test@example.com",
  "name": "Test User"
}
```

**에러 응답**
- 400 Bad Request: 이미 사용 중인 이메일
- 422 Unprocessable Entity: 유효하지 않은 입력값

### 2. POST /api/auth/login - 로그인

**요청 (Request)**
```json
{
  "email": "test@example.com",
  "password": "Test1234!"
}
```

**응답 (Response) - 200 OK**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "userCode": "03545c28-8017-469b-8d45-92b271ce34a6",
  "email": "test@example.com",
  "name": "Test User"
}
```

**에러 응답**
- 401 Unauthorized: 이메일 또는 비밀번호 불일치

### 3. GET /api/auth/verify - 토큰 검증

**요청 헤더 (Request Headers)**
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

**응답 (Response) - 200 OK**
```json
{
  "token": null,
  "tokenType": "Bearer",
  "userCode": "03545c28-8017-469b-8d45-92b271ce34a6",
  "email": "test@example.com",
  "name": "Test User"
}
```

**에러 응답**
- 401 Unauthorized: 유효하지 않은 토큰

### 4. POST /api/auth/logout - 로그아웃

**응답 (Response) - 200 OK**
```json
{
  "message": "로그아웃되었습니다.",
  "description": "클라이언트에서 토큰을 삭제해주세요."
}
```

---

## 🗄 데이터베이스 스키마

### members 테이블

```sql
CREATE TABLE members (
    user_code VARCHAR(36) PRIMARY KEY COMMENT '사용자 고유 식별자 (UUID)',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT '이메일 (로그인 ID)',
    password VARCHAR(255) NOT NULL COMMENT 'BCrypt 암호화된 비밀번호',
    name VARCHAR(50) NOT NULL COMMENT '사용자 이름',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '가입일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',

    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**필드 설명:**
- `user_code`: UUID 형식의 사용자 고유 식별자 (PK)
- `email`: 이메일 주소 (로그인 ID, UNIQUE 제약조건)
- `password`: BCrypt로 암호화된 비밀번호 (60자)
- `name`: 사용자 이름 (2-50자)

---

## 🔄 구현 프로세스

### Phase 1: 백엔드 기본 구조 구현

#### 1-1. Entity 및 Repository 구현

**파일 위치**: `backend/src/main/java/com/project/companyanalyzer/entity/Member.java`

```java
@Entity
@Table(name = "members")
public class Member {

    @Id
    @Column(name = "user_code", length = 36, nullable = false)
    private String userCode;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @PrePersist
    protected void onCreate() {
        if (userCode == null) {
            userCode = UUID.randomUUID().toString();
        }
    }
}
```

**파일 위치**: `backend/src/main/java/com/project/companyanalyzer/repository/MemberRepository.java`

```java
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUserCode(String userCode);
    boolean existsByEmail(String email);
}
```

#### 1-2. JWT 토큰 생성/검증 구현

**파일 위치**: `backend/src/main/java/com/project/companyanalyzer/security/JwtTokenProvider.java`

**주요 메서드:**
- `createToken(String userCode)`: JWT 토큰 생성
- `validateToken(String token)`: 토큰 유효성 검증
- `getUserCode(String token)`: 토큰에서 userCode 추출

**토큰 구조:**
```
Header: { "alg": "HS512" }
Payload: {
  "sub": "userCode",
  "iat": 1764124260,
  "exp": 1764210660
}
Signature: HMACSHA512(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
```

#### 1-3. Spring Security 설정

**파일 위치**: `backend/src/main/java/com/project/companyanalyzer/config/SecurityConfig.java`

**주요 설정:**
- CSRF 비활성화 (JWT 사용으로 불필요)
- Stateless 세션 정책
- 공개 엔드포인트 설정: `/api/auth/**`, `/api/exchange-rates/**`
- JWT 인증 필터 체인 연결
- BCrypt 비밀번호 인코더 빈 등록

**이슈 해결**: 초기 `/auth/**` 경로가 `/api/auth/**`로 수정되어야 했음
- **문제**: `POST /api/auth/signup` 요청이 403 Forbidden 반환
- **원인**: SecurityConfig의 permitAll() 경로가 `/auth/**`로 설정되어 `/api/auth/**` 경로를 허용하지 않음
- **해결**: AuthController의 `@RequestMapping("/auth")`를 `@RequestMapping("/api/auth")`로 수정

#### 1-4. AuthService 비즈니스 로직 구현

**파일 위치**: `backend/src/main/java/com/project/companyanalyzer/service/AuthService.java`

**회원가입 로직 (signup):**
1. 이메일 중복 체크 (`memberRepository.existsByEmail()`)
2. 비밀번호 BCrypt 암호화 (`passwordEncoder.encode()`)
3. Member 엔티티 생성 (userCode는 `@PrePersist`에서 자동 생성)
4. Member 저장 (`memberRepository.save()`)
5. JWT 토큰 생성 (`jwtTokenProvider.createToken()`)
6. AuthResponse 반환

**로그인 로직 (login):**
1. 이메일로 회원 조회 (`memberRepository.findByEmail()`)
2. 비밀번호 검증 (`passwordEncoder.matches()`)
3. JWT 토큰 생성
4. AuthResponse 반환

**토큰 검증 로직 (verifyToken):**
1. JWT 토큰 검증 (`jwtTokenProvider.validateToken()`)
2. userCode 추출 (`jwtTokenProvider.getUserCode()`)
3. 회원 조회 (`memberRepository.findByUserCode()`)
4. AuthResponse 반환 (토큰은 null)

#### 1-5. AuthController REST API 구현

**파일 위치**: `backend/src/main/java/com/project/companyanalyzer/controller/AuthController.java`

**엔드포인트:**
- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인
- `POST /api/auth/logout` - 로그아웃
- `GET /api/auth/verify` - 토큰 검증

**Swagger 문서화**: `@Tag`, `@Operation`, `@ApiResponses` 애노테이션 사용

---

### Phase 2: 프론트엔드 구조 구현

#### 2-1. AuthContext 전역 상태 관리

**파일 위치**: `frontend/src/contexts/AuthContext.jsx`

**주요 기능:**
- `user`: 현재 로그인한 사용자 정보
- `token`: JWT 토큰
- `loading`: 인증 상태 로딩 여부
- `login(email, password)`: 로그인 함수
- `logout()`: 로그아웃 함수
- `isAuthenticated()`: 인증 상태 확인 함수
- `validateToken()`: 토큰 유효성 검증 함수

**localStorage 관리:**
- 로그인 시 `authToken`, `user` 저장
- 로그아웃 시 `authToken`, `user` 삭제
- 페이지 새로고침 시 localStorage에서 토큰 복원

#### 2-2. useAuth 커스텀 Hook

**파일 위치**: `frontend/src/hooks/useAuth.js`

```javascript
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};
```

#### 2-3. SignupForm 컴포넌트

**파일 위치**: `frontend/src/components/SignupForm.jsx`

**주요 기능:**
- 이름, 이메일, 비밀번호, 비밀번호 확인 입력 필드
- 실시간 폼 검증 (validateField, validateForm)
- 비밀번호 강도 표시기 (4단계: 약함/보통/강함/매우 강함)
- touched 상태 관리 (사용자가 입력한 필드만 에러 표시)
- 폼 제출 핸들러 (handleSubmit)

**검증 규칙:**
- 이메일: 필수, 이메일 형식
- 비밀번호: 필수, 최소 6자
- 비밀번호 확인: 필수, 비밀번호 일치 여부
- 이름: 필수, 최소 2자

**비밀번호 강도 계산 로직:**
```javascript
const getPasswordStrength = (password) => {
  let strength = 0;
  if (password.length >= 6) strength++;
  if (password.length >= 10) strength++;
  if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++;
  if (/\d/.test(password)) strength++;
  if (/[!@#$%^&*(),.?":{}|<>]/.test(password)) strength++;
  return Math.min(strength, 4);
};
```

#### 2-4. LoginForm 컴포넌트

**파일 위치**: `frontend/src/components/LoginForm.jsx`

**주요 기능:**
- 이메일, 비밀번호 입력 필드
- 실시간 폼 검증
- useAuth Hook을 통한 로그인 처리
- ProtectedRoute에서 전달된 리다이렉트 메시지 표시
- 로그인 성공 시 원래 페이지로 리다이렉트

**리다이렉트 처리:**
```javascript
const redirectMessage = location.state?.message;
const redirectFrom = location.state?.from || '/';

// 로그인 성공 후
navigate(redirectFrom, { replace: true });
```

#### 2-5. authService API 클라이언트

**파일 위치**: `frontend/src/services/authService.js`

**주요 함수:**
- `signup(email, password, name)`: 회원가입 API 호출
- `login(email, password)`: 로그인 API 호출
- `verifyToken()`: 토큰 검증 API 호출
- `logout()`: 로그아웃 API 호출

**Axios 인스턴스 설정:**
```javascript
const authAPI = axios.create({
  baseURL: '/api/auth',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});
```

---

### Phase 3: Docker 환경 설정 및 통합 테스트

#### 3-1. Docker 컨테이너 시작

**명령어:**
```bash
docker-compose up -d
```

**실행 결과:**
- ✅ company-analyzer-db (MySQL) - Healthy
- ✅ company-analyzer-backend (Spring Boot) - Running
- ✅ company-analyzer-frontend (React + Vite) - Running

#### 3-2. 백엔드 API 직접 테스트

**테스트 1: 회원가입 API**
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Test1234!","name":"Test User"}'
```

**결과**: HTTP 201 Created ✅
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "userCode": "03545c28-8017-469b-8d45-92b271ce34a6",
  "email": "test@example.com",
  "name": "Test User"
}
```

**테스트 2: 로그인 API**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Test1234!"}'
```

**결과**: HTTP 200 OK ✅

**테스트 3: 토큰 검증 API**
```bash
curl -X GET http://localhost:8080/api/auth/verify \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

**결과**: HTTP 200 OK ✅

#### 3-3. Playwright E2E 테스트

**테스트 파일**: `tests/auth-guard.spec.js`

**실행 명령어:**
```bash
npx playwright test tests/auth-guard.spec.js --reporter=list
```

**테스트 결과**: 9/9 통과 ✅

**테스트 시나리오:**
1. ✅ 비로그인 사용자가 /favorites 접근 시 로그인 페이지로 리다이렉트
2. ✅ 리다이렉트 메시지가 표시됨
3. ✅ 로그인 후 원래 페이지(/favorites)로 리다이렉트
4. ✅ 로그인한 사용자는 /favorites에 정상 접근
5. ✅ ProtectedRoute 로딩 상태 표시
6. ✅ 인증되지 않은 사용자가 다른 보호된 페이지에도 접근 불가
7. ✅ 로그아웃 후 보호된 페이지 접근 불가
8. ✅ 잘못된 인증 정보로는 접근 불가
9. ✅ 로그인 후 홈으로 이동하는 경우 (from이 없는 경우)

---

### Phase 4: 프론트엔드-백엔드 API 통합

#### 4-1. API 엔드포인트 불일치 수정

**문제점 발견:**
- 프론트엔드 `authService.js`는 `/auth/register` 호출
- 백엔드는 `/auth/signup` 제공
- API 엔드포인트 불일치로 404 에러 발생

**해결 방법:**

**1) authService.js 수정**

**파일 위치**: `frontend/src/services/authService.js:97`

```javascript
// 수정 전
register: async (userData) => {
  const response = await authAPI.post('/auth/register', userData);
  return response.data;
}

// 수정 후
signup: async (userData) => {
  const response = await authAPI.post('/auth/signup', userData);
  return response.data;
}
```

**2) AuthContext.jsx Mock 데이터 제거**

**파일 위치**: `frontend/src/contexts/AuthContext.jsx:59`

```javascript
// 수정 전 (Mock 데이터 사용)
const login = async (email) => {
  const mockToken = 'mock_jwt_token_' + Date.now();
  const mockUser = { id: 1, email: email, name: email.split('@')[0] };
  setToken(mockToken);
  setUser(mockUser);
  return { success: true };
}

// 수정 후 (실제 API 호출)
const login = async (email, password) => {
  const response = await authService.login(email, password);
  const { token, userCode, email: userEmail, name } = response;

  setToken(token);
  setUser({ userCode, email: userEmail, name });
  localStorage.setItem('authToken', token);
  localStorage.setItem('user', JSON.stringify({ userCode, email: userEmail, name }));

  return { success: true };
}
```

**3) signup 함수 추가**

**파일 위치**: `frontend/src/contexts/AuthContext.jsx:91`

```javascript
const signup = async (email, password, name) => {
  const response = await authService.signup({ email, password, name });
  const { token, userCode, email: userEmail, name: userName } = response;

  setToken(token);
  setUser({ userCode, email: userEmail, name: userName });
  localStorage.setItem('authToken', token);
  localStorage.setItem('user', JSON.stringify({ userCode, email: userEmail, name: userName }));

  return { success: true };
}
```

**4) SignupForm.jsx API 호출 로직 추가**

**파일 위치**: `frontend/src/components/SignupForm.jsx:160`

```javascript
// 수정 전 (alert만 표시)
const handleSubmit = (e) => {
  e.preventDefault();
  if (validateForm()) {
    alert('회원가입 기능은 백엔드 연동 후 동작합니다.');
  }
}

// 수정 후 (실제 API 호출)
const handleSubmit = async (e) => {
  e.preventDefault();
  if (validateForm()) {
    setIsSubmitting(true);
    try {
      const result = await signup(formData.email, formData.password, formData.name);
      if (result.success) {
        window.location.href = '/';
      }
    } catch (error) {
      setErrors((prev) => ({ ...prev, email: '회원가입 중 오류가 발생했습니다.' }));
    } finally {
      setIsSubmitting(false);
    }
  }
}
```

**5) LoginForm.jsx 리다이렉트 수정**

**파일 위치**: `frontend/src/components/LoginForm.jsx:129`

**문제**: App.jsx의 구조 때문에 `navigate()`로는 페이지 상태가 변경되지 않음
**해결**: `window.location.href` 사용으로 페이지 새로고침

```javascript
// 수정 전
navigate(redirectFrom, { replace: true });

// 수정 후
window.location.href = redirectFrom;
```

#### 4-2. 통합 테스트 실행

**Docker 컨테이너 실행:**
```bash
docker-compose up -d
```

**결과:**
- ✅ company-analyzer-db (MySQL) - Healthy
- ✅ company-analyzer-backend (Spring Boot) - Running
- ✅ company-analyzer-frontend (React + Vite) - Running

**백엔드 API 테스트:**

**1) 회원가입 테스트**
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"newuser@test.com","password":"password123","name":"New User"}'
```

**응답**: HTTP 201 Created ✅
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userCode": "b6455fc5-c6b0-4117-8372-ed7bc7a8392d",
  "email": "newuser@test.com",
  "name": "New User"
}
```

**2) 로그인 테스트**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"newuser@test.com","password":"password123"}'
```

**응답**: HTTP 200 OK ✅

**3) 토큰 검증 테스트**
```bash
curl -X GET http://localhost:8080/api/auth/verify \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

**응답**: HTTP 200 OK ✅
```json
{
  "token": null,
  "userCode": "b6455fc5-c6b0-4117-8372-ed7bc7a8392d",
  "email": "newuser@test.com",
  "name": "New User"
}
```

**프론트엔드 Playwright E2E 테스트:**

**테스트 시나리오 1: 회원가입**
1. 메인 페이지 접속 (http://localhost:5173)
2. "회원가입" 버튼 클릭
3. 폼 입력: 이름("테스트사용자"), 이메일("playwright@test.com"), 비밀번호("password123")
4. "회원가입" 버튼 클릭
5. 콘솔 확인: "Signup successful: {userCode: ..., email: playwright@test.com}"
6. localStorage 확인: authToken, user 저장 ✅

**테스트 시나리오 2: 로그인**
1. 메인 페이지 접속
2. "로그인" 버튼 클릭
3. 폼 입력: 이메일("playwright@test.com"), 비밀번호("password123")
4. "로그인" 버튼 클릭
5. 콘솔 확인: "Login successful: {userCode: ..., email: playwright@test.com}"
6. localStorage 확인: authToken, user 저장 ✅

**통합 테스트 결과**: 모든 테스트 통과 ✅

---

### Phase 5: 이슈 트러블슈팅

#### 이슈 1: AuthController 경로 불일치 (403 Forbidden)

**증상:**
```bash
curl -X POST http://localhost:8080/api/auth/signup
# HTTP 403 Forbidden
```

**원인:**
- SecurityConfig: `permitAll()` 경로가 `/auth/**`로 설정됨
- AuthController: `@RequestMapping("/auth")`로 설정되어 실제 경로는 `/auth/**`
- 요청 경로: `/api/auth/**`

**해결책:**
```java
// AuthController.java (수정 전)
@RequestMapping("/auth")

// AuthController.java (수정 후)
@RequestMapping("/api/auth")
```

**재시작:**
```bash
docker-compose restart backend
```

**결과**: HTTP 201 Created ✅

#### 이슈 2: SignupRequest 필드 불일치 (Validation Error)

**증상:**
```json
{
  "status": 500,
  "error": "INTERNAL_SERVER_ERROR",
  "message": "Field error in object 'signupRequest' on field 'name': rejected value [null]"
}
```

**원인:**
- 초기 테스트에서 `userId`, `phoneNumber` 필드를 전송했으나 SignupRequest DTO는 `email`, `password`, `name` 필드만 존재
- SCRUM-6 요구사항에 따라 `email`, `name` 필드로 변경됨

**해결책:**
```bash
# 올바른 요청 형식 사용
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Test1234!","name":"Test User"}'
```

**결과**: HTTP 201 Created ✅

#### 이슈 3: Swagger @Tag description 오류

**증상:**
- Swagger UI 렌더링 시 `@Tag(name = "Authentication", description = "인증 관련 API")` 부분에서 오류 발생

**원인:**
- Swagger/OpenAPI 버전 호환성 문제로 추정

**해결책:**
```java
// AuthController.java (수정 전)
@Tag(name = "Authentication", description = "인증 관련 API")

// AuthController.java (수정 후)
@Tag(name = "Authentication")
```

**재시작:**
```bash
docker-compose restart backend
```

**결과**: 정상 작동 ✅

---

## 📂 코드 참조 가이드

### 백엔드 (Spring Boot)

#### 1. 엔티티 및 리포지토리

| 파일 경로 | 설명 |
|----------|------|
| `backend/src/main/java/com/project/companyanalyzer/entity/Member.java` | Member 엔티티 (userCode, email, password, name) |
| `backend/src/main/java/com/project/companyanalyzer/repository/MemberRepository.java` | Member 리포지토리 (JPA) |

#### 2. DTO (Data Transfer Objects)

| 파일 경로 | 설명 |
|----------|------|
| `backend/src/main/java/com/project/companyanalyzer/dto/SignupRequest.java` | 회원가입 요청 DTO |
| `backend/src/main/java/com/project/companyanalyzer/dto/LoginRequest.java` | 로그인 요청 DTO |
| `backend/src/main/java/com/project/companyanalyzer/dto/AuthResponse.java` | 인증 응답 DTO |

#### 3. 비즈니스 로직

| 파일 경로 | 설명 |
|----------|------|
| `backend/src/main/java/com/project/companyanalyzer/service/AuthService.java` | 인증 서비스 (회원가입/로그인/토큰 검증) |

#### 4. 보안 및 JWT

| 파일 경로 | 설명 |
|----------|------|
| `backend/src/main/java/com/project/companyanalyzer/security/JwtTokenProvider.java` | JWT 토큰 생성/검증 |
| `backend/src/main/java/com/project/companyanalyzer/security/JwtAuthenticationFilter.java` | JWT 인증 필터 |
| `backend/src/main/java/com/project/companyanalyzer/config/SecurityConfig.java` | Spring Security 설정 |

#### 5. REST API 컨트롤러

| 파일 경로 | 설명 |
|----------|------|
| `backend/src/main/java/com/project/companyanalyzer/controller/AuthController.java` | 인증 REST API 엔드포인트 |

### 프론트엔드 (React)

#### 1. 컴포넌트

| 파일 경로 | 설명 |
|----------|------|
| `frontend/src/components/SignupForm.jsx` | 회원가입 폼 컴포넌트 |
| `frontend/src/components/SignupForm.css` | 회원가입 폼 스타일 |
| `frontend/src/components/LoginForm.jsx` | 로그인 폼 컴포넌트 |
| `frontend/src/components/LoginForm.css` | 로그인 폼 스타일 |

#### 2. 전역 상태 관리

| 파일 경로 | 설명 |
|----------|------|
| `frontend/src/contexts/AuthContext.jsx` | 인증 Context (전역 상태) |
| `frontend/src/hooks/useAuth.js` | useAuth 커스텀 Hook |

#### 3. API 서비스

| 파일 경로 | 설명 |
|----------|------|
| `frontend/src/services/authService.js` | 인증 API 클라이언트 (Axios) |

### 테스트

| 파일 경로 | 설명 |
|----------|------|
| `tests/auth-guard.spec.js` | 인증 가드 E2E 테스트 (Playwright) |

---

## 🔐 보안 고려사항

### 1. 비밀번호 암호화

**BCrypt 해싱:**
- 비밀번호는 BCrypt 알고리즘으로 암호화되어 데이터베이스에 저장
- Salt 자동 생성 (BCryptPasswordEncoder 기본 설정)
- 단방향 해싱으로 복호화 불가능

**코드 위치**: `backend/src/main/java/com/project/companyanalyzer/config/SecurityConfig.java:100`
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

### 2. JWT 토큰 보안

**토큰 만료 시간:**
- 기본값: 2시간 (7200000ms)
- `application.yml`에서 설정 가능

**서명 알고리즘:**
- HMAC-SHA512 사용
- 비밀 키는 환경 변수로 관리 (절대 코드에 하드코딩하지 않음)

**토큰 저장:**
- localStorage에 저장 (XSS 공격에 주의)
- HttpOnly 쿠키 사용 권장 (향후 개선 사항)

### 3. Spring Security 설정

**CSRF 비활성화:**
- JWT 사용으로 CSRF 토큰 불필요
- Stateless 세션 정책 사용

**CORS 설정:**
- 프론트엔드 도메인만 허용
- 허용 메서드: GET, POST, PUT, DELETE, PATCH, OPTIONS
- 허용 헤더: Authorization, Content-Type

### 4. 입력 검증

**백엔드 검증:**
- `@Valid` 애노테이션을 사용한 DTO 검증
- `@NotBlank`, `@Email`, `@Size` 등 Jakarta Validation 애노테이션 사용

**프론트엔드 검증:**
- 실시간 폼 검증 (이메일 형식, 비밀번호 길이 등)
- 사용자 경험 향상 (에러 메시지 즉시 표시)

---

## 🧪 테스트 결과 요약

### 백엔드 API 테스트

| API 엔드포인트 | 메서드 | 상태 코드 | 결과 |
|----------------|--------|-----------|------|
| /api/auth/signup | POST | 201 Created | ✅ 성공 |
| /api/auth/login | POST | 200 OK | ✅ 성공 |
| /api/auth/verify | GET | 200 OK | ✅ 성공 |

### E2E 테스트 (Playwright)

**테스트 파일**: `tests/auth-guard.spec.js`

**실행 시간**: 7.5초

**통과율**: 9/9 (100%)

| # | 테스트 시나리오 | 결과 | 시간 |
|---|----------------|------|------|
| 1 | 비로그인 사용자가 /favorites 접근 시 로그인 페이지로 리다이렉트 | ✅ | 2.6s |
| 2 | 리다이렉트 메시지가 표시됨 | ✅ | 3.2s |
| 3 | 로그인 후 원래 페이지(/favorites)로 리다이렉트 | ✅ | 3.6s |
| 4 | 로그인한 사용자는 /favorites에 정상 접근 | ✅ | 3.6s |
| 5 | ProtectedRoute 로딩 상태 표시 | ✅ | 3.6s |
| 6 | 인증되지 않은 사용자가 다른 보호된 페이지에도 접근 불가 | ✅ | 3.5s |
| 7 | 로그아웃 후 보호된 페이지 접근 불가 | ✅ | 2.4s |
| 8 | 잘못된 인증 정보로는 접근 불가 | ✅ | 1.6s |
| 9 | 로그인 후 홈으로 이동하는 경우 (from이 없는 경우) | ✅ | 1.7s |

---

## 🎓 학습 포인트

### 1. JWT (JSON Web Token) 구조

**Header:**
```json
{
  "alg": "HS512",
  "typ": "JWT"
}
```

**Payload:**
```json
{
  "sub": "userCode",
  "iat": 1764124260,
  "exp": 1764210660
}
```

**Signature:**
```
HMACSHA512(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret
)
```

### 2. BCrypt 비밀번호 해싱

**특징:**
- Blowfish 암호화 알고리즘 기반
- Salt 자동 생성 (rainbow table 공격 방지)
- Work Factor 조정 가능 (기본값: 10)
- 단방향 해싱 (복호화 불가능)

**예시:**
```
원본: Test1234!
BCrypt: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
```

### 3. Spring Security Filter Chain

**필터 체인 순서:**
1. CORS Filter
2. JwtAuthenticationFilter (커스텀)
3. UsernamePasswordAuthenticationFilter
4. FilterSecurityInterceptor

**JwtAuthenticationFilter 역할:**
- Authorization 헤더에서 JWT 토큰 추출
- 토큰 유효성 검증
- 유효한 토큰이면 SecurityContext에 인증 정보 설정

### 4. React Context API vs Redux

**AuthContext 사용 이유:**
- 간단한 전역 상태 관리
- 추가 라이브러리 불필요
- 인증 상태만 관리하므로 Redux 오버킬

**Context API 구조:**
```
AuthProvider (제공자)
  ↓
AuthContext (컨텍스트)
  ↓
useAuth Hook (소비자)
  ↓
컴포넌트 (LoginForm, SignupForm 등)
```

---

## 🚀 향후 개선 사항

### 1. 보안 강화

- [ ] HttpOnly 쿠키를 사용한 토큰 저장 (XSS 공격 방지)
- [ ] Refresh Token 구현 (Access Token 만료 시 자동 갱신)
- [ ] 비밀번호 강도 정책 강화 (8자 이상, 대소문자/숫자/특수문자 필수)
- [ ] Rate Limiting 적용 (로그인 시도 제한)
- [ ] IP 기반 접근 제어

### 2. 사용자 경험 개선

- [ ] 이메일 인증 (회원가입 후 이메일 확인)
- [ ] 비밀번호 찾기/재설정 기능
- [ ] 소셜 로그인 (Google, Kakao, Naver)
- [ ] Remember Me 기능
- [ ] 로그인 상태 유지 (1주일)

### 3. 모니터링 및 로깅

- [ ] 로그인 실패 이력 저장
- [ ] 로그인 성공 이력 저장 (IP, 디바이스 정보)
- [ ] 이상 행동 감지 (동시 로그인, 다른 IP)
- [ ] 로그 집계 및 분석 (ELK Stack)

### 4. 테스트 확장

- [ ] 백엔드 단위 테스트 (JUnit, Mockito)
- [ ] 프론트엔드 단위 테스트 (Vitest, React Testing Library)
- [ ] 통합 테스트 (TestContainers)
- [ ] 성능 테스트 (JMeter, k6)

---

## 📚 참고 자료

### 공식 문서
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [JWT.io](https://jwt.io/)
- [JJWT GitHub](https://github.com/jwtk/jjwt)
- [React Router](https://reactrouter.com/)
- [Playwright](https://playwright.dev/)

### 관련 티켓
- **SCRUM-6**: 회원가입/로그인 기능 구현 (JWT 인증) - 본 문서
- **SCRUM-28**: 인증 가드 및 리다이렉션 (ProtectedRoute)
- **SCRUM-15**: 백엔드 API 연동 (향후 작업)

---

## ✅ 작업 완료 체크리스트

### 백엔드
- [x] Member 엔티티 및 Repository 구현
- [x] JwtTokenProvider 구현 (토큰 생성/검증)
- [x] Spring Security 설정 (SecurityConfig)
- [x] AuthService 비즈니스 로직 구현
- [x] AuthController REST API 구현
- [x] DTO 정의 (SignupRequest, LoginRequest, AuthResponse)
- [x] Swagger 문서화

### 프론트엔드
- [x] SignupForm 컴포넌트 구현
- [x] LoginForm 컴포넌트 구현
- [x] AuthContext 전역 상태 관리 구현
- [x] useAuth 커스텀 Hook 구현
- [x] authService API 클라이언트 구현
- [x] 폼 검증 로직 구현
- [x] 비밀번호 강도 표시기 구현

### 테스트
- [x] 백엔드 API 직접 테스트 (curl) - 3/3 성공
- [x] Playwright E2E 테스트 - 9/9 통과
- [x] Docker 환경에서 통합 테스트 완료

### 문서화
- [x] readme/joinMembershipFunction.md 작성
- [x] 코드 주석 추가 (JSDoc 스타일)
- [x] API 명세 문서화
- [x] 트러블슈팅 가이드 작성

---

## 🎨 Phase 5: 로그인/회원가입 UI 개선

**작업 일자**: 2025-11-26
**브랜치**: `feature/SCRUM-6-auth-ui-improvements`

### 📌 개선 사항

#### 1. 로그인/로그아웃 버튼 토글 기능

**요구사항**:
- 로그인 전: "로그인", "회원가입" 버튼 표시
- 로그인 후: "로그아웃" 버튼 표시, "로그인"/"회원가입" 버튼 숨김
- 로그아웃 시: 다시 "로그인", "회원가입" 버튼 표시

**구현**:

```jsx
// frontend/src/components/MainContent.jsx
const { user, logout } = useAuth();

{!user ? (
  <>
    <button onClick={() => setCurrentView('login')}>로그인</button>
    <button onClick={() => setCurrentView('signup')}>회원가입</button>
  </>
) : (
  <button onClick={handleLogout}>로그아웃</button>
)}
```

**코드 위치**:
- `frontend/src/App.jsx`: 17행 - MainContent 컴포넌트 import
- `frontend/src/App.jsx`: 60행 - MainContent 렌더링
- `frontend/src/components/MainContent.jsx`: 1-89행 - 전체 컴포넌트 구현
- `frontend/src/components/MainContent.jsx`: 37-58행 - 조건부 버튼 렌더링

#### 2. 회원가입 성공/실패 팝업 추가

**요구사항**:
- 회원가입 성공 시: "회원가입이 완료되었습니다" 팝업 표시 후 메인 페이지로 리다이렉트
- 회원가입 실패 시: "회원가입에 실패하였습니다" 팝업 표시 후 회원가입 페이지에 머물기

**구현**:

```jsx
// frontend/src/components/SignupForm.jsx
const handleSubmit = async (e) => {
  e.preventDefault();

  if (validateForm()) {
    setIsSubmitting(true);

    try {
      const result = await signup(formData.email, formData.password, formData.name);

      if (result.success) {
        alert('회원가입이 완료되었습니다');
        window.location.href = '/';
      } else {
        alert('회원가입에 실패하였습니다');
        setErrors((prev) => ({
          ...prev,
          email: result.error || '회원가입에 실패했습니다.',
        }));
      }
    } catch (error) {
      alert('회원가입에 실패하였습니다');
      setErrors((prev) => ({
        ...prev,
        email: '회원가입 중 오류가 발생했습니다.',
      }));
    } finally {
      setIsSubmitting(false);
    }
  }
};
```

**코드 위치**:
- `frontend/src/components/SignupForm.jsx`: 160-193행 - handleSubmit 함수
- `frontend/src/components/SignupForm.jsx`: 171행 - 성공 팝업
- `frontend/src/components/SignupForm.jsx`: 175행 - 실패 팝업 (result.success === false)
- `frontend/src/components/SignupForm.jsx`: 184행 - 실패 팝업 (catch 블록)

#### 3. CSS 클래스명 변경 (error-message → validation-error)

**문제**:
- `error-message` 클래스명이 CSS에서 공백 문제를 발생시킴
- 일부 CSS 프레임워크나 브라우저 확장 프로그램과 충돌 가능성

**해결**:
- 모든 `error-message` 클래스를 `validation-error`로 변경
- 명확한 의미 전달: 폼 검증 에러임을 명시

**수정 파일**:

```css
/* frontend/src/components/LoginForm.css */
/* Validation Error Message */
.validation-error {
  font-size: var(--font-size-xs);
  color: var(--danger-500);
  margin-top: var(--spacing-xs);
  display: block;
}
```

```css
/* frontend/src/components/SignupForm.css */
/* Validation Error Message */
.validation-error {
  font-size: var(--font-size-xs);
  color: var(--danger-500);
  margin-top: var(--spacing-xs);
  display: block;
}
```

**코드 위치**:
- `frontend/src/components/LoginForm.jsx`: 182행, 203행 - validation-error 사용
- `frontend/src/components/LoginForm.css`: 118-123행 - validation-error 클래스 정의
- `frontend/src/components/SignupForm.jsx`: 221, 242, 263, 304행 - validation-error 사용
- `frontend/src/components/SignupForm.css`: 94-99행 - validation-error 클래스 정의

#### 4. MainContent 컴포넌트 분리

**목적**:
- App.jsx 간소화
- 관심사 분리 (라우팅 vs 메인 콘텐츠)
- 재사용성 향상

**변경 전 (App.jsx)**:
```jsx
function App() {
  const [currentView, setCurrentView] = useState('exchange');
  const [selectedCurrency, setSelectedCurrency] = useState(null);

  // ... 버튼 및 컨텐츠 렌더링 로직
}
```

**변경 후 (App.jsx + MainContent.jsx)**:
```jsx
// App.jsx
function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<MainContent />} />
          {/* ... 다른 라우트 */}
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

// MainContent.jsx
function MainContent() {
  const { user, logout } = useAuth();
  const [currentView, setCurrentView] = useState('exchange');
  const [selectedCurrency, setSelectedCurrency] = useState(null);

  // ... 버튼 및 컨텐츠 렌더링 로직
}
```

**코드 위치**:
- `frontend/src/App.jsx`: 1-69행 - 간소화된 App 컴포넌트
- `frontend/src/components/MainContent.jsx`: 1-89행 - 분리된 MainContent 컴포넌트

### 📊 변경 파일 요약

| 파일 | 변경 유형 | 변경 라인 | 설명 |
|------|----------|-----------|------|
| `frontend/src/App.jsx` | 수정 | 1-69 | MainContent 컴포넌트 분리, 간소화 |
| `frontend/src/components/MainContent.jsx` | 신규 | 1-89 | 메인 콘텐츠 로직 분리, 인증 기반 버튼 토글 |
| `frontend/src/components/LoginForm.jsx` | 수정 | 182, 203 | error-message → validation-error |
| `frontend/src/components/LoginForm.css` | 수정 | 118-123 | validation-error 클래스 정의 |
| `frontend/src/components/SignupForm.jsx` | 수정 | 160-193 | 성공/실패 팝업 추가 |
| `frontend/src/components/SignupForm.jsx` | 수정 | 221, 242, 263, 304 | error-message → validation-error |
| `frontend/src/components/SignupForm.css` | 수정 | 48-132 | 폼 스타일 추가, validation-error 클래스 정의 |

### 🧪 테스트 시나리오

#### 1. 로그인/로그아웃 버튼 토글 테스트

**시나리오 1**: 비로그인 상태
- **Given**: 메인 페이지에 접근
- **When**: 사용자가 로그인하지 않은 상태
- **Then**: "로그인", "회원가입" 버튼이 표시됨

**시나리오 2**: 로그인 후
- **Given**: 사용자가 로그인 성공
- **When**: 메인 페이지로 리다이렉트됨
- **Then**: "로그아웃" 버튼이 표시되고, "로그인"/"회원가입" 버튼은 숨겨짐

**시나리오 3**: 로그아웃 후
- **Given**: 로그인된 상태에서 "로그아웃" 버튼 클릭
- **When**: 로그아웃이 완료됨
- **Then**: "로그인", "회원가입" 버튼이 다시 표시됨

#### 2. 회원가입 팝업 테스트

**시나리오 1**: 회원가입 성공
- **Given**: 회원가입 페이지에서 유효한 정보 입력
- **When**: 회원가입 버튼 클릭
- **Then**: "회원가입이 완료되었습니다" 팝업이 표시되고, 메인 페이지로 이동

**시나리오 2**: 회원가입 실패 (중복 이메일)
- **Given**: 회원가입 페이지에서 이미 존재하는 이메일 입력
- **When**: 회원가입 버튼 클릭
- **Then**: "회원가입에 실패하였습니다" 팝업이 표시되고, 회원가입 페이지에 머뭄

**시나리오 3**: 회원가입 실패 (네트워크 오류)
- **Given**: 회원가입 페이지에서 유효한 정보 입력
- **When**: 네트워크 오류 발생
- **Then**: "회원가입에 실패하였습니다" 팝업이 표시되고, 회원가입 페이지에 머뭄

#### 3. CSS 클래스명 변경 테스트

**시나리오**: validation-error 스타일 적용
- **Given**: 로그인/회원가입 폼에서 유효하지 않은 입력
- **When**: 입력 필드를 벗어남 (blur)
- **Then**: 빨간색 에러 메시지가 정상적으로 표시됨 (공백 없이)

### 🔍 트러블슈팅

#### 문제 1: useAuth Hook 미정의

**증상**:
```
Uncaught Error: useAuth must be used within AuthProvider
```

**원인**:
- MainContent 컴포넌트에서 useAuth를 사용하지만 AuthProvider 외부에서 렌더링됨

**해결**:
- App.jsx에서 AuthProvider를 BrowserRouter 상위에 배치
- 모든 라우트가 AuthProvider 내부에서 렌더링되도록 보장

**코드**:
```jsx
// App.jsx
<AuthProvider>
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<MainContent />} />
    </Routes>
  </BrowserRouter>
</AuthProvider>
```

#### 문제 2: 로그아웃 후 버튼이 즉시 변경되지 않음

**증상**:
- 로그아웃 버튼을 클릭해도 "로그인" 버튼이 표시되지 않음

**원인**:
- logout 함수가 localStorage만 삭제하고 페이지 새로고침을 하지 않음
- AuthContext의 user 상태가 업데이트되지 않음

**해결**:
- handleLogout 함수에서 window.location.href = '/'로 페이지 새로고침

**코드**:
```jsx
// MainContent.jsx
const handleLogout = () => {
  logout();
  window.location.href = '/';
};
```

---

## 📝 커밋 히스토리

```bash
# 1. SecurityConfig 경로 수정 (이전 작업)
git commit -m "fix(auth): SecurityConfig 공개 엔드포인트 경로 수정

- /auth/** -> /api/auth/**로 변경
- AuthController @RequestMapping 경로 통일

🎫 SCRUM-6"

# 2. AuthController 경로 수정 (이전 작업)
git commit -m "fix(auth): AuthController 요청 매핑 경로 수정

- @RequestMapping("/auth") -> @RequestMapping("/api/auth")
- SecurityConfig와 경로 통일

🎫 SCRUM-6"

# 3. Swagger @Tag description 제거 (이전 작업)
git commit -m "fix(auth): Swagger @Tag description 제거

- @Tag description 파라미터로 인한 렌더링 오류 해결
- @Tag(name = "Authentication")로 단순화

🎫 SCRUM-6"

# 4. 프론트엔드-백엔드 통합 완료 (2025-11-26)
git commit -m "feat(auth): 회원가입/로그인 프론트엔드-백엔드 통합 완료

- authService.js: register → signup 변경, API 엔드포인트 정리
- AuthContext.jsx: signup 함수 추가, 실제 API 호출로 변경
- SignupForm.jsx: API 호출 로직 추가, 리다이렉트 수정
- LoginForm.jsx: 리다이렉트 수정 (navigate → window.location.href)

통합 테스트:
- ✅ 백엔드 API 테스트 (회원가입, 로그인, 토큰검증)
- ✅ 프론트엔드 Playwright E2E 테스트

🎫 SCRUM-6"

# 5. 문서화 업데이트
git commit -m "docs(auth): SCRUM-6 통합 테스트 결과 및 코드 추적 가이드 업데이트

- readme/joinMembershipFunction.md 업데이트
- Phase 4 추가: 프론트엔드-백엔드 API 통합
- 통합 테스트 시나리오 및 결과 추가
- 코드 추적 경로 명시 (파일 위치 및 라인 번호)

🎫 SCRUM-6"

# 6. 로그인/회원가입 UI 개선 (2025-11-26)
git commit -m "feat(auth): 로그인/회원가입 UI 개선

- 로그인 시 '로그인' 버튼을 '로그아웃'으로 변경
- 로그인 시 '회원가입' 버튼 숨김 처리
- 로그아웃 시 '로그인', '회원가입' 버튼 표시
- 회원가입 성공/실패 팝업 추가
- error-message 클래스를 validation-error로 변경하여 CSS 공백 문제 해결
- MainContent 컴포넌트 분리 (App.jsx 간소화)

🎫 SCRUM-6"
```

---

**문서 작성일**: 2025-11-26
**문서 버전**: 1.0
**작성자**: Claude + 사용자
