package com.pharmacyfinder.service;

import com.pharmacyfinder.config.SimpleDatabaseManager;
import com.pharmacyfinder.model.Pharmacy;
import com.pharmacyfinder.model.Location;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PharmacyService {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final SimpleDatabaseManager databaseManager;

    public PharmacyService() {
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
        this.databaseManager = SimpleDatabaseManager.getInstance();
    }

    public CompletableFuture<List<Pharmacy>> findNearbyPharmacies(double lat, double lng, String medicine) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("🔍 Searching for pharmacies with medicine: " + medicine + " near (" + lat + ", " + lng + ")");
            
            // Query SQLite database
            List<Map<String, Object>> dbResults = databaseManager.findNearbyPharmacies(lat, lng, medicine);
            
            if (!dbResults.isEmpty()) {
                System.out.println("✅ Found " + dbResults.size() + " pharmacies from database");
                return convertDatabaseResults(dbResults, medicine);
            }
            
            // Fallback to external API if no database results
            System.out.println("⚠️ No database results, trying external API...");
            try {
                String query = buildOverpassQuery(lat, lng);
                String url = "https://overpass-api.de/api/interpreter?data=" +
                        java.net.URLEncoder.encode(query, "UTF-8");

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        String jsonResponse = response.body().string();
                        return parsePharmaciesFromJson(jsonResponse, medicine);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error with external API: " + e.getMessage());
            }
            
            // Final fallback to mock data
            System.out.println("⚠️ Using mock data as fallback");
            return getMockPharmacies(lat, lng, medicine);
        });
    }

    private String buildOverpassQuery(double lat, double lng) {
        double radius = 0.01; // ~1km radius
        return String.format("""
            [out:json][timeout:25];
            (
              node["amenity"="pharmacy"](around:1000,%f,%f);
              way["amenity"="pharmacy"](around:1000,%f,%f);
              relation["amenity"="pharmacy"](around:1000,%f,%f);
            );
            out body;
            """, lat, lng, lat, lng, lat, lng);
    }

    private List<Pharmacy> parsePharmaciesFromJson(String json, String medicine) {
        List<Pharmacy> pharmacies = new ArrayList<>();
        try {
            var node = objectMapper.readTree(json);
            var elements = node.get("elements");

            for (var element : elements) {
                if (element.has("tags") && element.get("tags").has("name")) {
                    var tags = element.get("tags");
                    String name = tags.get("name").asText();
                    double lat = element.get("lat").asDouble();
                    double lon = element.get("lon").asDouble();

                    String address = tags.has("addr:full") ?
                            tags.get("addr:full").asText() : "Address not available";
                    String phone = tags.has("phone") ?
                            tags.get("phone").asText() : "Phone not available";

                    // Mock availability check and create stock map
                    Map<String, Boolean> stock = new HashMap<>();
                    stock.put(medicine, Math.random() > 0.3); // 70% chance of having stock
                    
                    Location location = new Location(lat, lon, address);
                    double distance = calculateDistance(0, 0, lat, lon); // Mock distance
                    
                    pharmacies.add(new Pharmacy(name, address, phone, location, stock, distance));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pharmacies.isEmpty() ? getMockPharmacies(0, 0, medicine) : pharmacies;
    }

    private List<Pharmacy> getMockPharmacies(double lat, double lng, String medicine) {
        List<Pharmacy> mockPharmacies = new ArrayList<>();

        Map<String, Boolean> stock1 = new HashMap<>();
        stock1.put(medicine, true);
        Location loc1 = new Location(23.8103, 90.4125, "123 Main Street, Dhaka");
        mockPharmacies.add(new Pharmacy(
                "City Pharmacy",
                "123 Main Street, Dhaka",
                "+880-123-456789",
                loc1, stock1, 0.5
        ));

        Map<String, Boolean> stock2 = new HashMap<>();
        stock2.put(medicine, false);
        Location loc2 = new Location(23.8203, 90.4225, "456 Park Avenue, Dhaka");
        mockPharmacies.add(new Pharmacy(
                "HealthCare Plus",
                "456 Park Avenue, Dhaka",
                "+880-987-654321",
                loc2, stock2, 1.2
        ));

        return mockPharmacies;
    }
    
    /**
     * Convert database results to Pharmacy objects for frontend
     */
    private List<Pharmacy> convertDatabaseResults(List<Map<String, Object>> dbResults, String searchedMedicine) {
        return dbResults.stream().map(result -> {
            // Create Location object
            Location location = new Location(
                (Double) result.get("latitude"),
                (Double) result.get("longitude"),
                (String) result.get("address")
            );
            
            // Create stock map
            Map<String, Boolean> stock = new HashMap<>();
            stock.put(searchedMedicine, (Boolean) result.get("hasStock"));
            
            // Create new Pharmacy object with legacy constructor
            return new Pharmacy(
                (String) result.get("name"),
                (String) result.get("address"),
                (String) result.get("phone"),
                location,
                stock,
                (Double) result.get("distance")
            );
        }).toList();
    }
    
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Simple distance calculation (Haversine formula simplified)
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        return Math.sqrt(dlat * dlat + dlon * dlon) * 111; // Rough conversion to km
    }
}
