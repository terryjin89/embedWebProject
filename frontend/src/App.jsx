import { useState } from 'react';
import { AuthProvider } from './contexts/AuthContext';
import LoginForm from './components/LoginForm';
import SignupForm from './components/SignupForm';
import './App.css';

function App() {
  const [currentForm, setCurrentForm] = useState('login'); // 'login' or 'signup'

  return (
    <AuthProvider>
      <div className="app">
        <div className="form-toggle">
          <button
            className={`toggle-btn ${currentForm === 'login' ? 'active' : ''}`}
            onClick={() => setCurrentForm('login')}
          >
            로그인
          </button>
          <button
            className={`toggle-btn ${currentForm === 'signup' ? 'active' : ''}`}
            onClick={() => setCurrentForm('signup')}
          >
            회원가입
          </button>
        </div>

        {currentForm === 'login' ? <LoginForm /> : <SignupForm />}
      </div>
    </AuthProvider>
  );
}

export default App;
