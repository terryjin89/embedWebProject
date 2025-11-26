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
import FavoriteDetailPage from './pages/FavoriteDetailPage';
import StockChartTestPage from './pages/StockChartTestPage';
import NewsSearchPage from './pages/NewsSearchPage';
import MainContent from './components/MainContent';
import './App.css';

function App() {
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

            {/* 관심기업 상세 페이지 라우트 (인증 필요) */}
            <Route
              path="/favorites/:stockCode"
              element={
                <ProtectedRoute>
                  <FavoriteDetailPage />
                </ProtectedRoute>
              }
            />

            {/* 주가 차트 테스트 페이지 라우트 */}
            <Route path="/stock-chart-test" element={<StockChartTestPage />} />

            {/* 뉴스 검색 페이지 라우트 */}
            <Route path="/news" element={<NewsSearchPage />} />

            {/* 메인 페이지 라우트 */}
            <Route path="/" element={<MainContent />} />
          </Routes>
        </div>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
