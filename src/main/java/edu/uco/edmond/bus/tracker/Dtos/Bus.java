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
    private double lastLongitude;
    private double lastLatitude;
    
    public Bus() {}
    
    public Bus(int id, String name, String driver, String route) {
        this.id = id;
        this.name = name;
        this.driver = driver;
        this.route = route;
        lastLongitude = 0;
        lastLatitude = 0;
    }
    
    public Bus(int id, String name, String driver, String route, 
            String lastStop, Boolean active, String lastActive, double lastLongitude, double lastLatitude) {
        this.id = id;
        this.name = name;
        this.driver = driver;
        this.route = route;
        this.lastStop = lastStop;
        this.active = active;
        this.lastActive = lastActive;
        this.lastLongitude = lastLongitude;
        this.lastLatitude = lastLatitude;
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
    
    public double getLastLongitude()
    {
        return lastLongitude;
    }
    
    public double getLastLatitude()
    {
        return lastLatitude;
    }
}
