// Sidebar.js
import React, { useEffect } from 'react';
import { useAuth } from './AuthContext';
import '../styles/Sidebar.css';
import { Link, useLocation } from 'react-router-dom';
import { Home, ListTodo, Settings, LogOut } from 'lucide-react';

const Sidebar = () => {
  const { user, logout } = useAuth();
  const location = useLocation();

  useEffect(() => {
    console.log("Sidebar: Oggetto utente aggiornato dal contesto:", user);
    console.log("Sidebar: user?.profileImageUrl:", user?.profileImageUrl);
  }, [user]);

  const navLinks = [
    { to: '/tasks', label: 'Dashboard', icon: <Home size={18} /> },
    { to: '/my-tasks', label: 'My Task', icon: <ListTodo size={18} /> },
    { to: '/settings', label: 'Settings', icon: <Settings size={18} /> }
  ];

  return (
    <aside className="sidebar light">
      <div className="sidebar-content">
        <div className="profile">
          <img
            // Logica corretta: usa profileImageUrl se esiste, altrimenti il default
            src={user?.profileImageUrl ? `https://localhost:8443${user.profileImageUrl}` : '/default-avatar.png'} // <-- CORRETTO QUI!
            alt="Avatar"
            className="avatar"
          />
          <p className="username">{user?.username || 'Utente'}</p>
        </div>

        <nav className="menu">
          <p className="menu-label">Navigazione</p>
          <ul className="menu-list">
            {navLinks.map(link => (
              <li key={link.to}>
                <Link
                  className={`menu-link ${location.pathname === link.to ? 'is-active' : ''}`}
                  to={link.to}
                >
                  {link.icon}
                  <span>{link.label}</span>
                </Link>
              </li>
            ))}
          </ul>
        </nav>
      </div>

      <div className="sidebar-footer">
        <button className="menu-link logout" onClick={logout}>
          <LogOut size={18} />
          <span>Logout</span>
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;