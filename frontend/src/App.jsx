import { useState } from 'react';
import { AuthProvider } from './contexts/AuthContext';
import LoginForm from './components/LoginForm';
import SignupForm from './components/SignupForm';
import ExchangeRateTable from './components/ExchangeRateTable';
import './App.css';

function App() {
  const [currentView, setCurrentView] = useState('exchange'); // 'login', 'signup', 'exchange'

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
        {currentView === 'exchange' && <ExchangeRateTable />}
      </div>
    </AuthProvider>
  );
}

export default App;
