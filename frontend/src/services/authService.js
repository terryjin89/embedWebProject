import axios from 'axios';

// API Base URL (환경 변수에서 가져오기)
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

// Axios 인스턴스 생성
const authAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터: Authorization 헤더에 토큰 추가
authAPI.interceptors.request.use(
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
authAPI.interceptors.response.use(
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

      // 로그인 페이지로 리다이렉트 (필요시)
      // window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// 인증 서비스 객체
const authService = {
  /**
   * 사용자 로그인
   * @param {string} email - 사용자 이메일
   * @param {string} password - 사용자 비밀번호
   * @returns {Promise} API 응답
   */
  login: async (email, password) => {
    try {
      const response = await authAPI.post('/auth/login', {
        email,
        password,
      });
      return response.data;
    } catch (error) {
      console.error('Login API error:', error);
      throw error;
    }
  },

  /**
   * 사용자 회원가입
   * @param {Object} userData - 회원가입 데이터
   * @param {string} userData.email - 사용자 이메일
   * @param {string} userData.password - 사용자 비밀번호
   * @param {string} userData.name - 사용자 이름
   * @returns {Promise} API 응답
   */
  register: async (userData) => {
    try {
      const response = await authAPI.post('/auth/register', userData);
      return response.data;
    } catch (error) {
      console.error('Register API error:', error);
      throw error;
    }
  },

  /**
   * 사용자 로그아웃
   * @returns {Promise} API 응답
   */
  logout: async () => {
    try {
      const response = await authAPI.post('/auth/logout');
      return response.data;
    } catch (error) {
      console.error('Logout API error:', error);
      throw error;
    }
  },

  /**
   * 토큰 갱신
   * @returns {Promise} API 응답
   */
  refreshToken: async () => {
    try {
      const response = await authAPI.post('/auth/refresh');
      return response.data;
    } catch (error) {
      console.error('Refresh token API error:', error);
      throw error;
    }
  },

  /**
   * 현재 사용자 정보 조회
   * @returns {Promise} API 응답
   */
  getCurrentUser: async () => {
    try {
      const response = await authAPI.get('/auth/me');
      return response.data;
    } catch (error) {
      console.error('Get current user API error:', error);
      throw error;
    }
  },

  /**
   * 비밀번호 변경
   * @param {string} currentPassword - 현재 비밀번호
   * @param {string} newPassword - 새 비밀번호
   * @returns {Promise} API 응답
   */
  changePassword: async (currentPassword, newPassword) => {
    try {
      const response = await authAPI.put('/auth/password', {
        currentPassword,
        newPassword,
      });
      return response.data;
    } catch (error) {
      console.error('Change password API error:', error);
      throw error;
    }
  },
};

export default authService;
