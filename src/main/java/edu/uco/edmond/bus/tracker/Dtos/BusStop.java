
package edu.uco.edmond.bus.tracker.Dtos;

import org.primefaces.model.map.LatLng;

public class BusStop extends Dto{
    
    int id;
    String name;
    double latitude;
    double longitude;
    
    public BusStop(){}
    
    public BusStop(String name){
        this.name = name;
    }
    
    public BusStop(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public BusStop(int id, String name, double latitude, double longitude)
    {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public int getId()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public double getLat() {
        return latitude;
    }
    
     public double getLng() {
        return longitude;
    }

}
