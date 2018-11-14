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
 *        see  http://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class ExperimentationPostGIS extends Parameters {

  public final static String EXPERIMENTATION_TABLE = "experimentation";
  public final static String EXPERIMENTATION_ID = "idexp";
  public final static String EXPERIMENTATION_ID_PARAM = "idparam";
  private ResultSet rs;

  private Logger log = Logger.getLogger(ParametersPostgis.class);

  private int id;

  private String url, user, pw;

  public ExperimentationPostGIS(String host, String port, String database, String user, String pw, int id) throws SQLException {
    this.id = id;
    this.user = user;
    this.pw = pw;
    url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
    Connection conn = DriverManager.getConnection(url, user, pw);
    Statement s = conn.createStatement();
    String sql = "Select * from " + EXPERIMENTATION_TABLE + " where " + EXPERIMENTATION_ID + " =" + id  ;
    rs = s.executeQuery(sql);
    System.out.println(sql);
    boolean next = rs.next();
    if (!next) {
      log.error("No parameters line found");
      System.exit(1);
    }
    conn.close();
  }
  
  public boolean setProcessed() throws SQLException {
    Connection conn = DriverManager.getConnection(url, user, pw);
    Statement s = conn.createStatement();
    String sql = "UPDATE " + EXPERIMENTATION_TABLE + "  set isprocessed = 't'  where " + EXPERIMENTATION_ID + " = " + id;
    boolean isOk = s.execute(sql);
    conn.close();
    return isOk;
  }

  public Object get(String key) {
    if (rs != null) {
      try {
        return rs.getObject(key);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
