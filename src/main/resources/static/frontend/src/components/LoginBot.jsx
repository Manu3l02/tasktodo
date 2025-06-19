import React, { useEffect, useRef } from 'react';
import '../styles/LoginBot.css';

const clamp = (val, min, max) => Math.max(min, Math.min(max, val));

const LoginBot = ({ isPasswordActive }) => {
  const botRef = useRef();

  useEffect(() => {
    const handleMouseMove = (e) => {
      const { clientX, clientY } = e;
      const bot = botRef.current;
      if (!bot) return;

      const rect = bot.getBoundingClientRect();
      const centerX = rect.left + rect.width / 2;
      const centerY = rect.top + rect.height / 2;

      const angleX = clamp((clientX - centerX) / 20, -15, 15);
      const angleY = clamp((clientY - centerY) / 20, -15, 15);

      bot.style.transform = `rotateY(${angleX}deg) rotateX(${angleY * -1}deg)`;
    };

    window.addEventListener('mousemove', handleMouseMove);
    return () => window.removeEventListener('mousemove', handleMouseMove);
  }, []);

  return (
    <div className={`robot-wrapper ${isPasswordActive ? 'hide-eyes' : ''}`}>
      <div className="robot-3d" ref={botRef}>
        <div className="robot-head">
          <div className="antenna"></div>
          <div className="eyes">
            <div className="eye left"></div>
            <div className="eye right"></div>
          </div>
        </div>
        <div className="robot-body">
          <div className="arm left-arm"></div>
          <div className="arm right-arm"></div>
        </div>
      </div>
    </div>
  );
};

export default LoginBot;