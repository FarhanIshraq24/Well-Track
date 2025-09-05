package com.pharmacyfinder;

import com.pharmacyfinder.config.DatabaseManager;
import com.pharmacyfinder.model.Pharmacy;
import com.pharmacyfinder.repository.PharmacyRepository;
import com.pharmacyfinder.repository.PharmacyRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class DatabaseTest {
    public static void main(String[] args) {
        try {
            System.out.println("🔌 Testing database connection...");
            
            DatabaseManager dbManager = DatabaseManager.getInstance();
            EntityManager entityManager = dbManager.createEntityManager();
            
            System.out.println("✅ Database manager initialized successfully");
            
            PharmacyRepository pharmacyRepo = new PharmacyRepositoryImpl(entityManager);
            
            System.out.println("📋 Fetching all pharmacies from database...");
            List<Pharmacy> pharmacies = pharmacyRepo.findAll();
            
            System.out.println("📊 Found " + pharmacies.size() + " pharmacies:");
            
            for (Pharmacy pharmacy : pharmacies) {
                System.out.println("  🏥 " + pharmacy.getName() + " - " + pharmacy.getAddress());
            }
            
            // Test nearby pharmacy search
            System.out.println("\n🔍 Testing nearby pharmacy search (around Dhaka)...");
            List<Pharmacy> nearbyPharmacies = pharmacyRepo.findNearbyPharmacies(23.7461, 90.3742, 5.0);
            
            System.out.println("📍 Found " + nearbyPharmacies.size() + " nearby pharmacies:");
            for (Pharmacy pharmacy : nearbyPharmacies) {
                System.out.println("  🏥 " + pharmacy.getName() + " - Distance: " + String.format("%.2f km", pharmacy.getDistance()));
            }
            
            // Test medicine search
            System.out.println("\n💊 Testing medicine search (Paracetamol)...");
            List<Pharmacy> pharmaciesWithMedicine = pharmacyRepo.findPharmaciesWithMedicineInStock("Paracetamol");
            
            System.out.println("💊 Found " + pharmaciesWithMedicine.size() + " pharmacies with Paracetamol:");
            for (Pharmacy pharmacy : pharmaciesWithMedicine) {
                System.out.println("  🏥 " + pharmacy.getName() + " has Paracetamol in stock");
            }
            
            entityManager.close();
            dbManager.shutdown();
            
            System.out.println("\n✅ Database integration test completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error during database test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
