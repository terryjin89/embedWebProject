import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import PropTypes from 'prop-types';
import DisclosureTable from './DisclosureTable';
import StockAreaChart from './StockAreaChart';
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

  // 주가 차트 기간 상태 관리
  const [chartPeriod, setChartPeriod] = useState(30);

  // stockCode를 corpCode로 변환하는 매핑 테이블
  const stockToCorpCodeMap = {
    '005930': '00126380', // 삼성전자
    '000660': '00164779', // SK하이닉스
    '005380': '00401731', // 현대자동차
    '066570': '00176701', // LG전자
    '035420': '00782756', // NAVER
    '035720': '00356370', // 카카오
  };

  // corpCode 가져오기 (stockCode가 없으면 기본값 사용)
  const corpCode = stockToCorpCodeMap[stockCode] || '00126380';

  // 임시 더미 데이터 (실제 구현 시 API에서 가져올 데이터)
  const dummyStockData = {
    companyName: '삼성전자',
    stockCode: stockCode || '005930',
    currentPrice: '71,800',
    priceChange: '+1,200',
    priceChangeRate: '+1.70%',
    isPositive: true,
  };

  // 주가 차트 Mock 데이터 (금융위원회 API 형식)
  const mockChartData = [
    {
      basDt: "20251111",
      srtnCd: stockCode || "005930",
      itmsNm: dummyStockData.companyName,
      clpr: "103500",
      vs: "2900",
      fltRt: "2.88",
      mkp: "103700",
      hipr: "106000",
      lopr: "102000",
      trqu: "27742542"
    },
    {
      basDt: "20251110",
      srtnCd: stockCode || "005930",
      itmsNm: dummyStockData.companyName,
      clpr: "100600",
      vs: "2700",
      fltRt: "2.76",
      mkp: "98600",
      hipr: "101000",
      lopr: "97900",
      trqu: "23842327"
    },
    {
      basDt: "20251107",
      srtnCd: stockCode || "005930",
      itmsNm: dummyStockData.companyName,
      clpr: "97900",
      vs: "-1300",
      fltRt: "-1.31",
      mkp: "96400",
      hipr: "100300",
      lopr: "96300",
      trqu: "22908083"
    },
    {
      basDt: "20251106",
      srtnCd: stockCode || "005930",
      itmsNm: dummyStockData.companyName,
      clpr: "99200",
      vs: "-1400",
      fltRt: "-1.39",
      mkp: "103700",
      hipr: "103800",
      lopr: "98800",
      trqu: "28655689"
    },
    {
      basDt: "20251105",
      srtnCd: stockCode || "005930",
      itmsNm: dummyStockData.companyName,
      clpr: "100600",
      vs: "-4300",
      fltRt: "-4.1",
      mkp: "101000",
      hipr: "102000",
      lopr: "96700",
      trqu: "44843020"
    }
  ];

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

  // 주가 차트 기간 변경 핸들러
  const handleChartPeriodChange = (newPeriod) => {
    setChartPeriod(newPeriod);
    console.log('Chart period changed to:', newPeriod);
    // 실제 구현 시: 새로운 기간으로 API 호출
  };

  // 탭 콘텐츠 렌더링
  const renderTabContent = () => {
    switch (activeTab) {
      case 'disclosure':
        return (
          <div className="tab-content">
            <DisclosureTable corpCode={corpCode} />
          </div>
        );

      case 'chart':
        return (
          <div className="tab-content">
            <StockAreaChart
              data={mockChartData}
              period={chartPeriod}
              onPeriodChange={handleChartPeriodChange}
            />
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
