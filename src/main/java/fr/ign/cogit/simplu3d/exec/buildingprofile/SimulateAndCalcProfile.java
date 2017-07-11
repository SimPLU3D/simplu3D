package fr.ign.cogit.simplu3d.exec.buildingprofile;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.analysis.streetprofile.Profile;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.Road;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.generic.predicate.SamplePredicate;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

public class SimulateAndCalcProfile {

	public static void main(String[] args) throws Exception {

		// Loading of configuration file that contains sampling space
		// information and simulated annealing configuration
		String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
		String fileName = "building_parameters_project_expthese_3.xml";
		Parameters p = Parameters.unmarshall(new File(folderName + fileName));

		// Load default environment (data are in resource directory)
		Environnement env = LoaderSHP.loadNoDTM(new File(
				DemoEnvironmentProvider.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/data/").getPath()));

		// Rules parameters
		// Distance to road
		double distReculVoirie = 0.0;
		// Distance to bottom of the parcel
		double distReculFond = 2;
		// Distance to lateral parcel limits
		double distReculLat = 4;
		// Distance between two buildings of a parcel
		double distanceInterBati = 0;
		// Maximal ratio built area
		double maximalCES = 2;

		// Witting the output
		IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();

		for (int i = 0; i < env.getBpU().size(); i++) {

			// Select a parcel on which generation is proceeded
			BasicPropertyUnit bPU = env.getBpU().get(i);

			// Instantiation of the sampler
			OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

			// Instantiation of the rule checker
			SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(
					bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati, maximalCES);

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

		IFeatureCollection<IFeature> featCollOut = calculateProfile(env, iFeatC).getPproj();
		System.out.println("NB Points : " + featCollOut.size());
	}

	public static Profile calculateProfile(Environnement environnement,
			IFeatureCollection<IFeature> featCuboid) {
		// Mandatory due to precision trunk in Geoxygene core
		DirectPosition.PRECISION = 10;

		

		IFeatureCollection<IFeature> transformeRoad = new FT_FeatureCollection<>();

		for (Road feat : environnement.getRoads()) {
			IFeature featTemp = new DefaultFeature(Extrusion2DObject.convertFromGeometry(feat.getAxis(), 0, 0));
			transformeRoad.add(featTemp);
		}

		Profile profile = new Profile(transformeRoad,
				// Set of contigus roads from which the profil is calculated
				featCuboid,
				// 3D buildings used
				environnement.getCadastralParcels(),
				// Parcel as input (only buildings in the first parcel are used)
				1, // minimal height of the profile (it may be calculated
					// automatically)
				20, // maximal height of the profile (it may be calculated
					// automatically)
				1, // step along curvilinear abscissa
				1 // z step for profil calculation
		);

		// Data loading, if parcels have no z they are translated to the minimal
		// z of the scene
		profile.loadData();

		// This lines allows the visualisation of the scene
		//profile.display();

		// Calculation of the profilLe
		// The results may be acccessible by getPproj method
		// They are represented by 2D points with X = curvilinear abscissa et Y
		// = height
		// (the value is positive or negative according to the side of the orad)
		// height is measured according to an origin based on minimal height but
		// my be
		// parametrized by profile.setYProjectionShifting
		profile.process();

		// Update in the visualisation if available
		//profile.updateDisplay();

		// Point export
		//profile.exportPoints(folderOut + "pointout.shp");

		return profile;

	}

}
