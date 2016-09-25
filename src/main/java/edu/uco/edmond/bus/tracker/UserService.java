
package edu.uco.edmond.bus.tracker;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("userservice")
public class UserService {
    
    Gson gson = new Gson();
    //Connection database;
    List<User> users;
    
    public UserService() //throws SQLException
    {
        //this.database = new DBConnect().getDatabase();
        this.users = new ArrayList<>();
        getAllUsers();
    }
    
    private void getAllUsers() //throws SQLException
    {
        User user1 = new User(1,"testclient", "test", "client");
        User user2 = new User(2,"testadmin","test","administrator");
        users.add(user1);
        users.add(user2);
        /*Statement stmt = database.createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM tblusers");

        while(rs.next()){
            User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("usertype"));
            users.add(user);
        }*/
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
            return gson.toJson(null); // no users in system
        
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
    /*
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public User register(@FormParam("username") String username, @FormParam("password") String password, @FormParam("type") String type)
    {
        User tempUser = find(username, password);
        
        if(tempUser != null)
            return null; //send error message on client --user exists
        
        try{
            PreparedStatement stmt = database.prepareStatement("INSERT INTO tblusers VALUES(?,?,?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, type);

            int count = stmt.executeUpdate(); //?test executeupdate()???

            //get id of new user
            PreparedStatement stmt2 = database.prepareStatement("SELECT * FROM tblusers WHERE username=? AND password=?");
            stmt2.setString(1, username);
            stmt2.setString(2, password);

            ResultSet rs = stmt2.executeQuery();

            int id = rs.getInt("id");
            User user = new User(id,username,password,type);
            users.add(user);

            return user;
        }catch(SQLException s){
            return null; //SQL failed
        }
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public User edit(@FormParam("username") String username, @FormParam("oldpassword")String oldPassword, @FormParam("newpassword") String newPassword)
    {
        return null;
    }
    
    public User delete(String username, String password)
    {
        return null;
    }*/
}