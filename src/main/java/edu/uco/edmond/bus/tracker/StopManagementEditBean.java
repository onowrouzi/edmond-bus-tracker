package edu.uco.edmond.bus.tracker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean
@RequestScoped
public class StopManagementEditBean implements Serializable{
    
    public String mapKey = "https://maps.google.com/maps/api/js?key=AIzaSyAOm_hMAIA4Naz5-FXN7VTmLdMetew_uUE";
    private String ENV = "https://uco-edmond-bus.herokuapp.com/api/busstopservice/stops/update/";
    HttpURLConnection connection = null;
    private MapModel draggableModel;
    public RouteStop stop = new RouteStop();
    private Marker marker;
    
    @ManagedProperty(value = "#{param.stopname}")
    private String initialName;
    @ManagedProperty(value = "#{param.stoplat}")
    private double initialLat;
    @ManagedProperty(value = "#{param.stoplng}")
    private double initialLng;
    
    @PostConstruct
    public void init() {
        this.draggableModel = new DefaultMapModel();
        Marker newMarker = new Marker(new LatLng(this.getInitialLat(), this.getInitialLng()), "stop-marker");
        newMarker.setDraggable(true);
        this.draggableModel.addOverlay(newMarker);
        this.getStop().setStopName(this.getInitialName());
        this.getStop().setLocation(new LatLng(this.getInitialLat(), this.getInitialLng()));
    }
    
    public void setInitialState(String name, double lat, double lng) {
        this.setInitialName(name);
        this.setInitialLat(lat);
        this.setInitialLng(lng);
    }
    
    public void save() {
        DataOutputStream wr = null;
        try {
            String routeStopName = this.getStop().getStopName();
            String lat = String.valueOf(this.getStop().getLocation().getLat());
            String lng = String.valueOf(this.getStop().getLocation().getLng());
            String route = ENV + java.net.URLEncoder.encode(routeStopName, "UTF-8") + "/" + lat + "/" + lng;
            
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
    
    public void onMarkerDrag(MarkerDragEvent event) {
        System.out.println("THE FIRST");
        this.marker = event.getMarker();
        System.out.println("THE 2nnnnnnnn");
        LatLng coords = new LatLng(this.marker.getLatlng().getLat(), this.marker.getLatlng().getLng());
        System.out.println("THE 33333333333333333333");
        this.getStop().setLocation(coords);
        System.out.println("THE 44444444444444444444");
    }
    
    public RouteStop getStop() {
        return this.stop;
    }
    
    public String getMapKey() {
        return this.mapKey;
    }

    public MapModel getDraggableModel() {
        return draggableModel;
    }
    
    public void setStop(RouteStop stop) {
        this.stop = stop;
    }

    public String getInitialName() {
        return initialName;
    }

    public void setInitialName(String initialName) {
        this.initialName = initialName;
    }

    public double getInitialLat() {
        return initialLat;
    }

    public void setInitialLat(double initialLat) {
        this.initialLat = initialLat;
    }

    public double getInitialLng() {
        return initialLng;
    }

    public void setInitialLng(double initialLng) {
        this.initialLng = initialLng;
    }
}
