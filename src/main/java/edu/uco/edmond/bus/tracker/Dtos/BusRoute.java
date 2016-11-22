package edu.uco.edmond.bus.tracker.Dtos;

import java.util.ArrayList;

public class BusRoute extends Dto {
    
    private int id;
    private int busId;
    private String name;
    private String busName;
    private ArrayList<BusStop> busStops = new ArrayList<>();
    
    public BusRoute(int id, String name){
        this.id = id;
        this.name = name;
    }
    
    public BusRoute(int id, String name, ArrayList<BusStop> stops) {
        this.name = name;
        this.id = id;
        this.busStops = stops;
    }
    
    public BusRoute(int id, String name, String busName, int busId, ArrayList<BusStop> stops){
        this.name = name;
        this.id = id;
        this.busName = busName;
        this.busId = busId;
        this.busStops = stops;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<BusStop> getBusStops() {
        return busStops;
    }
    
    public void setBusName(String busName){
        this.busName = busName;
    }
    
    public String getBusName(){
        return busName;
    } 
    
    public void setBusId(int busId){
        this.busId = busId;
    }
    
    public int getBusId(){
        return busId;
    }
}
