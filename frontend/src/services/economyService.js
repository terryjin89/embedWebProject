import axios from 'axios';

// 한국수출입은행 API 설정
// 개발 환경에서는 Vite 프록시 사용
const EXCHANGE_RATE_API_URL = '/api/exchange';
const API_KEY = 'DvYPnli3e0IjaMMxPsqthtkwFfkWt9IW';

// Axios 인스턴스 생성
const economyAPI = axios.create({
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 경제 서비스 객체
const economyService = {
  /**
   * 환율 정보 조회
   * @param {string} searchDate - 조회 날짜 (YYYYMMDD 형식, 선택사항)
   * @returns {Promise} API 응답
   */
  getExchangeRates: async (searchDate) => {
    try {
      // 날짜가 없으면 오늘 날짜 사용
      let date = searchDate || economyService.getTodayDateString();

      // 최대 7일 전까지 시도
      for (let i = 0; i < 7; i++) {
        const response = await economyAPI.get(EXCHANGE_RATE_API_URL, {
          params: {
            authkey: API_KEY,
            searchdate: date,
            data: 'AP01', // AP01: 환율
          },
        });

        // API가 배열을 직접 반환
        if (response.data && Array.isArray(response.data) && response.data.length > 0) {
          return response.data;
        }

        // 데이터가 없으면 하루 전으로 시도
        const dateObj = new Date(
          date.substring(0, 4),
          parseInt(date.substring(4, 6)) - 1,
          parseInt(date.substring(6, 8))
        );
        dateObj.setDate(dateObj.getDate() - 1);
        date = economyService.formatDate(dateObj);
      }

      throw new Error('환율 데이터를 찾을 수 없습니다');
    } catch (error) {
      console.error('Exchange rate API error:', error);
      throw error;
    }
  },

  /**
   * 오늘 날짜를 YYYYMMDD 형식으로 반환
   * @returns {string} YYYYMMDD 형식의 날짜 문자열
   */
  getTodayDateString: () => {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}${month}${day}`;
  },

  /**
   * 날짜 문자열을 YYYYMMDD 형식으로 변환
   * @param {Date} date - Date 객체
   * @returns {string} YYYYMMDD 형식의 날짜 문자열
   */
  formatDate: (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}${month}${day}`;
  },

  /**
   * 전일대비 변화 계산
   * @param {number} current - 현재 값
   * @param {number} previous - 이전 값
   * @returns {Object} { change: 변화량, changePercent: 변화율, direction: 'up' | 'down' | 'same' }
   */
  calculateChange: (current, previous) => {
    if (!current || !previous) {
      return { change: 0, changePercent: 0, direction: 'same' };
    }

    const currentValue = parseFloat(current.replace(/,/g, ''));
    const previousValue = parseFloat(previous.replace(/,/g, ''));

    const change = currentValue - previousValue;
    const changePercent = ((change / previousValue) * 100).toFixed(2);

    let direction = 'same';
    if (change > 0) direction = 'up';
    else if (change < 0) direction = 'down';

    return {
      change: change.toFixed(2),
      changePercent: parseFloat(changePercent),
      direction,
    };
  },

  /**
   * 과거 환율 데이터 조회 (차트용)
   * @param {string} currencyCode - 통화 코드 (예: 'USD', 'JPY(100)')
   * @param {number} days - 조회할 기간 (일)
   * @returns {Promise<Array>} 차트 데이터 배열 [{ date: 'YYYY-MM-DD', rate: number }]
   */
  getHistoricalRates: async (currencyCode, days = 30) => {
    try {
      const chartData = [];
      const today = new Date();

      // 각 날짜별로 환율 데이터 조회
      for (let i = days - 1; i >= 0; i--) {
        const targetDate = new Date(today);
        targetDate.setDate(today.getDate() - i);
        const dateString = economyService.formatDate(targetDate);

        try {
          const response = await economyAPI.get(EXCHANGE_RATE_API_URL, {
            params: {
              authkey: API_KEY,
              searchdate: dateString,
              data: 'AP01',
            },
          });

          // 해당 통화 코드의 환율 찾기
          if (response.data && Array.isArray(response.data)) {
            const currencyData = response.data.find(
              (item) => item.cur_unit === currencyCode
            );

            if (currencyData && currencyData.deal_bas_r) {
              chartData.push({
                date: `${targetDate.getFullYear()}-${String(targetDate.getMonth() + 1).padStart(2, '0')}-${String(targetDate.getDate()).padStart(2, '0')}`,
                rate: parseFloat(currencyData.deal_bas_r.replace(/,/g, '')),
              });
            }
          }
        } catch (error) {
          // 특정 날짜의 데이터가 없어도 계속 진행
          console.warn(`No data for ${dateString}:`, error.message);
        }
      }

      return chartData;
    } catch (error) {
      console.error('Historical rates API error:', error);
      throw error;
    }
  },
};

export default economyService;
