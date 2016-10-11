package edu.uco.edmond.bus.tracker;

import edu.uco.edmond.bus.tracker.Dtos.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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

@ManagedBean
@RequestScoped
public class UserManagementBean implements Serializable {
    private ArrayList<User> admins = new ArrayList<>();
    
    private String username;
    private String password;
    private String type;
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    
    @PostConstruct
    public void init() {
        try {
            loadUserGroups("admin", admins);
        } catch (Exception ex) {
            Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadUserGroups(String userType, ArrayList<User> users) {
        try {
            //String url = "http://localhost:8080/edmond-bus-tracker/api/userservice/users/usertype/" + userType;
            String url = "https://uco-edmond-bus.herokuapp.com/api/userservice/users/usertype/" + userType;
            
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
            con.disconnect();
            
            if (response.toString().replace("\"", "").equals("No users currently registered.")) {
                admins = new ArrayList<>(); // show empty list
            } else {
                JSONArray jsonarray;
                try {
                    jsonarray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject;
                        try {
                            jsonobject = jsonarray.getJSONObject(i);
                            String id = jsonobject.getString("id");
                            String name = jsonobject.getString("username");
                            String usertype = jsonobject.getString("type");
                            String firstname = jsonobject.getString("firstName");
                            String lastname = jsonobject.getString("lastName");
                            String email = jsonobject.getString("email");
                            System.out.println(name);
                            User user = new User(Integer.valueOf(id), name, "", usertype, firstname, lastname, email);
                            users.add(user);
                        } catch (JSONException ex) {
                            Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String deleteUser(String username) {
        try {
            //String url = "http://localhost:8080/edmond-bus-tracker/api/userservice/users/delete/" + username;
            String url = "https://uco-edmond-bus.herokuapp.com/api/userservice/users/delete/" + username;
            
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
            con.disconnect();
            
        } catch (IOException ex) {
            Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        loadUserGroups("admin", admins);
        
        return "userManagement";
    }
    
    public String addUser(String username, String role, String password, String confirmPassword,
                            String firstName, String lastName, String email) throws IOException{
        
        if (!password.equals(confirmPassword)){
            return "Passwords must match!";
        }
        
        try {
            String url = "https://uco-edmond-bus.herokuapp.com/api/userservice/users/create/" 
                    + username + "/" + password + "/" + role + "/" + firstName + "/" + lastName + "/" + email;
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
            con.disconnect();

        } catch (IOException ex) {
            Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        loadUserGroups("admin", admins);
        context.redirect("userManagement.xhtml");
        
        return "User was added!";
    }
    
    public String editUser(String username, String password, String confirmPassword) throws IOException{
        if (!password.equals(confirmPassword) || (password.isEmpty() && confirmPassword.isEmpty())) {
            return "Passwords must match!";
        }
        
        try {
            //String url = "http://localhost:8080/edmond-bus-tracker/api/userservice/users/edit/" + username + "/" + password + "/" + confirmPassword + "/";
            String url = "https://uco-edmond-bus.herokuapp.com/api/userservice/users/edit/" + username + "/" + password + "/" + confirmPassword + "/";
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

        } catch (IOException ex) {
            Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        loadUserGroups("admin", admins);
        context.redirect("userManagement.xhtml");
        
        return "User was edited!";
    }
    
    public ArrayList<User> getAdmins() {
        return this.admins;
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

}
