package RESTMAN;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.xml.internal.bind.WhiteSpaceProcessor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import static RESTMAN.LoginController.CurrentUser;
import static RESTMAN.Parametres.getParametres;
import static RESTMAN.ParametresController.CurrentParametres;
import static RESTMAN.ParametresController.setCurrentParametres;
import static javafx.fxml.FXMLLoader.load;
import static javafx.scene.paint.Color.*;

public class MainMenuController implements Initializable {
    @FXML
    Button Users, Articles, Stats, Livraison, BtnParametres, Commandes;
    @FXML
    AnchorPane rootPane;
    @FXML
    AnchorPane rootPane2;
    @FXML
    Text nom, date;
    @FXML
    VBox vb;
    @FXML
    Pane Pane_recommendation;
    public static Stage MainStage;
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static Date now = new Date();
    public static String currenttime = formatter.format(now);


    public void Logout(Event e) throws IOException {
        Alert D = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous déconnecter?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = D.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {

            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.show();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (CurrentUser.isClientDivers() || CurrentUser.isClientRegulier()) {
            Users.setVisible(false);
            Stats.setVisible(false);
            BtnParametres.setVisible(false);
            if (CurrentUser.isClientDivers())
                Livraison.setVisible(false);
        }
        if (CurrentUser.isLivreur()) {
            Pane_recommendation.setVisible(false);
            Articles.setVisible(false);
            Commandes.setVisible(false);
            BtnParametres.setVisible(false);
            Users.setVisible(false);
            Stats.setVisible(false);
        }
        if (CurrentUser.isAdmin()) {
            Pane_recommendation.setVisible(false);
        }
        nom.setText(CurrentUser.getUsername());
        date.setText(currenttime);
        // get parametres
        try {
            setCurrentParametres();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // recommendations
        if (CurrentUser.isClientRegulier() || CurrentUser.isClientDivers()) {
            ObservableList<Articles> listA = FXCollections.observableArrayList();
            try {
                listA = Client.getRecommendation();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (Articles article : listA) {
                addItem(vb, article.getDesignation(), article.getPrix());
            }
        }
    }

    private void addItem(VBox vb, String des, int price) {
        Label Designation = new Label();
        Label Prix = new Label();
        Designation.setTextFill(Color.WHITE);
        Designation.setFont(Font.font(20));
        Prix.setTextFill(Color.WHITE);
        Prix.setFont(Font.font(20));
        Button cmd = new Button("Commander");
        cmd.setOnAction(e -> {
            try {
                OpenCommandes(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        cmd.setTextFill(WHITE);
        cmd.setStyle("-fx-background-color: black");
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER_RIGHT);
        hb.setPrefHeight(90);
        hb.setPrefWidth(430);
        hb.setSpacing(120);
        hb.setOnMouseEntered(event -> entred(event));
        hb.setOnMouseExited(event -> exited(event));
        Designation.setText(des);
        Prix.setText(String.valueOf(price));
        hb.getChildren().addAll(Designation, Prix, cmd);
        vb.setAlignment(Pos.TOP_CENTER);
        vb.getChildren().add(hb);
    }

    public void OpenUsers(Event e) throws IOException {
        Parent root = load(getClass().getResource("Users.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Users");
    }

    public void openProfile(Event e) throws IOException {
        if (!CurrentUser.isClientDivers()) {
            Parent root = load(getClass().getResource("Profile.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Profile");
        }
    }

    public void OpenArticles(Event e) throws IOException {
        Parent root = load(getClass().getResource("Articles.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Articles");
    }

    public void OpenStats(Event e) throws IOException {
        Parent root = load(getClass().getResource("Stats.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Statistiques");
    }

    public void OpenParametres(Event e) throws IOException {

        AnchorPane pane = load(getClass().getResource("Parametres.fxml"));
        pane.setLayoutX(300);
        pane.setLayoutY(100);
        rootPane2.setDisable(true);
        rootPane.getChildren().addAll(pane);
    }

    public void OpenCommandes(Event e) throws IOException {
        Parent root = load(getClass().getResource("Commandes.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Commandes");
    }

    public void OpenLivraison(Event e) throws IOException {
        Parent root = load(getClass().getResource("Livraison.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Livraison");
    }

    public void entred(Event e) {
        ((Node) e.getSource()).setScaleX(1.1);
        ((Node) e.getSource()).setScaleY(1.1);
    }

    public void exited(Event e) {
        ((Node) e.getSource()).setScaleX(1);
        ((Node) e.getSource()).setScaleY(1);
    }
}
