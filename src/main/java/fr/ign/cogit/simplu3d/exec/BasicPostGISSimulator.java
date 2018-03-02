package fr.ign.cogit.simplu3d.exec;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import fr.ign.cogit.simplu3d.experiments.thesis.predicate.UB16PredicateWithParameters;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.postgis.LoadPostGIS;
import fr.ign.cogit.simplu3d.io.postgis.ExperimentationPostGIS;
import fr.ign.cogit.simplu3d.io.postgis.ParametersPostgis;
import fr.ign.cogit.simplu3d.io.postgis.SaveEnergyPostGIS;
import fr.ign.cogit.simplu3d.io.shapefile.SaveGeneratedObjects;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidDirectRejectionNoVisitor;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;

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
public class BasicPostGISSimulator {

  /**
   * @param args
   * @throws Exception
   */

  public static void main() throws Exception {
    main(null);
  }

  // [building_footprint_rectangle_cli_main
  public static void main(String[] args) throws Exception {

    // Connexion à la base postgis
    String host = "rks1009w140";
    String port = "5432";
    String database = "gtru";
    String user = "postgres";
    String pw = "postgres";

    int lastExpID = 2;
    long nbRun = 2;

    // id de l'expérimentation considérée
    for (int idExperiment = 1; idExperiment <= lastExpID; idExperiment++) {

      // id du run
      for (long run = 0; run < nbRun; run++) {
        // TODO use something better that the run ID as a seed

        // Chargement de l'expérimentation
				ExperimentationPostGIS exp = new ExperimentationPostGIS(host, port, database, user, pw, idExperiment);

        // Chargement de l'environnement depuis PostGIS
        LoadPostGIS lP = new LoadPostGIS(host, port, database, user, pw);
        Environnement env = lP.loadNoOCLRules();

        // Chargement des paramètres de simulation
        ParametersPostgis p = new ParametersPostgis(host, port, database, user, pw,
            exp.getInteger(ExperimentationPostGIS.EXPERIMENTATION_ID_PARAM));

        for (BasicPropertyUnit bPU : env.getBpU()) {
          // Chargement de l'optimiseur
          OptimisedBuildingsCuboidDirectRejectionNoVisitor oCB = new OptimisedBuildingsCuboidDirectRejectionNoVisitor();

          // Chargement des règles à appliquer
          UB16PredicateWithParameters<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new UB16PredicateWithParameters<>(
              bPU, exp.getDouble("hIni"), exp.getDouble("slope"));

          RandomGenerator rng = new MersenneTwister(run);
          // Exécution de l'optimisation
          GraphConfiguration<Cuboid> cc = oCB.process(rng, bPU, p, env, pred);

          // Identifiant de la parcelle

          int idParcelle = bPU.getCadastralParcels().get(0).getId();

          // On sauve les objets
          SaveGeneratedObjects.save(host, port, database, user, pw, cc, idExperiment, idParcelle, run);

          // On sauve la valeur de l'optimisation
          SaveEnergyPostGIS.save(host, port, database, user, pw, idExperiment, idParcelle, (int) run, cc.getEnergy());
        }
        // On a bien effectué l'expérimentation
        exp.setProcessed();
      }
    }
    System.out.println("That's all folks");
  }
}
