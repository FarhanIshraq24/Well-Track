package com.pharmacyfinder.controller;

import com.pharmacyfinder.service.LocationService;
import com.pharmacyfinder.service.PharmacyService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private LocationService locationService;
    private PharmacyService pharmacyService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        locationService = new LocationService();
        pharmacyService = new PharmacyService();

        // Load React application in an external browser
        loadReactApp();
    }

    private void loadReactApp() {
        // Provide the absolute path of the index.html file
        String url = "file:///Users/farhanishraq/Documents/Welltrack/src/main/resources/web/index.html"; // Use the correct path to your HTML file

        openExternalBrowser(url); // Open it in an external browser
    }

    private void openExternalBrowser(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                URI uri = new URI(url); // Convert string URL to URI
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(uri); // Open the URL in the default web browser
            } catch (IOException | java.net.URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Desktop is not supported on this platform.");
        }
    }
}
