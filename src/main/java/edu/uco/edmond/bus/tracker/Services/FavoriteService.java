
package edu.uco.edmond.bus.tracker.Services;

import edu.uco.edmond.bus.tracker.Dtos.Bus;
import edu.uco.edmond.bus.tracker.Dtos.BusStop;
import edu.uco.edmond.bus.tracker.Dtos.Favorite;
import edu.uco.edmond.bus.tracker.Dtos.Notification;
import edu.uco.edmond.bus.tracker.Services.UserService;
import edu.uco.edmond.bus.tracker.Dtos.User;
import edu.uco.edmond.bus.tracker.Support.UsersFavorites;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("favoriteservice")
public class FavoriteService extends Service {
    
    UserService userService;
    BusService busService;
    BusStopService busStopService;
    List<Favorite> favorites;
    List<UsersFavorites> usersFavorites;
    
    public FavoriteService() throws SQLException
    {
        this.favorites = new ArrayList<>();
        this.usersFavorites = new ArrayList<>();
        userService = new UserService();
        busService = new BusService();
        busStopService = new BusStopService();
        getAllFavorites();
        getAllUsersFavorites();
    }
    
    private void getAllFavorites()
    {
        //Get all Bus Stop Favorites
        try{
            Statement stmtBusStop = getDatabase().createStatement();

            ResultSet rsBusStop = stmtBusStop.executeQuery("SELECT * FROM tblbusstopfavorites");

            while(rsBusStop.next()){
                Favorite favorite = new Favorite(rsBusStop.getInt("id"), rsBusStop.getInt("userbusstopId"), rsBusStop.getInt("busstopId"), "Bus Stop", rsBusStop.getString("name"));
                favorites.add(favorite);
            }

            stmtBusStop.close();

            //Get all Bus Favorites
            Statement stmtBus = getDatabase().createStatement();

            ResultSet rsBus = stmtBus.executeQuery("SELECT * FROM tblbusfavorites");

            while(rsBus.next()){
                Favorite favorite = new Favorite(rsBus.getInt("id"), rsBus.getInt("userbusId"), rsBus.getInt("busId"), "Bus", rsBus.getString("name"));
                favorites.add(favorite);
        }
            stmtBus.close();
            getDatabase().close();
            
        }catch(SQLException s){
            System.out.println(s.toString()); //SQL failed
        }
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
    @Path("favorites/notifications/{username}")
    public String getNotifications(@PathParam("username") String username)
    {
        UsersFavorites usersFavorite = find(username);
        
        if(usersFavorite == null)
            return getGson().toJson(null); //no user found
        
        if(usersFavorite.favorites().isEmpty())
            return getGson().toJson("No favorites saved for this user");
        
        List<Bus> myBuses = new ArrayList<>();
        List<BusStop> myBusStops = new ArrayList<>();
        List<Notification> notifications = new ArrayList<>();
        
        //transfer current favorites into buses and bus stops
        for(Favorite favorite : usersFavorite.favorites())
        {
            if(favorite.getType().equals("Bus"))
                myBuses.add(busService.find(favorite.getName()));
            else if(favorite.getType().equals("Bus Stop"))
                myBusStops.add(busStopService.find(favorite.getName()));
        }
        
        //add notifications involving stops
        for(Bus bus : myBuses)
        {
            notifications.add(new Notification(bus.getActive() ? bus.getName() + " is currently driving routes" : bus.getName() + " is not currently driving routes"));
            for(BusStop stop : myBusStops)
                if(bus.getLastStop() != null && bus.getLastStop().equals(stop.getName()))
                    notifications.add(new Notification(bus.getName() + " is at stop " + stop.getName()));
        }
        
        for(BusStop busStop : myBusStops)
        {
            for(Bus bus: busService.buses())
                if(bus.getActive() && bus.getLastStop() != null && busStop.getName() != null && bus.getLastStop().equals(busStop.getName()))
                    notifications.add(new Notification(bus.getName() + " is at one of your favorite stops, " + busStop.getName()));
        }
        
        return getGson().toJson(notifications);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("favorites/{username}")
    public String getUsersFavorites(@PathParam("username") String username)
    {
        UsersFavorites usersFavorite = find(username);
        
        if(usersFavorite == null)
            return getGson().toJson(null); //no user found
        
        if(usersFavorite.favorites().isEmpty())
            return getGson().toJson("No favorites saved for this user");
        
        return getGson().toJson(usersFavorite.favorites());
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("favorites/possible/{username}")
    public String getUsersPossibleFavorites(@PathParam("username") String username)
    {
        UsersFavorites usersFavorite = find(username);
        
        if(usersFavorite == null)
            return getGson().toJson(null); //no user found
        
        List<Favorite> possibleFavorites = new ArrayList<>();
        
        for(Bus bus : busService.buses())
            possibleFavorites.add(new Favorite(bus.getId(), "Bus", bus.getName()));
        for(BusStop stop : busStopService.busStops())
            possibleFavorites.add(new Favorite(stop.getId(), "Bus Stop", stop.getName()));
        
        for(Favorite impossible : usersFavorite.favorites())
            possibleFavorites.remove(impossible);
            
        if(possibleFavorites.isEmpty())
            return getGson().toJson("No possible favorites available for this user");
                
        return getGson().toJson(possibleFavorites);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("favorites/create/bus/{userId}/{favoriteId}")
    public String createBus(@PathParam("userId") int userId, @PathParam("favoriteId") int favoriteId)
    {  
        UsersFavorites usersFavorite = find(userId);
        String busName = busService.find(favoriteId).getName();
        
        if(usersFavorite == null)
            return getGson().toJson(null); //no user found
        
        for(Favorite favorite : usersFavorite.favorites())
            if(favorite.getType().equals("Bus") && favorite.getFavoriteId() == favoriteId)
                return getGson().toJson(null); //favorite already exists for this user
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("INSERT INTO tblbusfavorites (userbusId, busId, name) VALUES(?,?,?)");
            stmt.setInt(1, userId);
            stmt.setInt(2, favoriteId);
            stmt.setString(3, busName);

            int count = stmt.executeUpdate();
            
            stmt.close();

            //get id of new favorite
            PreparedStatement stmt2 = getDatabase().prepareStatement("SELECT id FROM tblbusfavorites WHERE userbusId=? AND busId=?");
            stmt2.setInt(1, userId);
            stmt2.setInt(2, favoriteId);

            ResultSet rs = stmt2.executeQuery();
            
            rs.first();

            int id = rs.getInt("id");
            Favorite newFavorite = new Favorite(id,userId,favoriteId,"Bus",busName);
            usersFavorite.favorites().add(newFavorite);  
            
            stmt2.close();
            
            //Close out current SQL connection
            getDatabase().close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString()); //SQL failed
        }
        
        return getGson().toJson(usersFavorite);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("favorites/create/busstop/{userId}/{favoriteId}")
    public String createBusStop(@PathParam("userId") int userId, @PathParam("favoriteId") int favoriteId)
    {  
        UsersFavorites usersFavorite = find(userId);
        String busStopName = busStopService.find(favoriteId).getName();
        
        if(usersFavorite == null)
            return getGson().toJson(null); //no user found
        
        for(Favorite favorite : usersFavorite.favorites())
            if(favorite.getType().equals("Bus Stop") && favorite.getFavoriteId() == favoriteId)
                return getGson().toJson(null); //favorite already exists for this user
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("INSERT INTO tblbusstopfavorites (userbusstopId, busstopId, name) VALUES(?,?,?)");
            stmt.setInt(1, userId);
            stmt.setInt(2, favoriteId);
            stmt.setString(3, busStopName);

            int count = stmt.executeUpdate();
            
            stmt.close();

            //get id of new favorite
            PreparedStatement stmt2 = getDatabase().prepareStatement("SELECT id FROM tblbusstopfavorites WHERE userbusstopId=? AND busstopId=?");
            stmt2.setInt(1, userId);
            stmt2.setInt(2, favoriteId);

            ResultSet rs = stmt2.executeQuery();
            
            rs.first();

            int id = rs.getInt("id");
            Favorite newFavorite = new Favorite(id,userId,favoriteId,"Bus Stop", busStopName);
            usersFavorite.favorites().add(newFavorite);  
            
            stmt2.close();
            
            //Close out current SQL connection
            getDatabase().close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString()); //SQL failed
        }
        
        return getGson().toJson(usersFavorite);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("favorites/delete/bus/{userId}/{favoriteId}")
    public String deleteBus(@PathParam("userId") int userId, @PathParam("favoriteId") int favoriteId)
    {
        UsersFavorites usersFavorite = find(userId);
        Favorite favoriteBus = null;
        
        if(usersFavorite == null)
            return getGson().toJson(null); //no user found
        
        for(Favorite favorite : usersFavorite.favorites()){
            if(favorite.getType().equals("Bus") && favorite.getFavoriteId() == favoriteId){
                favoriteBus = favorite; //Bus saved as favorite
                break;
            }
        }
        
        if(favoriteBus == null)
            return getGson().toJson(null); //Bus not currently saved as favorite --user cannot delete an unsaved favorite
        
        //remove bus from list of user's favorites
        usersFavorite.favorites().remove(favoriteBus);
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("DELETE FROM tblbusfavorites WHERE userbusId=? AND busId=?");
            stmt.setInt(1, userId);
            stmt.setInt(2, favoriteId);
                
            int count = stmt.executeUpdate();
            stmt.close();
            
            //Close out current SQL connection
            getDatabase().close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString()); //SQL failed
        }
        
        return getGson().toJson(favoriteBus);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("favorites/delete/busstop/{userId}/{favoriteId}")
    public String deleteBusStop(@PathParam("userId") int userId, @PathParam("favoriteId") int favoriteId)
    {
        UsersFavorites usersFavorite = find(userId);
        Favorite favoriteBusStop = null;
        
        if(usersFavorite == null)
            return getGson().toJson(null); //no user found
        
        for(Favorite favorite : usersFavorite.favorites()){
            if(favorite.getType().equals("Bus Stop") && favorite.getFavoriteId() == favoriteId){
                favoriteBusStop = favorite; //Bus Stop saved as favorite
                break;
            }
        }
        
        if(favoriteBusStop == null)
            return getGson().toJson(null); //Bus Stop not currently saved as favorite --user cannot delete an unsaved favorite
        
        //remove bus stop from list of user's favorites
        usersFavorite.favorites().remove(favoriteBusStop);
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("DELETE FROM tblbusstopfavorites WHERE userbusstopId=? AND busstopId=?");
            stmt.setInt(1, userId);
            stmt.setInt(2, favoriteId);
                
            int count = stmt.executeUpdate();
            stmt.close();
            
            //Close out current SQL connection
            getDatabase().close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString()); //SQL failed
        }
        
        return getGson().toJson(favoriteBusStop);
    }
}
