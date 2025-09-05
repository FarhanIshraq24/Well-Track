package com.pharmacyfinder.config;

import java.sql.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleDatabaseManager {
    private static SimpleDatabaseManager instance;
    private Connection connection;
    private final String DB_PATH = "./data/pharmacy_db.sqlite";
    
    private SimpleDatabaseManager() {
        initializeDatabase();
    }
    
    public static synchronized SimpleDatabaseManager getInstance() {
        if (instance == null) {
            instance = new SimpleDatabaseManager();
        }
        return instance;
    }
    
    private void initializeDatabase() {
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(Paths.get("data"));
            
            // Load SQLite JDBC driver explicitly
            Class.forName("org.sqlite.JDBC");
            
            // Connect to SQLite database (creates if doesn't exist)
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            
            // Enable foreign keys
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            
            // Initialize schema
            initializeSchema();
            
            // Initialize data if empty
            if (isEmpty()) {
                initializeData();
            }
            
            System.out.println("✅ SQLite database initialized successfully at: " + DB_PATH);
            
        } catch (Exception e) {
            System.err.println("❌ Error initializing SQLite database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initializeSchema() throws SQLException {
        String[] schemaSql = {
            // Pharmacy table
            """
            CREATE TABLE IF NOT EXISTS pharmacy (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                address TEXT NOT NULL,
                phone TEXT,
                latitude REAL NOT NULL,
                longitude REAL NOT NULL,
                is_24_hours INTEGER DEFAULT 0,
                rating REAL DEFAULT 0.0,
                created_at TEXT DEFAULT CURRENT_TIMESTAMP
            )
            """,
            
            // Medicine table
            """
            CREATE TABLE IF NOT EXISTS medicine (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                generic_name TEXT,
                category TEXT,
                unit_price REAL DEFAULT 0.00
            )
            """,
            
            // Stock table
            """
            CREATE TABLE IF NOT EXISTS pharmacy_medicine_stock (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                pharmacy_id INTEGER NOT NULL,
                medicine_id INTEGER NOT NULL,
                quantity INTEGER DEFAULT 0,
                price REAL DEFAULT 0.00,
                FOREIGN KEY (pharmacy_id) REFERENCES pharmacy(id),
                FOREIGN KEY (medicine_id) REFERENCES medicine(id),
                UNIQUE(pharmacy_id, medicine_id)
            )
            """,
            
            // Indexes
            "CREATE INDEX IF NOT EXISTS idx_pharmacy_location ON pharmacy(latitude, longitude)",
            "CREATE INDEX IF NOT EXISTS idx_medicine_name ON medicine(name)"
        };
        
        try (Statement stmt = connection.createStatement()) {
            for (String sql : schemaSql) {
                stmt.execute(sql);
            }
        }
    }
    
    private boolean isEmpty() throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM pharmacy")) {
            return rs.next() && rs.getInt(1) == 0;
        }
    }
    
    private void initializeData() throws SQLException {
        // Insert medicines first
        String[] medicines = {
            "INSERT INTO medicine (name, generic_name, category, unit_price) VALUES ('Paracetamol 500mg', 'Acetaminophen', 'Analgesic', 2.50)",
            "INSERT INTO medicine (name, generic_name, category, unit_price) VALUES ('Ibuprofen 400mg', 'Ibuprofen', 'NSAID', 3.75)",
            "INSERT INTO medicine (name, generic_name, category, unit_price) VALUES ('Amoxicillin 250mg', 'Amoxicillin', 'Antibiotic', 8.90)",
            "INSERT INTO medicine (name, generic_name, category, unit_price) VALUES ('Omeprazole 20mg', 'Omeprazole', 'Antacid', 12.50)",
            "INSERT INTO medicine (name, generic_name, category, unit_price) VALUES ('Metformin 500mg', 'Metformin', 'Antidiabetic', 5.25)"
        };
        
        // Insert pharmacies
        String[] pharmacies = {
            "INSERT INTO pharmacy (name, address, phone, latitude, longitude, is_24_hours, rating) VALUES ('City Pharmacy', '123 Main Street, Dhanmondi, Dhaka-1205', '+880-2-9612345', 23.7461, 90.3742, 1, 4.5)",
            "INSERT INTO pharmacy (name, address, phone, latitude, longitude, is_24_hours, rating) VALUES ('HealthCare Plus', '456 Park Avenue, Gulshan-1, Dhaka-1212', '+880-2-8812345', 23.7808, 90.4123, 0, 4.2)",
            "INSERT INTO pharmacy (name, address, phone, latitude, longitude, is_24_hours, rating) VALUES ('Green Life Pharmacy', '789 New Market Road, New Market, Dhaka-1205', '+880-2-9513456', 23.7275, 90.3854, 1, 4.7)",
            "INSERT INTO pharmacy (name, address, phone, latitude, longitude, is_24_hours, rating) VALUES ('Square Pharmacy', '654 Elephant Road, Kathalbagan, Dhaka-1205', '+880-2-9654321', 23.7500, 90.3889, 1, 4.8)",
            "INSERT INTO pharmacy (name, address, phone, latitude, longitude, is_24_hours, rating) VALUES ('Apollo Pharmacy', '147 Panthapath, Kawran Bazar, Dhaka-1215', '+880-2-9556677', 23.7510, 90.3931, 1, 4.6)"
        };
        
        try (Statement stmt = connection.createStatement()) {
            // Insert medicines
            for (String sql : medicines) {
                stmt.execute(sql);
            }
            
            // Insert pharmacies
            for (String sql : pharmacies) {
                stmt.execute(sql);
            }
            
            // Insert stock data (some medicines available at some pharmacies)
            String[] stockData = {
                "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (1, 1, 150, 2.50)",
                "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (1, 2, 80, 3.75)",
                "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (1, 3, 45, 8.90)",
                "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (2, 1, 100, 2.60)",
                "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (2, 4, 70, 12.75)",
                "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (3, 1, 200, 2.40)",
                "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (3, 2, 120, 3.60)",
                "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (3, 5, 140, 5.10)",
                "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (4, 1, 180, 2.30)",
                "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (4, 3, 55, 8.50)",
                "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (5, 1, 160, 2.45)",
                "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (5, 4, 95, 12.40)"
            };
            
            for (String sql : stockData) {
                stmt.execute(sql);
            }
        }
        
        System.out.println("✅ Sample data inserted successfully");
    }
    
    public List<Map<String, Object>> findNearbyPharmacies(double lat, double lng, String medicineName) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        String sql = """
            SELECT DISTINCT 
                p.id, p.name, p.address, p.phone, p.latitude, p.longitude, 
                p.is_24_hours, p.rating,
                (CASE WHEN s.quantity > 0 THEN 1 ELSE 0 END) as has_stock,
                s.quantity, s.price,
                ((p.latitude - ?) * (p.latitude - ?) + (p.longitude - ?) * (p.longitude - ?)) as distance_sq
            FROM pharmacy p
            LEFT JOIN pharmacy_medicine_stock s ON p.id = s.pharmacy_id
            LEFT JOIN medicine m ON s.medicine_id = m.id AND LOWER(m.name) LIKE LOWER(?)
            ORDER BY distance_sq
            LIMIT 10
            """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, lat);
            pstmt.setDouble(2, lat);
            pstmt.setDouble(3, lng);
            pstmt.setDouble(4, lng);
            pstmt.setString(5, "%" + medicineName + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> pharmacy = new HashMap<>();
                    pharmacy.put("name", rs.getString("name"));
                    pharmacy.put("address", rs.getString("address"));
                    pharmacy.put("phone", rs.getString("phone"));
                    pharmacy.put("latitude", rs.getDouble("latitude"));
                    pharmacy.put("longitude", rs.getDouble("longitude"));
                    pharmacy.put("hasStock", rs.getInt("has_stock") == 1);
                    pharmacy.put("searchedMedicine", medicineName);
                    pharmacy.put("distance", Math.sqrt(rs.getDouble("distance_sq")) * 111); // Rough km conversion
                    results.add(pharmacy);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error querying pharmacies: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void shutdown() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }
}
