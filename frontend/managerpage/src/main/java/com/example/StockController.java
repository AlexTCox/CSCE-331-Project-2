package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



public class StockController implements Initializable{
    // @FXML
    // ChoiceBox<String> tableSelctor;
    // @FXML
    // CheckBox itemCheckBox;
    @FXML
    private VBox tableItems;

    String dbUrl = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
    String user = "csce331_550_01_user";
    String password = "cSCUE8w9";

    // String queryTables = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE'";

    @FXML
    void btnActionIngredients(ActionEvent event){
        String query = "SELECT * FROM ingredients;";
        populateVBoxWithQueryResults(query);
    }
    @FXML
    void btnActionInMenu(ActionEvent event){
        String query = "SELECT * FROM menu_Item;";
        populateVBoxWithQueryResults(query);
    }
    @FXML
    void btnActionInDrink(ActionEvent event){
        tableItems.getChildren().clear();

        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM drinks")) {

            while (resultSet.next()) {
                String name = resultSet.getString("size");
                CheckBox checkBox = new CheckBox(name);
                tableItems.getChildren().add(checkBox);
            }
        } catch (SQLException ex) {
            System.out.println("failied");
        }
    }

    
    private void populateVBoxWithQueryResults(String query) {
        // Clear the existing content in the VBox
        tableItems.getChildren().clear();

        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Iterate over the result set and populate the VBox with data
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                CheckBox checkBox = new CheckBox(name);
                tableItems.getChildren().add(checkBox);
            }
        } catch (SQLException ex) {
            System.out.println("failied");
        }
    }
@Override
public void initialize(URL url, ResourceBundle resourceBundle) {

}

}
