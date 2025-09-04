package com.pharmacyfinder.service;

import com.pharmacyfinder.model.Medicine;
import com.pharmacyfinder.model.Pharmacy;
import com.pharmacyfinder.model.Location;
import java.util.*;
import java.util.stream.Collectors;

public class MedicineDataService {
    private static final List<Medicine> MEDICINES = Arrays.asList(
        // Pain Relief & Fever
        new Medicine("Acetaminophen", "Tylenol", "Pain reliever and fever reducer", "500mg", "Tablet", "Over-the-counter", 8.99),
        new Medicine("Ibuprofen", "Advil", "Anti-inflammatory pain reliever", "200mg", "Tablet", "Over-the-counter", 9.99),
        new Medicine("Aspirin", "Bayer", "Pain reliever and blood thinner", "325mg", "Tablet", "Over-the-counter", 6.49),
        new Medicine("Naproxen", "Aleve", "Long-lasting pain relief", "220mg", "Tablet", "Over-the-counter", 12.99),
        
        // Allergy & Cold
        new Medicine("Loratadine", "Claritin", "24-hour allergy relief", "10mg", "Tablet", "Over-the-counter", 14.99),
        new Medicine("Cetirizine", "Zyrtec", "Antihistamine for allergies", "10mg", "Tablet", "Over-the-counter", 13.99),
        new Medicine("Pseudoephedrine", "Sudafed", "Decongestant", "30mg", "Tablet", "Behind-counter", 11.99),
        new Medicine("Dextromethorphan", "Robitussin DM", "Cough suppressant", "15mg/5ml", "Syrup", "Over-the-counter", 9.49),
        
        // Digestive Health
        new Medicine("Omeprazole", "Prilosec", "Acid reducer", "20mg", "Capsule", "Over-the-counter", 18.99),
        new Medicine("Simethicone", "Gas-X", "Gas relief", "125mg", "Chewable", "Over-the-counter", 7.99),
        new Medicine("Loperamide", "Imodium", "Anti-diarrheal", "2mg", "Tablet", "Over-the-counter", 8.99),
        new Medicine("Docusate", "Colace", "Stool softener", "100mg", "Capsule", "Over-the-counter", 9.99),
        
        // Prescription Common Medicines
        new Medicine("Lisinopril", "Zestril", "ACE inhibitor for blood pressure", "10mg", "Tablet", "Prescription", 15.99),
        new Medicine("Metformin", "Glucophage", "Type 2 diabetes medication", "500mg", "Tablet", "Prescription", 12.99),
        new Medicine("Atorvastatin", "Lipitor", "Cholesterol medication", "20mg", "Tablet", "Prescription", 22.99),
        new Medicine("Amlodipine", "Norvasc", "Calcium channel blocker", "5mg", "Tablet", "Prescription", 18.99),
        new Medicine("Sertraline", "Zoloft", "Antidepressant", "50mg", "Tablet", "Prescription", 28.99),
        new Medicine("Levothyroxine", "Synthroid", "Thyroid hormone replacement", "100mcg", "Tablet", "Prescription", 16.99),
        
        // Vitamins & Supplements
        new Medicine("Vitamin D3", "Nature Made", "Bone and immune health", "2000 IU", "Softgel", "Over-the-counter", 12.99),
        new Medicine("Multivitamin", "Centrum", "Daily nutritional supplement", "1 tablet", "Tablet", "Over-the-counter", 16.99),
        new Medicine("Omega-3", "Fish Oil", "Heart and brain health", "1000mg", "Softgel", "Over-the-counter", 19.99),
        new Medicine("Calcium", "Caltrate", "Bone health supplement", "600mg", "Tablet", "Over-the-counter", 13.99)
    );
    
    private static final List<Pharmacy> PHARMACIES = Arrays.asList(
        new Pharmacy("CVS Pharmacy", "1234 Main St, New York, NY 10001", "+1-555-123-4567", 
                    new Location(40.7831, -73.9712, "1234 Main St, New York, NY 10001"), generateRandomStock(), 0.3),
        new Pharmacy("Walgreens", "5678 Broadway, New York, NY 10019", "+1-555-234-5678",
                    new Location(40.7614, -73.9776, "5678 Broadway, New York, NY 10019"), generateRandomStock(), 0.8),
        new Pharmacy("Rite Aid", "9012 Park Ave, New York, NY 10028", "+1-555-345-6789",
                    new Location(40.7736, -73.9566, "9012 Park Ave, New York, NY 10028"), generateRandomStock(), 1.2),
        new Pharmacy("Duane Reade", "3456 Lexington Ave, New York, NY 10016", "+1-555-456-7890",
                    new Location(40.7505, -73.9934, "3456 Lexington Ave, New York, NY 10016"), generateRandomStock(), 0.9),
        new Pharmacy("Independent Pharmacy", "7890 Queens Blvd, Queens, NY 11373", "+1-555-567-8901",
                    new Location(40.7282, -73.8648, "7890 Queens Blvd, Queens, NY 11373"), generateRandomStock(), 2.1),
        new Pharmacy("HealthMart Pharmacy", "2345 Flatbush Ave, Brooklyn, NY 11234", "+1-555-678-9012",
                    new Location(40.6182, -73.9442, "2345 Flatbush Ave, Brooklyn, NY 11234"), generateRandomStock(), 3.4),
        new Pharmacy("MediMax Pharmacy", "6789 Northern Blvd, Queens, NY 11377", "+1-555-789-0123",
                    new Location(40.7505, -73.8990, "6789 Northern Blvd, Queens, NY 11377"), generateRandomStock(), 2.8),
        new Pharmacy("CareFirst Pharmacy", "1357 Amsterdam Ave, New York, NY 10027", "+1-555-890-1234",
                    new Location(40.8176, -73.9482, "1357 Amsterdam Ave, New York, NY 10027"), generateRandomStock(), 1.7)
    );
    
    private static Map<String, Boolean> generateRandomStock() {
        Map<String, Boolean> stock = new HashMap<>();
        Random random = new Random();
        for (Medicine medicine : MEDICINES) {
            // 75% chance of having the medicine in stock
            stock.put(medicine.getName(), random.nextDouble() < 0.75);
        }
        return stock;
    }
    
    public List<Medicine> getAllMedicines() {
        return new ArrayList<>(MEDICINES);
    }
    
    public List<Medicine> searchMedicines(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllMedicines();
        }
        
        String searchTerm = query.toLowerCase().trim();
        return MEDICINES.stream()
            .filter(medicine -> 
                medicine.getName().toLowerCase().contains(searchTerm) ||
                medicine.getBrand().toLowerCase().contains(searchTerm) ||
                medicine.getDescription().toLowerCase().contains(searchTerm) ||
                medicine.getType().toLowerCase().contains(searchTerm)
            )
            .collect(Collectors.toList());
    }
    
    public List<Pharmacy> getAllPharmacies() {
        return new ArrayList<>(PHARMACIES);
    }
    
    public List<Pharmacy> findPharmaciesWithMedicine(String medicineName) {
        return PHARMACIES.stream()
            .filter(pharmacy -> pharmacy.hasStock(medicineName))
            .sorted(Comparator.comparingDouble(Pharmacy::getDistance))
            .collect(Collectors.toList());
    }
    
    public List<Pharmacy> findNearbyPharmacies(double userLat, double userLng, double radiusMiles) {
        return PHARMACIES.stream()
            .peek(pharmacy -> {
                double distance = calculateDistance(userLat, userLng, 
                    pharmacy.getLocation().getLatitude(), 
                    pharmacy.getLocation().getLongitude());
                pharmacy.setDistance(distance);
            })
            .filter(pharmacy -> pharmacy.getDistance() <= radiusMiles)
            .sorted(Comparator.comparingDouble(Pharmacy::getDistance))
            .collect(Collectors.toList());
    }
    
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 3959; // Earth's radius in miles
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c; // Distance in miles
    }
}
