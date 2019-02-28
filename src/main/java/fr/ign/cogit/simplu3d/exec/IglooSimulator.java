package fr.ign.cogit.simplu3d.exec;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import com.thoughtworks.xstream.io.path.Path;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.io.shapefile.SaveGeneratedObjects;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.CompacityCollectionEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.predicate.SamplePredicate;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.cogit.simplu3d.util.SimpluParametersJSON;
import fr.ign.cogit.simplu3d.util.merge.MergeCuboid;
import fr.ign.cogit.simplu3d.util.merge.SDPCalc;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.rjmcmc.energy.BinaryEnergy;
import fr.ign.rjmcmc.energy.CollectionEnergy;
import fr.ign.rjmcmc.energy.ConstantEnergy;
import fr.ign.rjmcmc.energy.MinusUnaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesBinaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesCollectionEnergy;
import fr.ign.rjmcmc.energy.MultipliesUnaryEnergy;
import fr.ign.rjmcmc.energy.PlusUnaryEnergy;
import fr.ign.rjmcmc.energy.UnaryEnergy;

public class IglooSimulator {

  public static void main(String[] args) {
    
    
    // Loading of configuration file that contains sampling space
    // information and simulated annealing configuration
    
    //ATTENTION : c'est dans target qu'il faut le mettre  !!!
    String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
    
    String fileName = "paramsIgloo.json";
    SimpluParameters p = new SimpluParametersJSON(new File(folderName + fileName));

    
    
    
    // Load default environment (data are in resource directory)
    Environnement env;
    try {

          
      Path folder = new Path("/home/paulchapron/dev/simplu3d/src/main/resources/fr/ign/cogit/simplu3d/data") ;
      File FICHIER_ZONAGE = null;
      File FICHIER_PARCELLE = new File( folder + "/"+  "parcelleIgloo.shp");
      File FICHIER_VOIRIE = new File(folder  + "/"+ "route.shp");
      File FICHIER_BATIMENTS = new File(folder  + "/"+ "batiment.shp");
      File FICHIER_PRESC_LINEAIRE = new File(folder + "/"+  "PRESPCRIPTION_LIN.shp");
      File FICHIER_PRESC_PONCTUELLE = null ; 
      File FICHIER_PRESC_SURFACIQUE = null;
      File FICHIER_DOC_URBA = new File (folder + "/"+"DOC_URBA.shp" );
     
        
      
      
      env=LoaderSHP.load(null, FICHIER_DOC_URBA,FICHIER_ZONAGE, FICHIER_PARCELLE, FICHIER_VOIRIE,
          FICHIER_BATIMENTS, FICHIER_PRESC_PONCTUELLE, FICHIER_PRESC_LINEAIRE,
          FICHIER_PRESC_SURFACIQUE, null);
      
   
      System.out.println(env.getBpU().size() + " parcelle(s) à simuler" );
      
      
      BasicPropertyUnit bPU = env.getBpU().get(0);
     

      
    
      // il faut d'abord créer la config pour y coller la fonction d'optim 
      
      
       
      

      
      
      
    // Select a parcel on which generation is proceeded
    //BasicPropertyUnit bPU = env.getBpU().get(17);

    // Instantiation of the sampler
    OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

    // Rules parameters.8
    // Distance to road
    double distReculVoirie = 1;
    // Distance to bottom of the parcel
    double distReculFond = 1;
    // Distance to lateral parcel limits
    double distReculLat = 2;
    // Distance between two buildings of a parcel
    double distanceInterBati = 0;
    // Maximal ratio built area
    double maximalCES = 0.9;

    // Instantiation of the rule checker
    SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(
            bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati, maximalCES);

    
    
    
    // Run of the optimisation on a parcel with the predicate
    // and a specifi empty configuration embedding compacity 
    
    Geometry geombPU =  AdapterFactory.toGeometry(new GeometryFactory(), bPU.getGeom());
    GraphConfiguration<Cuboid> compacityConfig =  create_configurationCompacity(p, geombPU, bPU);
    


//    BasicCuboidOptimizer< Cuboid> bco = new BasicCuboidOptimizer<>() ;
//    GraphConfiguration<Cuboid> confQuimarche  = bco.create_configuration(p, geombPU,bPU);
  //GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred, confQuimarche);
    
    
    GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred, compacityConfig);
    
    
    
    
    
    System.out.println("dans la parcelle" + bPU.getId()+ "il y a" + cc.size()+ "cuboides" );
    

    
    
    
    // Writting the output
    String pathCourant = p.get("result").toString() + "out_"+ bPU.getId() +".shp";
    SaveGeneratedObjects.saveShapefile(pathCourant, cc, bPU.getId(), 0);

     
    
    
    
    System.out.println("merging ");
    
    MergeCuboid recal = new MergeCuboid();
    
    // on récupère les cubes du graph
   List<Cuboid> listCubz= cc.getGraph().vertexSet() .stream().map(x -> x.getValue()).collect(Collectors.toList());
   // on transforme 
   IFeatureCollection<IFeature> CubzfeatColl= new FT_FeatureCollection<>();
   for (Cuboid cucube : listCubz) {
     CubzfeatColl.add((IFeature)cucube);
   }
   
     
    //merge a partir d'un groupe de cuboides
    IFeature mergedCubz = recal.mergeAGroupOfCuboid(CubzfeatColl,  0, 0.05, 0.001);    
 
    
    
  System.out.println("Surface" + recal.getSurface());
  System.out.println("Energie" + cc.getEnergy());
  System.out.println("ratio" + cc.getEnergy()/recal.getSurface());
    
    SDPCalc sdpcalc = new SDPCalc();
    Double surfDePlancher = sdpcalc.process(cc.getGraph().vertexSet().stream().map(x ->x.getValue()).collect(Collectors.toList()));
    
    //NB : dans notre cas , volume= unary + binary 
    Double homogene_A_volume = cc.getBinaryEnergy() + cc.getUnaryEnergy();
    
    System.out.println( bPU.getId()+","+ cc.size()+","+recal.getSurface()+","+homogene_A_volume+","+surfDePlancher);

    
    System.out.println("that's all folks");

    
  } catch (Exception e) {
    // catch block pour l'env
    e.printStackTrace();
  }

    
    System.exit(0);
    
  }  

  

public static GraphConfiguration<Cuboid> create_configurationCompacity(SimpluParameters p, Geometry geom, BasicPropertyUnit bpu) {
  // Énergie constante : énergie de création d'un cuboide  
  ConstantEnergy<Cuboid, Cuboid> energyCreation = new ConstantEnergy<Cuboid, Cuboid>(p.getDouble("energy"));
  
  
  // Énergie constante : pondération de l'intersection
    ConstantEnergy<Cuboid, Cuboid> ponderationVolume = new ConstantEnergy<Cuboid, Cuboid>(
      p.getDouble("ponderation_volume"));
  
  // Énergie unaire : aire dans la parcelle
  UnaryEnergy<Cuboid> energyVolume = new VolumeUnaryEnergy<Cuboid>();
  
  // Multiplication de l'énergie d'intersection et de l'aire
  UnaryEnergy<Cuboid> energyVolumePondere = new MultipliesUnaryEnergy<Cuboid>(ponderationVolume, energyVolume);

  // On retire de l'énergie de création, l'énergie de l'aire
  UnaryEnergy<Cuboid> u3 = new MinusUnaryEnergy<Cuboid>(energyCreation, energyVolumePondere);

  double ponderationExt = p.getDouble("ponderation_difference_ext");

  UnaryEnergy<Cuboid> unaryEnergy;

  if (ponderationExt != 0) {
    // Énergie constante : pondération de la différence
    ConstantEnergy<Cuboid, Cuboid> ponderationDifference = new ConstantEnergy<Cuboid, Cuboid>(
        p.getDouble("ponderation_difference_ext"));
    // On ajoute l'énergie de différence : la zone en dehors de la parcelle
    UnaryEnergy<Cuboid> u4 = new DifferenceVolumeUnaryEnergy<Cuboid>(geom);
    UnaryEnergy<Cuboid> u5 = new MultipliesUnaryEnergy<Cuboid>(ponderationDifference, u4);
    unaryEnergy = new PlusUnaryEnergy<Cuboid>(u3, u5);
  } else {
    unaryEnergy = u3;
  }

  // Énergie binaire : intersection entre deux rectangles
  ConstantEnergy<Cuboid, Cuboid> c3 = new ConstantEnergy<Cuboid, Cuboid>(p.getDouble("ponderation_volume_inter"));
  BinaryEnergy<Cuboid, Cuboid> b1 = new IntersectionVolumeBinaryEnergy<Cuboid>();
  BinaryEnergy<Cuboid, Cuboid> binaryEnergy = new MultipliesBinaryEnergy<Cuboid, Cuboid>(c3, b1);
  
  //Energie de la collection,   fonction de la compacite 
   CollectionEnergy<Cuboid>compacite= new CompacityCollectionEnergy<Cuboid>();
       
   
// Énergie constante : pondération de la compacité de la collection
   ConstantEnergy<Cuboid, Cuboid> ponderationCompacite = new ConstantEnergy<Cuboid, Cuboid>(
       p.getDouble("ponderation_compacity"));
   ;
   
   CollectionEnergy<Cuboid>compacityWeighted = new MultipliesCollectionEnergy<Cuboid>(ponderationCompacite, compacite);
   
   
   
   
  // empty initial configuration*/
  //GraphConfiguration<Cuboid> conf = new GraphConfiguration<>(unaryEnergy, binaryEnergy,compacityWeighted);
  GraphConfiguration<Cuboid> conf = new GraphConfiguration<>(unaryEnergy, binaryEnergy, compacityWeighted);
  
  return conf;
}
      
  
}
