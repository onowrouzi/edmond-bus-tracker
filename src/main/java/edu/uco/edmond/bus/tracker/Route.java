package edu.uco.edmond.bus.tracker;

import edu.uco.edmond.bus.tracker.Dtos.User;
import java.util.ArrayList;

public class Route {
    
    private ArrayList<RouteStop> routes = new ArrayList<>();
    private User driver;
    private String routeName;
    private String identifier; // used to identify the route when performing bulk operations
    
    public Route() {}

    public ArrayList<RouteStop> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<RouteStop> routes) {
        this.routes = routes;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }
    
    public void addRoute(RouteStop route) {
        this.routes.add(route);
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
    
    public void removeStop(int stopNumber) {
        this.routes.remove(stopNumber);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
