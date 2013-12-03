package fr.ign.cogit.simplu3d.exec;

import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.OCLBuildingsCuboidFinal;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.OCLBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.OCLBuildingsCuboidFinalWithPredicate;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.predicate.UXL3Predicate;
import fr.ign.parameters.Parameters;

public class BigFuckingUltimateBuildingGeneratorDeluxe {

  /**
   * @param args
   */

  // [building_footprint_rectangle_cli_main
  public static void main(String[] args) throws Exception {
    String folderName = "./src/main/resources/scenario/";
    String fileName = "building_parameters_project_expthese_1.xml";
    Parameters p = initialize_parameters(folderName + fileName);
    Environnement env = LoaderSHP.load(p.get("folder"));
  //   OCLBuildingsCuboidFinalDirectRejection oCB = new OCLBuildingsCuboidFinalDirectRejection();
     OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();
     UXL3Predicate<Cuboid2> pred = new UXL3Predicate<>(env.getBpU().get(1));
     oCB.process(env.getBpU().get(1), p, env, 1,pred);
     
     
   //OCLBuildingsCuboidFinal oCB = new OCLBuildingsCuboidFinal();  //Rejection sampler => Arrivera t il à proposer une solution ? La réponse dans un prochain épisode
   
   
  // OCLBuildingsCuboidFinalWithPredicate oCB = new OCLBuildingsCuboidFinalWithPredicate(); //Exécution de base
    /* Configuration<Cuboid2> cc = */
     
     
     
   //  oCB.process(env.getBpU().get(1), p, env, 1);
     

  }

  private static Parameters initialize_parameters(String name) {
    return Parameters.unmarshall(name);
  }
}
