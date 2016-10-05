package edu.uco.edmond.bus.tracker;

import org.primefaces.model.map.LatLng;

public class RouteStop {
    
    private String stopName;
    private LatLng location;
    // the number of what stop this is on the route in order
    private int stopOnRoute;
    private String identifier; // used to identify the route when performing bulk operations
    
    private boolean isBeingDrug = false;
    
    public RouteStop() {
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getStopOnRoute() {
        return stopOnRoute;
    }

    public void setStopOnRoute(int stopOnRoute) {
        this.stopOnRoute = stopOnRoute;
    }

    public boolean getIsBeingDrug() {
        return isBeingDrug;
    }

    public void setIsBeingDrug(boolean isBeingDrug) {
        this.isBeingDrug = isBeingDrug;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public double getStopLat() {
        return this.location.getLat();
    }
    
     public double getStopLng() {
        return this.location.getLng();
    }
}