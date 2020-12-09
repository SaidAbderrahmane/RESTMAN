package RESTMAN;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static RESTMAN.CommandesController.currentCommande;
import static RESTMAN.DB.a;

public class ComDistController implements Initializable {

    @FXML
    Button BtnValider;
    @FXML
    TextField ville;
    @FXML
    TextField adresse1;
    @FXML
    TextField adresse2;
    @FXML
    TextField code_postal;
    @FXML
    TextField tel;

    public static boolean comdistupdate;
    public static Alert D = new Alert(Alert.AlertType.CONFIRMATION, "Vous voulez confirmer la suppression?", ButtonType.YES, ButtonType.NO);

    public void CloseComDist(Event e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Commandes.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Commandes");
        stage.setResizable(false);
        stage.show();
    }

    public void addComDist(Event e) throws SQLException, IOException {
        int id = currentCommande.getId();
        String Cville = ville.getText();
        String Cadresse1 = adresse1.getText();
        String Cadresse2 = adresse2.getText();
        int Ccode_postal = Integer.parseInt(code_postal.getText());
        String Ctel = tel.getText();

        if (comdistupdate != true) {

            if (ComDist.insertComDist(id, Cville, Cadresse1, Cadresse2, Ccode_postal, Ctel)) {
                a.setContentText("Commande a distance ajouté avec succès!");
                a.show();
                CloseComDist(e);
            }
        } else {

            if (ComDist.updateComDist(id, Cville, Cadresse1, Cadresse2, Ccode_postal, Ctel)) {
                a.setContentText("Changements effectués avec succès!");
                a.show();
                CloseComDist(e);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (comdistupdate == true) {
            try {
                ComDist comdist = ComDist.getComDistById(currentCommande.getId());
                if (comdist != null) {
                    ville.setText(comdist.getVille());
                    adresse1.setText(comdist.getAdresse1());
                    adresse2.setText(comdist.getAdresse2());
                    code_postal.setText(Integer.toString(comdist.getCode_postal()));
                    tel.setText(comdist.getTel());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
