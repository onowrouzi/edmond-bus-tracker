package edu.uco.edmond.bus.tracker;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean
@RequestScoped
public class StopManagementEditBean implements Serializable{
    
    public String mapKey = "https://maps.google.com/maps/api/js?key=AIzaSyAOm_hMAIA4Naz5-FXN7VTmLdMetew_uUE";
    private MapModel draggableModel;
    public RouteStop stop = new RouteStop();
    private Marker marker;
    
    private String initialName;
    private double initialLat;
    private double initialLng;
    
    @PostConstruct
    public void init() {
        this.draggableModel = new DefaultMapModel();
        
        System.out.println(this.initialName);
        System.out.println(this.initialLat);
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
        
    }
    
    public void onMarkerDrag(MarkerDragEvent event) {
        System.out.println(marker.getLatlng().getLat());
        System.out.println(marker.getLatlng().getLng());
        marker = event.getMarker();
        LatLng coords = new LatLng(marker.getLatlng().getLat(), marker.getLatlng().getLng());
        this.getStop().setLocation(coords);
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
