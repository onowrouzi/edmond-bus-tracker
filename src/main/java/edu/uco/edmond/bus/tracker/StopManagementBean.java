package edu.uco.edmond.bus.tracker;

import edu.uco.edmond.bus.tracker.Dtos.BusStop;
import edu.uco.edmond.bus.tracker.Dtos.User;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
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
public class StopManagementBean implements Serializable {
     
    private ArrayList<BusStop> stops;
    public ArrayList<BusStop> filteredStops;
    private String name;
    private String ENV = "https://uco-edmond-bus.herokuapp.com/api/busstopservice/stops";
    private MapModel draggableModel;
    private Marker marker;
    private final double defaultLat = 35.6526783;
    private final double defaultLng = -97.4781833;
    private double lat, lng;
    
    public BusStop stop;
    public String mapKey = "https://maps.google.com/maps/api/js?key=AIzaSyAOm_hMAIA4Naz5-FXN7VTmLdMetew_uUE";// + System.getenv("MAP_API");
        
    HttpURLConnection connection = null;
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    
    @PostConstruct
    public void init() {
        stops = new ArrayList();
        try {
            URL obj = new URL(ENV);
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
                    int id = jsonobject.getInt("id");
                    String name = java.net.URLDecoder.decode(jsonobject.getString("name"), "UTF-8");
                    Double lat = jsonobject.getDouble("latitude");
                    Double lng = jsonobject.getDouble("longitude");
                    BusStop temp = new BusStop(id, name, lat, lng);
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
        
        draggableModel = new DefaultMapModel();
        
        for (BusStop s: stops){
            draggableModel.addOverlay(new Marker(new LatLng(s.getLat(), s.getLng()), s.getName()));
        }
        
        filteredStops = stops;
    }
    
    public void save(String name, double lat, double lng) {
        
        DataOutputStream wr = null;
        try {
            String route = ENV + "/create/" + java.net.URLEncoder.encode(name, "UTF-8") 
                    + "/" + String.valueOf(lat) + "/" + String.valueOf(lng);
            
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
            Logger.getLogger(StopManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }
    }
    
    public String delete(String stopName) throws IOException {
        System.out.println("Name: " + stopName);
        
        try {
            String url = ENV + "/delete/" + java.net.URLEncoder.encode(stopName, "UTF-8");
            
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
        
        init();
        
        context.redirect("stopManagement.xhtml");
        
        return "Stop was deleted!";
    }

//    public void onMarkerDrag(MarkerDragEvent event) {
//        marker = event.getMarker();
//        LatLng coords = new LatLng(marker.getLatlng().getLat(), marker.getLatlng().getLng());
//        this.getStop().setLocation(coords);
//    }
    
    
    public ArrayList<BusStop> getStops() {
        return stops;
    }
    
    public String getMapKey() {
        return mapKey;
    }

    public MapModel getDraggableModel() {
        return draggableModel;
    }

    public BusStop getStop() {
        return stop;
    }

    public void setStop(BusStop stop) {
        this.stop = stop;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
  
    public double getLat() {
        return lat;
    }
  
    public void setLat(double lat) {
        this.lat = lat;
    }
  
    public double getLng() {
        return lng;
    }
  
    public void setLng(double lng) {
        this.lng = lng;
    }
      
    public void addMarker(String name) {
        Marker newMarker = new Marker(new LatLng(lat, lng), name);
        draggableModel.addOverlay(newMarker);
                
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Name: " + name + "\nLat:" + lat + ", Lng:" + lng));

        save(name, lat, lng);          
    }

    public ArrayList<BusStop> getFilteredStops() {
        return filteredStops;
    }

    public void setFilteredStops(ArrayList<BusStop> filteredStops) {
        this.filteredStops = filteredStops;
    }
}
