package com.pharmacyfinder.repository;

import com.pharmacyfinder.model.Medicine;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class MedicineRepositoryImpl implements MedicineRepository {
    
    private final EntityManager entityManager;
    
    public MedicineRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Override
    public List<Medicine> findAll() {
        return entityManager.createQuery("SELECT m FROM Medicine m ORDER BY m.name", Medicine.class)
                .getResultList();
    }
    
    @Override
    public Optional<Medicine> findById(Long id) {
        Medicine medicine = entityManager.find(Medicine.class, id);
        return Optional.ofNullable(medicine);
    }
    
    @Override
    public List<Medicine> findByNameContainingIgnoreCase(String name) {
        return entityManager.createQuery(
                "SELECT m FROM Medicine m WHERE UPPER(m.name) LIKE UPPER(:name) ORDER BY m.name", 
                Medicine.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }
    
    @Override
    public List<Medicine> findByGenericNameContainingIgnoreCase(String genericName) {
        return entityManager.createQuery(
                "SELECT m FROM Medicine m WHERE UPPER(m.genericName) LIKE UPPER(:genericName) ORDER BY m.name", 
                Medicine.class)
                .setParameter("genericName", "%" + genericName + "%")
                .getResultList();
    }
    
    @Override
    public List<Medicine> findByCategory(String category) {
        return entityManager.createQuery(
                "SELECT m FROM Medicine m WHERE m.category = :category ORDER BY m.name", 
                Medicine.class)
                .setParameter("category", category)
                .getResultList();
    }
    
    @Override
    public List<String> findAllCategories() {
        return entityManager.createQuery(
                "SELECT DISTINCT m.category FROM Medicine m WHERE m.category IS NOT NULL ORDER BY m.category", 
                String.class)
                .getResultList();
    }
    
    @Override
    public Medicine save(Medicine medicine) {
        if (medicine.getId() == null) {
            entityManager.persist(medicine);
            return medicine;
        } else {
            return entityManager.merge(medicine);
        }
    }
    
    @Override
    public void deleteById(Long id) {
        Medicine medicine = entityManager.find(Medicine.class, id);
        if (medicine != null) {
            entityManager.remove(medicine);
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        Long count = entityManager.createQuery(
                "SELECT COUNT(m) FROM Medicine m WHERE m.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }
}

//vcdsjhbdsvdfjk vjvdfhjv dfjv fdv vjnf vjfd vjd h jhhh djv fdajv afjhv fdjhsfhjvfse jhsdf jhd vshv j dfbjv dsbv bhv b vbdf vjdfv jdv dbfjv bjdv jbdf vjdf
//hvcbdsuvbfuhvbfvuy hbhusbcvgbv

// frontend/src/components/PharmacyList.jsx
//import React from 'react';
//        import styled from 'styled-components';
//
//        const ListContainer = styled.div`
//flex: 1;
//overflow-y: auto;
//padding: 0;
//        `;
//
//        const EmptyState = styled.div`
//display: flex;
//flex-direction: column;
//align-items: center;
//justify-content: center;
//height: 200px;
//color: #999;
//text-align: center;
//padding: 2rem;
//
//  .icon {
//    font-size: 3rem;
//    margin-bottom: 1rem;
//}
//`;
//
//        const PharmacyCard = styled.div`
//padding: 1.5rem;
//border-bottom: 1px solid rgba(0, 0, 0, 0.1);
//cursor: pointer;
//transition: all 0.3s ease;
//position: relative;
//
//  &:hover {
//    background: rgba(102, 126, 234, 0.05);
//}
//
//${props => props.selected && `
//    background: rgba(102, 126, 234, 0.1);
//    border-left: 4px solid #667eea;
//  `}
//
//${props => props.hasStock ? `
//    &::before {
//        content: '✅';
//        position: absolute;
//        top: 1rem;
//        right: 1rem;
//    }
//  ` : `
//    &::before {
//        content: '❌';
//        position: absolute;
//        top: 1rem;
//        right: 1rem;
//    }
//  `}
//`;
//
//        const PharmacyName = styled.h3`
//margin: 0 0 0.5rem 0;
//color: #333;
//font-size: 1.1rem;
//font-weight: 600;
//        `;
//
//        const PharmacyAddress = styled.p`
//margin: 0 0 0.5rem 0;
//color: #666;
//font-size: 0.9rem;
//line-height: 1.4;
//        `;
//
//        const PharmacyPhone = styled.p`
//margin: 0 0 0.5rem 0;
//color: #667eea;
//font-size: 0.9rem;
//font-weight: 500;
//        `;
//
//        const StockStatus = styled.div`
//display: inline-block;
//padding: 0.25rem 0.5rem;
//border-radius: 4px;
//font-size: 0.8rem;
//font-weight: 600;
//
//${props => props.hasStock ? `
//    background: rgba(76, 175, 80, 0.1);
//    color: #4CAF50;
//  ` : `
//    background: rgba(244, 67, 54, 0.1);
//    color: #f44336;
//  `}
//`;
//
//        const Distance = styled.span`
//color: #999;
//font-size: 0.8rem;
//float: right;
//`;
//
//function PharmacyList({ pharmacies, selectedPharmacy, onSelectPharmacy }) {
//        if (pharmacies.length === 0) {
//        return (
//      <ListContainer>
//        <EmptyState>
//          <div className="icon">🏥</div>
//          <div>
//<h3>No pharmacies found</h3>
//            <p>Search for a medicine to find nearby pharmacies</p>
//          </div>
//        </EmptyState>
//      </ListContainer>
//        );
//        }
//
//        return (
//<ListContainer>
//{pharmacies.map((pharmacy, index) => (
//        <PharmacyCard
//    key={index}
//    selected={selectedPharmacy === pharmacy}
//    hasStock={pharmacy.hasStock}
//    onClick={() => onSelectPharmacy(pharmacy)}
//            >
//            <PharmacyName>
//            {pharmacy.name}
//    {pharmacy.distance && (
//            <Distance>{pharmacy.distance.toFixed(1)} km</Distance>
//            )}
//          </PharmacyName>
//        <PharmacyAddress>📍 {pharmacy.address}</PharmacyAddress>
//        <PharmacyPhone>📞 {pharmacy.phone}</PharmacyPhone>
//        <StockStatus hasStock={pharmacy.hasStock}>
//        {pharmacy.hasStock ? 'In Stock' : 'Out of Stock'}
//        </StockStatus>
//        </PharmacyCard>
//      ))}
//    </ListContainer>
//        );
//        }
//bvhhhjjjguyvouvuybhfbvhfebnhjefrvb feruhvbfrhuruhbvuhv fhbvuhrvbrvhvbrfhjvbeuhv hv dhbvuhebvehfbvehbveufhvb ehuv dfuhv wejhvbwefhveuhfvb efhv eh
//hvbdfhvb dfuhv dfuv fdsu dfsuhv dsfuhv dfhvsfvuhf vd dhbv db
//export default Pharhbvcfdhuvbeuhvbuebvebvehibvehuvb