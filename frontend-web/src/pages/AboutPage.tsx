import { Link } from 'react-router-dom';

export function AboutPage() {
  return (
    <div className="sub-view">
      <div className="sub-view-card about-card">
        <h2>About Us</h2>
        <p>Cognia is a cutting-edge educational platform designed to help educators structure lessons effectively and give students clear visibility into their learning milestones.</p>
        <p className="tagline-highlight">&ldquo;Structure your lessons. See the progress.&rdquo;</p>
        <Link to="/" className="cta-button">Back to Home</Link>
      </div>
    </div>
  );
}