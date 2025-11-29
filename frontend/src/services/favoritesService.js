import axios from 'axios';
import axiosInstance from './axiosInstance';
import companyService from './companyService';

// 금융위원회 주식시세정보 API 설정
const STOCK_API_KEY = '8676c1c0ad9eda160d90d5927a4dc21aace7f18de90365e4c993f72a66ce66b7';
const STOCK_API_BASE_URL =
  'https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo';

// 백엔드 API URL
const FAVORITES_API_URL = '/favorites';

// Axios 인스턴스 (공통 인터셉터 포함)
const favoritesAPI = axiosInstance;

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
      throw error;
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

      // 주가 정보 조회 실패 시 null 값 반환
      return {
        currentPrice: null,
        priceChange: null,
        changeRate: null,
        previousClose: null,
        baseDate: null,
      };
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
      throw error;
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

};

export default favoritesService;
