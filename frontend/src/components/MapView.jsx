import React, { useEffect, useRef } from 'react';
import styled from 'styled-components';

const MapContainer = styled.div`
  width: 100%;
  height: 100%;
  position: relative;
`;

const MapPlaceholder = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(45deg, #f0f0f0 25%, transparent 25%),
              linear-gradient(-45deg, #f0f0f0 25%, transparent 25%),
              linear-gradient(45deg, transparent 75%, #f0f0f0 75%),
              linear-gradient(-45deg, transparent 75%, #f0f0f0 75%);
  background-size: 20px 20px;
  background-position: 0 0, 0 10px, 10px -10px, -10px 0px;
  color: #666;
  font-size: 1.2rem;
  text-align: center;
`;

function MapView({ pharmacies, userLocation, selectedPharmacy, onSelectPharmacy }) {
  const mapRef = useRef(null);
  const mapInstance = useRef(null);

  useEffect(() => {
    // Initialize Leaflet map when component mounts
    if (typeof L !== 'undefined' && mapRef.current && !mapInstance.current) {
      initializeMap();
    }
  }, []);

  useEffect(() => {
    // Update markers when pharmacies or selection changes
    if (mapInstance.current) {
      updateMarkers();
    }
  }, [pharmacies, selectedPharmacy, userLocation]);

  const initializeMap = () => {
    const defaultCenter = userLocation ?
      [userLocation.latitude, userLocation.longitude] :
      [23.8103, 90.4125]; // Dhaka, Bangladesh

    mapInstance.current = L.map(mapRef.current).setView(defaultCenter, 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors'
    }).addTo(mapInstance.current);

    updateMarkers();
  };

  const updateMarkers = () => {
    if (!mapInstance.current) return;

    // Clear existing markers
    mapInstance.current.eachLayer((layer) => {
      if (layer instanceof L.Marker) {
        mapInstance.current.removeLayer(layer);
      }
    });

    // Add user location marker
    if (userLocation) {
      const userIcon = L.divIcon({
        html: '📍',
        iconSize: [30, 30],
        className: 'user-location-marker'
      });

      L.marker([userLocation.latitude, userLocation.longitude], { icon: userIcon })
        .addTo(mapInstance.current)
        .bindPopup('Your Location');
    }

    // Add pharmacy markers
    pharmacies.forEach((pharmacy) => {
      const isSelected = selectedPharmacy === pharmacy;
      const hasStock = pharmacy.hasStock;

      const icon = L.divIcon({
        html: hasStock ? '🟢' : '🔴',
        iconSize: isSelected ? [35, 35] : [25, 25],
        className: isSelected ? 'pharmacy-marker selected' : 'pharmacy-marker'
      });

      const marker = L.marker([pharmacy.latitude, pharmacy.longitude], { icon })
        .addTo(mapInstance.current);

      const popupContent = `
        <div style="min-width: 200px;">
          <h3 style="margin: 0 0 10px 0;">${pharmacy.name}</h3>
          <p style="margin: 5px 0;"><strong>Address:</strong> ${pharmacy.address}</p>
          <p style="margin: 5px 0;"><strong>Phone:</strong> ${pharmacy.phone}</p>
          <p style="margin: 5px 0;">
            <strong>Stock:</strong>
            <span style="color: ${hasStock ? 'green' : 'red'};">
              ${hasStock ? 'Available' : 'Out of Stock'}
            </span>
          </p>
        </div>
      `;

      marker.bindPopup(popupContent);

      marker.on('click', () => {
        onSelectPharmacy(pharmacy);
      });
    });

    // Fit map to show all markers
    if (pharmacies.length > 0 || userLocation) {
      const group = new L.featureGroup();

      if (userLocation) {
        group.addLayer(L.marker([userLocation.latitude, userLocation.longitude]));
      }

      pharmacies.forEach(pharmacy => {
        group.addLayer(L.marker([pharmacy.latitude, pharmacy.longitude]));
      });

      mapInstance.current.fitBounds(group.getBounds().pad(0.1));
    }
  };

  return (
    <MapContainer>
      {typeof L !== 'undefined' ? (
        <div ref={mapRef} style={{ width: '100%', height: '100%' }} />
      ) : (
        <MapPlaceholder>
          <div>
            <h3>🗺️ Interactive Map</h3>
            <p>Map will load with pharmacy locations</p>
            <small>Requires Leaflet.js library</small>
          </div>
        </MapPlaceholder>
      )}
    </MapContainer>
  );
}

export default MapView;