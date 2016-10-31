
package edu.uco.edmond.bus.tracker.Services;

import edu.uco.edmond.bus.tracker.Dtos.BusRouteStop;
import edu.uco.edmond.bus.tracker.Dtos.BusRouteStop;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("busroutestopservice")
public class BusRouteStopService extends Service{
    
    private List<BusRouteStop> busRouteStops;
    
    public BusRouteStopService() throws SQLException
    {
        busRouteStops = new ArrayList<>();
        getAllBusRouteStops();
    }
    
    public List<BusRouteStop> busRouteStops()
    {
        return busRouteStops;
    }
    
    private void getAllBusRouteStops()
    {
        try{
        Statement stmt = getDatabase().createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM tblbusroutestop");

        while(rs.next()){
            BusRouteStop stop = new BusRouteStop(rs.getInt("id"), rs.getString("route"), rs.getString("stop"), rs.getInt("stopOnRoute"));
            busRouteStops.add(stop);
        }
        
        stmt.close();
        
        //Close out current SQL connection
        getDatabase().close();
        }catch(SQLException s){
            System.out.println(s.toString()); //SQL error
        }
    }
    
    public BusRouteStop find(int id)
    {
        for(BusRouteStop stop : busRouteStops)
        {
            if(stop.getId() == id)
            {
                return stop; //stop found
            }
        }
            
        return null;
    }
    
    public BusRouteStop find(String route, String stop)
    {
        for(BusRouteStop bsr : busRouteStops)
        {
            if(bsr.getStop().equals(stop) && bsr.getRoute().equals(route))
            {
                return bsr; //stop found
            }
        }
        //no stop found
        return null;
    } 
    
    public ArrayList<BusRouteStop> findAllForRoute(String route){
        ArrayList<BusRouteStop> bsrList = new ArrayList<>();
        
        for (BusRouteStop bsr: busRouteStops){
            if (bsr.getRoute().equals(route)){
                bsrList.add(bsr);
            }
        }
        
        Collections.sort(bsrList, new Comparator<BusRouteStop>(){
            @Override
            public int compare(BusRouteStop o1, BusRouteStop o2)
            {
                return o1.getStopOnRoute() - o2.getStopOnRoute();
            }
        });
        
        return bsrList;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops")
    public String getStops()
    {
        if(busRouteStops.isEmpty())
            return getGson().toJson("No bus stops currently registered."); // no stops in system
        
        return getGson().toJson(busRouteStops);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/byid/{id}")
    public String getStop(@PathParam("id") int id){
        return getGson().toJson(find(id));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/{route}")
    public String getStopsForRoute(@PathParam("route") String route){
        return getGson().toJson(findAllForRoute(route));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/{route}/{stop}")
    public String getStop(@PathParam("route") String route, @PathParam("stop") String stop){
        return getGson().toJson(find(route, stop));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/create/{route}/{stop}/{stopOnRoute}")
    public String create(@PathParam("route") String route, @PathParam("stop") String stop, @PathParam("stopOnRoute") int stopOnRoute)
    {
        try {
            route = java.net.URLDecoder.decode(route, "UTF-8");
            stop = java.net.URLDecoder.decode(stop, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BusRouteStopService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        BusRouteStop bsr = find(route, stop);
        
        if(bsr != null)
            return getGson().toJson(null); //send error message on client --stop exists
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("INSERT INTO tblbusroutestop (route, stop, stoponroute) VALUES(?,?,?)");
            stmt.setString(1, route);
            stmt.setString(2, stop);
            stmt.setInt(3, stopOnRoute);

            int count = stmt.executeUpdate();
            
            stmt.close();

            //get id of new stop
            PreparedStatement stmt2 = getDatabase().prepareStatement("SELECT id FROM tblbusroutestop WHERE stop=? AND route=?");
            stmt2.setString(1, stop);
            stmt2.setString(2, route);
            
            ResultSet rs = stmt2.executeQuery();
            
            rs.first();

            int id = rs.getInt("id");
            bsr = new BusRouteStop(id,route,stop,stopOnRoute);
            busRouteStops.add(bsr);  
            
            stmt2.close();
            
            //Close out current SQL connection
            getDatabase().close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString()); //SQL failed
        }
        
        return getGson().toJson(bsr);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/delete/{route}/{stop}")
    public String delete(@PathParam("route") String route, @PathParam("stop") String stop)
    {
        BusRouteStop bsr = null;
        try {
            bsr = find(java.net.URLDecoder.decode(route, "UTF-8"), java.net.URLDecoder.decode(stop, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BusRouteStopService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(bsr == null)
            return getGson().toJson(null); //send error message on client --stop does not exist
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("DELETE FROM tblbusroutestop WHERE id=?");
            stmt.setInt(1, bsr.getId());
            int count = stmt.executeUpdate();
            stmt.close();
            
            //Close out current SQL connection
            getDatabase().close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString());
        }
        
        busRouteStops.remove(bsr);
        
        return getGson().toJson(bsr);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/update/{route}/{stop}/{stopOnRoute}")
    public String update(@PathParam("route") String route,@PathParam("stop") String stop, @PathParam("stopOnRoute") int stopOnRoute)
    {
        BusRouteStop bsr = null;
        try {
            bsr = find(java.net.URLDecoder.decode(route, "UTF-8"), java.net.URLDecoder.decode(stop, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BusRouteStopService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(bsr == null)
            return getGson().toJson(null); //send error message on client --stop does not exist
        
        bsr.setRoute(route);
        bsr.setStop(stop);
        bsr.setStopOnRoute(stopOnRoute);
        
        try{
            PreparedStatement stmt1 = getDatabase().prepareStatement("UPDATE tblbusroutestop set route=?, stop=?, stoponroute=? WHERE id=?");
            stmt1.setString(1, route);
            stmt1.setString(2, stop);
            stmt1.setInt(3, stopOnRoute);
            stmt1.setInt(4, bsr.getId());
            int count = stmt1.executeUpdate();
            stmt1.close();
            
            //Close out current SQL connection
            getDatabase().close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString());
        }
        
        busRouteStops.remove(bsr);
        
        return getGson().toJson(bsr);
    }
}
