package edu.uco.edmond.bus.tracker;

import edu.uco.edmond.bus.tracker.Dtos.User;
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
import java.util.stream.Collectors;
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
    private ArrayList<User> rolesType;
    private List<User> users;
    private List<User> drivers;
    private List<User> admins;
    
    private String username;
    private String password;
    private String type;
    private final String ENV = "https://uco-edmond-bus.herokuapp.com/api/userservice/users";
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    
    @PostConstruct
    public void init() {
        try {
            loadUserGroups("admin", "driver");
            drivers = rolesType.stream()
                    .filter(user -> user.getType().equals("driver"))
                    .collect(Collectors.toList());
            users = rolesType.stream()
                    .filter(user -> user.getType().equals("user"))
                    .collect(Collectors.toList());
            admins = rolesType.stream()
                    .filter(user -> user.getType().equals("admin"))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadUserGroups(String userType1, String userType2) {
        try {
            rolesType = new ArrayList<>();
            
            String url = ENV + "/usertype/" + userType1 + "/" + userType2;
            
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

                if (response.toString().replace("\"", "").equals("No users currently registered.")) {
                    rolesType = new ArrayList<>(); // show empty list
                } else {
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
                                User user = new User(Integer.valueOf(id), name, "", usertype, firstname, lastname, email);
                                rolesType.add(user);
                            } catch (JSONException ex) {
                                Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (JSONException ex) {
                        Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String deleteUser(String username) throws IOException {
        try {
            String url = ENV + "/delete/" + username;
            
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
            }
        } catch (IOException ex) {
            Logger.getLogger(UserManagementBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        loadUserGroups("admin", "driver");
        
        return "userManagement";
    }
    
    public String addUser(String username, String role, String password, String confirmPassword,
                            String firstName, String lastName, String email) throws IOException{
        
        if (!password.equals(confirmPassword)){
            return "Passwords must match!";
        }
        
        try {
            String url = ENV + "/create/" + username + "/" + password + "/" + role 
                            + "/" + firstName + "/" + lastName + "/" + email;
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
        
        context.redirect("userManagement.xhtml");
        
        return "User was added!";
    }
    
    public String editUser(String username, String password, String confirmPassword) throws IOException{
        if (!password.equals(confirmPassword) || (password.isEmpty() && confirmPassword.isEmpty())) {
            return "Passwords must match!";
        }
        
        try {
            String url = ENV + "/edit/" + username + "/" + password + "/" + confirmPassword + "/";
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
        
        context.redirect("userManagement.xhtml");
        
        return "User was edited!";
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

    public ArrayList<User> getRolesType() {
        return this.rolesType;
    }

    public List<User> getDrivers() {
        return drivers;
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public List<User> getAdmins() {
        return admins;
    }
}
