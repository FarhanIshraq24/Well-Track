package com.pharmacyfinder.service;

import com.pharmacyfinder.model.Location;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class LocationService {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public LocationService() {
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public CompletableFuture<Location> getCurrentLocation() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Using IP-based geolocation (free API)
                Request request = new Request.Builder()
                        .url("https://ipapi.co/json/")
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        String jsonResponse = response.body().string();
                        return parseLocationFromJson(jsonResponse);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Fallback location (Dhaka, Bangladesh)
            return new Location(23.8103, 90.4125, "Dhaka, Bangladesh");
        });
    }

    private Location parseLocationFromJson(String json) {
        try {
            var node = objectMapper.readTree(json);
            double lat = node.get("latitude").asDouble();
            double lng = node.get("longitude").asDouble();
            String city = node.get("city").asText();
            String country = node.get("country_name").asText();

            return new Location(lat, lng, city + ", " + country);
        } catch (Exception e) {
            e.printStackTrace();
            return new Location(23.8103, 90.4125, "Dhaka, Bangladesh");
        }
    }
}