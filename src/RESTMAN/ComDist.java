package RESTMAN;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import static RESTMAN.DB.getConnection;

public class ComDist {
    private int id;
    private String ville;
    private String adresse1;
    private String adresse2;
    private int code_postal;
    private String tel;
    private int livreur_id;
    private String livreur_nom;
    private String etat_livraison;
    private String date_depart;
    private Integer temps_restant;
    private Boolean ok_recu;
    private String date_reception;


    public ComDist(int id, String ville, String adresse1, String adresse2, int code_postal, String tel, int livreur_id, String livreur_nom, String etat_livraison, String date_depart, Integer temps_restant, Boolean ok_recu, String date_reception) {
        this.id = id;
        this.ville = ville;
        this.adresse1 = adresse1;
        this.adresse2 = adresse2;
        this.code_postal = code_postal;
        this.tel = tel;
        this.livreur_id = livreur_id;
        this.livreur_nom = livreur_nom;
        this.etat_livraison = etat_livraison;
        this.date_depart = date_depart;
        this.temps_restant = temps_restant;
        this.ok_recu = ok_recu;
        this.date_reception = date_reception;
    }

    public ComDist(int id, String ville, String adresse1, String adresse2, int code_postal, String tel) {

        this.id = id;
        this.ville = ville;
        this.adresse1 = adresse1;
        this.adresse2 = adresse2;
        this.code_postal = code_postal;
        this.tel = tel;
        this.livreur_id = 0;    //LoginController.CurrentUser.getId();
        this.livreur_nom = null;
        this.etat_livraison = null;
        this.date_depart = null;
        this.temps_restant = null;
        this.ok_recu = false;
        this.date_reception = null;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDate_reception() {
        return date_reception;
    }

    public void setDate_reception(String date_reception) {
        this.date_reception = date_reception;
    }

    public int getLivreur_id() {
        return livreur_id;
    }

    public void setLivreur_id(int livreur_id) {
        this.livreur_id = livreur_id;
    }

    public String getLivreur_nom() {
        return livreur_nom;
    }

    public void setLivreur_nom(String livreur_nom) {
        this.livreur_nom = livreur_nom;
    }

    public int getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(int code_postal) {
        this.code_postal = code_postal;
    }


    public Boolean getOk_recu() {
        return ok_recu;
    }

    public void setOk_recu(Boolean ok_recu) {
        this.ok_recu = ok_recu;
    }

    public String getAdresse1() {
        return adresse1;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEtat_livraison() {
        return etat_livraison;
    }

    public void setEtat_livraison(String etat_livraison) {
        this.etat_livraison = etat_livraison;
    }

    public Integer getTemps_restant() {
        return temps_restant;
    }

    public void setTemps_restant(Integer temps_restant) {
        this.temps_restant = temps_restant;
    }
    public String getDate_depart() {
        return date_depart;
    }

    public void setDate_depart(String date_depart) {
        this.date_depart = date_depart;
    }


    /*************************************************************** DB *********************************************/
    public static Alert a = new Alert(Alert.AlertType.INFORMATION, "Ajout avec succès", ButtonType.OK);
    public static Alert E = new Alert(Alert.AlertType.ERROR, "Erreur", ButtonType.OK);

    public static boolean insertComDist(int Commande_id, String Ville, String Adresse1, String Adresse2, int Code_postal, String Tel) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlselect = "insert into ComDist(COMMANDE_ID,Ville,Adresse1,Adresse2,Code_postal,tel,etat_livraison) values (" + Commande_id + ",'" + Ville + "','" + Adresse1 + "','" + Adresse2 + "'," + Code_postal + ",'" + Tel + "','passee')";
            s.execute(sqlselect);
            return true;
        } catch (Exception e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }

    }

    public static ComDist getComDistById(int id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "select ville,adresse1,adresse2,code_postal,tel from comdist where commande_id =" + id + "";
        ResultSet result = s.executeQuery(sqlSelect);
        ComDist Comdist = null;
        if (!result.isBeforeFirst()) return null;  // if there is no data
        while (result.next()) {
            String ville = result.getString("ville");
            String adresse1 = result.getString("adresse1");
            String adresse2 = result.getString("adresse2");
            int Code_postal = result.getInt("Code_postal");
            String tel = result.getString("tel");
          /*  Date date = result.getDate("date_reception");
            if (date != null) {
                String date_reception = MainMenuController.formatter.format(date);
            }*/
            Comdist = new ComDist(id, ville, adresse1, adresse2, Code_postal, tel);
        }
        return Comdist;
    }

    public static ObservableList<ComDist> getComDist() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect;
        if (LoginController.CurrentUser.isClientRegulier() || LoginController.CurrentUser.isClientDivers()) {
            sqlSelect = " select commande_id,Ville,Adresse1,Adresse2,Code_postal,c.Tel,Userid,username, etat_livraison, date_depart ," +
                    "temps_restant,ok_recu,date_reception from ComDist C left join Users U on C.livreur_id = U.Userid " +
                    "where commande_id in (select commande_id from commandes where client_id =" + LoginController.CurrentUser.getId() + ")  order by commande_id asc ";
        } else {
            sqlSelect = " select commande_id,Ville,Adresse1,Adresse2,Code_postal,c.Tel,Userid,username, etat_livraison, date_depart, " +
                    "temps_restant,ok_recu,date_reception from ComDist C left join Users U on C.livreur_id = U.Userid order by commande_id asc";
        }
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<ComDist> ComdistList = FXCollections.observableArrayList();
        String date_depart = null;
        String date_reception = null;
        while (result.next()) {
            int commande_id = result.getInt("Commande_id");
            String ville = result.getString("ville");
            String adresse1 = result.getString("adresse1");
            String adresse2 = result.getString("adresse2");
            int Code_postal = result.getInt("Code_postal");
            String tel = result.getString("tel");
            int livreur_id = result.getInt("Userid");
            String livreur_nom = result.getString("username");
            String etat_livraison = result.getString("etat_livraison");
            Date date1 = result.getDate("date_depart");
            Integer tps_restant = result.getInt("temps_restant");
            Boolean ok_recu = result.getString("ok_recu").equals("O");
            Date date2 = result.getDate("date_reception");
            if (date1 != null) {
                date_depart = MainMenuController.formatter.format(date1);
            } else date_depart = null;
            if (date2 != null) {
                date_reception = MainMenuController.formatter.format(date2);
            } else date_reception = null;
            ComdistList.add(new ComDist(commande_id, ville, adresse1, adresse2, Code_postal, tel, livreur_id, livreur_nom, etat_livraison,date_depart, tps_restant, ok_recu, date_reception));
        }
        return ComdistList;
    }

    public static boolean updateComDist(int Commande_id, String Ville, String Adresse1, String Adresse2, int Code_postal, String Tel) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect = "UPDATE COMDIST SET ville = '" + Ville + "', Adresse1 = '" + Adresse1 + "', Adresse2 = '" + Adresse2 + "', Code_postal = " + Code_postal + ", tel = '" + Tel + "' where Commande_id = " + Commande_id + "";
            s.execute(sqlSelect);
            return true;
        } catch (Exception e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }

    }

    public static boolean updateLivraison(int Commande_id, int Livreur_id, String Etat_livraison, int tps_restant) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect = "UPDATE COMDIST SET Livreur_id = " + Livreur_id + ", Etat_livraison = '" + Etat_livraison + "', TEMPS_RESTANT= " + tps_restant + " where Commande_id = " + Commande_id + "";
            s.execute(sqlSelect);
            return true;
        } catch (Exception e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }

    }

    public static boolean insertDateDepart(int id) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect;
            sqlSelect = "UPDATE ComDist set DATE_DEPART=SYSDATE where Commande_id=" + id + "";
            s.execute(sqlSelect);
            return true;
        } catch (SQLException e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

    public static boolean delComDist(int id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "DELETE FROM ComDist WHERE Commande_id = " + id + "";
        boolean execute = s.execute(sqlDelete);

        return execute;
    }


    public static boolean ComDistRecu(int id, boolean b) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect;
            if (b)
                sqlSelect = "UPDATE ComDist set Ok_recu= 'O',TEMPS_RESTANT = 0, DATE_RECEPTION=SYSDATE where Commande_id=" + id + "";
            else sqlSelect = "UPDATE ComDist set Ok_recu = 'N',DATE_RECEPTION='' where Commande_id=" + id + "";
            s.execute(sqlSelect);
            return true;
        } catch (SQLException e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

 /*   public static boolean setPrixlivraison(int prix) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect = "UPDATE Parametres SET prix_livraison = " + prix + "";
            s.execute(sqlSelect);
            return true;
        } catch (Exception e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

    public static int getPrixlivraison() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "select prix_livraison from parametres";
        ResultSet result = s.executeQuery(sqlSelect);
        int prix = 0;
        while (result.next()) {
            prix = result.getInt("prix_livraison");
        }
        return prix;
        }*/
    }