import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from './AuthContext';
import "./../styles/DarkTheme.css"; 

const TaskList = () => {
  const [tasks, setTasks] = useState([]);
  const [newTask, setNewTask] = useState('');
  const { user, logout } = useAuth();

  useEffect(() => {
    if (user) {
      axios
        .get('http://localhost:8080/api/tasks', { withCredentials: true })
        .then((response) => {
          setTasks(response.data);
        })
        .catch((error) => {
          if (error.response && error.response.status === 401) {
            logout();
          } else {
            console.error('Errore nel recupero delle task:', error);
          }
        });
    }
  }, [user, logout]);

  const addTask = () => {
    if (!newTask.trim()) return;

    axios
      .post('http://localhost:8080/api/tasks', { title: newTask, completed: false }, { withCredentials: true })
      .then((response) => {
        setTasks([...tasks, response.data]);
        setNewTask('');
      })
      .catch((error) => {
        if (error.response && error.response.status === 401) {
          logout();
        } else {
          console.error('Errore nell\'aggiunta della task:', error);
        }
      });
  };

  const deleteTask = (id) => {
    axios
      .delete('http://localhost:8080/api/tasks/${id}', { withCredentials: true })
      .then(() => {
        setTasks(tasks.filter((t) => t.taskId !== id));
      })
      .catch((error) => {
        if (error.response && error.response.status === 401) {
          logout();
        } else {
          console.error('Errore nell\'eliminazione della task:', error);
        }
      });
  };

  if (!user) {
    return <p>Reindirizzamento alla pagina di login...</p>;
  }

  return (
	<div className="container dark-theme">
	  <header className="header">
	    <h1 className="title is-3 has-text-light">📝 Task Manager</h1>
	    <button className="button is-danger" onClick={logout}>Logout</button>
	  </header>

	  <div className="box dark-box">
	    <h2 className="subtitle has-text-light">Le tue Task</h2>
	    <ul>
	      {tasks.map((task) => (
	        <li key={task.taskId} className="dark-task">
	          {task.title}
	          <button className="delete-button" onClick={() => deleteTask(task.taskId)}>❌</button>
	        </li>
	      ))}
	    </ul>
	    <div className="add-task">
	      <input 
	        className="input dark-input" 
	        type="text" 
	        value={newTask} 
	        onChange={(e) => setNewTask(e.target.value)} 
	        placeholder="Aggiungi una nuova task..."
	      />
	      <button className="button is-primary" onClick={addTask}>➕</button>
	    </div>
	  </div>
	</div>
  );
};

export default TaskList;