package edu.uco.edmond.bus.tracker;

import java.io.Serializable;
import java.util.UUID;
import javax.annotation.PostConstruct; 
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
  
import org.primefaces.event.map.MarkerDragEvent;
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
    
    private final double defaultLat = 35.6526783;
    private final double defaultLng = -97.4781833;
    private final String defaultStopName = "New Stop";
    
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
        System.out.println(marker.getTitle());
        
        // if we want to create a growl alert
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Dragged", "Lat:" + marker.getLatlng().getLat() + ", Lng:" + marker.getLatlng().getLng()));
    }
    
    public void addRoute() {
        String tempUuid = UUID.randomUUID().toString();
        RouteStop routeStop = new RouteStop();
        LatLng coords = new LatLng(defaultLat, defaultLng);
        this.addMarker(coords, tempUuid);
        routeStop.setStopOnRoute(currentRouteOrderNumber++);
        routeStop.setStopName(defaultStopName);
        routeStop.setLocation(coords);
        routeStop.setIdentifier(tempUuid);
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
    
    public void save() {
        // save logic
    }
    
    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
    
    private void addMarker(LatLng coords, String name) {
        Marker newMarker = new Marker(coords, name);
        newMarker.setDraggable(true);
        this.draggableModel.addOverlay(newMarker);
    }
}
