/* CalendarView.jsx */
import React, { useState } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import listPlugin from '@fullcalendar/list';
import PropTypes from 'prop-types';
import '../styles/CalendarView.css';

const VIEW_KEY = 'calendarView';

const CalendarView = ({ items, onSelectRange }) => {
  const [initialView, setInitialView] = useState(
    localStorage.getItem(VIEW_KEY) || 'timeGridWeek'
  );

  const events = (items || []).map(item =>
    item.type === 'EVENT'
      ? {
          id: item.id,
          title: item.title,
          start: item.startDateTime,
          end: item.endDateTime,
          allDay: false
        }
      : {
          id: item.id,
          title: item.title,
          start: item.dueDate,
          allDay: true
        }
  );

  const now = new Date();
  const scrollHour = String(Math.max(0, now.getHours() - 1)).padStart(2, '0');
  const scrollTime = `${scrollHour}:00:00`;

  const handleDatesSet = arg => {
    const viewType = arg.view.type;
    setInitialView(viewType);
    localStorage.setItem(VIEW_KEY, viewType);
  };

  // nuovo: render header di evento con time sopra e title sotto
  const renderEventContent = arg => {
    return (
      <div className="fc-custom-header">
        <div className="fc-custom-title">{arg.event.title}</div>
        <div className="fc-custom-time">{arg.timeText}</div>
      </div>
    );
  };

  return (
    <div className="calendar-container">
      <FullCalendar
        plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin, listPlugin]}
        initialView={initialView}
        initialDate={new Date().toISOString().slice(0, 10)}
        headerToolbar={{
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay'
        }}
        height="100%"
        selectable={true}
        selectMirror={true}
        selectAllow={() => true}
        select={info => onSelectRange(info)}
        events={events}
        scrollTime={scrollTime}
        slotMinTime="00:00:00"
        slotMaxTime="24:00:00"
        slotDuration="00:15:00"
        slotLabelInterval="01:00"
        slotLabelFormat={{
          hour: '2-digit',
          minute: '2-digit',
          hour12: false
        }}
        eventTimeFormat={{
          hour: '2-digit',
          minute: '2-digit',
          hour12: false
        }}
        nowIndicator={true}
        datesSet={handleDatesSet}
        eventContent={renderEventContent}  // header custom
      />
    </div>
  );
};

CalendarView.propTypes = {
  items: PropTypes.array.isRequired,
  onSelectRange: PropTypes.func.isRequired
};

export default CalendarView;