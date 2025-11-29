import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import FavoriteTable from '../components/FavoriteTable';
import './FavoritesPage.css';

/**
 * 관심기업 페이지
 * - ProtectedRoute로 보호됨 (인증된 사용자만 접근 가능)
 * - 관심기업 목록 표시 (FavoriteTable 컴포넌트 사용)
 * - 주가 정보 실시간 표시
 */
function FavoritesPage() {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  /**
   * 로그아웃 핸들러
   */
  const handleLogout = () => {
    logout();
    window.location.href = '/';
  };

  /**
   * 네비게이션 바 렌더링
   */
  const renderNavigation = () => (
    <div className="form-toggle">
      {!user ? (
        <>
          <button className="toggle-btn" onClick={() => navigate('/', { state: { view: 'login' } })}>
            로그인
          </button>
          <button className="toggle-btn" onClick={() => navigate('/', { state: { view: 'signup' } })}>
            회원가입
          </button>
        </>
      ) : (
        <button className="toggle-btn" onClick={handleLogout}>로그아웃</button>
      )}
      <button className="toggle-btn" onClick={() => navigate('/', { state: { view: 'exchange' } })}>
        환율정보
      </button>
      <button className="toggle-btn" onClick={() => navigate('/', { state: { view: 'companies' } })}>
        기업정보
      </button>
      <button className="toggle-btn active" onClick={() => navigate('/favorites')}>
        관심기업
      </button>
      <button className="toggle-btn" onClick={() => navigate('/news')}>
        뉴스검색
      </button>
    </div>
  );

  return (
    <div className="favorites-page">
      {/* 네비게이션 바 */}
      {renderNavigation()}

      {/* 페이지 헤더 */}
      <header className="page-header">
        <div className="header-content">
          <h1 className="page-title">관심기업 게시판</h1>
          <p className="page-description">
            등록한 관심기업의 주가 정보를 한눈에 모니터링할 수 있습니다.
          </p>
        </div>
        <div className="header-actions">
          <button className="btn-back" onClick={() => navigate('/')}>
            ← 홈으로
          </button>
        </div>
      </header>

      {/* 관심기업 테이블 */}
      <main className="page-content">
        <FavoriteTable />
      </main>

      {/* 페이지 푸터 */}
      <footer className="page-footer">
        <p className="footer-info">
          주가 정보는 금융위원회 주식시세정보 API를 통해 제공됩니다.
        </p>
        <p className="footer-notice">
          ※ 주가 정보는 실시간이 아닌 지연된 데이터일 수 있습니다.
        </p>
      </footer>
    </div>
  );
}

export default FavoritesPage;
