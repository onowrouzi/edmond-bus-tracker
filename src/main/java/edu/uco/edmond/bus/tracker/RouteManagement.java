/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.edmond.bus.tracker;

import edu.uco.edmond.bus.tracker.Dtos.BusRoute;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

/**
 *
 * @author omidnowrouzi
 */
@ManagedBean
@ViewScoped
public class RouteManagement implements Serializable {
    private ArrayList<BusRoute> routes = new ArrayList<>();
    private List<BusRoute> filteredRoutes;
    private final String ENV = "https://uco-edmond-bus.herokuapp.com/api/routeservice/routes";
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

    @PostConstruct
    public void init() {
        try {URL obj = new URL(ENV);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + ENV);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
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
                        System.out.println(name);
                        BusRoute temp = new BusRoute(id, name);
                        routes.add(temp);
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

    public ArrayList<BusRoute> getRoutes() {
        return this.routes;
    }

    public String addRoute(String name) throws IOException {

        try {
            String url = ENV + name;
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

        return "routeManagement";
    }

    /**
     * @return the filteredRoutes
     */
    public List<BusRoute> getFilteredRoutes() {
        return filteredRoutes;
    }

    /**
     * @param filteredRoutes the filteredRoutes to set
     */
    public void setFilteredRoutes(List<BusRoute> filteredRoutes) {
        this.filteredRoutes = filteredRoutes;
    }

}
