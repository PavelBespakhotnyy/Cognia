import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

export function RegisterPage() {
  const navigate = useNavigate();
  const [regRole, setRegRole] = useState<'tutor' | 'student' | 'parent'>('tutor');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phone, setPhone] = useState('');
  const [gradeLevel, setGradeLevel] = useState('');
  const [schoolName, setSchoolName] = useState('');
  const [preferredPaymentMethod, setPreferredPaymentMethod] = useState('TRANSFER');

  const handleRegisterSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Переход на страницу подтверждения почты
    navigate('/email-verification'); 
  };

  return (
    <div className="sub-view">
      <div className="sub-view-card auth-card wide-card">
        <h2>Create Your Account</h2>
        <p>Join Cognia as a Tutor, Student, or Parent to start your structured learning experience.</p>

        <div className="role-tabs">
          <button type="button" className={`role-tab ${regRole === 'tutor' ? 'active' : ''}`} onClick={() => setRegRole('tutor')}>Tutor</button>
          <button type="button" className={`role-tab ${regRole === 'student' ? 'active' : ''}`} onClick={() => setRegRole('student')}>Student</button>
          <button type="button" className={`role-tab ${regRole === 'parent' ? 'active' : ''}`} onClick={() => setRegRole('parent')}>Parent</button>
        </div>

        <form onSubmit={handleRegisterSubmit}>
          <div className="form-row">
            <div className="form-group">
              <label>First Name</label>
              <input type="text" required placeholder="Elena" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
            </div>
            <div className="form-group">
              <label>Last Name</label>
              <input type="text" required placeholder="Garcia" value={lastName} onChange={(e) => setLastName(e.target.value)} />
            </div>
          </div>

          <div className="form-group">
            <label>Email Address</label>
            <input type="email" required placeholder={`${regRole}@cognia.dev`} value={email} onChange={(e) => setEmail(e.target.value)} />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input type="password" required placeholder="StrongPass123" value={password} onChange={(e) => setPassword(e.target.value)} />
          </div>

          {regRole === 'tutor' && (
            <div className="form-group">
              <label>Phone Number</label>
              <input type="tel" required placeholder="+34600000001" value={phone} onChange={(e) => setPhone(e.target.value)} />
            </div>
          )}

          {regRole === 'student' && (
            <>
              <div className="form-group">
                <label>Grade Level</label>
                <input type="text" required placeholder="3 ESO" value={gradeLevel} onChange={(e) => setGradeLevel(e.target.value)} />
              </div>
              <div className="form-group">
                <label>School Name</label>
                <input type="text" required placeholder="Colegio Norte" value={schoolName} onChange={(e) => setSchoolName(e.target.value)} />
              </div>
            </>
          )}

          {regRole === 'parent' && (
            <>
              <div className="form-group">
                <label>Phone Number</label>
                <input type="tel" required placeholder="+34600000002" value={phone} onChange={(e) => setPhone(e.target.value)} />
              </div>
              <div className="form-group">
                <label>Preferred Payment Method</label>
                <select value={preferredPaymentMethod} onChange={(e) => setPreferredPaymentMethod(e.target.value)} className="form-select">
                  <option value="TRANSFER">Bank Transfer</option>
                  <option value="CARD">Credit / Debit Card</option>
                </select>
              </div>
            </>
          )}

          <button type="submit" className="cta-button full-width">Complete Registration</button>
        </form>

        <Link to="/login" className="link-button">Already have an account? Sign In</Link>
      </div>
    </div>
  );
}