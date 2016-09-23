//test Demechko branch
package edu.uco.edmond.bus.tracker;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class indexBean {
    DBConnect db = new DBConnect();
    private String message = "Hello Team UCO!";
    
    public String getMessage(){
        //UserService userService = new UserService(db.getDatabase());
        return message;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
}
