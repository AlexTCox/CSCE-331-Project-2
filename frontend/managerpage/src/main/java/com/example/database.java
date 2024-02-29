package com.example;

import java.sql.*;

public class database {


  public static void main(String args[]) {

    //Building the connection with your credentials
    Connection conn = null;
    String dbName = "csce331_550_01_db";
    String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
    String user = "csce331_550_01_user";
    String pswd = "cSCUE8w9";
    //Connecting to the database
    try {
        conn = DriverManager.getConnection(dbConnectionString, user, pswd);
     } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
     }

     System.out.println("Opened database successfully");

     try{
       //create a statement object
    Statement stmt = conn.createStatement();

    //Running a query
    //TODO: update the sql command here
    String sqlStatement = "SELECT name, stock FROM ingredients";

    ResultSet result = stmt.executeQuery(sqlStatement);
       
       System.out.println("--------------------Query Results--------------------");
       while (result.next()) {
        System.out.println("Item Name: " + result.getString("name"));
        System.out.println("Total Items: " + result.getInt("stock"));
        System.out.println("--------------------");
        }
   } catch (Exception e){
       e.printStackTrace();
       System.err.println(e.getClass().getName()+": "+e.getMessage());
       System.exit(0);
   }

    //closing the connection
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch(Exception e) {
      System.out.println("Connection NOT Closed.");
    }//end try catch
  }//end main
}//end Class