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
