package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable{

    @FXML
    private VBox vboxIngredients;
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public void Query(ActionEvent e) throws SQLException {
        String dbUrl = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

        String query = "SELECT * FROM menu_Item;";
        List<Button> buttonlist = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(dbUrl, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) { //iterate over every row returned
                String restaurant = resultSet.getString("name");
                buttonlist.add(new Button(restaurant));
            }
            vboxIngredients.getChildren().clear();
            vboxIngredients.getChildren().addAll(buttonlist);

        } catch (SQLException ex) {
                ex.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }  
        

    
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String dbUrl = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

        String queryIngredients = "SELECT * FROM menu_Item;";
        List<CheckBox> ingredientsList = new ArrayList<>();
        List<CheckBox> menuList = new ArrayList<>();
        List<CheckBox> drinkList = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(dbUrl, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(queryIngredients);
            while (resultSet.next()) { //iterate over every row returned
                String restaurant = resultSet.getString("name");
                ingredientsList.add(new CheckBox(restaurant));
            }
            vboxIngredients.getChildren().clear();
            vboxIngredients.getChildren().addAll(ingredientsList);

        } catch (SQLException ex) {
                ex.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    }