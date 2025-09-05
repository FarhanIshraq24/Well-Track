package com.pharmacyfinder.repository;

import com.pharmacyfinder.model.Medicine;
import java.util.List;
import java.util.Optional;

public interface MedicineRepository {
    
    /**
     * Find all medicines
     */
    List<Medicine> findAll();
    
    /**
     * Find medicine by ID
     */
    Optional<Medicine> findById(Long id);
    
    /**
     * Find medicines by name (case-insensitive)
     */
    List<Medicine> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find medicines by generic name (case-insensitive)
     */
    List<Medicine> findByGenericNameContainingIgnoreCase(String genericName);
    
    /**
     * Find medicines by category
     */
    List<Medicine> findByCategory(String category);
    
    /**
     * Find all medicine categories
     */
    List<String> findAllCategories();
    
    /**
     * Save or update medicine
     */
    Medicine save(Medicine medicine);
    
    /**
     * Delete medicine by ID
     */
    void deleteById(Long id);
    
    /**
     * Check if medicine exists by ID
     */
    boolean existsById(Long id);
}
