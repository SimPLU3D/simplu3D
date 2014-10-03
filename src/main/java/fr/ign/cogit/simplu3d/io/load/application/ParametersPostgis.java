package fr.ign.cogit.simplu3d.io.load.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import fr.ign.parameters.Parameters;

public class ParametersPostgis extends Parameters {

  public final static String PARAMETERS_TABLE = "parameters";
  private ResultSet rs;

  private Logger log = Logger.getLogger(ParametersPostgis.class);

  public ParametersPostgis(String host, String port, String database,
      String user, String pw, int id) throws SQLException {

    String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;

    Connection conn = DriverManager.getConnection(url, user, pw);

    Statement s = conn.createStatement();

    String sql = "Select * from " + PARAMETERS_TABLE;

    rs = s.executeQuery(sql);

    boolean next = rs.next();

    if (!next) {

      log.error("No parameters line found");
      System.exit(1);
    }

    conn.close();

  }

  @Override
  public Object get(String key) {
    // System.out.println("key = " + key);
    if (entry != null) {
       try {
        return rs.getObject(key);
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return null;
  }

}
