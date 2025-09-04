package com.pharmacyfinder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.fxml.Initializable;
import javafx.concurrent.Worker;
import javafx.application.Platform;
import com.pharmacyfinder.service.MedicineDataService;
import com.pharmacyfinder.model.Medicine;
import com.pharmacyfinder.model.Pharmacy;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.io.File;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;
    
    @FXML
    private WebView webView;
    
    @FXML
    private TextField searchField;
    
    private WebEngine webEngine;
    private MedicineDataService dataService;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataService = new MedicineDataService();
        
        if (webView != null) {
            webEngine = webView.getEngine();
            
            // Enable JavaScript
            webEngine.setJavaScriptEnabled(true);
            
            // Set up JavaScript bridge with real data
            setupJavaScriptBridge();
            
            // Load the functional WellTrack application
            loadWebApplication();
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        if (welcomeText != null) {
            welcomeText.setText("Welcome to JavaFX Application!");
        }
    }
    
    @FXML
    protected void onOpenWebInterface() {
        loadWebApplication();
    }
    
    @FXML
    protected void onSearch() {
        String query = searchField != null ? searchField.getText() : "";
        List<Medicine> medicines = dataService.searchMedicines(query);
        List<Pharmacy> pharmacies = dataService.getAllPharmacies();
        
        // Update web content with search results
        String searchHtml = createSearchResultsHtml(query, medicines, pharmacies);
        webEngine.loadContent(searchHtml);
    }
    
    private void loadWebApplication() {
        try {
            // Try to load the built React app first (with map)
            File indexFile = new File("src/main/resources/web/index.html");
            if (indexFile.exists()) {
                String url = indexFile.toURI().toString();
                webEngine.load(url);
            } else {
                // Fallback: show WellTrack dashboard with real medicine data
                List<Medicine> allMedicines = dataService.getAllMedicines();
                List<Pharmacy> allPharmacies = dataService.getAllPharmacies();
                String dashboardHtml = createFunctionalDashboardHtml(allMedicines, allPharmacies);
                webEngine.loadContent(dashboardHtml);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorHtml = createErrorHtml(e.getMessage());
            webEngine.loadContent(errorHtml);
        }
    }
    
    private void setupJavaScriptBridge() {
        webEngine.getLoadWorker().stateProperty().addListener(
            (obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    // Inject Java API with real data into JavaScript
                    Platform.runLater(() -> {
                        List<Medicine> allMedicines = dataService.getAllMedicines();
                        List<Pharmacy> allPharmacies = dataService.getAllPharmacies();
                        
                        StringBuilder medicineData = new StringBuilder();
                        for (Medicine med : allMedicines) {
                            medicineData.append(String.format(
                                "{ name: '%s', brand: '%s', price: %.2f, type: '%s', availability: '%s' },",
                                med.getName().replace("'", "\\'"), 
                                med.getBrand().replace("'", "\\'"), 
                                med.getPrice(), 
                                med.getType().replace("'", "\\'"),
                                med.getAvailability().replace("'", "\\'"))
                            );
                        }
                        
                        StringBuilder pharmacyData = new StringBuilder();
                        for (Pharmacy pharmacy : allPharmacies) {
                            pharmacyData.append(String.format(
                                "{ name: '%s', address: '%s', phone: '%s', distance: %.1f },",
                                pharmacy.getName().replace("'", "\\'"),
                                pharmacy.getAddress().replace("'", "\\'"),
                                pharmacy.getPhone(),
                                pharmacy.getDistance())
                            );
                        }
                        
                        webEngine.executeScript(
                            "window.wellTrackAPI = {" +
                                "medicines: [" + medicineData.toString() + "]," +
                                "pharmacies: [" + pharmacyData.toString() + "]," +
                                "searchMedicines: function(query) {" +
                                "    return this.medicines.filter(m => " +
                                "        m.name.toLowerCase().includes(query.toLowerCase()) ||" +
                                "        m.brand.toLowerCase().includes(query.toLowerCase()) ||" +
                                "        m.type.toLowerCase().includes(query.toLowerCase())" +
                                "    );" +
                                "}," +
                                "findPharmaciesWithMedicine: function(medicineName) {" +
                                "    return this.pharmacies.filter(p => Math.random() > 0.3);" + // Random stock simulation
                                "}" +
                            "};"
                        );
                    });
                }
            }
        );
    }
    
    private String createFunctionalDashboardHtml(List<Medicine> medicines, List<Pharmacy> pharmacies) {
        StringBuilder medicineCards = new StringBuilder();
        for (Medicine med : medicines.subList(0, Math.min(8, medicines.size()))) {
            String availability = med.getAvailability().equals("Prescription") ? "🔒 Rx" : "✅ OTC";
            medicineCards.append(String.format(
                "<div class='medicine-card' onclick='searchMedicine(\"%s\")'>" +
                "<div class='med-name'>%s</div>" +
                "<div class='med-brand'>%s</div>" +
                "<div class='med-price'>$%.2f</div>" +
                "<div class='med-type'>%s</div>" +
                "</div>",
                med.getName(), med.getName(), med.getBrand(), med.getPrice(), availability
            ));
        }
        
        return "<!DOCTYPE html>" +
               "<html><head><title>WellTrack - Dashboard</title>" +
               "<meta charset='UTF-8'>" +
               "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.7.1/dist/leaflet.css' />" +
               "<script src='https://unpkg.com/leaflet@1.7.1/dist/leaflet.js'></script>" +
               "<style>" +
               "@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap');" +
               "* { margin: 0; padding: 0; box-sizing: border-box; }" +
               "body { font-family: 'Inter', system-ui; background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 50%, #bbf7d0 100%); overflow-x: hidden; }" +
               
               "/* COOL LOADING ANIMATION */" +
               ".loading-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: linear-gradient(45deg, #1a5c3a, #2d7a4f); z-index: 9999; display: flex; align-items: center; justify-content: center; animation: fadeOut 2s ease-in-out forwards; }" +
               ".logo-container { text-align: center; animation: logoEntrance 1.5s ease-out; }" +
               ".logo-text { font-size: 4rem; font-weight: 800; color: white; margin-bottom: 1rem; letter-spacing: -2px; }" +
               ".logo-tagline { color: rgba(255,255,255,0.9); font-size: 1.2rem; font-weight: 500; }" +
               ".spinner { width: 60px; height: 60px; border: 4px solid rgba(255,255,255,0.3); border-top: 4px solid white; border-radius: 50%; animation: spin 1s linear infinite; margin: 20px auto; }" +
               
               "/* MAIN DASHBOARD ANIMATIONS */" +
               ".dashboard { display: grid; grid-template-columns: 1fr 420px; height: 100vh; gap: 24px; padding: 24px; animation: slideIn 1s ease-out 2s both; opacity: 0; }" +
               ".map-section { background: white; border-radius: 20px; box-shadow: 0 20px 50px rgba(45,122,79,0.15); overflow: hidden; position: relative; animation: mapSlideIn 1.2s ease-out 2.2s both; transform: translateX(-50px); opacity: 0; }" +
               "#map { height: 100%; width: 100%; }" +
               
               "/* SIDEBAR ANIMATIONS */" +
               ".sidebar { display: flex; flex-direction: column; gap: 20px; animation: sidebarSlideIn 1s ease-out 2.4s both; transform: translateX(50px); opacity: 0; }" +
               ".section { background: white; border-radius: 18px; padding: 24px; box-shadow: 0 8px 32px rgba(45,122,79,0.12); border: 1px solid rgba(45,122,79,0.1); transition: all 0.3s ease; position: relative; overflow: hidden; }" +
               ".section::before { content: ''; position: absolute; top: 0; left: 0; right: 0; height: 3px; background: linear-gradient(90deg, #1a5c3a, #2d7a4f, #059669); }" +
               ".section:hover { transform: translateY(-5px); box-shadow: 0 20px 50px rgba(45,122,79,0.2); }" +
               ".section h2 { color: #1a5c3a; margin-bottom: 20px; font-size: 1.5rem; font-weight: 800; display: flex; align-items: center; gap: 12px; }" +
               
               "/* SEARCH BOX WITH COOL EFFECTS */" +
               ".search-container { position: relative; }" +
               ".search-box { width: 100%; padding: 16px 20px 16px 50px; border: 2px solid #e5e7eb; border-radius: 12px; font-size: 14px; font-weight: 500; transition: all 0.3s ease; background: #fafafa; }" +
               ".search-box:focus { border-color: #2d7a4f; outline: none; background: white; box-shadow: 0 0 0 3px rgba(45,122,79,0.1); }" +
               ".search-icon { position: absolute; left: 18px; top: 50%; transform: translateY(-50%); color: #6b7280; font-size: 18px; }" +
               
               "/* MEDICINE CARDS WITH HOVER EFFECTS */" +
               ".medicine-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }" +
               ".medicine-card { background: linear-gradient(135deg, #f8f9f0, #f0fdf4); border: 1px solid rgba(45,122,79,0.2); border-radius: 12px; padding: 16px; cursor: pointer; transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); position: relative; overflow: hidden; }" +
               ".medicine-card::before { content: ''; position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: linear-gradient(135deg, #2d7a4f, #1a5c3a); opacity: 0; transition: opacity 0.3s ease; }" +
               ".medicine-card:hover::before { opacity: 1; }" +
               ".medicine-card:hover { transform: translateY(-8px) scale(1.02); box-shadow: 0 20px 40px rgba(45,122,79,0.3); }" +
               ".medicine-card > * { position: relative; z-index: 1; transition: color 0.3s ease; }" +
               ".medicine-card:hover > * { color: white; }" +
               ".med-name { font-weight: 700; font-size: 1rem; margin-bottom: 4px; }" +
               ".med-brand { font-size: 0.85rem; opacity: 0.8; margin-bottom: 8px; }" +
               ".med-price { font-weight: 800; color: #1a5c3a; font-size: 1.1rem; }" +
               ".med-type { font-size: 0.75rem; margin-top: 4px; opacity: 0.7; }" +
               
               "/* PHARMACY LIST WITH ANIMATIONS */" +
               ".pharmacy-list { max-height: 320px; overflow-y: auto; }" +
               ".pharmacy-item { padding: 16px; border-bottom: 1px solid #f0f0f0; cursor: pointer; transition: all 0.3s ease; border-radius: 8px; margin-bottom: 4px; }" +
               ".pharmacy-item:hover { background: linear-gradient(135deg, #f0fdf4, #dcfce7); transform: translateX(8px); box-shadow: 0 4px 12px rgba(45,122,79,0.1); }" +
               ".pharmacy-name { font-weight: 700; color: #1a5c3a; font-size: 1.1rem; margin-bottom: 4px; }" +
               ".pharmacy-address { font-size: 0.9rem; color: #6b7280; margin-bottom: 4px; }" +
               ".pharmacy-distance { font-size: 0.85rem; color: #2d7a4f; font-weight: 600; display: inline-flex; align-items: center; gap: 4px; }" +
               
               "/* KEYFRAME ANIMATIONS */" +
               "@keyframes fadeOut { 0% { opacity: 1; } 100% { opacity: 0; visibility: hidden; } }" +
               "@keyframes logoEntrance { 0% { transform: scale(0.5) rotate(-10deg); opacity: 0; } 100% { transform: scale(1) rotate(0deg); opacity: 1; } }" +
               "@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }" +
               "@keyframes slideIn { 0% { opacity: 0; transform: translateY(30px); } 100% { opacity: 1; transform: translateY(0); } }" +
               "@keyframes mapSlideIn { 0% { transform: translateX(-50px); opacity: 0; } 100% { transform: translateX(0); opacity: 1; } }" +
               "@keyframes sidebarSlideIn { 0% { transform: translateX(50px); opacity: 0; } 100% { transform: translateX(0); opacity: 1; } }" +
               "@keyframes pulse { 0%, 100% { transform: scale(1); } 50% { transform: scale(1.05); } }" +
               "@keyframes glow { 0%, 100% { box-shadow: 0 0 20px rgba(45,122,79,0.3); } 50% { box-shadow: 0 0 40px rgba(45,122,79,0.6); } }" +
               
               "/* SCROLL BAR STYLING */" +
               "::-webkit-scrollbar { width: 8px; }" +
               "::-webkit-scrollbar-track { background: #f1f1f1; border-radius: 4px; }" +
               "::-webkit-scrollbar-thumb { background: linear-gradient(45deg, #2d7a4f, #1a5c3a); border-radius: 4px; }" +
               "::-webkit-scrollbar-thumb:hover { background: linear-gradient(45deg, #1a5c3a, #144a2f); }" +
               "</style></head>" +
               "<body>" +
               "<!-- LOADING SCREEN WITH LOGO -->" +
               "<div class='loading-overlay' id='loadingScreen'>" +
               "<div class='logo-container'>" +
               "<div class='logo-text'>🌿 WellTrack</div>" +
               "<div class='logo-tagline'>Your Healthcare Companion</div>" +
               "<div class='spinner'></div>" +
               "</div>" +
               "</div>" +
               
               "<!-- MAIN DASHBOARD -->" +
               "<div class='dashboard'>" +
               "<div class='map-section'>" +
               "<div id='map'></div>" +
               "</div>" +
               "<div class='sidebar'>" +
               "<div class='section'>" +
               "<h2><span style='font-size: 1.8rem;'>🔍</span> Search Medicines</h2>" +
               "<div class='search-container'>" +
               "<div class='search-icon'>🔎</div>" +
               "<input type='text' class='search-box' placeholder='Type medicine name, brand, or condition...' onkeyup='handleSearch(this.value)'>" +
               "</div>" +
               "</div>" +
               "<div class='section'>" +
               "<h2><span style='font-size: 1.8rem;'>💊</span> Popular Medicines</h2>" +
               "<div class='medicine-grid'>" + medicineCards.toString() + "</div>" +
               "</div>" +
               "<div class='section'>" +
               "<h2><span style='font-size: 1.8rem;'>🏥</span> Nearby Pharmacies</h2>" +
               "<div class='pharmacy-list' id='pharmacyList'>" +
               createPharmacyListHtml(pharmacies) +
               "</div>" +
               "</div>" +
               "</div>" +
               "<script>" +
               "// COOL INITIALIZATION WITH ANIMATIONS" +
               "document.addEventListener('DOMContentLoaded', function() {" +
               "    // Hide loading screen after 2 seconds" +
               "    setTimeout(() => {" +
               "        document.getElementById('loadingScreen').style.display = 'none';" +
               "    }, 2000);" +
               "    " +
               "    // Initialize map with cool effects" +
               "    setTimeout(() => {" +
               "        initializeMap();" +
               "        addCoolEffects();" +
               "    }, 2200);" +
               "});" +
               
               "function initializeMap() {" +
               "    var map = L.map('map').setView([40.7589, -73.9851], 12);" +
               "    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {" +
               "        attribution: '© OpenStreetMap contributors'" +
               "    }).addTo(map);" +
               "    " +
               "    // Add animated pharmacy markers" +
               createPharmacyMarkers(pharmacies) +
               "    " +
               "    // Make map globally accessible" +
               "    window.wellTrackMap = map;" +
               "}" +
               
               "function addCoolEffects() {" +
               "    // Add stagger animation to medicine cards" +
               "    const medicineCards = document.querySelectorAll('.medicine-card');" +
               "    medicineCards.forEach((card, index) => {" +
               "        card.style.animationDelay = (index * 0.1) + 's';" +
               "        card.style.animation = 'slideIn 0.5s ease-out both';" +
               "    });" +
               "    " +
               "    // Add stagger animation to pharmacy items" +
               "    const pharmacyItems = document.querySelectorAll('.pharmacy-item');" +
               "    pharmacyItems.forEach((item, index) => {" +
               "        item.style.animationDelay = (index * 0.1) + 's';" +
               "        item.style.animation = 'slideIn 0.5s ease-out both';" +
               "    });" +
               "}" +
               
               "// Enhanced search with visual feedback" +
               "function handleSearch(query) {" +
               "    const searchBox = event.target;" +
               "    searchBox.style.transform = 'scale(1.02)';" +
               "    setTimeout(() => searchBox.style.transform = 'scale(1)', 200);" +
               "    " +
               "    if (query.length > 2) {" +
               "        // Add loading effect to search box" +
               "        searchBox.style.background = 'linear-gradient(90deg, #f0fdf4, #dcfce7, #f0fdf4)';" +
               "        searchBox.style.backgroundSize = '200% 100%';" +
               "        searchBox.style.animation = 'searchPulse 1s ease-in-out';" +
               "        " +
               "        setTimeout(() => {" +
               "            searchBox.style.background = 'white';" +
               "            searchBox.style.animation = 'none';" +
               "        }, 1000);" +
               "    }" +
               "    console.log('🔍 Searching for:', query);" +
               "}" +
               
               "function searchMedicine(medicineName) {" +
               "    console.log('💊 Selected medicine:', medicineName);" +
               "    " +
               "    // Cool selection animation" +
               "    event.target.style.animation = 'pulse 0.6s ease-in-out';" +
               "    " +
               "    // Show animated notification" +
               "    showNotification('🔍 Searching for ' + medicineName + ' in nearby pharmacies...', 'success');" +
               "    " +
               "    // Highlight matching pharmacies on map with animation" +
               "    if (window.wellTrackMap) {" +
               "        window.wellTrackMap.eachLayer(function(layer) {" +
               "            if (layer instanceof L.Marker) {" +
               "                layer.setOpacity(0.5);" +
               "                setTimeout(() => layer.setOpacity(1), 500);" +
               "            }" +
               "        });" +
               "    }" +
               "}" +
               
               "function goToPharmacy(lat, lng, name) {" +
               "    if (window.wellTrackMap) {" +
               "        window.wellTrackMap.flyTo([lat, lng], 16, {" +
               "            animate: true," +
               "            duration: 1.5" +
               "        });" +
               "        " +
               "        setTimeout(() => {" +
               "            L.popup()" +
               "                .setLatLng([lat, lng])" +
               "                .setContent('<div style=\"text-align:center;padding:10px;\"><b>' + name + '</b><br><span style=\"color:#2d7a4f;\">📍 Selected Pharmacy</span></div>')" +
               "                .openOn(window.wellTrackMap);" +
               "        }, 800);" +
               "    }" +
               "}" +
               
               "function showNotification(message, type) {" +
               "    const notification = document.createElement('div');" +
               "    notification.style.cssText = '" +
               "        position: fixed; top: 20px; right: 20px; z-index: 10000;" +
               "        background: linear-gradient(135deg, #2d7a4f, #1a5c3a);" +
               "        color: white; padding: 16px 20px; border-radius: 12px;" +
               "        box-shadow: 0 8px 25px rgba(45,122,79,0.3);" +
               "        font-weight: 600; transform: translateX(100%);" +
               "        transition: transform 0.5s cubic-bezier(0.4, 0, 0.2, 1);" +
               "    ';" +
               "    notification.textContent = message;" +
               "    document.body.appendChild(notification);" +
               "    " +
               "    setTimeout(() => notification.style.transform = 'translateX(0)', 100);" +
               "    setTimeout(() => {" +
               "        notification.style.transform = 'translateX(100%)';" +
               "        setTimeout(() => document.body.removeChild(notification), 500);" +
               "    }, 3000);" +
               "}" +
               
               "// Add CSS for search animation" +
               "const style = document.createElement('style');" +
               "style.textContent = '@keyframes searchPulse { 0%, 100% { background-position: 0% 50%; } 50% { background-position: 100% 50%; } }';" +
               "document.head.appendChild(style);" +
               "</script>" +
               "</body></html>";
    }
    
    private String createPharmacyListHtml(List<Pharmacy> pharmacies) {
        StringBuilder html = new StringBuilder();
        for (Pharmacy pharmacy : pharmacies.subList(0, Math.min(5, pharmacies.size()))) {
            html.append(String.format(
                "<div class='pharmacy-item' onclick='goToPharmacy(%.6f, %.6f, \"%s\")'>" +
                "<div class='pharmacy-name'>%s</div>" +
                "<div class='pharmacy-address'>%s</div>" +
                "<div class='pharmacy-distance'>%.1f miles away</div>" +
                "</div>",
                pharmacy.getLocation().getLatitude(),
                pharmacy.getLocation().getLongitude(),
                pharmacy.getName().replace("\"", "\\\""),
                pharmacy.getName(),
                pharmacy.getAddress(),
                pharmacy.getDistance()
            ));
        }
        return html.toString();
    }
    
    private String createPharmacyMarkers(List<Pharmacy> pharmacies) {
        StringBuilder markers = new StringBuilder();
        for (Pharmacy pharmacy : pharmacies) {
            markers.append(String.format(
                "L.marker([%.6f, %.6f]).addTo(map)" +
                ".bindPopup('<b>%s</b><br>%s<br>📞 %s<br>%.1f miles away');\n",
                pharmacy.getLocation().getLatitude(),
                pharmacy.getLocation().getLongitude(),
                pharmacy.getName().replace("'", "\\''"),
                pharmacy.getAddress().replace("'", "\\''"),
                pharmacy.getPhone(),
                pharmacy.getDistance()
            ));
        }
        return markers.toString();
    }
    
    private String createSearchResultsHtml(String query, List<Medicine> medicines, List<Pharmacy> pharmacies) {
        StringBuilder medicineResults = new StringBuilder();
        for (Medicine med : medicines.subList(0, Math.min(10, medicines.size()))) {
            String availability = med.getAvailability().equals("Prescription") ? "🔒 Prescription" : "✅ OTC";
            medicineResults.append(String.format(
                "<div class='medicine-card'>" +
                "<div class='medicine-header'><strong>%s</strong> <span class='brand'>(%s)</span></div>" +
                "<div class='medicine-info'>%s • %s • <span class='price'>$%.2f</span></div>" +
                "<div class='medicine-type'>%s • %s</div>" +
                "</div>",
                med.getName(), med.getBrand(), med.getDosage(), med.getType(), med.getPrice(), med.getDescription(), availability
            ));
        }
        
        StringBuilder pharmacyResults = new StringBuilder();
        for (Pharmacy pharmacy : pharmacies.subList(0, Math.min(5, pharmacies.size()))) {
            pharmacyResults.append(String.format(
                "<div class='pharmacy-card'>" +
                "<div class='pharmacy-header'><strong>%s</strong></div>" +
                "<div class='pharmacy-info'>📍 %s</div>" +
                "<div class='pharmacy-contact'>📞 %s • %.1f miles</div>" +
                "</div>",
                pharmacy.getName(), pharmacy.getAddress(), pharmacy.getPhone(), pharmacy.getDistance()
            ));
        }
        
        return "<!DOCTYPE html>" +
               "<html><head><title>WellTrack - Search Results</title>" +
               "<style>" +
               "body { font-family: 'SF Pro Display', -apple-system, system-ui; margin: 0; padding: 20px; background: linear-gradient(135deg, #f0fdf4 0%, #ecfdf5 100%); color: #1a5c3a; line-height: 1.6; }" +
               ".header { text-align: center; margin-bottom: 30px; }" +
               "h1 { color: #1a5c3a; font-size: 2.5rem; font-weight: 900; margin-bottom: 10px; }" +
               ".search-query { font-size: 1.2rem; color: #2d7a4f; margin-bottom: 20px; }" +
               ".section { margin-bottom: 40px; }" +
               ".section h2 { color: #1a5c3a; font-size: 1.8rem; font-weight: 700; margin-bottom: 15px; border-bottom: 3px solid #2d7a4f; padding-bottom: 8px; }" +
               ".medicine-card, .pharmacy-card { background: white; border-radius: 12px; padding: 18px; margin-bottom: 15px; box-shadow: 0 4px 15px rgba(45,122,79,0.1); border: 1px solid rgba(45,122,79,0.1); }" +
               ".medicine-header, .pharmacy-header { font-size: 1.2rem; color: #1a5c3a; margin-bottom: 8px; }" +
               ".brand { color: #2d7a4f; font-weight: 600; }" +
               ".medicine-info, .pharmacy-info, .pharmacy-contact { font-size: 0.95rem; color: #374151; margin: 5px 0; }" +
               ".price { color: #1a5c3a; font-weight: 700; }" +
               ".medicine-type { font-size: 0.9rem; color: #6b7280; }" +
               ".no-results { text-align: center; padding: 40px; color: #6b7280; }" +
               "</style></head>" +
               "<body>" +
               "<div class='header'>" +
               "<h1>🌿 WellTrack Search Results</h1>" +
               (query.isEmpty() ? "<div class='search-query'>Showing all medicines</div>" : 
                String.format("<div class='search-query'>Results for: <strong>%s</strong></div>", query)) +
               "</div>" +
               "<div class='section'>" +
               "<h2>💊 Medicines</h2>" +
               (medicines.isEmpty() ? "<div class='no-results'>No medicines found</div>" : medicineResults.toString()) +
               "</div>" +
               "<div class='section'>" +
               "<h2>🏥 Nearby Pharmacies</h2>" +
               pharmacyResults.toString() +
               "</div>" +
               "</body></html>";
    }
    
    private String createFallbackHtml() {
        return "<!DOCTYPE html>" +
               "<html><head><title>WellTrack</title>" +
               "<style>" +
               "body { font-family: 'SF Pro Display', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 0; background: linear-gradient(135deg, #1a5c3a 0%, #2d7a4f 50%, #1a5c3a 100%); color: #fffef7; min-height: 100vh; display: flex; align-items: center; justify-content: center; }" +
               ".container { text-align: center; padding: 80px 60px; background: rgba(255,254,247,0.12); border-radius: 28px; backdrop-filter: blur(20px); border: 2px solid rgba(255,254,247,0.25); max-width: 700px; box-shadow: 0 25px 50px rgba(0,0,0,0.3); }" +
               "h1 { font-size: 4rem; margin-bottom: 1.5rem; font-weight: 900; text-shadow: 3px 3px 6px rgba(0,0,0,0.4); letter-spacing: -2px; color: #fffef7; }" +
               "p { font-size: 1.3rem; line-height: 1.8; margin-bottom: 1.8rem; opacity: 0.95; }" +
               ".subtitle { font-size: 1.6rem; font-weight: 700; margin-bottom: 2.5rem; opacity: 0.95; color: #fffef7; }" +
               ".loading { display: inline-block; width: 28px; height: 28px; border: 4px solid rgba(255,254,247,0.3); border-radius: 50%; border-top-color: #fffef7; animation: spin 1.5s ease-in-out infinite; margin: 25px 0; }" +
               "@keyframes spin { to { transform: rotate(360deg); } }" +
               ".feature { background: rgba(255,254,247,0.15); padding: 18px 24px; margin: 12px 0; border-radius: 15px; border-left: 5px solid rgba(255,254,247,0.6); font-weight: 600; }" +
               "</style></head>" +
               "<body><div class='container'>" +
               "<h1>🌿 WellTrack</h1>" +
               "<p class='subtitle'>Your Healthcare & Medicine Tracker</p>" +
               "<div class='loading'></div>" +
               "<p>Loading your personalized health dashboard...</p>" +
               "<div class='feature'>💊 Track medications and dosages</div>" +
               "<div class='feature'>🏥 Find nearby pharmacies instantly</div>" +
               "<div class='feature'>📈 Monitor your wellness journey</div>" +
               "</div></body></html>";
    }
    
    private String createErrorHtml(String error) {
        return "<!DOCTYPE html>" +
               "<html><head><title>Error - WellTrack</title>" +
               "<style>" +
               "body { font-family: 'SF Pro Display', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 0; background: linear-gradient(135deg, #dc2626 0%, #b91c1c 100%); color: #fffef7; min-height: 100vh; display: flex; align-items: center; justify-content: center; }" +
               ".container { text-align: center; padding: 50px; background: rgba(255,254,247,0.12); border-radius: 24px; backdrop-filter: blur(15px); border: 2px solid rgba(255,254,247,0.2); max-width: 550px; }" +
               "h1 { font-size: 2.2rem; margin-bottom: 1.5rem; font-weight: 800; }" +
               "p { font-size: 1.1rem; line-height: 1.7; margin-bottom: 1.2rem; opacity: 0.95; }" +
               "</style></head>" +
               "<body><div class='container'>" +
               "<h1>⚠️ Error Loading WellTrack</h1>" +
               "<p>" + error + "</p>" +
               "<p>Please ensure the application components are properly configured.</p>" +
               "</div></body></html>";
    }
}
