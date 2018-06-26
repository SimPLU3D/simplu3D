package fr.ign.cogit.simplu3d.experiments.openmole;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.cogit.simplu3d.util.SimpluParametersJSON;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.feature.DefaultFeatureCollections;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.geotools.referencing.factory.epsg.FactoryUsingWKT;
import org.geotools.referencing.operation.DefaultMathTransformFactory;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.analysis.streetprofile.Profile;
import fr.ign.cogit.geoxygene.sig3d.analysis.streetprofile.Profile.SIDE;
import fr.ign.cogit.geoxygene.sig3d.analysis.streetprofile.stats.ProfileMoran;
import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.exec.buildingprofile.SimulateAndCalcProfile;
import fr.ign.cogit.simplu3d.exec.experimentation.SamplePredicate;
import fr.ign.cogit.simplu3d.experiments.openmole.diversity.BoxCounter;
import fr.ign.cogit.simplu3d.experiments.openmole.diversity.EntropyIndicator;
import fr.ign.cogit.simplu3d.experiments.openmole.diversity.MaxHeight;
import fr.ign.cogit.simplu3d.experiments.openmole.diversity.ParcelCoverageRatio;
import fr.ign.cogit.simplu3d.experiments.openmole.diversity.ParcelSignature;
import fr.ign.cogit.simplu3d.experiments.openmole.diversity.ShonCalculation;
import fr.ign.cogit.simplu3d.experiments.openmole.msc.TestDatumFactory;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.io.shapefile.SaveGeneratedObjects;
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
    //String fileName = "recuit_bourrin.xml";

    //int idBPU = 255;,1.0,0.0,0.5,,7146147864011356101
    double distReculVoirie = 10.0;
    double distReculFond = 10.0;
    double distReculLat = 5.0;
    double maximalCES = 1.0;
    double hIniRoad = 0.0;
    double slopeRoad = 0.5;
    double hauteurMax = 23.96742381;
    long seed = 7146147864011356101L;
    // TaskResult result = run(folder, folderOut, parameterFile, idBPU,
    //     distReculVoirie, distReculFond, distReculLat, maximalCES, hIniRoad,
    //    slopeRoad, hauteurMax, seed);

    TaskExploPSE result = run2(folder, folderOut, new File(folderName+fileName), 
        distReculVoirie, distReculFond, distReculLat, maximalCES, hIniRoad,
        slopeRoad, hauteurMax, seed);

    System.out.println(result.toString());
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
    //hints.put(Hints.FEATURE_FACTORY, FeatureFactory.class);
    //hints.put(Hints.ATTRIBUTE_TYPE_FACTORY, AttributeTypeBuilder.class);
    hints.put(Hints.FEATURE_TYPE_FACTORY, FeatureTypeFactoryImpl.class);
    //hints.put(Hints.ATTRIBUTE_TYPE_FACTORY, AttributeTypeFacImpl.class );

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
    SimpluParameters p = new SimpluParametersJSON(parameterFile);
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

  public static TaskExploPSE run2(File folder, File folderOut, File parameterFile,
      double distReculVoirie, double distReculFond, double distReculLat,
      double maximalCES, double hIniRoad, double slopeRoad, double hauteurMax,
      long seed) throws Exception {
    System.out.println("folder out = " + folderOut);

    // Tous les cuboids de la zone
    IFeatureCollection<IFeature> cuboidOut = new FT_FeatureCollection<>();
    Collection<String[]> arrayParametersOut = new ArrayList<>();
    //
    double energyTot = 0;
    double areaTot = 0;
    double shon = 0;

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
    SimpluParameters p = new SimpluParametersJSON(parameterFile);

    ArrayList<Double> energy_parcels = new ArrayList<Double>(env.getBpU().size()); 

    int sizeBpu = env.getBpU().size();
    // iteration sur les parcelle de la simu
    for (int idBPU=0; idBPU < sizeBpu; idBPU++) {
      BasicPropertyUnit bPUTemp = env.getBpU().get(idBPU);
      if (bPUTemp == null) {
        System.out.println("Error : BPU is null");
        continue;
      }

      System.out.println("ID Parcel : " + bPUTemp.getId());
      //System.out.println("Simulation begins");

      //System.out.println("Tile size : " + p.getDouble("tileSize"));
      // Chargement de l'optimiseur
      OptimisedBuildingsCuboidDirectRejectionNoVisitor oCB = new OptimisedBuildingsCuboidDirectRejectionNoVisitor();
      //  OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

      // Chargement des règles à appliquer
      SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(
          bPUTemp, distReculVoirie, distReculFond, distReculLat, maximalCES,
          hIniRoad, slopeRoad);
      p.set("maxheight", hauteurMax);
      RandomGenerator rng = new MersenneTwister(seed);
      // Exécution de l'optimisation
      //      GraphConfiguration<Cuboid> cc = oCB.process( bPUTemp, p, env, bPUTemp.getId(), pred);
      GraphConfiguration<Cuboid> cc = oCB.process(rng, bPUTemp, p, env, pred);
      
      ShonCalculation sC = new ShonCalculation(cc);
      shon = shon + sC.getShon();

      for (Cuboid c : cc) {
        double[] conf = c.toArray();
        String[] array = new String[conf.length + 1];
        array[0] = String.valueOf(bPUTemp.getId());
        for (int i = 0; i < conf.length; i++) {
          array[i+1] = String.valueOf(conf[i]);
        }
        arrayParametersOut.add(array);
      }

      ExportAsFeatureCollection export = new ExportAsFeatureCollection(cc, bPUTemp.getId());
      cuboidOut.addAll(export.getFeatureCollection());

      //on garde l'energie du batiment de la parcelle 
      double energy_parcel = cc.getEnergy();
     // System.out.println("parcelle ID "+ bPUTemp.getId()+" energie " + energy_parcel);

      energy_parcels.add(energy_parcel);
      energyTot = energyTot + energy_parcel;
      areaTot = areaTot + bPUTemp.getArea();

      //System.out.println("Simulation ends");

    }

 

    // Identifiant de la parcelle
    //System.out.println("ParcelSignature begins");
    //  ParcelSignature signatureEvaluator = new ParcelSignature(env.getBpU().getEnvelope(), cuboidOut);
    long signature = 0; // signatureEvaluator.getSignature(p.getDouble("tileSize"));


   //System.out.println("ParcelCoverageRatio begins");

    // Signature sur la zone
    ParcelCoverageRatio coverageRatioEvaluator = new ParcelCoverageRatio(cuboidOut, areaTot);
    double coverageRatio = coverageRatioEvaluator.getCoverageRatio();

    // System.out.println("ShapefileWriter begins");
    //    String pathShapeFile =folderOut + File.separator + "test.shp";
//    String pathShapeFile = folderOut + File.separator +  "out.shp";
//        ShapefileWriter.write(cuboidOut, pathShapeFile );
//        System.out.println("ShapefileWriter ends");
    EntropyIndicator ent = new EntropyIndicator();
    ent.calculate(env, energy_parcels, areaTot, energyTot);
    double gini = ent.getGiniFinal();
    double moran = ent.getMoranFinal();
    double entropy = ent.getEntropyRelFinal();
    
    BoxCounter bC = new BoxCounter(cuboidOut);
    double boxCount = bC.count();
    
    MaxHeight mX = new MaxHeight(cuboidOut);
    double maxHeight = mX.height();
    
    
    double densite = shon/ areaTot;
    System.out.println("essai profil");
    
    

    Profile profile = SimulateAndCalcProfile.calculateProfile(env, cuboidOut);
    
    List<Double> heights = profile.getHeightAlongRoad(SIDE.BOTH);

    ProfileMoran pM = new ProfileMoran();
    pM.calculate(heights);
    double moranProfile = pM.getMoranProfileFinal();
    
    
    
    //###########################################"
    
    
    
    
    
    
    

    String pathCSVFile =folderOut + File.separator + "out.csv";
    File outCSVFile=new File(pathCSVFile);
    BufferedWriter writer = new BufferedWriter(new FileWriter(outCSVFile));
    writer.write("idparcel,centerx, centery, length, width, height, orientation\n");
    for (String[] array:arrayParametersOut) {
      String line = array[0];
      for (int i = 1 ; i < array.length ; i++) line += ", " + array[i];
      writer.write(line+"\n");
    }
    //Close writer
    writer.close();

    System.out.println("Return Taskresult");

    return new TaskExploPSE(energyTot, coverageRatio, gini, moran, entropy, boxCount, maxHeight, densite, moranProfile);
  }

}
