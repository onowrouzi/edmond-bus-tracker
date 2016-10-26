package edu.uco.edmond.bus.tracker;

import edu.uco.edmond.bus.tracker.Dtos.BusRoute;
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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct; 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
  
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
  
@ManagedBean
@ViewScoped
public class CreateRouteView implements Serializable {
    
    /**
     * THIS WILL NOT WORK SINCE WE DID THE DATABASE STUFF
     */
    public HtmlPanelGroup buttonHolder;
    
    private MapModel draggableModel;
    private Marker marker;
    
    private BusRoute route;
    
    private final double defaultLat = 35.6526783;
    private final double defaultLng = -97.4781833;
    private final String defaultStopName = "Route";
        
    private int currentRouteOrderNumber = 1;
    
    public String mapKey = "https://maps.google.com/maps/api/js?key=AIzaSyDJCxt0cW1Pqy-RiS84LpVvCQRF04f9C9E";// + System.getenv("MAP_API");
        
    HttpURLConnection connection = null;
    
    // temp since we don't have users implemented yet
    private ArrayList<User> possibleDrivers = new ArrayList<>();
    private ArrayList<BusStop> selectedStops = new ArrayList<>();
    
    @PostConstruct
    public void init() {
        this.draggableModel = new DefaultMapModel();
    }
      
    public MapModel getDraggableModel() {
        return draggableModel;
    }
    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        LatLng coords = new LatLng(marker.getLatlng().getLat(), marker.getLatlng().getLng());

        this.showMessage("Marker Moved","Lat:" + marker.getLatlng().getLat() + ", Lng:" + marker.getLatlng().getLng());
    }
    
    public void addRoute() {

    }
    
    public void delete() {
        // delete logic
    }
    
    public void save() {
        
        for (BusStop s : route.getBusStops()) {
            System.out.println(s.getName());
            DataOutputStream wr = null;
            try {
                String routeStopName = s.getName();
                String lat = String.valueOf(s.getLat());
                String lng = String.valueOf(s.getLng());
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
    
    public BusRoute getRoute() {
        return route;
    }

    public void setRoute(BusRoute route) {
        this.route = route;
    }
    
//    public void addStop(double lat, double lng, String name) {
//        System.out.println("ADDING MARKER");
//        LatLng coords = new LatLng(lat, lng);
//        Marker newMarker = new Marker(coords, name);
//        //newMarker.setDraggable(true);
//        draggableModel.addOverlay(newMarker);
//    }
    
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
        return this.mapKey;
    }
    
    public HtmlPanelGroup getButtonHolder() {
	return buttonHolder;
    }
    
    public void setButtonHolder(HtmlPanelGroup buttonHolder) {
	this.buttonHolder = buttonHolder;
    }
    
    public ArrayList<BusStop> getSelectedStops(){
        return selectedStops;
    }
    
    public void setSelectedStops(ArrayList<BusStop> stops){
        selectedStops = stops;
    }
    
    public void addStop(BusStop s){
        for (int i = 0; i < selectedStops.size(); i++){
            if (selectedStops.get(i).getName().equals(s.getName())){
                selectedStops.remove(i);
                return;
            }
        }
        
        selectedStops.add(s);
    }
}
