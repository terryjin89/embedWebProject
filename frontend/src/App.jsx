import { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import LoginForm from './components/LoginForm';
import SignupForm from './components/SignupForm';
import ExchangeRateTable from './components/ExchangeRateTable';
import RateDetailChart from './components/RateDetailChart';
import CompanyTable from './components/CompanyTable';
import CompanyDetailPage from './pages/CompanyDetailPage';
import FavoritesPage from './pages/FavoritesPage';
import StockChartTestPage from './pages/StockChartTestPage';
import './App.css';

function App() {
  const [currentView, setCurrentView] = useState('exchange'); // 'login', 'signup', 'exchange', 'chart', 'companies'
  const [selectedCurrency, setSelectedCurrency] = useState(null);

  const handleCurrencySelect = (currency) => {
    // console.log('App: handleCurrencySelect 함수가 호출되었습니다.', currency);
    setSelectedCurrency(currency);
    setCurrentView('chart');
  };
  // console.log('App: 컴포넌트 렌더링. 현재 view:', currentView, '선택된 통화:',selectedCurrency);
  return (
    <AuthProvider>
      <BrowserRouter>
        <div className="app">
          <Routes>
            {/* 로그인 페이지 라우트 */}
            <Route path="/login" element={<LoginForm />} />

            {/* 회원가입 페이지 라우트 */}
            <Route path="/signup" element={<SignupForm />} />

            {/* 기업 상세 페이지 라우트 */}
            <Route path="/companies/:corpCode" element={<CompanyDetailPage />} />

            {/* 관심기업 페이지 라우트 (인증 필요) */}
            <Route
              path="/favorites"
              element={
                <ProtectedRoute>
                  <FavoritesPage />
                </ProtectedRoute>
              }
            />

            {/* 주가 차트 테스트 페이지 라우트 */}
            <Route path="/stock-chart-test" element={<StockChartTestPage />} />

            {/* 메인 페이지 라우트 */}
            <Route path="/" element={
              <>
                <div className="form-toggle">
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
                  <button
                    className="toggle-btn"
                    onClick={() => window.location.href = '/favorites'}
                  >
                    관심기업
                  </button>
                </div>

                {currentView === 'login' && <LoginForm />}
                {currentView === 'signup' && <SignupForm />}
                {currentView === 'exchange' && <ExchangeRateTable onRowClick={handleCurrencySelect}/>}
                {currentView === 'chart' && <RateDetailChart currencyCode={selectedCurrency.cur_unit} currencyName={selectedCurrency.cur_nm} />}
                {currentView === 'companies' && <CompanyTable />}
              </>
            } />
          </Routes>
        </div>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
