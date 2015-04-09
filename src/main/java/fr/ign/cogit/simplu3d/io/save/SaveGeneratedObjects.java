package fr.ign.cogit.simplu3d.io.save;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.Messages;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.geoxygene.util.conversion.WktGeOxygene;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.convert.GenerateSolidFromCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class SaveGeneratedObjects {

  private final static Logger logger = Logger
      .getLogger(SaveGeneratedObjects.class.getName());

  public final static String SRID = "2154";

  public static final String TABLE_GENERATED_BUILDING = "generatedbuildings";

  public static boolean saveShapefile(String path,
      GraphConfiguration<Cuboid> cc,  int idParcelle, int idRun) {

    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

    for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {
      double longueur = Math.max(v.getValue().length, v.getValue().width);
      double largeur = Math.min(v.getValue().length, v.getValue().width);
      double hauteur = v.getValue().height;
      double orientation = v.getValue().orientation;

      IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
      iMS.addAll(GenerateSolidFromCuboid.generate(v.getValue()).getFacesList());

      IFeature feat = new DefaultFeature(iMS);
      AttributeManager.addAttribute(feat, "idparc", idParcelle, "Integer");
      AttributeManager.addAttribute(feat, "largeur", largeur, "Double");
      AttributeManager.addAttribute(feat, "longueur", longueur, "Double");
      AttributeManager.addAttribute(feat, "hauteur", hauteur, "Double");
      AttributeManager.addAttribute(feat, "orient", orientation, "Double");
      AttributeManager.addAttribute(feat, "idRun", idRun, "Integer");

      featC.add(feat);

    }

    ShapefileWriter.write(featC, path);

    return true;

  }

  public static boolean save(String host, String port, String database,
      String user, String pw, GraphConfiguration<Cuboid> cc, int idExperiment,
      int idParcelle, int idRun) {

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

        IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
        iMS.addAll(GenerateSolidFromCuboid.generate(v.getValue())
            .getFacesList());

        String geomWKT = "ST_GeomFromText('" + WktGeOxygene.makeWkt(iMS) + "',"
            + SRID + ")";

        String sql = "Insert into "
            + TABLE_GENERATED_BUILDING
            + "(idparcelle,largeur,longueur,hauteur,orientation,idexperiment, the_geom, run) values ("
            + idParcelle + "," + largeur + "," + longueur + "," + hauteur + ","
            + orientation + "," + idExperiment + "," + geomWKT + "," + idRun
            + ")";

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
