package edu.uco.edmond.bus.tracker.Dtos;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class User extends Dto{
    private int id;
    private String username;
    private String password;
    private String type;
    private String firstName;
    private String lastName;
    private String email;
    
    public User(int id, String username, String password, String type, String firstName, String lastName, String email){
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type != null ? type : "client";
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    public User() {}
    
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}