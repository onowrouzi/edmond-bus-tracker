
package edu.uco.edmond.bus.tracker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("favoriteservice")
public class FavoriteService extends Service {
    
    UserService userService;
    List<Favorite> favorites;
    List<UsersFavorites> usersFavorites;
    
    public FavoriteService() throws SQLException
    {
        this.favorites = new ArrayList<>();
        this.usersFavorites = new ArrayList<>();
        userService = new UserService();
        getAllFavorites();
        getAllUsersFavorites();
    }
    
    private void getAllFavorites() throws SQLException
    {
        Statement stmt = getDatabase().createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM tblfavorites");

        while(rs.next()){
            Favorite favorite = new Favorite(rs.getInt("id"), rs.getInt("userId"), rs.getInt("favoriteId"), rs.getString("favoritetype"));
            favorites.add(favorite);
        }
        
        stmt.close();
    }
    
    private void getAllUsersFavorites()
    {
        for(User user : userService.users())
        {
            usersFavorites.add(new UsersFavorites(user.getId()));
        }
        
        for(UsersFavorites userfavorites : usersFavorites)
        {
            for(Favorite favorite : favorites)
            {
                if(userfavorites.getUserId() == favorite.getUserId())
                {
                    userfavorites.favorites().add(favorite);
                    break;
                }
            }
        }
        
        favorites = null; //set favorites list to null to ease garbage collector tasks
    }
    
    public UsersFavorites find(int userId)
    {
        for(UsersFavorites usersFavorite : usersFavorites)
        {
            if(usersFavorite.getUserId() == userId)
            {
                return usersFavorite; //user's favorites found
            }
        }
            
        return null; //user not found
    }
    
    public UsersFavorites find(String username)
    {
        User user = null;
        
        for(User u : userService.users())
        {
            if(u.getUsername().equals(username))
            {
                user = u; //user found
            }
        }
        
        if(user == null)
            return null; //no user found
        
        return find(user.getId());
    }  
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("favorites/{username}")
    public String getUsersFavorites(@PathParam("username") String username){
        UsersFavorites usersFavorite = find(username);
        
        if(usersFavorite == null)
            return getGson().toJson(null); //no user found
        
        if(usersFavorite.favorites().isEmpty())
            return getGson().toJson("No favorites saved for this user");
        
        return getGson().toJson(usersFavorite.favorites());
    }   
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("favorites/create/{userId}/{favoriteId}/{type}")
    public String create(@PathParam("userId") int userId, @PathParam("favoriteId") int favoriteId, @PathParam("type") String type)
    {
        boolean isValidType = false;
        
        for(String t : Favorite.possibleTypes)
            if(t.equals(type))
                isValidType = true;
        
        if(!(isValidType))
            getGson().toJson("Invalid favorite type");
        
        UsersFavorites usersFavorite = find(userId);
        
        if(usersFavorite == null)
            return getGson().toJson(null); //no user found
        
        for(Favorite favorite : usersFavorite.favorites())
            if(favorite.getType().equals(type) && favorite.getFavoriteId() == favoriteId)
                return getGson().toJson(null); //favorite already exists for this user
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("INSERT INTO tblfavorites (userId, favoriteId, favoritetype) VALUES(?,?,?)");
            stmt.setInt(1, userId);
            stmt.setInt(2, favoriteId);
            stmt.setString(3, type);

            int count = stmt.executeUpdate();
            
            stmt.close();

            //get id of new favorite
            PreparedStatement stmt2 = getDatabase().prepareStatement("SELECT id FROM tblfavorites WHERE userId=? AND favoriteId=?");
            stmt2.setInt(1, userId);
            stmt2.setInt(2, favoriteId);

            ResultSet rs = stmt2.executeQuery();
            
            rs.first();

            int id = rs.getInt("id");
            Favorite newFavorite = new Favorite(id,userId,favoriteId,type);
            usersFavorite.favorites().add(newFavorite);  
            
            stmt2.close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString()); //SQL failed
        }
        
        return getGson().toJson(usersFavorite);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("favorites/delete/{userId}/{favoriteId}/{type}")
    public String delete(@PathParam("userId") int userId, @PathParam("favoriteId") int favoriteId, @PathParam("type") String type)
    {
        return null;
    }
}
