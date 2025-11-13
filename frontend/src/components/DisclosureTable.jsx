import { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import companyService from '../services/companyService';
import './DisclosureTable.css';

function DisclosureTable({ corpCode }) {
  // 상태 관리
  const [disclosures, setDisclosures] = useState([]);
  const [disclosureTypes, setDisclosureTypes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 필터 상태
  const [selectedType, setSelectedType] = useState('');

  // 페이지네이션 상태
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [totalCount, setTotalCount] = useState(0);
  const pageCount = 10;

  // 공시 유형 목록 로드
  useEffect(() => {
    const types = companyService.getDisclosureTypes();
    setDisclosureTypes(types);
  }, []);

  // 공시 목록 로드
  useEffect(() => {
    const loadDisclosures = async () => {
      setLoading(true);
      setError(null);

      try {
        const data = await companyService.getDisclosures(corpCode, {
          pblntf_ty: selectedType,
          page_no: currentPage,
          page_count: pageCount,
        });

        setDisclosures(data.list || []);
        setTotalCount(data.total_count || 0);
        setTotalPages(data.total_page || 1);
      } catch (err) {
        console.error('Failed to load disclosures:', err);
        setError('공시 정보를 불러오는데 실패했습니다.');
        setDisclosures([]);
      } finally {
        setLoading(false);
      }
    };

    if (corpCode) {
      loadDisclosures();
    }
  }, [corpCode, selectedType, currentPage]);

  // 공시 유형 필터 변경 핸들러
  const handleTypeChange = (e) => {
    setSelectedType(e.target.value);
    setCurrentPage(1); // 필터 변경 시 첫 페이지로 이동
  };

  // 페이지 변경 핸들러
  const handlePageChange = (page) => {
    if (page >= 1 && page <= totalPages) {
      setCurrentPage(page);
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  };

  // DART 원문 링크 열기
  const handleDartLink = (rceptNo) => {
    const dartUrl = companyService.getDartUrl(rceptNo);
    window.open(dartUrl, '_blank', 'noopener,noreferrer');
  };

  // 날짜 포맷팅
  const formatDate = (dateStr) => {
    if (!dateStr || dateStr.length !== 8) return dateStr;
    return `${dateStr.substring(0, 4)}-${dateStr.substring(4, 6)}-${dateStr.substring(6, 8)}`;
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
  if (loading && disclosures.length === 0) {
    return (
      <div className="disclosure-table-container">
        <div className="loading-message">공시 정보를 불러오는 중...</div>
      </div>
    );
  }

  // 에러 상태
  if (error) {
    return (
      <div className="disclosure-table-container">
        <div className="error-message">{error}</div>
      </div>
    );
  }

  return (
    <div className="disclosure-table-container">
      {/* 필터 */}
      <div className="table-controls">
        <select
          value={selectedType}
          onChange={handleTypeChange}
          className="type-filter"
        >
          {disclosureTypes.map((type) => (
            <option key={type.code} value={type.code}>
              {type.name}
            </option>
          ))}
        </select>
        <span className="result-count">
          총 {totalCount}건의 공시
        </span>
      </div>

      {/* 테이블 */}
      <div className="table-wrapper">
        <table className="disclosure-table">
          <thead>
            <tr>
              <th>접수일자</th>
              <th>보고서명</th>
              <th>제출인</th>
              <th>비고</th>
              <th>원문</th>
            </tr>
          </thead>
          <tbody>
            {disclosures.length === 0 ? (
              <tr>
                <td colSpan="5" className="no-data">
                  공시 정보가 없습니다.
                </td>
              </tr>
            ) : (
              disclosures.map((disclosure, index) => (
                <tr
                  key={`${disclosure.rcept_no}-${index}`}
                  className={`table-row ${index % 2 === 0 ? 'even' : 'odd'}`}
                >
                  <td className="rcept-dt">{formatDate(disclosure.rcept_dt)}</td>
                  <td className="report-nm">{disclosure.report_nm}</td>
                  <td className="flr-nm">{disclosure.flr_nm || '-'}</td>
                  <td className="rm">{disclosure.rm || '-'}</td>
                  <td className="dart-link-cell">
                    <button
                      className="btn-dart-link"
                      onClick={() => handleDartLink(disclosure.rcept_no)}
                      title="DART 원문 보기"
                    >
                      원문보기
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* 페이지네이션 */}
      {disclosures.length > 0 && (
        <>
          <div className="table-footer">
            <p className="page-info">
              {currentPage} / {totalPages} 페이지
            </p>
          </div>

          {renderPagination()}
        </>
      )}
    </div>
  );
}

DisclosureTable.propTypes = {
  corpCode: PropTypes.string.isRequired,
};

export default DisclosureTable;
