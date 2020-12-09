package RESTMAN;

import com.sun.deploy.util.Waiter;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static RESTMAN.Commandes.a;
import static RESTMAN.LoginController.CurrentUser;
import static javafx.fxml.FXMLLoader.load;
import static javafx.fxml.FXMLLoader.setDefaultClassLoader;

public class ParametresController implements Initializable {
    @FXML
    private TextField TF_seuil_nbr_cmd;

    @FXML
    private TextField TF_prixlivraison;

    @FXML
    private TextField TF_seuil_val_cmd;

    @FXML
    AnchorPane ParametresPane;

    @FXML
    private Button BtnValider;

    public static Parametres CurrentParametres = null;



    public static void setCurrentParametres() throws SQLException {
        CurrentParametres = Parametres.getParametres();
    }

    public void ValiderParametres() throws SQLException {
        if (Parametres.updateParametres(Integer.parseInt(TF_prixlivraison.getText()), Integer.parseInt(TF_seuil_nbr_cmd.getText()), Integer.parseInt(TF_seuil_val_cmd.getText()))) {
            a.setContentText("Les parametres sont mis à jour");
            a.show();
        }
        CurrentParametres = new Parametres(Integer.parseInt(TF_prixlivraison.getText()), Integer.parseInt(TF_seuil_nbr_cmd.getText()), Integer.parseInt(TF_seuil_val_cmd.getText()));
        loadData();
    }

    private void loadData(){
        TF_prixlivraison.setText(Integer.toString(CurrentParametres.getPrixlivraison()));
        TF_seuil_nbr_cmd.setText(Integer.toString(CurrentParametres.getSeuil_nbr_cmd()));
        TF_seuil_val_cmd.setText(Integer.toString(CurrentParametres.getSeuil_val_cmd()));
    }
/*
    public void CloseParametres(Event e) throws IOException {
        AnchorPane pane = (AnchorPane) ((Node) e.getSource()).getScene().getRoot();
        pane.getChildren().remove(ParametresPane);
    }*/

public void CloseParametres(Event e) throws IOException{
    FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(500), ParametresPane);
    fadeOutTransition.setFromValue(1.0);
    fadeOutTransition.setToValue(0.0);
    fadeOutTransition.play();
    fadeOutTransition.setOnFinished((ActionEvent e1) -> {
        try {
            CloseParametres2(e,e1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    });

}
public void CloseParametres2(Event e,Event e1) throws IOException{
    Parent root = load(getClass().getResource("MainMenu.fxml"));
    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.centerOnScreen();
    stage.setTitle("Home");
}
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(500),ParametresPane);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);
        fadeInTransition.play();
  //      ParametresPane.setFocusTraversable(false);

        loadData();

    }


}
