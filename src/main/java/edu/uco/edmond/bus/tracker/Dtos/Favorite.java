
package edu.uco.edmond.bus.tracker.Dtos;
import edu.uco.edmond.bus.tracker.Dtos.Dto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Favorite {
    
    private int id;
    private int userId;
    private int favoriteId;
    private String type;
    
    public static String[] possibleTypes = {"BusStop"};
    
    public Favorite(int id, int userId, int favoriteId, String type)
    {
        this.id = id;
        this.userId = userId;
        this.favoriteId = favoriteId;
        this.type = type;
    }
    
    public int getId()
    {
        return id;
    }
    
    public int getUserId()
    {
        return userId;
    }
    
    public int getFavoriteId()
    {
        return favoriteId;
    }
    
    public String getType()
    {
        return type;
    }
}
