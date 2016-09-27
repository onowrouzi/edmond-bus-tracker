
package edu.uco.edmond.bus.tracker;
import edu.uco.edmond.bus.tracker.Dto;

public class Favorite {
    
    private int id;
    private int userId;
    private int favoriteId;
    private Dto type;
    
    private Dto dto;
    private BusStop busStop;
    
    public Favorite(int id, int userId, int favoriteId, String type)
    {
        this.id = id;
        this.userId = userId;
        this.favoriteId = favoriteId;
        
        if("".equals(type))
            this.type = dto;
        else if("BusStop".equals(type))
            this.type = busStop;
    }
    
    public int getUserId()
    {
        return userId;
    }
    
    public int getFavoriteId()
    {
        return favoriteId;
    }
    
    public Dto getType()
    {
        return type;
    }
}