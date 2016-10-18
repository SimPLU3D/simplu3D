package fr.ign.cogit.simplu3d.experiments.openmole;

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

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.exec.experimentation.SamplePredicate;
import fr.ign.cogit.simplu3d.experiments.openmole.diversity.ParcelCoverageRatio;
import fr.ign.cogit.simplu3d.experiments.openmole.diversity.ParcelSignature;
import fr.ign.cogit.simplu3d.experiments.openmole.msc.TestDatumFactory;
import fr.ign.cogit.simplu3d.io.SaveGeneratedObjects;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidDirectRejectionNoVisitor;
import fr.ign.cogit.simplu3d.util.AssignZ;
import fr.ign.cogit.simplu3d.util.convert.ExportAsFeatureCollection;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;

public class RunTask {
  public static void main(String[] args) throws Exception {
    File folder =  new File(
        DemoEnvironmentProvider.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/data/").getPath());
    
    File folderOut = new File("/home/pchapron/temp/");
    
    
    
    String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
    String fileName = "building_parameters_project_expthese_3.xml";
    int idBPU = 255;
    double distReculVoirie = 4.53125;
    double distReculFond = 2.65625;
    double distReculLat = 2.65625;
    double maximalCES = 0.40937499999999993;
    double hIniRoad = 3.59375;
    double slopeRoad = 0.09375;
    double hauteurMax = 14.6875;
    long seed = -3637137549655303736L;
   // TaskResult result = run(folder, folderOut, parameterFile, idBPU,
    //     distReculVoirie, distReculFond, distReculLat, maximalCES, hIniRoad,
    //    slopeRoad, hauteurMax, seed);
    
    TaskResult result = run2(folder, folderOut, new File(folderName+fileName), 
        distReculVoirie, distReculFond, distReculLat, maximalCES, hIniRoad,
        slopeRoad, hauteurMax, seed);
    
    
    System.out.println("energy = " + result.energy + " with coverage = "
        + result.coverageRatio + " and signature " + result.signature);
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

  public static TaskResult run(File folder, File folderOut, File parameterFile,
      int idBPU, double distReculVoirie, double distReculFond,
      double distReculLat, double maximalCES, double hIniRoad, double slopeRoad,
      double hauteurMax, long seed) throws Exception {
    System.out.println("folder out = " + folderOut);
    if (!folderOut.exists()) {
      folderOut.mkdirs();
      if (folderOut.exists())
        System.out.println("I had to create it though");
      else {
        System.out.println("I could not create it...");
        throw new Exception("Could not create temp directory");
      }
    } else {
      System.out.println("We're all good!");
    }
    if (hints == null)
      prepareHints();
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
    SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(
        bPU, distReculVoirie, distReculFond, distReculLat, maximalCES, hIniRoad,
        slopeRoad);
    p.set("maxheight", hauteurMax);
    RandomGenerator rng = new MersenneTwister(seed);
    // Exécution de l'optimisation
    GraphConfiguration<Cuboid> cc = oCB.process(rng, bPU, p, env, pred);
    // Identifiant de la parcelle
    double energy = cc.getEnergy();
    ParcelSignature signatureEvaluator = new ParcelSignature(cc, bPU);
    long signature = signatureEvaluator.getSignature(p.getDouble("tileSize"));
    ParcelCoverageRatio coverageRatioEvaluator = new ParcelCoverageRatio(cc,
        bPU);
    double coverageRatio = coverageRatioEvaluator.getCoverageRatio();
    String pathShapeFile = folderOut + File.separator + "sim_" + idBPU + "_drv_"
        + distReculVoirie + "_drf_" + distReculFond + "_drl_" + distReculLat
        + "_ces_" + maximalCES + "_hini_" + hIniRoad + "_sro_" + slopeRoad
        + "_hmax_" + hauteurMax + "_seed_" + seed + "_en_" + energy + "_sig_"
        + signature + "_cov_" + coverageRatio + ".shp";
    SaveGeneratedObjects.saveShapefile(pathShapeFile, cc, idBPU, seed);
    return new TaskResult(energy, coverageRatio, signature);
  }

  public static TaskResult run2(File folder, File folderOut, File parameterFile,
      double distReculVoirie, double distReculFond, double distReculLat,
      double maximalCES, double hIniRoad, double slopeRoad, double hauteurMax,
      long seed) throws Exception {
    System.out.println("folder out = " + folderOut);

    // Tous les cuboids de la zone
    IFeatureCollection<IFeature> cuboidOut = new FT_FeatureCollection<>();
    //
    double energyTot = 0;
    double areaTot = 0;
    
    if (!folderOut.exists()) {
      folderOut.mkdirs();
      if (folderOut.exists())
        System.out.println("I had to create it though");
      else {
        System.out.println("I could not create it...");
        throw new Exception("Could not create temp directory");
      }
    } else {
      System.out.println("We're all good!");
    }
    if (hints == null)
      prepareHints();
    GeoTools.init(hints);
    AssignZ.DEFAULT_Z = 0;
    // On charge l'environnement
    Environnement env = LoaderSHP.loadNoDTM(folder);
    // On charge le fichier de parametre
    Parameters p = Parameters.unmarshall(parameterFile);
    // On récupère la parcelle sur laquelle on effectue la simulation

    for (BasicPropertyUnit bPUTemp : env.getBpU()) {

      if (bPUTemp == null) {
        System.out.println("Error : BPU is null");
        continue;
      }
      
      System.out.println("ID Parcel : " + bPUTemp.getId());
      // Chargement de l'optimiseur
      OptimisedBuildingsCuboidDirectRejectionNoVisitor oCB = new OptimisedBuildingsCuboidDirectRejectionNoVisitor();
      // Chargement des règles à appliquer
      SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(
          bPUTemp, distReculVoirie, distReculFond, distReculLat, maximalCES,
          hIniRoad, slopeRoad);
      p.set("maxheight", hauteurMax);
      RandomGenerator rng = new MersenneTwister(seed);
      // Exécution de l'optimisation
      GraphConfiguration<Cuboid> cc = oCB.process(rng, bPUTemp, p, env, pred);

      ExportAsFeatureCollection export = new ExportAsFeatureCollection(cc,
          bPUTemp.getId(), seed);
      cuboidOut.addAll(export.getFeatureCollection());
      
      
      energyTot = energyTot + cc.getEnergy();
      areaTot = areaTot + bPUTemp.getArea();
      
      break;
    }

    // Identifiant de la parcelle
 
    ParcelSignature signatureEvaluator = new ParcelSignature(env.getBpU().getEnvelope(), cuboidOut);
    long signature = signatureEvaluator.getSignature(p.getDouble("tileSize"));

    
 
    // Signature sur la zone
    ParcelCoverageRatio coverageRatioEvaluator = new ParcelCoverageRatio(cuboidOut, areaTot);
    double coverageRatio = coverageRatioEvaluator.getCoverageRatio();

    String pathShapeFile = folderOut + File.separator + "sim_" + "_drv_"
        + distReculVoirie + "_drf_" + distReculFond + "_drl_" + distReculLat
        + "_ces_" + maximalCES + "_hini_" + hIniRoad + "_sro_" + slopeRoad
        + "_hmax_" + hauteurMax + "_seed_" + seed + "_en_" + energyTot + "_sig_"
        + signature + "_cov_" + coverageRatio + ".shp";
    
    
    ShapefileWriter.write(cuboidOut, pathShapeFile );
    

    
    
    return new TaskResult(energyTot, coverageRatio, signature);
  }

}
