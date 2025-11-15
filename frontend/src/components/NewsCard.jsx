import PropTypes from 'prop-types';
import './NewsCard.css';

/**
 * NewsCard 컴포넌트
 * 개별 뉴스 카드를 표시하는 컴포넌트
 */
function NewsCard({ news, onFormatDate, onStripHtml }) {
  return (
    <article className="news-card">
      <a
        href={news.link}
        target="_blank"
        rel="noopener noreferrer"
        className="news-card__link"
      >
        <h3
          className="news-card__title"
          dangerouslySetInnerHTML={{ __html: news.title }}
        ></h3>
        <p className="news-card__description">
          {onStripHtml ? onStripHtml(news.description) : news.description}
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
