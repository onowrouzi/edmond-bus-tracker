package edu.uco.edmond.bus.tracker.Dtos;

public class User extends Dto{
    private int id;
    private String username;
    private String password;
    private String type;
    
    public User(int id, String username, String password, String type){
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type != null ? type : "client";
    }
    
    public int getId(){
        return id;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getType(){
        return type;
    }
}