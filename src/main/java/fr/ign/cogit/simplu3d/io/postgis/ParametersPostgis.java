package fr.ign.cogit.simplu3d.io.postgis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import fr.ign.parameters.Parameters;


/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see http://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class ParametersPostgis extends Parameters {

  public final static String PARAMETERS_TABLE = "parameters";
  public final static String PARAMETERS_ID = "id";
  private ResultSet rs;

  private Logger log = Logger.getLogger(ParametersPostgis.class);

  public ParametersPostgis(String host, String port, String database, String user, String pw, int id) throws SQLException {
    String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
    Connection conn = DriverManager.getConnection(url, user, pw);
    Statement s = conn.createStatement();
    String sql = "Select * from " + PARAMETERS_TABLE + " where "+PARAMETERS_ID+" =" + id;
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
    if (entry != null) {
      try {
        return rs.getObject(key);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
