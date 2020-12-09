package RESTMAN;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    public static Alert a = new Alert(Alert.AlertType.INFORMATION, "Ajout avec succès", ButtonType.OK);
    public static Alert E = new Alert(Alert.AlertType.ERROR, "Erreur", ButtonType.OK);

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@localhost:1521:orcl1";
            String user = "system";
            String password = "said1234";
            con = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Exception: " + ex.getMessage());
            E.show();
        }
        return con;
    }
}

