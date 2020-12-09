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
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static RESTMAN.DB.a;

public class UsersController implements Initializable {

    @FXML
    private Label userid;
    @FXML
    private ChoiceBox usertype;
    @FXML
    private TextField username;
    @FXML
    private PasswordField MDP;

    ObservableList<Users> listU = null;
    @FXML
    private TableView<Users> tableU;
    @FXML
    private TableColumn<Users, Integer> c1;
    @FXML
    private TableColumn<Users, String> c2;
    @FXML
    private TableColumn<Users, String> c3;
    @FXML
    private TableColumn<Users, String> c4;
    @FXML
    private TableColumn<Users, String> c5;
    @FXML
    private TableColumn<Users, String> c6;
    @FXML
    private TableColumn<Users, String> c7;
    @FXML
    private TableColumn<Users, String> c8;

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

    public static Alert D = new Alert(Alert.AlertType.CONFIRMATION, "Vous voulez confirmer la suppression?", ButtonType.YES, ButtonType.NO);

    public void CloseUsers(Event e) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Accueil");
        stage.setResizable(false);
        stage.show();

    }

    public void Vider() {
        userid.setText("-");
        username.clear();
        usertype.getSelectionModel().clearSelection();
        MDP.clear();
        TF_nom.clear();
        TF_prenom.clear();
        TF_tel.clear();
        TF_email.clear();
        TF_adresse.clear();
    }

    public void addUsers() throws SQLException {
        String Uname = username.getText();
        String Utype = usertype.getSelectionModel().getSelectedItem().toString();
        String password = MDP.getText();
        String nom = TF_nom.getText();
        String prenom = TF_prenom.getText();
        String tel = TF_tel.getText();
        String email = TF_email.getText();
        String adresse = TF_adresse.getText();

        if (Users.insertUsers(Uname,Utype, password,  nom, prenom, tel, email, adresse)) {
            a.show();
        }
        try {
            listU = Users.getUsers();
        } catch (SQLException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableU.setItems(listU);

    }


    public void deleteUsers() throws SQLException {
        if (tableU.getSelectionModel().isEmpty()) {
            a.setContentText("veuillez selectionner un utilisateur!");
            a.showAndWait();
        } else {
            Users id = tableU.getSelectionModel().getSelectedItem();
            Optional<ButtonType> result = D.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {

                Users.delUsers(id.getId());
                try {
                    listU = Users.getUsers();
                } catch (SQLException ex) {
                    Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
                }
                tableU.setItems(listU);
            }
        }
    }

    public void updateusers(Event e) throws IOException, SQLException {
        if (tableU.getSelectionModel().isEmpty()) {
            a.setContentText("veuillez selectionner un utilisateur!");
            a.showAndWait();
        } else {
            int id = Integer.parseInt(userid.getText());
            String Uname = username.getText();
            String Utype = usertype.getSelectionModel().getSelectedItem().toString();
            String password = MDP.getText();
            String nom = TF_nom.getText();
            String prenom = TF_prenom.getText();
            String tel = TF_tel.getText();
            String email = TF_email.getText();
            String adrese = TF_adresse.getText();

            if (Users.updateUsers(id, Utype, Uname, password, nom, prenom, tel, email, adrese)) {
                a.setContentText("Changements effectués avec succés!");
                a.show();
            }

            try {
                listU = Users.getUsers();
            } catch (SQLException ex) {
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
            }
            tableU.setItems(listU);
            if (username.getText().equals(LoginController.CurrentUser.getUsername()))       // update currentuser
                LoginController.CurrentUser = Users.getCurrentUser(LoginController.CurrentUser.getUsername());
        }
    }


    public void loadData() throws SQLException {
        Users selectedUser = null;
        if (!tableU.getSelectionModel().isEmpty()) {
            selectedUser = tableU.getSelectionModel().getSelectedItem();
            userid.setText(Integer.toString(selectedUser.getId()));
            username.setText(selectedUser.getUsername());
            usertype.setValue(selectedUser.getUsertype());
            MDP.setText(selectedUser.getPassword());
            TF_nom.setText(selectedUser.getNom());
            TF_prenom.setText(selectedUser.getPrenom());
            TF_tel.setText(selectedUser.getTel());
            TF_email.setText(selectedUser.getEmail());
            TF_adresse.setText(selectedUser.getAdresse());

        }
      //  tableU.setItems(listU);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        usertype.getItems().addAll("Admin", "Livreur", "Client regulier", "Client divers");

        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<>("usertype"));
        c3.setCellValueFactory(new PropertyValueFactory<>("username"));
        c4.setCellValueFactory(new PropertyValueFactory<>("nom"));
        c5.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        c6.setCellValueFactory(new PropertyValueFactory<>("tel"));
        c7.setCellValueFactory(new PropertyValueFactory<>("email"));
        c8.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        tableU.getColumns().addAll();
        try {
            listU = Users.getUsers();
        } catch (SQLException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableU.setItems(listU);
    }
}