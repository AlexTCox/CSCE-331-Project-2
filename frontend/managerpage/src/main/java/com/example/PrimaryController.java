package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    private TableView<DataItem> tableView;
    @FXML
    private TableColumn<DataItem, String> nameColumn;
    @FXML
    private TableColumn<DataItem, Integer> quantityColumn;

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public void onSelect(ActionEvent e){
        
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String dbUrl = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

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
                CheckBox checkBox = new CheckBox(ingredient);
                checkBox.setOnAction(event -> {
                    if(checkBox.isSelected()){
                        populateTableView("ingredients", ingredient);
                    }else{
                        removeItemFromTableView(ingredient);
                    } });
                vboxIngredients.getChildren().add(checkBox);
            }
            // vboxIngredients.getChildren().clear();
            // vboxIngredients.getChildren().addAll(ingredientsList);

            //menu population
            resultSet = statement.executeQuery(queryMenu);
            while (resultSet.next()) {
                String menuItem = resultSet.getString("name");
                CheckBox checkBox = new CheckBox(menuItem);
                checkBox.setOnAction(event -> populateTableView("menu_item", menuItem) );
                vboxMenuItem.getChildren().add(checkBox);
            }
            // vboxMenuItem.getChildren().clear();
            // vboxMenuItem.getChildren().addAll(menuList);

            //drink population
            resultSet = statement.executeQuery(queryDrinks);
            while (resultSet.next()) {
                String drink = resultSet.getString("size");
                CheckBox checkBox = new CheckBox(drink);
                checkBox.setOnAction(event -> populateTableView("drinks", drink) );
                vboxDrink.getChildren().add(checkBox);
            }
            // vboxDrink.getChildren().clear();
            // vboxDrink.getChildren().addAll(drinkList);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {

        }
    }

    private void removeItemFromTableView(String itemName) {
        tableView.getItems().removeIf(item -> item.getName().equals(itemName));
    }

    private void populateTableView(String tableName, String itemName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE name = ?")) {
            preparedStatement.setString(1, itemName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int quantity;
                    String name = resultSet.getString("name");
                    if(tableName == "ingredients"){
                        quantity = resultSet.getInt("stock");
                    }else{
                        quantity = resultSet.getInt("price");
                    }
                    tableView.getItems().add(new DataItem(name, quantity));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
        public static class DataItem {
        private final String name;
        private final int quantity;

        public DataItem(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
