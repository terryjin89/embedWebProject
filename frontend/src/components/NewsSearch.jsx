import { useState } from 'react';
import PropTypes from 'prop-types';
import newsService from '../services/newsService';
import './NewsSearch.css';

// 해시태그 목록
const HASHTAGS = [
  { id: 'news', label: '#기사', value: '기사' },
  { id: 'earnings', label: '#실적', value: '실적' },
  { id: 'investment', label: '#투자', value: '투자' },
  { id: 'product', label: '#신제품', value: '신제품' },
  { id: 'personnel', label: '#인사', value: '인사' },
];

// 정렬 옵션
const SORT_OPTIONS = [
  { value: 'date', label: '최신순' },
  { value: 'sim', label: '관련도순' },
];

function NewsSearch({ onSearchResults, onLoading, onError }) {
  // 검색 상태
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedHashtag, setSelectedHashtag] = useState('');
  const [sortOrder, setSortOrder] = useState('date');
  const [isSearching, setIsSearching] = useState(false);

  /**
   * 검색 실행
   * @param {string} hashtagOverride - 해시태그 오버라이드 (선택적)
   */
  const performSearch = async (hashtagOverride) => {
    // 유효성 검증
    if (!searchQuery.trim()) {
      if (onError) {
        onError('기업명을 입력해주세요');
      }
      return;
    }

    setIsSearching(true);
    if (onLoading) {
      onLoading(true);
    }

    try {
      // 검색 쿼리 생성
      const company = searchQuery.trim();
      // hashtagOverride가 있으면 사용, 없으면 selectedHashtag 사용
      const currentHashtag = hashtagOverride !== undefined ? hashtagOverride : selectedHashtag;
      // 백엔드에서 '#' 기호를 추가하므로 여기서는 제거
      const hashtag = currentHashtag ? currentHashtag : '';

      // 검색 파라미터
      const searchParams = {
        company,
        hashtag,
        page: 1,
        size: 10,
        sort: sortOrder,
      };

      // 뉴스 검색
      const results = await newsService.searchNews(searchParams);

      // 검색 결과 및 파라미터 전달
      if (onSearchResults) {
        onSearchResults(results, searchParams);
      }

      // 에러 초기화
      if (onError) {
        onError(null);
      }
    } catch (error) {
      console.error('뉴스 검색 실패:', error);
      if (onError) {
        onError('뉴스 검색에 실패했습니다. 다시 시도해주세요.');
      }
    } finally {
      setIsSearching(false);
      if (onLoading) {
        onLoading(false);
      }
    }
  };

  /**
   * 검색창 입력 핸들러
   */
  const handleSearchInputChange = (e) => {
    setSearchQuery(e.target.value);
  };

  /**
   * Enter 키 핸들러
   */
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      performSearch();
    }
  };

  /**
   * 검색 버튼 클릭 핸들러
   */
  const handleSearchClick = () => {
    performSearch();
  };

  /**
   * 해시태그 클릭 핸들러
   */
  const handleHashtagClick = (hashtag) => {
    // 이미 선택된 해시태그를 다시 클릭하면 선택 해제
    if (selectedHashtag === hashtag.value) {
      setSelectedHashtag('');
      // 검색어가 있으면 해시태그 없이 재검색
      if (searchQuery.trim()) {
        performSearch(''); // 빈 문자열 = 해시태그 없음
      }
    } else {
      setSelectedHashtag(hashtag.value);
      // 검색어가 있으면 새 해시태그로 즉시 검색
      if (searchQuery.trim()) {
        performSearch(hashtag.value);
      }
    }
  };

  /**
   * 정렬 변경 핸들러
   */
  const handleSortChange = (e) => {
    setSortOrder(e.target.value);
  };

  return (
    <div className="news-search">
      <div className="news-search__container">
        {/* 검색창 */}
        <div className="news-search__input-group">
          <input
            type="text"
            className="news-search__input"
            placeholder="기업명을 입력하세요 (예: 삼성전자, 현대자동차)"
            value={searchQuery}
            onChange={handleSearchInputChange}
            onKeyPress={handleKeyPress}
            disabled={isSearching}
          />
          <button
            className="news-search__button"
            onClick={handleSearchClick}
            disabled={isSearching || !searchQuery.trim()}
          >
            {isSearching ? '검색 중...' : '검색'}
          </button>
        </div>

        {/* 해시태그 버튼 그룹 */}
        <div className="news-search__hashtags">
          <span className="news-search__hashtags-label">빠른 검색:</span>
          {HASHTAGS.map((hashtag) => (
            <button
              key={hashtag.id}
              className={`news-search__hashtag ${
                selectedHashtag === hashtag.value ? 'active' : ''
              }`}
              onClick={() => handleHashtagClick(hashtag)}
              disabled={isSearching}
            >
              {hashtag.label}
            </button>
          ))}
        </div>

        {/* 정렬 드롭다운 */}
        <div className="news-search__sort">
          <label htmlFor="sort-select" className="news-search__sort-label">
            정렬:
          </label>
          <select
            id="sort-select"
            className="news-search__sort-select"
            value={sortOrder}
            onChange={handleSortChange}
            disabled={isSearching}
          >
            {SORT_OPTIONS.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </div>
      </div>
    </div>
  );
}

NewsSearch.propTypes = {
  onSearchResults: PropTypes.func,
  onLoading: PropTypes.func,
  onError: PropTypes.func,
};

export default NewsSearch;
