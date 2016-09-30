package edu.uco.edmond.bus.tracker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import static java.lang.System.out;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
    private final String defaultStopName = "Route";
        
    private int currentRouteOrderNumber = 1;
    
    public String mapKey = "http://maps.google.com/maps/api/js?key=" + System.getenv("MAP_API");
        
    HttpURLConnection connection = null;
    
    // temp since we don't have users implemented yet
    private ArrayList<User> possibleDrivers = new ArrayList<>();
        
    @PostConstruct
    public void init() {
        this.draggableModel = new DefaultMapModel();
        getPossibleDrivers().add(new User(1, "John Doe", "asdf", "driver"));
        getPossibleDrivers().add(new User(2, "Jane Doe", "asdf", "driver"));
    }
      
    public MapModel getDraggableModel() {
        return draggableModel;
    }
      
    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        LatLng coords = new LatLng(marker.getLatlng().getLat(), marker.getLatlng().getLng());
        
        for (RouteStop r : this.route.getRoutes()) {
            if (r.getIdentifier().equals(marker.getTitle())) {
                r.setLocation(coords);
            }
        }
        this.showMessage("Marker Moved","Lat:" + marker.getLatlng().getLat() + ", Lng:" + marker.getLatlng().getLng());
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
    
    public void delete() {
        // delete logic
    }
    
    public void save() {
        
        for (RouteStop r : this.route.getRoutes()) {
            System.out.println(r.getStopName());
            DataOutputStream wr = null;
            try {
                String routeStopName = r.getStopName();
                String lat = String.valueOf(r.getLocation().getLat());
                String lng = String.valueOf(r.getLocation().getLng());
                String route = "https://uco-edmond-bus.herokuapp.com/api/busservice/stops/create/" + routeStopName + "/" + lat + "/" + lng;
                System.out.println(route);
                URL url = new URL(route);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                
                int responseCode = connection.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
 
            } catch (IOException ex) {
                Logger.getLogger(CreateRouteView.class.getName()).log(Level.SEVERE, null, ex);
            } finally {

            }
        }
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
    
    private void showMessage(String heading, String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(heading, message));
    }

    public ArrayList<User> getPossibleDrivers() {
        return possibleDrivers;
    }

    public void setPossibleDrivers(ArrayList<User> possibleDrivers) {
        this.possibleDrivers = possibleDrivers;
    }
    
    public String getMapKey() {
        return "http://maps.google.com/maps/api/js?key=AIzaSyDKLZuNqtEa-sc6hfNy9Rf3YUPE68WNnj0";
    }

}
