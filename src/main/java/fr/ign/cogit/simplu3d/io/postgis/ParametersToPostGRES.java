package fr.ign.cogit.simplu3d.io.postgis;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.parameters.Parameters;

/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 *
 * Classe pour charger dans PostGIS les paramètres xml des fichiers de
 * configuration. Le script tableParameter.sql doit être exécuté au préalable
 * pour crééer la table parameters.
 * 
 * 
 */
public class ParametersToPostGRES {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();

    String fileName = "building_parameters_project_expthese_3.xml";

    Parameters p = Parameters.unmarshall(new File(folderName + fileName));

    String host = "localhost";
    String port = "5432";
    String database = "gtru";
    String user = "postgres";
    String pw = "postgres";

    String sql = "Insert into parameters Values (" +

    p.getDouble("energy") + ", " + p.getDouble("ponderation_volume") + ", "
        + p.getDouble("ponderation_difference_ext") + ", "
        + p.getDouble("ponderation_volume_inter") + ", "
        + p.getDouble("mindim") + ", " + p.getDouble("maxdim") + ", "
        + p.getDouble("minheight") + ", " + p.getDouble("maxheight") + ", "
        + p.getDouble("pbirth") + ", " + p.getDouble("pdeath") + ", "
        + p.getDouble("amplitudeMaxDim") + ", "
        + p.getDouble("amplitudeHeight") + ", " + p.getDouble("amplitudeMove")
        + ", " + p.getDouble("amplitudeRotate") + ", " + p.getDouble("temp")
        + ", " + p.getDouble("deccoef") + ", "
        + p.getString("end_test_type") + ", " + p.getInteger("absolute_nb_iter")
        + ", " + p.getInteger("relative_nb_iter") + ", " + p.getDouble("delta")
        + ", " + p.getDouble("poisson") +
        ");";

    String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
    Connection conn = DriverManager.getConnection(url, user, pw);
    Statement s = conn.createStatement();
    s.execute(sql);
    conn.close();
  }
}
