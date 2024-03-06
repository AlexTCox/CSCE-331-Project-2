package com.example;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.*;
import java.io.IOException;
public class LogIn {
    public LogIn()
    {

    }
    @FXML
    private Label wronglogin;
    @FXML
    private TextField name;
    @FXML
    private PasswordField passkey;

    /**
     * Performs user login authentication based on the provided username and password.
     * 
     * @param event An ActionEvent object representing the event of the button press.
     * @throws IOException if there is an error loading the FXML file
     */
    public void userLogIn(ActionEvent event) throws IOException
    {
        // Storing JDBC URL, username, and password of SQL server
        String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

        // Storing user_name get from the user in GUI
        String user_name = name.getText();
        String user_spassword = passkey.getText();

        // Retrieving the username and password entered by the user from GUI elements
        int user_password =  user_spassword.hashCode();

        // Initializing a boolean variable to track user authentication result
        Boolean user_result = false;

        // Initializing a boolean variable to track adim authentication result
        Boolean admin_result = false;

        try {
            Connection connection = DriverManager.getConnection(url, user, password);

            // Defining a SQL query to call a stored function with an output parameter
            String user_query =  "{ ? = call check_pin(?, ?) }";
            //Preparing a CallableStatement to call a function
            CallableStatement user_cstmt = connection.prepareCall(user_query);
            //Registering the out parameter of the function (return type)
            user_cstmt.registerOutParameter(1, Types.BOOLEAN);
            //Setting the input parameters of the function
            user_cstmt.setString(2, user_name);
            user_cstmt.setInt(3, user_password);
            user_cstmt.execute();
            // Retrieving the result from the output parameter
            user_result = user_cstmt.getBoolean(1);

            // Defining a SQL query to call a stored function with an output parameter
            String admin_query = "{ ? = call check_admin(?) }";
            CallableStatement admin_cstmt = connection.prepareCall(admin_query);
            admin_cstmt.registerOutParameter(1, Types.BOOLEAN);
            // Setting the input parameters of the function
            admin_cstmt.setString(2, user_name);
            admin_cstmt.execute();
            // Retrieving the result from the output parameter
            admin_result = admin_cstmt.getBoolean(1);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        // Creating an instance of the Main class
        App m = new App();

        // Checking if user authentication was successful
         if(user_result){
             wronglogin.setText("Success!");

             // Checking if the user is an admin
             if(admin_result)
             {
                 wronglogin.setText("Manager");

                 // Redirecting to the admin dashboard scene
                 m.changeuserScene("primary.fxml");
             }else
             {
                 wronglogin.setText("Server");
                 // Redirecting to the server dashboard scene
                 m.changeuserScene("WaiterView.fxml");
                 
             }

         }else
         {
             // Displaying error message for wrong username or password
             wronglogin.setText("Wrong username or password!");
         }
    }
}
