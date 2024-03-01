package com.example.loginpage;
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
    private Button button;
    @FXML
    private Label wronglogin;
    @FXML
    private TextField name;
    @FXML
    private PasswordField passkey;

    public void userLogIn(ActionEvent event) throws IOException
    {

        String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";
        String user_name = name.getText();
        String user_spassword = passkey.getText();
        int user_password =  user_spassword.hashCode();
        Boolean user_result = false;

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            // first ? is for return value
            String user_query =  "{ ? = call check_pin(?, ?) }";
            //Preparing a CallableStatement to call a function
            CallableStatement user_cstmt = connection.prepareCall(user_query);
            //Registering the out parameter of the function (return type)
            user_cstmt.registerOutParameter(1, Types.BOOLEAN);
            //Setting the input parameters of the function
            user_cstmt.setString(2, user_name);
            user_cstmt.setInt(3, user_password);
            user_cstmt.execute();
            user_result = user_cstmt.getBoolean(1);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        Main m = new Main();
        Boolean admin_result = false;

         if(user_result){
             wronglogin.setText("Success!");
             try
             {
                 Connection connection = DriverManager.getConnection(url, user, password);
                 // first ? is for return value
                 String admin_query = "{ ? = call check_admin(?) }";
                 CallableStatement admin_cstmt = connection.prepareCall(admin_query);
                 admin_cstmt.registerOutParameter(1, Types.BOOLEAN);
                 // Setting the input parameters of the function
                 admin_cstmt.setString(2, user_name);
                 admin_cstmt.execute();
                 admin_result = admin_cstmt.getBoolean(1);
             }
             catch (SQLException ex)
             {
                 ex.printStackTrace();
             }
             if(admin_result)
             {
                 wronglogin.setText("Manager");
                 m.changeuserScene("AdminafterLogin.fxml");
             }else
             {
                 wronglogin.setText("Server");
                 m.changeuserScene("ServerafterLogin.fxml");
             }

         }else
         {
             wronglogin.setText("Wrong username or password!");
         }
    }
}
