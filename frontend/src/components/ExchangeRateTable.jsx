import { useState, useEffect } from 'react';
import economyService from '../services/economyService';
import './ExchangeRateTable.css';

function ExchangeRateTable() {
  const [exchangeRates, setExchangeRates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [lastUpdated, setLastUpdated] = useState(null);
  const [selectedRow, setSelectedRow] = useState(null);

  // 환율 데이터 로드
  const loadExchangeRates = async () => {
    setLoading(true);
    setError(null);

    try {
      const data = await economyService.getExchangeRates();
      setExchangeRates(data);
      setLastUpdated(new Date());
    } catch (err) {
      console.error('Failed to load exchange rates:', err);
      setError('환율 데이터를 불러오는데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  // 컴포넌트 마운트 시 데이터 로드
  useEffect(() => {
    loadExchangeRates();
  }, []);

  // 새로고침 버튼 핸들러
  const handleRefresh = () => {
    loadExchangeRates();
  };

  // 행 클릭 핸들러
  const handleRowClick = (currency) => {
    setSelectedRow(currency.cur_unit);
    console.log('Selected currency:', currency);
  };

  // 전일대비 아이콘 렌더링
  const renderChangeIndicator = (rate) => {
    // 매매기준율과 전일 종가 비교 (간단한 예시)
    const currentRate = parseFloat(rate.deal_bas_r?.replace(/,/g, '') || 0);

    // 실제로는 전일 데이터가 필요하지만, 여기서는 임의로 표시
    // TODO: 전일 데이터와 비교 로직 추가

    return (
      <span className="change-indicator">
        <span className="change-neutral">-</span>
      </span>
    );
  };

  // 로딩 상태
  if (loading) {
    return (
      <div className="exchange-rate-container">
        <div className="loading-message">환율 정보를 불러오는 중...</div>
      </div>
    );
  }

  // 에러 상태
  if (error) {
    return (
      <div className="exchange-rate-container">
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
    <div className="exchange-rate-container">
      <div className="table-header">
        <div className="header-left">
          <h2 className="table-title">환율 정보</h2>
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

      <div className="table-wrapper">
        <table className="exchange-rate-table">
          <thead>
            <tr>
              <th>통화코드</th>
              <th>국가/통화명</th>
              <th>매매기준율</th>
              <th>전신환(송금) 받을때</th>
              <th>전신환(송금) 보낼때</th>
              <th>전일대비</th>
            </tr>
          </thead>
          <tbody>
            {exchangeRates.length === 0 ? (
              <tr>
                <td colSpan="6" className="no-data">
                  환율 정보가 없습니다.
                </td>
              </tr>
            ) : (
              exchangeRates.map((rate, index) => (
                <tr
                  key={`${rate.cur_unit}-${index}`}
                  className={`
                    table-row
                    ${selectedRow === rate.cur_unit ? 'selected' : ''}
                    ${index % 2 === 0 ? 'even' : 'odd'}
                  `}
                  onClick={() => handleRowClick(rate)}
                >
                  <td className="currency-code">{rate.cur_unit}</td>
                  <td className="currency-name">{rate.cur_nm}</td>
                  <td className="rate-value">
                    {rate.deal_bas_r ? parseFloat(rate.deal_bas_r).toLocaleString('ko-KR', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    }) : '-'}
                  </td>
                  <td className="rate-value">
                    {rate.ttb ? parseFloat(rate.ttb).toLocaleString('ko-KR', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    }) : '-'}
                  </td>
                  <td className="rate-value">
                    {rate.tts ? parseFloat(rate.tts).toLocaleString('ko-KR', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    }) : '-'}
                  </td>
                  <td className="rate-change">
                    {renderChangeIndicator(rate)}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {exchangeRates.length > 0 && (
        <div className="table-footer">
          <p className="data-count">총 {exchangeRates.length}개 통화</p>
          <p className="data-source">
            출처: 한국수출입은행
          </p>
        </div>
      )}
    </div>
  );
}

export default ExchangeRateTable;
