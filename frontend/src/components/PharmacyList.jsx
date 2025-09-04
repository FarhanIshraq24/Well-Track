// frontend/src/components/PharmacyList.jsx
import React from 'react';
import styled from 'styled-components';

const ListContainer = styled.div`
  flex: 1;
  overflow-y: auto;
  padding: 0;
`;

const EmptyState = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #999;
  text-align: center;
  padding: 2rem;

  .icon {
    font-size: 3rem;
    margin-bottom: 1rem;
  }
`;

const PharmacyCard = styled.div`
  padding: 1.5rem;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;

  &:hover {
    background: rgba(102, 126, 234, 0.05);
  }

  ${props => props.selected && `
    background: rgba(102, 126, 234, 0.1);
    border-left: 4px solid #667eea;
  `}

  ${props => props.hasStock ? `
    &::before {
      content: '✅';
      position: absolute;
      top: 1rem;
      right: 1rem;
    }
  ` : `
    &::before {
      content: '❌';
      position: absolute;
      top: 1rem;
      right: 1rem;
    }
  `}
`;

const PharmacyName = styled.h3`
  margin: 0 0 0.5rem 0;
  color: #333;
  font-size: 1.1rem;
  font-weight: 600;
`;

const PharmacyAddress = styled.p`
  margin: 0 0 0.5rem 0;
  color: #666;
  font-size: 0.9rem;
  line-height: 1.4;
`;

const PharmacyPhone = styled.p`
  margin: 0 0 0.5rem 0;
  color: #667eea;
  font-size: 0.9rem;
  font-weight: 500;
`;

const StockStatus = styled.div`
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 600;

  ${props => props.hasStock ? `
    background: rgba(76, 175, 80, 0.1);
    color: #4CAF50;
  ` : `
    background: rgba(244, 67, 54, 0.1);
    color: #f44336;
  `}
`;

const Distance = styled.span`
  color: #999;
  font-size: 0.8rem;
  float: right;
`;

function PharmacyList({ pharmacies, selectedPharmacy, onSelectPharmacy }) {
  if (pharmacies.length === 0) {
    return (
      <ListContainer>
        <EmptyState>
          <div className="icon">🏥</div>
          <div>
            <h3>No pharmacies found</h3>
            <p>Search for a medicine to find nearby pharmacies</p>
          </div>
        </EmptyState>
      </ListContainer>
    );
  }

  return (
    <ListContainer>
      {pharmacies.map((pharmacy, index) => (
        <PharmacyCard
          key={index}
          selected={selectedPharmacy === pharmacy}
          hasStock={pharmacy.hasStock}
          onClick={() => onSelectPharmacy(pharmacy)}
        >
          <PharmacyName>
            {pharmacy.name}
            {pharmacy.distance && (
              <Distance>{pharmacy.distance.toFixed(1)} km</Distance>
            )}
          </PharmacyName>
          <PharmacyAddress>📍 {pharmacy.address}</PharmacyAddress>
          <PharmacyPhone>📞 {pharmacy.phone}</PharmacyPhone>
          <StockStatus hasStock={pharmacy.hasStock}>
            {pharmacy.hasStock ? 'In Stock' : 'Out of Stock'}
          </StockStatus>
        </PharmacyCard>
      ))}
    </ListContainer>
  );
}

export default PharmacyList;