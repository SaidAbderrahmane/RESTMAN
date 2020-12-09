package RESTMAN;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static RESTMAN.DB.getConnection;

public class Livreur extends Users {

    int nbr_liv;

    public Livreur(int id, String usertype, String username, String password, String nom, String prenom, String tel, String email, String adresse, int nbr_liv) {
        super(id, usertype, username, password, nom, prenom, tel, email, adresse);
        this.nbr_liv = nbr_liv;
    }

    public int getNbr_liv() {
        return nbr_liv;
    }

    public void setNbr_liv(int nbr_cmd) {
        this.nbr_liv = nbr_liv;
    }

    /**************************************************** DB ****************************************************/

    public static ObservableList<Livreur> getLivreursProfiles() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "select U.userid, usertype,username,password,nom,prenom,tel,email,adresse, nbr_liv from users U, Livreurs L where U.userid = L.userid order by userid asc";
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<RESTMAN.Livreur> livreurs = FXCollections.observableArrayList();
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
            int nbr_liv = result.getInt("nbr_liv");

            livreurs.add(new RESTMAN.Livreur(id, usertype, username, password, nom, prenom, tel, email, adresse, nbr_liv));
        }
        return livreurs;
    }

    public static Livreur getLivreur(int livreurid) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        updatenbrlivraison(livreurid);
        String sqlSelect = "select U.userid, usertype,username,password,nom,prenom,tel,email,adresse, nbr_liv from users U, Livreurs L where U.userid = L.userid and U.userid = " + livreurid + "  order by userid asc";
        ResultSet result = s.executeQuery(sqlSelect);
        Livreur livreur = null;
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
            int nbr_liv = result.getInt("nbr_liv");

            livreur = new RESTMAN.Livreur(id, usertype, username, password, nom, prenom, tel, email, adresse, nbr_liv);
        }
        return livreur;
    }

    public static boolean updatenbrlivraison(int id) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect = "update Livreurs set nbr_liv = (select COUNT(Livreur_ID) FROM COMDIST WHERE Livreur_id=" + id + ") " +
                    "where userid = " + id + "";
            s.execute(sqlSelect);
            return true;
        } catch (SQLException e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

}


