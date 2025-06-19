// StatsCharts.jsx
import React, { useEffect, useState } from 'react';
import { CircularProgressbar, buildStyles } from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';

const StatsCharts = ({ tasks }) => {
  const [animatedPercent, setAnimatedPercent] = useState({
    completed: 0,
    inProgress: 0,
    toDo: 0,
  });

  const completed = tasks.filter(t => t.completed).length;
  const inProgress = tasks.filter(t => !t.completed && !t.reminderSent).length;
  const toDo = tasks.filter(t => !t.completed && t.reminderSent).length;
  const total = tasks.length;

  const percent = count => (total ? Math.round((count / total) * 100) : 0);

  useEffect(() => {
    const timer = setTimeout(() => {
      setAnimatedPercent({
        completed: percent(completed),
        inProgress: percent(inProgress),
        toDo: percent(toDo),
      });
    }, 400);
    return () => clearTimeout(timer);
  }, [tasks]);

  const stats = [
    { label: 'Completed', value: animatedPercent.completed, color: '#4caf50' },
    { label: 'In Progress', value: animatedPercent.inProgress, color: '#ff9800' },
    { label: 'To Do', value: animatedPercent.toDo, color: '#f44336' }
  ];

  return (
    <div className="stats-section" style={{ display: 'flex', gap: '2rem', flexWrap: 'wrap' }}>
      {stats.map((stat, i) => (
        <div key={i} className="stat-card" style={{ width: 100 }}>
          <CircularProgressbar
            value={stat.value}
            text={`${stat.value}%`}
            styles={buildStyles({
              pathColor: stat.color,
              textColor: stat.color,
              trailColor: '#eee',
            })}
          />
          <p style={{ textAlign: 'center', marginTop: '0.5rem' }}>{stat.label}</p>
        </div>
      ))}
    </div>
  );
};

export default StatsCharts;
