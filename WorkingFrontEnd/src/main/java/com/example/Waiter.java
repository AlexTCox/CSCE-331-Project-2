package com.example;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

    public void Menu_Query(ActionEvent e) throws SQLException {

        String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

        String query = "SELECT * FROM menu_Item";
        List<Button> buttonlist = new ArrayList<>();

        // (Connection con = DriverManager.getConnection(url, user, password);
        //             PreparedStatement pst = con.prepareStatement(query))

        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) { //iterate over every row returned
                String menuItemName = resultSet.getString("name");
                double menuItemPrice = Double.parseDouble(resultSet.getString("price"));
                buttonlist.add(new Button(menuItemName));
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

        // (Connection con = DriverManager.getConnection(url, user, password);
        //             PreparedStatement pst = con.prepareStatement(query))

        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) { //iterate over every row returned
                String addonName = resultSet.getString("name");
                double addonPrice = Double.parseDouble(resultSet.getString("add_on_price"));
                buttonlist.add(new Button(addonName));
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

    public void Drink_Query(ActionEvent e) throws SQLException {

        String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

        String query = "SELECT * FROM drinks";
        List<Button> buttonlist = new ArrayList<>();

        // (Connection con = DriverManager.getConnection(url, user, password);
        //             PreparedStatement pst = con.prepareStatement(query))

        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) { //iterate over every row returned
                String drinkName = resultSet.getString("size");
                double drinkPrice = Double.parseDouble(resultSet.getString("price"));
                buttonlist.add(new Button(drinkName));
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

        myListView.getItems().clear();
        priceList.clear();
        menuList.clear();
        addonList.clear();
        drinkList.clear();

        price = 0;
        myLabel.setText("Order: $" + price);

        index = 0;


        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

            String[] preMenuArray = new String[menuList.size()];
            menuList.toArray(preMenuArray);
            Array menuArray = connection.createArrayOf("TEXT", preMenuArray);

            String[] preDrinkArray = new String[drinkList.size()];
            drinkList.toArray(preDrinkArray);
            Array drinkArray = connection.createArrayOf("TEXT", preDrinkArray);

            String[] preAddonArray = new String[addonList.size()];
            addonList.toArray(preAddonArray);
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
    }


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
    public void showOrderID(ActionEvent e) {
        myLabel1.setText("ID: " + orderID);
    }


    @FXML
    private ListView<String> myListView;
    private double price = 0;
    private int index = 0;

    @FXML
    private Circle myCircle;
    private double x;
    private double y;

    public void add(ActionEvent e) {
        price += 2;
        myLabel.setText("Order: $" + price);
        myListView.getItems().add(index, "Pizza $2.00");
        index += 1;
    }

    public void pizza(ActionEvent e) {
        price += 2;
        myLabel.setText("Order: $" + price);
        myListView.getItems().add(index, "Pizza $2.00");
        index += 1;
    }

    public void burger(ActionEvent e) {
        price += 4;
        myLabel.setText("Order: $" + price);
        myListView.getItems().add(index, "Pizza $4.00");
        index += 1;
    }
    public void down(ActionEvent e) {
        myCircle.setCenterY(y += 10);
    }
    public void left(ActionEvent e) {
        myCircle.setCenterX(x -= 10);
    }
    public void right(ActionEvent e) {
        myCircle.setCenterX(x += 10);
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    // public void switchToScene1(ActionEvent event) throws IOException {
    //     root = FXMLLoader.load(getClass().getResource("view_A.fxml"));
    //     stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    //     scene = new Scene(root);
    //     stage.setScene(scene);
    //     stage.show();
    // }

    // public void switchToScene2(ActionEvent event) throws IOException {
    //     root = FXMLLoader.load(getClass().getResource("view_B.fxml"));
    //     stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    //     scene = new Scene(root);
    //     stage.setScene(scene);
    //     stage.show();
    // }

    public void table(ActionEvent e) {
    }

    @FXML
    private BorderPane mainView;

    @FXML
    private void handleChangeView(ActionEvent event) {
        try {
            String menuItemID = ((Button) event.getSource()).getId();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(menuItemID + ".fxml"));
            loader.setController(this);

            mainView.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                buttonlist.add(new Button(menuItemName));
                buttonlist.get(i).setOnAction(e1 -> {
                    price += menuItemPrice;
                    priceList.add(menuItemPrice);
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