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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static RESTMAN.ArticlesController.CurrentArticle;
import static RESTMAN.ArticlesController.ListA;
import static RESTMAN.FicheTechnique.E;
import static RESTMAN.FicheTechnique.a;
import static RESTMAN.LoginController.CurrentUser;

public class FicheTechniqueController implements Initializable {

    ObservableList<Ingredients> List_ingredients = FXCollections.observableArrayList();
    ObservableList<Preparation> List_preparation = FXCollections.observableArrayList();
    public static String prevroot;
    @FXML
    private Button BtnQuitter;
    @FXML
    private Button BtnValiderFT;
    @FXML
    private Label Label_spec;
    @FXML
    private Label Label_article;
    @FXML
    private Label Label_cat;
    @FXML
    private Pane Pane_coutparportion;
    @FXML
    private Label coutparportion;
    @FXML
    private TableView<Ingredients> Table_ingredients;
    @FXML
    private TableColumn<Ingredients, String> ing_c1;
    @FXML
    private TableColumn<Ingredients, Float> ing_c2;
    @FXML
    private TableColumn<Ingredients, String> ing_c3;
    @FXML
    private TableColumn<Ingredients, Integer> ing_c4;
    @FXML
    private Button BtnAjouterIng;
    @FXML
    private Button BtnAjouterPrep;
    @FXML
    private Button BtnSupprimerIng;
    @FXML
    private Button BtnSupprimerPrep;
    @FXML
    private TableView<Preparation> Table_preparation;
    @FXML
    private TableColumn<Preparation, Integer> c1;
    @FXML
    private TableColumn<Preparation, String> c2;
    @FXML
    private TableColumn<Preparation, Integer> c3;
    @FXML
    private TableColumn<Preparation, String> c4;
    @FXML
    private Label Label_prix;

    public void CloseFicheTechnique(Event e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(prevroot + ".fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(prevroot);
        stage.setResizable(false);
        stage.show();
    }

    public void Suivant() {
        if (ListA.indexOf(CurrentArticle) <= ListA.size() - 2)
            CurrentArticle = ListA.get(ListA.indexOf(CurrentArticle) + 1);
        initTable();
    }

    public void Precedent() {
        if (ListA.indexOf(CurrentArticle) - 1 >= 0)
            CurrentArticle = ListA.get(ListA.indexOf(CurrentArticle) - 1);
        initTable();
    }

    public void ajouterIngredient() {
        Ingredients ing = new Ingredients("", 0, 0, "kg");
        List_ingredients.add(ing);
        Table_ingredients.setItems(List_ingredients);
    }

    public void ajouterPreparation() {
        Preparation prep = new Preparation(List_preparation.size() + 1, "", 0, "");
        List_preparation.add(prep);
        Table_preparation.setItems(List_preparation);
    }

    public boolean updateIngredients() throws SQLException { //to be modified in DB ;
        if (List_ingredients.isEmpty()) {
            E.setContentText("veuillez saisir les ingredients pour pouvoir valider!");
            E.show();
            return false;
        } else {
            FicheTechnique.delIngredients(CurrentArticle.getId());
            for (Ingredients ing : List_ingredients) {
                if (!FicheTechnique.insertIngredients(CurrentArticle.getId(), ing.getIngredient_nom(), ing.getQuantite(), ing.getUnite(), ing.getPrix()))
                    return false;
            }

        }
        return true;
    }

    public boolean updatePreparation() throws SQLException {
        if (List_preparation.isEmpty()) {
            E.setContentText("veuillez saisir la methode  de préparation pour pouvoir valider!");
            E.show();
            return false;
        } else {
            FicheTechnique.delPreparation(CurrentArticle.getId());
            for (Preparation prep : List_preparation) {
                if (!FicheTechnique.insertPreparation(CurrentArticle.getId(), prep.getNum_etape(), prep.getNom_etape(), prep.getTempsNec(), prep.getDescription()))
                    return false;
            }
        }

        return true;
    }

    public int CalcPrix(ObservableList<Ingredients> list) {
        int prix = 0;
        for (Ingredients ing : list) {
            prix = (int) (prix + ing.getPrix() * ing.getQuantite());
        }
        return prix;
    }

    public boolean addFT() throws SQLException {
        FicheTechnique.delFiche_Technique(CurrentArticle.getId());
        return FicheTechnique.insertFiche_Technique(CurrentArticle.getId(), CalcPrix(List_ingredients));
    }

    public void supprimerPreparation() {
        List_preparation.remove(Table_preparation.getSelectionModel().getSelectedItem());
        for (Preparation prep : List_preparation) {
            prep.setNum_etape(List_preparation.indexOf(prep) + 1);
        }
        Table_preparation.setItems(List_preparation);
    }

    public void supprimerIngredients() {
        List_ingredients.remove(Table_ingredients.getSelectionModel().getSelectedItem());
        Table_ingredients.setItems(List_ingredients);
    }

    public void validerFT() throws SQLException {
        if (addFT()) {
            if (updatePreparation() && updateIngredients()) {
                a.setContentText("Fiche technique ajouté avec succès!");
                a.show();
                initTable();
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        if (CurrentUser.isClientRegulier() || CurrentUser.isClientDivers()) {
            BtnAjouterIng.setVisible(false);
            BtnAjouterPrep.setVisible(false);
            BtnValiderFT.setVisible(false);
            BtnSupprimerIng.setVisible(false);
            BtnSupprimerPrep.setVisible(false);
            Pane_coutparportion.setVisible(false);
            Table_preparation.setEditable(false);
            Table_ingredients.setEditable(false);

        }
    }


    private void initTable() {
        initCols();
        try {
            List_ingredients = FicheTechnique.getIngredients(CurrentArticle.getId());
        } catch (SQLException ex) {
            Logger.getLogger(CommandesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Table_ingredients.setItems(List_ingredients);
        Table_ingredients.getColumns().addAll();
        ing_c1.setSortType(TableColumn.SortType.ASCENDING);
        Table_ingredients.getSortOrder().add(ing_c1);

        try {
            List_preparation = FicheTechnique.getPreparation(CurrentArticle.getId());
        } catch (SQLException ex) {
            Logger.getLogger(CommandesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Table_preparation.setItems(List_preparation);
        Table_preparation.getColumns().addAll();
        c1.setSortType(TableColumn.SortType.ASCENDING);
        Table_preparation.getSortOrder().add(c1);
        //filling labels
        Label_article.setText(CurrentArticle.getDesignation());
        Label_cat.setText(CurrentArticle.getCategorie());
        Label_spec.setText(CurrentArticle.getSpecialite());
        Label_prix.setText(CurrentArticle.getPrix() + ",00");
        coutparportion.setText(CalcPrix(List_ingredients) + ",00");
    }

    private void setWrapCellFactory(TableColumn<Preparation, String> table) {   //pour le saute des lignes dans le tableau preparation-client
        table.setCellFactory(tablecol -> {
            TableCell<Preparation, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    private void initCols() {

        // tableau ingredients
        ing_c1.setCellValueFactory(new PropertyValueFactory<>("ingredient_nom"));
        ing_c2.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        ing_c3.setCellValueFactory(new PropertyValueFactory<>("unite"));
        ing_c4.setCellValueFactory(new PropertyValueFactory<>("prix"));
        Table_ingredients.setItems(List_ingredients);
        Table_ingredients.getColumns().addAll();

        // tableau preparation
        c1.setCellValueFactory(new PropertyValueFactory<>("num_etape"));
        c2.setCellValueFactory(new PropertyValueFactory<>("nom_etape"));
        c3.setCellValueFactory(new PropertyValueFactory<>("tempsNec"));
        c4.setCellValueFactory(new PropertyValueFactory<>("description"));
        //  c4.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getDescription()));
        setWrapCellFactory(c4);
        Table_preparation.setFixedCellSize(80);//Region.USE_COMPUTED_SIZE);
        Table_preparation.setItems(List_preparation);
        Table_preparation.getColumns().addAll();

        //if(CurrentUser.isAdmin()||CurrentUser.isLivreur())
        if (!CurrentUser.isClientDivers() && !CurrentUser.isClientRegulier())
            editableCols();


    }


    private void editableCols() {
        /*---------------------Ingredients table ---------------*/

        ing_c1.setCellFactory(TextFieldTableCell.forTableColumn());
        ing_c1.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setIngredient_nom(e.getNewValue());
        });
        ing_c2.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        ing_c2.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setQuantite(e.getNewValue());
        });
        ing_c3.setCellFactory(TextFieldTableCell.forTableColumn());
        ing_c3.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setUnite(e.getNewValue());
        });
        ing_c4.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        ing_c4.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setPrix(e.getNewValue());
        });
        Table_ingredients.setEditable(true);


        /*-----------------------Preparation table -----------------------------------------------*/

        c2.setCellFactory(TextFieldTableCell.forTableColumn());
        c2.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setNom_etape(e.getNewValue());
        });
        c3.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        c3.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setTempsNec(e.getNewValue());
        });
        c4.setCellFactory(TextFieldTableCell.forTableColumn());
        c4.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setDescription(e.getNewValue());
        });
        Table_preparation.setEditable(true);
        c1.setEditable(false);


    }

}



