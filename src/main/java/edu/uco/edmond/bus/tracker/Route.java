package edu.uco.edmond.bus.tracker;

import edu.uco.edmond.bus.tracker.Dtos.User;
import java.util.ArrayList;

public class Route {
    
    private ArrayList<RouteStop> stops = new ArrayList<>();
    private User driver;
    private int id;
    private String routeName;
    private String identifier; // used to identify the route when performing bulk operations
    
    public Route() {}
    
    public Route(int id, String routeName) {
        this.id = id;
        this.routeName = routeName;
    }

    public ArrayList<RouteStop> getStops() {
        return stops;
    }

    public void setRoutes(ArrayList<RouteStop> stops) {
        this.stops = stops;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }
    
    public void addStop(RouteStop stop) {
        this.stops.add(stop);
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
    
    public void removeStop(int stopNumber) {
        this.stops.remove(stopNumber);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.routeName;
    }

}
