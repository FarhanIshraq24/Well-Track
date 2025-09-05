#!/bin/bash
echo "🔧 Initializing Welltrack Database..."

# Create data directory
mkdir -p data

# Compile the project
echo "📦 Compiling project..."
mvn compile -q

# Create a simple Java class to initialize the database
cat > src/main/java/com/pharmacyfinder/InitDatabase.java << 'EOF'
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
EOF

# Compile and run the initialization
echo "🏗️  Running database initialization..."
mvn compile -q
java -cp "target/classes:$(mvn dependency:build-classpath -q 2>/dev/null | tail -n 1)" com.pharmacyfinder.InitDatabase

# Check if database files were created
echo "📁 Checking database files..."
ls -la data/

echo "✅ Database initialization complete!"
