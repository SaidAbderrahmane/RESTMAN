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

public class Panier {
    int user_id;
    int article_id;

    public Panier(int user_id, int article_id) {
        this.user_id = user_id;
        this.article_id = article_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

/********************************************************* DB ******************************************************/

public static Alert a = new Alert(Alert.AlertType.INFORMATION, "Ajout avec succès", ButtonType.OK);
    public static Alert E = new Alert(Alert.AlertType.ERROR, "Erreur", ButtonType.OK);

    public static boolean insertPanier(int user_id, int article_id) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlselect = " INSERT INTO PANIER (USERID,ARTICLE_ID) VALUES ("+ user_id +"," + article_id + ")";
            s.execute(sqlselect);
            return true;
        } catch (Exception e) {
            E.show();
            return false;
        }
    }
    public static boolean delPanier(int user_id, int article_id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "DELETE FROM Panier WHERE Userid = "+user_id+" and Article_id = " + article_id + "";
        boolean execute = s.execute(sqlDelete);
        return execute;
    }
    public static boolean estdansPanier(int user_id, int article_id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "SELECT ARTICLE_ID FROM Panier WHERE Userid = "+user_id+" and Article_id = " + article_id + "";
        ResultSet result = s.executeQuery(sqlDelete);
        if(result.isBeforeFirst()) return true;
        else return false ;
    }
    public static ObservableList<Articles> getPanier(int user_id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "SELECT Article_id,Designation,Cat_nom,Specialite,Prix FROM ARTICLES A,CATEGORIE C WHERE A.Cat_id = C.Cat_id " +
                "and A.ARTICLE_ID IN (SELECT ARTICLE_ID FROM PANIER WHERE USERID ="+user_id+") order by article_id asc";
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
