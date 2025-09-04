package com.pharmacyfinder.model;

public class Medicine {
    private String name;
    private String brand;
    private String description;
    private String dosage;
    private String type;
    private String availability;
    private double price;
    
    public Medicine(String name, String brand, String description, String dosage, String type, String availability, double price) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.dosage = dosage;
        this.type = type;
        this.availability = availability;
        this.price = price;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %s - $%.2f", name, brand, dosage, price);
    }
}
