package edu.uco.edmond.bus.tracker;

import edu.uco.edmond.bus.tracker.Dtos.Bus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

@ManagedBean
@ViewScoped
public class BusManagement implements Serializable {
    
    private ArrayList<Bus> buses = new ArrayList<>();
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    
    @PostConstruct
    public void init() {
        try {
            String url = "https://uco-edmond-bus.herokuapp.com/api/busservice/buses";
            
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            // optional default is GET
            con.setRequestMethod("GET");
            
            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            
            StringBuffer response;
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            con.disconnect();
            
            //print result
            System.out.println(response.toString());
            JSONArray jsonarray;
            try {
                jsonarray = new JSONArray(response.toString());
                for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject;
                try {
                    jsonobject = jsonarray.getJSONObject(i);
                    int id = jsonobject.getInt("id");
                    String name = jsonobject.getString("name");
                    String driver = jsonobject.has("driver") ? jsonobject.getString("driver") : "none";
                    String route = jsonobject.getString("route");
                    String lastStop = jsonobject.getString("lastStop");
                    Boolean active = jsonobject.getBoolean("active");
                    String lastActive = jsonobject.getString("lastActive");
                    double lastLong = jsonobject.getDouble("lastLong");
                    double lastLat = jsonobject.getDouble("lastLat");
                    System.out.println(name);
                    Bus temp = new Bus(id, name, driver, route, lastStop, active, lastActive, lastLong, lastLat);
                    buses.add(temp);
                } catch (JSONException ex) {
                    Logger.getLogger(BusManagement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            } catch (JSONException ex) {
                Logger.getLogger(BusManagement.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(BusManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Bus> getBuses() {
        return this.buses;
    }
    
    public void addBus(String name, String driver, String route) throws IOException {
        
        try {
            String url = "https://uco-edmond-bus.herokuapp.com/api/busservice/buses/create/" 
                    + name + "/" + driver + "/" + route;
            url = url.replace(" ", "%20");
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            // optional default is GET
            con.setRequestMethod("GET");
            
            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            
            if (responseCode > 400) {
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

        } catch (IOException ex) {
            Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            context.redirect("busManagement.xhtml");
        }
//        return "busManagement";
    }
    
}
