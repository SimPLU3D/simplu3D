package fr.ign.cogit.simplu3d.exec;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.io.load.application.LoadPostGIS;
import fr.ign.cogit.simplu3d.io.load.application.ParametersPostgis;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.convert.GenerateSolidFromCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained.OptimisedBuildingsCuboidDirectRejectionNoVisitor;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.predicate.UB16PredicateWithParameters;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.rjmcmc.configuration.Configuration;

/**
 * Simulateur standard
 * 
 * @author MBrasebin
 * 
 */
public class BasicPostGISSimulator {

  /**
   * @param args
   */

  // [building_footprint_rectangle_cli_main
  public static void main(String[] args) throws Exception {

    String host = "localhost";
    String port = "5432";
    String database = "gtru";
    String user = "postgres";
    String pw = "postgres";

    int idParameter = 2;

    // Chargement de l'environnement depuis PostGIS
    LoadPostGIS lP = new LoadPostGIS(host, port, database, user, pw);
    Environnement env = lP.loadNoOCLRules();

    ParametersPostgis p = new ParametersPostgis(host, port, database, user, pw,
        idParameter);

    // Pour l'instant on prend le premier
    BasicPropertyUnit bPU = env.getBpU().get(1);

    OptimisedBuildingsCuboidDirectRejectionNoVisitor oCB = new OptimisedBuildingsCuboidDirectRejectionNoVisitor();

    // UXL3Predicate<Cuboid> pred = new UXL3Predicate<>(bPU);

    UB16PredicateWithParameters<Cuboid> pred = new UB16PredicateWithParameters<Cuboid>(
        bPU, 3, 0.5);

    /*
     * for(BasicPropertyUnit bPUTemp: env.getBpU()){
     * 
     * System.out.println(bPUTemp.getCadastralParcel().get(0).getId());
     * 
     * }
     */

    Configuration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);

    IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();

    for (GraphConfiguration<Cuboid>.GraphVertex v : ((GraphConfiguration<Cuboid>) cc)
        .getGraph().vertexSet()) {

      IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
      iMS.addAll(GenerateSolidFromCuboid.generate(v.getValue()).getFacesList());

      IFeature feat = new DefaultFeature(iMS);

      AttributeManager.addAttribute(feat, "Longueur",
          Math.max(v.getValue().length, v.getValue().width), "Double");
      AttributeManager.addAttribute(feat, "Largeur",
          Math.min(v.getValue().length, v.getValue().width), "Double");
      AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height,
          "Double");
      AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation,
          "Double");

      iFeatC.add(feat);

    }

    ShapefileWriter.write(iFeatC, "out.shp");

    System.out.println("That's all folks");

  }

}
