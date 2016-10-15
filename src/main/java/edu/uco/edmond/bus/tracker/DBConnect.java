
package edu.uco.edmond.bus.tracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect {
    private Connection database;
    
    public DBConnect()
    {
        connect();
    }
    
    public void connect()
    {
        try{
        String host = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net:3306/heroku_0d3e98bff6a2c85";
        String username = "b73f7005c851b7";
        String password = "7412647b";

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
    
    public Connection getDatabase() throws SQLException{
        return database;
    }
    
    public boolean isConnected() throws SQLException
    {
        return (database != null || (!(database.isClosed())));
    }
}
