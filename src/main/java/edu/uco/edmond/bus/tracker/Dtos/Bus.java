package edu.uco.edmond.bus.tracker.Dtos;

import java.sql.Date;

public class Bus extends Dto {
    
    private int id;
    private String name;
    private String driver;
    private String route;
    private String lastStop;
    private Boolean active;
    private String lastActive;
    
    public Bus() {}
    
    public Bus(int id, String name, String driver, String route, 
            String lastStop, Boolean active, String lastActive) {
        this.id = id;
        this.name = name;
        this.driver = driver;
        this.route = route;
        this.lastStop = lastStop;
        this.active = active;
        this.lastActive = lastActive;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDriver() {
        return driver;
    }

    public String getRoute() {
        return route;
    }
    
    public String getLastStop(){
        return lastStop;
    }
    
    public Boolean getActive(){
        return active;
    }
    
    public String getLastActive(){
        return lastActive;
    }
}
