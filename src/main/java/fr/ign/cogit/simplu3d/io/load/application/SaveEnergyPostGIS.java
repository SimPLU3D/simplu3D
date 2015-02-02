package fr.ign.cogit.simplu3d.io.load.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SaveEnergyPostGIS {

  public final static String TABLE_SAVE_ENERGY = "volgeneratedbyparcel";

  public static void save(String host, String port, String database,
      String user, String pw, int id_exp, int id_parcelle, double energy)
      throws SQLException {

    String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;

    Connection conn = DriverManager.getConnection(url, user, pw);

    Statement s = conn.createStatement();

    String sql = "insert into " + TABLE_SAVE_ENERGY + "(id_exp, id_parcelle, energy) VALUES  ( " + id_exp
        + "," + id_parcelle + "," + energy + ")";

    System.out.println(sql);
    s.execute(sql);

    conn.close();

  }

}
