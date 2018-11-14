package fr.ign.cogit.simplu3d.io.shapefile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.sig3d.Messages;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.geoxygene.util.conversion.WktGeOxygene;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.util.convert.ExportAsFeatureCollection;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see http://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class SaveGeneratedObjects {

  private final static Logger logger = Logger.getLogger(SaveGeneratedObjects.class.getName());

  public final static String SRID = "2154";

  public static final String TABLE_GENERATED_BUILDING = "generatedbuildings";

  public static boolean saveShapefile(String path, GraphConfiguration<? extends AbstractSimpleBuilding> cc, int idParcelle, long seed) {
    ExportAsFeatureCollection exporter = new ExportAsFeatureCollection(cc, idParcelle);
    ShapefileWriter.write(exporter.getFeatureCollection(), path);
    return true;
  }

  public static boolean save(String host, String port, String database, String user, String pw, GraphConfiguration<Cuboid> cc,
      int idExperiment, int idParcelle, long seed) {
    try {
      // Création de l'URL de chargement
      String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
      SaveGeneratedObjects.logger.info(Messages.getString("PostGIS.Try") + url);
      // Connexion
      Connection conn = DriverManager.getConnection(url, user, pw);
      Statement s = conn.createStatement();
      for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {
        double longueur = Math.max(v.getValue().length, v.getValue().width);
        double largeur = Math.min(v.getValue().length, v.getValue().width);
        double hauteur = v.getValue().height;
        double orientation = v.getValue().orientation;
        String geomWKT = "ST_GeomFromText('" + WktGeOxygene.makeWkt(v.getValue().generated3DGeom()) + "'," + SRID + ")";
        String sql = "Insert into " + TABLE_GENERATED_BUILDING
            + "(idparcelle,largeur,longueur,hauteur,orientation,idexperiment, the_geom, seed) values (" + idParcelle + "," + largeur + ","
            + longueur + "," + hauteur + "," + orientation + "," + idExperiment + "," + geomWKT + "," + seed + ")";
        System.out.println(sql);
        s.addBatch(sql);
      }
      s.executeBatch();
      conn.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
