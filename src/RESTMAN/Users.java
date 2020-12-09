package RESTMAN;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static RESTMAN.DB.getConnection;
import static RESTMAN.LoginController.CurrentUser;

public class Users {


    private int id;
    private String usertype;
    private String username;
    private String password;
    private String nom;
    private String prenom;
    private String tel;
    private String email;
    private String adresse;

    public Users(int id, String usertype, String username, String password, String nom, String prenom, String tel, String email, String adresse) {
        this.id = id;
        this.usertype = usertype;
        this.username = username;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.email = email;
        this.adresse = adresse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public boolean isClientDivers() {
        if (this.getUsertype().equals("Client divers"))
            return true;
        else return false;
    }

    public boolean isClientRegulier() {
        if (this.getUsertype().equals("Client regulier"))
            return true;
        else return false;
    }

    public boolean isLivreur() {
        if (this.getUsertype().equals("Livreur"))
            return true;
        else return false;
    }

    public boolean isAdmin() {
        if (this.getUsertype().equals("Admin"))
            return true;
        else return false;
    }


    /*-------------------------------------------------DB--------------------------------------------------------------*/

    public static Alert a = new Alert(Alert.AlertType.INFORMATION, "Ajout avec succès", ButtonType.OK);
    public static Alert E = new Alert(Alert.AlertType.ERROR, "Erreur", ButtonType.OK);

    public static boolean login_check(String username, String passsword) throws SQLException {

        Connection con = DB.getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "select password from USERS where username ='" + username + "'";
        ResultSet result = s.executeQuery(sqlSelect);
        while (result.next()) {
            if (result.getString("password").equals(passsword)) return true;
        }
        return false;

    }

    public static Users getCurrentUser(String username) throws SQLException {
        Connection con = DB.getConnection();
        Statement s = con.createStatement();
        String sqlselect = "select * from USERS where username ='" + username + "'";
        ResultSet result = s.executeQuery((sqlselect));
        Users currentuser = null;
        while (result.next()) {
            int id = result.getInt("userid");
            String usertype = result.getString("usertype");
            String password = result.getString("password");
            String nom = result.getString("nom");
            String prenom = result.getString("prenom");
            String tel = result.getString("tel");
            String email = result.getString("email");
            String adresse = result.getString("adresse");


            currentuser = new Users(id, usertype, username, password, nom, prenom, tel, email, adresse);
        }
        return currentuser;
    }

    public static boolean insertUsers(String username, String usertype, String password, String nom, String prenom, String tel, String email, String adresse) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlselect = "insert into USERS (Username,Password,Usertype,Nom,Prenom,Tel,email,adresse) values ('" + username + "','" + password + "','" + usertype + "','" + nom + "','" + prenom + "','" + tel + "','" + email + "','" + adresse + "')";
            s.execute(sqlselect);
            return true;
        } catch (Exception e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

    public static ObservableList<Users> getUsers() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "select * from users order by userid asc";
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<Users> usersList = FXCollections.observableArrayList();
        while (result.next()) {
            int id = result.getInt("userid");
            String usertype = result.getString("usertype");
            String username = result.getString("username");
            String password = result.getString("password");
            String nom = result.getString("nom");
            String prenom = result.getString("prenom");
            String tel = result.getString("tel");
            String email = result.getString("email");
            String adresse = result.getString("adresse");

            usersList.add(new Users(id, usertype, username, password, nom, prenom, tel, email, adresse));
        }
        return usersList;
    }


    public static ObservableList<Users> getLivreurs() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "select * from Users where usertype = 'Livreur' or usertype = 'Admin'";
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<Users> UsersList = FXCollections.observableArrayList();
        while (result.next()) {
            int id = result.getInt("userid");
            String usertype = result.getString("usertype");
            String username = result.getString("username");
            String password = result.getString("password");
            String nom = result.getString("nom");
            String prenom = result.getString("prenom");
            String tel = result.getString("tel");
            String email = result.getString("email");
            String adresse = result.getString("adresse");

            UsersList.add(new Users(id, usertype, username, password, nom, prenom, tel, email, adresse));
        }
        return UsersList;
    }

    public static boolean updateUsers(int id, String usertype, String username, String password, String nom, String prenom, String tel, String email, String adresse) throws SQLException {

        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect;

        sqlSelect = "UPDATE users SET username = '" + username + "' , usertype = '" + usertype + "',password = '" + password + "', nom = '" + nom + "' ,prenom = '" + prenom + "',tel= '" + tel + "', email= '" + email + "',adresse = '" + adresse + "' where userid =" + id + "";
        s.execute(sqlSelect);
        return true;

    }

    public static boolean delUsers(int id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "DELETE FROM Users WHERE userid = " + id + " ";
        boolean execute = s.execute(sqlDelete);

        return execute;
    }
}