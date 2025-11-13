import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import companyService from '../services/companyService';
import DisclosureTable from '../components/DisclosureTable';
import './CompanyDetailPage.css';

function CompanyDetailPage() {
  const { corpCode } = useParams();
  const navigate = useNavigate();

  // 상태 관리
  const [companyInfo, setCompanyInfo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 기업 상세 정보 로드
  useEffect(() => {
    const loadCompanyInfo = async () => {
      setLoading(true);
      setError(null);

      try {
        const data = await companyService.getCompanyDetail(corpCode);
        setCompanyInfo(data);
      } catch (err) {
        console.error('Failed to load company info:', err);
        setError('기업 정보를 불러오는데 실패했습니다.');
      } finally {
        setLoading(false);
      }
    };

    if (corpCode) {
      loadCompanyInfo();
    }
  }, [corpCode]);

  // 뒤로가기 버튼 핸들러
  const handleBack = () => {
    navigate('/');
  };

  // 로딩 상태
  if (loading) {
    return (
      <div className="company-detail-container">
        <div className="loading-message">기업 정보를 불러오는 중...</div>
      </div>
    );
  }

  // 에러 상태
  if (error) {
    return (
      <div className="company-detail-container">
        <div className="error-message">
          <p>{error}</p>
          <button className="btn-back" onClick={handleBack}>
            목록으로 돌아가기
          </button>
        </div>
      </div>
    );
  }

  // 데이터 없음
  if (!companyInfo) {
    return (
      <div className="company-detail-container">
        <div className="error-message">
          <p>기업 정보를 찾을 수 없습니다.</p>
          <button className="btn-back" onClick={handleBack}>
            목록으로 돌아가기
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="company-detail-container">
      {/* 헤더 */}
      <div className="detail-header">
        <button className="btn-back" onClick={handleBack}>
          ← 목록으로
        </button>
        <h1 className="company-title">{companyInfo.corp_name}</h1>
      </div>

      {/* 기업 기본 정보 카드 */}
      <div className="company-info-card">
        <h2 className="card-title">기업 기본 정보</h2>
        <div className="info-grid">
          <div className="info-item">
            <span className="info-label">정식명칭</span>
            <span className="info-value">{companyInfo.corp_name || '-'}</span>
          </div>
          <div className="info-item">
            <span className="info-label">영문명칭</span>
            <span className="info-value">{companyInfo.corp_name_eng || '-'}</span>
          </div>
          <div className="info-item">
            <span className="info-label">종목명</span>
            <span className="info-value">{companyInfo.stock_name || '-'}</span>
          </div>
          <div className="info-item">
            <span className="info-label">종목코드</span>
            <span className="info-value">{companyInfo.stock_code || '-'}</span>
          </div>
          <div className="info-item">
            <span className="info-label">대표자명</span>
            <span className="info-value">{companyInfo.ceo_nm || '-'}</span>
          </div>
          <div className="info-item">
            <span className="info-label">설립일</span>
            <span className="info-value">
              {companyInfo.est_dt
                ? `${companyInfo.est_dt.substring(0, 4)}-${companyInfo.est_dt.substring(4, 6)}-${companyInfo.est_dt.substring(6, 8)}`
                : '-'}
            </span>
          </div>
          <div className="info-item">
            <span className="info-label">주소</span>
            <span className="info-value">{companyInfo.adres || '-'}</span>
          </div>
          <div className="info-item">
            <span className="info-label">전화번호</span>
            <span className="info-value">{companyInfo.phn_no || '-'}</span>
          </div>
          <div className="info-item">
            <span className="info-label">홈페이지</span>
            <span className="info-value">
              {companyInfo.hm_url ? (
                <a
                  href={`http://${companyInfo.hm_url}`}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="info-link"
                >
                  {companyInfo.hm_url}
                </a>
              ) : (
                '-'
              )}
            </span>
          </div>
        </div>
      </div>

      {/* 공시 목록 */}
      <div className="disclosure-section">
        <h2 className="section-title">공시 정보</h2>
        <DisclosureTable corpCode={corpCode} />
      </div>
    </div>
  );
}

export default CompanyDetailPage;
