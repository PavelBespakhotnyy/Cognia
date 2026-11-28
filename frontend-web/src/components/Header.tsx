import { Link } from 'react-router-dom';
import logoImg from '../assets/logo-color.png';

export function Header() {
  return (
    <header className="header">
      <Link to="/" className="logo-wrapper" style={{ textDecoration: 'none', color: 'inherit' }}>
        <img src={logoImg} alt="Cognia Logo" className="logo-img" />
        <span className="brand-name">Cognia</span>
      </Link>
      <nav className="nav-links">
        <Link to="/about" className="nav-btn ghost">About Us</Link>
        <Link to="/login" className="nav-btn ghost">Sign In</Link>
        <Link to="/register" className="nav-btn primary">Register</Link>
      </nav>
    </header>
  );
}