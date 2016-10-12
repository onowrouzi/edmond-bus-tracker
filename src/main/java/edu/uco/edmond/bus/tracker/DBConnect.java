
package edu.uco.edmond.bus.tracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DBConnect {
    static String host = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net:3306/heroku_0d3e98bff6a2c85";
    static String username = "b73f7005c851b7";
    static String password = "7412647b";
    private static Connection database;
    
    public static Connection getDatabase()
    {
        if(!(isConnected()))
            setConnection();
        return database;
    }
    
    public static void setConnection()
    {
        try{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        database = DriverManager.getConnection(host, username, password);
        System.out.println("Connection successful...");
        }catch(SQLException e){
        System.out.println(e.getMessage());
        }
    }
    
    public static boolean isConnected()
    {
        return database != null;
    }
}
