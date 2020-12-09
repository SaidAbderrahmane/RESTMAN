package RESTMAN;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static RESTMAN.DB.getConnection;
import static RESTMAN.LoginController.CurrentUser;
import static RESTMAN.ParametresController.CurrentParametres;

public class Client extends Users {
    int nbr_cmd;
    int val_cmd;
    int nbr_bonus;

    public Client(int id, String usertype, String username, String password, String nom, String prenom, String tel, String email, String adresse, int nbr_cmd, int val_cmd, int nbr_bonus) {
        super(id, usertype, username, password, nom, prenom, tel, email, adresse);
        this.nbr_cmd = nbr_cmd;
        this.val_cmd = val_cmd;
        this.nbr_bonus = nbr_bonus;
    }

    public int getNbr_cmd() {
        return nbr_cmd;
    }

    public void setNbr_cmd(int nbr_cmd) {
        this.nbr_cmd = nbr_cmd;
    }

    public int getVal_cmd() {
        return val_cmd;
    }

    public void setVal_cmd(int val_cmd) {
        this.val_cmd = val_cmd;
    }

    public int getNbr_bonus() {
        return nbr_bonus;
    }

    public void setNbr_bonus(int nbr_bonus) {
        this.nbr_bonus = nbr_bonus;
    }

    /**************************************************** DB ****************************************************/

    public static ObservableList<Client> getClients() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "select U.userid, usertype,username,password,nom,prenom,tel,email,adresse, nbr_cmd, val_cmd, nbr_bonus from users U, Clients C where U.userid = C.userid order by userid asc";
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<Client> clients = FXCollections.observableArrayList();
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
            int nbr_cmd = result.getInt("nbr_cmd");
            int val_cmd = result.getInt("nbr_cmd");
            int nbr_bonus = result.getInt("nbr_bonus");

            clients.add(new Client(id, usertype, username, password, nom, prenom, tel, email, adresse, nbr_cmd, nbr_cmd, nbr_bonus));
        }
        return clients;
    }

    public static Client getClient(int clientid) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        updateStats(clientid);
        String sqlSelect = "select U.userid, usertype,username,password,nom,prenom,tel,email,adresse, nbr_cmd, val_cmd, nbr_bonus from users U, Clients C where U.userid = C.userid and U.userid= " + clientid + " order by userid asc";
        ResultSet result = s.executeQuery(sqlSelect);
        Client client = null;
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
            int nbr_cmd = result.getInt("nbr_cmd");
            int val_cmd = result.getInt("val_cmd");
            int nbr_bonus = result.getInt("nbr_bonus");
            nbr_bonus = calcBonus(nbr_cmd, val_cmd, nbr_bonus);
            client = new Client(id, usertype, username, password, nom, prenom, tel, email, adresse, nbr_cmd, val_cmd, nbr_bonus);
        }
        return client;
    }

    public static boolean updateStats(int id) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect = "update clients set nbr_cmd = (select COUNT(COMMANDE_ID) FROM COMMANDES WHERE CLIENT_id=" + id + "), " +
                    "val_cmd = (select sum(montant) FROM COMMANDES WHERE CLIENT_id=" + id + ")  where userid = " + id + "";
            s.execute(sqlSelect);
            return true;
        } catch (SQLException e) {

            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

    public static boolean updateBonus(int nbr_bonus) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect = "update clients set nbr_bonus = " + nbr_bonus + " where userid =" + CurrentUser.getId() + "";
            s.execute(sqlSelect);
            return true;
        } catch (SQLException e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

    public static int calcBonus(int nbr_cmd, int val_cmd, int nbr_bonus) {
        if (nbr_cmd - (CurrentParametres.getSeuil_nbr_cmd() * nbr_bonus) > CurrentParametres.getSeuil_nbr_cmd() || val_cmd - (CurrentParametres.seuil_val_cmd * nbr_bonus) > CurrentParametres.seuil_nbr_cmd) {
            if (((nbr_cmd - (CurrentParametres.getSeuil_nbr_cmd() * nbr_bonus)) / CurrentParametres.getSeuil_nbr_cmd()) > ((val_cmd - (CurrentParametres.seuil_val_cmd * nbr_bonus)) / CurrentParametres.getSeuil_val_cmd())) {
                nbr_bonus = nbr_bonus / CurrentParametres.getSeuil_nbr_cmd();
                updateBonus(nbr_bonus);
            } else {
                nbr_bonus = val_cmd / CurrentParametres.getSeuil_val_cmd();
                updateBonus(nbr_bonus);
            }
        }
        return nbr_bonus;
    }

    public static String getPreferenceCulinaire() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "select stats_mode(specialite) as preference from articles A,Ligne_Commande L where L.article_id=A.article_id";
        ResultSet result = s.executeQuery(sqlSelect);
        String pref = null;
        while (result.next()) {
            pref = result.getString("preference");
        }
        return pref;
    }
    public static ObservableList<Articles> getRecommendation() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "SELECT * FROM (SELECT Article_id,Designation,Cat_nom,Specialite,Prix FROM ARTICLES A,CATEGORIE C WHERE A.Cat_id = C.Cat_id and specialite ='"+getPreferenceCulinaire()+"' ORDER BY dbms_random. value) WHERE rownum < 5";
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<Articles> ArticlesList = FXCollections.observableArrayList();
        while (result.next()) {
            int id = result.getInt("Article_id");
            String designation = result.getString("designation");
            String categorie = result.getString("cat_nom");
            String specialite = result.getString("specialite");
            int prix = result.getInt("prix");
            ArticlesList.add(new Articles(id, designation, categorie, specialite, prix));
        }
        return ArticlesList;
    }

}
