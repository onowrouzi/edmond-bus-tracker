/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.edmond.bus.tracker.Services;

import edu.uco.edmond.bus.tracker.Dtos.Bus;
import edu.uco.edmond.bus.tracker.Route;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author omidnowrouzi
 */
@Path("routeservice")
public class RouteService extends Service {
     private List<Route> routes;
    
    public RouteService() throws SQLException
    {
        this.routes = new ArrayList<>();
        getAllBuses();
    }
    
    public List<Route> routes()
    {
        return routes;
    }
    
    private void getAllBuses() throws SQLException
    {
        
        Statement stmt = getDatabase().createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM tblbusroute");

        while(rs.next()){
            Route route = new Route(rs.getInt("id"), rs.getString("name"));
            routes.add(route);
        }
        
        stmt.close();
    }
    
    public Route find(int id)
    {
        for(Route route : routes)
        {
            if(route.getId() == id)
            {
                return route; //route found
            }
        }
            
        return null;
    }
    
    public Route find(String query)
    {
        for(Route route : routes)
        {
            if(route.getName().equals(query))
            {
                return route; //route found
            }
        }
        
        //no route found
        return null;
    }   
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("routes")
    public String getBusess()
    {
        if(routes.isEmpty())
            return getGson().toJson("No buses currently registered."); // no buses in system
        
        return getGson().toJson(routes);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("routes/byid/{id}")
    public String getRoute(@PathParam("id") int id) throws SQLException {
        return getGson().toJson(find(id));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("routes/{query}")
    public String getRoute(@PathParam("query") String query) throws SQLException {
        return getGson().toJson(find(query));
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("routes/edit/{oldName}/{newName}")
    public Bus edit(@PathParam("oldName") String oldName, @PathParam("newName") String newName)
    {
        return null;
    }
   
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("routes/create/{name}")
    public String create(@PathParam("name") String name)
    {
        Route route = find(name);
        
        if(route != null)
            return getGson().toJson(null); //send error message on client --bus exists
        
        try{
            try (PreparedStatement stmt = getDatabase()
                    .prepareStatement("INSERT INTO tblbusroute (name) VALUES(?)")) {
                stmt.setString(1, name);
                
                System.out.println("STATEMENT 1: " + stmt);
                
                int count = stmt.executeUpdate();
            }

            try ( //get id of new stop
                    PreparedStatement stmt2 = getDatabase().prepareStatement("SELECT id FROM tblbusroute WHERE name=?")) {
                stmt2.setString(1, name);
                
                System.out.println("STATEMENT 2: " + stmt2);
                
                ResultSet rs = stmt2.executeQuery();
                
                rs.first();
                
                int id = rs.getInt("id");
                route = new Route(id,name);
                routes.add(route);
            }
            
        }catch(SQLException s){
            return getGson().toJson(s.toString()); //SQL failed
        }
        
        return getGson().toJson(route);
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("routes/delete/{name}")
    public String delete(@PathParam("name") String name)
    {
        Route route = find(name);
        
        if(route == null)
            return getGson().toJson(null); //send error message on client --bus does not exist
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("DELETE FROM tblbusroute WHERE id=?");
            stmt.setInt(1, route.getId());

            int count = stmt.executeUpdate();
            
            stmt.close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString());
        }
        
        routes.remove(route);
        
        return getGson().toJson(route);
    }
}

