import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import SearchForm from './components/SearchForm';
import PharmacyList from './components/PharmacyList';
import MapView from './components/MapView';

const AppContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
`;

const Header = styled.header`
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  padding: 1rem 2rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
`;

const Title = styled.h1`
  color: white;
  margin: 0;
  font-size: 2rem;
  font-weight: 300;
  text-align: center;
`;

const MainContent = styled.div`
  display: flex;
  flex: 1;
  overflow: hidden;
`;

const LeftPanel = styled.div`
  width: 400px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  display: flex;
  flex-direction: column;
  border-right: 1px solid rgba(0, 0, 0, 0.1);
`;

const MapContainer = styled.div`
  flex: 1;
  position: relative;
`;

const LoadingOverlay = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
`;

const LoadingSpinner = styled.div`
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;

  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
`;

function App() {
  const [pharmacies, setPharmacies] = useState([]);
  const [userLocation, setUserLocation] = useState(null);
  const [loading, setLoading] = useState(false);
  const [selectedPharmacy, setSelectedPharmacy] = useState(null);

  useEffect(() => {
    getCurrentLocation();
  }, []);

  const getCurrentLocation = async () => {
    setLoading(true);
    try {
      if (window.javaAPI && window.javaAPI.getCurrentLocation) {
        const location = await window.javaAPI.getCurrentLocation();
        setUserLocation(location);
      } else {
        // Fallback for development
        setUserLocation({
          latitude: 23.8103,
          longitude: 90.4125,
          address: "Dhaka, Bangladesh"
        });
      }
    } catch (error) {
      console.error('Error getting location:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (medicine) => {
    if (!userLocation || !medicine.trim()) return;

    setLoading(true);
    console.log('🔍 Searching for:', medicine, 'at location:', userLocation);
    
    try {
      // Try to call the Java backend first
      if (window.javaAPI && window.javaAPI.findNearbyPharmacies) {
        console.log('📞 Calling Java backend API...');
        const results = await window.javaAPI.findNearbyPharmacies(
          userLocation.latitude,
          userLocation.longitude,
          medicine
        );
        console.log('✅ Received results from backend:', results);
        setPharmacies(results);
      } else {
        console.log('⚠️ Java backend not available, using realistic mock data');
        // More realistic mock data based on actual database
        const mockPharmacies = [
          {
            name: "City Pharmacy",
            address: "123 Main Street, Dhanmondi, Dhaka-1205",
            phone: "+880-2-9612345",
            latitude: 23.7461,
            longitude: 90.3742,
            hasStock: medicine.toLowerCase().includes('paracetamol') || medicine.toLowerCase().includes('ibuprofen'),
            searchedMedicine: medicine,
            distance: 0.8
          },
          {
            name: "Green Life Pharmacy",
            address: "789 New Market Road, New Market, Dhaka-1205",
            phone: "+880-2-9513456",
            latitude: 23.7275,
            longitude: 90.3854,
            hasStock: medicine.toLowerCase().includes('paracetamol') || medicine.toLowerCase().includes('metformin'),
            searchedMedicine: medicine,
            distance: 1.2
          },
          {
            name: "Square Pharmacy",
            address: "654 Elephant Road, Kathalbagan, Dhaka-1205",
            phone: "+880-2-9654321",
            latitude: 23.7500,
            longitude: 90.3889,
            hasStock: medicine.toLowerCase().includes('paracetamol'),
            searchedMedicine: medicine,
            distance: 0.5
          },
          {
            name: "Apollo Pharmacy",
            address: "147 Panthapath, Kawran Bazar, Dhaka-1215",
            phone: "+880-2-9556677",
            latitude: 23.7510,
            longitude: 90.3931,
            hasStock: medicine.toLowerCase().includes('omeprazole'),
            searchedMedicine: medicine,
            distance: 1.0
          }
        ].filter(pharmacy => pharmacy.hasStock || Math.random() > 0.5);
        
        console.log('📊 Using mock pharmacies:', mockPharmacies);
        setPharmacies(mockPharmacies);
      }
    } catch (error) {
      console.error('❌ Error searching pharmacies:', error);
      // Fallback to minimal mock data on error
      setPharmacies([
        {
          name: "Error - Using Fallback Data",
          address: "Please check backend connection",
          phone: "N/A",
          latitude: 23.7461,
          longitude: 90.3742,
          hasStock: false,
          searchedMedicine: medicine,
          distance: 0
        }
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <AppContainer>
      <Header>
        <Title>🏥 Pharmacy Finder</Title>
      </Header>

      <MainContent>
        <LeftPanel>
          <SearchForm
            onSearch={handleSearch}
            userLocation={userLocation}
            loading={loading}
          />
          <PharmacyList
            pharmacies={pharmacies}
            selectedPharmacy={selectedPharmacy}
            onSelectPharmacy={setSelectedPharmacy}
          />
        </LeftPanel>

        <MapContainer>
          {loading && (
            <LoadingOverlay>
              <LoadingSpinner />
            </LoadingOverlay>
          )}
          <MapView
            pharmacies={pharmacies}
            userLocation={userLocation}
            selectedPharmacy={selectedPharmacy}
            onSelectPharmacy={setSelectedPharmacy}
          />
        </MapContainer>
      </MainContent>
    </AppContainer>
  );
}

export default App;