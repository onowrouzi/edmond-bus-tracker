package edu.uco.edmond.bus.tracker;

import edu.uco.edmond.bus.tracker.Dtos.BusStop;
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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean
@ViewScoped
public class StopManagementEditBean implements Serializable{
    
    public String mapKey = "https://maps.google.com/maps/api/js?key=AIzaSyAOm_hMAIA4Naz5-FXN7VTmLdMetew_uUE";
    private String ENV = "https://uco-edmond-bus.herokuapp.com/api/busstopservice/stops/update/";
    HttpURLConnection connection = null;
    public MapModel draggableModel;
    public BusStop stop = new BusStop();
    private Marker marker;
    private String initialName;
    private double initialLat;
    private double initialLng;
    
    @PostConstruct
    public void init() {
        initialName = String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("stopname"));
        initialLat = Double.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("stoplat"));
        initialLng = Double.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("stoplng"));
        draggableModel = new DefaultMapModel();
        Marker newMarker = new Marker(new LatLng(initialLat, initialLng), "stop-marker");
        newMarker.setDraggable(true);
        draggableModel.addOverlay(newMarker);
        stop.setName(getInitialName());
        stop.setLat(initialLat);
        stop.setLng(initialLng);
    }
    
    public void save() {
        DataOutputStream wr = null;
        try {
            String routeStopName = stop.getName();
            String lat = String.valueOf(stop.getLat());
            String lng = String.valueOf(stop.getLng());
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
        } catch (IOException ex) {
            Logger.getLogger(CreateRouteView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        stop.setLat(marker.getLatlng().getLat());
        stop.setLng(marker.getLatlng().getLng());
    }
    
    public BusStop getStop() {
        return stop;
    }
    
    public String getMapKey() {
        return mapKey;
    }

    public MapModel getDraggableModel() {
        return draggableModel;
    }
    
    public void setStop(BusStop stop) {
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