// TaskFormModal.jsx
import React, { useState, useEffect } from 'react';

const TaskFormModal = ({ isOpen, onClose, onSubmit, initialData, initialRange, mode, setMode }) => {
  const [formData, setFormData] = useState({
    title: '', description: '', dueDate: '', reminderDateTime: '', startDateTime: '', endDateTime: ''
  });

  const [internalMode, setInternalMode] = useState(mode);

  useEffect(() => {
    setInternalMode(mode);
  }, [mode]);

  useEffect(() => {
    if (!isOpen) return;
    const toInput = dt => dt ? dt.substring(0,16) : '';

    if (initialData && initialData.id) {
      setFormData({
        title: initialData.title || '',
        description: initialData.description || '',
        dueDate: initialData.dueDate || '',
        reminderDateTime: initialData.reminderDateTime || '',
        startDateTime: toInput(initialData.startDateTime),
        endDateTime: toInput(initialData.endDateTime)
      });
    } else if (initialRange) {
      const start = toInput(initialRange.startDateTime);
      const end = toInput(initialRange.endDateTime);
      setFormData({
        title: '',
        description: '',
        dueDate: internalMode === 'task' ? start.substring(0, 10) : '',
        reminderDateTime: '',
        startDateTime: internalMode === 'event' ? start : '',
        endDateTime: internalMode === 'event' ? end : ''
      });
    } else {
      setFormData({ title:'',description:'',dueDate:'',reminderDateTime:'',startDateTime:'',endDateTime:'' });
    }
  }, [isOpen, initialData, initialRange, internalMode]);

  const handleChange = e => setFormData(prev=>({...prev,[e.target.name]:e.target.value}));

  const handleModeSwitch = (newMode) => {
    setInternalMode(newMode);
    if (initialRange && newMode === 'task') {
      setFormData(prev => ({
        ...prev,
        dueDate: prev.dueDate || initialRange.startDateTime.substring(0, 10)
      }));
    }
    if (initialRange && newMode === 'event') {
      setFormData(prev => ({
        ...prev,
        startDateTime: prev.startDateTime || initialRange.startDateTime.substring(0, 16),
        endDateTime: prev.endDateTime || initialRange.endDateTime.substring(0, 16)
      }));
    }
  };

  const submit = e => {
    e.preventDefault();
    if(formData.title.trim()) {
      setMode(internalMode);
      onSubmit(formData);
    }
  };

  if(!isOpen) return null;
  return (
    <div className='modal is-active'>
      <div className='modal-background' onClick={onClose}></div>
      <div className='modal-card' style={{width:'500px'}}>
        <header className='modal-card-head'>
          <p className='modal-card-title'>
            {internalMode==='task'? (initialData?'Modifica Task':'Nuova Task'):(initialData?'Modifica Evento':'Nuovo Evento')}
          </p>
          <button className='delete' onClick={onClose}></button>
        </header>
        <form onSubmit={submit}>
          <section className='modal-card-body'>
            <div className='field is-grouped is-grouped-centered mb-4'>
              <p className='control'>
                <button type='button' className={`button ${internalMode === 'task' ? 'is-link is-light' : ''}`} onClick={() => handleModeSwitch('task')}>Task</button>
              </p>
              <p className='control'>
                <button type='button' className={`button ${internalMode === 'event' ? 'is-link is-light' : ''}`} onClick={() => handleModeSwitch('event')}>Evento</button>
              </p>
            </div>
            <div className='field'>
              <label className='label'>Titolo</label>
              <input className='input' name='title' value={formData.title} onChange={handleChange} required />
            </div>
            <div className='field'>
              <label className='label'>Descrizione</label>
              <textarea className='textarea' name='description' value={formData.description} onChange={handleChange} />
            </div>
            {internalMode==='task'?(<>
              <div className='field'>
                <label className='label'>Data scadenza</label>
                <input className='input' type='date' name='dueDate' value={formData.dueDate} onChange={handleChange} />
              </div>
              <div className='field'>
                <label className='label'>Promemoria</label>
                <input className='input' type='datetime-local' name='reminderDateTime' value={formData.reminderDateTime} onChange={handleChange} />
              </div>
            </>):(<>
              <div className='field'>
                <label className='label'>Inizio</label>
                <input className='input' type='datetime-local' name='startDateTime' value={formData.startDateTime} onChange={handleChange} required />
              </div>
              <div className='field'>
                <label className='label'>Fine</label>
                <input className='input' type='datetime-local' name='endDateTime' value={formData.endDateTime} onChange={handleChange} required />
              </div>
            </>)}
          </section>
          <footer className='modal-card-foot'>
            <button className='button is-primary' type='submit'>{initialData?'Aggiorna':'Crea'}</button>
            <button className='button' type='button' onClick={onClose}>Annulla</button>
          </footer>
        </form>
      </div>
    </div>
  );
};
export default TaskFormModal;