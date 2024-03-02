package com.example;

import java.io.IOException;
// import java.lang.reflect.Array;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ResourceBundle;
import java.sql.Array;
import java.sql.CallableStatement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class StockController implements Initializable{

    // main area for fxID declarations
    @FXML
    private TextField ingredientsField;
    @FXML
    private VBox tableItems;
    @FXML
    private Button exiButton;
    @FXML
    private TextField stockField;
    @FXML
    private Button stockSetBtn;
    @FXML
    private TextField priceField;
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
    @FXML
    private ScrollPane ingredientPane;
    @FXML
    private ScrollPane menuPane;
    @FXML
    private ScrollPane drinkPane;


    //need to move this somewhere more secure
    String dbUrl = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
    String user = "csce331_550_01_user";
    String password = "cSCUE8w9";

    //controls what fields are visable when igredients category is open
    @FXML
    void btnActionIngredients(ActionEvent event){

        category = "ingredients";
        stockField.setVisible(false);
        stockSetBtn.setVisible(false);
        priceField.setVisible(false);
        priceButton.setVisible(false);
        ingredientsField.setVisible(false);
        setFieldsVisibility(true, true, true, true, true);
        String query = "SELECT * FROM ingredients;";
        populateVBoxWithQueryResults(query, true);
        ingredientPane.setContent(tableItems);

    }
    @FXML
    public void changeScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Manager View");
        scene.getStylesheets().add("application.css");
        stage.show();
    }

    //controls what fields are visable when menu category is open
    @FXML
    void btnActionInMenu(ActionEvent event){

        category = "menu_item";
        stockField.setVisible(false);
        stockSetBtn.setVisible(false);
        priceField.setVisible(false);
        priceButton.setVisible(false);
        ingredientsField.setVisible(true);
        String query = "SELECT * FROM menu_Item;";
        setFieldsVisibility(true, true, false, false, true);
        populateVBoxWithQueryResults(query, false);

    }

    //controls what fields are visable when drink category is open and populates the drink box
    // dumbest way possible to do this but didnt want to have to add another variable to function
    @FXML
    void btnActionInDrink(ActionEvent event){
        category = "drinks";
        stockField.setVisible(false);
        stockSetBtn.setVisible(false);
        priceField.setVisible(false);
        priceButton.setVisible(false);
        ingredientsField.setVisible(false);
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
                    priceField.setVisible(true);
                    stockField.setVisible(false);
                    stockSetBtn.setVisible(false);
                    ingredientsField.setVisible(false);
                    setFieldsVisibility(false, false, false, false, false);
                });
                tableItems.getChildren().add(radioButton);
            }
            // ingredientPane.setFitToHeight(true);
            // ingredientPane.setFitToWidth(true);
        } catch (SQLException ex) {
            System.out.println("failed");
        }
        }

    //ended up having to add another variable anyways
    //this functioon just populates the vbox for ingredients and menu. but can be more generalized if more tables added
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
                        priceField.setVisible(true);
                        stockField.setVisible(true);
                        stockSetBtn.setVisible(true);
                        ingredientsField.setVisible(false);
                    }else{
                        priceButton.setVisible(true);
                        priceField.setVisible(true);
                        stockField.setVisible(false);
                        stockSetBtn.setVisible(false);
                        ingredientsField.setVisible(false);
                    }
                    setFieldsVisibility(false, false, false, false, false);

                });
                tableItems.getChildren().add(radioButton);
            }
        } catch (SQLException ex) {
            System.out.println("failied");
        }
    }
    //oop needed to condense the amount of times i did this
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

    //this acts just to get the text field that the radio button has
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

    //BIG function. this is how all the prices are updated when the set price button is pressed
    @FXML
    void priceButtonAction(ActionEvent event) {
        // Get the selected menu item from the radio buttons
        RadioButton selectedRadioButton = getSelectedRadioButton();
        
        if (selectedRadioButton != null) {
            String itemName = selectedRadioButton.getText();
            String priceText = priceField.getText();
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
                        clearFields();
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

    //Big function 2 updates the stock of item
    @FXML
    void stockBtnAction(ActionEvent event){
        RadioButton selectedRadioButton =getSelectedRadioButton();
        String stockText = stockField.getText();
        if(selectedRadioButton != null){
            if(!stockText.isEmpty()){
                try{
                    String itemName = selectedRadioButton.getText();
                    int newStock = Integer.parseInt(stockField.getText());
                    try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
                        PreparedStatement statement = connection.prepareStatement("UPDATE ingredients SET stock = ? WHERE name = ?")) {
                        statement.setDouble(1, newStock);
                        statement.setString(2, itemName);
                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Stock updated successfully.");
                        } else {
                            System.out.println("Failed to update stock.");
                        }
                        clearFields();
                    } catch (SQLException ex) {
                        System.out.println("Error updating stock: " + ex.getMessage());
                    }
                }catch (NumberFormatException e) {
                    System.out.println("Invalid price format.");
                }
            }else{
                System.out.println("Please enter a stock.");
            }
        }else{
            System.out.println("Please select a menu item.");
        }
    }
    // @FXML
    // void onExitBtn(ActionEvent event){
    //     Stage stage = (Stage) exiButton.getScene().getWindow();
    //     stage.close();
    // }
    @FXML
    void newItemBtn(ActionEvent event){
        String stockText = newStockTextField.getText();
        String nameText = nameTextField.getText();
        String priceText = priceTextField.getText();
        String minStockText = minStockTextField.getText();
        String ingredientsText = ingredientsField.getText();
        int rowsAffected = 0;
        Boolean fxnCalled = false;
    

    try(Connection connection = DriverManager.getConnection(dbUrl, user, password)) {
        CallableStatement menuFxn = null;
        PreparedStatement statement =null;
        switch (category) {
            case "ingredients":
                int newStock = Integer.parseInt(stockText);
                double newPrice = Double.parseDouble(priceText);
                int newMinStock = Integer.parseInt(minStockText);
                statement = connection.prepareStatement("INSERT INTO ingredients (name, stock, add_on_price, min_stock) VALUES (?, ?, ?, ?)");
                statement.setString(1, nameText);
                statement.setInt(2, newStock);
                statement.setDouble(3, newPrice);
                statement.setInt(4, newMinStock);
                break;
            case "menu_item": 
                String[] ingredientsArray = ingredientsText.split(",");
                Array ingredients = connection.createArrayOf("TEXT", ingredientsArray);
                double menuItemPrice = Double.parseDouble(priceText);
                menuFxn = connection.prepareCall("{? = CALL new_menu_option(?, ?, ?)}");
                menuFxn.registerOutParameter(1, Types.BOOLEAN);
                menuFxn.setString(2, nameText);
                menuFxn.setFloat(3, (float) menuItemPrice);
                menuFxn.setArray(4, ingredients);
                fxnCalled =true;
                break;
            case "drinks":
                String size = nameText;
                double drinkPrice = Double.parseDouble(priceText);
                statement = connection.prepareStatement("INSERT INTO drinks (size, price) VALUES (?, ?)");
                statement.setString(1, size);
                statement.setDouble(2, drinkPrice);
                break;
            default:
                throw new IllegalArgumentException("Invalid category: " + category);
        }
        if(statement != null){
            rowsAffected = statement.executeUpdate();
        }
        if(fxnCalled){
            rowsAffected = menuFxn.executeUpdate();
        }
        if (rowsAffected > 0 ) {
            System.out.println("Item added successfully.");
        } else {
            System.out.println("Failed to add item.");
        }
        clearFields();
    } catch (SQLException ex) {
        System.out.println("Error adding item: " + ex.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Invalid number format.");
    }
}
//general initialize. most likley wont use here
@Override
public void initialize(URL url, ResourceBundle resourceBundle) {

}

}
