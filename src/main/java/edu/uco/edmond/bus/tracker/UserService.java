
package edu.uco.edmond.bus.tracker;

import com.google.gson.Gson;
import java.sql.Connection;
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

@Path("userservice")
public class UserService {
    
    //public User test = new User("test", "test", "test");
    Gson gson = new Gson();
    DBConnect db;
    Connection database;
    boolean upToDate;
    List<User> users; //may change to hashset to make sure a user is not duplicated in db.
    
    public UserService()
    {
        db = new DBConnect();
        this.database = db.getDatabase();
        this.upToDate = false;
        this.users = new ArrayList<>();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users")
    public String getUsers() throws SQLException {
        
        Statement stmt = database.createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM tblusers");
        
        while(rs.next()){
            User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("usertype"));
            users.add(user);
        }
        
        upToDate = true;
        
        return gson.toJson(users);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/{id}")
    public String getUser(@PathParam("id") int id) throws SQLException {
        if(!upToDate)
            getUsers();
        
        for(User user : users)
        {
            if(user.getId() == id)
            {
                return gson.toJson(user); //user found
            }
        }
        
        //no user found
        return gson.toJson(null);
    }
    
    public User administratorLogin(String username, String password) throws SQLException
    {
        User user = login(username, password);
        
        if(user != null)
            if("Administrator".equals(user.getType()))
                return user; //admin found
            else
                return null; //user is only allowed on mobile --send message to client code
        
        return null; //user not found
    }
    
    public User clientLogin(String username, String password) throws SQLException
    {
        //admin should be able to login to both mobile and web systems
        return login(username, password);
    }
    
    private User login(String username, String password) throws SQLException
    {
        if(!upToDate)
            getUsers();
        
        for(User user : users)
        {
            if(user.getUsername().equals(username))
            {
                if(user.getPassword().equals(password))
                    return user; //user found
                else
                    break; //username exists but wrong password --send message back to client code
            }
        }
        
        //no user found
        return null;
    }
    
    public void logout()
    {
        
    }
    
    public User register(String username, String password)
    {
        //make SQL call and change the users list in code (than we can remove the booleans) or just keep the booleans and just make the appropriate SQL calls. We will discuss
        upToDate = false;
        return null;
    }
    
    public User edit(String oldUsername, String oldPassword, String newUsername, String newPassword)
    {
        //make SQL call and change the users list in code (than we can remove the booleans) or just keep the booleans and just make the appropriate SQL calls. We will discuss
        upToDate = false;
        return null;
    }
    
    public User delete(String username, String password)
    {
        //make SQL call and change the users list in code (than we can remove the booleans) or just keep the booleans and just make the appropriate SQL calls. We will discuss
        upToDate = false;
        return null;
    }
}