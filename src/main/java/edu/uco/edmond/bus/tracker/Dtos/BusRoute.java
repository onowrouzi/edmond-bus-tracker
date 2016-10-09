package edu.uco.edmond.bus.tracker.Dtos;

import java.util.ArrayList;

public class BusRoute extends Dto {
    
    private int id;
    private String name;
    private ArrayList<BusStop> busStops;
    
    public BusRoute(int id, String name, ArrayList<BusStop> stops) {
        this.name = name;
        this.id = id;
        this.busStops = new ArrayList<>(stops);
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
    
}
