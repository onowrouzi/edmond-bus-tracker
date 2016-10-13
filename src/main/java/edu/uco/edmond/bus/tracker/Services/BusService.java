/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.edmond.bus.tracker.Services;

import edu.uco.edmond.bus.tracker.Dtos.Bus;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author omidnowrouzi
 */
@Path("busservice")
public class BusService extends Service {
     private List<Bus> buses;
    
    public BusService() throws SQLException
    {
        this.buses = new ArrayList<>();
        getAllBuses();
    }
    
    public List<Bus> buses()
    {
        return buses;
    }
    
    private void getAllBuses() throws SQLException
    {
        
        Statement stmt = getDatabase().createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM tblbus");

        while(rs.next()){
            Boolean active = false;
            if (rs.getInt("active") > 0) active = true;
            Bus bus = new Bus(rs.getInt("id"), rs.getString("name"), rs.getString("driver"), rs.getString("route"),
                        rs.getString("laststop"), active, rs.getString("lastactive"), rs.getDouble("lastLong"), rs.getDouble("lastLat"));
            buses.add(bus);
        }
        
        stmt.close();
    }
    
    public Bus find(int id)
    {
        for(Bus bus : buses)
        {
            if(bus.getId() == id)
            {
                return bus; //bus found
            }
        }
            
        return null;
    }
    
    public Bus find(String query)
    {
        for(Bus bus : buses)
        {
            if(bus.getName().equals(query) || bus.getDriver().equals(query) || bus.getRoute().equals(query))
            {
                return bus; //bus found
            }
        }
        
        //no stop found
        return null;
    }   
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("buses")
    public String getBusess()
    {
        if(buses.isEmpty())
            return getGson().toJson("No buses currently registered."); // no buses in system
        
        return getGson().toJson(buses);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("buses/byid/{id}")
    public String getBus(@PathParam("id") int id) throws SQLException {
        return getGson().toJson(find(id));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("buses/{query}")
    public String getBus(@PathParam("query") String query) throws SQLException {
        return getGson().toJson(find(query));
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("buses/edit/{oldName}/{newName}/{oldDriver}/{newDriver}/{oldRoute}/{newRoute}")
    public Bus edit(@PathParam("oldName") String oldName, @PathParam("newName") String newName, 
            @PathParam("oldDriver") String oldDriver, @PathParam("newDriver") String newDriver,
                @PathParam("oldRoute") String oldRoute, @PathParam("newRoute") String newRoute)
    {
        return null;
    }
   
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("buses/create/{name}/{driver}/{route}")
    public String create(@PathParam("name") String name, @PathParam("driver") String driver, @PathParam("route") String route)
    {
        name = name.replace("%20", " ");
        driver = driver.replace("%20", " ");
        route = route.replace("%20", " ");
        
        Bus bus = new Bus(); //find(name);
        
        //if(bus != null)
        //    return getGson().toJson(null); //send error message on client --bus exists
        
        try{
            try (PreparedStatement stmt = getDatabase()
                    .prepareStatement("INSERT INTO tblbus (name, driver, route) VALUES(?,?,?)")) {
                stmt.setString(1, name);
                stmt.setString(2, driver);
                stmt.setString(3, route);
                
                System.out.println("STATEMENT 1: " + stmt);
                
                int count = stmt.executeUpdate();
            }

            try ( //get id of new stop
                    PreparedStatement stmt2 = getDatabase().prepareStatement("SELECT id FROM tblbus WHERE name=?")) {
                stmt2.setString(1, name);
                
                System.out.println("STATEMENT 2: " + stmt2);
                
                ResultSet rs = stmt2.executeQuery();
                
                rs.first();
                
                int id = rs.getInt("id");
                bus = new Bus(id,name,driver,route);
                buses.add(bus);
            }
            
        }catch(SQLException s){
            return getGson().toJson(s.toString()); //SQL failed
        }
        
        return getGson().toJson(bus);
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("buses/delete/{name}")
    public String delete(@PathParam("name") String name)
    {
        Bus bus = find(name);
        
        if(bus == null)
            return getGson().toJson(null); //send error message on client --bus does not exist
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("DELETE FROM tblbus WHERE id=?");
            stmt.setInt(1, bus.getId());

            int count = stmt.executeUpdate();
            
            stmt.close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString());
        }
        
        buses.remove(bus);
        
        return getGson().toJson(bus);
    }
}
