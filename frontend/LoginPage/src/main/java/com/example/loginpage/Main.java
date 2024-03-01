package com.example.loginpage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    // Stage object to hold the primary stage
    private static Stage stg;
    @Override
    public void start(Stage primarystage) throws IOException {
        // Assign the primary stage to the static variable
        stg = primarystage;
        // Make the primary stage non-resizable
        primarystage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        primarystage.setTitle("Login");
        primarystage.setScene(scene);
        primarystage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    // Method to change the scene to the one specified by the given FXML file
    public void changeuserScene(String fxml) throws IOException
    {
        // Load the FXML file for the new scene using FXMLLoader
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        // Load the root element of the FXML file
        Parent root = fxmlLoader.load();
        // Set the root element of the current scene to the new root element
        stg.getScene().setRoot(root);
    }
}