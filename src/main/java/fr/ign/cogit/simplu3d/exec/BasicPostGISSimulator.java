package fr.ign.cogit.simplu3d.exec;

import fr.ign.cogit.simplu3d.io.load.application.ExperimentationPostGIS;
import fr.ign.cogit.simplu3d.io.load.application.LoadPostGIS;
import fr.ign.cogit.simplu3d.io.load.application.ParametersPostgis;
import fr.ign.cogit.simplu3d.io.save.application.SaveGeneratedObjects;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained.OptimisedBuildingsCuboidDirectRejectionNoVisitor;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.predicate.UB16PredicateWithParameters;
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

    // Connexion à la base postgis
    String host = "localhost";
    String port = "5432";
    String database = "gtru";
    String user = "postgres";
    String pw = "postgres";

    // id de l'expérimentation considérée
    int idExperiment = 1;

    // Chargement de l'expérimentation
    ExperimentationPostGIS exp = new ExperimentationPostGIS(host, port,
        database, user, pw, idExperiment);

    // Chargement de l'environnement depuis PostGIS
    LoadPostGIS lP = new LoadPostGIS(host, port, database, user, pw);
    Environnement env = lP.loadNoOCLRules();

    // Chargement des paramètres de simulation
    ParametersPostgis p = new ParametersPostgis(host, port, database, user, pw,
        exp.getInteger(ExperimentationPostGIS.EXPERIMENTATION_ID_PARAM));

    // Pour l'instant on prend le premier
    BasicPropertyUnit bPU = env.getBpU().get(1);

    //Chargement de l'optimiseur
    OptimisedBuildingsCuboidDirectRejectionNoVisitor oCB = new OptimisedBuildingsCuboidDirectRejectionNoVisitor();

    //Chargement des règles à appliquer
    UB16PredicateWithParameters<Cuboid> pred = new UB16PredicateWithParameters<Cuboid>(
        bPU, exp.getDouble("hini"), exp.getDouble("slope"));

    //Exécution de l'optimisation
    Configuration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);

    // On sauve les objets
    boolean saved = SaveGeneratedObjects.save(host, port, database, user, pw,
        cc, idExperiment, bPU.getCadastralParcel().get(0).getId());

    // On a bien effectué l'expérimentation
    if (saved) {
      exp.setProcessed();
    }

    System.out.println("That's all folks");

  }

}
