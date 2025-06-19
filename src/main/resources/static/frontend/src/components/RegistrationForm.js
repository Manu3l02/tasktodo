import React, { useState } from "react";
import axios from "axios";
import api from "../api";
import { useNavigate, Link } from "react-router-dom";
import "./../styles/AuthForm.css";

const RegistrationForm = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");

    api
      .post("signup", { username, email, password })
      .then(() => {
        navigate("/login");
      })
      .catch((err) => {
        setError("Errore durante la registrazione. Riprova.");
        console.error("Errore nel sign up:", err);
      });
  };

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h2 className="title is-4">📝 Registrazione</h2>
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
          <div className="field">
            <label className="label">Email</label>
            <input
              className="input"
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="field">
            <label className="label">Password</label>
            <input
              className="input"
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
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