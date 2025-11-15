import PropTypes from 'prop-types';
import NewsCard from './NewsCard';
import './NewsList.css';

/**
 * NewsList 컴포넌트
 * 뉴스 카드 리스트와 페이지네이션(더보기) 기능을 제공하는 컴포넌트
 */
function NewsList({ newsResults, onFormatDate, onStripHtml, onLoadMore, hasMore, isLoadingMore }) {
  if (!newsResults || !newsResults.items || newsResults.items.length === 0) {
    return (
      <div className="news-list__empty">
        <span className="empty-icon">📰</span>
        <p>검색 결과가 없습니다</p>
      </div>
    );
  }

  return (
    <div className="news-list-container">
      {/* 검색 결과 헤더 */}
      <div className="news-list__header">
        <h2 className="news-list__title">
          검색 결과 ({newsResults.total || newsResults.display}건)
        </h2>
      </div>

      {/* 뉴스 카드 리스트 */}
      <div className="news-list">
        {newsResults.items.map((news, index) => (
          <NewsCard
            key={`${news.link}-${index}`}
            news={news}
            onFormatDate={onFormatDate}
            onStripHtml={onStripHtml}
          />
        ))}
      </div>

      {/* 더보기 버튼 */}
      {hasMore && onLoadMore && (
        <div className="news-list__more">
          <button
            className="news-list__more-button"
            onClick={onLoadMore}
            disabled={isLoadingMore}
          >
            {isLoadingMore ? (
              <>
                <span className="button-spinner"></span>
                로딩 중...
              </>
            ) : (
              '더보기'
            )}
          </button>
        </div>
      )}
    </div>
  );
}

NewsList.propTypes = {
  newsResults: PropTypes.shape({
    items: PropTypes.arrayOf(
      PropTypes.shape({
        title: PropTypes.string.isRequired,
        link: PropTypes.string.isRequired,
        description: PropTypes.string.isRequired,
        pubDate: PropTypes.string.isRequired,
      })
    ).isRequired,
    total: PropTypes.number,
    display: PropTypes.number,
  }),
  onFormatDate: PropTypes.func,
  onStripHtml: PropTypes.func,
  onLoadMore: PropTypes.func,
  hasMore: PropTypes.bool,
  isLoadingMore: PropTypes.bool,
};

NewsList.defaultProps = {
  hasMore: false,
  isLoadingMore: false,
};

export default NewsList;
