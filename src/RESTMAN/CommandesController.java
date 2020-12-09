package RESTMAN;

import com.sun.javafx.css.converters.BooleanConverter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.BooleanStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static RESTMAN.Commandes.a;
import static RESTMAN.LoginController.CurrentUser;
import static RESTMAN.ParametresController.CurrentParametres;
import static javafx.fxml.FXMLLoader.load;


public class CommandesController implements Initializable {

    @FXML
    Button BtnAjouter;
    @FXML
    Button BtnModifier;
    @FXML
    Button BtnSupprimer;
    @FXML
    Button BtnQuitter;
    @FXML
    TextField commandesid;
    @FXML
    TextField commande_date;
    @FXML
    TextField client_id;
    @FXML
    TextField montant;
    @FXML
    Label prixlivraison;
    @FXML
    ChoiceBox type;
    @FXML
    CheckBox regle_cb;
    @FXML
    CheckBox cloture_cb;
    @FXML
    Pane Pane_prixlivraison;


    ObservableList<Commandes> List = null;

    @FXML
    TableView<Commandes> Table;
    @FXML
    TableColumn<Commandes, Integer> c1;
    @FXML
    TableColumn<Commandes, String> c2;
    @FXML
    TableColumn<Commandes, String> c3;
    @FXML
    TableColumn<Commandes, Integer> c4;
    @FXML
    TableColumn<Commandes, String> c5;
    @FXML
    TableColumn<Commandes, Integer> c6;
    @FXML
    TableColumn<Commandes, Boolean> c7;
    @FXML
    TableColumn<Commandes, Boolean> c8;

    public static Commandes currentCommande;
    public static Alert D = new Alert(Alert.AlertType.CONFIRMATION, "Vous voulez confirmer la suppression?", ButtonType.YES, ButtonType.NO);
//    public static int currentprixlivraison;
    public void CloseCommandes(Event e) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Accueil");
        stage.setResizable(false);
        stage.show();

    }

    public void regler() throws SQLException {
        Commandes cmd = Table.getSelectionModel().getSelectedItem();
        cmd.setReglement(regle_cb.isSelected());
        Commandes.reglerCommandes(cmd.getId(), cmd.getReglement());
        loadData();
    }

    public void cloturer() throws SQLException {
        Commandes cmd = Table.getSelectionModel().getSelectedItem();
        cmd.setOk_cloture(cloture_cb.isSelected());
        Commandes.cloturerCommandes(cmd.getId(), cloture_cb.isSelected());
        loadData();
    }
/*

    public void setprixlivraison() throws SQLException {
        if (ComDist.setPrixlivraison(Integer.parseInt(prixlivraison.getText()))) {
            a.setContentText("prix de livraison est mis à jour");
            a.show();
        }
        currentprixlivraison = Integer.parseInt(prixlivraison.getText());

    }
*/


    public void Vider() {
        commandesid.clear();
        commande_date.clear();
        type.getSelectionModel().clearSelection();
        client_id.clear();
        montant.clear();
    }

    public void addCommandes() throws SQLException {
        int cmdclient_id = CurrentUser.getId();//Integer.parseInt(client_id.getText());
        String cmd_type = type.getSelectionModel().getSelectedItem().toString();
        int Amontant = Integer.parseInt(montant.getText());
        if (Commandes.insertCommandes(cmd_type, cmdclient_id, Amontant)) {
            a.show();
        }

        try {
            List = Commandes.getCommandes();
        } catch (SQLException ex) {
            Logger.getLogger(CommandesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Table.setItems(List);

    }


    public void deleteCommandes() throws SQLException {
        if (Table.getSelectionModel().isEmpty()) {
            a.setContentText("veuillez selectionner une commande!");
            a.showAndWait();
        } else {
            Commandes id = Table.getSelectionModel().getSelectedItem();
            Optional<ButtonType> result = D.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {

                Commandes.delCommandes(id.getId());
                try {
                    List = Commandes.getCommandes();
                } catch (SQLException ex) {
                    Logger.getLogger(CommandesController.class.getName()).log(Level.SEVERE, null, ex);
                }
                Table.setItems(List);
            }
        }

    }

    public void updateCommandes(Event e) throws IOException, SQLException {

        int id = Integer.parseInt(commandesid.getText());
        //Date cmd_date = now;
        //int cmdclient_id = client_id.getText();
        int cmdclient_id = CurrentUser.getId();
        String cmd_type = type.getSelectionModel().getSelectedItem().toString();
        int cmdmontant = Integer.parseInt(montant.getText());

        if (Commandes.updateCommandes(id, cmd_type, cmdclient_id, cmdmontant)) {
            a.show();
        }

        try {
            List = Commandes.getCommandes();
        } catch (SQLException ex) {
            Logger.getLogger(CommandesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Table.setItems(List);

    }


    public void loadData() throws SQLException {
        if (!Table.getSelectionModel().isEmpty()) {
            Commandes cmd = Table.getSelectionModel().getSelectedItem();
            if (cmd.getReglement()) regle_cb.setSelected(true);
            else regle_cb.setSelected(false);
            if (cmd.getOk_cloture()) {                  //si une commande est cloturé, l'utilisateur ne peut pas la modifier ou la supprimer
                cloture_cb.setSelected(true);
                BtnModifier.setDisable(true);
                BtnSupprimer.setDisable(true);
            } else {
                cloture_cb.setSelected(false);
                BtnModifier.setDisable(false);
                BtnSupprimer.setDisable(false);
            }
            Table.refresh();
            Table.getItems();
        }
    }

    public void OpenLigneCommandeAjout(Event e) throws IOException {
        currentCommande = null;

        Parent root = load(getClass().getResource("LigneCommande.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("LigneCommande");
    }

    public void OpenLigneCommandeModif(Event e) throws IOException {
        if (Table.getSelectionModel().isEmpty()) {
            a.setContentText("veuillez selectionner une commande!");
            a.showAndWait();
        }
        //   currentCommande = new Commandes();
        else {
            currentCommande = Table.getSelectionModel().getSelectedItem();

            Parent root = load(getClass().getResource("LigneCommande.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("LigneCommande");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (CurrentUser.isClientRegulier() || CurrentUser.isClientDivers()) {
            cloture_cb.setVisible(false);
            regle_cb.setVisible(false);
          //  Pane_prixlivraison.setVisible(false);
        }
        prixlivraison.setText(Integer.toString(CurrentParametres.getPrixlivraison()));

        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<>("cmd_date"));
        c3.setCellValueFactory(new PropertyValueFactory<>("type"));
        c4.setCellValueFactory(new PropertyValueFactory<>("client_id"));
        c5.setCellValueFactory(new PropertyValueFactory<>("client_nom"));
        c6.setCellValueFactory(new PropertyValueFactory<>("montant"));
        c7.setCellValueFactory(new PropertyValueFactory<>("reglement"));
        //  c7.setCellFactory(CheckBoxTableCell.forTableColumn(c7));
        // c7.setEditable(true);
        c8.setCellValueFactory(new PropertyValueFactory<>("ok_cloture"));
        //c8.setCellFactory(CheckBoxTableCell.forTableColumn(c8));
        //c8.setEditable(true);

        try {
            List = Commandes.getCommandes();
        } catch (SQLException ex) {
            Logger.getLogger(CommandesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Table.setItems(List);
        Table.getColumns().addAll();
        c2.setSortType(TableColumn.SortType.DESCENDING);
        Table.getSortOrder().add(c2);

    }
}