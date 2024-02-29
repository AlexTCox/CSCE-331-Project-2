package com.example.loginpage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
        checkLogin();
    }
    public void checkLogin() throws IOException
    {
        Main m = new Main();
        if(name.getText().toString().equals("raghav") && passkey.getText().toString().equals("123"))
        {
              wronglogin.setText("Success!");
              m.changeScene("afterLogin.fxml");
        }
        else if(name.getText().isEmpty() && passkey.getText().isEmpty())
        {
            wronglogin.setText("Please enter your data.");
        }else
        {
            wronglogin.setText("Wrong username or password!");
        }

    }
}
