import PropTypes from 'prop-types';
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from 'recharts';
import PeriodSelector from './PeriodSelector';
import StockDetailCard from './StockDetailCard';
import './StockAreaChart.css';

/**
 * Stock Area Chart Component
 *
 * Displays stock price data using Recharts Area Chart
 * with blue gradient styling and responsive design
 *
 * @param {Object} props
 * @param {Array} props.data - Stock price data array
 * @param {boolean} props.loading - Loading state
 * @param {string} props.error - Error message
 * @param {number} props.period - Selected period in days
 * @param {function} props.onPeriodChange - Callback when period changes
 */
function StockAreaChart({
  data = [],
  loading = false,
  error = null,
  period = 30,
  onPeriodChange = null
}) {
  // Custom Tooltip with 전일대비 정보
  const CustomTooltip = ({ active, payload }) => {
    if (active && payload && payload.length) {
      const data = payload[0].payload;

      // 전일대비 계산 및 스타일링
      const vs = data.vs ? parseInt(data.vs) : 0;
      const fltRt = data.fltRt ? parseFloat(data.fltRt) : 0;
      const isPositive = vs > 0;
      const isNegative = vs < 0;

      return (
        <div className="stock-tooltip">
          <p className="tooltip-date">{data.basDt}</p>
          <p className="tooltip-price">
            종가: <span>{parseInt(data.clpr).toLocaleString()}원</span>
          </p>
          {(data.vs && data.fltRt) && (
            <p className={`tooltip-vs ${isPositive ? 'positive' : isNegative ? 'negative' : ''}`}>
              전일대비: {isPositive ? '+' : ''}{vs.toLocaleString()}원
              ({isPositive ? '+' : ''}{fltRt}%)
            </p>
          )}
          <p className="tooltip-change">
            시가: {parseInt(data.mkp).toLocaleString()}원
          </p>
          <p className="tooltip-volume">
            거래량: {parseInt(data.trqu).toLocaleString()}주
          </p>
        </div>
      );
    }
    return null;
  };

  // Loading state
  if (loading) {
    return (
      <div className="stock-area-chart">
        <div className="loading-message">차트를 불러오는 중...</div>
      </div>
    );
  }

  // Error state
  if (error) {
    return (
      <div className="stock-area-chart">
        <div className="error-message">
          <p>{error}</p>
        </div>
      </div>
    );
  }

  // No data state
  if (!data || data.length === 0) {
    return (
      <div className="stock-area-chart">
        <div className="no-data">주가 데이터가 없습니다.</div>
      </div>
    );
  }

  // Get latest data for detail card
  const latestData = data && data.length > 0 ? data[0] : null;

  return (
    <div className="stock-area-chart">
      {/* Period Selector */}
      {onPeriodChange && (
        <PeriodSelector
          selectedPeriod={period}
          onPeriodChange={onPeriodChange}
        />
      )}

      {/* Area Chart */}
      <ResponsiveContainer width="100%" height={400}>
        <AreaChart
          data={data}
          margin={{ top: 10, right: 30, left: 0, bottom: 0 }}
        >
          {/* Gradient Definition */}
          <defs>
            <linearGradient id="colorPrice" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="#1890ff" stopOpacity={0.8} />
              <stop offset="95%" stopColor="#1890ff" stopOpacity={0} />
            </linearGradient>
          </defs>

          {/* Grid */}
          <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />

          {/* X Axis - Date */}
          <XAxis
            dataKey="basDt"
            stroke="#666"
            style={{ fontSize: '12px' }}
            tickFormatter={(value) => {
              // Format: YYYYMMDD -> MM/DD
              return `${value.slice(4, 6)}/${value.slice(6, 8)}`;
            }}
          />

          {/* Y Axis - Price */}
          <YAxis
            stroke="#666"
            style={{ fontSize: '12px' }}
            tickFormatter={(value) => `${(value / 1000).toFixed(0)}K`}
            domain={['auto', 'auto']}
          />

          {/* Tooltip */}
          <Tooltip content={<CustomTooltip />} />

          {/* Area - Closing Price */}
          <Area
            type="monotone"
            dataKey="clpr"
            stroke="#1890ff"
            strokeWidth={2}
            fillOpacity={1}
            fill="url(#colorPrice)"
            animationDuration={1000}
          />
        </AreaChart>
      </ResponsiveContainer>

      {/* Stock Detail Card */}
      {latestData && <StockDetailCard data={latestData} />}
    </div>
  );
}

StockAreaChart.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      basDt: PropTypes.string.isRequired,      // 기준일자 (YYYYMMDD)
      clpr: PropTypes.string.isRequired,        // 종가
      mkp: PropTypes.string,                    // 시가
      hipr: PropTypes.string,                   // 고가
      lopr: PropTypes.string,                   // 저가
      trqu: PropTypes.string,                   // 거래량
      vs: PropTypes.string,                     // 전일대비 (등락)
      fltRt: PropTypes.string,                  // 등락률
    })
  ),
  loading: PropTypes.bool,
  error: PropTypes.string,
  period: PropTypes.number,
  onPeriodChange: PropTypes.func,
};

export default StockAreaChart;
