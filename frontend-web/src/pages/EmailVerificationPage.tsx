import { Link } from 'react-router-dom';

export function EmailVerificationPage() {
  return (
    <div className="sub-view">
      <div className="sub-view-card">
        <div className="success-icon-wrapper">
          <svg viewBox="0 0 24 24" width="48" height="48" stroke="currentColor" fill="none" strokeWidth="2">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
            <polyline points="22 4 12 14.01 9 11.01"></polyline>
          </svg>
        </div>
        <h2>Registration Successful!</h2>
        <p>We have sent a confirmation email to your inbox. Please check it and click the verification link to activate your Cognia account.</p>
        <Link to="/login" className="cta-button full-width">Proceed to Sign In</Link>
        <Link to="/" className="link-button">Back to Home</Link>
      </div>
    </div>
  );
}