package edu.uco.edmond.bus.tracker.Dtos;

public class Bus extends Dto {
    
    private int id;
    private String name;
    private String driver;
    private String route;
    private String lastStop;
    private Boolean active;
    private String lastActive;
    private double lastLong;
    private double lastLat;
    
    public Bus() {}
    
    public Bus(int id, String name, String driver, String route) {
        this.id = id;
        this.name = name;
        this.driver = driver;
        this.route = route;
        lastLong = 0;
        lastLat = 0;
    }
    
    public Bus(int id, String name, String driver, String route, 
            String lastStop, Boolean active, String lastActive, double lastLong, double lastLat) {
        this.id = id;
        this.name = name;
        this.driver = driver;
        this.route = route;
        this.lastStop = lastStop;
        this.active = active;
        this.lastActive = lastActive;
        this.lastLong = lastLong;
        this.lastLat = lastLat;
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
        return lastLong;
    }
    
    public double getLastLatitude()
    {
        return lastLat;
    }
    
    public void changeStatus()
    {
        active = !active;
    }
}
