package com.pharmacyfinder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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

        // Set application icon (commented out until icon is available)
        // primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/pharmacy-icon.png")));

        // Show the primary stage
        primaryStage.show();
        
        // Focus on the application window
        primaryStage.toFront();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
