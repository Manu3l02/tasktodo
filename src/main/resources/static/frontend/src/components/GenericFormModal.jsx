import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import '../styles/ModalStyles.css';

const GenericFormModal = ({
  isOpen,
  onClose,
  onSubmit,
  initialTask,
  initialEvent,
  initialRange,
  selectedMode
}) => {
  const [mode, setMode] = useState(selectedMode || 'TASK');
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    dueDateTime: '',
    startDateTime: '',
    endDateTime: '',
    reminderMinutesBefore: ''
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (mode === 'TASK') {
      setFormData({
        title: initialTask?.title ?? '',
        description: initialTask?.description ?? '',
        dueDateTime: initialTask?.dueDateTime?.slice(0, 16) ?? '',
        startDateTime: '',
        endDateTime: '',
        reminderMinutesBefore: initialTask?.reminderMinutesBefore?.toString() ?? ''
      });
    } else {
      const base = {
        title: '',
        description: '',
        startDateTime: '',
        endDateTime: '',
        reminderMinutesBefore: ''
      };

      const source = initialEvent || initialRange || base;

      setFormData({
        title: source.title ?? '',
        description: source.description ?? '',
        startDateTime: source.startDateTime?.slice(0, 16) ?? '',
        endDateTime: source.endDateTime?.slice(0, 16) ?? '',
        dueDateTime: '',
        reminderMinutesBefore: source.reminderMinutesBefore?.toString() ?? ''
      });
    }
    setErrors({});
  }, [mode, initialTask, initialEvent, initialRange, isOpen]);

  useEffect(() => {
    if (isOpen && selectedMode) {
      setMode(selectedMode.toUpperCase());
    }
  }, [selectedMode, isOpen]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'reminderMinutesBefore' && value !== "" 
        ? parseInt(value, 10)
        : value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const newErrors = {};
    if (!formData.title.trim()) {
      newErrors.title = "Il titolo è obbligatorio";
    }

    if (mode === 'TASK' && !formData.dueDateTime) {
      newErrors.dueDateTime = "La data di scadenza è obbligatoria";
    }

    if (mode === 'EVENT') {
      if (!formData.startDateTime) {
        newErrors.startDateTime = "Data e ora di inizio obbligatorie";
      }
      if (!formData.endDateTime) {
        newErrors.endDateTime = "Data e ora di fine obbligatorie";
      }
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setErrors({});
    onSubmit(formData, mode);
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
                  className={`input ${errors.title ? 'is-danger' : ''}`}
                  name="title"
                  value={formData.title}
                  onChange={handleChange}
                  placeholder={mode === 'TASK' ? 'Titolo della task' : 'Titolo dell\'evento'}
                />
              </div>
              {errors.title && <p className="help is-danger">{errors.title}</p>}
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
                      className={`input ${errors.dueDateTime ? 'is-danger' : ''}`}
                      type="datetime-local"
                      name="dueDateTime"
                      value={formData.dueDateTime}
                      onChange={handleChange}
                    />
                  </div>
                </div>

                <div className="field">
                  <label className="label">Promemoria (prima della scadenza)</label>
                  <div className="control">
                    <div className="select is-fullwidth">
                      <select
                        name="reminderMinutesBefore"
                        value={formData.reminderMinutesBefore}
                        onChange={handleChange}
                      >
                        <option value="">Seleziona un intervallo</option>
                        <option value="5">5 minuti prima</option>
                        <option value="10">10 minuti prima</option>
                        <option value="15">15 minuti prima</option>
                        <option value="30">30 minuti prima</option>
                        <option value="60">1 ora prima</option>
                      </select>
                    </div>
                  </div>
                </div>
              </>
            ) : (
              <>
                <div className="field">
                  <label className="label">Start</label>
                  <div className="control">
                    <input
                      className={`input ${errors.startDateTime ? 'is-danger' : ''}`}
                      type="datetime-local"
                      name="startDateTime"
                      value={formData.startDateTime}
                      onChange={handleChange}
                    />
                  </div>
                  {errors.startDateTime && <p className="help is-danger">{errors.startDateTime}</p>}
                </div>

                <div className="field">
                  <label className="label">End</label>
                  <div className="control">
                    <input
                      className={`input ${errors.endDateTime ? 'is-danger' : ''}`}
                      type="datetime-local"
                      name="endDateTime"
                      value={formData.endDateTime}
                      onChange={handleChange}
                    />
                  </div>
                  {errors.endDateTime && <p className="help is-danger">{errors.endDateTime}</p>}
                </div>

                <div className="field">
                  <label className="label">Promemoria (prima dell’inizio)</label>
                  <div className="control">
                    <div className="select is-fullwidth">
                      <select
                        name="reminderMinutesBefore"
                        value={formData.reminderMinutesBefore}
                        onChange={handleChange}
                      >
                        <option value="">Nessun promemoria</option>
                        <option value="5">5 minuti prima</option>
                        <option value="10">10 minuti prima</option>
                        <option value="15">15 minuti prima</option>
                        <option value="30">30 minuti prima</option>
                        <option value="60">1 ora prima</option>
                      </select>
                    </div>
                  </div>
                </div>
              </>
            )}
          </section>

          <footer className="modal-card-foot">
            <button className="button is-primary" type="submit">
              {mode === 'TASK'
                ? (initialTask ? 'Aggiorna Task' : 'Aggiungi Task')
                : (initialEvent ? 'Aggiorna Event' : 'Aggiungi Event')}
            </button>
            <button className="button" type="button" onClick={onClose}>Annulla</button>
          </footer>
        </form>
      </motion.div>
    </div>
  );
};

export default GenericFormModal;