package fr.ign.cogit.simplu3d.experiments.mupcity.util;

import java.io.File;
import java.util.HashMap;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.experiments.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.UrbaZone;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.generic.predicate.SamplePredicate;
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
public class Simulator {

	/**
	 * @param args
	 */

	// [building_footprint_rectangle_cli_main
	public static void main(String[] args) throws Exception {

		// Loading of configuration file that contains sampling space
		// information and simulated annealing configuration
		String folderName = Simulator.class.getClassLoader().getResource("scenario/").getPath();
		System.out.println(folderName);
		String fileName = "building_parameters_fontain.xml";
		Parameters p = Parameters.unmarshall(new File(folderName + fileName));

		// Load default environment (data are in resource directory)
		Environnement env = LoaderSHP.loadNoDTM(new File("/home/mcolomb/informatique/workspace/simplu3d/simplu3D/src/main/resources/fr/ign/cogit/simplu3d/fontain"));

		System.out.println("Nombre de bâtiments : " + env.getBuildings());
		
		// Select a parcel on which generation is proceeded

		
		// definition de différents profils de réglementation
		
		HashMap <String,SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> catalog = new HashMap<String,SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>>();
		for (int i = 1; i < env.getBpU().size(); i++) {
			BasicPropertyUnit bPU = env.getBpU().get(i);

			// Instantiation of the sampler
			OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();
			String libelle = new String();
			String typez = new String();

			for (UrbaZone zone : env.getUrbaZones()) {
				if (zone.getGeom().contains(bPU.getGeom())) {
					libelle = zone.getLibelle();
					typez = zone.getTypeZone();
					System.out.println("got it " + typez);
				}
			}
			// Rules parameters
			double distReculVoirie = 0;
			double distReculFond = 0;
			double distReculLat = 0;
			double distanceInterBati = 0;
			double maximalCES = 0;
			double maximalhauteur = 0;
			
			
			if (typez.equals("Ua")) {
				distReculVoirie = 3;
				// Distance to bottom of the parcel
				distReculFond = 1.5;
				// Distance to lateral parcel limits
				distReculLat = 1.5;
				// Distance between two buildings of a parcel
				distanceInterBati = 3;
				// Maximal ratio built area
				maximalCES = 1;
				maximalhauteur=6;
			}
			else if (typez.equals("Ub")) {
				distReculVoirie = 4;
				// Distance to bottom of the parcel
				distReculFond = 1.5;
				// Distance to lateral parcel limits
				distReculLat = 1.5;
				// Distance between two buildings of a parcel
				distanceInterBati = 3;
				// Maximal ratio built area
				maximalCES = 0.5;
				maximalhauteur=6;
			}

			// Instantiation of the rule checker
			SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati, maximalCES);

			
			// Run of the optimisation on a parcel with the predicate
			GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);

			// Witting the output
			IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();
			// For all generated boxes
			for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {

				// Output feature with generated geometry
				IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());

				// We write some attributes
				AttributeManager.addAttribute(feat, "Longueur", Math.max(v.getValue().length, v.getValue().width), "Double");
				AttributeManager.addAttribute(feat, "Largeur", Math.min(v.getValue().length, v.getValue().width), "Double");
				AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height, "Double");
				AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation, "Double");
				iFeatC.add(feat);
			}

			// A shapefile is written as output
			// WARNING : 'out' parameter from configuration file have to be change
			
			ShapefileWriter.write(iFeatC, p.get("result").toString() + "out-parcel_"+i+".shp");

			System.out.println("That's all folks");
		}
	}

}
