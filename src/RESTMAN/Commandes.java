package RESTMAN;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static RESTMAN.DB.getConnection;
import static RESTMAN.LoginController.CurrentUser;

public class Commandes {
    private int id;
    private String cmd_date;
    private String type;
    private Number montant;
    private int client_id;
    private String client_nom;
    private Boolean reglement;
    private Boolean ok_cloture;


    public Commandes(int id, String cmd_date, String type, int client_id, String client_nom, int montant, Boolean reglement, Boolean ok_cloture) {
        this.id = id;
        this.cmd_date = cmd_date;
        this.type = type;
        this.client_id = client_id;
        this.client_nom = client_nom;
        this.montant = montant;
        this.reglement = reglement;
        this.ok_cloture = ok_cloture;

    }

    public Commandes() {
        this.id = 0;
        this.cmd_date = null;
        this.type = null;
        this.client_id = CurrentUser.getId();
        this.client_nom = null;
        this.montant = null;
        this.reglement = false;
        this.ok_cloture = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCmd_date() {
        return cmd_date;
    }

    public void setCmd_date(String cmd_date) {
        this.cmd_date = cmd_date;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getClient_nom() {
        return client_nom;
    }

    public void setClient_id(String client_nom) {
        this.client_nom = client_nom;
    }

    public Number getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public Boolean getReglement() {
        return reglement;
    }

    public void setReglement(Boolean reglement) {
        this.reglement = reglement;
    }

    public Boolean getOk_cloture() {
        return ok_cloture;
    }

    public void setOk_cloture(Boolean ok_cloture) {
        this.ok_cloture = ok_cloture;
    }

    /**************************************************************** DB *******************************************/

    public static Alert a = new Alert(Alert.AlertType.INFORMATION, "Ajout avec succès", ButtonType.OK);
    public static Alert E = new Alert(Alert.AlertType.ERROR, "Erreur", ButtonType.OK);

    public static boolean insertCommandes(String Commande_type, int client_id, int montant) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlselect = "insert into Commandes(COMMANDE_DATE,COMMANDE_TYPE,CLIENT_ID,MONTANT) values (SYSDATE,'" + Commande_type + "'," + client_id + "," + montant + ")";
            s.execute(sqlselect);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            E.show();
            return false;
        }

    }

    public static ObservableList<Commandes> getCommandes() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect;
        if (CurrentUser.isClientRegulier() || CurrentUser.isClientDivers()) {
            sqlSelect = "select Commande_id,Commande_date,Commande_type,Client_id,Nom,montant,reglement,ok_cloture from Commandes C," +
                    "Users U where C.client_id = U.Userid and client_id =" + CurrentUser.getId() + "";
        } else {
            sqlSelect = "select Commande_id,Commande_date,Commande_type,Client_id,Nom,montant,reglement,ok_cloture from Commandes C," +
                    "Users U where C.client_id = U.Userid";
        }
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<Commandes> CommandesList = FXCollections.observableArrayList();
        while (result.next()) {
            int id = result.getInt("Commande_id");
            String cmd_date = MainMenuController.formatter.format(result.getDate("commande_date"));
            String Commande_type = result.getString("Commande_type");
            int client_id = result.getInt("Client_id");
            int montant = result.getInt("montant");
            Boolean reglement = result.getString("reglement").equals("O");
            Boolean ok_cloture = result.getString("ok_cloture").equals("O");
            String client_nom = result.getString("Nom");
            CommandesList.add(new Commandes(id, cmd_date, Commande_type, client_id, client_nom, montant, reglement, ok_cloture));
        }
        return CommandesList;
    }

    public static boolean updateCommandes(int id, String Commande_type, int client_id, int montant) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect;
            /*if (CurrentUser.isClientRegulier() || CurrentUser.isClientDivers()) {
                sqlSelect = "UPDATE Commandes SET Commande_date = SYSDATE , Commande_type = '" + Commande_type + "',client_id=" + client_id + ",montant= " + montant + " where Commande_id=" + id + ";\n" +
                        "update Clients set nbr_cmd = (select count(commande_id) nbr from Commandes where client_id =" + CurrentUser.getId() + "),\n" +
                        "val_cmd = (select sum(montant) nbr from Commandes where client_id =" + CurrentUser.getId() + ")";
            } else {*/
                sqlSelect = "UPDATE Commandes SET Commande_date = SYSDATE , Commande_type = '" + Commande_type + "',client_id=" + client_id + ",montant= " + montant + " where Commande_id=" + id + "";
            //}
            s.execute(sqlSelect);
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            E.show();
            return false;
        }
    }

    public static boolean reglerCommandes(int id, boolean b) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect;
            if (b) sqlSelect = "UPDATE Commandes set reglement= 'O' where Commande_id=" + id + "";
            else sqlSelect = "UPDATE Commandes set reglement= 'N'  where Commande_id=" + id + "";
            s.execute(sqlSelect);
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            E.show();
            return false;
        }
    }

    public static boolean cloturerCommandes(int id, boolean b) {
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String sqlSelect;
            if (b) sqlSelect = "UPDATE Commandes set Ok_cloture= 'O' where Commande_id=" + id + "";
            else sqlSelect = "UPDATE Commandes set Ok_cloture = 'N'  where Commande_id=" + id + "";
            s.execute(sqlSelect);
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            E.show();
            return false;
        }
    }

    public static boolean delCommandes(int id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "DELETE FROM Commandes WHERE Commande_id = " + id + "";
        boolean execute = s.execute(sqlDelete);

        return execute;
    }

    public static Commandes getLastCommande() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "select Commande_id,Commande_date,Commande_type,Client_id,Nom,montant,reglement,ok_cloture from Commandes C,Users U where  Commande_id = ( select max(Commande_id) from Commandes) and C.client_id = U.Userid";
        ResultSet result = s.executeQuery(sqlDelete);
        Commandes commande = null;
        while (result.next()) {
            int id = result.getInt("Commande_id");
            String cmd_date = MainMenuController.formatter.format(result.getDate("commande_date"));
            String Commande_type = result.getString("Commande_type");
            int client_id = result.getInt("Client_id");
            int montant = result.getInt("montant");
            Boolean reglement = result.getString("reglement").equals("O");
            Boolean ok_cloture = result.getString("ok_cloture").equals("O");
            String client_nom = result.getString("Nom");
            commande = new Commandes(id, cmd_date, Commande_type, client_id, client_nom, montant, reglement, ok_cloture);
        }
        return commande;
    }

    public static Commandes getCommandeById(int id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "select Commande_id,Commande_date,Commande_type,Client_id,Nom,montant,reglement,ok_cloture from Commandes C,Users U where  Commande_id = " + id + " and C.client_id = U.Userid";
        ResultSet result = s.executeQuery(sqlDelete);
        Commandes commande = null;
        while (result.next()) {
            String cmd_date = MainMenuController.formatter.format(result.getDate("commande_date"));
            String Commande_type = result.getString("Commande_type");
            int client_id = result.getInt("Client_id");
            int montant = result.getInt("montant");
            Boolean reglement = result.getString("reglement").equals("O");
            Boolean ok_cloture = result.getString("ok_cloture").equals("O");
            String client_nom = result.getString("Nom");
            commande = new Commandes(id, cmd_date, Commande_type, client_id, client_nom, montant, reglement, ok_cloture);
        }
        return commande;

    }


}