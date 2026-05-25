package fr.ign.cogit.simplu3d.io.shapefile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.Logger;

import fr.ign.common.io.ShapefileWriter;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.referencing.CRS;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.util.convert.ExportAsFeatureCollection;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import org.locationtech.jts.io.WKTWriter;

/**
 *
 * This software is released under the licence CeCILL
 * <p>
 * see LICENSE.TXT
 * <p>
 * see <a href="http://www.cecill.info/">...</a>
 * <p>
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

  public static boolean saveShapefile(String path, GraphConfiguration<? extends AbstractSimpleBuilding> cc, String idParcelle, long seed) {
    ExportAsFeatureCollection exporter = new ExportAsFeatureCollection(cc, idParcelle);
    try {
		ShapefileWriter.write(exporter.getFeatureCollection(), exporter.getFeatureType(), CRS.decode("EPSG:"+SRID), path);
	} catch (NoSuchAuthorityCodeException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (FactoryException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return true;
  }

  public static boolean save(String host, String port, String database, String user, String pw, GraphConfiguration<Cuboid> cc,
      int idExperiment, int idParcelle, long seed) {
    try {
      // Création de l'URL de chargement
      String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
      SaveGeneratedObjects.logger.info("PostGIS.Try: " + url);
      // Connexion
      Connection conn = DriverManager.getConnection(url, user, pw);
      Statement s = conn.createStatement();
      for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {
        double longueur = Math.max(v.getValue().length, v.getValue().width);
        double largeur = Math.min(v.getValue().length, v.getValue().width);
        double hauteur = v.getValue().height;
        double orientation = v.getValue().orientation;
        WKTWriter writer = new WKTWriter(3);
        String geomWKT = "ST_GeomFromText('" + writer.write(v.getValue().generated3DGeom()) + "'," + SRID + ")";
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
