import axios from 'axios';

// DART 전자공시 API 설정
const DART_API_KEY = 'd76b2823154aff2001264dd25f0cc7bf256c6c7b';
const DART_API_BASE_URL = 'https://opendart.fss.or.kr/api';

// 백엔드 API URL (프록시 사용)
const BACKEND_API_URL = '/api/companies';

// Axios 인스턴스 생성
const companyAPI = axios.create({
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 기업 서비스 객체
const companyService = {
  /**
   * 기업 목록 조회 (페이지네이션, 검색, 필터링)
   * @param {Object} params - 조회 파라미터
   * @param {number} params.page - 페이지 번호 (기본값: 1)
   * @param {number} params.limit - 페이지당 항목 수 (기본값: 20)
   * @param {string} params.search - 검색어 (기업명)
   * @param {string} params.industry - 업종 코드
   * @returns {Promise<Object>} { companies: [], total: number, page: number, totalPages: number }
   */
  getCompanies: async (params = {}) => {
    try {
      const { page = 1, limit = 20, search = '', industry = '' } = params;

      // 백엔드 API 호출 (실제 구현 시)
      const response = await companyAPI.get(BACKEND_API_URL, {
        params: {
          page,
          limit,
          search,
          industry,
        },
      });

      // 응답 데이터 검증
      if (response.data && response.data.companies && Array.isArray(response.data.companies)) {
        return response.data;
      } else {
        // 유효하지 않은 응답이면 목업 데이터 사용
        console.log('백엔드 API 응답이 유효하지 않습니다. 목업 데이터를 사용합니다.');
        return companyService.getMockCompanyList(params);
      }
    } catch (error) {
      console.error('Company list API error:', error);

      // 개발 환경: 목업 데이터 반환 (백엔드 API 없을 때)
      console.log('백엔드 API가 없으므로 목업 데이터를 사용합니다.');
      return companyService.getMockCompanyList(params);
    }
  },

  /**
   * 기업 상세 정보 조회 (DART API 직접 호출)
   * @param {string} corpCode - 고유번호 (8자리)
   * @returns {Promise<Object>} 기업 상세 정보
   */
  getCompanyDetail: async (corpCode) => {
    try {
      const response = await axios.get(`${DART_API_BASE_URL}/company.json`, {
        params: {
          crtfc_key: DART_API_KEY,
          corp_code: corpCode,
        },
      });

      if (response.data.status === '000') {
        return response.data;
      } else {
        throw new Error(response.data.message || '기업 정보를 찾을 수 없습니다');
      }
    } catch (error) {
      console.error('Company detail API error:', error);
      throw error;
    }
  },

  /**
   * 기업 검색
   * @param {string} keyword - 검색 키워드
   * @returns {Promise<Array>} 검색 결과 배열
   */
  searchCompanies: async (keyword) => {
    try {
      return await companyService.getCompanies({ search: keyword, limit: 50 });
    } catch (error) {
      console.error('Company search error:', error);
      throw error;
    }
  },

  /**
   * 업종 목록 조회
   * @returns {Promise<Array>} 업종 목록 [{ code: string, name: string }]
   */
  getIndustries: async () => {
    try {
      // 백엔드 API 호출 (실제 구현 시)
      const response = await companyAPI.get(`${BACKEND_API_URL}/industries`);

      // 응답 데이터 검증
      if (response.data && Array.isArray(response.data)) {
        return response.data;
      } else {
        // 유효하지 않은 응답이면 목업 데이터 사용
        console.log('백엔드 API 응답이 유효하지 않습니다. 목업 데이터를 사용합니다.');
        return companyService.getMockIndustries();
      }
    } catch (error) {
      console.error('Industries API error:', error);

      // 개발 환경: 목업 데이터 반환 (백엔드 API 없을 때)
      return companyService.getMockIndustries();
    }
  },

  /**
   * 관심기업 등록
   * @param {string} stockCode - 종목코드
   * @param {string} companyName - 기업명
   * @returns {Promise<Object>} 등록 결과
   */
  addToFavorites: async (stockCode, companyName) => {
    try {
      const response = await companyAPI.post('/api/favorites', {
        stockCode,
        companyName,
      });
      return response.data;
    } catch (error) {
      console.error('Add to favorites error:', error);
      throw error;
    }
  },

  /**
   * 관심기업 삭제
   * @param {string} stockCode - 종목코드
   * @returns {Promise<Object>} 삭제 결과
   */
  removeFromFavorites: async (stockCode) => {
    try {
      const response = await companyAPI.delete(`/api/favorites/${stockCode}`);
      return response.data;
    } catch (error) {
      console.error('Remove from favorites error:', error);
      throw error;
    }
  },

  /**
   * 관심기업 목록 조회
   * @returns {Promise<Array>} 관심기업 목록
   */
  getFavorites: async () => {
    try {
      const response = await companyAPI.get('/api/favorites');
      return response.data;
    } catch (error) {
      console.error('Get favorites error:', error);
      throw error;
    }
  },

  /**
   * 개발용 목업 데이터 - 기업 목록
   * @param {Object} params - 조회 파라미터
   * @returns {Object} 목업 기업 목록
   */
  getMockCompanyList: (params = {}) => {
    const { page = 1, limit = 20, search = '', industry = '' } = params;

    // 목업 데이터
    const allCompanies = [
      {
        corp_code: '00126380',
        corp_name: '삼성전자(주)',
        stock_name: '삼성전자',
        stock_code: '005930',
        ceo_nm: '전영현',
        induty_code: '264',
        est_dt: '19690113',
        isFavorite: false,
      },
      {
        corp_code: '00164779',
        corp_name: 'SK하이닉스(주)',
        stock_name: 'SK하이닉스',
        stock_code: '000660',
        ceo_nm: '곽노정',
        induty_code: '264',
        est_dt: '19960322',
        isFavorite: false,
      },
      {
        corp_code: '00401731',
        corp_name: '현대자동차(주)',
        stock_name: '현대차',
        stock_code: '005380',
        ceo_nm: '장재훈',
        induty_code: '304',
        est_dt: '19670222',
        isFavorite: false,
      },
      {
        corp_code: '00176701',
        corp_name: 'LG전자(주)',
        stock_name: 'LG전자',
        stock_code: '066570',
        ceo_nm: '조주완',
        induty_code: '264',
        est_dt: '19580101',
        isFavorite: false,
      },
      {
        corp_code: '00782756',
        corp_name: 'NAVER(주)',
        stock_name: 'NAVER',
        stock_code: '035420',
        ceo_nm: '최수연',
        induty_code: '639',
        est_dt: '19990602',
        isFavorite: false,
      },
      {
        corp_code: '00356370',
        corp_name: '카카오(주)',
        stock_name: '카카오',
        stock_code: '035720',
        ceo_nm: '정신아',
        induty_code: '639',
        est_dt: '19950204',
        isFavorite: false,
      },
    ];

    // 검색 필터링
    let filteredCompanies = allCompanies;
    if (search) {
      filteredCompanies = filteredCompanies.filter(
        (company) =>
          company.corp_name.includes(search) ||
          company.stock_name.includes(search) ||
          company.stock_code.includes(search)
      );
    }

    // 업종 필터링
    if (industry) {
      filteredCompanies = filteredCompanies.filter(
        (company) => company.induty_code === industry
      );
    }

    // 페이지네이션
    const total = filteredCompanies.length;
    const totalPages = Math.ceil(total / limit);
    const startIndex = (page - 1) * limit;
    const endIndex = startIndex + limit;
    const companies = filteredCompanies.slice(startIndex, endIndex);

    return {
      companies,
      total,
      page,
      limit,
      totalPages,
    };
  },

  /**
   * 개발용 목업 데이터 - 업종 목록
   * @returns {Array} 업종 목록
   */
  getMockIndustries: () => {
    return [
      { code: '', name: '전체' },
      { code: '264', name: '전자부품/반도체' },
      { code: '304', name: '자동차' },
      { code: '639', name: 'IT서비스' },
      { code: '241', name: '화학' },
      { code: '291', name: '기계' },
    ];
  },
};

export default companyService;
