import axios from 'axios';
import companyService from './companyService';

// 금융위원회 주식시세정보 API 설정
const STOCK_API_KEY = '8676c1c0ad9eda160d90d5927a4dc21aace7f18de90365e4c993f72a66ce66b7';
const STOCK_API_BASE_URL =
  'https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo';

// 백엔드 API URL
const FAVORITES_API_URL = '/api/favorites';

// Axios 인스턴스 생성
const favoritesAPI = axios.create({
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 관심기업 서비스 객체
const favoritesService = {
  /**
   * 관심기업 목록 조회 (주가 정보 포함)
   * @returns {Promise<Array>} 관심기업 목록 + 주가 정보
   */
  getFavoritesWithStockInfo: async () => {
    try {
      // 1. 관심기업 목록 조회
      const response = await favoritesAPI.get(FAVORITES_API_URL);
      const favorites = response.data;

      if (!Array.isArray(favorites) || favorites.length === 0) {
        return [];
      }

      // 2. 각 관심기업의 주가 정보 조회 (병렬 처리)
      const favoritesWithStock = await Promise.all(
        favorites.map(async (favorite) => {
          try {
            const stockInfo = await favoritesService.getLatestStockInfo(
              favorite.stockCode
            );

            return {
              ...favorite,
              stockInfo: stockInfo || {
                currentPrice: null,
                priceChange: null,
                changeRate: null,
                previousClose: null,
              },
            };
          } catch (error) {
            console.error(
              `Failed to load stock info for ${favorite.stockCode}:`,
              error
            );

            // 주가 정보 조회 실패 시 null 값으로 반환
            return {
              ...favorite,
              stockInfo: {
                currentPrice: null,
                priceChange: null,
                changeRate: null,
                previousClose: null,
              },
            };
          }
        })
      );

      return favoritesWithStock;
    } catch (error) {
      console.error('Get favorites with stock info error:', error);

      // 개발 환경: 목업 데이터 반환
      console.log('백엔드 API가 없으므로 목업 데이터를 사용합니다.');
      return favoritesService.getMockFavoritesWithStock();
    }
  },

  /**
   * 최신 주가 정보 조회 (30일치 중 최근 데이터)
   * @param {string} stockCode - 종목코드 (6자리)
   * @returns {Promise<Object>} { currentPrice, priceChange, changeRate, previousClose, baseDate }
   */
  getLatestStockInfo: async (stockCode) => {
    try {
      // 오늘 날짜와 30일 전 날짜 계산
      const today = new Date();
      const endDate = favoritesService.formatDate(today);

      const thirtyDaysAgo = new Date();
      thirtyDaysAgo.setDate(today.getDate() - 30);
      const beginDate = favoritesService.formatDate(thirtyDaysAgo);

      // 금융위원회 API 호출
      const response = await axios.get(STOCK_API_BASE_URL, {
        params: {
          serviceKey: STOCK_API_KEY,
          numOfRows: 30, // 30일치
          pageNo: 1,
          resultType: 'json',
          likeSrtnCd: stockCode,
          beginBasDt: beginDate,
          endBasDt: endDate,
        },
      });

      // 응답 데이터 확인
      if (
        response.data &&
        response.data.response &&
        response.data.response.body &&
        response.data.response.body.items &&
        response.data.response.body.items.item
      ) {
        const items = response.data.response.body.items.item;
        const latestData = Array.isArray(items) ? items[0] : items;

        // 주가 정보 반환
        return {
          currentPrice: parseInt(latestData.clpr), // 종가
          priceChange: parseInt(latestData.vs), // 대비
          changeRate: parseFloat(latestData.fltRt), // 등락률
          previousClose: parseInt(latestData.clpr) - parseInt(latestData.vs), // 전일종가 = 현재가 - 대비
          baseDate: latestData.basDt, // 기준일자
        };
      }

      throw new Error('Invalid stock API response');
    } catch (error) {
      console.error('Stock info API error:', error);

      // 개발 환경: 목업 데이터 반환
      return favoritesService.getMockStockInfo(stockCode);
    }
  },

  /**
   * 관심기업 삭제
   * @param {string} stockCode - 종목코드
   * @returns {Promise<Object>} 삭제 결과
   */
  removeFavorite: async (stockCode) => {
    try {
      const response = await favoritesAPI.delete(
        `${FAVORITES_API_URL}/${stockCode}`
      );
      return response.data;
    } catch (error) {
      console.error('Remove favorite error:', error);

      // 개발 환경: 성공 응답 반환
      console.log('백엔드 API가 없으므로 목업 응답을 반환합니다.');
      return { success: true, message: '관심기업이 삭제되었습니다.' };
    }
  },

  /**
   * 관심기업 목록 새로고침
   * @returns {Promise<Array>} 최신 관심기업 목록 + 주가 정보
   */
  refreshFavorites: async () => {
    return await favoritesService.getFavoritesWithStockInfo();
  },

  /**
   * 날짜 포맷팅 (YYYYMMDD)
   * @param {Date} date - 날짜 객체
   * @returns {string} YYYYMMDD 형식 문자열
   */
  formatDate: (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}${month}${day}`;
  },

  /**
   * 가격 포맷팅 (천 단위 콤마)
   * @param {number} price - 가격
   * @returns {string} 포맷팅된 가격
   */
  formatPrice: (price) => {
    if (price === null || price === undefined) {
      return '-';
    }
    return price.toLocaleString('ko-KR');
  },

  /**
   * 변동률 포맷팅 (부호 + %)
   * @param {number} changeRate - 변동률
   * @returns {string} 포맷팅된 변동률
   */
  formatChangeRate: (changeRate) => {
    if (changeRate === null || changeRate === undefined) {
      return '-';
    }

    const sign = changeRate > 0 ? '+' : '';
    return `${sign}${changeRate.toFixed(2)}%`;
  },

  /**
   * 가격 변동 포맷팅 (부호 + 금액)
   * @param {number} priceChange - 가격 변동
   * @returns {string} 포맷팅된 가격 변동
   */
  formatPriceChange: (priceChange) => {
    if (priceChange === null || priceChange === undefined) {
      return '-';
    }

    const sign = priceChange > 0 ? '+' : '';
    return `${sign}${priceChange.toLocaleString('ko-KR')}`;
  },

  /**
   * 변동 방향에 따른 클래스명 반환
   * @param {number} value - 변동값 (가격 변동 또는 변동률)
   * @returns {string} 'up' | 'down' | 'neutral'
   */
  getPriceChangeClass: (value) => {
    if (value === null || value === undefined || value === 0) {
      return 'neutral';
    }
    return value > 0 ? 'up' : 'down';
  },

  /**
   * 개발용 목업 데이터 - 주가 정보
   * @param {string} stockCode - 종목코드
   * @returns {Object} 목업 주가 정보
   */
  getMockStockInfo: (stockCode) => {
    const mockStockData = {
      '005930': {
        // 삼성전자
        currentPrice: 71500,
        priceChange: 1200,
        changeRate: 1.71,
        previousClose: 70300,
        baseDate: '20241114',
      },
      '000660': {
        // SK하이닉스
        currentPrice: 128000,
        priceChange: -2500,
        changeRate: -1.92,
        previousClose: 130500,
        baseDate: '20241114',
      },
      '005380': {
        // 현대차
        currentPrice: 215000,
        priceChange: 5000,
        changeRate: 2.38,
        previousClose: 210000,
        baseDate: '20241114',
      },
      '066570': {
        // LG전자
        currentPrice: 92300,
        priceChange: -800,
        changeRate: -0.86,
        previousClose: 93100,
        baseDate: '20241114',
      },
      '035420': {
        // NAVER
        currentPrice: 187500,
        priceChange: 3500,
        changeRate: 1.9,
        previousClose: 184000,
        baseDate: '20241114',
      },
      '035720': {
        // 카카오
        currentPrice: 43200,
        priceChange: -600,
        changeRate: -1.37,
        previousClose: 43800,
        baseDate: '20241114',
      },
    };

    return (
      mockStockData[stockCode] || {
        currentPrice: null,
        priceChange: null,
        changeRate: null,
        previousClose: null,
        baseDate: null,
      }
    );
  },

  /**
   * 개발용 목업 데이터 - 관심기업 + 주가 정보
   * @returns {Array} 목업 관심기업 목록
   */
  getMockFavoritesWithStock: () => {
    return [
      {
        id: 1,
        stockCode: '005930',
        corpCode: '00126380',
        companyName: '삼성전자(주)',
        stockName: '삼성전자',
        registeredAt: '2024-11-01T10:30:00',
        stockInfo: {
          currentPrice: 71500,
          priceChange: 1200,
          changeRate: 1.71,
          previousClose: 70300,
          baseDate: '20241114',
        },
      },
      {
        id: 2,
        stockCode: '000660',
        corpCode: '00164779',
        companyName: 'SK하이닉스(주)',
        stockName: 'SK하이닉스',
        registeredAt: '2024-11-03T14:20:00',
        stockInfo: {
          currentPrice: 128000,
          priceChange: -2500,
          changeRate: -1.92,
          previousClose: 130500,
          baseDate: '20241114',
        },
      },
      {
        id: 3,
        stockCode: '035420',
        corpCode: '00782756',
        companyName: 'NAVER(주)',
        stockName: 'NAVER',
        registeredAt: '2024-11-05T09:15:00',
        stockInfo: {
          currentPrice: 187500,
          priceChange: 3500,
          changeRate: 1.9,
          previousClose: 184000,
          baseDate: '20241114',
        },
      },
      {
        id: 4,
        stockCode: '035720',
        corpCode: '00356370',
        companyName: '카카오(주)',
        stockName: '카카오',
        registeredAt: '2024-11-07T16:45:00',
        stockInfo: {
          currentPrice: 43200,
          priceChange: -600,
          changeRate: -1.37,
          previousClose: 43800,
          baseDate: '20241114',
        },
      },
      {
        id: 5,
        stockCode: '005380',
        corpCode: '00401731',
        companyName: '현대자동차(주)',
        stockName: '현대차',
        registeredAt: '2024-11-10T11:00:00',
        stockInfo: {
          currentPrice: 215000,
          priceChange: 5000,
          changeRate: 2.38,
          previousClose: 210000,
          baseDate: '20241114',
        },
      },
    ];
  },
};

export default favoritesService;
