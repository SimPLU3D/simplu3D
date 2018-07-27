package fr.ign.cogit.simplu3d.experiments.openmole.paris.simulation;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.util.AssignZ;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

public class ParisSimulator {

	public static void main(String[] args) throws Exception {

		// Folder
		String folderName = "/home/mbrasebin/Documents/Donnees/Exp/Eugene_Million/";

		String shapefileOut = "/tmp/tmp/simulation.shp";

		// Scenario name
		String scenarioName = "scenario.xml";
		Parameters p = Parameters.unmarshall(new File(folderName + scenarioName));

		// Z default
		AssignZ.DEFAULT_Z = 36;

		// Load default environment (data are in resource directory)
		Environnement env = LoaderSHP.loadNoDTM(new File(folderName));

		
		// Writting the output
		IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();
		for (BasicPropertyUnit bPU : env.getBpU()) {
			
			

			if (!bPU.getCadastralParcels().get(0).hasToBeSimulated()) {
				continue;
			}

			
			/*
			//Limits the number of simulation by IDPAR attribute
			if(! bPU.getCadastralParcels().get(0).getCode().equalsIgnoreCase("75056000BN0008")) {
				continue;
			}
			*/
		
			
			
			
			// Instantiation of the sampler
			OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

			
			
			ParisPredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new ParisPredicate<>(
					bPU);

			// Run of the optimisation on a parcel with the predicate
			GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);

			// For all generated boxes
			for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {

				// Output feature with generated geometry
				IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());

				// We write some attributes
				AttributeManager.addAttribute(feat, "Longueur", Math.max(v.getValue().length, v.getValue().width),
						"Double");
				AttributeManager.addAttribute(feat, "Largeur", Math.min(v.getValue().length, v.getValue().width),
						"Double");
				AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height, "Double");
				AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation, "Double");

				iFeatC.add(feat);

			}
		}

		System.out.println(iFeatC.size());

		// A shapefile is written as output
		// WARNING : 'out' parameter from configuration file have to be change
		ShapefileWriter.write(iFeatC, shapefileOut);
		System.out.println("That's all folks");

	}

}
