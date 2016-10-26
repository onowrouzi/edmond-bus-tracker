package edu.uco.edmond.bus.tracker;

import org.primefaces.model.map.LatLng;

public class RouteStop {
    
    private String stopName;
    private LatLng location;
    
    public RouteStop() {}
    
    public RouteStop(String name, double lat, double lng){
        stopName = name;
        location = new LatLng(lat, lng);
    }
    
    public RouteStop(String stopName)
    {
        this.stopName = stopName;
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
    
    public double getStopLat() {
        return this.location.getLat();
    }
    
     public double getStopLng() {
        return this.location.getLng();
    }
     
    public String getLocationString(){
        return location.getLat() + ", " + location.getLng();
    }
}
