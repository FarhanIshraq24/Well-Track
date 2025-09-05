package com.pharmacyfinder.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "pharmacy")
public class Pharmacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, length = 500)
    private String address;
    
    private String phone;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column(name = "is_24_hours")
    private Boolean is24Hours = false;
    
    private BigDecimal rating = BigDecimal.ZERO;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PharmacyMedicineStock> medicineStock;
    
    // Transient fields for compatibility with existing code
    @Transient
    private Location location;
    
    @Transient
    private Map<String, Boolean> stock;
    
    @Transient
    private double distance;

    // Default constructor for JPA
    public Pharmacy() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor for database entities
    public Pharmacy(String name, String address, String phone, Double latitude, Double longitude) {
        this();
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // Legacy constructor for compatibility
    public Pharmacy(String name, String address, String phone, Location location, Map<String, Boolean> stock, double distance) {
        this();
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.location = location;
        this.stock = stock;
        this.distance = distance;
        if (location != null) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        }
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public Boolean getIs24Hours() { return is24Hours; }
    public void setIs24Hours(Boolean is24Hours) { this.is24Hours = is24Hours; }
    
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<PharmacyMedicineStock> getMedicineStock() { return medicineStock; }
    public void setMedicineStock(List<PharmacyMedicineStock> medicineStock) { this.medicineStock = medicineStock; }

    // Legacy compatibility methods
    public Location getLocation() { 
        if (location == null && latitude != null && longitude != null) {
            location = new Location(latitude, longitude, address);
        }
        return location; 
    }
    public void setLocation(Location location) { 
        this.location = location;
        if (location != null) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        }
    }

    public Map<String, Boolean> getStock() { return stock; }
    public void setStock(Map<String, Boolean> stock) { this.stock = stock; }

    public boolean hasStock(String medicineName) {
        return stock != null && stock.getOrDefault(medicineName, false);
    }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%.1f miles)", name, address, distance);
    }
}
