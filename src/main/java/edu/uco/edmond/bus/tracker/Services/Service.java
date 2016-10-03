
package edu.uco.edmond.bus.tracker.Services;

import com.google.gson.Gson;
import edu.uco.edmond.bus.tracker.DBConnect;
import java.sql.Connection;
import java.sql.SQLException;

public class Service {
    
    private Connection database;
    private Gson gson;
    
    public Service() throws SQLException
    {
        database = new DBConnect().getDatabase();
        gson = new Gson();
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
