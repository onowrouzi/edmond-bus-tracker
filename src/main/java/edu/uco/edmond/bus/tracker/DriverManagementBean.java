package edu.uco.edmond.bus.tracker;

import edu.uco.edmond.bus.tracker.Dtos.User;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

@ManagedBean (name = "driverManagementBean")
@RequestScoped
public class DriverManagementBean implements Serializable {
    private ArrayList<User> selectedDrivers;
    private ArrayList<User> drivers;
    private List<User> filteredDrivers;
    
    private String username;
    private String password;
    private String type;
    private final String ENV = "https://uco-edmond-bus.herokuapp.com/api/userservice/users";
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    
    @PostConstruct
    public void init() {
        try {
            selectedDrivers = new ArrayList<>();
            drivers = new ArrayList<>();
            String url = ENV + "/usertype/driver";
            
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            // optional default is GET
            con.setRequestMethod("GET");
            
            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            
            if (responseCode != 200) {
                con.disconnect(); // disconnect on error
            } else {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                con.disconnect();

                JSONArray jsonarray;
                try {
                    jsonarray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject;
                        try {
                            jsonobject = jsonarray.getJSONObject(i);
                            int id = jsonobject.getInt("id");
                            String name = jsonobject.getString("username");
                            String usertype = jsonobject.getString("type");
                            String firstname = jsonobject.getString("firstName");
                            String lastname = jsonobject.getString("lastName");
                            String email = jsonobject.getString("email");
                            System.out.println(name);
                            User user = new User(id, name, "", usertype, firstname, lastname, email);
                            selectedDrivers.add(user);
                        } catch (JSONException ex) {
                            Logger.getLogger(DriverManagementBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(DriverManagementBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } catch (Exception ex) {
            Logger.getLogger(DriverManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String editDriver(String username, String firstName, String lastName, String email, String password, String confirmPassword) throws Exception{
        if (!password.equals(confirmPassword) || (password.isEmpty() && confirmPassword.isEmpty())) {
            return "Passwords must match!";
        }
        
        try {
            String url = ENV + "/edit/" + username + "/" + firstName + "/" + lastName + "/" + email + "/"+ password + "/" + confirmPassword + "/";
            HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            int responseCode = con.getResponseCode();
            
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuffer response = new StringBuffer();
            
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            con.disconnect();
            
        } catch (Exception ex) {
            Logger.getLogger(DriverManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        context.redirect("driverManagement.xhtml");
        
        return "Driver was edited!";
    }
    
    public String deleteDriver(String username) throws Exception {
        try {
            String url;
            
            url = ENV + "/delete/" + username;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            // optional default is GET
            con.setRequestMethod("GET");
            
            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            int responseCode = con.getResponseCode();
            
            if (responseCode != 200) {
                con.disconnect(); // disconnect on error
            } else {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                con.disconnect();
                
                ListIterator<User> iter = selectedDrivers.listIterator();
                while(iter.hasNext()){
                    if(iter.next().getUsername().equals(username)){
                    iter.remove();
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DriverManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "driverManagement";
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<User> getSelectedDrivers() {
        return this.selectedDrivers;
    }

    public ArrayList<User> getDrivers() {
        return this.drivers;
    }

    public List<User> getFilteredDrivers() {
        return filteredDrivers;
    }

    public void setFilteredDrivers(List<User> filteredDrivers) {
        this.filteredDrivers = filteredDrivers;
    }
    
}
