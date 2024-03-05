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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.sql.Array;
import java.sql.CallableStatement;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    @FXML
    private ScrollPane menuIngredients;
    @FXML
    private VBox menuIngredVbox;
    @FXML
    private Text currStock;
    @FXML
    Text selectIngredients;
    @FXML
    Text newItemText;

    ObservableList<Node> checkboxes;
    ArrayList<String> selectedItems = new ArrayList<>();


    //need to move this somewhere more secure
    String dbUrl = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
    String user = "csce331_550_01_user";
    String password = "cSCUE8w9";
    ToggleGroup group = new ToggleGroup();
    public class itemData {
        private int stock;
        private String name;
    
        public itemData(int stock, String name) {
            this.stock = stock;
            this.name = name;
        }

        public int getStock() {
            return stock;
        }
        public String getName() {
            return name;
        }
    
    }



    @FXML
    void btnActionIngredients(ActionEvent event){

        category = "ingredients";
        newItemText.setText("Create New Ingredient");
        nameTextField.setPromptText("Name");
        selectIngredients.setText("");
        stockField.setVisible(false);
        stockSetBtn.setVisible(false);
        priceField.setVisible(false);
        priceButton.setVisible(false);
        menuIngredients.setVisible(false);
        setFieldsVisibility(true, true, true, true, true);
        String query = "SELECT * FROM ingredients;";
        populateVBoxWithQueryResults(query, true);

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

        selectedItems.clear();
        currStock.setText("");
        newItemText.setText("Create New Menu Item");
        nameTextField.setPromptText("Name");
        category = "menu_item";

        selectIngredients.setText("Select Ingredients");
        stockField.setVisible(false);
        stockSetBtn.setVisible(false);
        priceField.setVisible(false);
        priceButton.setVisible(false);
        menuIngredients.setVisible(true);


        String query = "SELECT * FROM menu_Item;";
        String ingQuery ="SELECT * FROM ingredients;";
        setFieldsVisibility(true, true, false, false, true);
        populateVBoxWithQueryResults(query, false);
        ingredientPane.setContent(tableItems);
        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(ingQuery)) {
            List<CheckBox> checkBoxes = new ArrayList<>();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                CheckBox checkBox = new CheckBox(name);
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        selectedItems.add(checkBox.getText());
                    } else {
                        selectedItems.remove(checkBox.getText());
                    }});
                
                checkBoxes.add(checkBox);
            }
            checkBoxes.sort(Comparator.comparing(CheckBox::getText));
            menuIngredVbox.getChildren().addAll(checkBoxes);
            menuIngredients.setContent(menuIngredVbox);

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

    //controls what fields are visable when drink category is open and populates the drink box
    // dumbest way possible to do this but didnt want to have to add another variable to function
    @FXML
    void btnActionInDrink(ActionEvent event){
        category = "drinks";
        newItemText.setText("Create New Drink Size");
        nameTextField.setPromptText("Size");


        stockField.setVisible(false);
        stockSetBtn.setVisible(false);
        priceField.setVisible(false);
        priceButton.setVisible(false);
        menuIngredients.setVisible(false);
        currStock.setText("");
        selectIngredients.setText("");

        setFieldsVisibility(true, true, false, false, true);
        tableItems.getChildren().clear();
        String query = "SELECT * FROM drinks;";
        populateVBoxWithQueryResults(query, false);

        }

    //ended up having to add another variable anyways
    //this functioon just populates the vbox for ingredients and menu. but can be more generalized if more tables added
    private void populateVBoxWithQueryResults(String query, Boolean ingredient) {
        // Clear the existing content in the VBox
        tableItems.getChildren().clear();

        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            List<RadioButton> radioButtons = new ArrayList<>();
            radioButtons.clear();
            // Iterate over the result set and populate the VBox with data
            while (resultSet.next()) {
                String name = "";
                Double price = 0.0;
                int stock =0;

                if(!"ingredients".equals(category)){
                    price = resultSet.getDouble("price"); 
                }else{
                    price = resultSet.getDouble("add_on_price");
                    stock = resultSet.getInt("stock");
                }


                if("drinks".equals(category)){
                    name = resultSet.getString("size");
                }else{
                    name = resultSet.getString("name");
                }

                RadioButton radioButton = new RadioButton("$"+price+ " " +name);
                radioButton.setToggleGroup(group);
                radioButton.getStyleClass().add("radioBtn");
                itemData data = new itemData(stock, name);
                radioButton.setUserData(data);
                radioButton.setOnAction(event -> {
                    if("ingredients".equals(category)){
                        priceButton.setVisible(true);
                        priceField.setVisible(true);
                        stockField.setVisible(true);
                        stockSetBtn.setVisible(true);
                        menuIngredients.setVisible(false);
                        itemData itemData = (itemData) radioButton.getUserData();
                        currStock.setText("Current Stock: " + itemData.getStock());
            
                    }else if("menu_item".equals(category)){
                        priceButton.setVisible(true);
                        priceField.setVisible(true);
                        stockField.setVisible(false);
                        stockSetBtn.setVisible(false);
                        menuIngredients.setVisible(false);
            
                    }
                    else{
                        priceButton.setVisible(true);
                        priceField.setVisible(true);
                        stockField.setVisible(false);
                        stockSetBtn.setVisible(false);
                        menuIngredients.setVisible(false);
                    }
                    newItemText.setText("");
                    selectIngredients.setText("");
                    setFieldsVisibility(false, false, false, false, false);

                });
                radioButtons.add(radioButton);
            }
            // Sort the radio buttons before adding them to the VBox

            radioButtons.sort(Comparator.comparing(radioButton -> ((itemData) radioButton.getUserData()).getName().toLowerCase()));
            tableItems.getChildren().addAll(radioButtons);
            ingredientPane.setContent(tableItems);
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
        priceField.clear();

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
            itemData itemData = (itemData) selectedRadioButton.getUserData();
            String itemName = itemData.getName();
            String priceText = priceField.getText();
            String priceName = "price";
            if (!priceText.isEmpty()) {
                try {
                    double newPrice = Double.parseDouble(priceText);
                    if(newPrice <= 0){
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("PRIVE ERROR");
                        alert.setHeaderText("FAILED TO UPDATE PRICE");
                        alert.setContentText("Ensure you entered a price that is more then 0 ");
                        alert.showAndWait();
                        return;
                    }
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
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("PRICE ERROR");
                            alert.setHeaderText("FAILED TO UPDATE PRICE");
                            alert.setContentText("Ensure you entered a price and that you are connected to the internet");
                            alert.showAndWait();
                            return;
                        }
                        refreshVBox();
                        clearFields();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("SQL ERROR");
                        alert.setHeaderText("Database not connected");
                        alert.setContentText("Enusure you are connected to internet and try again. If problem persists, contact support." + e);
                        alert.showAndWait();
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("PRICE ERROR");
                    alert.setHeaderText("INVAID PRICE FORMAT");
                    alert.setContentText("Ensure you are entering a number" + e);
                    alert.showAndWait();
                    return;
                }
            } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("PRICE ERROR");
                    alert.setHeaderText("NO PRICE ENTERED");
                    alert.setContentText("Ensure you are entering a price");
                    alert.showAndWait();
                    return;
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ITEM ERROR");
            alert.setHeaderText("NO ITEM SELECTED");
            alert.setContentText("Ensure you are selecting an item");
            alert.showAndWait();
            return;
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
                    itemData itemData = (itemData) selectedRadioButton.getUserData();
                    String itemName = itemData.getName();
                    int newStock = Integer.parseInt(stockField.getText());
                    if(newStock <= 0){
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("STOCK ERROR");
                        alert.setHeaderText("INVAID STOCK");
                        alert.setContentText("Ensure you are entering a stock that is more then 0 ");
                        alert.showAndWait();
                        return;
                    }
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
                        refreshVBox();
                        clearFields();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("SQL ERROR");
                        alert.setHeaderText("Database not connected");
                        alert.setContentText("Enusure you are connected to internet and try again. If problem persists, contact support." + e);
                        alert.showAndWait();
                        return;
                    }
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("PRICE ERROR");
                    alert.setHeaderText("INVAID PRICE FORMAT");
                    alert.setContentText("Ensure you are entering a number" + e);
                    alert.showAndWait();
                    return;
                }
            }else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("STOCK ERROR");
                alert.setHeaderText("INVAID STOCK");
                alert.setContentText("Ensure you are entering a stock");
                alert.showAndWait();
                return;
            }
        }else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ITEM ERROR");
            alert.setHeaderText("NO ITEM SELECTED");
            alert.setContentText("Ensure you are selecting an item");
            alert.showAndWait();
            return;
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
                if(newStock <= 0 || newPrice <= 0 || newMinStock <= 0){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("FORMAT ERROR");
                    alert.setHeaderText("INVALID FORMAT");
                    alert.setContentText("Ensure all numbers are above 0 and all fields are completed");
                    alert.showAndWait();
                    return;
                }
                statement = connection.prepareStatement("INSERT INTO ingredients (name, stock, add_on_price, min_stock) VALUES (?, ?, ?, ?)");
                statement.setString(1, nameText);
                statement.setInt(2, newStock);
                statement.setDouble(3, newPrice);
                statement.setInt(4, newMinStock);
                populateVBoxWithQueryResults("SELECT * FROM ingredients;", true);
                break;
            case "menu_item": 
                
                String[] ingredientsArray = selectedItems.toArray(new String[selectedItems.size()]);
                Array ingredients = connection.createArrayOf("TEXT", ingredientsArray);
                double menuItemPrice = Double.parseDouble(priceText);
                if(menuItemPrice <= 0 || ingredientsArray.length <= 0){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("FORMAT ERROR");
                    alert.setHeaderText("INVALID FORMAT");
                    alert.setContentText("Ensure all numbers are above 0 and all fields are completed");
                    alert.showAndWait();
                    return;
                }
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
                if(drinkPrice <= 0){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("FORMAT ERROR");
                    alert.setHeaderText("INVALID FORMAT");
                    alert.setContentText("Ensure all numbers are above 0 and all fields are completed");
                    alert.showAndWait();
                    return;
                }
                statement = connection.prepareStatement("INSERT INTO drinks (size, price) VALUES (?, ?)");
                statement.setString(1, size);
                statement.setDouble(2, drinkPrice);
                break;
            default:
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("CATEGORY ERROR");
                alert.setHeaderText("INVALID CATEGORY");
                alert.setContentText("THIS SHOULD BE IMPOSSIBLE");
                alert.showAndWait();
        }
        if(statement != null){
            rowsAffected = statement.executeUpdate();
        }
        if(fxnCalled){
            rowsAffected = menuFxn.executeUpdate();
        }
        if (rowsAffected > 0 || fxnCalled) {
            // Alert alert = new Alert(AlertType.ERROR);
            // alert.setTitle("ITEM ERROR");
            // alert.setHeaderText("FAILED TO ADD ITEM");
            // alert.setContentText("Ensure you entered all fields and that you are connected to the internet");
            // alert.showAndWait();
            // return;
        } else {
            System.out.println("Failed to add item.");
        }
        refreshVBox();
        clearFields();
    } catch (SQLException e) {
        e.printStackTrace();
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("SQL ERROR");
        alert.setHeaderText("Database not connected");
        alert.setContentText("Enusure you are connected to internet and try again. If problem persists, contact support." + e);
        alert.showAndWait();
        return;
    } catch (NumberFormatException e) {
        e.printStackTrace();
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("FIELD ERROR");
        alert.setHeaderText("INVAID FORMAT");
        alert.setContentText("Enusure all fields are completed" + e);
        alert.showAndWait();
        return;
    }
}

private void refreshVBox() {
    switch (category) {
        case "ingredients":
            populateVBoxWithQueryResults("SELECT * FROM ingredients;", true);
            break;
        case "menu_item":
            populateVBoxWithQueryResults("SELECT * FROM menu_Item;", false);
            break;
        case "drinks":
            populateVBoxWithQueryResults("SELECT * FROM drinks;", false);
            break;
        default:
            System.out.println("Invalid category");
        
    }

    priceButton.setVisible(false);
    priceField.setVisible(false);
    stockField.setVisible(false);
    stockSetBtn.setVisible(false);
    menuIngredients.setVisible(false);
    setFieldsVisibility(false, false, false, false, false);
    clearFields();

}


//general initialize. most likley wont use here
@Override
public void initialize(URL url, ResourceBundle resourceBundle) {

}

}
