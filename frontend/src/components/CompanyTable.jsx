import { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import companyService from '../services/companyService';
import './CompanyTable.css';

function CompanyTable() {
  const navigate = useNavigate();

  // 상태 관리
  const [companies, setCompanies] = useState([]);
  const [industries, setIndustries] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [lastUpdated, setLastUpdated] = useState(null);

  // 검색 및 필터 상태
  const [searchInput, setSearchInput] = useState('');        // 검색 입력값 (실제 input value)
  const [searchKeyword, setSearchKeyword] = useState('');    // 검색 실행값 (API 호출 시 사용)
  const [selectedIndustry, setSelectedIndustry] = useState('');

  // 페이지네이션 상태
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [total, setTotal] = useState(0);
  const limit = 20;

  // 업종 목록 로드
  useEffect(() => {
    const loadIndustries = async () => {
      try {
        const data = await companyService.getIndustries();
        setIndustries(Array.isArray(data) ? data : []);
      } catch (err) {
        console.error('Failed to load industries:', err);
        // 기본 업종 목록 설정
        setIndustries([{ code: '', name: '전체' }]);
      }
    };

    loadIndustries();
  }, []);

  // 기업 목록 로드
  // 📝 문서 참고: readme/companyInfoFunction.md - "5. 기업고유번호 검색" 섹션 (143-148라인)
  const loadCompanies = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      // 8자리 숫자인 경우 기업고유번호(corpCode)로 DART API를 통해 검색
      // 정규식으로 8자리 숫자 패턴 확인
      const isCorpCode = /^\d{8}$/.test(searchKeyword.trim());

      if (isCorpCode) {
        console.log('기업고유번호로 DART API 검색:', searchKeyword.trim());

        // DART API를 통해 기업 상세 정보 조회 (백엔드 경유)
        const companyDetail = await companyService.getCompanyDetail(searchKeyword.trim());

        // 응답이 성공이면 배열로 변환하여 표시
        if (companyDetail && companyDetail.status === '000') {
          setCompanies([companyDetail]);
          setTotal(1);
          setTotalPages(1);
          setCurrentPage(1);
        } else {
          // 검색 결과 없음
          setCompanies([]);
          setTotal(0);
          setTotalPages(1);
          setError('해당 기업고유번호로 등록된 기업을 찾을 수 없습니다.');
        }
      } else {
        // 일반 검색 (기업명, 종목명, 종목코드)
        const data = await companyService.getCompanies({
          page: currentPage,
          limit,
          search: searchKeyword,
          industry: selectedIndustry,
        });

        setCompanies(data.companies || []);
        setTotal(data.total || 0);
        setTotalPages(data.totalPages || 1);
      }

      setLastUpdated(new Date());
    } catch (err) {
      console.error('Failed to load companies:', err);
      setError('기업 정보를 불러오는데 실패했습니다.');
      setCompanies([]);
      setTotal(0);
      setTotalPages(1);
    } finally {
      setLoading(false);
    }
  }, [currentPage, searchKeyword, selectedIndustry]);

  // 컴포넌트 마운트 및 필터 변경 시 데이터 로드
  useEffect(() => {
    loadCompanies();
  }, [loadCompanies]);

  // 새로고침 버튼 핸들러
  const handleRefresh = () => {
    loadCompanies();
  };

  // 검색어 입력 핸들러 (입력값만 업데이트, API 호출 안 함)
  const handleSearchChange = (e) => {
    setSearchInput(e.target.value);
  };

  // 검색 실행 핸들러 (버튼 클릭 또는 엔터키)
  const handleSearch = () => {
    setSearchKeyword(searchInput);
    setCurrentPage(1); // 검색 시 첫 페이지로 이동
  };

  // 엔터키 입력 핸들러
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  // 업종 필터 변경 핸들러
  const handleIndustryChange = (e) => {
    setSelectedIndustry(e.target.value);
    setCurrentPage(1); // 필터 변경 시 첫 페이지로 이동
  };

  // 페이지 변경 핸들러
  const handlePageChange = (page) => {
    if (page >= 1 && page <= totalPages) {
      setCurrentPage(page);
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  };

  // 행 클릭 핸들러 (상세 페이지 이동)
  const handleRowClick = (company) => {
    console.log('기업 상세 페이지 이동:', company.corp_name, company.corp_code);
    navigate(`/companies/${company.corp_code}`);
  };

  // 관심기업 등록/해제 핸들러
  const handleFavoriteToggle = async (e, company) => {
    e.stopPropagation(); // 행 클릭 이벤트 전파 방지

    try {
      if (company.isFavorite) {
        await companyService.removeFromFavorites(company.stock_code);
      } else {
        await companyService.addToFavorites(
          company.stock_code,
          company.corp_name
        );
      }

      // 목록 새로고침
      loadCompanies();
    } catch (err) {
      console.error('Favorite toggle error:', err);
      alert('관심기업 등록/해제에 실패했습니다.');
    }
  };

  // 페이지네이션 렌더링
  const renderPagination = () => {
    const pageNumbers = [];
    const maxVisiblePages = 5;
    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

    // 시작 페이지 조정
    if (endPage - startPage < maxVisiblePages - 1) {
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pageNumbers.push(i);
    }

    return (
      <div className="pagination">
        <button
          className="pagination-btn"
          onClick={() => handlePageChange(1)}
          disabled={currentPage === 1}
        >
          &lt;&lt;
        </button>
        <button
          className="pagination-btn"
          onClick={() => handlePageChange(currentPage - 1)}
          disabled={currentPage === 1}
        >
          &lt;
        </button>

        {pageNumbers.map((page) => (
          <button
            key={page}
            className={`pagination-btn ${
              currentPage === page ? 'active' : ''
            }`}
            onClick={() => handlePageChange(page)}
          >
            {page}
          </button>
        ))}

        <button
          className="pagination-btn"
          onClick={() => handlePageChange(currentPage + 1)}
          disabled={currentPage === totalPages}
        >
          &gt;
        </button>
        <button
          className="pagination-btn"
          onClick={() => handlePageChange(totalPages)}
          disabled={currentPage === totalPages}
        >
          &gt;&gt;
        </button>
      </div>
    );
  };

  // 로딩 상태
  if (loading && companies.length === 0) {
    return (
      <div className="company-table-container">
        <div className="loading-message">기업 정보를 불러오는 중...</div>
      </div>
    );
  }

  // 에러 상태
  if (error) {
    return (
      <div className="company-table-container">
        <div className="error-message">
          <p>{error}</p>
          <button className="btn-refresh" onClick={handleRefresh}>
            다시 시도
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="company-table-container">
      {/* 헤더 */}
      <div className="table-header">
        <div className="header-left">
          <h2 className="table-title">기업 정보</h2>
          {lastUpdated && (
            <span className="last-updated">
              최종 업데이트: {lastUpdated.toLocaleTimeString('ko-KR')}
            </span>
          )}
        </div>
        <button className="btn-refresh" onClick={handleRefresh}>
          <span className="refresh-icon">↻</span> 새로고침
        </button>
      </div>

      {/* 검색 및 필터 */}
      <div className="table-controls">
        <div className="search-box">
          <input
            type="text"
            placeholder="기업고유번호로 검색..."
            value={searchInput}
            onChange={handleSearchChange}
            onKeyPress={handleKeyPress}
            className="search-input"
          />
          <button
            className="btn-search"
            onClick={handleSearch}
            title="검색"
          >
            🔍
          </button>
        </div>

        <select
          value={selectedIndustry}
          onChange={handleIndustryChange}
          className="industry-filter"
        >
          {industries.map((industry) => (
            <option key={industry.code} value={industry.code}>
              {industry.name}
            </option>
          ))}
        </select>
      </div>

      {/* 테이블 */}
      <div className="table-wrapper">
        <table className="company-table">
          <thead>
            <tr>
              <th>기업명</th>
              <th>종목명</th>
              <th>종목코드</th>
              <th>대표자</th>
              <th>업종</th>
              <th>설립일</th>
              <th>관심기업</th>
            </tr>
          </thead>
          <tbody>
            {companies.length === 0 ? (
              <tr>
                <td colSpan="7" className="no-data">
                  검색 결과가 없습니다.
                </td>
              </tr>
            ) : (
              companies.map((company, index) => (
                <tr
                  key={`${company.corp_code}-${index}`}
                  className={`table-row ${index % 2 === 0 ? 'even' : 'odd'}`}
                  onClick={() => handleRowClick(company)}
                >
                  <td className="company-name">{company.corp_name}</td>
                  <td className="stock-name">{company.stock_name || '-'}</td>
                  <td className="stock-code">{company.stock_code || '-'}</td>
                  <td className="ceo-name">{company.ceo_nm || '-'}</td>
                  <td className="industry-code">{company.induty_code || '-'}</td>
                  <td className="est-date">
                    {company.est_dt
                      ? `${company.est_dt.substring(0, 4)}-${company.est_dt.substring(4, 6)}-${company.est_dt.substring(6, 8)}`
                      : '-'}
                  </td>
                  <td className="favorite-cell">
                    <button
                      className={`btn-favorite ${
                        company.isFavorite ? 'active' : ''
                      }`}
                      onClick={(e) => handleFavoriteToggle(e, company)}
                      title={
                        company.isFavorite
                          ? '관심기업 해제'
                          : '관심기업 등록'
                      }
                    >
                      ⭐
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* 푸터 및 페이지네이션 */}
      {companies.length > 0 && (
        <>
          <div className="table-footer">
            <p className="data-count">
              총 {total}개 기업 (현재 {currentPage}/{totalPages} 페이지)
            </p>
            <p className="data-source">출처: DART 전자공시시스템</p>
          </div>

          {renderPagination()}
        </>
      )}
    </div>
  );
}

export default CompanyTable;
