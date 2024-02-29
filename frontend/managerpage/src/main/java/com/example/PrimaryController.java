package com.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {

    @FXML
    private VBox vboxIngredients;
    @FXML
    private VBox vboxMenuItem;
    @FXML
    private VBox vboxDrink;

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String dbUrl = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

        String queryIngredients = "SELECT * FROM ingredients;";
        String queryMenu = "SELECT * FROM menu_Item;";
        String queryDrinks = "SELECT * FROM drinks;";
        List<CheckBox> ingredientsList = new ArrayList<>();
        List<CheckBox> menuList = new ArrayList<>();
        List<CheckBox> drinkList = new ArrayList<>();

        try {
            //ingredient population
            connection = DriverManager.getConnection(dbUrl, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(queryIngredients);
            while (resultSet.next()) {
                String ingredient = resultSet.getString("name");
                ingredientsList.add(new CheckBox(ingredient));
            }
            vboxIngredients.getChildren().clear();
            vboxIngredients.getChildren().addAll(ingredientsList);

            //menu population
            resultSet = statement.executeQuery(queryMenu);
            while (resultSet.next()) {
                String menuItem = resultSet.getString("name");
                menuList.add(new CheckBox(menuItem));
            }
            vboxMenuItem.getChildren().clear();
            vboxMenuItem.getChildren().addAll(menuList);

            //drink population
            resultSet = statement.executeQuery(queryDrinks);
            while (resultSet.next()) {
                String drink = resultSet.getString("size");
                drinkList.add(new CheckBox(drink));
            }
            vboxDrink.getChildren().clear();
            vboxDrink.getChildren().addAll(drinkList);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
