// Dashboard.jsx
import React, { useEffect, useState } from 'react';
import Sidebar from './Sidebar';
import GenericFormModal from './GenericFormModal';
import { ToastContainer, toast } from 'react-toastify';
import { useAuth } from './AuthContext';
import { format, parseISO } from 'date-fns';
import { Plus } from 'lucide-react';
import CalendarView from './CalendarView';
import { Helmet } from "react-helmet-async";
import api from '../api';
import '../styles/Dashboard.css';

const Dashboard = () => {
  const { user } = useAuth();
  const [items, setItems] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [events, setEvents] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedMode, setSelectedMode] = useState('task');
  const [editingItem, setEditingItem] = useState(null);
  const [initialRange, setInitialRange] = useState(null);

  useEffect(() => {
    if (!user) return;
    api.get('/calendar/items')
      .then(res => {
        setItems(res.data);
        setTasks(res.data.filter(i => i.type === 'TASK'));
        setEvents(res.data.filter(i => i.type === 'EVENT'));
      })
      .catch(console.error);
  }, [user]);

  const openModal = (mode = 'task', item = null, range = null) => {
    setSelectedMode(mode);
    setEditingItem(item);
    setInitialRange(range);
    setIsModalOpen(true);
  };
  const closeModal = () => {
    setIsModalOpen(false);
    setEditingItem(null);
    setInitialRange(null);
  };

  const handleSubmit = async (formData, mode) => {
    const payload = mode === 'TASK'
      ? {
          title: formData.title,
          description: formData.description,
          dueDateTime: formData.dueDateTime || null,
          reminderMinutesBefore: formData.reminderMinutesBefore || null
        }
      : {
          title: formData.title,
          description: formData.description,
          startDateTime: formData.startDateTime,
          endDateTime: formData.endDateTime,
          reminderMinutesBefore: formData.reminderMinutesBefore || null
        };

    const endpoint = mode === 'TASK' ? '/calendar/tasks' : '/calendar/events';
    const method = editingItem && editingItem.id ? api.put : api.post;
    const url = editingItem && editingItem.id ? `${endpoint}/${editingItem.id}` : endpoint;

    try {
      await method(url, payload);
      const res = await api.get('/calendar/items');
      setItems(res.data);
      setTasks(res.data.filter(i => i.type === 'TASK'));
      setEvents(res.data.filter(i => i.type === 'EVENT'));
      toast.success('Salvato!');
      closeModal();
    } catch (err) {
      // 🔴 Qui catturiamo errori di validazione
      if (err.response?.status === 400 && err.response.data?.errors) {
        const errors = err.response.data.errors;
        const errorMessages = Object.values(errors).join('\n');
        toast.error(errorMessages);
        console.error("Errori di validazione:", errors);
      } else {
        toast.error('Errore sconosciuto');
        console.error(err);
      }
    }
  };

  const handleDelete = (mode, id) => {
    const endpoint = mode === 'task' ? '/calendar/tasks' : '/calendar/events';
    api.delete(`${endpoint}/${id}`)
      .then(() => {
        if (mode === 'task') setTasks(tasks.filter(t => t.id !== id));
        else setEvents(events.filter(e => e.id !== id));
        toast.info('Eliminato!');
      })
      .catch(console.error);
  };

  const handleSelectRange = info => {
    const isAllDay = info.allDay;

    openModal(
      isAllDay ? 'task' : 'event',
      null,
      {
        startDateTime: info.startStr,
        endDateTime: info.endStr,
      }
    );
  };
  
  // Funzione per formattare orario
  const formatTime = (datetimeStr) => {
    return new Date(datetimeStr).toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
	<>
	<Helmet>
	  <title>Dashboard - ToDoList App</title>
	</Helmet>
    <div className='dashboard-container'>
      <ToastContainer position='bottom-right' autoClose={3000} />
      <Sidebar />
      <div className='main-content'>
        <header className='dashboard-header'>
          <h1 className='title is-3'>Dashboard</h1>
        </header>

        <div className='buttons mb-5'>
          <button className='button is-primary' onClick={() => openModal()}>
            <Plus /> Aggiungi
          </button>
        </div>

		<div className='columns is-variable is-6'>

		  {/* TASK LIST */}
		  <div className='column is-6'>
		    <div className='box list-box'>
		      <h2 className='subtitle is-4 mb-2'>Le mie Task</h2>

		      <div className='list has-hoverable-list-items has-overflow-ellipsis'>
		        {tasks.length === 0 && (
		          <div className='light-text'>Nessuna task.</div>
		        )}

		        {tasks.map(t => (
		          <div key={t.id} className='list-item list-row'>
		            <div className='is-flex is-align-items-center is-justify-content-space-between is-flex-grow-1'>

		              <div className='list-item-text'>
		                <div className='is-flex is-justify-content-space-between mb-1'>
		                  <span className='has-text-weight-semibold'>{t.title}</span>
		                  <span className='light-text is-size-7'>{t.dueDate}</span>
		                </div>
		                {t.description && (
		                  <div className='list-item-description is-size-7'>{t.description}</div>
		                )}
		              </div>

		              <div className='list-item-buttons'>
		                <button
		                  className='button is-info is-small mr-2'
		                  onClick={() => openModal('task', t)}
		                >
		                  <span className='icon'>✏️</span>
		                </button>
		                <button
		                  className='button is-danger is-small'
		                  onClick={() => handleDelete('task', t.id)}
		                >
		                  <span className='icon'>🗑️</span>
		                </button>
		              </div>

		            </div>
		          </div>
		        ))}
		      </div>
		    </div>
		  </div>

		  {/* EVENT LIST */}
		  <div className='column is-6'>
		    <div className='box list-box'>
		      <h2 className='subtitle is-4 mb-2'>I miei Eventi</h2>

		      <div className='list has-hoverable-list-items has-overflow-ellipsis'>
		        {events.length === 0 && (
		          <div className='light-text'>Nessun evento.</div>
		        )}

		        {events.map(e => (
		          <div key={e.id} className='list-item list-row'>
		            <div className='is-flex is-align-items-center is-justify-content-space-between is-flex-grow-1'>

		              <div className='list-item-text'>
		                <div className='is-flex is-justify-content-space-between mb-1'>
		                  <span className='has-text-weight-semibold'>{e.title}</span>
		                  <span className='light-text is-size-7'>
		                    {formatTime(e.startDateTime)} - {formatTime(e.endDateTime)}
		                  </span>
		                </div>
		                {e.description && (
		                  <div className='list-item-description is-size-7'>{e.description}</div>
		                )}
		              </div>

		              <div className='list-item-buttons'>
		                <button
		                  className='button is-info is-small mr-2'
		                  onClick={() => openModal('event', e)}
		                >
		                  <span className='icon'>✏️</span>
		                </button>
		                <button
		                  className='button is-danger is-small'
		                  onClick={() => handleDelete('event', e.id)}
		                >
		                  <span className='icon'>🗑️</span>
		                </button>
		              </div>

		            </div>
		          </div>
		        ))}
		      </div>
		    </div>
		  </div>

		</div>

        <CalendarView items={items} onSelectRange={handleSelectRange} />
      </div>

	  <GenericFormModal
	    isOpen={isModalOpen}
	    onClose={closeModal}
	    onSubmit={handleSubmit}
	    initialTask={selectedMode === 'task' ? editingItem : null}
	    initialEvent={selectedMode === 'event' ? editingItem : null}
		initialRange={initialRange}
		selectedMode={selectedMode}
	  />

    </div>
	</>
  );
};

export default Dashboard;