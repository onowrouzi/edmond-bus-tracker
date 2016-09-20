
package edu.uco.edmond.bus.tracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    Connection database;
    
    public DBConnect()
    {
        try{
        String host = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net";
        String username = "b73f7005c851b7";
        String password = "7412647b";

        database = DriverManager.getConnection(host, username, password);
        }catch(SQLException e){
        System.out.println(e.getMessage());
        }
    }  
}
