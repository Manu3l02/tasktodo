import React, { useState } from "react";
import api from "../api";
import { Helmet } from "react-helmet-async";
import "./../styles/AuthForm.css";

const ForgotPasswordForm = () => {
  const [email, setEmail] = useState("");
  const [sent, setSent] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      await api.post("/auth/forgot-password", { email });
      setSent(true);
    } catch (err) {
      console.error("Errore nel reset:", err);
      setError("Errore durante l'invio dell'email. Controlla l'indirizzo.");
    }
  };

  return (
    <>
      <Helmet>
        <title>Password dimenticata - ToDoList App</title>
      </Helmet>

      <div className="auth-container">
        <div className="auth-box">
          <h2 className="title is-4">Recupera password</h2>

          {sent ? (
            <p className="notification is-success">
              📧 Se l’email è corretta, riceverai un link per resettare la password.
            </p>
          ) : (
            <form onSubmit={handleSubmit}>
              {error && <p className="notification is-danger">{error}</p>}
              <div className="field">
                <label className="label">Email</label>
                <input
                  className="input"
                  type="email"
                  placeholder="Inserisci la tua email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              <button className="button is-primary" type="submit">
                Invia link di reset
              </button>
            </form>
          )}
        </div>
      </div>
    </>
  );
};

export default ForgotPasswordForm;