import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../api";
import { useAuth } from "./AuthContext";
import { Helmet } from "react-helmet-async";
import "./../styles/AuthForm.css";
import logo from "../images/logotodolist.png";

const LoginForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [formErrors, setFormErrors] = useState({});
  const [submitError, setSubmitError] = useState("");
  const navigate = useNavigate();
  const { login } = useAuth();

  useEffect(() => {
    document.body.style.overflow = 'hidden';
    return () => {
      document.body.style.overflow = 'auto';
    };
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitError("");

    const errors = {};
    if (!username.trim()) errors.username = "Username obbligatorio";
    if (!password) errors.password = "Password obbligatoria";

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      return;
    }

    setFormErrors({});

    try {
      const res = await api.post("/login", { username, password });
      login(res.data);
      navigate("/tasks");
    } catch (err) {
      console.error("Errore nel login:", err);
      setSubmitError("Credenziali errate. Riprova.");
    }
  };

  return (
    <>
      <Helmet>
        <title>Login - ToDoList App</title>
      </Helmet>

      <div className="auth-container">
        <img src={logo} alt="Logo ToDoList" className="login-logo" />

        <div className="auth-box">
          <h2 className="title is-4">Login</h2>
          {submitError && <p className="notification is-danger">{submitError}</p>}
          <form onSubmit={handleSubmit}>
            <div className="field">
              <label className="label">Username</label>
              <input
                className={`input ${formErrors.username ? 'is-danger' : ''}`}
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
                  onClick={() => setShowPassword(prev => !prev)}
                >
                  {showPassword ? "👁️" : "🔒"}
                </span>
              </div>
              <div style={{ minHeight: "1.5em" }}>
                {formErrors.password && <p className="help is-danger">{formErrors.password}</p>}
              </div>
            </div>

            <button className="button is-primary" type="submit">
              Accedi
            </button>
          </form>

          <div className="auth-link">
            <p>
              Non sei ancora registrato? <Link to="/signup">Registrati qui.</Link>
            </p>
          </div>
        </div>
      </div>
    </>
  );
};

export default LoginForm;