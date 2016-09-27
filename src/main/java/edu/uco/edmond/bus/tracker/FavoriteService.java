
package edu.uco.edmond.bus.tracker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;

public class FavoriteService extends Service {
    
    UserService userService;
    List<User> users;
    List<Favorite> favorites;
    
    public FavoriteService() throws SQLException
    {
        this.favorites = new ArrayList<>();
        userService = new UserService();
        users = userService.users();
        getAllFavorites();
    }
    
    private void getAllFavorites() throws SQLException
    {
        Statement stmt = getDatabase().createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM tblfavorites");

        while(rs.next()){
            Favorite favorite = new Favorite(rs.getInt("id"), rs.getInt("userId"), rs.getInt("favoriteId"), rs.getString("type"));
            favorites.add(favorite);
        }
        
        stmt.close();
    }
}
