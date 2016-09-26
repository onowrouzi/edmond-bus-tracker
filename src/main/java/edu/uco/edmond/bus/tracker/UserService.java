
package edu.uco.edmond.bus.tracker;

import com.google.gson.Gson;
import java.sql.Connection;
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

@Path("userservice")
public class UserService {
    
    Gson gson = new Gson();
    Connection database;
    List<User> users;
    
    public UserService() throws SQLException
    {
        database = new DBConnect().getDatabase();
        this.users = new ArrayList<>();
        getAllUsers();
    }
    
    private void getAllUsers() throws SQLException
    {
        Statement stmt = database.createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM tblusers");

        while(rs.next()){
            User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("usertype"));
            users.add(user);
        }
        
        stmt.close();
    }
    
    public User find(int id)
    {
        for(User user : users)
        {
            if(user.getId() == id)
            {
                return user; //user found
            }
        }
            
        return null;
    }
    
    public User find(String username)
    {
        for(User user : users)
        {
            if(user.getUsername().equals(username))
            {
                return user; //user found
            }
        }
        
        //no user found
        return null;
    }   
        
    public User find(String username, String password)
    {
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
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users")
    public String getUsers(){
        if(users.isEmpty())
            return gson.toJson("No users currently registered."); // no users in system
        
        return gson.toJson(users);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/{id}")
    public String getUser(@PathParam("id") int id) throws SQLException {
        return gson.toJson(find(id));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/administrator/{username}/{password}") //talk about using optional params ?isadmin=true
    public String administratorLogin(@PathParam("username") String username, @PathParam("password") String password) throws SQLException
    {
        User user = find(username, password);
        
        if(user != null)
            if("Administrator".equals(user.getType()))
                return gson.toJson(user); //admin found
            else
                return gson.toJson(null); //user is only allowed on mobile --send message to client code
        
        return gson.toJson(null); //user not found
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/{username}/{password}")//talk about using optional params ?isadmin=false
    public String clientLogin(@PathParam("username") String username, @PathParam("password") String password) throws SQLException
    {
        //admin should be able to login to both mobile and web systems
        return gson.toJson(find(username, password));
    }
    
    public void logout()
    {
        
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/create/{username}/{password}/{type}")
    public String register(@PathParam("username") String username, @PathParam("password") String password, @PathParam("type") String type)
    {
        User User = find(username);
        
        if(User != null)
            return gson.toJson(null); //send error message on client --user exists
        
        try{
            PreparedStatement stmt = database.prepareStatement("INSERT INTO tblusers (username, password, usertype) VALUES(?,?,?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, type);

            int count = stmt.executeUpdate();
            
            stmt.close();

            //get id of new user
            PreparedStatement stmt2 = database.prepareStatement("SELECT id FROM tblusers WHERE username=?");
            stmt2.setString(1, username);

            ResultSet rs = stmt2.executeQuery();
            
            rs.first();

            int id = rs.getInt("id");
            User = new User(id,username,password,type);
            users.add(User);  
            
            stmt2.close();
            
        }catch(SQLException s){
            return gson.toJson(s.toString()); //SQL failed
        }
        
        return gson.toJson(User);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/edit/{username}/{oldPassword}/{newPassword}")
    public User edit(@PathParam("username") String username, @PathParam("oldPassword")String oldPassword, @PathParam("newPassword") String newPassword)
    {
        return null;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/delete/{username}")
    public String delete(@PathParam("username") String username)
    {
        User User = find(username);
        
        if(User == null)
            return gson.toJson(null); //send error message on client --user does not exist
        
        try{
            PreparedStatement stmt = database.prepareStatement("DELETE FROM tblusers WHERE id=?");
            stmt.setInt(1, User.getId());

            int count = stmt.executeUpdate();
            
            stmt.close();
            
        }catch(SQLException s){
            return gson.toJson(s.toString());
        }
        
        users.remove(User);
        
        return gson.toJson(User);
    }
}