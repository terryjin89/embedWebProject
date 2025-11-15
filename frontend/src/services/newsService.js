import axios from 'axios';

// 백엔드 API URL
const NEWS_API_URL = '/api/news';

// Axios 인스턴스 생성
const newsAPI = axios.create({
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 뉴스 서비스 객체
const newsService = {
  /**
   * 뉴스 검색
   * @param {Object} params - 검색 파라미터
   * @param {string} params.company - 기업명 (필수)
   * @param {string} params.hashtag - 해시태그 (선택)
   * @param {number} params.page - 페이지 번호 (기본값: 1)
   * @param {number} params.size - 페이지당 항목 수 (기본값: 10)
   * @param {string} params.sort - 정렬 기준 (sim: 관련도순, date: 최신순, 기본값: date)
   * @returns {Promise<Object>} { items: [], total: number, display: number, start: number }
   */
  searchNews: async (params = {}) => {
    try {
      const {
        company,
        hashtag = '',
        page = 1,
        size = 10,
        sort = 'date', // date: 최신순, sim: 관련도순
      } = params;

      // 기업명이 없으면 에러
      if (!company || company.trim() === '') {
        throw new Error('기업명을 입력해주세요');
      }

      // 검색 쿼리 생성
      const query = hashtag ? `${company} ${hashtag}` : company;

      // 백엔드 API 호출
      const response = await newsAPI.get(`${NEWS_API_URL}/search`, {
        params: {
          query,
          display: size,
          start: (page - 1) * size + 1,
          sort,
        },
      });

      return response.data;
    } catch (error) {
      console.error('뉴스 검색 실패:', error);

      // 백엔드가 아직 구현되지 않은 경우 목업 데이터 반환
      if (error.response?.status === 404 || error.response?.status === 500 || error.code === 'ERR_NETWORK') {
        console.log('백엔드 API가 아직 구현되지 않았습니다. 목업 데이터를 사용합니다.');
        return newsService.getMockNewsList(params);
      }

      throw error;
    }
  },

  /**
   * 목업 뉴스 데이터 생성
   * @param {Object} params - 검색 파라미터
   * @returns {Object} 목업 뉴스 데이터
   */
  getMockNewsList: (params = {}) => {
    const { company = '삼성전자', hashtag = '', page = 1, size = 10 } = params;

    const mockItems = [
      {
        title: `<b>${company}</b> 2025년 1분기 실적 발표`,
        originallink: 'https://example.com/news/1',
        link: 'https://example.com/news/1',
        description: `${company}이 2025년 1분기 실적을 발표했습니다. 매출 15조원, 영업이익 2.5조원을 기록했습니다...`,
        pubDate: 'Thu, 15 Nov 2025 10:30:00 +0900',
      },
      {
        title: `<b>${company}</b> 신제품 출시 예정`,
        originallink: 'https://example.com/news/2',
        link: 'https://example.com/news/2',
        description: `${company}이 차세대 반도체 신제품을 출시할 예정입니다. 업계 전문가들은...`,
        pubDate: 'Wed, 14 Nov 2025 14:20:00 +0900',
      },
      {
        title: `<b>${company}</b> 투자 유치 성공`,
        originallink: 'https://example.com/news/3',
        link: 'https://example.com/news/3',
        description: `${company}이 주요 투자사로부터 5000억원 규모의 투자를 유치했습니다...`,
        pubDate: 'Tue, 13 Nov 2025 09:15:00 +0900',
      },
      {
        title: `<b>${company}</b> 인사 발표`,
        originallink: 'https://example.com/news/4',
        link: 'https://example.com/news/4',
        description: `${company}이 신임 CEO를 발표했습니다. 새로운 경영진의 향후 전략이 주목받고 있습니다...`,
        pubDate: 'Mon, 12 Nov 2025 16:45:00 +0900',
      },
      {
        title: `<b>${company}</b> 시장 점유율 확대`,
        originallink: 'https://example.com/news/5',
        link: 'https://example.com/news/5',
        description: `${company}이 글로벌 시장에서 점유율을 확대하고 있습니다. 최근 발표에 따르면...`,
        pubDate: 'Sun, 11 Nov 2025 11:30:00 +0900',
      },
    ];

    // 해시태그에 따라 필터링
    let filteredItems = mockItems;
    if (hashtag) {
      filteredItems = mockItems.filter((item) => {
        const lowerTitle = item.title.toLowerCase();
        const lowerDesc = item.description.toLowerCase();
        const lowerHashtag = hashtag.toLowerCase().replace('#', '');
        return lowerTitle.includes(lowerHashtag) || lowerDesc.includes(lowerHashtag);
      });
    }

    const total = filteredItems.length;
    const start = (page - 1) * size;
    const items = filteredItems.slice(start, start + size);

    return {
      lastBuildDate: new Date().toISOString(),
      total,
      start: start + 1,
      display: items.length,
      items,
    };
  },
};

export default newsService;
