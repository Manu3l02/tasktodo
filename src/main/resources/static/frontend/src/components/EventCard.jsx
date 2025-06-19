import React from 'react';
import { format, parseISO } from 'date-fns';
import { Edit, Trash2 } from 'lucide-react';
import { motion } from 'framer-motion';

const EventCard = ({ event, onEdit, onDelete }) => {
  return (
    <motion.div
      className="list-item"
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      exit={{ opacity: 0, y: -10 }}
    >
      <div className="list-item-content">
        <strong>{event.title}</strong>
        <p className="has-text-grey-light">{event.description || 'Nessuna descrizione'}</p>
        <p className="is-size-7 has-text-grey-light">
          {format(parseISO(event.startDateTime), 'PPpp')} - {format(parseISO(event.endDateTime), 'PPpp')}
        </p>
      </div>
      <div className="list-item-controls">
        <button className="button is-small is-info mr-1" onClick={() => onEdit(event)}>
          <Edit size={16} />
        </button>
        <button className="button is-small is-danger" onClick={() => onDelete(event.id)}>
          <Trash2 size={16} />
        </button>
      </div>
    </motion.div>
  );
};

export default EventCard;
