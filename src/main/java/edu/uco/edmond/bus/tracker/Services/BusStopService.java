
package edu.uco.edmond.bus.tracker.Services;

import edu.uco.edmond.bus.tracker.Dtos.BusStop;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("busstopservice")
public class BusStopService extends Service{
    
    private List<BusStop> busStops;
    
    public BusStopService() throws SQLException
    {
        busStops = new ArrayList<>();
        getAllBusStops();
    }
    
    public List<BusStop> busStops()
    {
        return busStops;
    }
    
    private void getAllBusStops()
    {
        try{
        Statement stmt = getDatabase().createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM tblbusstop");

        while(rs.next()){
            BusStop stop = new BusStop(rs.getInt("id"), rs.getString("name"), rs.getFloat("lat"), rs.getFloat("lng"));
            busStops.add(stop);
        }
        
        stmt.close();
        
        //Close out current SQL connection
        getDatabase().close();
        }catch(SQLException s){
            System.out.println(s.toString()); //SQL error
        }
    }
    
    public BusStop find(int id)
    {
        for(BusStop stop : busStops)
        {
            if(stop.getId() == id)
            {
                return stop; //stop found
            }
        }
            
        return null;
    }
    
    public BusStop find(String name)
    {
        for(BusStop stop : busStops)
        {
            if(stop.getName().equals(name))
            {
                return stop; //stop found
            }
        }
        
        //no stop found
        return null;
    }   
        
    public BusStop find(float latitude, float longitude)
    {
        for(BusStop stop : busStops)
        {
            if(stop.getLat() == latitude && stop.getLng() == longitude)
            {
                return stop; //stop found
            }
        }
        
        //no stop found
        return null;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops")
    public String getStops()
    {
        if(busStops.isEmpty())
            return getGson().toJson("No bus stops currently registered."); // no stops in system
        
        return getGson().toJson(busStops);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/byid/{id}")
    public String getStop(@PathParam("id") int id){
        return getGson().toJson(find(id));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/{name}")
    public String getStop(@PathParam("name") String name){
        return getGson().toJson(find(name));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/{latitude}/{longitude}")
    public String getStop(@PathParam("latitude") float latitude, @PathParam("longitude") float longitude){
        return getGson().toJson(find(latitude, longitude));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/create/{name}/{latitude}/{longitude}")
    public String create(@PathParam("name") String name, @PathParam("latitude") float latitude, @PathParam("longitude") float longitude)
    {
        try {
            name = java.net.URLDecoder.decode(name, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BusStopService.class.getName()).log(Level.SEVERE, null, ex);
        }
        BusStop Stop = find(name);
        
        if(Stop != null)
            return getGson().toJson(null); //send error message on client --stop exists
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("INSERT INTO tblbusstop (name, lat, lng) VALUES(?,?,?)");
            stmt.setString(1, name);
            stmt.setFloat(2, latitude);
            stmt.setFloat(3, longitude);

            int count = stmt.executeUpdate();
            
            stmt.close();

            //get id of new stop
            PreparedStatement stmt2 = getDatabase().prepareStatement("SELECT id FROM tblbusstop WHERE name=?");
            stmt2.setString(1, name);

            ResultSet rs = stmt2.executeQuery();
            
            rs.first();

            int id = rs.getInt("id");
            Stop = new BusStop(id,name,latitude,longitude);
            busStops.add(Stop);  
            
            stmt2.close();
            
            //Close out current SQL connection
            getDatabase().close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString()); //SQL failed
        }
        
        return getGson().toJson(Stop);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/delete/{name}")
    public String delete(@PathParam("name") String name)
    {
        BusStop Stop = null;
        try {
            Stop = find(java.net.URLDecoder.decode(name, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BusStopService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(Stop == null)
            return getGson().toJson(null); //send error message on client --stop does not exist
        
        try{
            PreparedStatement stmt1 = getDatabase().prepareStatement("UPDATE tblbus SET laststop=null where laststop=?");
            stmt1.setString(1, Stop.getName());
            int count = stmt1.executeUpdate();
            stmt1.close();
            PreparedStatement stmt2 = getDatabase().prepareStatement("DELETE from tblbusroutestop where stop=?");
            stmt2.setString(1, Stop.getName());
            count = stmt2.executeUpdate();
            stmt2.close();
            PreparedStatement stmt3 = getDatabase().prepareStatement("DELETE from tblbusstopfavorites where busstopId=?");
            stmt3.setInt(1, Stop.getId());
            count = stmt3.executeUpdate();
            stmt3.close();
            PreparedStatement stmt4 = getDatabase().prepareStatement("DELETE FROM tblbusstop WHERE id=?");
            stmt4.setInt(1, Stop.getId());
            count = stmt4.executeUpdate();
            stmt4.close();
            
            //Close out current SQL connection
            getDatabase().close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString());
        }
        
        busStops.remove(Stop);
        
        return getGson().toJson(Stop);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/update/{name}/{lat}/{lng}")
    public String update(@PathParam("name") String name,@PathParam("lat") float lat, @PathParam("lng") float lng)
    {
        BusStop Stop = null;
        try {
            Stop = find(java.net.URLDecoder.decode(name, "UTF-8"));
            System.out.println(Stop.getName());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BusStopService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(Stop == null)
            return getGson().toJson(null); //send error message on client --stop does not exist
        
        Stop.setLat(lat);
        Stop.setLng(lng);
        
        try{
            PreparedStatement stmt1 = getDatabase().prepareStatement("UPDATE tblbusstop set lat=?, lng=? WHERE id=?");
            stmt1.setFloat(1, lat);
            stmt1.setFloat(2, lng);
            stmt1.setInt(3, Stop.getId());
            int count = stmt1.executeUpdate();
            stmt1.close();
            
            //Close out current SQL connection
            getDatabase().close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString());
        }
        
        busStops.remove(Stop);
        
        return getGson().toJson(Stop);
    }
}
