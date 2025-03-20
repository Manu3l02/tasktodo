import React, { useEffect, useState } from "react";
import TaskList from "./components/TaskList";
import LoginForm from "./components/LoginForm";
import "./styles/App.css";
import { AuthProvider, useAuth } from "./components/AuthContext";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import PrivateRoute from "./components/PrivateRoute";
import axios from "axios";

function AppContent() {
  const { user, login, logout } = useAuth();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 🔥 Verifica se l'utente ha una sessione valida al caricamento
    const user = localStorage.getItem("user"); // O prendi il valore dal contesto se lo usi

    if (user) { // Solo se l'utente è presente
      axios.get("http://localhost:8080/api/check-auth", { withCredentials: true })
        .then(res => {
          // Se autenticato, aggiorna il contesto auth
          login({ username: res.data }); // O passa l'intero oggetto user se preferisci
          setLoading(false);
        })
        .catch(err => {
          // Non autenticato → lascia user null
          setLoading(false);
        });
    } else {
      setLoading(false); // Se non c'è un utente, non fare la richiesta
    }
  }, [login]);


  if (loading) {
    return <div>Loading...</div>; // Evita flickering
  }

  return (
    <div className="container dark-theme">
      <h1 className="title is-2">TASK TO-DO</h1>
      <Routes>
        <Route
          path="/tasks"
          element={
            <PrivateRoute>
              <TaskList />
            </PrivateRoute>
          }
        />
        <Route path="/login" element={<LoginForm />} />
        <Route
          path="/"
          element={
            user ? <Navigate to="/tasks" /> : <Navigate to="/login" />
          }
        />
      </Routes>
    </div>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppContent />
      </Router>
    </AuthProvider>
  );
}

export default App;
