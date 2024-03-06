package com.example;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// import static com.example.demo.Database.Query;

public class Waiter implements Initializable{

    @FXML
    private VBox hboxx_menu;
    private int i = 0;
    private int orderID;
    private int orderIDTemp;
    private int operatorID = 2;
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    List<Double> priceList = new ArrayList<>();
    List<String> menuList = new ArrayList<>();
    List<String> drinkList = new ArrayList<>();
    List<String> addonList = new ArrayList<>();
    CallableStatement compOrder;

    /**
    *The Menu_Query function gets every menu item that is in the database and turns them 
    *into buttons which can then be pressed on the GUI. It will get called
    *at the beginning and whenever a change happens to a menu item such as
    *price change and add a new button if a new menu item is made.
    *@param e Not used but it is available if we want to make the query into a button
    *@return void Does not return anything but should populate a V Box with the items as buttons
    *@throws SQLException If database communicatino isn't done correctly
    */
    public void Menu_Query(ActionEvent e) throws SQLException {

        String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

        String query = "SELECT * FROM menu_Item";
        List<Button> buttonlist = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) { //iterate over every row returned
                String menuItemName = resultSet.getString("name");
                double menuItemPrice = Double.parseDouble(resultSet.getString("price"));
                Button button = new Button(menuItemName);
                button.getStyleClass().add("buttons");
                buttonlist.add(button);
                buttonlist.get(i).setOnAction(e1 -> {
                    price += menuItemPrice;
                    priceList.add(menuItemPrice);
                    menuList.add(menuItemName);
                    myLabel.setText("Order: $" + price);
                    myListView.getItems().add(index, menuItemName+ " $" + menuItemPrice);
                    index += 1;
                });
                i += 1;
            }
            hboxx_menu.getChildren().clear();
            hboxx_menu.getChildren().addAll(buttonlist);
            i = 0;

        } catch (SQLException ex) {
                ex.printStackTrace();
        } finally {
                resultSet.close();
                statement.close();
                connection.close();
        }
    }

    public void Addon_Query(ActionEvent e) throws SQLException {

        String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

        String query = "SELECT * FROM ingredients";
        List<Button> buttonlist = new ArrayList<>();
        //             PreparedStatement pst = con.prepareStatement(query))

        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) { //iterate over every row returned
                String addonName = resultSet.getString("name");
                double addonPrice = Double.parseDouble(resultSet.getString("add_on_price"));
                Button button = new Button(addonName);
                button.getStyleClass().add("buttons");
                buttonlist.add(button);
                buttonlist.get(i).setOnAction(e1 -> {
                    price += addonPrice;
                    priceList.add(addonPrice);
                    addonList.add(addonName);
                    myLabel.setText("Order: $" + price);
                    myListView.getItems().add(index, "+ " + addonName + " $" + addonPrice);
                    index += 1;
                });
                i += 1;
            }
            hboxx_menu.getChildren().clear();
            hboxx_menu.getChildren().addAll(buttonlist);
            i = 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
    }

/**
 * When the drinks category is selected the Vbox and scroll pane element are popoulated.
 * The action event argument is in place for compatablity with javaFX controllers.
 * When the Vbox is populated it is populated with javaFX buttons that add to the order.
 * 
 * <p>
 * This method executes on button press every time. Even if the Vbox is already populated
 * 
 * @param e An action event not defined by the user. Only used to run on button press.
 * @return void
 * @throws SQLException if not connected to database
*/
    public void Drink_Query(ActionEvent e) throws SQLException {

        String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

        String query = "SELECT * FROM drinks";
        List<Button> buttonlist = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) { //iterate over every row returned
                String drinkName = resultSet.getString("size");
                double drinkPrice = Double.parseDouble(resultSet.getString("price"));
                Button button = new Button(drinkName);
                button.getStyleClass().add("buttons");
                buttonlist.add(button);
                buttonlist.get(i).setOnAction(e1 -> {
                    price += drinkPrice;
                    priceList.add(drinkPrice);
                    drinkList.add(drinkName);
                    myLabel.setText("Order: $" + price);
                    myListView.getItems().add(index, "+ " + drinkName + " $" + drinkPrice);
                    index += 1;
                });
                i += 1;
            }
            hboxx_menu.getChildren().clear();
            hboxx_menu.getChildren().addAll(buttonlist);
            i = 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
    }

    /**
    *Submits a new order to the database, clearing the current order data from the GUI upon successful execution.
    *This method first establishes a connection to the database using provided credentials, retrieves a new order ID by executing a SQL query,
    *and then submits order details including menu items, drinks, and add-ons. Finally, it clears the current order data from the interface.
    *@param e the ActionEvent triggered by submitting the order, not used in the method but required for interface compatibility
    *@throws SQLException if there is any issue with the database connection, executing queries, or other SQL-related operations.
    *This includes issues such as invalid credentials, unreachable database server, malformed SQL queries, and failure in executing the * prepared statements.
    *This method directly affects the state of the database and the GUI, and it handles all SQLExceptions by printing stack trace.
    *Proper exception handling and user feedback mechanisms should be implemented in a production environment.
    *@return is void.
    */
    public void SubmitOrder(ActionEvent e) throws SQLException {

        String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

        String query = "SELECT NEW_ORDER(" + operatorID + ")";
        List<Integer> orderIDList = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                orderID = resultSet.getInt("new_order");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }


        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

            String[] preMenuArray = new String[menuList.size()];
            for (int j = 0; j < menuList.size(); j++) {
                preMenuArray[j] = menuList.get(j);
            }
            Array menuArray = connection.createArrayOf("TEXT", preMenuArray);

            String[] preDrinkArray = new String[drinkList.size()];
            for (int j = 0; j < drinkList.size(); j++) {
                preDrinkArray[j] = drinkList.get(j);
            }
            Array drinkArray = connection.createArrayOf("TEXT", preDrinkArray);

            String[] preAddonArray = new String[addonList.size()];
            for (int j = 0; j < addonList.size(); j++) {
                preAddonArray[j] = addonList.get(j);
            }
            Array addonArray = connection.createArrayOf("TEXT", preAddonArray);

            compOrder = connection.prepareCall("SELECT complete_order(?, ?, ?, ?)");
            compOrder.setInt(1, orderID);
            compOrder.setArray(2, menuArray);
            compOrder.setArray(3, drinkArray);
            compOrder.setArray(4, addonArray);

            compOrder.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }

        myListView.getItems().clear();
        priceList.clear();
        menuList.clear();
        addonList.clear();
        drinkList.clear();

        price = 0;
        myLabel.setText("Order: $" + price);

        index = 0;
    }

    /**
     * Removes the last item from the list and updates the order total.
     * @param  e  The ActionEvent that triggers the removal of the last item
     * @returns void
     */
    public void RemoveLast(ActionEvent e) {
        if (index != 0) {
            int listSize = myListView.getItems().size(); // Get size of list
            myListView.getItems().remove(listSize - 1); // Remove last item on list

            price -= priceList.get(index - 1); // Subtract price of last menu item
            myLabel.setText("Order: $" + price); // Update order total

            priceList.remove(index - 1); // Remove price from array
            index -= 1; // Index for list goes down by one
        }
    }

    /**
     * Clears the entire order by removing all items from the lists
     * and resetting the order total to zero.
     *
     * @param e The ActionEvent that triggers the clearing of the order.
     * @returns void
     */
    public void ClearOrder(ActionEvent e) {
        if (index != 0) {
            myListView.getItems().clear();
            priceList.clear();
            menuList.clear();
            addonList.clear();
            drinkList.clear();

            price = 0;
            myLabel.setText("Order: $" + price);

            index = 0;
        }
    }

    @FXML
    private Label myLabel;
    @FXML
    private Label myLabel1;

    @FXML
    private ListView<String> myListView;
    private double price = 0;
    private int index = 0;
    
    /**
     * Changes to manager page when the logout button is pressed
     * 
     * @param event
     * @throws IOException if FXML is not in the resources folder or found in project
     * @return void
     */

    @FXML
    public void logoutBtnAction(ActionEvent event){
        try{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));            
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
        scene.getStylesheets().add("application.css");
        stage.show();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private BorderPane mainView;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

        String query = "SELECT * FROM menu_Item";
        List<Button> buttonlist = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) { //iterate over every row returned
                String menuItemName = resultSet.getString("name");
                double menuItemPrice = Double.parseDouble(resultSet.getString("price"));
                Button button = new Button(menuItemName);
                button.getStyleClass().add("buttons");
                buttonlist.add(button);
                buttonlist.get(i).setOnAction(e1 -> {
                    price += menuItemPrice;
                    priceList.add(menuItemPrice);
                    menuList.add(menuItemName);
                    myLabel.setText("Order: $" + price);
                    myListView.getItems().add(index, menuItemName+ " $" + menuItemPrice);
                    index += 1;
                });
                i += 1;
            }
            hboxx_menu.getChildren().clear();
            hboxx_menu.getChildren().addAll(buttonlist);
            i = 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}