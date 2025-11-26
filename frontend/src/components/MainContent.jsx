/**
 * 메인 컨텐츠 컴포넌트
 *
 * 인증 상태에 따라 네비게이션 버튼을 조건부 렌더링합니다.
 * - 로그인 전: "로그인", "회원가입" 버튼 표시
 * - 로그인 후: "로그아웃" 버튼 표시 (로그인/회원가입 버튼 숨김)
 * - 로그아웃 클릭 시 logout 함수 호출 후 페이지 새로고침
 *
 * 📁 상세 문서: readme/joinMembershipFunction.md
 * 🎫 SCRUM-6
 */
import { useState } from 'react';
import { useAuth } from '../hooks/useAuth';
import LoginForm from './LoginForm';
import SignupForm from './SignupForm';
import ExchangeRateTable from './ExchangeRateTable';
import RateDetailChart from './RateDetailChart';
import CompanyTable from './CompanyTable';

function MainContent() {
  const { user, logout } = useAuth();
  const [currentView, setCurrentView] = useState('exchange'); // 'login', 'signup', 'exchange', 'chart', 'companies'
  const [selectedCurrency, setSelectedCurrency] = useState(null);

  const handleCurrencySelect = (currency) => {
    setSelectedCurrency(currency);
    setCurrentView('chart');
  };

  const handleLogout = () => {
    logout();
    // 로그아웃 후 페이지 새로고침
    window.location.href = '/';
  };

  return (
    <>
      <div className="form-toggle">
        {/* 로그인 상태에 따른 조건부 렌더링 */}
        {!user ? (
          <>
            <button
              className={`toggle-btn ${currentView === 'login' ? 'active' : ''}`}
              onClick={() => setCurrentView('login')}
            >
              로그인
            </button>
            <button
              className={`toggle-btn ${currentView === 'signup' ? 'active' : ''}`}
              onClick={() => setCurrentView('signup')}
            >
              회원가입
            </button>
          </>
        ) : (
          <button className="toggle-btn" onClick={handleLogout}>
            로그아웃
          </button>
        )}

        <button
          className={`toggle-btn ${currentView === 'exchange' ? 'active' : ''}`}
          onClick={() => setCurrentView('exchange')}
        >
          환율정보
        </button>
        <button
          className={`toggle-btn ${currentView === 'companies' ? 'active' : ''}`}
          onClick={() => setCurrentView('companies')}
        >
          기업정보
        </button>
        <button className="toggle-btn" onClick={() => (window.location.href = '/favorites')}>
          관심기업
        </button>
        <button className="toggle-btn" onClick={() => (window.location.href = '/news')}>
          뉴스검색
        </button>
      </div>

      {currentView === 'login' && <LoginForm />}
      {currentView === 'signup' && <SignupForm />}
      {currentView === 'exchange' && <ExchangeRateTable onRowClick={handleCurrencySelect} />}
      {currentView === 'chart' && selectedCurrency && (
        <RateDetailChart currencyCode={selectedCurrency.curUnit} currencyName={selectedCurrency.curNm} />
      )}
      {currentView === 'companies' && <CompanyTable />}
    </>
  );
}

export default MainContent;
