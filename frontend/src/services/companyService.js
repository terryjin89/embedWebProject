import axios from 'axios';
import axiosInstance from './axiosInstance';

// DART 전자공시 API 설정
const DART_API_KEY = 'd76b2823154aff2001264dd25f0cc7bf256c6c7b';
const DART_API_BASE_URL = 'https://opendart.fss.or.kr/api';

// 백엔드 API URL (프록시 사용)
const BACKEND_API_URL = '/companies';

// Axios 인스턴스 (공통 인터셉터 포함)
const companyAPI = axiosInstance;

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
      // 백엔드는 page=0부터 시작하므로 -1 처리
      // 빈 문자열은 undefined로 변환 (백엔드에서 null로 처리되도록)
      const requestParams = {
        page: page - 1,  // 프론트는 1부터, 백엔드는 0부터 시작
        size: limit,     // 백엔드는 'size' 파라미터 사용
      };

      // 검색어가 있을 때만 keyword 파라미터 추가
      if (search && search.trim()) {
        requestParams.keyword = search.trim();
      }

      // 업종 코드가 있을 때만 indutyCode 파라미터 추가
      if (industry && industry.trim()) {
        requestParams.indutyCode = industry.trim();
      }

      const response = await companyAPI.get(BACKEND_API_URL, { params: requestParams });

      // 응답 데이터 검증
      if (response.data && response.data.companies && Array.isArray(response.data.companies)) {
        // 백엔드 응답(camelCase)을 프론트엔드가 기대하는 형식(snake_case)으로 변환
        const companies = response.data.companies.map(company => ({
          corp_code: company.corpCode,
          corp_name: company.corpName,
          corp_name_eng: company.corpNameEng,
          stock_name: company.stockName,
          stock_code: company.stockCode,
          ceo_nm: company.ceoNm,
          corp_cls: company.corpCls,
          corp_cls_name: company.corpClsName,
          jurir_no: company.jurirNo,
          bizr_no: company.bizrNo,
          adres: company.adres,
          hm_url: company.hmUrl,
          ir_url: company.irUrl,
          phn_no: company.phnNo,
          fax_no: company.faxNo,
          induty_code: company.indutyCode,
          est_dt: company.estDt,
          est_dt_formatted: company.estDtFormatted,
          acc_mt: company.accMt,
          isFavorite: company.isFavorite || false,
        }));

        return {
          companies,
          total: response.data.totalElements,  // totalElements -> total
          totalPages: response.data.totalPages,
          page: response.data.currentPage + 1,  // 0-based -> 1-based
        };
      } else {
        throw new Error('Invalid API response');
      }
    } catch (error) {
      console.error('Company list API error:', error);
      throw error;
    }
  },

  /**
   * 기업 상세 정보 조회 (백엔드 API 호출)
   * 📝 문서 참고: readme/companyInfoFunction.md - "6. 기업 상세 정보 조회" 섹션 (150-158라인)
   *
   * @param {string} corpCode - 고유번호 (8자리)
   * @returns {Promise<Object>} 기업 상세 정보
   */
  getCompanyDetail: async (corpCode) => {
    try {
      // 백엔드 API 호출: GET /api/companies/{corpCode}
      // 데이터 소스: 데이터베이스 (백엔드에서 조회)
      const response = await companyAPI.get(`${BACKEND_API_URL}/${corpCode}`);

      // 백엔드 응답(camelCase)을 프론트엔드가 기대하는 형식(snake_case)으로 변환
      const company = response.data;
      return {
        status: '000',
        message: '정상',
        corp_code: company.corpCode,
        corp_name: company.corpName,
        corp_name_eng: company.corpNameEng,
        stock_name: company.stockName,
        stock_code: company.stockCode,
        ceo_nm: company.ceoNm,
        corp_cls: company.corpCls,
        jurir_no: company.jurirNo,
        bizr_no: company.bizrNo,
        adres: company.adres,
        hm_url: company.hmUrl,
        ir_url: company.irUrl,
        phn_no: company.phnNo,
        fax_no: company.faxNo,
        induty_code: company.indutyCode,
        est_dt: company.estDt,
        acc_mt: company.accMt,
      };
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
      const response = await companyAPI.get(`${BACKEND_API_URL}/industries`);
      if (response.data && Array.isArray(response.data)) {
        return response.data;
      } else {
        throw new Error('Invalid API response');
      }
    } catch (error) {
      console.error('Industries API error:', error);
      throw error;
    }
  },

  /**
   * 관심기업 등록
   * @param {string} stockCode - 종목코드
   * @param {string} corpCode - 기업 고유번호
   * @returns {Promise<Object>} 등록 결과
   */
  addToFavorites: async (stockCode, corpCode) => {
    try {
      const response = await companyAPI.post('/favorites', {
        stockCode,
        corpCode,
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
      const response = await companyAPI.delete(`/favorites/${stockCode}`);
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
      const response = await companyAPI.get('/favorites');
      return response.data;
    } catch (error) {
      console.error('Get favorites error:', error);
      throw error;
    }
  },

  /**
   * 공시 목록 조회 (백엔드 API 호출)
   * 📝 문서 참고: readme/companyInfoFunction.md - "7. 공시 정보 조회" 섹션 (160-175라인)
   *
   * @param {string} corpCode - 고유번호 (8자리)
   * @param {Object} params - 조회 파라미터
   * @param {string} params.bgn_de - 시작일 (YYYYMMDD), 기본값: 20240101
   * @param {string} params.end_de - 종료일 (YYYYMMDD), 기본값: 20241231
   * @param {string} params.pblntf_ty - 공시유형 (A~J)
   * @param {number} params.page_no - 페이지 번호
   * @param {number} params.page_count - 페이지당 건수
   * @returns {Promise<Object>} { list: [], total_count: number, page_no: number, total_page: number }
   */
  getDisclosures: async (corpCode, params = {}) => {
    try {
      const {
        bgn_de = '20240101',
        end_de = '20241231',
        pblntf_ty = '',
        page_no = 1,
        page_count = 10,
      } = params;

      // 백엔드 API 호출: GET /api/companies/{corpCode}/disclosures
      // 데이터 소스: DART API (백엔드 프록시를 통해 실시간 조회)
      const requestParams = {
        pageNo: page_no,
        pageCount: page_count,
      };

      // 시작일이 있으면 추가
      if (bgn_de) {
        requestParams.bgnDe = bgn_de;
      }

      // 종료일이 있으면 추가
      if (end_de) {
        requestParams.endDe = end_de;
      }

      // 공시유형이 있으면 추가
      if (pblntf_ty) {
        requestParams.pblntfTy = pblntf_ty;
      }

      const response = await companyAPI.get(`${BACKEND_API_URL}/${corpCode}/disclosures`, {
        params: requestParams,
      });

      if (response.data.status === '000') {
        return response.data;
      } else {
        throw new Error(response.data.message || '공시 정보를 찾을 수 없습니다');
      }
    } catch (error) {
      console.error('Disclosures API error:', error);
      throw error;
    }
  },

  /**
   * DART 원문 링크 생성
   * @param {string} rceptNo - 접수번호 (14자리)
   * @returns {string} DART 원문 URL
   */
  getDartUrl: (rceptNo) => {
    return `https://dart.fss.or.kr/dsaf001/main.do?rcpNo=${rceptNo}`;
  },

  /**
   * 공시 유형 목록
   * @returns {Array} 공시 유형 목록 [{ code: string, name: string }]
   */
  getDisclosureTypes: () => {
    return [
      { code: '', name: '전체' },
      { code: 'A', name: '정기공시' },
      { code: 'B', name: '주요사항보고' },
      { code: 'C', name: '발행공시' },
      { code: 'D', name: '지분공시' },
      { code: 'E', name: '기타공시' },
      { code: 'F', name: '외부감사관련' },
      { code: 'G', name: '펀드공시' },
      { code: 'H', name: '자산유동화' },
      { code: 'I', name: '거래소공시' },
      { code: 'J', name: '공정위공시' },
    ];
  },
};

export default companyService;
