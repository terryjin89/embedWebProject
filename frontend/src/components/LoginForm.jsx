import { useState } from 'react';
import './LoginForm.css';

function LoginForm() {
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
  const handleSubmit = (e) => {
    e.preventDefault();

    if (validateForm()) {
      console.log('로그인 데이터:', formData);
      // TODO: API 호출 로직 추가 (SCRUM-14에서 구현)
      alert('로그인 기능은 백엔드 연동 후 동작합니다.');
    }
  };

  return (
    <div className="login-form-container">
      <div className="login-card">
        <div className="login-header">
          <h1 className="login-title">로그인</h1>
          <p className="login-subtitle">기업분석 플랫폼에 오신 것을 환영합니다</p>
        </div>

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
          <button type="submit" className="btn-primary">
            로그인
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
