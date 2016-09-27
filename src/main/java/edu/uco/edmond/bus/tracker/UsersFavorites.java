
package edu.uco.edmond.bus.tracker;

import java.util.ArrayList;
import java.util.List;

public class UsersFavorites {
    
    private int userId;
    private List<Favorite> favorites;
    
    public UsersFavorites(int userId)
    {
        this.userId = userId;
        favorites = new ArrayList<>();
    }
    
    public int getUserId()
    {
        return userId;
    }
    
    public List<Favorite> favorites()
    {
        return favorites;
    }   
}
