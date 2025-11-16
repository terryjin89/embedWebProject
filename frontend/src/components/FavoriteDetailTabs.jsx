import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import PropTypes from 'prop-types';
import './FavoriteDetailTabs.css';

/**
 * FavoriteDetailTabs 컴포넌트
 * 관심기업 상세페이지 탭 기반 통합 대시보드
 *
 * 4개 탭 제공:
 * - 공시정보 (Disclosure Info)
 * - 주가차트 (Stock Chart)
 * - 관련기사 (Related News)
 * - 메모 (Notes)
 */
function FavoriteDetailTabs() {
  const { stockCode } = useParams();
  const navigate = useNavigate();

  // 탭 상태 관리
  const [activeTab, setActiveTab] = useState('disclosure');

  // 임시 더미 데이터 (실제 구현 시 API에서 가져올 데이터)
  const dummyStockData = {
    companyName: '삼성전자',
    stockCode: stockCode || '005930',
    currentPrice: '71,800',
    priceChange: '+1,200',
    priceChangeRate: '+1.70%',
    isPositive: true,
  };

  // 탭 목록 정의
  const tabs = [
    { id: 'disclosure', label: '공시정보', icon: '📋' },
    { id: 'chart', label: '주가차트', icon: '📈' },
    { id: 'news', label: '관련기사', icon: '📰' },
    { id: 'memo', label: '메모', icon: '📝' },
  ];

  // 탭 전환 핸들러
  const handleTabChange = (tabId) => {
    setActiveTab(tabId);
  };

  // 뒤로가기 핸들러
  const handleBack = () => {
    navigate('/favorites');
  };

  // 탭 콘텐츠 렌더링
  const renderTabContent = () => {
    switch (activeTab) {
      case 'disclosure':
        return (
          <div className="tab-content">
            <div className="content-placeholder">
              <p className="placeholder-icon">📋</p>
              <h3>공시정보</h3>
              <p>기업의 공시 정보가 여기에 표시됩니다.</p>
              <p className="placeholder-hint">
                (DisclosureTable 컴포넌트 연동 예정)
              </p>
            </div>
          </div>
        );

      case 'chart':
        return (
          <div className="tab-content">
            <div className="content-placeholder">
              <p className="placeholder-icon">📈</p>
              <h3>주가차트</h3>
              <p>주가 차트가 여기에 표시됩니다.</p>
              <p className="placeholder-hint">
                (StockAreaChart 컴포넌트 연동 예정)
              </p>
            </div>
          </div>
        );

      case 'news':
        return (
          <div className="tab-content">
            <div className="content-placeholder">
              <p className="placeholder-icon">📰</p>
              <h3>관련기사</h3>
              <p>기업 관련 뉴스 기사가 여기에 표시됩니다.</p>
              <p className="placeholder-hint">
                (NewsCardList 컴포넌트 연동 예정)
              </p>
            </div>
          </div>
        );

      case 'memo':
        return (
          <div className="tab-content">
            <div className="content-placeholder">
              <p className="placeholder-icon">📝</p>
              <h3>메모</h3>
              <p>사용자 메모가 여기에 표시됩니다.</p>
              <p className="placeholder-hint">
                (메모 작성/수정 기능 구현 예정)
              </p>
            </div>
          </div>
        );

      default:
        return null;
    }
  };

  return (
    <div className="favorite-detail-tabs">
      {/* 헤더 영역 */}
      <header className="detail-header">
        <button className="btn-back" onClick={handleBack}>
          ← 목록으로
        </button>

        <div className="header-info">
          <h1 className="company-name">{dummyStockData.companyName}</h1>
          <span className="stock-code">({dummyStockData.stockCode})</span>
        </div>

        <div className="price-info">
          <div className="current-price">{dummyStockData.currentPrice}원</div>
          <div className={`price-change ${dummyStockData.isPositive ? 'positive' : 'negative'}`}>
            <span className="change-amount">{dummyStockData.priceChange}</span>
            <span className="change-rate">{dummyStockData.priceChangeRate}</span>
          </div>
        </div>
      </header>

      {/* 탭 네비게이션 */}
      <nav className="tabs-navigation">
        <ul className="tabs-list">
          {tabs.map((tab) => (
            <li key={tab.id} className="tab-item">
              <button
                className={`tab-button ${activeTab === tab.id ? 'active' : ''}`}
                onClick={() => handleTabChange(tab.id)}
              >
                <span className="tab-icon">{tab.icon}</span>
                <span className="tab-label">{tab.label}</span>
              </button>
            </li>
          ))}
        </ul>
      </nav>

      {/* 탭 콘텐츠 영역 */}
      <main className="tabs-content">
        {renderTabContent()}
      </main>
    </div>
  );
}

FavoriteDetailTabs.propTypes = {
  // 필요 시 props 추가
};

export default FavoriteDetailTabs;
