import PropTypes from 'prop-types';
import './StockDetailCard.css';

/**
 * Stock Detail Card Component
 *
 * Displays detailed stock information (시가, 고가, 저가, 거래량)
 * in a card layout with proper formatting
 *
 * @param {Object} props
 * @param {Object} props.data - Latest stock data
 */
function StockDetailCard({ data }) {
  if (!data) {
    return null;
  }

  const detailItems = [
    {
      label: '시가',
      value: data.mkp,
      unit: '원',
      icon: '📊',
    },
    {
      label: '고가',
      value: data.hipr,
      unit: '원',
      icon: '📈',
    },
    {
      label: '저가',
      value: data.lopr,
      unit: '원',
      icon: '📉',
    },
    {
      label: '거래량',
      value: data.trqu,
      unit: '주',
      icon: '💹',
    },
  ];

  return (
    <div className="stock-detail-card">
      <h3 className="card-title">주가 상세 정보</h3>
      <div className="detail-grid">
        {detailItems.map((item) => (
          <div key={item.label} className="detail-item">
            <div className="detail-icon">{item.icon}</div>
            <div className="detail-content">
              <div className="detail-label">{item.label}</div>
              <div className="detail-value">
                {parseInt(item.value).toLocaleString()}
                <span className="detail-unit">{item.unit}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

StockDetailCard.propTypes = {
  data: PropTypes.shape({
    mkp: PropTypes.string,   // 시가
    hipr: PropTypes.string,  // 고가
    lopr: PropTypes.string,  // 저가
    trqu: PropTypes.string,  // 거래량
  }),
};

export default StockDetailCard;
