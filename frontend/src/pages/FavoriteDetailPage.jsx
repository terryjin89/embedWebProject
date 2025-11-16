import FavoriteDetailTabs from '../components/FavoriteDetailTabs';
import './FavoriteDetailPage.css';

/**
 * 관심기업 상세페이지
 * 탭 기반 통합 대시보드 (공시정보, 주가차트, 관련기사, 메모)
 */
function FavoriteDetailPage() {
  return (
    <div className="favorite-detail-page">
      <FavoriteDetailTabs />
    </div>
  );
}

export default FavoriteDetailPage;
