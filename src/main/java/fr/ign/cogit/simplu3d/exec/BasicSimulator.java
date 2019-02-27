package fr.ign.cogit.simplu3d.exec;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.io.shapefile.SaveGeneratedObjects;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.generic.predicate.SamplePredicate;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.cogit.simplu3d.util.SimpluParametersJSON;
import fr.ign.cogit.simplu3d.util.merge.MergeCuboid;
import fr.ign.cogit.simplu3d.util.merge.SDPCalc;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see http://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 * 
 *          Simulateur standard
 * 
 * 
 */
public class BasicSimulator {

	
	public static void main(String[] args) throws Exception {

		// Loading of configuration file that contains sampling space
		// information and simulated annealing configuration
		String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
		
		String fileName = "building_parameters_project_expthese_3.json";
		SimpluParameters p = new SimpluParametersJSON(new File(folderName + fileName));

		// Load default environment (data are in resource directory)
		Environnement env = LoaderSHP.loadNoDTM(new File(
		DemoEnvironmentProvider.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/data/").getPath()));

		for(int i=0; i<80; i++) {
		System.out.println("------------------------------");
		  for(BasicPropertyUnit bPU: env.getBpU()) {
		
		// Select a parcel on which generation is proceeded
		//BasicPropertyUnit bPU = env.getBpU().get(17);

		// Instantiation of the sampler
		OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

		// Rules parameters.8
		// Distance to road
		double distReculVoirie = 1;
		// Distance to bottom of the parcel
		double distReculFond = 0;
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
		GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);
		
		
		//System.out.println("dans la parcelle" + bPU.getId()+ "il y a" + cc.size()+ "cuboides" );
		
	
		
		
		
		// Writting the output
		String pathCourant = p.get("result").toString() + "out_"+ bPU.getId() +".shp";
		SaveGeneratedObjects.saveShapefile(pathCourant, cc, bPU.getId(), 0);
		
		MergeCuboid recal = new MergeCuboid();

        IFeatureCollection<IFeature> fus = recal.mergeFromShapefile(pathCourant, 0.0);
//		System.out.println("Surface" + recal.getSurface());
//		System.out.println("Energie" + cc.getEnergy());
//		System.out.println("ratio" + cc.getEnergy()/recal.getSurface());
        
        SDPCalc sdpcalc = new SDPCalc();
        
        Double sdp = sdpcalc.process(pathCourant);
        
        System.out.println( bPU.getId()+","+ cc.size()+","+recal.getSurface()+","+cc.getEnergy()+","+sdp);
		
        
        
		}
		}
        System.out.println("that's all folks");
	}
	
	
	

}
