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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.fxml.FXMLLoader.load;

public class StatsController implements Initializable {

    @FXML
    private Label Label_total_cmd;

    @FXML
    private Label Label_total_recu;

    @FXML
    private Label Label_cmd_en_attente;

    @FXML
    private Label Label_cmd_cloture;

    @FXML
    private Label Label_nbr_clients;

    @FXML
    private Label Label_nbr_livreurs;

    @FXML
    private Label Label_CA;

    @FXML
    private Label Label_CM;

    @FXML
    private PieChart pie_cmd;
    @FXML
    private PieChart pie_cmd2;
    @FXML
    private Label Label_nbr_sur_place;
    @FXML
    private Label Label_nbr_a_distance;
    @FXML
    private Label Label_CM_sur_place;
    @FXML
    private Label Label_CM_a_distance;
    @FXML
    private Label Label_total_articles;

    @FXML
    private Label Label_nbr_plats;

    @FXML
    private Label Label_nbr_boissons;

    @FXML
    private Label Label_nbr_dessets;

    @FXML
    private BarChart<String, Integer> Bar_top5_plats;

    @FXML
    private BarChart<String, Integer> Bar_top5_boissons;

    @FXML
    private BarChart<String, Integer> Bar_top5_client_nbr;

    @FXML
    private BarChart<String, Integer> Bar_top5_client_val;
    @FXML
    private TableView<Stats> Table_client;

    @FXML
    private TableColumn<Stats, Integer> Client_c1;

    @FXML
    private TableColumn<Stats, String> Client_c2;

    @FXML
    private TableColumn<Stats, Integer> Client_c3;

    @FXML
    private TableColumn<Stats, Integer> Client_c4;

    @FXML
    private BarChart<Stats, Integer> Bar_top5_livreur;

    @FXML
    private TableView<Stats> Table_livreur;

    @FXML
    private TableColumn<Stats, Integer> Livreur_c1;

    @FXML
    private TableColumn<Stats, String> Livreur_c2;

    @FXML
    private TableColumn<Stats, Integer> Livreur_c3;

    @FXML
    private Button BtnQuitter;

    int nbr_sur_place = 0, nbr_a_distance = 0, CM_sur_place = 0, CM_a_distance = 0;
    ObservableList<Stats> barchartdata = FXCollections.observableArrayList();
    ObservableList<Stats> barchartdata2 = FXCollections.observableArrayList();
    ObservableList<Stats> barchartdata3 = FXCollections.observableArrayList();
    ObservableList<Stats> barchartdata4 = FXCollections.observableArrayList();
    ObservableList<Stats> barchartdata5 = FXCollections.observableArrayList();

    public void CloseStats(Event e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Accueil");
        stage.setResizable(false);
        stage.show();
    }


    public void loadData() throws SQLException {

        try {

            //------------------------------------------------- Aperçu ------------------------------------------------//

            Label_CM.setText(Integer.toString(Stats.getnbr("select sum(montant) nbr from commandes where commande_date between last_day(add_months(trunc(sysdate,'mm'),-1)) and last_day(trunc(sysdate,'mm'))")));
            Label_CA.setText(Integer.toString(Stats.getnbr("select sum(montant) nbr from commandes")));

            //------------------------------------------------ Commandes ---------------------------------------------//

            Label_total_cmd.setText(Integer.toString(Stats.getnbr("select count(Commande_id) nbr from commandes")));
            Label_total_recu.setText(Integer.toString(Stats.getnbr("select count(Commande_id) nbr from commandes where ok_Cloture = 'O'")));
            Label_cmd_en_attente.setText(Integer.toString(Stats.getnbr("select count(Commande_id) nbr from commandes where ok_Cloture = 'N'")));
            nbr_sur_place = Stats.getnbr("select count(Commande_id) nbr from commandes where commande_type = 'Sur place'");
            nbr_a_distance = Stats.getnbr("select count(Commande_id) nbr from commandes where commande_type = 'A distance'");
            CM_sur_place = Stats.getnbr("select sum(montant) nbr from commandes where commande_type = 'Sur place'");
            CM_a_distance = Stats.getnbr("select sum(montant) nbr from commandes where commande_type = 'A distance'");
            Label_nbr_sur_place.setText(Integer.toString(nbr_sur_place));
            Label_nbr_a_distance.setText(Integer.toString(nbr_a_distance));
            Label_CM_a_distance.setText(Integer.toString(CM_a_distance));
            Label_CM_sur_place.setText(Integer.toString(CM_sur_place));
            Label_nbr_clients.setText(Integer.toString(Stats.getnbr("select count(userid) nbr from users where usertype = 'Client'")));
            Label_nbr_livreurs.setText(Integer.toString(Stats.getnbr("select count(userid) nbr from users where usertype = 'Livreur'")));

            //---------------------------------------------------------- Articles ----------------------------------//

            Label_total_articles.setText(Integer.toString(Stats.getnbr("select count(article_id) nbr from articles")));
            Label_nbr_plats.setText(Integer.toString(Stats.getnbr("select count(article_id) nbr from articles where cat_id = 1")));
            Label_nbr_boissons.setText(Integer.toString(Stats.getnbr("select count(article_id) nbr from articles where cat_id = 2")));
            Label_nbr_dessets.setText(Integer.toString(Stats.getnbr("select count(article_id) nbr from articles where cat_id = 3")));

            barchartdata = Stats.getXYdata("SELECT * FROM\n" +
                    "         ( select designation x,sum(l.quantite) y from articles a inner join ligne_commande l on a.article_id = l.article_id and cat_id = 1 group by a.article_id,designation order by y desc )\n" +
                    "         WHERE ROWNUM <= 5");
            barchartdata2 = Stats.getXYdata(" SELECT * FROM\n" +
                    "         ( select designation x,sum(l.quantite) y from articles a inner join ligne_commande l on a.article_id = l.article_id and cat_id = 2 group by a.article_id,designation order by y desc )\n" +
                    "         WHERE ROWNUM <= 5");
            //------------------------------------------------------ Clients -----------------------------------------//
            barchartdata3 = Stats.getXYdata(" SELECT * FROM\n" +
                    "         (select userid,nom x ,count(commande_id) y from commandes c, users t where c.client_id = t.userid group by userid,nom order by y desc )\n" +
                    "         WHERE ROWNUM <= 5");
            barchartdata4 = Stats.getXYdata(" SELECT * FROM\n" +
                    "         (select userid,nom x ,sum(montant) y from commandes c, users t where c.client_id = t.userid group by userid,nom order by y desc )\n" +
                    "         WHERE ROWNUM <= 5");
            //------------------------------------------------------ Livreurs ----------------------------------------//
            barchartdata5 = Stats.getXYdata(" SELECT * FROM\n" +
                    "         (select userid,nom x ,count(userid)y from comdist c, users t where c.livreur_id = t.userid group by userid,nom order by y desc )\n" +
                    "         WHERE ROWNUM <= 5");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //------------------------------------------- Commandes Pie Chart ---------------------------------------------//
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Commandes à distance", nbr_a_distance),
                        new PieChart.Data("Commandes sur place", nbr_sur_place));
        pie_cmd.setData(pieChartData);
        pie_cmd.setTitle("Nbr Commandes par type de commande");
        ObservableList<PieChart.Data> pieChartData2 =
                FXCollections.observableArrayList(
                        new PieChart.Data("Commandes à distance", CM_a_distance),
                        new PieChart.Data("Commandes sur place", CM_sur_place));
        pie_cmd2.setData(pieChartData2);
        pie_cmd2.setTitle("Chiffre Monsuel par type de commande");

        //----------------------------------------------- Articles Bar Charts ----------------------------------------//
        // top 5 plats
        Bar_top5_plats.setTitle("Top 5 plats par qte commandée");
        XYChart.Series series1 = new XYChart.Series<>();
        series1.setName("qte vendue des plats ");
        for (Stats xy : barchartdata) {
            series1.getData().add(new XYChart.Data(xy.getX(), xy.getY()));
        }
        Bar_top5_plats.getData().addAll(series1);
        // top 5 boisson
        Bar_top5_boissons.setTitle("Top 5 boissons par qte commandée");
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("qte vendue des boissons");
        for (Stats xy : barchartdata2) {
            series2.getData().add(new XYChart.Data(xy.getX(), xy.getY()));
        }
        Bar_top5_boissons.getData().addAll(series2);
        //-------------------------------------------------- Clients --------------------------------------------------//
        // top 5 client par nbr de commandes
        Bar_top5_client_nbr.setTitle("Top 5 Client par nbr des commandes");
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Nbr de commandes");
        for (Stats xy : barchartdata3) {
            series3.getData().add(new XYChart.Data(xy.getX(), xy.getY()));
        }
        Bar_top5_client_nbr.getData().addAll(series3);
        // top 5 clients par val commandes
        Bar_top5_client_val.setTitle("Top 5 Client par valeur des commandes");
        XYChart.Series series4 = new XYChart.Series();
        series4.setName("Valeur de commandes");
        for (Stats xy : barchartdata4) {
            series4.getData().add(new XYChart.Data(xy.getX(), xy.getY()));
        }
        Bar_top5_client_val.getData().addAll(series4);
        // tableau :
        ObservableList<Stats> List_client = null;
        Client_c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        Client_c2.setCellValueFactory(new PropertyValueFactory<>("x"));
        Client_c3.setCellValueFactory(new PropertyValueFactory<>("y"));
        Client_c4.setCellValueFactory(new PropertyValueFactory<>("z"));
        try {
            List_client = Stats.getIdXYZdata("select userid id,nom x, count(commande_id) y, sum(montant) z from Commandes c, users t where " +
                    "c.client_id = t.userid group by userid, nom order by y desc");
        } catch (SQLException ex) {
            Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Table_client.getColumns().addAll();
        Table_client.setItems(List_client);
        //----------------------------------------------- Livreurs ---------------------------------------------------//
        // top 5 livreurs par nbr de livraisons
        Bar_top5_livreur.setTitle("Top 5 livreurs par nbr des livraisons");
        XYChart.Series series5 = new XYChart.Series();
        series5.setName("Nbr de livraisons");
        for (Stats xy : barchartdata5) {
            series5.getData().add(new XYChart.Data(xy.getX(), xy.getY()));
        }
        Bar_top5_livreur.getData().addAll(series5);
        // tableau :
        ObservableList<Stats> List_Livreur = null;
        Livreur_c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        Livreur_c2.setCellValueFactory(new PropertyValueFactory<>("x"));
        Livreur_c3.setCellValueFactory(new PropertyValueFactory<>("y"));
        try {
            List_Livreur = Stats.getIdXYdata("select userid id,nom x ,count(userid)y from comdist c, users t " +
                    "where c.livreur_id = t.userid group by userid,nom order by y desc");
        } catch (SQLException ex) {
            Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Table_livreur.getColumns().addAll();
        Table_livreur.setItems(List_Livreur);

    }
}

