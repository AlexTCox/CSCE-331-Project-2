package com.example.loginpage;

import javafx.event.ActionEvent;

import java.io.IOException;

// Class responsible for handling Manager actions after login
public class AdminAfterLogin
{

    // Method to handle manager logout action and adminlogout on-action button name
    public void adminlogout(ActionEvent event) throws IOException
    {
        // Create an instance of the Main class
        Main m = new Main();

        // Call the changeuserScene method of the Main class to switch to the login scene
        m.changeuserScene("login.fxml");
    }
}
