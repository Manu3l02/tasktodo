import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./AuthContext";
import "./../styles/LoginForm.css";

const LoginForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = (e) => {
    e.preventDefault();

    axios
      .post(
        "http://localhost:8080/api/login",
        { username, password },
        { withCredentials: true }
      )
      .then((response) => {
        if (response.data) {
          login(response.data); // Usa il metodo login del context!
          navigate("/tasks");
        }
      })
      .catch((err) => {
        setError("Credenziali errate. Riprova.");
        console.error("Errore nel login", err);
      });
  };

  return (
    <div className="container">
      <div className="box login-box">
        <h2 className="title is-4 has-text-centered">Login</h2>
        {error && <p className="notification is-danger">{error}</p>}
        <form onSubmit={handleSubmit}>
          <div className="field">
            <label className="label">UserName</label>
            <div className="control">
              <input
                className="input"
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>
          </div>

          <div className="field">
            <label className="label">Password</label>
            <div className="control">
              <input
                className="input"
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </div>

          <div className="control">
            <button className="button is-primary is-fullwidth" type="submit">
              Accedi
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default LoginForm;
