import React, { createContext, useContext, useState, useEffect, useCallback } from "react";
import api from "../api";

export const AuthContext = createContext(null); // Meglio inizializzare con null o un oggetto di default

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  // La funzione login ora accetta un oggetto user completo e lo imposta
  const login = useCallback((userData) => {
    localStorage.setItem("token", userData.token); // Assicurati che token sia sempre presente in userData
    api.defaults.headers.common["Authorization"] = `Bearer ${userData.token}`;
    setUser(userData); // <-- MODIFICA QUI: imposta l'intero oggetto userData
    console.log("User logged in:", userData);
  }, []); // login è stabile grazie a useCallback

  const logout = useCallback(() => {
    api.post("/logout")
      .then(() => {
        localStorage.removeItem("token");
        delete api.defaults.headers.common["Authorization"]; // Rimuovi l'header di autorizzazione
        setUser(null);
        window.location.href = "/login"; // Reindirizza dopo il logout
      })
      .catch((err) => console.error("Errore durante il logout:", err));
  }, []);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      api.get("/check-auth")
        .then((res) => {
          // Quando check-auth risponde, assicurati che la risposta contenga tutte le info necessarie
          login({
            username: res.data.username,
            userId: res.data.userId,
            token: token, // Il token viene già gestito da localStorage
            profileImageUrl: res.data.profileImageUrl // <-- ASSICURATI CHE check-auth LA RESTITUISCA
          });
        })
        .catch((err) => {
          console.warn("Sessione non valida o errore check-auth:", err.response?.data || err.message);
          localStorage.removeItem("token");
          delete api.defaults.headers.common["Authorization"]; // Rimuovi l'header di autorizzazione
          setUser(null);
        });
    }
  }, [login]); // Dipende da login, che è stabile

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth deve essere usato all\'interno di un AuthProvider');
  }
  return context;
};