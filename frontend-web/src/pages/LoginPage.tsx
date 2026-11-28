import { Link, useNavigate } from 'react-router-dom';

export function LoginPage() {
  const navigate = useNavigate();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Логика входа...
    navigate('/dashboard'); // Переход в личный кабинет
  };

  return (
    <div className="sub-view">
      <div className="sub-view-card auth-card">
        <h2>Sign In to Cognia</h2>
        <p>Enter your credentials to access your portal.</p>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email Address</label>
            <input type="email" required placeholder="tutor@cognia.dev" />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input type="password" required placeholder="••••••••" />
          </div>
          <div className="form-actions-row">
            <Link to="/forgot-password" className="link-button inline-link">Forgot password?</Link>
          </div>
          <button type="submit" className="cta-button full-width">Sign In</button>
        </form>
        <Link to="/register" className="link-button">Don't have an account? Register</Link>
        <Link to="/" className="link-button">&larr; Back to Home</Link>
      </div>
    </div>
  );
}