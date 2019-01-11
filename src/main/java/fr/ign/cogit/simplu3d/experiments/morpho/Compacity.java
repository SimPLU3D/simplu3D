package fr.ign.cogit.simplu3d.experiments.morpho;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.ShapeFileLoader;
import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.io.shapefile.SaveGeneratedObjects;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.generic.predicate.SamplePredicate;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.cogit.simplu3d.util.SimpluParametersJSON;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;

/*
computation of generated buildings compacity
*/

public class Compacity {

  public static void main(String[] args) throws Exception {

    
    String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
    String fileName = "building_parameters_project_expthese_3.json";
   // SimpluParameters p = new SimpluParametersJSON(new File(folderName + fileName));

    // Load default environment (data are in resource directory)
    Environnement env = LoaderSHP.loadNoDTM(new File(
            DemoEnvironmentProvider.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/data/").getPath()));

    // list of parcels
    IFeatureCollection<BasicPropertyUnit> bPU = env.getBpU();

    
     String ConfigfolderName = "/tmp/" ;
    String ConfigfileName = "out.shp";
 
   FT_FeatureCollection<IFeature > cc =  ShapeFileLoader.loadingShapeFile(ConfigfolderName + ConfigfileName, "", "", true);
    
//   System.out.println(cc.size());
   
    IFeature batiment = cc.get(0);
    IGeometry geomBatiment= batiment.getGeom();
   
//    System.out.println(p.get("result").toString() + "out.shp");
//    System.out.println("that's all folks");
}
  
  
  
}
