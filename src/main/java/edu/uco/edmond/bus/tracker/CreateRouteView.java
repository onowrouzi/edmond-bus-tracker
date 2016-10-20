package edu.uco.edmond.bus.tracker;

import edu.uco.edmond.bus.tracker.Dtos.User;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct; 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
  
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
  
@ManagedBean
@ViewScoped
public class CreateRouteView implements Serializable {

    private MapModel draggableModel;
    private Marker marker;
    
    private Route route = new Route();
    private ArrayList<RouteStop> stops = new ArrayList<>();
    private ArrayList<RouteStop> stopOnRoute = new ArrayList<>();
    private int currentStopOnRoute = 1;
        
    private int currentRouteOrderNumber = 1;
    
    public String mapKey = "https://maps.google.com/maps/api/js?key=" + System.getenv("MAP_API");
        
    HttpURLConnection connection = null;
            
    @PostConstruct
    public void init() {
        try {
            String url = "https://uco-edmond-bus.herokuapp.com/api/busstopservice/stops";
            
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            // optional default is GET
            con.setRequestMethod("GET");
            
            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            int responseCode = con.getResponseCode();
            
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            //print result
            JSONArray jsonarray;
            try {
                jsonarray = new JSONArray(response.toString());
                for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject;
                try {
                    jsonobject = jsonarray.getJSONObject(i);
                    String id = jsonobject.getString("id");
                    String name = java.net.URLDecoder.decode(jsonobject.getString("name"), "UTF-8");
                    Double lat = jsonobject.getDouble("latitude");
                    Double lng = jsonobject.getDouble("longitude");
                    RouteStop temp = new RouteStop();
                    temp.setStopName(name);
                    temp.setLocation(new LatLng(lat, lng));
                    stops.add(temp);
                } catch (JSONException ex) {
                    Logger.getLogger(RouteManagement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            } catch (JSONException ex) {
                Logger.getLogger(RouteManagement.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(RouteManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<RouteStop> getStops() {
        return this.stops;
    }
    
    public ArrayList<RouteStop> getStopsOnRoute() {
        return this.stopOnRoute;
    }
    
    public void addToRoute(String name) {
        for (RouteStop r : this.stops) {
            if (r.getStopName().equals(name)) {
                this.stopOnRoute.add(r);
            }
        }
    }
    
    public void removeFromRoute(String name) {
        System.out.println(name);
    }
}