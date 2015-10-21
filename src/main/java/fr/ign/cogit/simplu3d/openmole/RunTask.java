package fr.ign.cogit.simplu3d.openmole;

import java.io.File;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.feature.DefaultFeatureCollections;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.geotools.referencing.factory.epsg.FactoryUsingWKT;
import org.geotools.referencing.operation.DefaultMathTransformFactory;

import fr.ign.cogit.simplu3d.exec.experimentation.SamplePredicate;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.io.save.SaveGeneratedObjects;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.openmole.diversity.ParcelCoverageRatio;
import fr.ign.cogit.simplu3d.openmole.diversity.ParcelSignature;
import fr.ign.cogit.simplu3d.openmole.misc.TestDatumFactory;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained.OptimisedBuildingsCuboidDirectRejectionNoVisitor;
import fr.ign.cogit.simplu3d.util.AssignZ;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;

public class RunTask {
  public static void main(String[] args) throws Exception {
    File folder = new File("/home/julien/data/data");
    File folderOut = new File("/home/julien/tmp");
    File parameterFile = new File("/home/julien/data/building_parameters_project_expthese_temp.xml");
    int idBPU = 255;
    double distReculVoirie = 0.5;
    double distReculFond = 0.5;
    double distReculLat = 0.5;
    double maximalCES = 0.5;
    double hIniRoad = 10;
    double slopeRoad = 3;
    double hauteurMax = 20;
    long seed = 42;
    run(folder, folderOut, parameterFile, idBPU, distReculVoirie, distReculFond, distReculLat, maximalCES, hIniRoad, slopeRoad, hauteurMax,
        seed);
  }

  public static Hints hints = null;

  public static void prepareHints() {
    hints = GeoTools.getDefaultHints();
    hints.put(Hints.CRS_AUTHORITY_FACTORY, FactoryUsingWKT.class);
    hints.put(Hints.CRS_FACTORY, ReferencingObjectFactory.class);
    hints.put(Hints.MATH_TRANSFORM_FACTORY, DefaultMathTransformFactory.class);
    hints.put(Hints.DATUM_FACTORY, TestDatumFactory.class);
    hints.put(Hints.FEATURE_COLLECTIONS, DefaultFeatureCollections.class);
    hints.put(Hints.FILTER_FACTORY, FilterFactoryImpl.class);
  }

  public static TaskResult run(File folder, File folderOut, File parameterFile, int idBPU, double distReculVoirie, double distReculFond,
      double distReculLat, double maximalCES, double hIniRoad, double slopeRoad, double hauteurMax, long seed) throws Exception {
    System.out.println("folder : " + folder);
    System.out.println("folderOut : " + folderOut);
    System.out.println("parameterFile : " + parameterFile);
    if (hints == null)
      prepareHints();
    GeoTools.init(hints);
    AssignZ.DEFAULT_Z = 0;
    System.out.println("test");
    // On charge l'environnement
    Environnement env = LoaderSHP.loadNoDTM(folder);
    System.out.println("Env " + env.getCadastralParcels().size());
    // On charge le fichier de parametre
    if (!parameterFile.canRead()) System.out.println("can't read the parameter file"); 
    Parameters p = Parameters.unmarshall(parameterFile);
    // On récupère la parcelle sur laquelle on effectue la simulation
    BasicPropertyUnit bPU = null;
    for (BasicPropertyUnit bPUTemp : env.getBpU()) {
      System.out.println("id = " + bPUTemp.getId());
      if (bPUTemp.getId() == idBPU) {
        bPU = bPUTemp;
        break;
      }
    }
    if (bPU == null) {
      System.out.println("C'est null " + idBPU);
      throw new Exception("Null Property Unit");
    }
    // Chargement de l'optimiseur
    OptimisedBuildingsCuboidDirectRejectionNoVisitor oCB = new OptimisedBuildingsCuboidDirectRejectionNoVisitor();
    // Chargement des règles à appliquer
    SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(bPU, distReculVoirie,
        distReculFond, distReculLat, maximalCES, hIniRoad, slopeRoad);
    p.set("maxheight", hauteurMax);
    RandomGenerator rng = new MersenneTwister(seed);
    // Exécution de l'optimisation
    GraphConfiguration<Cuboid> cc = oCB.process(rng, bPU, p, env, pred);
    // Identifiant de la parcelle
    System.out.println("Nombre de blocs : " + cc.size());
    double energy = cc.getEnergy();
    ParcelSignature signatureEvaluator = new ParcelSignature(cc, bPU);
    long signature = signatureEvaluator.getSignature(p.getDouble("tileSize"));
    ParcelCoverageRatio coverageRatioEvaluator = new ParcelCoverageRatio(cc, bPU);
    double coverageRatio = coverageRatioEvaluator.getCoverageRatio();
    System.out.println("signature = " + signature);
    System.out.println("coverageArea = " + coverageRatio);
    String pathShapeFile = folderOut + File.separator + "sim_" + idBPU + "_drv_" + distReculVoirie + "_drf_" + distReculFond + "_drl_"
        + distReculLat + "_ces_" + maximalCES + "_hini_" + hIniRoad + "_sro_" + slopeRoad + "_hmax_" + hauteurMax + "_seed_" + seed + "_en_"
        + energy + "_sig_" + signature + "_cov_" + coverageRatio + ".shp";
    SaveGeneratedObjects.saveShapefile(pathShapeFile, cc, idBPU, seed);
    return new TaskResult(energy, coverageRatio, signature);
  }
}
