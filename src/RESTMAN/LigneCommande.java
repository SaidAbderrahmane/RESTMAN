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

public class LigneCommande {
    private int lcmd_id;
    private int cmd_id;
    private int article_id;
    private String article_des;
    private int prix;
    private int qte;
    private int montantL;


    public LigneCommande(int cmd_id, int lcmd_id, int article_id, String article_des, int prix, int qte) {
        this.lcmd_id = lcmd_id;
        this.cmd_id = cmd_id;
        this.article_id = article_id;
        this.article_des = article_des;
        this.prix = prix;
        this.qte = qte;
        this.setMontantL();

    }

    public int getLcmd_id() {
        return lcmd_id;
    }

    public void setLcmd_id(int lcmd_id) {
        this.lcmd_id = lcmd_id;
    }

    public int getCmd_id() {
        return cmd_id;
    }

    public void setCmd_id(int cmd_id) {
        this.cmd_id = cmd_id;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public String getArticle_des() {
        return article_des;
    }

    public void setArticle_id(String article_des) {
        this.article_des = article_des;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }


    public int getMontantL() {
        return montantL;
    }

    public void setMontantL() {
        this.montantL = this.qte * this.prix;
    }


    /************************************************* DB ****************************************/

    public static Alert a = new Alert(Alert.AlertType.INFORMATION, "Ajout avec succès", ButtonType.OK);
    public static Alert E = new Alert(Alert.AlertType.ERROR, "Erreur", ButtonType.OK);

    public static boolean insertLigneCommande(int cmd_id, int article_id, /*String article_des, int prix,*/ int qte, int montantL) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlselect = "insert into Ligne_Commande (COMMANDE_ID,ARTICLE_ID,quantite,montantL) values (" + cmd_id + "," + article_id + "," + qte + "," + montantL + ")";
            s.execute(sqlselect);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            E.show();
            return false;
        }

    }

    public static ObservableList<LigneCommande> getLigneCommande(int cmd_id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "select Ligne_cmd_id,quantite,L.article_id,designation,prix,montantL from Ligne_Commande L,Articles A where L.article_id = A.article_id and L.commande_id =" + cmd_id + " ";
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<LigneCommande> LigneCommandeList = FXCollections.observableArrayList();
        while (result.next()) {
            // int cmd_id = result.getInt("Commande_id");
            int ligne_cmd_id = result.getInt("Ligne_Cmd_id");
            int qte = result.getInt("quantite");
            int article_id = result.getInt("article_id");
            String article_des = result.getString("designation");
            //int article_des = result.getInt("article_des");
            int prix = result.getInt("prix");
            //int montantL = result.getInt("montantL");
            LigneCommandeList.add(new LigneCommande(cmd_id, ligne_cmd_id, article_id, article_des, prix, qte/*, montantL*/));
        }
        return LigneCommandeList;
    }

    //non utilisable car les lignes commanedes sont inseres par blocs
    public static boolean updateLigneCommande(int lcmd_id, int cmd_id, int article_id, String article_des, /*int prix,*/int qte, int montantL) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect;
            sqlSelect = "UPDATE LigneCommande SET Commande_id = " + cmd_id + ", article_id = " + article_id + ", quantite = " + qte + ", montantL = " + montantL + " where ligne_commande_id=" + lcmd_id + "";
            s.execute(sqlSelect);
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            E.show();
            return false;
        }
    }

    public static boolean delLigneCommande(int id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "DELETE FROM Ligne_Commande WHERE Commande_id = " + id + "";
        boolean execute = s.execute(sqlDelete);

        return execute;
    }
}

