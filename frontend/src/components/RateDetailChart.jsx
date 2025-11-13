import { useState, useEffect } from 'react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from 'recharts';
import economyService from '../services/economyService';
import './RateDetailChart.css';

function RateDetailChart({ currencyCode, currencyName }) {
  const [chartData, setChartData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedPeriod, setSelectedPeriod] = useState(30); // 기본값: 30일
  const [statistics, setStatistics] = useState({
    highest: 0,
    lowest: 0,
    average: 0,
  });

  // 기간 옵션
  const periodOptions = [
    { label: '7일', value: 7 },
    { label: '30일', value: 30 },
    { label: '90일', value: 90 },
    { label: '1년', value: 365 },
  ];

  // 환율 데이터 로드
  const loadChartData = async (period) => {
    setLoading(true);
    setError(null);

    try {
      const data = await economyService.getHistoricalRates(currencyCode, period);
      setChartData(data);

      // 통계 계산
      if (data.length > 0) {
        const rates = data.map((item) => parseFloat(item.rate));
        setStatistics({
          highest: Math.max(...rates).toFixed(2),
          lowest: Math.min(...rates).toFixed(2),
          average: (rates.reduce((a, b) => a + b, 0) / rates.length).toFixed(2),
        });
      }
    } catch (err) {
      console.error('Failed to load chart data:', err);
      setError('차트 데이터를 불러오는데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  // 컴포넌트 마운트 시 및 기간 변경 시 데이터 로드
  useEffect(() => {
    if (currencyCode) {
      loadChartData(selectedPeriod);
    }
  }, [currencyCode, selectedPeriod]);

  // 기간 선택 핸들러
  const handlePeriodChange = (period) => {
    setSelectedPeriod(period);
  };

  // 툴팁 커스터마이징
  const CustomTooltip = ({ active, payload }) => {
    if (active && payload && payload.length) {
      return (
        <div className="custom-tooltip">
          <p className="tooltip-date">{payload[0].payload.date}</p>
          <p className="tooltip-rate">
            환율: <span>{payload[0].value.toLocaleString('ko-KR')}</span>
          </p>
        </div>
      );
    }
    return null;
  };

  // 로딩 상태
  if (loading) {
    return (
      <div className="rate-detail-chart">
        <div className="loading-message">차트를 불러오는 중...</div>
      </div>
    );
  }

  // 에러 상태
  if (error) {
    return (
      <div className="rate-detail-chart">
        <div className="error-message">
          <p>{error}</p>
          <button className="btn-retry" onClick={() => loadChartData(selectedPeriod)}>
            다시 시도
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="rate-detail-chart">
      {/* 헤더 */}
      <div className="chart-header">
        <div className="chart-title-section">
          <h2 className="chart-title">
            {currencyCode} - {currencyName}
          </h2>
          <p className="chart-subtitle">환율 추이</p>
        </div>

        {/* 기간 선택 버튼 */}
        <div className="period-selector">
          {periodOptions.map((option) => (
            <button
              key={option.value}
              className={`period-btn ${selectedPeriod === option.value ? 'active' : ''}`}
              onClick={() => handlePeriodChange(option.value)}
            >
              {option.label}
            </button>
          ))}
        </div>
      </div>

      {/* 통계 정보 카드 */}
      <div className="statistics-cards">
        <div className="stat-card stat-highest">
          <div className="stat-label">최고가</div>
          <div className="stat-value">{statistics.highest}</div>
          <div className="stat-icon">▲</div>
        </div>
        <div className="stat-card stat-lowest">
          <div className="stat-label">최저가</div>
          <div className="stat-value">{statistics.lowest}</div>
          <div className="stat-icon">▼</div>
        </div>
        <div className="stat-card stat-average">
          <div className="stat-label">평균가</div>
          <div className="stat-value">{statistics.average}</div>
          <div className="stat-icon">─</div>
        </div>
      </div>

      {/* 차트 */}
      <div className="chart-container">
        {chartData.length === 0 ? (
          <div className="no-data">선택한 기간의 환율 데이터가 없습니다.</div>
        ) : (
          <ResponsiveContainer width="100%" height={400}>
            <LineChart
              data={chartData}
              margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
            >
              <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
              <XAxis
                dataKey="date"
                stroke="#666"
                style={{ fontSize: '12px' }}
                angle={-45}
                textAnchor="end"
                height={80}
              />
              <YAxis
                stroke="#666"
                style={{ fontSize: '12px' }}
                tickFormatter={(value) => value.toLocaleString('ko-KR')}
              />
              <Tooltip content={<CustomTooltip />} />
              <Legend
                wrapperStyle={{ paddingTop: '20px' }}
                iconType="line"
              />
              <Line
                type="monotone"
                dataKey="rate"
                stroke="#1890ff"
                strokeWidth={2}
                dot={{ r: 3 }}
                activeDot={{ r: 5 }}
                name="환율"
              />
            </LineChart>
          </ResponsiveContainer>
        )}
      </div>

      {/* 차트 푸터 */}
      {chartData.length > 0 && (
        <div className="chart-footer">
          <p className="data-info">
            조회 기간: {chartData[0].date} ~ {chartData[chartData.length - 1].date}
          </p>
          <p className="data-source">출처: 한국수출입은행</p>
        </div>
      )}
    </div>
  );
}

export default RateDetailChart;
