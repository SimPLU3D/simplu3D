package fr.ign.cogit.simplu3d.exec;

import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.OCLBuildingsCuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.OCLBuildingsCuboidFinal;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.mpp.configuration.Configuration;
import fr.ign.parameters.Parameters;

public class BigFuckingUltimateBuildingGeneratorDeluxe {

  /**
   * @param args
   */

  // [building_footprint_rectangle_cli_main
  public static void main(String[] args) throws Exception {

    String folderName = "./src/main/resources/scenario/";
    
    String fileName ="building_parameters_project_expthese_1.xml";

    Parameters p = initialize_parameters(folderName+fileName);

    Environnement env = LoaderSHP.load(p.get("folder"));


    OCLBuildingsCuboidFinal oCB = new OCLBuildingsCuboidFinal();

      Configuration<Cuboid2> cc = oCB.process(
          env.getBpU().get(1), p, env, 1);


  }

  private static Parameters initialize_parameters(String name) {
    return Parameters.unmarshall(name);
  }
}
