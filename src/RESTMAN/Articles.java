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

public class Articles {
    private int id;
    private String designation;
    private String categorie;
    private String specialite;
    private int prix;


    public Articles(int id, String designation, String categorie, String specialite, int prix) {
        this.id = id;
        this.designation = designation;
        this.specialite = specialite;
        this.categorie = categorie;
        this.prix = prix;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCat_id(String categorie) {
        this.categorie = categorie;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    /********************************************* DB *********************************************************************/

    public static Alert a = new Alert(Alert.AlertType.INFORMATION, "Ajout avec succès", ButtonType.OK);
    public static Alert E = new Alert(Alert.AlertType.ERROR, "Erreur", ButtonType.OK);

    public static boolean insertArticles(String designation, int cat_id, String specialite, int prix) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlselect = " INSERT INTO ARTICLES(Designation,cat_id,Specialite,Prix) values ('" + designation + "'," + cat_id + ",'" + specialite + "'," + prix + ")";
            s.execute(sqlselect);
            return true;
        } catch (Exception e) {
            E.show();
            return false;
        }
    }

    public static ObservableList<Articles> getArticles() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "SELECT Article_id,Designation,Cat_nom,Specialite,Prix FROM ARTICLES A,CATEGORIE C WHERE A.Cat_id = C.Cat_id order by article_id asc";
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

    public static boolean updateArticles(int id, String designation, int cat_id, String specialite, int prix){
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect;

            sqlSelect = "UPDATE Articles SET designation = '" + designation + "' , cat_id = " + cat_id + ",specialite = '" + specialite + "',prix= " + prix + " where Article_id=" + id + "";
            s.execute(sqlSelect);
            return true;
        } catch (SQLException e) {
            E.show();
            return false;
        }
    }

    public static boolean delArticles(int id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "DELETE FROM Articles WHERE Article_id = " + id + " ";
        boolean execute = s.execute(sqlDelete);

        return execute;
    }
}