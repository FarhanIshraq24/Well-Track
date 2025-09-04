package com.pharmacyfinder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.net.URI;

public class PharmacyFinderApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the main window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);

        // Add stylesheet
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        // Set up the stage (window)
        primaryStage.setTitle("Pharmacy Finder");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        // Set application icon
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/pharmacy-icon.png")));

        // Show the primary stage
        primaryStage.show();

        // Launch the external browser
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            URI uri = new URI("http://your-url.com"); // Replace with the URL you want to open
            desktop.browse(uri);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
