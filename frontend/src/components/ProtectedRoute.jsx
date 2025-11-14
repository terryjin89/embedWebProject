import { Navigate, useLocation } from 'react-router-dom';
import PropTypes from 'prop-types';
import { useAuth } from '../hooks/useAuth';

/**
 * ProtectedRoute 컴포넌트
 *
 * 인증이 필요한 라우트를 보호하는 컴포넌트입니다.
 * 사용자가 인증되지 않은 경우 로그인 페이지로 리다이렉트하며,
 * 로그인 후 원래 페이지로 돌아갈 수 있도록 location 정보를 전달합니다.
 *
 * @param {Object} props
 * @param {React.ReactNode} props.children - 보호할 컴포넌트
 * @param {string} props.redirectMessage - 리다이렉트 시 표시할 메시지 (기본값: '로그인이 필요한 페이지입니다.')
 */
const ProtectedRoute = ({ children, redirectMessage = '로그인이 필요한 페이지입니다.' }) => {
  const { user, loading, isAuthenticated } = useAuth();
  const location = useLocation();

  // 인증 상태를 확인하는 동안 로딩 표시
  if (loading) {
    return (
      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          minHeight: '100vh',
          fontSize: '1.125rem',
          color: '#6b7280',
        }}
      >
        인증 상태를 확인하는 중...
      </div>
    );
  }

  // 인증되지 않은 경우 로그인 페이지로 리다이렉트
  if (!user || !isAuthenticated()) {
    return (
      <Navigate
        to="/login"
        state={{
          from: location.pathname,
          message: redirectMessage,
        }}
        replace
      />
    );
  }

  // 인증된 경우 자식 컴포넌트 렌더링
  return children;
};

ProtectedRoute.propTypes = {
  children: PropTypes.node.isRequired,
  redirectMessage: PropTypes.string,
};

export default ProtectedRoute;
