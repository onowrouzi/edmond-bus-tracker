
package edu.uco.edmond.bus.tracker.Services;

import com.google.gson.Gson;
import edu.uco.edmond.bus.tracker.Dtos.User;
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
public class UserService extends Service{
    
    private List<User> users;
    
    public UserService() throws SQLException
    {
        this.users = new ArrayList<>();
        getAllUsers();
    }
    
    public List<User> users()
    {
        return users;
    }
    
    private void getAllUsers() throws SQLException
    {
        Statement stmt = getDatabase().createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM tbluser");

        while(rs.next()){
            User user = new User(rs.getInt("id"), rs.getString("username"), rs.getNString("password"), rs.getString("role"),
                rs.getString("firstname"), rs.getString("lastname"), rs.getString("email"));
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
    
    public List<User> findGroupUsers(String userType)
    {
        List<User> groupUsers = new ArrayList<>();
        for(User user : users)
        {
            if(user.getType().equals(userType))
            {
                // add user to list
                groupUsers.add(user);
            }
        }
        
        if (groupUsers.isEmpty()) {
            return null;
        } else {
            return groupUsers;
        }
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
            return getGson().toJson("No users currently registered."); // no users in system
        
        return getGson().toJson(users);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/{id}")
    public String getUser(@PathParam("id") int id) throws SQLException {
        return getGson().toJson(find(id));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/usertype/{type}")
    public String getGroupUsers(@PathParam("type") String userType) throws SQLException {
        List<User> groupUsers = findGroupUsers(userType);
        if (groupUsers == null || groupUsers.isEmpty())
            return getGson().toJson("No users currently registered.");
        
        return getGson().toJson(groupUsers);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/administrator/{username}/{password}") //talk about using optional params ?isadmin=true
    public String administratorLogin(@PathParam("username") String username, @PathParam("password") String password) throws SQLException
    {
        User user = find(username, password);
        
        if(user != null)
            if("admin".equals(user.getType()))
                return getGson().toJson(user); //admin found
            else
                return getGson().toJson(null); //user is only allowed on mobile --send message to client code
        
        return getGson().toJson(null); //user not found
    }
     
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/{username}/{password}")//talk about using optional params ?isadmin=false
    public String clientLogin(@PathParam("username") String username, @PathParam("password") String password) throws SQLException
    {
        //admin should be able to login to both mobile and web systems
        return getGson().toJson(find(username, password));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/create/{username}/{password}/{type}/{firstname}/{lastname}/{email}")
    public String register(@PathParam("username") String username, @PathParam("password") String password, 
            @PathParam("type") String type, @PathParam("firstname") String firstname, 
            @PathParam("lastname") String lastname, @PathParam("email") String email)
    {
        User user = find(username);
        
        if(user != null)
            return getGson().toJson(null); //send error message on client --user exists
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("INSERT INTO tbluser (username, password, role, firstname, lastname, email) VALUES(?,?,?,?,?,?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, type);
            stmt.setString(4, firstname);
            stmt.setString(5, lastname);
            stmt.setString(6, email);

            int count = stmt.executeUpdate();
            
            stmt.close();

            //get id of new user
            PreparedStatement stmt2 = getDatabase().prepareStatement("SELECT id FROM tbluser WHERE username=?");
            stmt2.setString(1, username);

            ResultSet rs = stmt2.executeQuery();
            
            rs.first();

            int id = rs.getInt("id");
            user = new User(id,username,password,type,firstname,lastname,email);
            users.add(user);  
            
            stmt2.close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString()); //SQL failed
        }
        
        return getGson().toJson(user);
    }
    
    //TODO change route to oldusername/newusername/newpassword, update mobile client as well
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/edit/{username}/{oldPassword}/{newPassword}")
    public String edit(@PathParam("username") String username, @PathParam("oldPassword")String oldPassword, @PathParam("newPassword") String newPassword)
    {
        User user = find(username);
        
        if(user == null)
            return getGson().toJson(null); //send error message on client --user does not exist, due to the way the client is set up this should never happen
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("UPDATE tbluser SET password=? WHERE id=?");
            stmt.setString(1, newPassword);
            stmt.setInt(2, user.getId());

            int count = stmt.executeUpdate();
            
            stmt.close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString());
        }
        
        user.setPassword(newPassword); //update instance
        
        return getGson().toJson(user);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/delete/{username}")
    public String delete(@PathParam("username") String username)
    {
        User user = find(username);
        
        if(user == null)
            return getGson().toJson(null); //send error message on client --user does not exist
        
        try{
            PreparedStatement stmt;
            
            // check if user is admin or not
            // in order to delete the proper data
            if (user.getType().equals("admin")) {
                stmt = getDatabase().prepareStatement("DELETE FROM tblnotifications WHERE sentby=?");
                stmt.setString(1, username);
                
                int count = stmt.executeUpdate();
                stmt.close();
            } else { // delete user data
                stmt = getDatabase().prepareStatement("DELETE FROM tblusernotifications WHERE sentby=?");
                stmt.setString(1, username);
                
                int count = stmt.executeUpdate();
                stmt.close();
            }
            
            stmt = getDatabase().prepareStatement("DELETE FROM tbluser WHERE id=?");
            stmt.setInt(1, user.getId());

            int count = stmt.executeUpdate();
            
            stmt.close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString());
        }
        
        users.remove(user);
        
        return getGson().toJson(user);
    }
}