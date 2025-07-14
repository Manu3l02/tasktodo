import React, { useState, useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import api from "../api";
import { Helmet } from "react-helmet-async";
import "./../styles/AuthForm.css";

const ResetPasswordForm = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");

  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (password !== confirmPassword) {
      setError("Le password non coincidono.");
      return;
    }

    try {
      await api.post("/auth/reset-password", {
        token,
        newPassword: password,
      });
      setSuccess(true);
      setTimeout(() => navigate("/login"), 3000);
    } catch (err) {
      console.error("Errore reset password:", err);
      setError("Token non valido o scaduto.");
    }
  };

  if (!token) {
    return (
      <div className="auth-container">
        <p className="notification is-danger">Token mancante nell’URL.</p>
      </div>
    );
  }

  return (
    <>
      <Helmet>
        <title>Resetta password - ToDoList App</title>
      </Helmet>

      <div className="auth-container">
        <div className="auth-box">
          <h2 className="title is-4">Imposta una nuova password</h2>

          {success ? (
            <p className="notification is-success">
              ✅ Password aggiornata! Verrai reindirizzato al login...
            </p>
          ) : (
            <form onSubmit={handleSubmit}>
              {error && <p className="notification is-danger">{error}</p>}
              <div className="field" style={{ position: "relative" }}>
                <label className="label">Nuova password</label>
                <input
                  className="input"
                  type={showNewPassword ? "text" : "password"}
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
				  onClick={() => setShowNewPassword(prev => !prev)}
				>
				  {showNewPassword ? "👁️" : "🔒"}
				</span>
              </div>
              <div className="field" style={{position:"relative"}}>
                <label className="label">Conferma password</label>
                <input
                  className="input"
                  type={showConfirmPassword ? "text" : "password"}
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
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
				  onClick={() => setShowConfirmPassword(prev => !prev)}
				>
				  {showConfirmPassword ? "👁️" : "🔒"}
				</span>
              </div>
              <button className="button is-primary" type="submit">
                Cambia password
              </button>
            </form>
          )}
        </div>
      </div>
    </>
  );
};

export default ResetPasswordForm;