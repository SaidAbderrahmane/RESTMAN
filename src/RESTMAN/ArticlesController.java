package RESTMAN;

import com.sun.webkit.ContextMenuItem;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static RESTMAN.Articles.E;
import static RESTMAN.Articles.a;
import static RESTMAN.LoginController.CurrentUser;
import static javafx.fxml.FXMLLoader.load;

public class ArticlesController implements Initializable {
    @FXML
    Pane pane;
    @FXML
    Button BtnAjouter;
    @FXML
    Button BtnModifier;
    @FXML
    Button BtnSupprimer;
    @FXML
    Button BtnQuitter;
    @FXML
    Label articlesid;
    @FXML
    TextField designation;
    @FXML
    TextField specialite;
    @FXML
    TextField prix;
    @FXML
    ChoiceBox categorie;
    @FXML
    Button BtnVider;
    @FXML
    Button BtnVoirFT;
    @FXML
    CheckBox panierCB;
    @FXML
    ToggleButton toggle;
    @FXML
    MenuItem mc_ajouteraupanier;
    @FXML
    MenuItem mc_enleverdupanier;
    @FXML
    ContextMenu mc;

    public static ObservableList<Articles> ListA = null;
    @FXML
    TableView<Articles> TableA;
    @FXML
    TableColumn<Articles, Integer> c1;
    @FXML
    TableColumn<Articles, String> c2;
    @FXML
    TableColumn<Articles, String> c3;
    @FXML
    TableColumn<Articles, String> c4;
    @FXML
    TableColumn<Articles, String> c5;

    public static Articles CurrentArticle = null;
    public static Alert D = new Alert(Alert.AlertType.CONFIRMATION, "Vous voulez confirmer la suppression?", ButtonType.YES, ButtonType.NO);

    public void CloseArticles(Event e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Accueil");
        stage.setResizable(false);
        stage.show();
    }

    public void Vider() {
        articlesid.setText("-");
        designation.clear();
        categorie.getSelectionModel().clearSelection();
        specialite.clear();
        prix.clear();
    }

    public void addArticles() throws SQLException {
        String Adesignation = designation.getText();
        String Aspecialite = specialite.getText();
        int Acat_id = 1;
        switch (categorie.getSelectionModel().getSelectedItem().toString()) {
            case "Plat":
                Acat_id = 1;
                break;
            case "Boisson":
                Acat_id = 2;
                break;
            case "Dessert":
                Acat_id = 3;
        }
        int Aprix = Integer.parseInt(prix.getText());
        if (Articles.insertArticles(Adesignation, Acat_id, Aspecialite, Aprix)) {
            a.show();
        }

        try {
            ListA = Articles.getArticles();
        } catch (SQLException ex) {
            Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        TableA.setItems(ListA);

    }


    public void deleteArticles() throws SQLException {
        if (TableA.getSelectionModel().isEmpty()) {
            E.setContentText("veuillez selectionner un article!");
            E.showAndWait();
        } else {
            Articles id = TableA.getSelectionModel().getSelectedItem();
            Optional<ButtonType> result = D.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {

                Articles.delArticles(id.getId());
                try {
                    ListA = Articles.getArticles();
                } catch (SQLException ex) {
                    Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
                }
                TableA.setItems(ListA);
            }
        }
    }

    public void openFicheTechnique(Event e) throws IOException {
        FicheTechniqueController.prevroot = "Articles";
        CurrentArticle = TableA.getSelectionModel().getSelectedItem();
        Parent root = load(getClass().getResource("FicheTechnique.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Fiche Technique");
    }

    public void updateArticles(Event e) throws IOException, SQLException {
        if (TableA.getSelectionModel().isEmpty()) {
            E.setContentText("veuillez selectionner un article!");
            E.showAndWait();
        } else {
            int id = Integer.parseInt(articlesid.getText());
            String Adesignation = designation.getText();
            String Aspecialite = specialite.getText();
            int Acat_id = 1;
            switch (categorie.getSelectionModel().getSelectedItem().toString()) {
                case "Plat":
                    Acat_id = 1;
                    break;
                case "Boisson":
                    Acat_id = 2;
                    break;
                case "Dessert":
                    Acat_id = 3;
            }
            int Aprix = Integer.parseInt(prix.getText());
            if (Articles.updateArticles(id, Adesignation, Acat_id, Aspecialite, Aprix)) {
                a.show();
            }

            try {
                ListA = Articles.getArticles();
            } catch (SQLException ex) {
                Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            TableA.setItems(ListA);
        }

    }

    public void checkpanier() throws SQLException {
        if (TableA.getSelectionModel().isEmpty()) {
            panierCB.setDisable(true);
        } else {
            if (panierCB.isSelected())
                Panier.insertPanier(LoginController.CurrentUser.getId(), TableA.getSelectionModel().getSelectedItem().getId());
            else
                Panier.delPanier(LoginController.CurrentUser.getId(), TableA.getSelectionModel().getSelectedItem().getId());
        }
        loadData();
    }

    public void ajouteraupanier() throws SQLException {
        if (!TableA.getSelectionModel().isEmpty()) {
            Panier.insertPanier(LoginController.CurrentUser.getId(), TableA.getSelectionModel().getSelectedItem().getId());
            loadData();
        }
    }

    public void enleverdupanier() throws SQLException {
        if (!TableA.getSelectionModel().isEmpty()) {
            Panier.delPanier(LoginController.CurrentUser.getId(), TableA.getSelectionModel().getSelectedItem().getId());
            ListA = Panier.getPanier(CurrentUser.getId());
            TableA.setItems(ListA);
        }
    }

    public void togglemenupanier() throws SQLException {
        if (toggle.isSelected()) {
            ListA = Panier.getPanier(CurrentUser.getId());
            panierCB.setVisible(false);
        } else {
            ListA = Articles.getArticles();
            panierCB.setVisible(true);
        }
        loadData();

    }

    public void loadData() throws SQLException {
        if (!TableA.getSelectionModel().isEmpty()) {    // si un element du tableau est selectionné
            BtnVoirFT.setDisable(false);
            panierCB.setDisable(false);
            CurrentArticle = TableA.getSelectionModel().getSelectedItem();
            articlesid.setText(Integer.toString(CurrentArticle.getId()));
            designation.setText(CurrentArticle.getDesignation());
            specialite.setText(CurrentArticle.getSpecialite());
            prix.setText(Integer.toString(CurrentArticle.getPrix()));
            categorie.setValue(CurrentArticle.getCategorie());
            panierCB.setSelected(Panier.estdansPanier(LoginController.CurrentUser.getId(), CurrentArticle.getId()));
            // verifier si l'article est dans le panier  - pb : frequent pinging
            if (toggle.isSelected()) {
                mc_ajouteraupanier.setVisible(false);
                mc_enleverdupanier.setVisible(true);

            } else {
                if (Panier.estdansPanier(LoginController.CurrentUser.getId(), CurrentArticle.getId())) {
                    mc_ajouteraupanier.setVisible(false);
                } else {
                    mc_ajouteraupanier.setVisible(true);
                }
                mc_enleverdupanier.setVisible(false);
            }
        } else {                                  // si aucun element n'est selectionné
            mc_ajouteraupanier.setVisible(false);
            mc_enleverdupanier.setVisible(false);
        }
        TableA.setItems(ListA);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (LoginController.CurrentUser.isClientRegulier() || LoginController.CurrentUser.isClientDivers()) {
            BtnAjouter.setVisible(false);
            BtnModifier.setVisible(false);
            BtnSupprimer.setVisible(false);
            pane.setVisible(false);
            if (LoginController.CurrentUser.isClientDivers()) {
                panierCB.setVisible(false);
                toggle.setVisible(false);
                mc_enleverdupanier.setVisible(false);
                mc_ajouteraupanier.setVisible(false);
            }
        }
        mc_enleverdupanier.setVisible(false);
        categorie.getItems().addAll("Plat", "Boisson", "Dessert");

        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<>("designation"));
        c3.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        c4.setCellValueFactory(new PropertyValueFactory<>("specialite"));
        c5.setCellValueFactory(new PropertyValueFactory<>("prix"));

        TableA.getColumns().addAll();
        try {
            ListA = Articles.getArticles();
        } catch (SQLException ex) {
            Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        TableA.setItems(ListA);
        if (TableA.getSelectionModel().isEmpty()) {
            panierCB.setDisable(true);
            BtnVoirFT.setDisable(true);
        }
    }
}