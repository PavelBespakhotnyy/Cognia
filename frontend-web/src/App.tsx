import { useState } from 'react';
import logoImg from './assets/logo-color.png';
import heroPhoto from './assets/hero-photo.jpg';
import './App.css';

export function App() {
  const [view, setView] = useState<
    'landing' | 'app' | 'signin' | 'register' | 'forgot-password' | 'email-verification-sent' | 'about'
  >('landing');

  // Registration state
  const [regRole, setRegRole] = useState<'tutor' | 'student' | 'parent'>('tutor');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [phone, setPhone] = useState('');
  const [gradeLevel, setGradeLevel] = useState('');
  const [schoolName, setSchoolName] = useState('');
  const [preferredPaymentMethod, setPreferredPaymentMethod] = useState('TRANSFER');

  // Password reset state
  const [resetEmail, setResetEmail] = useState('');
  const [resetSent, setResetSent] = useState(false);

  const handleRegisterSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Transition to email verification sent success screen
    setView('email-verification-sent');
  };

  const handlePasswordReset = (e: React.FormEvent) => {
    e.preventDefault();
    setResetSent(true);
  };

  return (
    <div className="landing-container">
      {/* Header */}
      <header className="header">
        <div className="logo-wrapper" onClick={() => setView('landing')} style={{ cursor: 'pointer' }}>
          <img src={logoImg} alt="Cognia Logo" className="logo-img" />
          <span className="brand-name">Cognia</span>
        </div>
        <nav className="nav-links">
          <button className="nav-btn ghost" onClick={() => setView('about')}>
            About Us
          </button>
          <button className="nav-btn ghost" onClick={() => setView('signin')}>
            Sign In
          </button>
          <button className="nav-btn primary" onClick={() => setView('register')}>
            Register
          </button>
        </nav>
      </header>

      {/* Main Content Area based on View */}
      {view === 'landing' && (
        <main className="hero-main">
          <div className="hero-content">
            <div className="badge">Welcome to Cognia</div>
            <h1 className="hero-title">
              Structure your lessons.<br />
              <span className="gradient-text">See the progress.</span>
            </h1>
            <p className="hero-subtitle">
              Empower your teaching and learning journey with structured lesson planning, real-time analytics, and seamless progress tracking.
            </p>
            <div className="hero-actions">
              <button className="cta-button" onClick={() => setView('register')}>
                Get Started
                <svg className="arrow-icon" viewBox="0 0 24 24" width="20" height="20">
                  <path fill="currentColor" d="M5 13h11.586l-4.293 4.293 1.414 1.414L21.414 12l-7.707-7.707-1.414 1.414L16.586 11H5v2z" />
                </svg>
              </button>
              <button className="secondary-button" onClick={() => setView('about')}>
                Learn More
              </button>
            </div>
          </div>
          <div className="hero-visual">
            <div className="image-card">
              <img src={heroPhoto} alt="Education and Progress" className="hero-photo" />
              <div className="floating-badge">
                <span className="pulse-dot"></span>
                <span>Active Learning Platform</span>
              </div>
            </div>
          </div>
        </main>
      )}

      {view === 'register' && (
        <div className="sub-view">
          <div className="sub-view-card auth-card wide-card">
            <h2>Create Your Account</h2>
            <p>Join Cognia as a Tutor, Student, or Parent to start your structured learning experience.</p>

            {/* Role Tabs */}
            <div className="role-tabs">
              <button
                type="button"
                className={`role-tab ${regRole === 'tutor' ? 'active' : ''}`}
                onClick={() => setRegRole('tutor')}
              >
                Tutor
              </button>
              <button
                type="button"
                className={`role-tab ${regRole === 'student' ? 'active' : ''}`}
                onClick={() => setRegRole('student')}
              >
                Student
              </button>
              <button
                type="button"
                className={`role-tab ${regRole === 'parent' ? 'active' : ''}`}
                onClick={() => setRegRole('parent')}
              >
                Parent
              </button>
            </div>

            <form onSubmit={handleRegisterSubmit}>
              <div className="form-row">
                <div className="form-group">
                  <label>First Name</label>
                  <input
                    type="text"
                    required
                    placeholder="Elena"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                  />
                </div>
                <div className="form-group">
                  <label>Last Name</label>
                  <input
                    type="text"
                    required
                    placeholder="Garcia"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                  />
                </div>
              </div>

              <div className="form-group">
                <label>Email Address</label>
                <input
                  type="email"
                  required
                  placeholder={`${regRole}@cognia.dev`}
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </div>

              <div className="form-group">
                <label>Password</label>
                <input
                  type="password"
                  required
                  placeholder="StrongPass123"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>

              {regRole === 'tutor' && (
                <div className="form-group">
                  <label>Phone Number</label>
                  <input
                    type="tel"
                    required
                    placeholder="+34600000001"
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                  />
                </div>
              )}

              {regRole === 'student' && (
                <>
                  <div className="form-group">
                    <label>Grade Level</label>
                    <input
                      type="text"
                      required
                      placeholder="3 ESO"
                      value={gradeLevel}
                      onChange={(e) => setGradeLevel(e.target.value)}
                    />
                  </div>
                  <div className="form-group">
                    <label>School Name</label>
                    <input
                      type="text"
                      required
                      placeholder="Colegio Norte"
                      value={schoolName}
                      onChange={(e) => setSchoolName(e.target.value)}
                    />
                  </div>
                </>
              )}

              {regRole === 'parent' && (
                <>
                  <div className="form-group">
                    <label>Phone Number</label>
                    <input
                      type="tel"
                      required
                      placeholder="+34600000002"
                      value={phone}
                      onChange={(e) => setPhone(e.target.value)}
                    />
                  </div>
                  <div className="form-group">
                    <label>Preferred Payment Method</label>
                    <select
                      value={preferredPaymentMethod}
                      onChange={(e) => setPreferredPaymentMethod(e.target.value)}
                      className="form-select"
                    >
                      <option value="TRANSFER">Bank Transfer</option>
                      <option value="CARD">Credit / Debit Card</option>
                    </select>
                  </div>
                </>
              )}

              <button type="submit" className="cta-button full-width">
                Complete Registration
              </button>
            </form>

            <button className="link-button" onClick={() => setView('signin')}>
              Already have an account? Sign In
            </button>
          </div>
        </div>
      )}

      {view === 'email-verification-sent' && (
        <div className="sub-view">
          <div className="sub-view-card">
            <div className="success-icon-wrapper">
              <svg viewBox="0 0 24 24" width="48" height="48" stroke="currentColor" fill="none" strokeWidth="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                <polyline points="22 4 12 14.01 9 11.01"></polyline>
              </svg>
            </div>
            <h2>Registration Successful!</h2>
            <p>
              We have sent a confirmation email to <strong>{email || 'your email'}</strong>. Please check your inbox and click the verification link to activate your Cognia account.
            </p>
            <button className="cta-button full-width" onClick={() => setView('signin')}>
              Proceed to Sign In
            </button>
            <button className="link-button" onClick={() => setView('landing')}>
              Back to Home
            </button>
          </div>
        </div>
      )}

      {view === 'forgot-password' && (
        <div className="sub-view">
          <div className="sub-view-card auth-card">
            <h2>Reset Your Password</h2>
            <p>Enter your account email address and we will send you a secure link to reset your password.</p>

            {!resetSent ? (
              <form onSubmit={handlePasswordReset}>
                <div className="form-group">
                  <label>Email Address</label>
                  <input
                    type="email"
                    required
                    placeholder="name@example.com"
                    value={resetEmail}
                    onChange={(e) => setResetEmail(e.target.value)}
                  />
                </div>
                <button type="submit" className="cta-button full-width">
                  Send Reset Link
                </button>
              </form>
            ) : (
              <div className="success-message-box">
                <p>
                  Password reset link has been successfully sent to <strong>{resetEmail}</strong>. Please check your inbox.
                </p>
              </div>
            )}

            <button className="link-button" onClick={() => setView('signin')}>
              &larr; Back to Sign In
            </button>
          </div>
        </div>
      )}

      {view === 'signin' && (
        <div className="sub-view">
          <div className="sub-view-card auth-card">
            <h2>Sign In to Cognia</h2>
            <p>Enter your credentials to access your portal.</p>
            <form onSubmit={(e) => { e.preventDefault(); setView('app'); }}>
              <div className="form-group">
                <label>Email Address</label>
                <input type="email" required placeholder="tutor@cognia.dev" defaultValue="" />
              </div>
              <div className="form-group">
                <label>Password</label>
                <input type="password" required placeholder="••••••••" defaultValue="" />
              </div>
              <div className="form-actions-row">
                <button
                  type="button"
                  className="link-button inline-link"
                  onClick={() => setView('forgot-password')}
                >
                  Forgot password?
                </button>
              </div>
              <button type="submit" className="cta-button full-width">
                Sign In
              </button>
            </form>
            <button className="link-button" onClick={() => setView('register')}>
              Don't have an account? Register
            </button>
            <button className="link-button" onClick={() => setView('landing')}>
              &larr; Back to Home
            </button>
          </div>
        </div>
      )}

      {view === 'app' && (
        <div className="sub-view">
          <div className="sub-view-card">
            <h2>Welcome to Cognia Dashboard</h2>
            <p>You are successfully logged in. Structure your lessons and track progress in real time.</p>
            <button className="cta-button" onClick={() => setView('landing')}>
              Log Out / Home
            </button>
          </div>
        </div>
      )}

      {view === 'about' && (
        <div className="sub-view">
          <div className="sub-view-card about-card">
            <h2>About Us</h2>
            <p>
              Cognia is a cutting-edge educational platform designed to help educators structure lessons effectively and give students clear visibility into their learning milestones.
            </p>
            <p className="tagline-highlight">
              &ldquo;Structure your lessons. See the progress.&rdquo;
            </p>
            <button className="cta-button" onClick={() => setView('landing')}>
              Back to Home
            </button>
          </div>
        </div>
      )}

      {/* Footer */}
      <footer className="footer">
        <p>&copy; {new Date().getFullYear()} Cognia. All rights reserved. Strictly English interface.</p>
      </footer>
    </div>
  );
}

export default App;
