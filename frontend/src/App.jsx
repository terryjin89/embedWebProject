import { useState } from 'react';
import { AuthProvider } from './contexts/AuthContext';
import LoginForm from './components/LoginForm';
import SignupForm from './components/SignupForm';
import ExchangeRateTable from './components/ExchangeRateTable';
import RateDetailChart from './components/RateDetailChart';
import './App.css';

function App() {
  const [currentView, setCurrentView] = useState('exchange'); // 'login', 'signup', 'exchange', 'chart'
  const [selectedCurrency, setSelectedCurrency] = useState(null);

  const handleCurrencySelect = (currency) => {
    // console.log('App: handleCurrencySelect 함수가 호출되었습니다.', currency);
    setSelectedCurrency(currency); 
    setCurrentView('chart');
  };
  // console.log('App: 컴포넌트 렌더링. 현재 view:', currentView, '선택된 통화:',selectedCurrency);
  return (
    <AuthProvider>
      <div className="app">
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
        </div>

        {currentView === 'login' && <LoginForm />}
        {currentView === 'signup' && <SignupForm />}
        {currentView === 'exchange' && <ExchangeRateTable onRowClick={handleCurrencySelect}/>}
        {currentView === 'chart' && <RateDetailChart currencyCode={selectedCurrency.cur_unit} currencyName={selectedCurrency.cur_nm} />}
      </div>
    </AuthProvider>
  );
}

export default App;
