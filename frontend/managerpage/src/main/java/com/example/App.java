package com.example;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.*;

import java.io.IOException;

/**
 * JavaFX App
 */
    public class App extends Application {

        @Override
        public void start(Stage stage) throws IOException {
            VBox root = new VBox(10); // Container to hold CheckBoxes
            String user = "csce331_550_01_user";
            String pswd = "cSCUE8w9";
            try {
                Connection connection = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db", user, pswd);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT name, stock FROM ingredients");
    
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    CheckBox checkBox = new CheckBox(name);
                    root.getChildren().add(checkBox); // Add CheckBox to VBox
                }
                resultSet.close();
                statement.close();
                connection.close();
    
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
    
            Scene scene = new Scene(root, 600, 500, Color.BLUE);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    
            stage.setScene(scene);
            stage.show();
        }
    
        public static void main(String[] args) {
            launch();
        }
    }
    