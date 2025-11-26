/**
 * 로그인 폼 컴포넌트
 *
 * 사용자 인증을 위한 로그인 폼을 제공합니다.
 * - 이메일, 비밀번호 입력 필드
 * - 실시간 폼 검증
 * - useAuth Hook을 통한 로그인 처리
 * - ProtectedRoute에서 전달된 리다이렉트 메시지 표시
 * - 로그인 성공 시 원래 페이지로 리다이렉트
 *
 * 📁 상세 문서: readme/joinMembershipFunction.md
 * 🔗 API: POST /api/auth/login
 * 🎫 SCRUM-6
 */
import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import './LoginForm.css';

function LoginForm() {
  const { login } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  // ProtectedRoute에서 전달된 리다이렉트 정보
  const redirectMessage = location.state?.message;
  const redirectFrom = location.state?.from || '/';

  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  const [errors, setErrors] = useState({
    email: '',
    password: '',
  });

  const [touched, setTouched] = useState({
    email: false,
    password: false,
  });

  const [isSubmitting, setIsSubmitting] = useState(false);

  // 입력 필드 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    // 입력 시 해당 필드 검증
    if (touched[name]) {
      validateField(name, value);
    }
  };

  // 필드 포커스 해제 시 touched 상태 업데이트
  const handleBlur = (e) => {
    const { name, value } = e.target;
    setTouched((prev) => ({
      ...prev,
      [name]: true,
    }));
    validateField(name, value);
  };

  // 필드별 유효성 검증
  const validateField = (name, value) => {
    let error = '';

    switch (name) {
      case 'email':
        if (!value) {
          error = '이메일을 입력해주세요.';
        } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(value)) {
          error = '올바른 이메일 형식이 아닙니다.';
        }
        break;

      case 'password':
        if (!value) {
          error = '비밀번호를 입력해주세요.';
        } else if (value.length < 6) {
          error = '비밀번호는 최소 6자 이상이어야 합니다.';
        }
        break;

      default:
        break;
    }

    setErrors((prev) => ({
      ...prev,
      [name]: error,
    }));

    return error === '';
  };

  // 폼 전체 유효성 검증
  const validateForm = () => {
    const emailValid = validateField('email', formData.email);
    const passwordValid = validateField('password', formData.password);

    // 모든 필드를 touched로 표시
    setTouched({
      email: true,
      password: true,
    });

    return emailValid && passwordValid;
  };

  // 폼 제출 핸들러
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (validateForm()) {
      setIsSubmitting(true);

      try {
        const result = await login(formData.email, formData.password);

        if (result.success) {
          // 로그인 성공 시 원래 페이지로 리다이렉트 (페이지 새로고침)
          window.location.href = redirectFrom;
        } else {
          setErrors((prev) => ({
            ...prev,
            email: result.error || '로그인에 실패했습니다.',
          }));
        }
      } catch (error) {
        console.error('Login error:', error);
        setErrors((prev) => ({
          ...prev,
          email: '로그인 중 오류가 발생했습니다.',
        }));
      } finally {
        setIsSubmitting(false);
      }
    }
  };

  return (
    <div className="login-form-container">
      <div className="login-card">
        <div className="login-header">
          <h1 className="login-title">로그인</h1>
          <p className="login-subtitle">기업분석 플랫폼에 오신 것을 환영합니다</p>
        </div>

        {/* 리다이렉트 메시지 표시 */}
        {redirectMessage && (
          <div className="redirect-message">
            <span className="message-icon">ℹ️</span>
            <span className="message-text">{redirectMessage}</span>
          </div>
        )}

        <form className="login-form" onSubmit={handleSubmit} noValidate>
          {/* 이메일 입력 */}
          <div className="form-group">
            <label htmlFor="email" className="form-label">
              이메일
            </label>
            <input
              type="email"
              id="email"
              name="email"
              className={`form-input ${errors.email && touched.email ? 'error' : ''}`}
              placeholder="example@email.com"
              value={formData.email}
              onChange={handleChange}
              onBlur={handleBlur}
              autoComplete="email"
            />
            {errors.email && touched.email && (
              <span className="error-message">{errors.email}</span>
            )}
          </div>

          {/* 비밀번호 입력 */}
          <div className="form-group">
            <label htmlFor="password" className="form-label">
              비밀번호
            </label>
            <input
              type="password"
              id="password"
              name="password"
              className={`form-input ${errors.password && touched.password ? 'error' : ''}`}
              placeholder="6자 이상 입력"
              value={formData.password}
              onChange={handleChange}
              onBlur={handleBlur}
              autoComplete="current-password"
            />
            {errors.password && touched.password && (
              <span className="error-message">{errors.password}</span>
            )}
          </div>

          {/* 로그인 버튼 */}
          <button type="submit" className="btn-primary" disabled={isSubmitting}>
            {isSubmitting ? '로그인 중...' : '로그인'}
          </button>
        </form>

        <div className="login-footer">
          <p className="signup-link">
            계정이 없으신가요? <a href="/signup">회원가입</a>
          </p>
        </div>
      </div>
    </div>
  );
}

export default LoginForm;
