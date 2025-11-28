import PropTypes from 'prop-types';
import './NewsCard.css';

/**
 * NewsCard 컴포넌트
 * 개별 뉴스 카드를 표시하는 컴포넌트
 */
function NewsCard({ news, onFormatDate, onStripHtml }) {
  // Title에서 HTML 태그 제거
  const cleanTitle = onStripHtml ? onStripHtml(news.title) : news.title;

  // Description에서 HTML 태그 제거 및 길이 제한 (150자)
  const cleanDescription = onStripHtml ? onStripHtml(news.description) : news.description;
  const truncatedDescription = cleanDescription.length > 150
    ? cleanDescription.substring(0, 150) + '...'
    : cleanDescription;

  return (
    <article className="news-card">
      <a
        href={news.link}
        target="_blank"
        rel="noopener noreferrer"
        className="news-card__link"
      >
        <h3 className="news-card__title">
          {cleanTitle}
        </h3>
        <p className="news-card__description">
          {truncatedDescription}
        </p>
        <div className="news-card__meta">
          <span className="news-card__date">
            {onFormatDate ? onFormatDate(news.pubDate) : news.pubDate}
          </span>
          <span className="news-card__arrow">→</span>
        </div>
      </a>
    </article>
  );
}

NewsCard.propTypes = {
  news: PropTypes.shape({
    title: PropTypes.string.isRequired,
    link: PropTypes.string.isRequired,
    description: PropTypes.string.isRequired,
    pubDate: PropTypes.string.isRequired,
  }).isRequired,
  onFormatDate: PropTypes.func,
  onStripHtml: PropTypes.func,
};

export default NewsCard;
