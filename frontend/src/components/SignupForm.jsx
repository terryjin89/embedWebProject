import { useState } from 'react';
import './SignupForm.css';

function SignupForm() {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    passwordConfirm: '',
    name: '',
  });

  const [errors, setErrors] = useState({
    email: '',
    password: '',
    passwordConfirm: '',
    name: '',
  });

  const [touched, setTouched] = useState({
    email: false,
    password: false,
    passwordConfirm: false,
    name: false,
  });

  // 비밀번호 강도 계산
  const getPasswordStrength = (password) => {
    if (!password) return 0;

    let strength = 0;
    if (password.length >= 6) strength++;
    if (password.length >= 10) strength++;
    if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++;
    if (/\d/.test(password)) strength++;
    if (/[!@#$%^&*(),.?":{}|<>]/.test(password)) strength++;

    return Math.min(strength, 4);
  };

  const passwordStrength = getPasswordStrength(formData.password);
  const strengthLabels = ['', '약함', '보통', '강함', '매우 강함'];
  const strengthColors = ['', '#f44336', '#ff9800', '#4caf50', '#2196f3'];

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
        // 비밀번호 변경 시 비밀번호 확인 필드도 재검증
        if (formData.passwordConfirm) {
          validateField('passwordConfirm', formData.passwordConfirm);
        }
        break;

      case 'passwordConfirm':
        if (!value) {
          error = '비밀번호 확인을 입력해주세요.';
        } else if (value !== formData.password) {
          error = '비밀번호가 일치하지 않습니다.';
        }
        break;

      case 'name':
        if (!value) {
          error = '이름을 입력해주세요.';
        } else if (value.length < 2) {
          error = '이름은 최소 2자 이상이어야 합니다.';
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
    const passwordConfirmValid = validateField('passwordConfirm', formData.passwordConfirm);
    const nameValid = validateField('name', formData.name);

    // 모든 필드를 touched로 표시
    setTouched({
      email: true,
      password: true,
      passwordConfirm: true,
      name: true,
    });

    return emailValid && passwordValid && passwordConfirmValid && nameValid;
  };

  // 폼 제출 핸들러
  const handleSubmit = (e) => {
    e.preventDefault();

    if (validateForm()) {
      console.log('회원가입 데이터:', formData);
      // TODO: API 호출 로직 추가 (SCRUM-14에서 구현)
      alert('회원가입 기능은 백엔드 연동 후 동작합니다.');
    }
  };

  return (
    <div className="signup-form-container">
      <div className="signup-card">
        <div className="signup-header">
          <h1 className="signup-title">회원가입</h1>
          <p className="signup-subtitle">새로운 계정을 만들어보세요</p>
        </div>

        <form className="signup-form" onSubmit={handleSubmit} noValidate>
          {/* 이름 입력 */}
          <div className="form-group">
            <label htmlFor="name" className="form-label">
              이름
            </label>
            <input
              type="text"
              id="name"
              name="name"
              className={`form-input ${errors.name && touched.name ? 'error' : ''}`}
              placeholder="홍길동"
              value={formData.name}
              onChange={handleChange}
              onBlur={handleBlur}
              autoComplete="name"
            />
            {errors.name && touched.name && (
              <span className="error-message">{errors.name}</span>
            )}
          </div>

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
              autoComplete="new-password"
            />
            {errors.password && touched.password && (
              <span className="error-message">{errors.password}</span>
            )}

            {/* 비밀번호 강도 표시기 */}
            {formData.password && (
              <div className="password-strength">
                <div className="strength-bar-container">
                  {[1, 2, 3, 4].map((level) => (
                    <div
                      key={level}
                      className={`strength-bar ${passwordStrength >= level ? 'active' : ''}`}
                      style={{
                        backgroundColor: passwordStrength >= level ? strengthColors[passwordStrength] : '',
                      }}
                    />
                  ))}
                </div>
                <span className="strength-label" style={{ color: strengthColors[passwordStrength] }}>
                  {strengthLabels[passwordStrength]}
                </span>
              </div>
            )}
          </div>

          {/* 비밀번호 확인 입력 */}
          <div className="form-group">
            <label htmlFor="passwordConfirm" className="form-label">
              비밀번호 확인
            </label>
            <input
              type="password"
              id="passwordConfirm"
              name="passwordConfirm"
              className={`form-input ${errors.passwordConfirm && touched.passwordConfirm ? 'error' : ''}`}
              placeholder="비밀번호 재입력"
              value={formData.passwordConfirm}
              onChange={handleChange}
              onBlur={handleBlur}
              autoComplete="new-password"
            />
            {errors.passwordConfirm && touched.passwordConfirm && (
              <span className="error-message">{errors.passwordConfirm}</span>
            )}
          </div>

          {/* 회원가입 버튼 */}
          <button type="submit" className="btn-primary">
            회원가입
          </button>
        </form>

        <div className="signup-footer">
          <p className="login-link">
            이미 계정이 있으신가요? <a href="/login">로그인</a>
          </p>
        </div>
      </div>
    </div>
  );
}

export default SignupForm;
