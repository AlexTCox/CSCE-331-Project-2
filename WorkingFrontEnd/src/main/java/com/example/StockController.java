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
    private Button waiterBtn;
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

    //class to hold the data for the radio buttons because the name is price + name
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

    /**
     * Changes the scene to the primary scene.
     * Loads the primary scene FXML file and sets it as the current scene.
     * 
     * @param event An ActionEvent object representing the event of the button press.
     * @throws IOException if there is an error loading the FXML file
     */
    @FXML
    public void changeScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Manager");
        scene.getStylesheets().add("application.css");
        stage.show();
    }

    /**
     * Handles the button action for the ingredients category.
     * Sets the category to ingredients and adjusts visibility of UI elements accordingly.
     * Populates the VBox with ingredients data retrieved from the database.
     * 
     * @param event An ActionEvent object representing the event of the button press.
     */
    @FXML
    void btnActionIngredients(ActionEvent event){

        //set the category to ingredients and set text of text fields
        category = "ingredients";
        newItemText.setText("Create New Ingredient");
        nameTextField.setPromptText("Name");
        selectIngredients.setText("");

        //set all the fields to be visible or not
        stockField.setVisible(false);
        stockSetBtn.setVisible(false);
        priceField.setVisible(false);
        priceButton.setVisible(false);
        menuIngredients.setVisible(false);
        setFieldsVisibility(true, true, true, true, true);

        //populate the vbox with the ingredients
        String query = "SELECT * FROM ingredients;";
        populateVBoxWithQueryResults(query);

    }

    /**
     * Handles the button action for the menu category.
     * Sets the category to menu_item and adjusts visibility of UI elements accordingly.
     * Populates the VBox with menu item data retrieved from the database.
     * 
     * @param event An ActionEvent object representing the event of the button press.
     */
    @FXML
    void btnActionInMenu(ActionEvent event){

        //clear the selected items and set the text of the text fields
        selectedItems.clear();
        currStock.setText("");
        newItemText.setText("Create New Menu Item");
        nameTextField.setPromptText("Name");
        category = "menu_item";
        selectIngredients.setText("Select Ingredients");

        //set the fields to be visible or not
        stockField.setVisible(false);
        stockSetBtn.setVisible(false);
        priceField.setVisible(false);
        priceButton.setVisible(false);
        menuIngredients.setVisible(true);
        setFieldsVisibility(true, true, false, false, true);

        //quieries
        String query = "SELECT * FROM menu_Item;";
        String ingQuery ="SELECT * FROM ingredients;";

        populateVBoxWithQueryResults(query);
        
        //this populates the table that manager selects from for the ingredients for the menu item
        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(ingQuery)) {

            List<CheckBox> checkBoxes = new ArrayList<>();

            // Iterate over the result set and populate the VBox with data
            while (resultSet.next()) {

                String name = resultSet.getString("name");
                CheckBox checkBox = new CheckBox(name);

                // Add a listener to the check box to add or remove the selected item from the list
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        selectedItems.add(checkBox.getText());
                    } else {
                        selectedItems.remove(checkBox.getText());
                    }});
                
                checkBoxes.add(checkBox);
            }

            // Sort the check boxes before adding them to the VBox
            checkBoxes.sort(Comparator.comparing(CheckBox::getText));
            menuIngredVbox.getChildren().addAll(checkBoxes);
            menuIngredients.setContent(menuIngredVbox);


        //catch sql errors and display an alert
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

    /**
     * Handles the button action for the drinks category.
     * Sets the category to drinks and adjusts visibility of UI elements accordingly.
     * Populates the VBox with drink data retrieved from the database.
     * 
     * @param event An ActionEvent object representing the event of the button press.
     */
    @FXML
    void btnActionInDrink(ActionEvent event){

        category = "drinks";
        newItemText.setText("Create New Drink Size");
        nameTextField.setPromptText("Size");
        currStock.setText("");
        selectIngredients.setText("");

        //set the fields to be visible or not
        stockField.setVisible(false);
        stockSetBtn.setVisible(false);
        priceField.setVisible(false);
        priceButton.setVisible(false);
        menuIngredients.setVisible(false);

        setFieldsVisibility(true, true, false, false, true);

        tableItems.getChildren().clear();
        String query = "SELECT * FROM drinks;";
        populateVBoxWithQueryResults(query);

        }

    /**
     * Populates the VBox with query results based on the given SQL query.
     * Clears the existing content in the VBox and populates it with data retrieved from the database.
     * 
     * @param query The SQL query to retrieve data from the database.
     */
    private void populateVBoxWithQueryResults(String query) {
        // Clear the existing content in the VBox
        tableItems.getChildren().clear();

        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            // Create a list to hold the radio buttons
            List<RadioButton> radioButtons = new ArrayList<>();
            radioButtons.clear();

            // Iterate over the result set and populate the VBox with data
            while (resultSet.next()) {

                String name = "";
                Double price = 0.0;
                int stock =0;

                // Get the price of the item
                if(!"ingredients".equals(category)){
                    price = resultSet.getDouble("price"); 
                }else{
                    price = resultSet.getDouble("add_on_price");
                    stock = resultSet.getInt("stock");
                }

                // Get the name of the item
                if("drinks".equals(category)){
                    name = resultSet.getString("size");
                }else{
                    name = resultSet.getString("name");
                }

                // Create a radio button for the item
                RadioButton radioButton = new RadioButton("$"+price+ " " +name);
                radioButton.setToggleGroup(group);
                radioButton.getStyleClass().add("radioBtn");
                itemData data = new itemData(stock, name);
                radioButton.setUserData(data);

                // Add a listener to the radio button to display the stock of the selected item
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
                        currStock.setText("");
            
                    }
                    else{
                        priceButton.setVisible(true);
                        priceField.setVisible(true);
                        stockField.setVisible(false);
                        stockSetBtn.setVisible(false);
                        menuIngredients.setVisible(false);
                        currStock.setText("");
                    }

                    //clear text fields
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

        //cat sql errors and show alert
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
    /**
     * Sets the visibility of various UI elements.
     * Controls which UI elements are visible based on the parameters.
     * 
     * @param nameVisible     Visibility of the nameTextField.
     * @param priceVisible    Visibility of the priceTextField.
     * @param newStockVisible Visibility of the newStockTextField.
     * @param minStockVisible Visibility of the minStockTextField.
     * @param submitVisible   Visibility of the submitItemBtn.
     */
    private void setFieldsVisibility(boolean nameVisible, boolean priceVisible, boolean newStockVisible,
                                     boolean minStockVisible, boolean submitVisible) {
        nameTextField.setVisible(nameVisible);
        priceTextField.setVisible(priceVisible);
        newStockTextField.setVisible(newStockVisible);
        minStockTextField.setVisible(minStockVisible);
        submitItemBtn.setVisible(submitVisible);
    }

    
    /**
     * Clears all input fields.
     * Resets the text content of all input fields to empty strings.
     */
    private void clearFields() {
        nameTextField.clear();
        priceTextField.clear();
        newStockTextField.clear();
        minStockTextField.clear();
        stockField.clear();
        priceField.clear();

    }

    /**
     * Retrieves the selected radio button from the VBox.
     * Iterates through the children of the VBox to find the selected radio button.
     * 
     * @return The selected RadioButton object, or null if none is selected.
     */
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

    /**
     * Handles the action when the price button is clicked.
     * Updates the price of the selected item in the database based on user input.
     * 
     * @param event An ActionEvent object representing the event of the button press.
     */
    @FXML
    void priceButtonAction(ActionEvent event) {

        // Get the selected menu item from the radio buttons
        RadioButton selectedRadioButton = getSelectedRadioButton();
        
        // Check if a button is selected
        if (selectedRadioButton != null) {

            itemData itemData = (itemData) selectedRadioButton.getUserData();
            String itemName = itemData.getName();
            String priceText = priceField.getText();
            String priceName = "price";

            // Check if the price field is empty
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

                        // Check if the price was updated successfully
                        if (rowsAffected > 0) {
                            currStock.setText("New price for" + itemName + " is $" +newPrice);
                        } else {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("PRICE ERROR");
                            alert.setHeaderText("FAILED TO UPDATE PRICE");
                            alert.setContentText("Ensure you entered a price and that you are connected to the internet");
                            alert.showAndWait();
                            return;
                        }

                        // Refresh the VBox and clear the fields
                        refreshVBox();
                        clearFields();

                    // Catch SQL errors and display an alert
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("SQL ERROR");
                        alert.setHeaderText("Database not connected");
                        alert.setContentText("Enusure you are connected to internet and try again. If problem persists, contact support." + e);
                        alert.showAndWait();
                        return;
                    }

                // Catch number format errors and display an alert
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("PRICE ERROR");
                    alert.setHeaderText("INVAID PRICE FORMAT");
                    alert.setContentText("Ensure you are entering a number" + e);
                    alert.showAndWait();
                    return;
                }
            
            // Display an alert if the price field is empty
            } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("PRICE ERROR");
                    alert.setHeaderText("NO PRICE ENTERED");
                    alert.setContentText("Ensure you are entering a price");
                    alert.showAndWait();
                    return;
            }

        // Display an alert if no item is selected
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ITEM ERROR");
            alert.setHeaderText("NO ITEM SELECTED");
            alert.setContentText("Ensure you are selecting an item");
            alert.showAndWait();
            return;
        }
    }

    /**
     * Handles the action when the stock button is clicked.
     * Updates the stock of the selected item in the database based on user input.
     * 
     * @param event An ActionEvent object representing the event of the button press.
     */
    @FXML
    void stockBtnAction(ActionEvent event){
        RadioButton selectedRadioButton =getSelectedRadioButton();
        String stockText = stockField.getText();

        // Check if a button is selected
        if(selectedRadioButton != null){

            // Check if the stock field is empty
            if(!stockText.isEmpty()){

                try{

                    itemData itemData = (itemData) selectedRadioButton.getUserData();
                    String itemName = itemData.getName();
                    int newStock = Integer.parseInt(stockField.getText());

                    // Check if the stock is more than 0
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

                        // Check if the stock was updated successfully
                        if (rowsAffected > 0) {
                            currStock.setText("New stock for" + itemName + " is " + newStock + " Units");
                        } else {
                            System.out.println("Failed to update stock.");
                        }

                        // Refresh the VBox and clear the fields
                        refreshVBox();
                        clearFields();

                    // catch sql errors and show alert
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("SQL ERROR");
                        alert.setHeaderText("Database not connected");
                        alert.setContentText("Enusure you are connected to internet and try again. If problem persists, contact support." + e);
                        alert.showAndWait();
                        return;
                    }

                // Catch number format errors and display an alert
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("PRICE ERROR");
                    alert.setHeaderText("INVAID PRICE FORMAT");
                    alert.setContentText("Ensure you are entering a number" + e);
                    alert.showAndWait();
                    return;
                }

            // Display an alert if the stock field is empty
            }else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("STOCK ERROR");
                alert.setHeaderText("INVAID STOCK");
                alert.setContentText("Ensure you are entering a stock");
                alert.showAndWait();
                return;
            }

        // Display an alert if no item is selected
        }else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ITEM ERROR");
            alert.setHeaderText("NO ITEM SELECTED");
            alert.setContentText("Ensure you are selecting an item");
            alert.showAndWait();
            return;
        }
    }

    /**
     * Handles the action when the new item button is clicked.
     * Adds a new item to the database based on user input.
     * 
     * @param event An ActionEvent object representing the event of the button press.
     */
    @FXML
    void newItemBtn(ActionEvent event){

        // Get the text from the text fields
        String stockText = newStockTextField.getText();
        String nameText = nameTextField.getText();
        String priceText = priceTextField.getText();
        String minStockText = minStockTextField.getText();
        int rowsAffected = 0;

        //bool to check if a function was called and not just a query
        Boolean fxnCalled = false;
    

    try(Connection connection = DriverManager.getConnection(dbUrl, user, password)) {
        CallableStatement menuFxn = null;
        PreparedStatement statement =null;

        switch (category) {

            case "ingredients":
                
                int newStock = Integer.parseInt(stockText);
                double newPrice = Double.parseDouble(priceText);
                int newMinStock = Integer.parseInt(minStockText);

                // Check if the stock, price, and min stock are more than 0
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
                currStock.setText(nameText + " Added");
                

                // populateVBoxWithQueryResults("SELECT * FROM ingredients;");
                break;
            case "menu_item": 
                
                String[] ingredientsArray = selectedItems.toArray(new String[selectedItems.size()]);
                Array ingredients = connection.createArrayOf("TEXT", ingredientsArray);
                double menuItemPrice = Double.parseDouble(priceText);

                // Check if the price and ingredients are more than 0
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
                fxnCalled = true;
                currStock.setText(nameText + " Added");
                

                break;

            case "drinks":
                
                String size = nameText;
                double drinkPrice = Double.parseDouble(priceText);

                // Check if the price is more than 0
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
                currStock.setText(size + " Added");
                break;

            default:
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("CATEGORY ERROR");
                alert.setHeaderText("INVALID CATEGORY");
                alert.setContentText("THIS SHOULD BE IMPOSSIBLE");
                alert.showAndWait();
                
        }

        // Execute the statement and get the number of rows affected or if fxn was called act accordingly
        if(statement != null){
            rowsAffected = statement.executeUpdate();
        }
        if(fxnCalled){
            rowsAffected = menuFxn.executeUpdate();
            fxnCalled =menuFxn.getBoolean(1);
        }
        if (rowsAffected > 0 || fxnCalled) {

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ITEM ERROR");
            alert.setHeaderText("FAILED TO ADD ITEM");
            alert.setContentText("Ensure you entered all fields and the item is unique");
            alert.showAndWait();
            return;
        }
        
        for(Node node :menuIngredVbox.getChildren()){
            if(node instanceof CheckBox){
                ((CheckBox)node).setSelected(false);
            }
        }
        refreshVBox();
        clearFields();

    //catch sql errors and show alert
    } catch (SQLException e) {

        e.printStackTrace();
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("SQL ERROR");
        alert.setHeaderText("Database not connected");
        alert.setContentText("Enusure you are connected to internet and try again. If problem persists, contact support." + e);
        alert.showAndWait();
        return;

    } 

    //catch number format errors and show alert
    catch (NumberFormatException e) {
        e.printStackTrace();
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("FIELD ERROR");
        alert.setHeaderText("INVAID FORMAT");
        alert.setContentText("Enusure all fields are completed" + e);
        alert.showAndWait();
        return;
    }
}

    /**
     * Refreshes the VBox to update information.
     * Refreshes the content of the VBox based on the current category.
     */
    private void refreshVBox() {
        switch (category) {
            case "ingredients":
                populateVBoxWithQueryResults("SELECT * FROM ingredients;");
                break;
            case "menu_item":
                populateVBoxWithQueryResults("SELECT * FROM menu_Item;");
                break;
            case "drinks":
                populateVBoxWithQueryResults("SELECT * FROM drinks;");
                break;
            default:
                System.out.println("Invalid category");
            
        }

        clearFields();
    }



//general initialize. most likley wont use here
@Override
public void initialize(URL url, ResourceBundle resourceBundle) {

}

}