package fr.ign.cogit.simplu3d.exec;

import fr.ign.cogit.simplu3d.io.load.application.ExperimentationPostGIS;
import fr.ign.cogit.simplu3d.io.load.application.LoadPostGIS;
import fr.ign.cogit.simplu3d.io.load.application.ParametersPostgis;
import fr.ign.cogit.simplu3d.io.load.application.SaveEnergyPostGIS;
import fr.ign.cogit.simplu3d.io.save.SaveGeneratedObjects;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained.OptimisedBuildingsCuboidDirectRejectionNoVisitor;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.predicate.UB16PredicateWithParameters;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;

/**
 * Simulateur standard
 * 
 * @author MBrasebin
 * 
 */
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

    for (BasicPropertyUnit bPU : env.getBpU()) {
      // Chargement de l'optimiseur
      OptimisedBuildingsCuboidDirectRejectionNoVisitor oCB = new OptimisedBuildingsCuboidDirectRejectionNoVisitor();

      // Chargement des règles à appliquer
      UB16PredicateWithParameters<Cuboid,GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new UB16PredicateWithParameters<>(
          bPU, exp.getDouble("hini"), exp.getDouble("slope"));

      // Exécution de l'optimisation
      GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);

      // Identifiant de la parcelle

      int idParcelle = bPU.getCadastralParcel().get(0).getId();

      // On sauve les objets
      SaveGeneratedObjects.save(host, port, database, user, pw, cc,
          idExperiment, idParcelle);

      // On sauve la valeur de l'optimisation
      SaveEnergyPostGIS.save(host, port, database, user, pw, idExperiment,
          idParcelle, cc.getEnergy());

    }

    // On a bien effectué l'expérimentation
    exp.setProcessed();

    System.out.println("That's all folks");

  }

}
