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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static RESTMAN.LoginController.CurrentUser;
import static RESTMAN.Users.a;

public class ProfileController implements Initializable {


    @FXML
    private Label userid;
    @FXML
    private Label usertype;
    @FXML
    private TextField username;
    @FXML
    private PasswordField MDP;
    @FXML
    private TextField TF_nom;
    @FXML
    private TextField TF_prenom;
    @FXML
    private TextField TF_tel;
    @FXML
    private TextField TF_email;
    @FXML
    private TextField TF_adresse;
    @FXML
    private HBox HB;
    @FXML
    private VBox VB_nbr_cmd;
    @FXML
    private VBox VB_val_cmd;
    @FXML
    private VBox VB_nbr_bonus;
    @FXML
    private Label Txt_nbr_cmd;
    @FXML
    private Label Label_nbr_cmd;
    @FXML
    private Label Label_val_cmd;
    @FXML
    private Label Label_nbr_bonus;

    ObservableList<Users> listU = FXCollections.observableArrayList();

    public void CloseProfile(Event e) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Accueil");
        stage.setResizable(false);
        stage.show();

    }


    public void updateusers(Event e) throws IOException, SQLException {
        if (username.getText().isEmpty() || MDP.getText().isEmpty() || TF_nom.getText().isEmpty() || TF_prenom.getText().isEmpty()) {
            a.setContentText("veuillez remplir les champs importants (*)!");
            a.showAndWait();
        } else {
            int id = Integer.parseInt(userid.getText());
            String Uname = username.getText();
            String Utype = CurrentUser.getUsertype();
            String password = MDP.getText();
            String nom = TF_nom.getText();
            String prenom = TF_prenom.getText();
            String tel = TF_tel.getText();
            String email = TF_email.getText();
            String adrese = TF_adresse.getText();

            if (Users.updateUsers(id, Utype, Uname, password, nom, prenom, tel, email, adrese)) {
                a.setContentText("Le profile est mis à jour avec succés!");
                a.show();
            }

            try {
                listU = Users.getUsers();
            } catch (SQLException ex) {
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
            }
            CurrentUser = Users.getCurrentUser(CurrentUser.getUsername());
        }
    }


    public void loadData() throws SQLException {
        if (CurrentUser.isClientRegulier()) {    // in case client
            Client selectedUser = Client.getClient(CurrentUser.getId());
            userid.setText(Integer.toString(selectedUser.getId()));
            username.setText(selectedUser.getUsername());
            usertype.setText(selectedUser.getUsertype());
            MDP.setText(selectedUser.getPassword());
            TF_nom.setText(selectedUser.getNom());
            TF_prenom.setText(selectedUser.getPrenom());
            TF_tel.setText(selectedUser.getTel());
            TF_email.setText(selectedUser.getEmail());
            TF_adresse.setText(selectedUser.getAdresse());
            Label_nbr_cmd.setText(Integer.toString(selectedUser.getNbr_cmd()));
            Label_val_cmd.setText(Integer.toString(selectedUser.getVal_cmd()));
            Label_nbr_bonus.setText(Integer.toString(selectedUser.getNbr_bonus()));
        } else {                         // in case livreur
            Livreur selectedUser = Livreur.getLivreur(CurrentUser.getId());
            userid.setText(Integer.toString(selectedUser.getId()));
            username.setText(selectedUser.getUsername());
            usertype.setText(selectedUser.getUsertype());
            MDP.setText(selectedUser.getPassword());
            TF_nom.setText(selectedUser.getNom());
            TF_prenom.setText(selectedUser.getPrenom());
            TF_tel.setText(selectedUser.getTel());
            TF_email.setText(selectedUser.getEmail());
            TF_adresse.setText(selectedUser.getAdresse());
            Txt_nbr_cmd.setText("Nombre de livraisons");
            Label_nbr_cmd.setText(Integer.toString(selectedUser.getNbr_liv()));
            VB_nbr_bonus.setVisible(false);
            VB_val_cmd.setVisible(false);
        }
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}