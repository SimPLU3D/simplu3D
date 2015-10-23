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
    double distReculVoirie = 1.;
    double distReculFond = 1.;
    double distReculLat = 1.;
    double maximalCES = 0.5;
    double hIniRoad = 5;
    double slopeRoad = 1.5;
    double hauteurMax = 20;
    long seed = 8792926807833038395l;
    TaskResult result = run(folder, folderOut, parameterFile, idBPU, distReculVoirie, distReculFond, distReculLat, maximalCES, hIniRoad, slopeRoad, hauteurMax, seed);
    System.out.println("energy = " + result.energy + " with coverage = " + result.coverageRatio + " and signature " + result.signature);
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
    System.out.println("folder out = " + folderOut);
    if (!folderOut.exists()) {
      folderOut.mkdirs();
      if (folderOut.exists()) System.out.println("I had to create it though");
      else {
        System.out.println("I could not create it...");
        throw new Exception("Could not create temp directory");
      }
    } else {
      System.out.println("We're all good!");
    }
    if (hints == null) prepareHints();
    GeoTools.init(hints);
    AssignZ.DEFAULT_Z = 0;
    // On charge l'environnement
    Environnement env = LoaderSHP.loadNoDTM(folder);
    // On charge le fichier de parametre
    Parameters p = Parameters.unmarshall(parameterFile);
    // On récupère la parcelle sur laquelle on effectue la simulation
    BasicPropertyUnit bPU = null;
    for (BasicPropertyUnit bPUTemp : env.getBpU()) {
      if (bPUTemp.getId() == idBPU) {
        bPU = bPUTemp;
        break;
      }
    }
    if (bPU == null) {
      throw new Exception("Null Property Unit " + bPU);
    }
    // Chargement de l'optimiseur
    OptimisedBuildingsCuboidDirectRejectionNoVisitor oCB = new OptimisedBuildingsCuboidDirectRejectionNoVisitor();
    // Chargement des règles à appliquer
    SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(bPU, distReculVoirie, distReculFond, distReculLat, maximalCES, hIniRoad, slopeRoad);
    p.set("maxheight", hauteurMax);
    RandomGenerator rng = new MersenneTwister(seed);
    // Exécution de l'optimisation
    GraphConfiguration<Cuboid> cc = oCB.process(rng, bPU, p, env, pred);
    // Identifiant de la parcelle
    double energy = cc.getEnergy();
    ParcelSignature signatureEvaluator = new ParcelSignature(cc, bPU);
    long signature = signatureEvaluator.getSignature(p.getDouble("tileSize"));
    ParcelCoverageRatio coverageRatioEvaluator = new ParcelCoverageRatio(cc, bPU);
    double coverageRatio = coverageRatioEvaluator.getCoverageRatio();
    String pathShapeFile = folderOut + File.separator + "sim_" + idBPU + "_drv_" + distReculVoirie + "_drf_" + distReculFond + "_drl_"
        + distReculLat + "_ces_" + maximalCES + "_hini_" + hIniRoad + "_sro_" + slopeRoad + "_hmax_" + hauteurMax + "_seed_" + seed + "_en_"
        + energy + "_sig_" + signature + "_cov_" + coverageRatio + ".shp";
    SaveGeneratedObjects.saveShapefile(pathShapeFile, cc, idBPU, seed);
    return new TaskResult(energy, coverageRatio, signature);
  }
}
