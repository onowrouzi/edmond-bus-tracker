
package edu.uco.edmond.bus.tracker.Dtos;

public class BusRouteStop {
    private int id;
    private String route;
    private String stop;
    private int stopOnRoute;
    
    public BusRouteStop() {}
    
    public BusRouteStop(String stop){
        this.stop = stop;
    }
    
    public BusRouteStop(String route, String stop){
        this.route = route;
        this.stop = stop;
    }
    
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
    
    public int getStopOnRoute(){
        return stopOnRoute;
    }
    
    public void setRoute(String route){
        this.route = route;
    }
    
    public void setStop(String stop){
        this.stop = stop;
    }
    
    public void setStopOnRoute(int stopOnRoute){
        this.stopOnRoute = stopOnRoute;
    }
}
