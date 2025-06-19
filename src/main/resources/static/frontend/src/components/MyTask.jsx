// MyTask.jsx
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { CircularProgressbar, buildStyles } from 'react-circular-progressbar';
import CalendarView from './CalendarView';
import TaskCard from './TaskCard';
import Sidebar from './Sidebar';
import { useAuth } from './AuthContext';
import 'react-circular-progressbar/dist/styles.css';
import '../styles/CalendarView.css';
import '../styles/Dashboard.css'; // Per mantenere lo stile coerente

const MyTask = () => {
  const { user, logout } = useAuth();
  const [tasks, setTasks] = useState([]);

  useEffect(() => {
    if (user) {
      axios.get('/calendar/items', { withCredentials: true })
        .then(res => {
          const taskItems = res.data.filter(i => i.type === 'TASK');
          setTasks(taskItems);
        })
        .catch(err => {
          if (err.response?.status === 401) logout();
          else console.error('Errore nel recupero delle task:', err);
        });
    }
  }, [user, logout]);

  const completedTasks = tasks.filter(task => task.completed);
  const incompleteTasks = tasks.filter(task => !task.completed);

  const total = tasks.length || 1;
  const percentage = Math.round((completedTasks.length / total) * 100);

  return (
    <div className="dashboard-container">
      <Sidebar />
      <div className="main-content">
        <div className="section">
          <h1 className="title has-text-white mb-5">Le mie attività</h1>

          {/* Grafico di completamento */}
          <div className="columns is-multiline is-centered mb-5">
            <div className="column is-4-desktop stat-box">
              <CircularProgressbar
                value={percentage}
                text={`${percentage}%`}
                styles={buildStyles({
                  pathColor: '#7C3AED',
                  textColor: '#fff',
                  trailColor: 'rgba(255, 255, 255, 0.2)',
                })}
              />
              <p className="has-text-centered mt-3">Completamento attività</p>
            </div>
          </div>

          {/* Lista task */}
          <div className="columns is-desktop is-variable is-6 mb-5">
            <div className="column task-list-section">
              <h2 className="subtitle has-text-white">Da completare</h2>
              <div className="task-list">
                {incompleteTasks.map(task => (
                  <TaskCard key={task.id} task={task} />
                ))}
                {incompleteTasks.length === 0 && (
                  <p className="has-text-grey-light">Nessuna attività da completare.</p>
                )}
              </div>
            </div>
            <div className="column task-list-section">
              <h2 className="subtitle has-text-white">Completate</h2>
              <div className="task-list">
                {completedTasks.map(task => (
                  <TaskCard key={task.id} task={task} />
                ))}
                {completedTasks.length === 0 && (
                  <p className="has-text-grey-light">Nessuna attività completata.</p>
                )}
              </div>
            </div>
          </div>

          {/* Calendario */}
          <div className="calendar-section">
            <CalendarView items={tasks} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyTask;
