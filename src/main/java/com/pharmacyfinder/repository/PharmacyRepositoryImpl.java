package com.pharmacyfinder.repository;

import com.pharmacyfinder.model.Pharmacy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class PharmacyRepositoryImpl implements PharmacyRepository {
    
    private final EntityManager entityManager;
    
    public PharmacyRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Override
    public List<Pharmacy> findAll() {
        return entityManager.createQuery("SELECT p FROM Pharmacy p ORDER BY p.name", Pharmacy.class)
                .getResultList();
    }
    
    @Override
    public Optional<Pharmacy> findById(Long id) {
        Pharmacy pharmacy = entityManager.find(Pharmacy.class, id);
        return Optional.ofNullable(pharmacy);
    }
    
    @Override
    public List<Pharmacy> findByNameContainingIgnoreCase(String name) {
        return entityManager.createQuery(
                "SELECT p FROM Pharmacy p WHERE UPPER(p.name) LIKE UPPER(:name) ORDER BY p.name", 
                Pharmacy.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }
    
    @Override
    public List<Pharmacy> findNearbyPharmacies(double latitude, double longitude, double radiusKm) {
        // Using Haversine formula to find nearby pharmacies
        String query = """
            SELECT p, (6371 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * 
            cos(radians(p.longitude) - radians(:lng)) + sin(radians(:lat)) * 
            sin(radians(p.latitude)))) AS distance 
            FROM Pharmacy p 
            WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * 
            cos(radians(p.longitude) - radians(:lng)) + sin(radians(:lat)) * 
            sin(radians(p.latitude)))) < :radius 
            ORDER BY distance
            """;
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createQuery(query)
                .setParameter("lat", latitude)
                .setParameter("lng", longitude)
                .setParameter("radius", radiusKm)
                .getResultList();
        
        return results.stream()
                .map(result -> {
                    Pharmacy pharmacy = (Pharmacy) result[0];
                    Double distance = (Double) result[1];
                    pharmacy.setDistance(distance);
                    return pharmacy;
                })
                .toList();
    }
    
    @Override
    public List<Pharmacy> findPharmaciesWithMedicineInStock(String medicineName) {
        return entityManager.createQuery("""
            SELECT DISTINCT p FROM Pharmacy p 
            JOIN p.medicineStock s 
            JOIN s.medicine m 
            WHERE UPPER(m.name) LIKE UPPER(:medicineName) AND s.quantity > 0 
            ORDER BY p.name
            """, Pharmacy.class)
            .setParameter("medicineName", "%" + medicineName + "%")
            .getResultList();
    }
    
    @Override
    public List<Pharmacy> findNearbyPharmaciesWithMedicine(double latitude, double longitude, double radiusKm, String medicineName) {
        String query = """
            SELECT p, (6371 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * 
            cos(radians(p.longitude) - radians(:lng)) + sin(radians(:lat)) * 
            sin(radians(p.latitude)))) AS distance 
            FROM Pharmacy p 
            JOIN p.medicineStock s 
            JOIN s.medicine m 
            WHERE UPPER(m.name) LIKE UPPER(:medicineName) 
            AND s.quantity > 0 
            AND (6371 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * 
            cos(radians(p.longitude) - radians(:lng)) + sin(radians(:lat)) * 
            sin(radians(p.latitude)))) < :radius 
            ORDER BY distance
            """;
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createQuery(query)
                .setParameter("lat", latitude)
                .setParameter("lng", longitude)
                .setParameter("radius", radiusKm)
                .setParameter("medicineName", "%" + medicineName + "%")
                .getResultList();
        
        return results.stream()
                .map(result -> {
                    Pharmacy pharmacy = (Pharmacy) result[0];
                    Double distance = (Double) result[1];
                    pharmacy.setDistance(distance);
                    return pharmacy;
                })
                .toList();
    }
    
    @Override
    public List<Pharmacy> find24HourPharmacies() {
        return entityManager.createQuery(
                "SELECT p FROM Pharmacy p WHERE p.is24Hours = true ORDER BY p.name", 
                Pharmacy.class)
                .getResultList();
    }
    
    @Override
    public Pharmacy save(Pharmacy pharmacy) {
        if (pharmacy.getId() == null) {
            entityManager.persist(pharmacy);
            return pharmacy;
        } else {
            return entityManager.merge(pharmacy);
        }
    }
    
    @Override
    public void deleteById(Long id) {
        Pharmacy pharmacy = entityManager.find(Pharmacy.class, id);
        if (pharmacy != null) {
            entityManager.remove(pharmacy);
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        Long count = entityManager.createQuery(
                "SELECT COUNT(p) FROM Pharmacy p WHERE p.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }
}
