/**
 * 인증 Context (전역 상태 관리)
 *
 * 애플리케이션 전역에서 사용자 인증 상태를 관리합니다.
 * - user: 현재 로그인한 사용자 정보
 * - token: JWT 토큰
 * - loading: 인증 상태 로딩 여부
 * - login(email, password): 로그인 함수
 * - signup(email, password, name): 회원가입 함수
 * - logout(): 로그아웃 함수
 * - isAuthenticated(): 인증 상태 확인 함수
 * - validateToken(): 토큰 유효성 검증 함수
 *
 * localStorage 관리:
 * - 로그인 시 authToken, user 저장
 * - 로그아웃 시 authToken, user 삭제
 * - 페이지 새로고침 시 localStorage에서 토큰 복원
 *
 * 📁 상세 문서: readme/joinMembershipFunction.md
 * 🎫 SCRUM-6
 */
import { createContext, useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import authService from '../services/authService';

// AuthContext 생성
const AuthContext = createContext(null);

// AuthProvider 컴포넌트
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);

  // 컴포넌트 마운트 시 localStorage에서 토큰 및 사용자 정보 불러오기
  useEffect(() => {
    const initializeAuth = () => {
      try {
        const storedToken = localStorage.getItem('authToken');
        const storedUser = localStorage.getItem('user');

        if (storedToken && storedUser) {
          setToken(storedToken);
          setUser(JSON.parse(storedUser));
        }
      } catch (error) {
        console.error('Failed to initialize auth:', error);
        // 손상된 데이터 정리
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
      } finally {
        setLoading(false);
      }
    };

    initializeAuth();
  }, []);

  // 로그인 함수
  const login = async (email, password) => {
    try {
      // 백엔드 API 호출 (SCRUM-15 구현 완료)
      const response = await authService.login(email, password);

      // 응답 데이터 구조: { token, userCode, email, name }
      const { token: newToken, userCode, email: userEmail, name } = response;

      const userData = {
        userCode,
        email: userEmail,
        name,
      };

      // 상태 업데이트
      setToken(newToken);
      setUser(userData);

      // localStorage에 저장
      localStorage.setItem('authToken', newToken);
      localStorage.setItem('user', JSON.stringify(userData));

      console.log('Login successful:', { userCode, email: userEmail });
      return { success: true };
    } catch (error) {
      console.error('Login failed:', error);
      const errorMessage = error.response?.data?.message || error.message || '로그인에 실패했습니다.';
      return { success: false, error: errorMessage };
    }
  };

  // 회원가입 함수
  const signup = async (email, password, name) => {
    try {
      // 백엔드 API 호출
      const response = await authService.signup({
        email,
        password,
        name,
      });

      // 응답 데이터 구조: { token, userCode, email, name }
      const { token: newToken, userCode, email: userEmail, name: userName } = response;

      const userData = {
        userCode,
        email: userEmail,
        name: userName,
      };

      // 상태 업데이트
      setToken(newToken);
      setUser(userData);

      // localStorage에 저장
      localStorage.setItem('authToken', newToken);
      localStorage.setItem('user', JSON.stringify(userData));

      console.log('Signup successful:', { userCode, email: userEmail });
      return { success: true };
    } catch (error) {
      console.error('Signup failed:', error);
      const errorMessage = error.response?.data?.message || error.message || '회원가입에 실패했습니다.';
      return { success: false, error: errorMessage };
    }
  };

  // 로그아웃 함수
  const logout = () => {
    try {
      // 상태 초기화
      setToken(null);
      setUser(null);

      // localStorage 정리
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');

      return { success: true };
    } catch (error) {
      console.error('Logout failed:', error);
      return { success: false, error: error.message };
    }
  };

  // 토큰 검증 함수 (만료 여부 확인)
  const validateToken = async () => {
    if (!token) {
      return false;
    }

    try {
      // 백엔드 API 호출로 토큰 검증
      await authService.verifyToken(token);
      return true;
    } catch (error) {
      console.error('Token validation failed:', error);
      // 토큰이 유효하지 않으면 로그아웃 처리
      logout();
      return false;
    }
  };

  // 인증 상태 확인 함수
  const isAuthenticated = () => {
    return token !== null && user !== null && validateToken();
  };

  // Context value
  const value = {
    user,
    token,
    loading,
    login,
    signup,
    logout,
    isAuthenticated,
    validateToken,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

AuthProvider.propTypes = {
  children: PropTypes.node.isRequired,
};

export default AuthContext;
