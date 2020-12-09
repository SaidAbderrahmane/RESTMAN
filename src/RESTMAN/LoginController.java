package RESTMAN;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static javafx.fxml.FXMLLoader.load;

public class LoginController implements Initializable {
    public static Users CurrentUser = null;
    @FXML
    TextField username;
    @FXML
    PasswordField password;

    public void login(Event e) throws IOException, SQLException {

        if (Users.login_check(username.getText(), password.getText())) {
            CurrentUser = Users.getCurrentUser(username.getText());
            Parent root = load(getClass().getResource("MainMenu.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Accueil");

        } else {
            new Alert(Alert.AlertType.ERROR, "Nom d'utilisateur/mot de passe incorrecte !", ButtonType.OK).show();
        }

    }
    public void diverslogin(Event e) throws IOException, SQLException {
            CurrentUser = Users.getCurrentUser("divers");
            Parent root = load(getClass().getResource("MainMenu.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Accueil");
    }

    public void exit(Event e) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
