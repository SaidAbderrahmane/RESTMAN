package RESTMAN;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static RESTMAN.DB.getConnection;

public class Parametres {
    int prixlivraison;
    int seuil_nbr_cmd;
    int seuil_val_cmd;

    public Parametres(int prixlivraison, int seuil_nbr_cmd, int seuil_val_cmd) {
        this.prixlivraison = prixlivraison;
        this.seuil_nbr_cmd = seuil_nbr_cmd;
        this.seuil_val_cmd = seuil_val_cmd;
    }


    public int getPrixlivraison() {
        return prixlivraison;
    }

    public void setPrixlivraison(int prixlivraison) {
        this.prixlivraison = prixlivraison;
    }

    public int getSeuil_nbr_cmd() {
        return seuil_nbr_cmd;
    }

    public void setSeuil_nbr_cmd(int seuil_nbr_cmd) {
        this.seuil_nbr_cmd = seuil_nbr_cmd;
    }

    public int getSeuil_val_cmd() {
        return seuil_val_cmd;
    }

    public void setSeuil_val_cmd(int seil_val_cmd) {
        this.seuil_val_cmd = seil_val_cmd;
    }

    /*********************************** DB ***********************************/

    public static Alert a = new Alert(Alert.AlertType.INFORMATION, "Ajout avec succès", ButtonType.OK);
    public static Alert E = new Alert(Alert.AlertType.ERROR, "Erreur", ButtonType.OK);


    public static boolean updateParametres(int prix,int seuil_nbr_cmd, int seil_val_cmd) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect = "UPDATE Parametres SET prix_livraison = " + prix + ", seuil_nbr_cmd = "+seuil_nbr_cmd+", seuil_val_cmd = "+seil_val_cmd+"";
            s.execute(sqlSelect);
            return true;
        } catch (Exception e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

    public static Parametres getParametres() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "select * from parametres";
        ResultSet result = s.executeQuery(sqlSelect);
        Parametres parametres = null;
        int prixlivraison = 0;
        int seuil_nbr_cmd = 0;
        int seuil_val_cmd = 0;
        while (result.next()) {
            prixlivraison = result.getInt("prix_livraison");
            seuil_nbr_cmd = result.getInt("seuil_nbr_cmd");
            seuil_val_cmd = result.getInt("seuil_val_cmd");
            parametres = new Parametres(prixlivraison,seuil_nbr_cmd,seuil_val_cmd);
        }
        return parametres;
    }
}
