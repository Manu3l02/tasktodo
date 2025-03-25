import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./../styles/RegistrationForm.css"; // Crea e personalizza questo file CSS come preferisci

const RegistrationForm = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    // Invia i dati di registrazione al backend
    axios
      .post("http://localhost:8080/api/signup", { username, email, password })
      .then((response) => {
        // Dopo la registrazione, puoi reindirizzare l'utente alla pagina di login
		console.log("Dopo il post a signup navigate login")
        navigate("/login");
      })
      .catch((err) => {
        setError("Errore durante la registrazione. Riprova.");
        console.error("Errore nel sign up:", err);
      });
  };

  return (
    <div className="registration-container">
      <div className="registration-box">
        <h2 className="title">Registrazione</h2>
        {error && <p className="error">{error}</p>}
        <form onSubmit={handleSubmit}>
          <div className="field">
            <label className="label">Username</label>
            <input
              className="input"
              type="text"
              placeholder="Inserisci il tuo username"
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
              placeholder="Inserisci la tua email"
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
              placeholder="Inserisci la password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button className="button" type="submit">
            Registrati
          </button>
        </form>
      </div>
    </div>
  );
};

export default RegistrationForm;
