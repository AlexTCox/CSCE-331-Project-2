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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
    @FXML
    private TextField stockField;
    @FXML
    private Button stockSetBtn;
    @FXML
    private TextField pricField;
    @FXML
    private Button priceButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField newStockTextField;
    @FXML
    private TextField minStockTextField;
    @FXML
    private Button submitItemBtn;
    @FXML
    private String category = "";

    String dbUrl = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
    String user = "csce331_550_01_user";
    String password = "cSCUE8w9";

    // String queryTables = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE'";

    @FXML
    void btnActionIngredients(ActionEvent event){
        category = "ingredients";
        stockField.setVisible(false);
        stockSetBtn.setVisible(false);
        pricField.setVisible(false);
        priceButton.setVisible(false);
        setFieldsVisibility(true, true, true, true, true);
        String query = "SELECT * FROM ingredients;";
        populateVBoxWithQueryResults(query, true);

    }
    @FXML
    void btnActionInMenu(ActionEvent event){
        category = "menu_item";
        stockField.setVisible(false);
        stockSetBtn.setVisible(false);
        pricField.setVisible(false);
        priceButton.setVisible(false);
        String query = "SELECT * FROM menu_Item;";
        setFieldsVisibility(true, true, false, false, true);
        populateVBoxWithQueryResults(query, false);

    }
    @FXML
    void btnActionInDrink(ActionEvent event){
        category = "drinks";
        stockField.setVisible(false);
        stockSetBtn.setVisible(false);
        pricField.setVisible(false);
        priceButton.setVisible(false);
        setFieldsVisibility(true, true, false, false, true);
        tableItems.getChildren().clear();
        ToggleGroup group = new ToggleGroup();

        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM drinks")) {

            while (resultSet.next()) {
                String name = resultSet.getString("size");
                RadioButton radioButton = new RadioButton(name);
                radioButton.setToggleGroup(group);
                radioButton.setOnAction(e -> {
                    priceButton.setVisible(true);
                    pricField.setVisible(true);
                    stockField.setVisible(false);
                    stockSetBtn.setVisible(false);
                    setFieldsVisibility(false, false, false, false, false);
                });
                tableItems.getChildren().add(radioButton);
            }
        } catch (SQLException ex) {
            System.out.println("failed");
        }
        }

    
    private void populateVBoxWithQueryResults(String query, Boolean ingredient) {
        // Clear the existing content in the VBox
        ToggleGroup group = new ToggleGroup();
        tableItems.getChildren().clear();

        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Iterate over the result set and populate the VBox with data
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                RadioButton radioButton = new RadioButton(name);
                radioButton.setToggleGroup(group);
                radioButton.setOnAction(event -> {
                    if(ingredient){
                        priceButton.setVisible(true);
                        pricField.setVisible(true);
                        stockField.setVisible(true);
                        stockSetBtn.setVisible(true);
                    }else{
                        priceButton.setVisible(true);
                        pricField.setVisible(true);
                        stockField.setVisible(false);
                        stockSetBtn.setVisible(false);
                    }
                    setFieldsVisibility(false, false, false, false, false);

                });
                tableItems.getChildren().add(radioButton);
            }
        } catch (SQLException ex) {
            System.out.println("failied");
        }
    }
private void setFieldsVisibility(boolean nameVisible, boolean priceVisible, boolean newStockVisible,
                                     boolean minStockVisible, boolean submitVisible) {
        nameTextField.setVisible(nameVisible);
        priceTextField.setVisible(priceVisible);
        newStockTextField.setVisible(newStockVisible);
        minStockTextField.setVisible(minStockVisible);
        submitItemBtn.setVisible(submitVisible);
    }

    private void clearFields() {
        nameTextField.clear();
        priceTextField.clear();
        newStockTextField.clear();
        minStockTextField.clear();
        stockField.clear();
    }

    private RadioButton getSelectedRadioButton() {
        for (int i = 0; i < tableItems.getChildren().size(); i++) {
            if (tableItems.getChildren().get(i) instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) tableItems.getChildren().get(i);
                if (radioButton.isSelected()) {
                    return radioButton;
                }
            }
        }
        return null;
    }

    @FXML
    void priceButtonAction(ActionEvent event) {
        // Get the selected menu item from the radio buttons
        RadioButton selectedRadioButton = getSelectedRadioButton();
        
        if (selectedRadioButton != null) {
            String itemName = selectedRadioButton.getText();
            String priceText = pricField.getText();
            String priceName = "price";
            if (!priceText.isEmpty()) {
                try {
                    double newPrice = Double.parseDouble(priceText);
                    String tableName = category;
                    if("ingredients".equals(tableName)){
                        priceName="add_on_price";
                    }
                    // Update the price of the selected item in the database
                    try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
                         PreparedStatement statement = connection.prepareStatement("UPDATE " + tableName +" SET "+ priceName +" = ? WHERE name = ?")) {
                        statement.setDouble(1, newPrice);
                        statement.setString(2, itemName);
                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Price updated successfully.");
                        } else {
                            System.out.println("Failed to update price.");
                        }
                    } catch (SQLException ex) {
                        System.out.println("Error updating price: " + ex.getMessage());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price format.");
                }
            } else {
                System.out.println("Please enter a price.");
            }
        } else {
            System.out.println("Please select a menu item.");
        }
    }


@Override
public void initialize(URL url, ResourceBundle resourceBundle) {

}

}
