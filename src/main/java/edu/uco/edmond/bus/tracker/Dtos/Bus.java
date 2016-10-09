package edu.uco.edmond.bus.tracker.Dtos;

public class Bus extends Dto {
    
    private int id;
    private String name;
    private String driver;
    private String route;
    
    public Bus(int id, String name, String driver, String route) {
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

    public String getDriver() {
        return driver;
    }

    public String getRoute() {
        return route;
    }
}
