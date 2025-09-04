package com.pharmacyfinder.service;

import com.pharmacyfinder.model.Pharmacy;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PharmacyService {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public PharmacyService() {
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public CompletableFuture<List<Pharmacy>> findNearbyPharmacies(double lat, double lng, String medicine) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Using Overpass API (OpenStreetMap) for pharmacy data
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
                e.printStackTrace();
            }

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

                    // Mock availability check
                    boolean hasStock = Math.random() > 0.3; // 70% chance of having stock

                    pharmacies.add(new Pharmacy(name, address, phone, lat, lon, hasStock, medicine));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pharmacies.isEmpty() ? getMockPharmacies(0, 0, medicine) : pharmacies;
    }

    private List<Pharmacy> getMockPharmacies(double lat, double lng, String medicine) {
        List<Pharmacy> mockPharmacies = new ArrayList<>();

        mockPharmacies.add(new Pharmacy(
                "City Pharmacy",
                "123 Main Street, Dhaka",
                "+880-123-456789",
                23.8103, 90.4125, true, medicine
        ));

        mockPharmacies.add(new Pharmacy(
                "HealthCare Plus",
                "456 Park Avenue, Dhaka",
                "+880-987-654321",
                23.8203, 90.4225, false, medicine
        ));

        return mockPharmacies;
    }
}