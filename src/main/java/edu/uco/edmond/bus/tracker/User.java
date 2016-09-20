package edu.uco.edmond.bus.tracker;

public class User {
    private int id;
    private String username;
    private String password;
    private String type;
    
    public User(String username, String password, String type){
        this.username = username;
        this.password = password;
        this.type = type;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }  
    
    public String getType(){
        return type;
    }
}
