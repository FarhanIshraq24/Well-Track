// frontend/src/index.js
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

// Global styles
const globalStyles = `
  * {
    box-sizing: border-box;
  }

  body {
    margin: 0;
    padding: 0;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }

  .user-location-marker {
    background: none;
    border: none;
    font-size: 20px;
  }

  .pharmacy-marker {
    background: none;
    border: none;
    font-size: 16px;
    transition: all 0.3s ease;
  }

  .pharmacy-marker.selected {
    font-size: 20px;
    filter: drop-shadow(0 0 5px rgba(102, 126, 234, 0.6));
  }

  .leaflet-popup-content {
    margin: 8px 12px;
  }

  .leaflet-popup-content h3 {
    color: #333;
    font-size: 1.1em;
  }
`;

// Inject global styles
const styleElement = document.createElement('style');
styleElement.textContent = globalStyles;
document.head.appendChild(styleElement);

// Create root and render app
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<App />);
