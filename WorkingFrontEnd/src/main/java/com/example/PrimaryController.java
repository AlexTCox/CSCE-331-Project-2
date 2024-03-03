package com.example;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.scene.Node;

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
    private TableColumn<DataItem, Integer> stockColumn;
    @FXML 
    TableView<RowData> salesTable;
    @FXML 
    TableColumn<RowData, String> salesName;
    @FXML
    TableColumn<RowData, Integer> salesQuantity;
    @FXML
    TableColumn<RowData, Double> saleDate;
    @FXML
    private Button stockBtn;
    @FXML
    private TableColumn<DataItem, Integer> priceColumn;
    @FXML
    DatePicker startDate;
    @FXML
    DatePicker endDate;


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
            stage.setTitle("Manage Stock");
            scene.getStylesheets().add("application.css");
            stage.show();
            
        }catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void changeScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("stockWindow.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Manage Stock");
        scene.getStylesheets().add("application.css");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //need to securely do this
        String dbUrl = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

            //hold the name and quanity of items
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        salesName.setCellValueFactory(new PropertyValueFactory<>("column1"));
        salesQuantity.setCellValueFactory(new PropertyValueFactory<>("column2"));

        //general queries to populate
        String queryIngredients = "SELECT * FROM ingredients;";
        String queryMenu = "SELECT * FROM menu_Item;";
        String queryDrinks = "SELECT * FROM drinks;";

        startDate.setDayCellFactory(picker -> new DateCell() {
        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            LocalDate minDate = LocalDate.of(2023, 1, 1);
            LocalDate maxDate = LocalDate.now();
            setDisable(empty || date.compareTo(minDate) < 0 || date.compareTo(maxDate) > 0);
        }
        });

        endDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate minDate = startDate.getValue();
        
                setDisable(empty || (minDate != null && date.compareTo(minDate) < 0) || date.compareTo(LocalDate.now()) > 0);
            }
        });


        try {
            //ingredient population
            connection = DriverManager.getConnection(dbUrl, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(queryIngredients);
            while (resultSet.next()) {
                String ingredient = resultSet.getString("name");
                CheckBox checkBox = new CheckBox(ingredient);
                checkBox.getStyleClass().add("checkBox");
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
                checkBox.getStyleClass().add("checkBox");
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
                checkBox.getStyleClass().add("checkBox");
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

    @FXML
    public void logoutBtnAction(ActionEvent event){
        try{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));            
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void salesTableBtnAction(ActionEvent event){
        LocalDate startDateValue = startDate.getValue();
        LocalDate endDateValue = endDate.getValue();

        Timestamp start = Timestamp.valueOf(startDateValue.atStartOfDay());
        Timestamp end = Timestamp.valueOf(endDateValue.atStartOfDay());
        try{
            connection = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db", "csce331_550_01_user", "cSCUE8w9");
            CallableStatement statement = connection.prepareCall("{call sales_report(?, ?)}");
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            ResultSet resultSet = statement.executeQuery();
    
            // Clear the table
            salesTable.getItems().clear();
    
            // Loop through the result set and add rows to the table
            while (resultSet.next()) {
                String column1 = resultSet.getString("item_name");
                long column2 = resultSet.getLong("count");
                // Get more columns as needed
    
                RowData row = new RowData(column1, column2);
                salesTable.getItems().add(row);
            }
    
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static class RowData {
    private final SimpleStringProperty column1;
    private final LongProperty column2;
    // Add more properties as needed

    public RowData(String column1, long column2) {
        this.column1 = new SimpleStringProperty(column1);
        this.column2 = new SimpleLongProperty(column2);
    }

    public String getColumn1() { return column1.get(); }
    public long getColumn2() { return column2.get(); }
    // Add more getters as needed
}

    //adds the items to the table
    private void populateTableView(String tableName, String itemName) {
        String columnName = "name";
        String priceName = "price";
        if ("drinks".equals(tableName)) {
            columnName = "size";
        }
        if("ingredients".equals(tableName)){
            priceName = "add_on_price";
        }
    
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE " + columnName + " = ?")) {
            preparedStatement.setString(1, itemName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    double quantity;
                    String name;
                    String stock;
                    if("drinks".equals(tableName)){
                        name = resultSet.getString("size");
                        stock = "N/A";
                    }else{
                        name = resultSet.getString("name");
                        if("ingredients".equals(tableName)){
                            stock = String.valueOf(resultSet.getInt("stock"));
                        }else{
                            stock = "N/A";
                        }
                    }
                    quantity=resultSet.getDouble(priceName);
                    
                    tableView.getItems().add(new DataItem(name, quantity, stock));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static class DataItem {
        private final String name;
        private final double price;
        private final String stock;
    
        public DataItem(String name, double price, String stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }
    
        public String getName() {
            return name;
        }
    
        public double getPrice() {
            return price;
        }
    
        public String getStock() {
            return stock;
        }
    }
}
