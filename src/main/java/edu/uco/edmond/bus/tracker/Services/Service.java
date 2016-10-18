
package edu.uco.edmond.bus.tracker.Services;

import com.google.gson.Gson;
import edu.uco.edmond.bus.tracker.DBConnect;
import java.sql.Connection;
import java.sql.SQLException;

public class Service {
    
    private DBConnect db = new DBConnect();
    private Connection database;
    private Gson gson;
    
    public Service() throws SQLException
    {
        database = db.getDatabase();
        gson = new Gson();
    }
    
    public Connection getDatabase() throws SQLException
    {
        if(database == null || database.isClosed()){
            db.connect();
            database = db.getDatabase();
        }
        return database;
    }
    
    public Gson getGson()
    {
        return gson;
    }
}
