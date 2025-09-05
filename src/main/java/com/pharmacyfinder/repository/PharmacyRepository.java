package com.pharmacyfinder.repository;

import com.pharmacyfinder.model.Pharmacy;
import java.util.List;
import java.util.Optional;

public interface PharmacyRepository {
    
    /**
     * Find all pharmacies
     */
    List<Pharmacy> findAll();
    
    /**
     * Find pharmacy by ID
     */
    Optional<Pharmacy> findById(Long id);
    
    /**
     * Find pharmacies by name (case-insensitive)
     */
    List<Pharmacy> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find pharmacies within a radius of given coordinates
     */
    List<Pharmacy> findNearbyPharmacies(double latitude, double longitude, double radiusKm);
    
    /**
     * Find pharmacies that have a specific medicine in stock
     */
    List<Pharmacy> findPharmaciesWithMedicineInStock(String medicineName);
    
    /**
     * Find nearby pharmacies that have a specific medicine in stock
     */
    List<Pharmacy> findNearbyPharmaciesWithMedicine(double latitude, double longitude, double radiusKm, String medicineName);
    
    /**
     * Find 24-hour pharmacies
     */
    List<Pharmacy> find24HourPharmacies();
    
    /**
     * Save or update pharmacy
     */
    Pharmacy save(Pharmacy pharmacy);
    
    /**
     * Delete pharmacy by ID
     */
    void deleteById(Long id);
    
    /**
     * Check if pharmacy exists by ID
     */
    boolean existsById(Long id);
}
