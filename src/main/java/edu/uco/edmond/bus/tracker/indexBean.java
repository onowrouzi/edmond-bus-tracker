//test Drew_branch
package edu.uco.edmond.bus.tracker;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class indexBean {
    
    private String message = "Hello Team UCO!";
    
    public String getMessage(){
        return message;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
}
