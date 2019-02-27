package fr.ign.cogit.simplu3d.experiments.morpho;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.ShapeFileLoader;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_OrientableSurface;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.io.shapefile.SaveGeneratedObjects;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.generic.predicate.SamplePredicate;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.cogit.simplu3d.util.SimpluParametersJSON;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;

/*
computation of generated buildings compacity
*/

public class Compacity {

  public static void main(String[] args) throws Exception {
    
    String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
    String fileName = "building_parameters_project_expthese_3.json";
    SimpluParameters p = new SimpluParametersJSON(new File(folderName + fileName));

    // Load default environment (data are in resource directory)
    Environnement env = LoaderSHP.loadNoDTM(new File(
            DemoEnvironmentProvider.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/data/").getPath()));

    // list of parcels
    BasicPropertyUnit bPU = env.getBpU().get(8);

    
    
 // Instantiation of the sampler
    OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

    // Rules parameters.8
    // Distance to road
    double distReculVoirie = 2;
    // Distance to bottom of the parcel
    double distReculFond = 0;
    // Distance to lateral parcel limits
    double distReculLat = 4;
    // Distance between two buildings of a parcel
    double distanceInterBati = 0;
    // Maximal ratio built area
    double maximalCES = 0.5;

    // Instantiation of the rule checker
    SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati, maximalCES);

    // Run of the optimisation on a parcel with the predicate
    GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);
    
    System.out.println("optim terminée");
    System.out.println("nb cuboid" +cc.size());
    
   
    
    IFeatureCollection<IFeature> featCollec = new FT_FeatureCollection<>();
    for (GraphVertex<? extends AbstractSimpleBuilding> v : cc.getGraph().vertexSet()) {

        AbstractSimpleBuilding building = v.getValue();
        
        featCollec.add(building);
    }
    
    
      AbstractSimpleBuilding premierbatiment = (AbstractSimpleBuilding)featCollec.get(0);
    
      System.out.println("Wall surface" + premierbatiment.getWallSurfaces() );
      System.out.println("Volume" + premierbatiment.getVolume());
      
     IMultiSurface<IOrientableSurface> multisurf = (IMultiSurface<IOrientableSurface>) premierbatiment.getLod2MultiSurface();
      
      System.out.println("nb surfaces" + multisurf.size());
//       IFeature batiment = cc.;
//       
////   
//    Double aire = geomBatiment.area() ;  
//    int nbppints =  geomBatiment.numPoints() ;
//    System.out.println("formes constituées de " + nbppints +"points"+ "aire de " + aire);
    
    
    
    System.out.println("that's all folks");
}
 
 
  
  
  
}
