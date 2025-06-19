import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../api";
import { useAuth } from "./AuthContext";
import { Helmet } from "react-helmet-async";
import "./../styles/AuthForm.css";
import logo from "../images/logotodolist.png";
import LoginBot from "./LoginBot"; // ✅ nuovo componente
import "../styles/LoginBot.css"; // ✅ nuovo stile

const LoginForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
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
    setError("");

    try {
      const res = await api.post("/login", { username, password });
      login(res.data);
      navigate("/tasks");
    } catch (err) {
      console.error("Errore nel login:", err);
      setError("Credenziali errate. Riprova.");
    }
  };

  return (
    <>
      <Helmet>
        <title>Login - ToDoList App</title>
      </Helmet>

      <div className="auth-container">
        <div className="login-bot-container">
          <LoginBot isPasswordActive={password.length > 0} />
        </div>

        <img
          src={logo}
          alt="Logo ToDoList"
          className="login-logo"
        />

        <div className="auth-box">
          <h2 className="title is-4">Login</h2>
          {error && <p className="notification is-danger">{error}</p>}
          <form onSubmit={handleSubmit}>
            <div className="field">
              <label className="label">Username</label>
              <input
                className="input"
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </div>

            <div className="field" style={{ position: "relative" }}>
              <label className="label">Password</label>
              <input
                className="input"
                type={showPassword ? "text" : "password"}
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <span
                style={{
                  position: "absolute",
                  right: "10px",
                  top: "58%",
                  cursor: "pointer",
                  color: "#ccc",
                }}
                onClick={() => setShowPassword(prev => !prev)}
              >
                {showPassword ? "👁️" : "🔒"}
              </span>
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