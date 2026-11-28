import { Link } from 'react-router-dom';

export function DashboardPage() {
  return (
    <div className="sub-view">
      <div className="sub-view-card">
        <h2>Welcome to Cognia Dashboard</h2>
        <p>You are successfully logged in. Structure your lessons and track progress in real time.</p>
        <Link to="/" className="cta-button">Log Out / Home</Link>
      </div>
    </div>
  );
}