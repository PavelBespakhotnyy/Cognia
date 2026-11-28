import { useState } from 'react';
import { Link } from 'react-router-dom';

export function ForgotPasswordPage() {
  const [resetEmail, setResetEmail] = useState('');
  const [resetSent, setResetSent] = useState(false);

  const handlePasswordReset = (e: React.FormEvent) => {
    e.preventDefault();
    setResetSent(true);
  };

  return (
    <div className="sub-view">
      <div className="sub-view-card auth-card">
        <h2>Reset Your Password</h2>
        <p>Enter your account email address and we will send you a secure link to reset your password.</p>

        {!resetSent ? (
          <form onSubmit={handlePasswordReset}>
            <div className="form-group">
              <label>Email Address</label>
              <input type="email" required placeholder="name@example.com" value={resetEmail} onChange={(e) => setResetEmail(e.target.value)} />
            </div>
            <button type="submit" className="cta-button full-width">Send Reset Link</button>
          </form>
        ) : (
          <div className="success-message-box">
            <p>Password reset link has been successfully sent to <strong>{resetEmail}</strong>. Please check your inbox.</p>
          </div>
        )}

        <Link to="/login" className="link-button">&larr; Back to Sign In</Link>
      </div>
    </div>
  );
}