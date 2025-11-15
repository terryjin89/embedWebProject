import { useState } from 'react';
import NewsSearch from '../components/NewsSearch';
import './NewsSearchPage.css';

function NewsSearchPage() {
  const [newsResults, setNewsResults] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  /**
   * 검색 결과 핸들러
   */
  const handleSearchResults = (results) => {
    setNewsResults(results);
  };

  /**
   * 로딩 상태 핸들러
   */
  const handleLoading = (loading) => {
    setIsLoading(loading);
  };

  /**
   * 에러 핸들러
   */
  const handleError = (errorMessage) => {
    setError(errorMessage);
    // 에러 발생 시 3초 후 자동으로 사라지도록
    if (errorMessage) {
      setTimeout(() => setError(null), 3000);
    }
  };

  /**
   * 날짜 포맷팅
   */
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now - date);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays === 0) {
      return '오늘';
    } else if (diffDays === 1) {
      return '어제';
    } else if (diffDays < 7) {
      return `${diffDays}일 전`;
    } else {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}.${month}.${day}`;
    }
  };

  /**
   * HTML 태그 제거
   */
  const stripHtmlTags = (html) => {
    const div = document.createElement('div');
    div.innerHTML = html;
    return div.textContent || div.innerText || '';
  };

  return (
    <div className="news-search-page">
      <div className="news-search-page__container">
        {/* 페이지 헤더 */}
        <div className="news-search-page__header">
          <h1 className="news-search-page__title">뉴스 검색</h1>
          <p className="news-search-page__subtitle">
            기업 관련 뉴스를 검색하고 최신 동향을 파악하세요
          </p>
        </div>

        {/* 검색 컴포넌트 */}
        <NewsSearch
          onSearchResults={handleSearchResults}
          onLoading={handleLoading}
          onError={handleError}
        />

        {/* 에러 메시지 */}
        {error && (
          <div className="news-search-page__error">
            <span className="error-icon">⚠️</span>
            {error}
          </div>
        )}

        {/* 로딩 인디케이터 */}
        {isLoading && (
          <div className="news-search-page__loading">
            <div className="loading-spinner"></div>
            <p>뉴스를 검색하는 중...</p>
          </div>
        )}

        {/* 검색 결과 */}
        {!isLoading && newsResults && (
          <div className="news-search-page__results">
            <div className="results-header">
              <h2 className="results-title">
                검색 결과 ({newsResults.total || newsResults.display}건)
              </h2>
            </div>

            {newsResults.items && newsResults.items.length > 0 ? (
              <div className="news-list">
                {newsResults.items.map((news, index) => (
                  <article key={index} className="news-card">
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
                        {stripHtmlTags(news.description)}
                      </p>
                      <div className="news-card__meta">
                        <span className="news-card__date">
                          {formatDate(news.pubDate)}
                        </span>
                        <span className="news-card__arrow">→</span>
                      </div>
                    </a>
                  </article>
                ))}
              </div>
            ) : (
              <div className="news-search-page__empty">
                <span className="empty-icon">📰</span>
                <p>검색 결과가 없습니다</p>
              </div>
            )}
          </div>
        )}

        {/* 검색 전 안내 */}
        {!isLoading && !newsResults && !error && (
          <div className="news-search-page__placeholder">
            <span className="placeholder-icon">🔍</span>
            <h3>기업명을 입력하고 검색해보세요</h3>
            <p>해시태그를 사용하면 더 정확한 뉴스를 찾을 수 있습니다</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default NewsSearchPage;
