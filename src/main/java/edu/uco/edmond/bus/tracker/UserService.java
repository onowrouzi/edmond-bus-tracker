
package edu.uco.edmond.bus.tracker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    Connection database;
    boolean upToDate;
    List<User> users; //may change to hashset to make sure a user is not duplicated in db.
    
    public UserService(Connection database){
        //establishes a new datbase connection
        this.database = database;
        upToDate = false;
        users = new ArrayList<>();
    }
    
    public List<User> getUsers() throws SQLException{
        
        if(upToDate)
            return users;
        
        Statement stmt = database.createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM tblusers");
        
        while(rs.next()){
            User user = new User(rs.getString("username"), rs.getString("password"), rs.getString("type"));
            users.add(user);
        }
        
        database.close();
        
        upToDate = true;
        
        return users;
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
