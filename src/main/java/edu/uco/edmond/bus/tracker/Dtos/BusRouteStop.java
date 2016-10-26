
package edu.uco.edmond.bus.tracker.Dtos;

public class BusRouteStop {
    private int id;
    private String route;
    private String stop;
    private int stopOnRoute;
    
    public BusRouteStop(int id, String route, String stop, int stopOnRoute)
    {
        this.id = id;
        this.route = route;
        this.stop = stop;
        this.stopOnRoute = stopOnRoute;
    }
    
    public String getRoute()
    {
        return route;
    }
    
    public String getStop()
    {
        return stop;
    }
}
