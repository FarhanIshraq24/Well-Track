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
               "<html><head><title>Pharmacy Finder</title>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; margin: 40px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }" +
               ".container { text-align: center; padding: 40px; background: rgba(255,255,255,0.1); border-radius: 10px; }" +
               "</style></head>" +
               "<body><div class='container'>" +
               "<h1>🏥 Pharmacy Finder</h1>" +
               "<p>Welcome to the Pharmacy Finder application!</p>" +
               "<p>The React frontend is loading...</p>" +
               "<p>This JavaFX application will help you find nearby pharmacies with medicine availability.</p>" +
               "</div></body></html>";
    }
    
    private String createErrorHtml(String error) {
        return "<!DOCTYPE html>" +
               "<html><head><title>Error - Pharmacy Finder</title>" +
               "<style>body { font-family: Arial, sans-serif; margin: 40px; background: #f44336; color: white; text-align: center; }</style></head>" +
               "<body>" +
               "<h1>⚠️ Error Loading Application</h1>" +
               "<p>" + error + "</p>" +
               "<p>Please ensure the React frontend is built properly.</p>" +
               "</body></html>";
    }
}
