package RESTMAN;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static RESTMAN.DB.getConnection;

public class Stats {


    int id;
    String x;
    int y;
    int z;



    public Stats(int id, String x, int y, int z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Stats(int id, String x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    public Stats(String x, int y) {
        this.id = 0;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public static ObservableList<Stats> getIdXYdata(String sqlSelect) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<Stats> stat = FXCollections.observableArrayList();
        while (result.next()) {
            int id = result.getInt("id");
            String x = result.getString("x");
            int y = result.getInt("y");
            stat.add(new Stats(id, x, y));
        }
        return stat;
    }

    public static ObservableList<Stats> getXYdata(String sqlSelect) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<Stats> stat = FXCollections.observableArrayList();
        while (result.next()) {
            String x= result.getString("x");
            int y = result.getInt("y");
            stat.add(new Stats(x,y));
        }
        return stat;
    }

    public static ObservableList<Stats> getIdXYZdata(String sqlSelect) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<Stats> stat = FXCollections.observableArrayList();
        while (result.next()) {
            int id = result.getInt("id");
            String x= result.getString("x");
            int y = result.getInt("y");
            int z = result.getInt("z");
            stat.add(new Stats(id,x,y,z));
        }
        return stat;
    }
    public static int getnbr(String sqlSelect) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        ResultSet result = s.executeQuery(sqlSelect);
        int stat = 0;
        while (result.next()) {
            stat = result.getInt("nbr");
        }
        return stat;
    }


}
