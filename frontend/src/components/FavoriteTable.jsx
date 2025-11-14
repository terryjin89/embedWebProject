import { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import favoritesService from '../services/favoritesService';
import './FavoriteTable.css';

function FavoriteTable() {
  const navigate = useNavigate();

  // 상태 관리
  const [favorites, setFavorites] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [lastUpdated, setLastUpdated] = useState(null);

  // 정렬 상태
  const [sortBy, setSortBy] = useState('registeredAt'); // 'registeredAt' | 'changeRate' | 'companyName'
  const [sortOrder, setSortOrder] = useState('desc'); // 'asc' | 'desc'

  // 삭제 확인 다이얼로그 상태
  const [deleteDialog, setDeleteDialog] = useState({
    show: false,
    stockCode: null,
    companyName: null,
  });

  // 관심기업 목록 로드
  const loadFavorites = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const data = await favoritesService.getFavoritesWithStockInfo();
      setFavorites(data || []);
      setLastUpdated(new Date());
    } catch (err) {
      console.error('Failed to load favorites:', err);
      setError('관심기업 정보를 불러오는데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  }, []);

  // 컴포넌트 마운트 시 데이터 로드
  useEffect(() => {
    loadFavorites();
  }, [loadFavorites]);

  // 새로고침 버튼 핸들러
  const handleRefresh = () => {
    loadFavorites();
  };

  // 정렬 기능
  const handleSort = (newSortBy) => {
    if (sortBy === newSortBy) {
      // 같은 컬럼 클릭 시 정렬 순서 변경
      setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
    } else {
      // 다른 컬럼 클릭 시 해당 컬럼으로 정렬 (기본 내림차순)
      setSortBy(newSortBy);
      setSortOrder('desc');
    }
  };

  // 정렬된 관심기업 목록
  const sortedFavorites = [...favorites].sort((a, b) => {
    let compareValue = 0;

    switch (sortBy) {
      case 'registeredAt':
        compareValue =
          new Date(a.registeredAt) - new Date(b.registeredAt);
        break;
      case 'changeRate':
        compareValue =
          (a.stockInfo?.changeRate || 0) - (b.stockInfo?.changeRate || 0);
        break;
      case 'companyName':
        compareValue = a.companyName.localeCompare(b.companyName, 'ko-KR');
        break;
      default:
        compareValue = 0;
    }

    return sortOrder === 'asc' ? compareValue : -compareValue;
  });

  // 삭제 확인 다이얼로그 열기
  const openDeleteDialog = (e, favorite) => {
    e.stopPropagation(); // 행 클릭 이벤트 전파 방지
    setDeleteDialog({
      show: true,
      stockCode: favorite.stockCode,
      companyName: favorite.companyName,
    });
  };

  // 삭제 확인 다이얼로그 닫기
  const closeDeleteDialog = () => {
    setDeleteDialog({
      show: false,
      stockCode: null,
      companyName: null,
    });
  };

  // 관심기업 삭제
  const handleDelete = async () => {
    try {
      await favoritesService.removeFavorite(deleteDialog.stockCode);

      // 목록 새로고침
      loadFavorites();

      // 다이얼로그 닫기
      closeDeleteDialog();

      // 성공 메시지 (선택적)
      console.log('관심기업이 삭제되었습니다.');
    } catch (err) {
      console.error('Delete error:', err);
      alert('관심기업 삭제에 실패했습니다.');
      closeDeleteDialog();
    }
  };

  // 상세보기 버튼 핸들러
  const handleViewDetail = (e, favorite) => {
    e.stopPropagation(); // 행 클릭 이벤트 전파 방지
    console.log('상세 페이지 이동:', favorite.companyName, favorite.stockCode);
    navigate(`/favorites/${favorite.stockCode}`);
  };

  // 행 클릭 핸들러 (상세 페이지 이동)
  const handleRowClick = (favorite) => {
    console.log('상세 페이지 이동:', favorite.companyName, favorite.stockCode);
    navigate(`/favorites/${favorite.stockCode}`);
  };

  // 정렬 아이콘 렌더링
  const renderSortIcon = (column) => {
    if (sortBy !== column) {
      return <span className="sort-icon">⇅</span>;
    }
    return sortOrder === 'asc' ? (
      <span className="sort-icon active">↑</span>
    ) : (
      <span className="sort-icon active">↓</span>
    );
  };

  // 로딩 상태
  if (loading && favorites.length === 0) {
    return (
      <div className="favorite-table-container">
        <div className="loading-message">관심기업 정보를 불러오는 중...</div>
      </div>
    );
  }

  // 에러 상태
  if (error) {
    return (
      <div className="favorite-table-container">
        <div className="error-message">
          <p>{error}</p>
          <button className="btn-refresh" onClick={handleRefresh}>
            다시 시도
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="favorite-table-container">
      {/* 헤더 */}
      <div className="table-header">
        <div className="header-left">
          <h2 className="table-title">관심기업</h2>
          {lastUpdated && (
            <span className="last-updated">
              최종 업데이트: {lastUpdated.toLocaleTimeString('ko-KR')}
            </span>
          )}
        </div>
        <button className="btn-refresh" onClick={handleRefresh}>
          <span className="refresh-icon">↻</span> 새로고침
        </button>
      </div>

      {/* 정렬 컨트롤 */}
      <div className="table-controls">
        <div className="sort-info">
          <span>정렬: </span>
          <select
            value={`${sortBy}-${sortOrder}`}
            onChange={(e) => {
              const [newSortBy, newSortOrder] = e.target.value.split('-');
              setSortBy(newSortBy);
              setSortOrder(newSortOrder);
            }}
            className="sort-select"
          >
            <option value="registeredAt-desc">등록일순 (최신순)</option>
            <option value="registeredAt-asc">등록일순 (오래된순)</option>
            <option value="changeRate-desc">변동률순 (높은순)</option>
            <option value="changeRate-asc">변동률순 (낮은순)</option>
            <option value="companyName-asc">기업명순 (가나다순)</option>
            <option value="companyName-desc">기업명순 (역순)</option>
          </select>
        </div>
        <div className="data-count">
          총 {favorites.length}개 관심기업
        </div>
      </div>

      {/* 테이블 */}
      <div className="table-wrapper">
        <table className="favorite-table">
          <thead>
            <tr>
              <th onClick={() => handleSort('companyName')}>
                기업명 {renderSortIcon('companyName')}
              </th>
              <th>종목명</th>
              <th>현재가</th>
              <th onClick={() => handleSort('changeRate')}>
                전일대비 {renderSortIcon('changeRate')}
              </th>
              <th onClick={() => handleSort('registeredAt')}>
                등록일 {renderSortIcon('registeredAt')}
              </th>
              <th>관리</th>
            </tr>
          </thead>
          <tbody>
            {sortedFavorites.length === 0 ? (
              <tr>
                <td colSpan="6" className="no-data">
                  등록된 관심기업이 없습니다.
                </td>
              </tr>
            ) : (
              sortedFavorites.map((favorite, index) => (
                <tr
                  key={`${favorite.stockCode}-${index}`}
                  className={`table-row ${index % 2 === 0 ? 'even' : 'odd'}`}
                  onClick={() => handleRowClick(favorite)}
                >
                  <td className="company-name">{favorite.companyName}</td>
                  <td className="stock-name">{favorite.stockName}</td>
                  <td className="current-price">
                    {favoritesService.formatPrice(
                      favorite.stockInfo?.currentPrice
                    )}
                  </td>
                  <td
                    className={`price-change ${favoritesService.getPriceChangeClass(
                      favorite.stockInfo?.priceChange
                    )}`}
                  >
                    <div className="change-info">
                      <span className="change-symbol">
                        {favorite.stockInfo?.priceChange > 0
                          ? '▲'
                          : favorite.stockInfo?.priceChange < 0
                          ? '▼'
                          : '-'}
                      </span>
                      <div className="change-details">
                        <div>
                          {favoritesService.formatPriceChange(
                            favorite.stockInfo?.priceChange
                          )}
                        </div>
                        <div className="change-rate">
                          {favoritesService.formatChangeRate(
                            favorite.stockInfo?.changeRate
                          )}
                        </div>
                      </div>
                    </div>
                  </td>
                  <td className="registered-at">
                    {new Date(favorite.registeredAt).toLocaleDateString(
                      'ko-KR'
                    )}
                  </td>
                  <td className="action-cell">
                    <div className="action-buttons">
                      <button
                        className="btn-detail"
                        onClick={(e) => handleViewDetail(e, favorite)}
                        title="상세보기"
                      >
                        보기
                      </button>
                      <button
                        className="btn-delete"
                        onClick={(e) => openDeleteDialog(e, favorite)}
                        title="삭제"
                      >
                        삭제
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* 푸터 */}
      {favorites.length > 0 && (
        <div className="table-footer">
          <p className="data-source">주가 정보: 금융위원회 주식시세정보</p>
        </div>
      )}

      {/* 삭제 확인 다이얼로그 */}
      {deleteDialog.show && (
        <div className="dialog-overlay" onClick={closeDeleteDialog}>
          <div
            className="dialog-content"
            onClick={(e) => e.stopPropagation()}
          >
            <h3 className="dialog-title">관심기업 삭제</h3>
            <p className="dialog-message">
              <strong>{deleteDialog.companyName}</strong>을(를) 관심기업에서
              삭제하시겠습니까?
            </p>
            <div className="dialog-actions">
              <button className="btn-cancel" onClick={closeDeleteDialog}>
                취소
              </button>
              <button className="btn-confirm" onClick={handleDelete}>
                삭제
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default FavoriteTable;
