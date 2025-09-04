package com.pharmacyfinder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.fxml.Initializable;
import javafx.concurrent.Worker;
import javafx.application.Platform;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.File;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;
    
    @FXML
    private WebView webView;
    
    private WebEngine webEngine;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (webView != null) {
            webEngine = webView.getEngine();
            
            // Enable JavaScript
            webEngine.setJavaScriptEnabled(true);
            
            // Set up JavaScript bridge
            setupJavaScriptBridge();
            
            // Load the built React application
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
    
    private void loadWebApplication() {
        try {
            // Try to load the built React app first
            File indexFile = new File("src/main/resources/web/index.html");
            if (indexFile.exists()) {
                String url = indexFile.toURI().toString();
                webEngine.load(url);
            } else {
                // Fallback: show a simple HTML page
                String fallbackHtml = createFallbackHtml();
                webEngine.loadContent(fallbackHtml);
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
                    // Inject Java API into JavaScript
                    Platform.runLater(() -> {
                        webEngine.executeScript(
                            "window.javaAPI = {" +
                                "getCurrentLocation: function() {" +
                                "    return {" +
                                "        latitude: 23.8103," +
                                "        longitude: 90.4125," +
                                "        address: 'Dhaka, Bangladesh'" +
                                "    };" +
                                "}," +
                                "findNearbyPharmacies: function(lat, lng, medicine) {" +
                                "    return [" +
                                "        {" +
                                "            name: 'City Pharmacy'," +
                                "            address: '123 Main Street, Dhaka'," +
                                "            phone: '+880-123-456789'," +
                                "            latitude: 23.8103," +
                                "            longitude: 90.4125," +
                                "            hasStock: true," +
                                "            searchedMedicine: medicine," +
                                "            distance: 0.5" +
                                "        }," +
                                "        {" +
                                "            name: 'HealthCare Plus'," +
                                "            address: '456 Park Avenue, Dhaka'," +
                                "            phone: '+880-987-654321'," +
                                "            latitude: 23.8203," +
                                "            longitude: 90.4225," +
                                "            hasStock: false," +
                                "            searchedMedicine: medicine," +
                                "            distance: 1.2" +
                                "        }," +
                                "        {" +
                                "            name: 'MediCare Pharmacy'," +
                                "            address: '789 Health Avenue, Dhaka'," +
                                "            phone: '+880-555-123456'," +
                                "            latitude: 23.8000," +
                                "            longitude: 90.4100," +
                                "            hasStock: true," +
                                "            searchedMedicine: medicine," +
                                "            distance: 0.8" +
                                "        }" +
                                "    ];" +
                                "}" +
                            "};"
                        );
                    });
                }
            }
        );
    }
    
    private String createFallbackHtml() {
        return "<!DOCTYPE html>" +
               "<html><head><title>Welltrack</title>" +
               "<style>" +
               "body { font-family: 'SF Pro Display', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 0; background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%); color: white; min-height: 100vh; display: flex; align-items: center; justify-content: center; }" +
               ".container { text-align: center; padding: 60px 40px; background: rgba(255,255,255,0.1); border-radius: 20px; backdrop-filter: blur(10px); border: 1px solid rgba(255,255,255,0.2); max-width: 600px; }" +
               "h1 { font-size: 3rem; margin-bottom: 1rem; font-weight: 700; }" +
               "p { font-size: 1.1rem; line-height: 1.6; margin-bottom: 1rem; opacity: 0.9; }" +
               ".loading { display: inline-block; width: 20px; height: 20px; border: 3px solid rgba(255,255,255,0.3); border-radius: 50%; border-top-color: white; animation: spin 1s ease-in-out infinite; }" +
               "@keyframes spin { to { transform: rotate(360deg); } }" +
               "</style></head>" +
               "<body><div class='container'>" +
               "<h1>💊 Welltrack</h1>" +
               "<p>Your Healthcare & Medicine Tracker</p>" +
               "<div class='loading'></div>" +
               "<p>Loading your health dashboard...</p>" +
               "<p>Track medications, find pharmacies, and manage your health journey with ease.</p>" +
               "</div></body></html>";
    }
    
    private String createErrorHtml(String error) {
        return "<!DOCTYPE html>" +
               "<html><head><title>Error - Welltrack</title>" +
               "<style>" +
               "body { font-family: 'SF Pro Display', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 0; background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%); color: white; min-height: 100vh; display: flex; align-items: center; justify-content: center; }" +
               ".container { text-align: center; padding: 40px; background: rgba(255,255,255,0.1); border-radius: 20px; backdrop-filter: blur(10px); border: 1px solid rgba(255,255,255,0.2); max-width: 500px; }" +
               "h1 { font-size: 2rem; margin-bottom: 1rem; }" +
               "p { font-size: 1rem; line-height: 1.6; margin-bottom: 1rem; opacity: 0.9; }" +
               "</style></head>" +
               "<body><div class='container'>" +
               "<h1>⚠️ Error Loading Welltrack</h1>" +
               "<p>" + error + "</p>" +
               "<p>Please ensure the application components are properly configured.</p>" +
               "</div></body></html>";
    }
}
