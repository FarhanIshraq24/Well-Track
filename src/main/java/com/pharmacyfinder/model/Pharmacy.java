package com.pharmacyfinder.model;

import java.util.Map;

public class Pharmacy {
    private String name;
    private String address;
    private String phone;
    private Location location;
    private Map<String, Boolean> stock;
    private double distance;

    public Pharmacy(String name, String address, String phone, Location location, Map<String, Boolean> stock, double distance) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.location = location;
        this.stock = stock;
        this.distance = distance;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

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
