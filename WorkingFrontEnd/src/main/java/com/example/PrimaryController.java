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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    TableColumn<RowData, String> item2Name;
    @FXML
    private Button stockBtn;
    @FXML
    private TableColumn<DataItem, Integer> priceColumn;
    @FXML
    DatePicker startDate;
    @FXML
    DatePicker endDate;
    @FXML
    RadioButton salesBtn;
    @FXML
    RadioButton usageBtn;
    @FXML
    RadioButton restockBtn;
    @FXML
    RadioButton excessBtn;
    @FXML
    RadioButton pairsBtn;



    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static String reportType;

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
            reportType = null;
            endDate.setDisable(false);
            
        }catch(IOException e) {
            e.printStackTrace();
        }

    }

    //Simple change scene function
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
        salesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //set the columns to the correct valuesas
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        salesName.setCellValueFactory(new PropertyValueFactory<>("column1"));
        salesQuantity.setCellValueFactory(new PropertyValueFactory<>("column2"));
        item2Name.setCellValueFactory(new PropertyValueFactory<>("column3"));

        //general queries to populate
        String queryIngredients = "SELECT * FROM ingredients;";
        String queryMenu = "SELECT * FROM menu_Item;";
        String queryDrinks = "SELECT * FROM drinks;";
        ToggleGroup group = new ToggleGroup();

        //set the radio buttons to the correct group
        salesBtn.setToggleGroup(group);
        usageBtn.setToggleGroup(group);
        restockBtn.setToggleGroup(group);
        excessBtn.setToggleGroup(group);
        pairsBtn.setToggleGroup(group);

        //set actions for radio buttons
        salesBtn.onActionProperty().set(event -> {
            reportType = "sales";
            endDate.setDisable(false);
            startDate.setDisable(false);
        });
        usageBtn.onActionProperty().set(event -> {
            reportType = "usage";
            endDate.setDisable(false);
            startDate.setDisable(false);
        });
        restockBtn.onActionProperty().set(event -> {
            reportType = "restock";
            endDate.setDisable(true);
            startDate.setDisable(true);
        });
        excessBtn.onActionProperty().set(event -> {
            reportType = "excess";
            endDate.setDisable(true);
            startDate.setDisable(false);
        });
        pairsBtn.onActionProperty().set(event -> {
            reportType = "pairs";
            endDate.setDisable(false);
            startDate.setDisable(false);
        });
        
        //disable dates that are not in the range
        startDate.setDayCellFactory(picker -> new DateCell() {
        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            LocalDate minDate = LocalDate.of(2023, 1, 1);
            LocalDate maxDate = LocalDate.now();
            setDisable(empty || date.compareTo(minDate) < 0 || date.compareTo(maxDate) > 0);
        }
        });

        //disable dates that are not in the range
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

                //add the item to the table view check if unchecked
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

                //add the item to the table view check if unchecked
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

                //add the item to the table view check if unchecked
                checkBox.setOnAction(event -> {
                    if(checkBox.isSelected()){
                        populateTableView("drinks", drink);
                    }else{
                        removeItemFromTableView(drink);
                    } });
                vboxDrink.getChildren().add(checkBox);
            }

        //catch sql erros and show error window
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("SQL ERROR");
            alert.setHeaderText("Database not connected");
            alert.setContentText("Enusure you are connected to internet and try again. If problem persists, contact support." + e);
            alert.showAndWait();
            return;
        }
    }
    //when diselected item is removed from table view
    private void removeItemFromTableView(String itemName) {
        tableView.getItems().removeIf(item -> item.getName().equals(itemName));
    }

    //change scene to Waiter
    @FXML
    public void changeSceneWaiter(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("WaiterView.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Waiter");
        scene.getStylesheets().add("application.css");
        stage.show();
    }

    //logout button just changes back to login fxml
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

    //generates the report based on the radio button selected
    @FXML
    void salesTableBtnAction(ActionEvent event){
        LocalDate startDateValue = startDate.getValue();
        LocalDate endDateValue = endDate.getValue();
        Timestamp start = null;
        Timestamp end = null;

        //setting time to now if end date not selected
        if (endDateValue == null) {
            endDateValue = LocalDate.now();
        }

        //restock requires no date and time no need to set
        if(!("restock".equals(reportType))){
            start = Timestamp.valueOf(startDateValue.atStartOfDay());
            LocalDateTime endDateTime = endDateValue.atTime(23, 59, 59);
            end = Timestamp.valueOf(endDateTime);
        }
           
        try{
            connection = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db", "csce331_550_01_user", "cSCUE8w9");

            //error if no report type selected
            if (reportType == null) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("NO REPORT SELECTED");
                alert.setHeaderText("you must select a report type to generate a report");
                alert.setContentText("Please select a report type to generate a report.");

                alert.showAndWait();
                return;
            }

            switch (reportType) {

                case "sales":

                if(start == null || end == null){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("NO DATE SELECTED");
                    alert.setHeaderText("you must select a date to generate a report");
                    alert.setContentText("Please select a date to generate a report.");

                    alert.showAndWait();
                    return;
                }

                salesName.setText("Item");
                item2Name.setText("");
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
      
                    RowData row = new RowData(column1, column2,null);
                    salesTable.getItems().add(row);
                }
                break;

                case "usage":

                if(start == null || end == null){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("NO DATE SELECTED");
                    alert.setHeaderText("you must select a date to generate a report");
                    alert.setContentText("Please select a date to generate a report.");

                    alert.showAndWait();
                    return;
                }

                CallableStatement statement2 = connection.prepareCall("{call product_usage(?, ?)}");
                salesName.setText("Item");
                item2Name.setText("");
                statement2.setTimestamp(1, start);
                statement2.setTimestamp(2, end);
                resultSet = statement2.executeQuery();

                // Clear the table
                salesTable.getItems().clear();

                // Loop through the result set and add rows to the table
                while (resultSet.next()) {
                    String column1 = resultSet.getString("name");
                    long column2 = resultSet.getLong("count");
                    RowData row = new RowData(column1, column2,null);
                    salesTable.getItems().add(row);
                }
                break;
                
                case "excess":
                    if (start ==null) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("NO DATE SELECTED");
                        alert.setHeaderText("you must select a date to generate a report");
                        alert.setContentText("Please select a date to generate a report.");
    
                        alert.showAndWait();
                        return;
                    }

                    CallableStatement statement3 = connection.prepareCall("{call excess_report(?, ?)}");
                    statement3.setTimestamp(1, start);
                    statement3.setTimestamp(2, end );
                    resultSet = statement3.executeQuery();

                    // Clear the table
                    salesTable.getItems().clear();

                    // Loop through the result set and add rows to the table
                    while (resultSet.next()) {
                        String column1 = resultSet.getString("name_of_item");
                        RowData row = new RowData(column1, 0,null);
                        salesTable.getItems().add(row);
                    }
                    break;
                
                case "pairs":

                    CallableStatement statement4 = connection.prepareCall("{call sells_together(?,?)}");
                    salesName.setText("Item 1");
                    item2Name.setText("Item 2");
                    statement4.setTimestamp(1, start);
                    statement4.setTimestamp(2, end);
                    resultSet = statement4.executeQuery();

                    // Clear the table
                    salesTable.getItems().clear();

                    // Loop through the result set and add rows to the table
                    while (resultSet.next()) {
                        String column1 = resultSet.getString("item_1");
                        String column2 = resultSet.getString("item_2");
                        long column3 = resultSet.getLong("total_times_combined");
                        RowData row = new RowData(column1, column3, column2);
                        salesTable.getItems().add(row);
                    }
                    break;

                case "restock":
                    salesName.setText("Item");
                    item2Name.setText("");
                    CallableStatement statement5 = connection.prepareCall("{call restock()}");
                    resultSet = statement5.executeQuery();

                    // Clear the table
                    salesTable.getItems().clear();

                    // Loop through the result set and add rows to the table
                    while (resultSet.next()) {
                        String column1 = resultSet.getString("item");
                        int column2 = resultSet.getInt("quantity");
                        RowData row = new RowData(column1, (long) column2, null);
                        salesTable.getItems().add(row);
                    }
                    break;

        }

        // clear dates after report is generated
        startDate.setValue(null);
        endDate.setValue(null);

        //catch sql errors and show error window
        }catch(SQLException e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("SQL ERROR");
            alert.setHeaderText("Database not connected");
            alert.setContentText("Enusure you are connected to internet and try again. If problem persists, contact support." + e);
            alert.showAndWait();
            return;
        }
    }

    //class to hold the data for the table
    public static class RowData {
    private final SimpleStringProperty column1;
    private final LongProperty column2;
    private final SimpleStringProperty column3;

    public RowData(String column1, long column2, String column3) {
        this.column1 = new SimpleStringProperty(column1);
        this.column2 = new SimpleLongProperty(column2);
        if (column3 != null) {
            this.column3 = new SimpleStringProperty(column3);
        } else {
            this.column3 = new SimpleStringProperty("");
        }
    }

    public String getColumn1() {
        return column1.get();
    }

    public long getColumn2() {
        return column2.get();
    }

    public String getColumn3() {
        return column3.get();
    }
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
                    }

                    if("ingredients".equals(tableName)){
                        stock = String.valueOf(resultSet.getInt("stock"));
                    }else{
                        stock = "N/A";
                    }
                    quantity=resultSet.getDouble(priceName);
                    
                    tableView.getItems().add(new DataItem(name, quantity, stock));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("SQL ERROR");
            alert.setHeaderText("Database not connected");
            alert.setContentText("Enusure you are connected to internet and try again. If problem persists, contact support." + e);
            alert.showAndWait();
            return;
        }
    }

    //class to hold the data for the table
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
