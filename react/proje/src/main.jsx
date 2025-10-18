import React from 'react';
axios.defaults.withCredentials = true; // 🔥 Cookie'leri otomatik gönder
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import './index.css';
import axios from "axios";

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <App />
    </React.StrictMode>,
);
