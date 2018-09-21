package fr.ign.cogit.simplu3d.exec;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
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
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
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

	/**
	 * @param args
	 */

	// [buildin-g_footprint_rectangle_cli_main
	public static void main(String[] args) throws Exception {
	 
		// Loading of configuration file that contains sampling space
		// information and simulated annealing configuration
		String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
		String fileName = "building_parameters_project_expthese_3.xml";
		SimpluParameters p = new SimpluParametersJSON(new File(folderName + fileName));

		// Load default environment (data are in resource directory)
		Environnement env = LoaderSHP.loadNoDTM(new File(
				DemoEnvironmentProvider.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/data/").getPath()));

		// Select a parcel on which generation is proceeded
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
		SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(
				bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati, maximalCES);

		// Run of the optimisation on a parcel with the predicate
		GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);
		//Writting the output
		SaveGeneratedObjects.saveShapefile( p.get("result").toString() + "out.shp", cc, bPU.getId(), 0);
		
	}

}
