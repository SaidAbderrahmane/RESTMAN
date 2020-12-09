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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static RESTMAN.ComDist.a;
import static RESTMAN.MainMenuController.currenttime;

public class LivraisonController implements Initializable {
    @FXML
    Pane livreur_pane;
    @FXML
    Button BtnQuitter;
    @FXML
    ComboBox<String> livreur_combo;
    @FXML
    TextField tps_restant;
    @FXML
    CheckBox recu_cb;
    @FXML
    DatePicker date_rec;
    @FXML
    Slider etat_slider;
    @FXML
    Label date_depart_label;
    @FXML
    Label tps_restant_label;

    ObservableList<ComDist> List = null;
    ObservableList<Users> Listlivreur = null;
    @FXML
    TableView<ComDist> Table;
    @FXML
    TableColumn<ComDist, Integer> c1;
    @FXML
    TableColumn<ComDist, String> c2;
    @FXML
    TableColumn<ComDist, String> c3;
    @FXML
    TableColumn<ComDist, String> c4;
    @FXML
    TableColumn<ComDist, Integer> c5;
    @FXML
    TableColumn<ComDist, String> c6;
    @FXML
    TableColumn<ComDist, String> c7;
    @FXML
    TableColumn<ComDist, Boolean> c8;
    @FXML
    TableColumn<ComDist, String> c9;
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd 24HH:mm");

    public static Alert D = new Alert(Alert.AlertType.CONFIRMATION, "Vous voulez confirmer la suppression?", ButtonType.YES, ButtonType.NO);

    public void CloseLivraison(Event e) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Accueil");
        stage.setResizable(false);
        stage.show();

    }


    public void updateLivraison(Event e) throws IOException, SQLException {
        if (Table.getSelectionModel().isEmpty()) {                                              //verifier s'il y a un element selectionné
            a.setContentText("veuillez selectionner une commande!");
            a.showAndWait();
        } else if (livreur_combo.getSelectionModel().isEmpty() || tps_restant.getText() == null) {  //verifier s'il les champs sont remplis
            a.setContentText("veuillez remplir les champs!");
            a.showAndWait();
        } else {

            ComDist cmd = Table.getSelectionModel().getSelectedItem();
            int id = cmd.getId();
            int liv_id = 0;
            String liv_nom = livreur_combo.getSelectionModel().getSelectedItem();
            for (Users liv : Listlivreur) {
                if (liv.getUsername().equals(liv_nom))
                    liv_id = liv.getId();
            }

            String etatliv = null;
            switch ((int) etat_slider.getValue()) {
                case 1:
                    etatliv = "passee";
                    break;
                case 2:
                    etatliv = "preparation";
                    break;
                case 3:
                    etatliv = "expediee";
                    break;
                case 4:
                    etatliv = "arrivee";

            }
            int temps_restant = Integer.parseInt(tps_restant.getText());

            if (etatliv.equals("expediee")) {
                ComDist.insertDateDepart(cmd.getId());
            }
            if (ComDist.updateLivraison(id, liv_id, etatliv, temps_restant)) {
                a.setContentText("livraison mis a jour!");
                a.show();
            }

            try {
                List = ComDist.getComDist();
            } catch (SQLException ex) {
                Logger.getLogger(ComDistController.class.getName()).log(Level.SEVERE, null, ex);
            }
            loadData();
        }
    }

    public void mettreRecu() throws SQLException {
        ComDist cmd = Table.getSelectionModel().getSelectedItem();
        if (recu_cb.isSelected()) {
            cmd.setOk_recu(true);
            cmd.setEtat_livraison("arrivee");
            cmd.setTemps_restant(0);
        } else {
            cmd.setOk_recu(false);
            cmd.setDate_reception(null);
            cmd.setEtat_livraison("passee");
            cmd.setTemps_restant(0);
        }
        ComDist.ComDistRecu(cmd.getId(), recu_cb.isSelected());
        Commandes.cloturerCommandes(cmd.getId(), recu_cb.isSelected());   // si une commande est reçu -> elle est cloturée
        try {
            List = ComDist.getComDist();
        } catch (SQLException ex) {
            Logger.getLogger(ComDistController.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadData();
        recu_cb.setDisable(true);

    }


    public void loadData() {
        Table.setItems(List);
        ComDist cmd = Table.getSelectionModel().getSelectedItem();
        if (!Table.getSelectionModel().isEmpty()) {       // verifier s'il y a un element selectionné
            if (!LoginController.CurrentUser.isClientRegulier() && !LoginController.CurrentUser.isClientDivers()) {

                recu_cb.setDisable(false);
                if (cmd.getOk_recu()) {                              //mettre reçu
                    recu_cb.setSelected(true);
                    tps_restant.setText("0");
                    tps_restant.setDisable(true);
                    etat_slider.setDisable(true);
                } else {// mettre non reçu
                    livreur_combo.setDisable(false);
                    recu_cb.setSelected(false);
                    tps_restant.clear();
                    tps_restant.setDisable(false);
                    etat_slider.setDisable(false);
                }
            }
            if(cmd.getDate_depart() == null)
            date_depart_label.setText("      -   ");
            else  date_depart_label.setText(cmd.getDate_depart());
            if (Integer.toString(cmd.getTemps_restant()).equals("0"))
                tps_restant_label.setText("  - ");
            else tps_restant_label.setText(Integer.toString(cmd.getTemps_restant()));   // MAJ le label temps restant
            tps_restant.setText(Integer.toString(cmd.getTemps_restant()));
            livreur_combo.setValue(cmd.getLivreur_nom());
            if (cmd.getEtat_livraison() != null) {                        // MAJ le slider de etat_livraison
                switch ((cmd.getEtat_livraison())) {
                    case "passee":
                        etat_slider.setValue(1);
                        break;
                    case "preparation":
                        etat_slider.setValue(2);
                        break;
                    case "expediee":
                        etat_slider.setValue(3);
                        break;
                    case "arrivee":
                        etat_slider.setValue(4);

                }
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (LoginController.CurrentUser.isClientRegulier()) {
            livreur_pane.setVisible(false);
            etat_slider.setDisable(true);
        } else {
            if (Table.getSelectionModel().isEmpty()) {  // verifier s'il y a un element selectionné
                tps_restant.setDisable(true);
                etat_slider.setDisable(true);
                livreur_combo.setDisable(true);
                recu_cb.setDisable(true);
            }
        }

        try {
            Listlivreur = Users.getLivreurs();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        livreur_combo.setEditable(true);
        ObservableList<String> listliv = FXCollections.observableArrayList();
        for (Users liv : Listlivreur) {
            listliv.add(liv.getUsername());
        }
        livreur_combo.setItems(listliv);    ///////////// not exactly working since nom isn't unique!!!!!!!!


        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<>("ville"));
        c3.setCellValueFactory(new PropertyValueFactory<>("adresse1"));
        c4.setCellValueFactory(new PropertyValueFactory<>("adresse2"));
        c5.setCellValueFactory(new PropertyValueFactory<>("code_postal"));
        c6.setCellValueFactory(new PropertyValueFactory<>("tel"));
        c7.setCellValueFactory(new PropertyValueFactory<>("livreur_nom"));
        c8.setCellValueFactory(new PropertyValueFactory<>("ok_recu"));
        c9.setCellValueFactory(new PropertyValueFactory<>("date_reception"));

        Table.getColumns().addAll();
        try {
            List = ComDist.getComDist();
        } catch (SQLException ex) {
            Logger.getLogger(ComDistController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Table.setItems(List);

    }
}


