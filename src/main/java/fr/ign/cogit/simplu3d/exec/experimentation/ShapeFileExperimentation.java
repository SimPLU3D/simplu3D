package fr.ign.cogit.simplu3d.exec.experimentation;

import java.io.File;

import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.cogit.simplu3d.util.SimpluParametersJSON;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.io.shapefile.SaveGeneratedObjects;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidDirectRejectionNoVisitor;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
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
 **/
public class ShapeFileExperimentation {

  /**
   * @param args
   * @throws Exception
   */

  public static void main() throws Exception {
    main(null);
  }

  // [building_footprint_rectangle_cli_main
  public static void main(String[] args) throws Exception {

    // Chargement du fichier de configuration
    String folderName = BasicSimulator.class.getClassLoader()
        .getResource("scenario/").getPath();
    String fileName = "building_parameters_project_expthese_3.xml";

    SimpluParameters p = new SimpluParametersJSON(new File(folderName + fileName));
    String folderSave = p.getString("result");

    int nbRun = 1;

    double distReculFondMin = 0;
    double distReculFondMax = 10;
    double distReculFondStep = 0.2;

    double distReculLatMin = 0;
    double distReculLatMax = 7.5;
    double distReculLatStep = 0.2;

    double distReculVoirieMin = 0;
    double distReculVoirieMax = 10;
    double distReculVoirieStep = 0.2;

    double maximalCESMin = 0.5;
    double maximalCESMax = 1;
    double maximalCESStep = 0.1;

    double hIniRoadMin = 5;
    double hIniRoadMax = 10;
    double hIniRoadStep = 0.5;

    double slopeRoadMin = 0;
    double slopeRoadMax = 4;
    double slopeRoadStep = 0.2;

    double hauteurMaxMin = 5;
    double hauteurMaxMax = 20;
    double hauteurMaxStep = 0.5;

    for (int run = 0; run < nbRun; run++) {

      for (double distReculFond = distReculFondMin; distReculFond <= distReculFondMax; distReculFond = distReculFond
          + distReculFondStep) {

        for (double distReculLat = distReculLatMin; distReculLat <= distReculLatMax; distReculLat = distReculLat
            + distReculLatStep) {

          for (double distReculVoirie = distReculVoirieMin; distReculVoirie <= distReculVoirieMax; distReculVoirie = distReculVoirie
              + distReculVoirieStep) {

            for (double maximalCES = maximalCESMin; maximalCES < maximalCESMax; maximalCES = maximalCES
                + maximalCESStep) {

              for (double hIniRoad = hIniRoadMin; hIniRoad < hIniRoadMax; hIniRoad = hIniRoad
                  + hIniRoadStep) {

                for (double slopeRoad = slopeRoadMin; slopeRoad < slopeRoadMax; slopeRoad = slopeRoad
                    + slopeRoadStep) {
                  for (double hauteurMax = hauteurMaxMin; hauteurMax < hauteurMaxMax; hauteurMax = hauteurMax
                      + hauteurMaxStep) {

                    // Chargement de l'environnement
                    Environnement env = DemoEnvironmentProvider.getDefaultEnvironment();

                    for (BasicPropertyUnit bPU : env.getBpU()) {
                      ShapeFileExperimentation.run(p, folderSave, bPU, env,
                          distReculVoirie, distReculFond, distReculLat,
                          maximalCES, hIniRoad, slopeRoad, hauteurMax, run);
                    }

                 //   System.exit(0);
                  }
                }
              }

            }

          }
        }
      }

    }

  }

  public static void run(SimpluParameters p, String folderSave,
      BasicPropertyUnit bPU, Environnement env, double distReculVoirie,
      double distReculFond, double distReculLat, double maximalCES,
      double hIniRoad, double slopeRoad, double hauteurMax, long seed) {

    // id du run

    // Chargement de l'environnement depuis PostGIS

    // Chargement de l'optimiseur
    OptimisedBuildingsCuboidDirectRejectionNoVisitor oCB = new OptimisedBuildingsCuboidDirectRejectionNoVisitor();

    // Chargement des règles à appliquer
    SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(
        bPU, distReculVoirie, distReculFond, distReculLat, maximalCES,
        hIniRoad, slopeRoad);

    p.set("maxheight", hauteurMax);
    RandomGenerator rng = new MersenneTwister(seed);

    // Exécution de l'optimisation
    GraphConfiguration<Cuboid> cc = oCB.process(rng, bPU, p, env, pred);

    // Identifiant de la parcelle

    int idParcelle = bPU.getCadastralParcels().get(0).getId();

    double energy = cc.getEnergy();

    String pathShapeFile = folderSave + "_"+ idParcelle + "_drv_" + distReculVoirie + "_drf_"
        + distReculFond + "_drl_" + distReculLat + "_ces_" + maximalCES
        + "_hini_" + hIniRoad + "_sro_" + slopeRoad + "_hmax_" + hauteurMax
        + "_seed_" + seed + "_en_" + energy + ".shp";

    SaveGeneratedObjects.saveShapefile(pathShapeFile, cc, idParcelle, seed);

  }

}
