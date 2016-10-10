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
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

@ManagedBean
@ViewScoped
public class BusManagement implements Serializable {
    
    private ArrayList<Bus> buses = new ArrayList<>();
    
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
            
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
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
                    String driver = jsonobject.getString("driver");
                    String route = jsonobject.getString("route");
                    String lastStop = jsonobject.getString("laststop");
                    Boolean active = false;
                    if (jsonobject.getInt("active") == 1) active = true;
                    String lastActive = jsonobject.getString("lastactive");
                    System.out.println(name);
                    Bus temp = new Bus(id, name, driver, route, lastStop, active, lastActive);
                    buses.add(temp);
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
    }
    
    public ArrayList<Bus> getBuses() {
        return this.buses;
    }
    
}
