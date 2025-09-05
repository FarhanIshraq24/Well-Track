package com.pharmacyfinder;

import com.pharmacyfinder.config.SimpleDatabaseManager;
import java.util.List;
import java.util.Map;

public class TestSQLiteDatabase {
    public static void main(String[] args) {
        System.out.println("🧪 Testing SQLite Database Integration");
        
        try {
            // Initialize database
            SimpleDatabaseManager dbManager = SimpleDatabaseManager.getInstance();
            
            // Test search for Paracetamol near Dhaka
            System.out.println("\n🔍 Searching for Paracetamol near Dhaka...");
            List<Map<String, Object>> results = dbManager.findNearbyPharmacies(23.7461, 90.3742, "Paracetamol");
            
            System.out.println("📊 Found " + results.size() + " results:");
            
            for (Map<String, Object> pharmacy : results) {
                System.out.println("  🏥 " + pharmacy.get("name"));
                System.out.println("     📍 " + pharmacy.get("address"));
                System.out.println("     📞 " + pharmacy.get("phone"));
                System.out.println("     💊 Has Stock: " + pharmacy.get("hasStock"));
                System.out.println("     📏 Distance: " + String.format("%.2f km", (Double)pharmacy.get("distance")));
                System.out.println();
            }
            
            // Test search for another medicine
            System.out.println("🔍 Searching for Ibuprofen...");
            results = dbManager.findNearbyPharmacies(23.7461, 90.3742, "Ibuprofen");
            System.out.println("📊 Found " + results.size() + " results for Ibuprofen");
            
            dbManager.shutdown();
            System.out.println("✅ Database test completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Database test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
