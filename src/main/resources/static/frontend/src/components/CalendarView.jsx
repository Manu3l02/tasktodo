// CalendarView.jsx
import React, { useState } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import listPlugin from '@fullcalendar/list';
import PropTypes from 'prop-types';

const VIEW_KEY = 'calendarView';

const CalendarView = ({ items, onSelectRange }) => {
  // Carica vista da localStorage (default: settimana)
  const [initialView, setInitialView] = useState(
    localStorage.getItem(VIEW_KEY) || 'timeGridWeek'
  );

  // Prepara gli eventi
  const events = (items || []).map(item => (
    item.type === 'EVENT'
      ? { id: item.id, title: item.title, start: item.startDateTime, end: item.endDateTime, allDay: false }
      : { id: item.id, title: item.title, start: item.dueDate, allDay: true }
  ));

  // Calcola scroll iniziale centrato sull'ora corrente
  const now = new Date();
  const scrollHour = String(Math.max(0, now.getHours() - 4)).padStart(2,'0');
  const scrollTime = `${scrollHour}:00:00`;

  // Callback quando cambia vista
  const handleDatesSet = arg => {
    const viewType = arg.view.type;
    setInitialView(viewType);
    localStorage.setItem(VIEW_KEY, viewType);
  };

  return (
    <div className="calendar-container">
      <FullCalendar
        plugins={[ dayGridPlugin, timeGridPlugin, interactionPlugin, listPlugin ]}
        initialView={initialView}
        initialDate={new Date().toISOString().slice(0,10)}
        headerToolbar={{
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
        }}
        height="100%"
        selectable={true}
        select={info => onSelectRange(info)}
        events={events}
        scrollTime={scrollTime}
        slotMinTime="06:00:00"
        slotMaxTime="23:00:00"
        nowIndicator={true}
        eventTimeFormat={{
          hour: '2-digit',
          minute: '2-digit',
          hour12: false
        }}
        datesSet={handleDatesSet}
      />
    </div>
  );
};

CalendarView.propTypes = {
  items: PropTypes.array.isRequired,
  onSelectRange: PropTypes.func.isRequired
};

export default CalendarView;