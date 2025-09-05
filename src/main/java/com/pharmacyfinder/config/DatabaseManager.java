package com.pharmacyfinder.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    
    private static DatabaseManager instance;
    private DataSource dataSource;
    private EntityManagerFactory entityManagerFactory;
    
    private DatabaseManager() {
        initializeDatabase();
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    private void initializeDatabase() {
        // Create data directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            System.err.println("Warning: Could not create data directory: " + e.getMessage());
        }
        
        // Configure HikariCP connection pool for SQLite
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:./data/pharmacy_db.sqlite");
        config.setDriverClassName("org.sqlite.JDBC");
        config.setUsername("");
        config.setPassword("");
        
        // Connection pool settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        
        this.dataSource = new HikariDataSource(config);
        
        // Initialize database schema and data
        initializeSchemaAndData();
        
        // Configure JPA EntityManagerFactory for SQLite
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.driver", "org.sqlite.JDBC");
        properties.put("jakarta.persistence.jdbc.url", "jdbc:sqlite:./data/pharmacy_db.sqlite");
        properties.put("jakarta.persistence.jdbc.user", "");
        properties.put("jakarta.persistence.jdbc.password", "");
        
        // Hibernate properties for SQLite
        properties.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.use_sql_comments", "true");
        
        this.entityManagerFactory = Persistence.createEntityManagerFactory("pharmacy-persistence-unit", properties);
    }
    
    private void initializeSchemaAndData() {
        try (Connection connection = dataSource.getConnection()) {
            // Execute schema.sql
            executeScript(connection, "src/main/resources/schema.sql");
            
            // Check if data already exists
            if (!hasExistingData(connection)) {
                // Execute data.sql only if no existing data
                executeScript(connection, "src/main/resources/data.sql");
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void executeScript(Connection connection, String scriptPath) {
        try {
            String script = Files.readString(Paths.get(scriptPath));
            String[] statements = script.split(";");
            
            try (Statement statement = connection.createStatement()) {
                for (String sql : statements) {
                    sql = sql.trim();
                    if (!sql.isEmpty() && !sql.startsWith("--")) {
                        statement.execute(sql);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            System.err.println("Error executing script " + scriptPath + ": " + e.getMessage());
        }
    }
    
    private boolean hasExistingData(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            var resultSet = statement.executeQuery("SELECT COUNT(*) FROM pharmacy");
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            // Table might not exist yet, which is fine
            return false;
        }
        return false;
    }
    
    public DataSource getDataSource() {
        return dataSource;
    }
    
    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    public void shutdown() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
    }
}
