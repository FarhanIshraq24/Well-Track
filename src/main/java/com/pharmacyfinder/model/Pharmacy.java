package com.pharmacyfinder.model;

public class Pharmacy {
    private String name;
    private String address;
    private String phone;
    private double latitude;
    private double longitude;
    private boolean hasStock;
    private String searchedMedicine;
    private double distance;

    public Pharmacy(String name, String address, String phone,
                    double latitude, double longitude, boolean hasStock, String searchedMedicine) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hasStock = hasStock;
        this.searchedMedicine = searchedMedicine;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public boolean hasStock() { return hasStock; }
    public void setHasStock(boolean hasStock) { this.hasStock = hasStock; }

    public String getSearchedMedicine() { return searchedMedicine; }
    public void setSearchedMedicine(String searchedMedicine) { this.searchedMedicine = searchedMedicine; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
}