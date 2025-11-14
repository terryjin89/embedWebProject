import { useNavigate } from 'react-router-dom';
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

  return (
    <div className="favorites-page">
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
