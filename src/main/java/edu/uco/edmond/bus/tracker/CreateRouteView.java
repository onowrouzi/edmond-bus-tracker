package edu.uco.edmond.bus.tracker;

import java.io.Serializable;
import javax.annotation.PostConstruct; 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
  
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.OverlaySelectEvent;
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
    
    private double defaultLat = 35.6526783;
    private double defaultLng = -97.4781833;
    private String defaultStopName = "New Stop";
    
    private int currentRouteOrderNumber = 1;
        
    @PostConstruct
    public void init() {
        this.draggableModel = new DefaultMapModel();
    }
      
    public MapModel getDraggableModel() {
        return draggableModel;
    }
      
    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        System.out.println(marker.getUUID());
        
        // if we want to create a growl alert
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Dragged", "Lat:" + marker.getLatlng().getLat() + ", Lng:" + marker.getLatlng().getLng()));
    }
    
    public void addRoute() {
        RouteStop routeStop = new RouteStop();
        LatLng coords = new LatLng(defaultLat, defaultLng);
        this.addMarker(coords);
        routeStop.setStopOnRoute(currentRouteOrderNumber++);
        this.verifyDefaultStopName();
        routeStop.setStopName(defaultStopName);
        routeStop.setLocation(coords);
        this.route.addRoute(routeStop);
    }
//
//    public void onMarkerDragStart(OverlaySelectEvent event) {
//        System.out.println("MARGKGKGKGKGKGKGKGKGKGKGKG");
//        Marker markerBeingDrug = (Marker) event.getOverlay();
//        for (RouteStop r : this.route.getRoutes()) {
//            if (r.getLocation().getLat() == markerBeingDrug.getLatlng().getLat() &&
//                    r.getLocation().getLng() == markerBeingDrug.getLatlng().getLng()) {
//                System.out.println("FOUND IT");
//                System.out.println(r.getStopName());
//            }
//        }
//        System.out.println(markerBeingDrug.getTitle() + "AYyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
//    }
    
    public void onDragEnd() {
        System.out.println("here i am");
    }
    
    public void save() {
        // save logic
    }
    
    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
    
    private void addMarker(LatLng coords) {
        Marker newMarker = new Marker(coords, defaultStopName);
        newMarker.setDraggable(true);
        this.draggableModel.addOverlay(newMarker);
    }
    
    private void checkDefaultName(String nameToTest) {
        
        String defaultNameRegex = "[0-8]";
        String characterToTest = nameToTest.substring(0, nameToTest.length() - 1);
        
        if (characterToTest.matches(defaultNameRegex)) {
            // if not 9
                // convert to int
                // increment
                // remove old number from 
        }
        else {
            // add number to the end of name
        }
    }
}
