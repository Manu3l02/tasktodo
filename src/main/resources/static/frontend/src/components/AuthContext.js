import React, { createContext, useContext, useState, useEffect } from "react";
import axios from "axios";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  // Verifica se l'utente è già loggato al primo caricamento (sia da localStorage che da sessione)
  useEffect(() => {
    const storedUser = localStorage.getItem("user");

    if (storedUser) {
      // Se l'utente è già presente nel localStorage, impostalo nello stato
      setUser(JSON.parse(storedUser)); 
	  // Fai la richiesta per verificare la sessione
	  axios
	    .get("http://localhost:8080/api/check-auth", { withCredentials: true })
	    .then((res) => {
	      setUser(res.data); // Se autenticato, salva l'utente in stato
	      localStorage.setItem("user", JSON.stringify(res.data)); // Salva nel localStorage
	    })
	    .catch((err) => {
	      setUser(null); // Se l'utente non è autenticato
	      console.error("Utente non Autenticato (sono react)", err);
	    });    
	 }
  }, []);


  const login = (userData) => {
    localStorage.setItem("user", JSON.stringify(userData));
    setUser(userData);
  };
  
  const logout = () => {
    // Effettua la richiesta di logout al backend
    axios.post("http://localhost:8080/api/logout", {}, { withCredentials: true })
      .then(() => {
        // Rimuovi l'utente dal contesto
        setUser(null);

        // Rimuovi l'utente da localStorage
        localStorage.removeItem("user");
        
        // Reimposta axios per non inviare credenziali
        axios.defaults.withCredentials = false;

        // Reindirizza alla pagina di login
        window.location.href = "/login"; // Si può usare anche <Navigate />.
      })
      .catch((err) => console.log('Errore durante il logout:', err));
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
