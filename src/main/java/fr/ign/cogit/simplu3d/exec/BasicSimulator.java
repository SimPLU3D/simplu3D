package fr.ign.cogit.simplu3d.exec;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.exe.LoadDefaultEnvironment;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.convert.GenerateSolidFromCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.predicate.SamplePredicate;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.predicate.UB16PredicateWithParameters;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

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
 * 
 *          Simulateur standard
 * 
 * 
 */
public class BasicSimulator {

  /**
   * @param args
   */

  // [building_footprint_rectangle_cli_main
  public static void main(String[] args) throws Exception {

    String folderName = BasicSimulator.class.getClassLoader()
        .getResource("scenario/").getPath();

    // String folderName = "./src/main/resources/scenario/";
    String fileName = "building_parameters_project_expthese_3.xml";

    Parameters p = Parameters.unmarshall(new File(folderName + fileName));

    Environnement env = LoadDefaultEnvironment.getENVDEF();

    BasicPropertyUnit bPU = env.getBpU().get(8);

    // OCLBuildingsCuboidFinalDirectRejection oCB = new
    // OCLBuildingsCuboidFinalDirectRejection();
    OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

    double distReculVoirie = 0.0;
    double distReculFond = 2;
    double distReculLat = 4;
    double distanceInterBati = 5;
    double maximalCES =2;
    
    
    
    SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati, maximalCES);
    
    
  //  UB16PredicateWithParameters<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new UB16PredicateWithParameters<>(
    //      bPU, 2, 2);

    // UXL3PredicateBuildingSeparation<Cuboid2> pred = new
    // UXL3PredicateBuildingSeparation<>(
    // env.getBpU().get(1));

    // UXL3PredicateGroup<Cuboid2> pred = new
    // UXL3PredicateGroup<Cuboid2>(env.getBpU().get(1),3);

    // UB16PredicateWithParameters<Cuboid2> pred = new
    // UB16PredicateWithParameters<Cuboid2>(bPU ,0,0.5);

    GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);

    IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();

    for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {

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

    ShapefileWriter.write(iFeatC, p.get("result").toString() + "out.shp");

    System.out.println("That's all folks");

    // OCLBuildingsCuboidFinal oCB = new OCLBuildingsCuboidFinal();
    // //Rejection
    // sampler => Arrivera t il à proposer une solution ? La réponse dans un
    // prochain épisode

    // OCLBuildingsCuboidFinalWithPredicate oCB = new
    // OCLBuildingsCuboidFinalWithPredicate(); //Exécution de base
    /* Configuration<Cuboid2> cc = */

    // oCB.process(env.getBpU().get(1), p, env, 1);

  }

}
