import React, { useState } from "react";
import api from "../api";
import { useNavigate, Link } from "react-router-dom";
import "./../styles/AuthForm.css";

const RegistrationForm = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [formErrors, setFormErrors] = useState({});
  const [submitError, setSubmitError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    setSubmitError("");

    const errors = {};
    if (!username.trim()) errors.username = "Username obbligatorio";
    if (!email.trim()) errors.email = "Email obbligatoria";
    else if (!/\S+@\S+\.\S+/.test(email)) errors.email = "Email non valida";

    if (!password) errors.password = "Password obbligatoria";
    if (!confirmPassword) errors.confirmPassword = "Conferma la password";
    else if (password !== confirmPassword)
      errors.confirmPassword = "Le password non corrispondono";

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      return;
    }

    setFormErrors({});

    api
      .post("signup", { username, email, password })
      .then(() => navigate("/login"))
      .catch((err) => {
        setSubmitError("Errore durante la registrazione. Riprova.");
        console.error("Errore nel sign up:", err);
      });
  };

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h2 className="title is-4">📝 Registrazione</h2>
        {submitError && <p className="notification is-danger">{submitError}</p>}
        <form onSubmit={handleSubmit}>
          <div className="field">
            <label className="label">Username</label>
            <input
              className={`input ${formErrors.username ? "is-danger" : ""}`}
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            <div style={{ minHeight: "1.5em" }}>
              {formErrors.username && <p className="help is-danger">{formErrors.username}</p>}
            </div>
          </div>

          <div className="field">
            <label className="label">Email</label>
            <input
              className={`input ${formErrors.email ? "is-danger" : ""}`}
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <div style={{ minHeight: "1.5em" }}>
              {formErrors.email && <p className="help is-danger">{formErrors.email}</p>}
            </div>
          </div>

          <div className="field">
            <label className="label">Password</label>
            <div className="control has-icons-right" style={{ position: "relative" }}>
              <input
                className={`input ${formErrors.password ? "is-danger" : ""}`}
                type={showPassword ? "text" : "password"}
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <span
                style={{
                  position: "absolute",
                  right: "10px",
                  top: "50%",
                  transform: "translateY(-50%)",
                  cursor: "pointer",
                  color: "#ccc",
                }}
                onClick={() => setShowPassword((prev) => !prev)}
              >
                {showPassword ? "👁️" : "🔒"}
              </span>
            </div>
            <div style={{ minHeight: "1.5em" }}>
              {formErrors.password && <p className="help is-danger">{formErrors.password}</p>}
            </div>
          </div>

          <div className="field">
            <label className="label">Conferma Password</label>
            <div className="control has-icons-right" style={{ position: "relative" }}>
              <input
                className={`input ${formErrors.confirmPassword ? "is-danger" : ""}`}
                type={showConfirmPassword ? "text" : "password"}
                placeholder="Conferma Password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
              <span
                style={{
                  position: "absolute",
                  right: "10px",
                  top: "50%",
                  transform: "translateY(-50%)",
                  cursor: "pointer",
                  color: "#ccc",
                }}
                onClick={() => setShowConfirmPassword((prev) => !prev)}
              >
                {showConfirmPassword ? "👁️" : "🔒"}
              </span>
            </div>
            <div style={{ minHeight: "1.5em" }}>
              {formErrors.confirmPassword && <p className="help is-danger">{formErrors.confirmPassword}</p>}
            </div>
          </div>

          <button className="button is-primary" type="submit">
            Registrati
          </button>
        </form>
        <div className="auth-link">
          <p>
            Hai già un account? <Link to="/login">Accedi qui.</Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default RegistrationForm;