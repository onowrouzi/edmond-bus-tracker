
package edu.uco.edmond.bus.tracker.Dtos;

public class BusStop extends Dto{
    
    int id;
    String name;
    float latitude;
    float longitude;
    
    public BusStop(int id, String name, float latitude, float longitude)
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
    
    public float getLatitude()
    {
        return latitude;
    }
    
    public float getLongitude()
    {
        return longitude;
    }
}
