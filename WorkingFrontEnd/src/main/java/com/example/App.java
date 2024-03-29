package com.example;
import com.example.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    // Stage object to hold the primary stage
    private static Stage stg;
    @Override
    public void start(Stage primarystage) throws IOException {
        // Assign the primary stage to the static variable
        stg = primarystage;
        // Make the primary stage non-resizable
        // primarystage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primarystage.setTitle("Login");
        primarystage.setScene(scene);
        scene.getStylesheets().add("application.css");
        primarystage.show();
    }

    public static void main(String[] args) {
        launch();
    }

       /**
     * Changes the scene to manager or waiter scene.
     * Loads the scene FXML file and sets it as the current scene.
     * 
     * @param event An ActionEvent object representing the event of the button press.
     * @throws IOException if there is an error loading the FXML file
     */
    public void changeuserScene(String fxml) throws IOException
    {
        // Load the FXML file for the new scene using FXMLLoader
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        if (App.class.getResource(fxml) != null) {
            String resourcePath = App.class.getResource(fxml).toString();
            if (resourcePath.endsWith("primary.fxml")) {
                stg.setTitle("Manager");
            } else if (resourcePath.endsWith("WaiterView.fxml")) {
                stg.setTitle("Waiter");
            }
        }
        // Load the root element of the FXML file
        Parent root = fxmlLoader.load();
        // Set the root element of the current scene to the new root element
        stg.getScene().setRoot(root);
        stg.getScene().getStylesheets().add("application.css");
    }
}