package com.example.loginpage;

import javafx.event.ActionEvent;

import java.io.IOException;

public class AdminAfterLogin
{
    public void adminlogout(ActionEvent event) throws IOException
    {
        Main m = new Main();
        m.changeuserScene("login.fxml");
    }
}
