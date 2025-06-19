// App.js

import React, { useEffect, useState } from "react";
import Dashboard from "./components/Dashboard";
import LoginForm from "./components/LoginForm";
import MyTask from "./components/MyTask";
import Settings from "./components/Settings";
import RegistrationForm from "./components/RegistrationForm";
import "./styles/App.scss";
import { AuthProvider, useAuth } from "./components/AuthContext"; // Corrected import
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { HelmetProvider } from "react-helmet-async";
import PrivateRoute from "./components/PrivateRoute";
import api from "./api"; // Import the configured axios instance

function AppContent() {
  const { login } = useAuth();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token"); // Check for token

    if (token) {
      api.get("/check-auth")
        .then(res => {
          login({ 
            username: res.data.username, 
            userId: res.data.userId,
            token: token
          });
          setLoading(false);
        })
        .catch(err => {
          console.error("Authentication check failed:", err);
          localStorage.removeItem("token");
          setLoading(false);
        });
    } else {
      setLoading(false);
    }
  }, [login]);


  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="app-container">
      <Routes>
        <Route
          path="/tasks"
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/my-tasks"
          element={
            <PrivateRoute>
              <MyTask />
            </PrivateRoute>
          }
        />
		<Route
		  path="/settings"
		  element={
		    <PrivateRoute>
		      <Settings />
		    </PrivateRoute>
		  }
		/>
        <Route path="/login" element={<LoginForm />} />
        <Route path="/signup" element={<RegistrationForm />} />
        <Route
          path="/"
          element={localStorage.getItem("token") ? <Navigate to="/tasks" /> : <Navigate to="/login" />}
        />
      </Routes>
    </div>
  );
}

function App() {
  return (
	<HelmetProvider>
      <AuthProvider>
        <Router>
          <AppContent />
        </Router>
      </AuthProvider>
	</HelmetProvider>
  );
}

export default App;