package com.pharmacyfinder.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "medicine")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "generic_name")
    private String genericName;
    
    private String category;
    
    @Column(name = "unit_price")
    private BigDecimal unitPrice = BigDecimal.ZERO;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PharmacyMedicineStock> pharmacyStock;
    
    // Constructors
    public Medicine() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Medicine(String name, String genericName, String category, BigDecimal unitPrice) {
        this();
        this.name = name;
        this.genericName = genericName;
        this.category = category;
        this.unitPrice = unitPrice;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getGenericName() { return genericName; }
    public void setGenericName(String genericName) { this.genericName = genericName; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public List<PharmacyMedicineStock> getPharmacyStock() { return pharmacyStock; }
    public void setPharmacyStock(List<PharmacyMedicineStock> pharmacyStock) { this.pharmacyStock = pharmacyStock; }
    
    // Legacy compatibility methods for existing code
    @Transient
    private String brand;
    
    @Transient
    private String description;
    
    @Transient
    private String dosage;
    
    @Transient
    private String type;
    
    @Transient
    private String availability;
    
    // Legacy constructor for backward compatibility
    public Medicine(String name, String brand, String description, String dosage, String type, String availability, double price) {
        this();
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.dosage = dosage;
        this.type = type;
        this.availability = availability;
        this.unitPrice = BigDecimal.valueOf(price);
        this.category = type; // Map type to category
        this.genericName = name; // Default generic name to name
    }
    
    // Legacy getter methods
    public String getBrand() { 
        return brand != null ? brand : genericName;
    }
    
    public void setBrand(String brand) { 
        this.brand = brand;
    }
    
    public String getDescription() { 
        return description != null ? description : (category + " medicine");
    }
    
    public void setDescription(String description) { 
        this.description = description;
    }
    
    public String getDosage() { 
        return dosage != null ? dosage : "Standard dosage";
    }
    
    public void setDosage(String dosage) { 
        this.dosage = dosage;
    }
    
    public String getType() { 
        return type != null ? type : category;
    }
    
    public void setType(String type) { 
        this.type = type;
        this.category = type;
    }
    
    public String getAvailability() { 
        return availability != null ? availability : "Available";
    }
    
    public void setAvailability(String availability) { 
        this.availability = availability;
    }
    
    public double getPrice() { 
        return unitPrice != null ? unitPrice.doubleValue() : 0.0;
    }
    
    public void setPrice(double price) { 
        this.unitPrice = BigDecimal.valueOf(price);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %s - $%.2f", name, getBrand(), getDosage(), getPrice());
    }
}
