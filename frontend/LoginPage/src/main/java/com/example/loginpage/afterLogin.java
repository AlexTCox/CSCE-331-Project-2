package com.example.loginpage;

import javafx.event.ActionEvent;
import java.io.IOException;
public class afterLogin {

    public void userlogout(ActionEvent event) throws IOException
    {
        Main m = new Main();
        m.changeScene("login.fxml");
    }

}
