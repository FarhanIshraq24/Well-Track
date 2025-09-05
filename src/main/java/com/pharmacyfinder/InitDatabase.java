package com.pharmacyfinder;

import com.pharmacyfinder.config.DatabaseManager;

public class InitDatabase {
    public static void main(String[] args) {
        System.out.println("🔌 Initializing database...");
        
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            System.out.println("✅ Database initialized successfully!");
            
            // Check if we can connect
            var entityManager = dbManager.createEntityManager();
            System.out.println("✅ Entity manager created successfully!");
            entityManager.close();
            
            dbManager.shutdown();
            System.out.println("✅ Database shutdown successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
