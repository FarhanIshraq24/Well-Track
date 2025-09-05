import java.sql.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("🧪 Testing SQLite Database Creation");
        
        try {
            // Create data directory
            Files.createDirectories(Paths.get("data"));
            
            // Load SQLite driver
            Class.forName("org.sqlite.JDBC");
            
            // Connect to database
            Connection conn = DriverManager.getConnection("jdbc:sqlite:./data/pharmacy_db.sqlite");
            
            // Create tables
            Statement stmt = conn.createStatement();
            
            // Pharmacy table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pharmacy (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    address TEXT NOT NULL,
                    phone TEXT,
                    latitude REAL NOT NULL,
                    longitude REAL NOT NULL,
                    is_24_hours INTEGER DEFAULT 0,
                    rating REAL DEFAULT 0.0
                )
            """);
            
            // Medicine table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS medicine (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    generic_name TEXT,
                    category TEXT,
                    unit_price REAL DEFAULT 0.00
                )
            """);
            
            // Stock table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pharmacy_medicine_stock (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    pharmacy_id INTEGER NOT NULL,
                    medicine_id INTEGER NOT NULL,
                    quantity INTEGER DEFAULT 0,
                    price REAL DEFAULT 0.00,
                    FOREIGN KEY (pharmacy_id) REFERENCES pharmacy(id),
                    FOREIGN KEY (medicine_id) REFERENCES medicine(id)
                )
            """);
            
            System.out.println("✅ Tables created successfully");
            
            // Insert sample data
            insertSampleData(conn);
            
            // Test query
            testQuery(conn);
            
            conn.close();
            System.out.println("✅ Database test completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void insertSampleData(Connection conn) throws SQLException {
        // Check if data already exists
        PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM pharmacy");
        ResultSet rs = check.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            System.out.println("🔄 Data already exists, skipping insert");
            return;
        }
        
        // Insert medicines
        String[] medicines = {
            "INSERT INTO medicine (name, generic_name, category, unit_price) VALUES ('Paracetamol 500mg', 'Acetaminophen', 'Analgesic', 2.50)",
            "INSERT INTO medicine (name, generic_name, category, unit_price) VALUES ('Ibuprofen 400mg', 'Ibuprofen', 'NSAID', 3.75)",
            "INSERT INTO medicine (name, generic_name, category, unit_price) VALUES ('Amoxicillin 250mg', 'Amoxicillin', 'Antibiotic', 8.90)"
        };
        
        // Insert pharmacies  
        String[] pharmacies = {
            "INSERT INTO pharmacy (name, address, phone, latitude, longitude, is_24_hours, rating) VALUES ('City Pharmacy', '123 Main Street, Dhanmondi, Dhaka-1205', '+880-2-9612345', 23.7461, 90.3742, 1, 4.5)",
            "INSERT INTO pharmacy (name, address, phone, latitude, longitude, is_24_hours, rating) VALUES ('Green Life Pharmacy', '789 New Market Road, New Market, Dhaka-1205', '+880-2-9513456', 23.7275, 90.3854, 1, 4.7)",
            "INSERT INTO pharmacy (name, address, phone, latitude, longitude, is_24_hours, rating) VALUES ('Square Pharmacy', '654 Elephant Road, Kathalbagan, Dhaka-1205', '+880-2-9654321', 23.7500, 90.3889, 1, 4.8)"
        };
        
        // Insert stock
        String[] stock = {
            "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (1, 1, 150, 2.50)",
            "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (1, 2, 80, 3.75)",
            "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (2, 1, 200, 2.40)",
            "INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES (3, 1, 180, 2.30)"
        };
        
        Statement stmt = conn.createStatement();
        
        for (String sql : medicines) {
            stmt.execute(sql);
        }
        
        for (String sql : pharmacies) {
            stmt.execute(sql);
        }
        
        for (String sql : stock) {
            stmt.execute(sql);
        }
        
        System.out.println("✅ Sample data inserted");
    }
    
    private static void testQuery(Connection conn) throws SQLException {
        System.out.println("\n🔍 Testing pharmacy search query...");
        
        String sql = """
            SELECT DISTINCT p.name, p.address, p.phone, p.latitude, p.longitude,
                   (CASE WHEN s.quantity > 0 THEN 1 ELSE 0 END) as has_stock
            FROM pharmacy p
            LEFT JOIN pharmacy_medicine_stock s ON p.id = s.pharmacy_id
            LEFT JOIN medicine m ON s.medicine_id = m.id AND LOWER(m.name) LIKE LOWER(?)
            ORDER BY p.name
        """;
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, "%paracetamol%");
        
        ResultSet rs = pstmt.executeQuery();
        
        System.out.println("📊 Results for Paracetamol:");
        while (rs.next()) {
            System.out.println("  🏥 " + rs.getString("name"));
            System.out.println("     📍 " + rs.getString("address"));  
            System.out.println("     📞 " + rs.getString("phone"));
            System.out.println("     💊 Has Stock: " + (rs.getInt("has_stock") == 1));
            System.out.println("     📍 Location: (" + rs.getDouble("latitude") + ", " + rs.getDouble("longitude") + ")");
            System.out.println();
        }
    }
}
