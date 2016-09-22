
package edu.uco.edmond.bus.tracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    private Connection database;
    
    public DBConnect()
    {
        try{
        String host = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/heroku_0d3e98bff6a2c85";
        String username = "b73f7005c851b7";
        String password = "7412647b";

        database = DriverManager.getConnection(host, username, password);
        System.out.println("Connection successful...");
        }catch(SQLException e){
        System.out.println(e.getMessage());
        }
    }
    
    public Connection getDatabase(){
        return database;
    }
    
    public boolean isConnected()
    {
        return database != null;
    }
}
