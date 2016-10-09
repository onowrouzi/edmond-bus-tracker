package edu.uco.edmond.bus.tracker.Dtos;

public class Bus extends Dto {
    
    private int id;
    private String name;
    private User driver;
    private BusRoute route;
    
    public Bus(int id, String name, User driver, BusRoute route) {
        this.id = id;
        this.name = name;
        this.driver = driver;
        this.route = route;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getDriver() {
        return driver;
    }

    public BusRoute getRoute() {
        return route;
    }
}
