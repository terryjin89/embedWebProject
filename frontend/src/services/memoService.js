import axios from 'axios';

// 백엔드 API URL
const MEMO_API_URL = '/api/favorites';

// Axios 인스턴스 생성
const memoAPI = axios.create({
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 메모 서비스 객체
const memoService = {
  /**
   * 메모 조회
   * @param {string} stockCode - 종목 코드
   * @returns {Promise<Object>} { content: string, updatedAt: string }
   */
  getMemo: async (stockCode) => {
    try {
      if (!stockCode || stockCode.trim() === '') {
        throw new Error('종목 코드를 입력해주세요');
      }

      const response = await memoAPI.get(`${MEMO_API_URL}/${stockCode}/memo`);
      return response.data;
    } catch (error) {
      console.error('메모 조회 실패:', error);

      // 백엔드가 아직 구현되지 않은 경우 목업 데이터 반환
      if (error.response?.status === 404 || error.response?.status === 500 || error.code === 'ERR_NETWORK') {
        console.log('백엔드 API가 아직 구현되지 않았습니다. 목업 데이터를 사용합니다.');
        return memoService.getMockMemo(stockCode);
      }

      throw error;
    }
  },

  /**
   * 메모 저장
   * @param {string} stockCode - 종목 코드
   * @param {string} content - 메모 내용
   * @returns {Promise<Object>} { success: boolean, message: string, updatedAt: string }
   */
  saveMemo: async (stockCode, content) => {
    try {
      if (!stockCode || stockCode.trim() === '') {
        throw new Error('종목 코드를 입력해주세요');
      }

      if (content && content.length > 2000) {
        throw new Error('메모는 최대 2000자까지 입력할 수 있습니다');
      }

      const response = await memoAPI.post(`${MEMO_API_URL}/${stockCode}/memo`, {
        content: content || '',
      });

      return response.data;
    } catch (error) {
      console.error('메모 저장 실패:', error);

      // 백엔드가 아직 구현되지 않은 경우 목업 데이터 반환
      if (error.response?.status === 404 || error.response?.status === 500 || error.code === 'ERR_NETWORK') {
        console.log('백엔드 API가 아직 구현되지 않았습니다. 목업 저장을 시뮬레이션합니다.');
        return memoService.saveMockMemo(stockCode, content);
      }

      throw error;
    }
  },

  /**
   * 목업 메모 조회
   * @param {string} stockCode - 종목 코드
   * @returns {Object} 목업 메모 데이터
   */
  getMockMemo: (stockCode) => {
    // localStorage에서 메모 가져오기
    const savedMemo = localStorage.getItem(`memo_${stockCode}`);

    if (savedMemo) {
      try {
        return JSON.parse(savedMemo);
      } catch (e) {
        console.error('메모 파싱 실패:', e);
      }
    }

    // 저장된 메모가 없으면 빈 메모 반환
    return {
      content: '',
      updatedAt: null,
    };
  },

  /**
   * 목업 메모 저장
   * @param {string} stockCode - 종목 코드
   * @param {string} content - 메모 내용
   * @returns {Object} 성공 응답
   */
  saveMockMemo: (stockCode, content) => {
    const memo = {
      content: content || '',
      updatedAt: new Date().toISOString(),
    };

    // localStorage에 저장
    localStorage.setItem(`memo_${stockCode}`, JSON.stringify(memo));

    return {
      success: true,
      message: '메모가 성공적으로 저장되었습니다',
      updatedAt: memo.updatedAt,
    };
  },
};

export default memoService;
