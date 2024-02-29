package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
    @FXML
    private Button stockBtn;


    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    @FXML

    //this just opens the second window to edit stuff
    void stockBtnAction(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("stockWindow.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //need to securely do this
        String dbUrl = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

            //hold the name and quanity of items
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        //general queries to populate
        String queryIngredients = "SELECT * FROM ingredients;";
        String queryMenu = "SELECT * FROM menu_Item;";
        String queryDrinks = "SELECT * FROM drinks;";



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

            //menu population
            resultSet = statement.executeQuery(queryMenu);
            while (resultSet.next()) {
                String menuItem = resultSet.getString("name");
                CheckBox checkBox = new CheckBox(menuItem);
                checkBox.setOnAction(event -> {
                    if(checkBox.isSelected()){
                        populateTableView("menu_item", menuItem);
                    }else{
                        removeItemFromTableView(menuItem);
                    } });
                vboxMenuItem.getChildren().add(checkBox);
            }

            //drink population
            resultSet = statement.executeQuery(queryDrinks);
            while (resultSet.next()) {
                String drink = resultSet.getString("size");
                CheckBox checkBox = new CheckBox(drink);
                checkBox.setOnAction(event -> {
                    if(checkBox.isSelected()){
                        populateTableView("drinks", drink);
                    }else{
                        removeItemFromTableView(drink);
                    } });
                vboxDrink.getChildren().add(checkBox);
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {

        }
    }
    //when diselected item is removed from table view
    private void removeItemFromTableView(String itemName) {
        tableView.getItems().removeIf(item -> item.getName().equals(itemName));
    }

    //adds the items to the table
    private void populateTableView(String tableName, String itemName) {
        String columnName = "name";
        if ("drinks".equals(tableName)) {
            columnName = "size";
        }
    
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE " + columnName + " = ?")) {
            preparedStatement.setString(1, itemName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int quantity;
                    String name;
                    if("drinks".equals(tableName)){
                        name = resultSet.getString("size");
                    }else{
                        name = resultSet.getString("name");
                    }
                    if("ingredients".equals(tableName)){
                        quantity = resultSet.getInt("stock");
                    }else{
                        quantity=resultSet.getInt("price");
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
