import React, { useState, useRef } from 'react';
import api from '../api';
import { useAuth } from './AuthContext';

const ProfileImageUpload = () => {
  const { user, login } = useAuth();
  const [file, setFile] = useState(null);
  const [dragging, setDragging] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);
  const fileInputRef = useRef();

  const handleDrop = (e) => {
    e.preventDefault();
    setDragging(false);
    const droppedFile = e.dataTransfer.files[0];
    if (droppedFile && droppedFile.type.startsWith('image/')) {
      setFile(droppedFile);
      setMessage('');
      setIsError(false);
    }
  };

  const handleUpload = async () => {
    if (!file) {
      setMessage("Seleziona un'immagine prima!");
      setIsError(true);
      return;
    }
    setUploading(true);
    setMessage('');
    setIsError(false);

    const formData = new FormData();
    formData.append('file', file);

    try {
      const res = await api.post(`/users/${user.userId}/upload-profile-image`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });

      login({ ...user, profileImageUrl: res.data.url });
      setMessage("Immagine modificata con successo!");
    } catch (err) {
      console.error('Errore upload:', err);
      setMessage(err.response?.data || "Errore durante l'upload");
      setIsError(true);
    } finally {
      setUploading(false);
    }
  };

  return (
    <div className="upload-wrapper">
      <div
        className={`upload-dropzone ${dragging ? 'dragging' : ''}`}
        onClick={() => fileInputRef.current.click()}
        onDragOver={(e) => {
          e.preventDefault();
          setDragging(true);
        }}
        onDragLeave={() => setDragging(false)}
        onDrop={handleDrop}
      >
        <span className="upload-icon">+</span>
        <p className="upload-text">Modifica Foto</p>
        <input
          type="file"
          accept="image/*"
          ref={fileInputRef}
          onChange={(e) => {
            setFile(e.target.files[0]);
            setMessage('');
            setIsError(false);
          }}
          hidden
        />
      </div>

      <button
        className="button is-primary mt-3"
        onClick={handleUpload}
        disabled={uploading}
      >
        {uploading ? 'Caricamento...' : 'Carica immagine'}
      </button>

      {message && (
        <p className={`mt-2 ${isError ? 'has-text-danger' : 'has-text-success'}`}>
          {message}
        </p>
      )}

      {file && (
        <div className="mt-4">
          <p>Anteprima:</p>
          <img
            src={URL.createObjectURL(file)}
            alt="Preview"
            style={{ width: '120px', borderRadius: '8px', marginTop: '8px' }}
          />
        </div>
      )}
    </div>
  );
};

export default ProfileImageUpload;