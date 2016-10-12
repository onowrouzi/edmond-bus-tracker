
package edu.uco.edmond.bus.tracker.Services;

import com.google.gson.Gson;
import edu.uco.edmond.bus.tracker.DBConnect;
import java.sql.Connection;
import java.sql.SQLException;

public class Service {
    
    private static Connection database;
    private static Gson gson = new Gson();
    
    public Service() throws SQLException
    {
        database = DBConnect.getDatabase();
    }
    
    public Connection getDatabase()
    {
        return database;
    }
    
    public Gson getGson()
    {
        return gson;
    }
}
