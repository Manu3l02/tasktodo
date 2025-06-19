// TaskCard.jsx
import React from 'react';
import { format } from 'date-fns';
import { Edit, Trash2 } from 'lucide-react';
import { motion } from 'framer-motion';

const TaskCard = ({ task, onDelete, onComplete, onEdit }) => {
  return (
    <motion.div
      className="list-item"
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      exit={{ opacity: 0, y: -10 }}
    >
      <div className="list-item-content">
        <div className="is-flex is-align-items-center mb-1">
          <input
            type="checkbox"
            checked={task.completed}
            onChange={() => onComplete(task.id)}
            className="mr-2"
          />
          <strong className={task.completed ? 'has-text-grey-light' : ''}>{task.title}</strong>
        </div>
        <p className="has-text-grey-light">{task.description || 'Nessuna descrizione'}</p>
        <p className="is-size-7 has-text-grey-light">
          Scadenza: {task.dueDate ? new Date(task.dueDate).toLocaleDateString() : '—'}
        </p>
        {task.reminderDateTime && (
          <p className="is-size-7 has-text-grey-light">
            Promemoria: {new Date(task.reminderDateTime).toLocaleString()}
          </p>
        )}
      </div>
      <div className="list-item-controls">
        <button className="button is-small is-primary mr-1" onClick={() => onEdit(task)}>
          <Edit size={16} />
        </button>
        <button className="button is-small is-danger" onClick={() => onDelete(task.id)}>
          <Trash2 size={16} />
        </button>
      </div>
    </motion.div>
  );
};

export default TaskCard;
