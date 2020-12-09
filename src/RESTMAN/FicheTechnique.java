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

public class FicheTechnique {                       //Composition : FicheTechnique HAS-A Ingredients AND Preparation
    int id;
    int article_id;
    int prix;
    Ingredients ingredients;
    Preparation preparation;


    public FicheTechnique(int id, int article_id, int prix, Ingredients ingredients, Preparation preparation) {
        this.id = id;
        this.article_id = article_id;
        this.prix = prix;
        this.ingredients = ingredients;
        this.preparation = preparation;
    }

    public FicheTechnique() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public void setIngredients(Ingredients ingredients) {
        this.ingredients = ingredients;
    }

    public Ingredients setIngredients() {
        return ingredients;
    }

    public void setPreparation(Preparation preparation) {
        this.preparation = preparation;
    }

    public Preparation setPreparation() {
        return preparation;
    }


    /******************************************************* DB ***************************************************/
    public static Alert a = new Alert(Alert.AlertType.INFORMATION, "Ajout avec succès", ButtonType.OK);
    public static Alert E = new Alert(Alert.AlertType.ERROR, "Erreur", ButtonType.OK);

    public static boolean insertFiche_Technique(int article_id, int prix) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlselect = "INSERT INTO Fiche_Technique(fiche_id,Prix) values (" + article_id + "," + prix + ")";
            s.execute(sqlselect);
            return true;
        } catch (Exception e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

    public static boolean insertIngredients(int fiche_id, String ingredient_nom, float qte, String unite, int prix_u) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlselect = " INSERT INTO Ingredients(Fiche_id,ingredient_nom,quantite,unite, prix_unitaire) values (" + fiche_id + ",'" + ingredient_nom + "'," + qte + ",'" + unite + "'," + prix_u + ")";
            s.execute(sqlselect);
            return true;
        } catch (Exception e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

    public static boolean insertPreparation(int fiche_id, int Num_etape, String Nom_etape, int tempsNec, String Description) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlselect = " INSERT INTO Preparation (fiche_id,num_etape,nom_etape,TempsNec,description) values (" + fiche_id + "," + Num_etape + ",'" + Nom_etape + "'," + tempsNec + ",'" + Description + "')";
            s.execute(sqlselect);
            return true;
        } catch (Exception e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

    public static ObservableList<Ingredients> getIngredients(int Fiche_id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "SELECT ingredient_nom,quantite,prix_unitaire,unite FROM Ingredients WHERE Fiche_id = " + Fiche_id + "";
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<Ingredients> IngredientsList = FXCollections.observableArrayList();
        while (result.next()) {
            String ingredient_nom = result.getString("ingredient_nom");
            float qte = result.getFloat("quantite");
            int prix = result.getInt("prix_unitaire");
            String unite = result.getString("unite");
            IngredientsList.add(new Ingredients(ingredient_nom, qte, prix, unite));//int ingredient_id, String ingredient_nom, int quantite, int prix, String unite
        }
        return IngredientsList;
    }

    public static ObservableList<Preparation> getPreparation(int Fiche_id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "SELECT num_etape,nom_etape,TempsNec,description FROM Preparation WHERE Fiche_id = " + Fiche_id + "";
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<Preparation> PreparationList = FXCollections.observableArrayList();
        while (result.next()) {
            int num_etape = result.getInt("num_etape");
            String nom_etape = result.getString("nom_etape");
            int tempsNec = result.getInt("TempsNec");
            String description = result.getString("description");
            PreparationList.add(new Preparation(num_etape, nom_etape, tempsNec, description));
        }
        return PreparationList;
    }

    public static boolean updateFiche_Technique(int id, int prix) throws SQLException {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect;

            sqlSelect = "UPDATE Fiche_Technique SET prix= " + prix + " where Fiche_id=" + id + "";
            s.execute(sqlSelect);
            return true;
        } catch (SQLException e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

    public static boolean updateIngredients(int fiche_id, int ingredient_id, float qte) throws SQLException {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect;

            sqlSelect = "UPDATE Ingredients SET ingredient_id = " + ingredient_id + ", quantite = " + qte + " where Fiche_id=" + fiche_id + "";
            s.execute(sqlSelect);
            return true;
        } catch (SQLException e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

    public static boolean updatePreparation(int fiche_id, int Num_etape, String Nom_etape, int tempsNec, String Description) throws SQLException {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect;
            sqlSelect = "UPDATE Preparation SET num_etape=" + Num_etape + ", nom_etape ='" + Nom_etape + "',TempsNec = " + tempsNec + ", description = '" + Description + "' where Fiche_id=" + fiche_id + "";
            s.execute(sqlSelect);
            return true;
        } catch (SQLException e) {
            E.setContentText(e.toString());
            E.show();
            return false;
        }
    }

    public static boolean delFiche_Technique(int id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "DELETE FROM Fiche_Technique WHERE Fiche_id = " + id + " ";
        boolean execute = s.execute(sqlDelete);
        return execute;
    }

    public static boolean delIngredients(int id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "DELETE FROM Ingredients WHERE Fiche_id = " + id + " ";
        boolean execute = s.execute(sqlDelete);

        return execute;
    }

    public static boolean delPreparation(int id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "DELETE FROM Preparation WHERE Fiche_id = " + id + " ";
        boolean execute = s.execute(sqlDelete);
        return execute;
    }

}

