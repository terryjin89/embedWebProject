/**
 * 인증 Context (전역 상태 관리)
 *
 * 애플리케이션 전역에서 사용자 인증 상태를 관리합니다.
 * - user: 현재 로그인한 사용자 정보
 * - token: JWT 토큰
 * - loading: 인증 상태 로딩 여부
 * - login(email, password): 로그인 함수
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
  const login = async (email) => {
    try {
      // TODO: SCRUM-15에서 실제 API 호출로 대체
      // const response = await authService.login(email, password);
      // const { token: newToken, user: userData } = response.data;

      // 임시 Mock 데이터 (백엔드 연동 전)
      const mockToken = 'mock_jwt_token_' + Date.now();
      const mockUser = {
        id: 1,
        email: email,
        name: email.split('@')[0],
      };

      // 상태 업데이트
      setToken(mockToken);
      setUser(mockUser);

      // localStorage에 저장
      localStorage.setItem('authToken', mockToken);
      localStorage.setItem('user', JSON.stringify(mockUser));

      return { success: true };
    } catch (error) {
      console.error('Login failed:', error);
      return { success: false, error: error.message };
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
  const validateToken = () => {
    // TODO: SCRUM-15에서 JWT 디코딩 및 만료 시간 검증 추가
    if (!token) {
      return false;
    }

    // 임시 검증 로직 (백엔드 연동 전)
    // 실제로는 JWT 디코딩하여 exp claim 확인 필요
    return true;
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
