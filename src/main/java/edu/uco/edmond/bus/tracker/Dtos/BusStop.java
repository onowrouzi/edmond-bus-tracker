
package edu.uco.edmond.bus.tracker.Dtos;

public class BusStop extends Dto{
    
    int id;
    String name;
    private float latitude;
    private float longitude;
    
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

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
