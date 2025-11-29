/**
 * 공통 Axios 인스턴스
 *
 * 모든 API 서비스에서 사용하는 공통 axios 설정
 * - Authorization 헤더에 JWT 토큰 자동 추가 (요청 인터셉터)
 * - 401 Unauthorized 에러 자동 처리 (응답 인터셉터)
 */
import axios from 'axios';

// API Base URL
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

// Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터: Authorization 헤더에 토큰 추가
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터: 401 Unauthorized 에러 처리
axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      // 토큰 만료 또는 유효하지 않은 경우
      console.error('Authentication failed: Token expired or invalid');

      // localStorage 정리
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');

      // 로그인 페이지로 리다이렉트
      if (!window.location.pathname.includes('/login')) {
        window.location.href = '/';
      }
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
