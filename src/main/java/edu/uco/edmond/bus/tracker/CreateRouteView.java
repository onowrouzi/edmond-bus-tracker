package edu.uco.edmond.bus.tracker;

import edu.uco.edmond.bus.tracker.Dtos.BusRoute;
import edu.uco.edmond.bus.tracker.Dtos.BusRouteStop;
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
    private BusStop selectedStop;
    
    private final double defaultLat = 35.6526783;
    private final double defaultLng = -97.4781833;
    private final String defaultStopName = "Route";
        
    private int currentRouteOrderNumber = 1;
    
    public String mapKey = "https://maps.google.com/maps/api/js?key=AIzaSyDJCxt0cW1Pqy-RiS84LpVvCQRF04f9C9E";// + System.getenv("MAP_API");
        
    HttpURLConnection connection = null;
    
    // temp since we don't have users implemented yet
    private ArrayList<User> possibleDrivers = new ArrayList<>();
    private ArrayList<BusRouteStop> selectedStops = new ArrayList<>();
    
    @PostConstruct
    public void init() {
        draggableModel = new DefaultMapModel();
    }
      
    public MapModel getDraggableModel() {
        return draggableModel;
    }
    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        LatLng coords = new LatLng(marker.getLatlng().getLat(), marker.getLatlng().getLng());

        showMessage("Marker Moved","Lat:" + marker.getLatlng().getLat() + ", Lng:" + marker.getLatlng().getLng());
    }
    
    public void delete() {
        // delete logic
    }

    public String addRoute(String name) throws IOException {

        try {
            String url = "https://uco-edmond-bus.herokuapp.com/api/routeservice/routes/create/" + name;
            url = url.replace(" ", "%20");
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("POST");

            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            if (responseCode != 200) {
                System.out.println("CONNECTION ERROR: " + con.getErrorStream());
            }

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println("INPUT STREAM: " + response);
            }
            con.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(RouteManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("SIZE OF SELECTED STOPS: " + selectedStops.size());
        for (BusRouteStop brs: selectedStops){
            try {
            String url = "https://uco-edmond-bus.herokuapp.com/api/busroutestopservice/stops/create/" 
                    + name + "/" + brs.getStop()  + "/" + brs.getStopOnRoute();
            url = url.replace(" ", "%20");
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("POST");

            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            if (responseCode != 200) {
                System.out.println("CONNECTION ERROR: " + con.getErrorStream());
            }

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println("INPUT STREAM: " + response);
            }
                con.disconnect();
            } catch (IOException ex) {
                Logger.getLogger(RouteManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return "routeManagement";
    }
    
    public BusRoute getRoute() {
        return route;
    }

    public void setRoute(BusRoute route) {
        this.route = route;
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
        return mapKey;
    }
    
    public HtmlPanelGroup getButtonHolder() {
	return buttonHolder;
    }
    
    public void setButtonHolder(HtmlPanelGroup buttonHolder) {
	this.buttonHolder = buttonHolder;
    }
    
    public ArrayList<BusRouteStop> getSelectedStops(){
        return selectedStops;
    }
    
    public void setSelectedStops(ArrayList<BusRouteStop> selectedStops){
        this.selectedStops = selectedStops;
    }
    
    public void addStop(BusStop s){
        for (int i = 0; i < selectedStops.size(); i++){
            if (selectedStops.get(i).getStop().equals(s.getName())){
                selectedStops.remove(i);
                return;
            }
        }
        
        selectedStops.add(new BusRouteStop(s.getName()));
    }
    
    public void setStopOnRoute(BusStop s, int index){
        for (int i = 0; i < selectedStops.size(); i++){
            if (selectedStops.get(i).getStop().equals(s.getName())){
                selectedStops.get(i).setStopOnRoute(index);
                System.out.println(selectedStops.get(i).getStop() + " stopOnRoute = " + 
                        selectedStops.get(i).getStopOnRoute());
                return;
            }
        }
    }
    
    public void setSelectedStop(BusStop s){
        selectedStop = s;
    }
    
    public BusStop getSelectedStop(){
        return selectedStop;
    }
}
