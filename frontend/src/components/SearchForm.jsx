// frontend/src/components/SearchForm.jsx
import React, { useState } from 'react';
import styled from 'styled-components';

const FormContainer = styled.div`
  padding: 2rem;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
`;

const FormGroup = styled.div`
  margin-bottom: 1.5rem;
`;

const Label = styled.label`
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #333;
  font-size: 0.9rem;
`;

const Input = styled.input`
  width: 100%;
  padding: 0.75rem;
  border: 2px solid transparent;
  border-radius: 8px;
  background: rgba(0, 0, 0, 0.05);
  font-size: 1rem;
  transition: all 0.3s ease;
  box-sizing: border-box;

  &:focus {
    outline: none;
    border-color: #667eea;
    background: white;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  }

  &::placeholder {
    color: #999;
  }
`;

const SearchButton = styled.button`
  width: 100%;
  padding: 0.75rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
  }

  &:active {
    transform: translateY(0);
  }

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
    box-shadow: none;
  }
`;

const LocationInfo = styled.div`
  background: rgba(102, 126, 234, 0.1);
  padding: 0.75rem;
  border-radius: 8px;
  margin-bottom: 1rem;
  font-size: 0.9rem;
  color: #555;

  .location-icon {
    margin-right: 0.5rem;
  }
`;

function SearchForm({ onSearch, userLocation, loading }) {
  const [medicine, setMedicine] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (medicine.trim()) {
      onSearch(medicine);
    }
  };

  return (
    <FormContainer>
      {userLocation && (
        <LocationInfo>
          <span className="location-icon">📍</span>
          Current location: {userLocation.address}
        </LocationInfo>
      )}

      <form onSubmit={handleSubmit}>
        <FormGroup>
          <Label htmlFor="medicine">Medicine Name</Label>
          <Input
            type="text"
            id="medicine"
            value={medicine}
            onChange={(e) => setMedicine(e.target.value)}
            placeholder="Enter medicine name (e.g., Paracetamol)"
            disabled={loading}
          />
        </FormGroup>

        <SearchButton type="submit" disabled={loading || !medicine.trim()}>
          {loading ? '🔍 Searching...' : '🔍 Find Pharmacies'}
        </SearchButton>
      </form>
    </FormContainer>
  );
}

export default SearchForm;