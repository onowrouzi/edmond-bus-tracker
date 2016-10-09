
package edu.uco.edmond.bus.tracker.Services;

import edu.uco.edmond.bus.tracker.Dtos.BusStop;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("busservice")
public class BusStopService extends Service{
    
    private List<BusStop> busStops;
    
    public BusStopService() throws SQLException
    {
        this.busStops = new ArrayList<>();
        getAllBusStops();
    }
    
    public List<BusStop> busStops()
    {
        return busStops;
    }
    
    private void getAllBusStops() throws SQLException
    {
        
        Statement stmt = getDatabase().createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM tblbusstops");

        while(rs.next()){
            BusStop stop = new BusStop(rs.getInt("id"), rs.getString("name"), rs.getFloat("latitude"), rs.getFloat("longitude"));
            busStops.add(stop);
        }
        
        stmt.close();
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
            if(stop.getLatitude() == latitude && stop.getLongitude() == longitude)
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
    public String getStop(@PathParam("id") int id) throws SQLException {
        return getGson().toJson(find(id));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/{name}")
    public String getStop(@PathParam("name") String name) throws SQLException {
        return getGson().toJson(find(name));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/{latitude}/{longitude}")
    public String getStop(@PathParam("latitude") float latitude, @PathParam("longitude") float longitude) throws SQLException {
        return getGson().toJson(find(latitude, longitude));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/edit/{oldName}/{newName}/{oldLatitude}/{newLatitude}/{oldLongitude}/{newLongitude}")
    public BusStop edit(@PathParam("oldName") String oldName, @PathParam("newName") String newName, 
            @PathParam("oldLatitude") float oldLatitude, @PathParam("newLatitude") float newLatitude,
                @PathParam("oldLongitude") float oldLongitude, @PathParam("newLongitude") float newLongitude)
    {
        return null;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stops/create/{name}/{latitude}/{longitude}")
    public String create(@PathParam("name") String name, @PathParam("latitude") float latitude, @PathParam("longitude") float longitude)
    {
        BusStop Stop = find(name);
        
        if(Stop != null)
            return getGson().toJson(null); //send error message on client --stop exists
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("INSERT INTO tblbusstops (name, latitude, longitude) VALUES(?,?,?)");
            stmt.setString(1, name);
            stmt.setFloat(2, latitude);
            stmt.setFloat(3, longitude);

            int count = stmt.executeUpdate();
            
            stmt.close();

            //get id of new stop
            PreparedStatement stmt2 = getDatabase().prepareStatement("SELECT id FROM tblbusstops WHERE name=?");
            stmt2.setString(1, name);

            ResultSet rs = stmt2.executeQuery();
            
            rs.first();

            int id = rs.getInt("id");
            Stop = new BusStop(id,name,latitude,longitude);
            busStops.add(Stop);  
            
            stmt2.close();
            
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
        BusStop Stop = find(name);
        
        if(Stop == null)
            return getGson().toJson(null); //send error message on client --stop does not exist
        
        try{
            PreparedStatement stmt = getDatabase().prepareStatement("DELETE FROM tblbusstops WHERE id=?");
            stmt.setInt(1, Stop.getId());

            int count = stmt.executeUpdate();
            
            stmt.close();
            
        }catch(SQLException s){
            return getGson().toJson(s.toString());
        }
        
        busStops.remove(Stop);
        
        return getGson().toJson(Stop);
    }
}
