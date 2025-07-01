import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import '../styles/ModalStyles.css';

const GenericFormModal = ({
  isOpen,
  onClose,
  onSubmitTask,
  onSubmitEvent,
  initialTask,
  initialEvent,
}) => {
  const [mode, setMode] = useState('TASK');
  const [formData, setFormData] = useState({});

  useEffect(() => {
    if (mode === 'TASK') {
      setFormData({
        title: initialTask?.title || '',
        description: initialTask?.description || '',
        dueDate: initialTask?.dueDate ? initialTask.dueDate.split('T')[0] : '',
        reminderDateTime: initialTask?.reminderDateTime || '',
      });
    } else {
      setFormData({
        title: initialEvent?.title || '',
        description: initialEvent?.description || '',
        startDateTime: initialEvent?.startDateTime || '',
        endDateTime: initialEvent?.endDateTime || '',
      });
    }
  }, [mode, initialTask, initialEvent, isOpen]);

  const handleChange = e => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = e => {
    e.preventDefault();
    if (!formData.title.trim()) return;
    if (mode === 'TASK') onSubmitTask(formData);
    else onSubmitEvent(formData);
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="modal is-active">
      <div className="modal-background" onClick={onClose}></div>
      <motion.div
        className="modal-card custom-generic-modal-box generic-modal-card-custom"
        initial={{ scale: 0.8, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        exit={{ scale: 0.8, opacity: 0 }}
      >
        <header className="modal-card-head modal-header-custom">
          <p className="modal-card-title">
            {mode === 'TASK' ? 'Nuova Task' : 'Nuovo Evento'}
          </p>
          <div className="field is-grouped is-grouped-centered">
            <p className="control">
              <button
                type="button"
                className={`toggle-button ${mode === 'TASK' ? 'selected' : ''}`}
                onClick={() => setMode('TASK')}
              >
                Task
              </button>
            </p>
            <p className="control">
              <button
                type="button"
                className={`toggle-button ${mode === 'EVENT' ? 'selected' : ''}`}
                onClick={() => setMode('EVENT')}
              >
                Evento
              </button>
            </p>
          </div>
          <button className="delete" aria-label="close" onClick={onClose}></button>
        </header>

        <form onSubmit={handleSubmit}>
          <section className="modal-card-body">
            <div className="field">
              <label className="label">Titolo</label>
              <div className="control">
                <input
                  className="input"
                  name="title"
                  value={formData.title}
                  onChange={handleChange}
                  placeholder={mode === 'TASK' ? 'Titolo della task' : 'Titolo dell\'evento'}
                  required
                />
              </div>
            </div>

            <div className="field">
              <label className="label">Descrizione</label>
              <div className="control">
                <textarea
                  className="textarea"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  placeholder="Descrizione dettagliata"
                />
              </div>
            </div>

            {mode === 'TASK' ? (
              <>
                <div className="field">
                  <label className="label">Data di scadenza</label>
                  <div className="control">
                    <input
                      className="input"
                      type="date"
                      name="dueDate"
                      value={formData.dueDate}
                      onChange={handleChange}
                    />
                  </div>
                </div>
                <div className="field">
                  <label className="label">Promemoria</label>
                  <div className="control">
                    <input
                      className="input"
                      type="datetime-local"
                      name="reminderDateTime"
                      value={formData.reminderDateTime}
                      onChange={handleChange}
                    />
                  </div>
                </div>
              </>
            ) : (
              <>
                <div className="field">
                  <label className="label">Start</label>
                  <div className="control">
                    <input
                      className="input"
                      type="datetime-local"
                      name="startDateTime"
                      value={formData.startDateTime}
                      onChange={handleChange}
                      required
                    />
                  </div>
                </div>
                <div className="field">
                  <label className="label">End</label>
                  <div className="control">
                    <input
                      className="input"
                      type="datetime-local"
                      name="endDateTime"
                      value={formData.endDateTime}
                      onChange={handleChange}
                      required
                    />
                  </div>
                </div>
              </>
            )}
          </section>

          <footer className="modal-card-foot">
            <button className="button is-primary" type="submit">
              {mode === 'TASK' ? (initialTask ? 'Aggiorna Task' : 'Aggiungi Task') : (initialEvent ? 'Aggiorna Event' : 'Aggiungi Event')}
            </button>
            <button className="button" type="button" onClick={onClose}>Annulla</button>
          </footer>
        </form>
      </motion.div>
    </div>
  );
};

export default GenericFormModal;