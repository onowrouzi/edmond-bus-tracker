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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean
@RequestScoped
public class StopManagementBean implements Serializable {
     
    private ArrayList<RouteStop> stops = new ArrayList<>();
    
    private MapModel draggableModel;
    private Marker marker;
    
    public RouteStop stop = new RouteStop();
    
    private final double defaultLat = 35.6526783;
    private final double defaultLng = -97.4781833;
            
    public String mapKey = "https://maps.google.com/maps/api/js?key=AIzaSyAOm_hMAIA4Naz5-FXN7VTmLdMetew_uUE";// + System.getenv("MAP_API");
        
    HttpURLConnection connection = null;

    
    @PostConstruct
    public void init() {
        
        this.draggableModel = new DefaultMapModel();
        
        Marker newMarker = new Marker(new LatLng(defaultLat, defaultLng), "stop-marker");
        newMarker.setDraggable(true);
        this.draggableModel.addOverlay(newMarker);
        
        this.getStop().setStopName("Stop Name");
        this.getStop().setLocation(new LatLng(this.defaultLat, this.defaultLng));
                
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
    
    public void save() {
        
        DataOutputStream wr = null;
        try {
            String routeStopName = this.getStop().getStopName();
            String lat = String.valueOf(this.getStop().getLocation().getLat());
            String lng = String.valueOf(this.getStop().getLocation().getLng());
            String route = "https://uco-edmond-bus.herokuapp.com/api/busstopservice/stops/create/" + java.net.URLEncoder.encode(routeStopName, "UTF-8") + "/" + lat + "/" + lng;
            URL url = new URL(route);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();

            //print result
        } catch (IOException ex) {
            Logger.getLogger(CreateRouteView.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }
    }
    
    public String delete(String stopName) {
        try {
            String url = "https://uco-edmond-bus.herokuapp.com/api/busstopservice/stops/delete/" + java.net.URLEncoder.encode(stopName, "UTF-8");
            
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

        } catch (IOException ex) {
            Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return "stopManagement";
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        LatLng coords = new LatLng(marker.getLatlng().getLat(), marker.getLatlng().getLng());
        this.getStop().setLocation(coords);
    }
    
    
    public ArrayList<RouteStop> getStops() {
        return this.stops;
    }
    
    public String getMapKey() {
        return this.mapKey;
    }

    public MapModel getDraggableModel() {
        return draggableModel;
    }

    public RouteStop getStop() {
        return stop;
    }

    public void setStop(RouteStop stop) {
        this.stop = stop;
    }
}
