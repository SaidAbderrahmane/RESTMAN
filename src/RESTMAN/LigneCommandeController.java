package RESTMAN;

import javafx.collections.FXCollections;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static RESTMAN.ArticlesController.ListA;
import static RESTMAN.Commandes.getLastCommande;
import static RESTMAN.CommandesController.currentCommande;
import static RESTMAN.ArticlesController.CurrentArticle;
import static RESTMAN.DB.*;
import static RESTMAN.LoginController.CurrentUser;
import static RESTMAN.ParametresController.CurrentParametres;
import static javafx.fxml.FXMLLoader.load;

public class LigneCommandeController implements Initializable {

    public static Alert D = new Alert(Alert.AlertType.CONFIRMATION, "Vous voulez confirmer la suppression?", ButtonType.YES, ButtonType.NO);
    @FXML
    Button BtnAjouter;
    @FXML
    Button BtnModifier;
    @FXML
    Button BtnSupprimer;
    @FXML
    Button BtnQuitter;
    @FXML
    Label Label_montantTotal;
    @FXML
    Label Label_prixlivraison;
    @FXML
    Label Label_id_cmd;
    @FXML
    ChoiceBox type;
    @FXML
    Button BtnVider;
    ObservableList<Commandes> List = null;
    ObservableList<Articles> List_menu = null;
    ObservableList<LigneCommande> List_ligne_cmd = FXCollections.observableArrayList();
    @FXML
    TableView<LigneCommande> Table_cmd;
    @FXML
    TableView<Articles> Table_menu;
    @FXML
    TableColumn<LigneCommande, Integer> c1;
    @FXML
    TableColumn<LigneCommande, String> c2;
    @FXML
    TableColumn<LigneCommande, Number> c3;
    @FXML
    TableColumn<LigneCommande, Integer> c4;
    @FXML
    TableColumn<LigneCommande, Number> c5;
    @FXML
    TableColumn<Articles, Integer> menu_c1;
    @FXML
    TableColumn<Articles, String> menu_c2;
    @FXML
    TableColumn<Articles, String> menu_c3;
    @FXML
    TableColumn<Articles, String> menu_c4;
    @FXML
    TableColumn<Articles, Number> menu_c5;
    @FXML
    ToggleButton toggle;

    public void CloseLigneCommande(Event e) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("Commandes.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Commandes");
        stage.setResizable(false);
        stage.show();

    }

    public void openFicheTechnique(Event e) throws IOException {
        ListA = List_menu;
        FicheTechniqueController.prevroot = "LigneCommande";
        CurrentArticle = Table_menu.getSelectionModel().getSelectedItem();
        Parent root = load(getClass().getResource("FicheTechnique.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Fiche Technique");
    }


    public Integer findLigneByArticle_id(ObservableList<LigneCommande> list, int id) {
        for (LigneCommande Lcmd : list) {
            if (Lcmd.getArticle_id() == id) {
                return list.indexOf(Lcmd);
            }
        }
        return null;
    }

    public void addToCarte() throws SQLException {
        if (Table_menu.getSelectionModel().isEmpty()) {
            a.setContentText("veuillez selectionner un article!");
            a.showAndWait();
        } else {
            Articles Article = Table_menu.getSelectionModel().getSelectedItem();
            LigneCommande ligne = null;
            if (findLigneByArticle_id(List_ligne_cmd, Article.getId()) != null) {
                ligne = List_ligne_cmd.get(findLigneByArticle_id(List_ligne_cmd, Article.getId()));
                ligne.setQte(ligne.getQte() + 1);
                ligne.setMontantL();
                List_ligne_cmd.set(findLigneByArticle_id(List_ligne_cmd, Article.getId()), ligne);
            } else {
                List_ligne_cmd.add(new LigneCommande(0, 0, Article.getId(), Article.getDesignation(), Article.getPrix(), 1));
            }
            Table_cmd.setItems(List_ligne_cmd);
            Label_montantTotal.setText(Integer.toString(CalcMontantTotal()));
        }
    }


    public void RemovefromCarte() throws SQLException {
        if (Table_cmd.getSelectionModel().isEmpty()) {
            a.setContentText("veuillez selectionner une ligne!");
            a.show();
        } else {
            LigneCommande Article = Table_cmd.getSelectionModel().getSelectedItem();
            LigneCommande ligne = null;
            ligne = List_ligne_cmd.get(findLigneByArticle_id(List_ligne_cmd, Article.getArticle_id()));
            if (ligne.getQte() > 1) {
                ligne.setQte(ligne.getQte() - 1);
                ligne.setMontantL();
                List_ligne_cmd.set(findLigneByArticle_id(List_ligne_cmd, Article.getArticle_id()), ligne);
            } else
                List_ligne_cmd.remove(ligne);

            Table_cmd.setItems(List_ligne_cmd);
            Label_montantTotal.setText(Integer.toString(CalcMontantTotal()));
        }
    }

    public void vider() throws SQLException {
        List_ligne_cmd.clear();
        Table_cmd.setItems(List_ligne_cmd);
        Label_montantTotal.setText(Integer.toString(CalcMontantTotal()));

    }

    public int CalcMontantTotal() {
        int Total = 0;
        for (LigneCommande ligne : List_ligne_cmd) {
            Total = Total + ligne.getMontantL();
        }
        if (!type.getSelectionModel().isEmpty()) {
            if (type.getSelectionModel().getSelectedItem().toString().equals("A distance"))
                return (Total + CurrentParametres.getPrixlivraison());
        }
        return Total;
    }

    public boolean addLigneCommande() throws SQLException {
        if (List_ligne_cmd.isEmpty()) {
            a.setContentText("veuillez choisir des articles pour pouvoir valider la commande!");
            a.show();
            return false;
        } else {
            for (LigneCommande ligne : List_ligne_cmd) {
                LigneCommande.insertLigneCommande(currentCommande.getId(), ligne.getArticle_id(), ligne.getQte(), ligne.getMontantL());
            }
        }

        return true;
    }

    public boolean updateLigneCommande() throws SQLException {
        if (List_ligne_cmd.isEmpty()) {
            a.setContentText("veuillez choisir des articles pour pouvoir valider la commande!");
            a.show();
            return false;
        } else {
            LigneCommande.delLigneCommande(currentCommande.getId());
            for (LigneCommande ligne : List_ligne_cmd) {
                LigneCommande.insertLigneCommande(currentCommande.getId(), ligne.getArticle_id(), ligne.getQte(), ligne.getMontantL());
            }
        }

        return true;
    }

    public boolean addCommandes() throws SQLException {
        int cmdclient_id = CurrentUser.getId();//Integer.parseInt(client_id.getText());
        if (type.getSelectionModel().isEmpty()) {
            a.setContentText("veuillez choisir le type de commande!");
            a.show();
            return false;
        } else {
            String cmd_type = type.getSelectionModel().getSelectedItem().toString();
            int montantTotal = CalcMontantTotal();
            Commandes.insertCommandes(cmd_type, cmdclient_id, montantTotal);

        }
        currentCommande = getLastCommande();
        return true;
    }

    public boolean updateCommandes(Event e) throws SQLException {
        int cmdclient_id = CurrentUser.getId();//Integer.parseInt(client_id.getText());
        if (type.getSelectionModel().isEmpty()) {
            a.setContentText("veuillez choisir le type de commande!");
            a.show();
            return false;
        } else {
            String cmd_type = type.getSelectionModel().getSelectedItem().toString();
            int montantTotal = CalcMontantTotal();
            Commandes.updateCommandes(currentCommande.getId(), cmd_type, cmdclient_id, montantTotal);
        }
        currentCommande = Commandes.getCommandeById(currentCommande.getId());
        return true;
    }

    public void OpenComDist(Event e) throws IOException {
        Parent root = load(getClass().getResource("ComDist.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Formulaire de liviraison");
    }


    public void ValiderCommande(Event e) throws IOException, SQLException {
        if (currentCommande == null) {
            if (addCommandes() && addLigneCommande()) {
                if (currentCommande.getType().equals("A distance")) {
                    OpenComDist(e);
                } else {
                    a.setContentText("commande ajouté avec succès!");
                    a.show();
                    CloseLigneCommande(e);
                }
            }
        } else {
            if (updateCommandes(e) && updateLigneCommande()) {
                if (currentCommande.getType().equals("A distance")) {
                    OpenComDist(e);
                } else {
                    ComDist.delComDist(currentCommande.getId());
                    a.setContentText("changements effectués avec succès!");
                    a.show();
                    CloseLigneCommande(e);
                }
            }
        }
    }

    public void togglemenupanier() throws SQLException {
        if (toggle.isSelected())
            List_menu = Panier.getPanier(CurrentUser.getId());
        else List_menu = Articles.getArticles();
        Table_menu.setItems(List_menu);
    }

    public void loadData() throws SQLException {
        //List_menu = Articles.getArticles();
        //  Table_menu.setItems(List_menu);
        //     List_ligne_cmd = LigneCommande.getLigneCommande(currentCommande.getId());
        //   Table_cmd.setItems(List_ligne_cmd);
        if (!type.getSelectionModel().isEmpty()) {
            if (type.getSelectionModel().getSelectedItem().equals("A distance"))
                Label_prixlivraison.setText(String.valueOf(CurrentParametres.getPrixlivraison()));
            else Label_prixlivraison.setText("       - ");
            Label_montantTotal.setText(Integer.toString(CalcMontantTotal()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        type.getItems().addAll("Sur place", "A distance");
        Table_cmd.setItems(List_ligne_cmd);

        if (CurrentUser.isClientDivers())
            toggle.setVisible(false);

        menu_c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        menu_c2.setCellValueFactory(new PropertyValueFactory<>("designation"));
        menu_c3.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        menu_c4.setCellValueFactory(new PropertyValueFactory<>("specialite"));
        menu_c5.setCellValueFactory(new PropertyValueFactory<>("prix"));


        c1.setCellValueFactory(new PropertyValueFactory<>("article_id"));
        c2.setCellValueFactory(new PropertyValueFactory<>("article_des"));
        c3.setCellValueFactory(new PropertyValueFactory<>("prix"));
        c4.setCellValueFactory(new PropertyValueFactory<>("qte"));
        c5.setCellValueFactory(new PropertyValueFactory<>("montantL"));

        Table_cmd.getColumns().addAll();
        Table_menu.getColumns().addAll();
        setEditable(); // set qte column editable
        try {
            List_menu = Articles.getArticles();
            if (currentCommande != null)
                List_ligne_cmd = LigneCommande.getLigneCommande(currentCommande.getId());
        } catch (SQLException ex) {
            Logger.getLogger(CommandesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Table_menu.setItems(List_menu);
        Table_cmd.setItems(List_ligne_cmd);
        Label_montantTotal.setText(Integer.toString(CalcMontantTotal()));
        if (currentCommande != null) { // setting labels
            type.setValue(currentCommande.getType());
            if (currentCommande.getType().equals("A distance")) ComDistController.comdistupdate = true;
            Label_id_cmd.setText(Integer.toString(currentCommande.getId()));
            // label prix livraison
            if (currentCommande.getType().equals("A distance"))
                Label_prixlivraison.setText(String.valueOf(CurrentParametres.getPrixlivraison()));
            else Label_prixlivraison.setText("       - ");
        } else {
            Label_id_cmd.setText("N/A");
        }

    }

    private void setEditable() {
        c4.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        c4.setOnEditCommit(e -> {
            /*e.getTableView().getItems().get(e.getTablePosition().getRow()).setQte(e.getNewValue());
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setMontantL();*/
            e.getTableView().getSelectionModel().getSelectedItem().setQte(e.getNewValue());
            e.getTableView().getSelectionModel().getSelectedItem().setMontantL();
            List_ligne_cmd.set(e.getTableView().getSelectionModel().getFocusedIndex(), e.getTableView().getSelectionModel().getSelectedItem());
            Table_cmd.setItems(List_ligne_cmd);
        });
        Table_cmd.setEditable(true);
    }
}