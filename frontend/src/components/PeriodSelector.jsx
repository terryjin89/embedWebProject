import PropTypes from 'prop-types';
import './PeriodSelector.css';

/**
 * Period Selector Component
 *
 * Displays period selection buttons (30/60/90 days)
 * with highlight for the selected period
 *
 * @param {Object} props
 * @param {number} props.selectedPeriod - Currently selected period in days
 * @param {function} props.onPeriodChange - Callback when period is changed
 */
function PeriodSelector({ selectedPeriod = 30, onPeriodChange }) {
  const periods = [
    { value: 30, label: '30일' },
    { value: 60, label: '60일' },
    { value: 90, label: '90일' },
  ];

  return (
    <div className="period-selector">
      <span className="period-label">조회 기간:</span>
      <div className="period-buttons">
        {periods.map((period) => (
          <button
            key={period.value}
            className={`period-button ${
              selectedPeriod === period.value ? 'active' : ''
            }`}
            onClick={() => onPeriodChange(period.value)}
            type="button"
          >
            {period.label}
          </button>
        ))}
      </div>
    </div>
  );
}

PeriodSelector.propTypes = {
  selectedPeriod: PropTypes.number,
  onPeriodChange: PropTypes.func.isRequired,
};

export default PeriodSelector;
