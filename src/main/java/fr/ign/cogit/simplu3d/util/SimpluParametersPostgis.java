package fr.ign.cogit.simplu3d.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class SimpluParametersPostgis implements SimpluParameters{
    private final static String PARAMETERS_TABLE = "parameters";
    private final static String PARAMETERS_ID = "id";
    private ResultSet rs;

    public SimpluParametersPostgis(String host, String port, String database, String user, String pw, int id) throws SQLException {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        Connection conn = DriverManager.getConnection(url, user, pw);
        Statement s = conn.createStatement();
        String sql = "Select * from " + PARAMETERS_TABLE + " where "+PARAMETERS_ID+" =" + id;
        rs = s.executeQuery(sql);
        boolean next = rs.next();
        if (!next) {
            Logger log = Logger.getLogger(SimpluParametersPostgis.class);
            log.error("No parameters line found");
            System.exit(1);
        }
        conn.close();
    }

    public Object get(String key) {
            try {
                return rs.getObject(key);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
    }

    public String getString(String name) {
        Object value = this.get(name);
        return value == null ? "" : value.toString();
    }

    public boolean getBoolean(String name) {
        Object value = this.get(name);
        return value != null && Boolean.parseBoolean(value.toString());
    }

    public double getDouble(String name) {
        Object value = this.get(name);
        return value == null ? 0.0D : Double.parseDouble(value.toString());
    }

    public int getInteger(String name) {
        Object value = this.get(name);
        return value == null ? 0 : Integer.parseInt(value.toString());
    }

    public float getFloat(String name) {
        Object value = this.get(name);
        return value == null ? 0.0F : Float.parseFloat(value.toString());
    }

    public void set(String name, Object value) {
    }
}
