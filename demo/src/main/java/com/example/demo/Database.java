package com.example.demo;

import javafx.application.Application;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database{

    public static void Query() {

        String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_550_01_db";
        String user = "csce331_550_01_user";
        String password = "cSCUE8w9";

        String query = "SELECT * FROM menu_Item;";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.executeUpdate();

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(Application.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
