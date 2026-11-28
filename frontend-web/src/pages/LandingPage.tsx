import './LandingPage.css';
import heroPhoto from '../assets/hero-photo.jpg';
import { Link } from 'react-router-dom';

export function LandingPage() {
  return (
    <main className="hero-main">
      <div className="hero-content">
        <div className="badge">Welcome to Cognia</div>
        <h1 className="hero-title">
          Structure your lessons.<br />
          <span className="gradient-text">See the progress.</span>
        </h1>
        <p className="hero-subtitle">
          Empower your teaching and learning journey...
        </p>
        <div className="hero-actions">
          <Link to="/register" className="cta-button">
            Get Started
          </Link>
          <Link to="/about" className="secondary-button">
            Learn More
          </Link>
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
  );
}