package fr.ign.cogit.simplu3d.exec;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Stream;

import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.io.shapefile.SaveGeneratedObjects;
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
 * <p>
 * see LICENSE.TXT
 * <p>
 * see <a href="http://www.cecill.info/">...</a>
 * <p>
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
public class BlockSimulator {
	public record SimulationResult (GraphConfiguration<Cuboid> configuration, int id) {}
	public static Stream<SimulationResult> simulate(String configurationFileName, String dataFolderName) throws Exception {
		// Load default environment (data are in resource directory)
		//List<GraphConfiguration<Cuboid>> results = new ArrayList<>();
		SimpluParameters p = new SimpluParametersJSON(new File(configurationFileName));
		Environnement env = LoaderSHP.loadNoDTM(new File(dataFolderName));
		return env.getBpU().stream().map(bPU -> {
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

            try {
				// Instantiation of the rule checker
				SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(
                        bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati, maximalCES);
				// Run of the optimisation on a parcel with the predicate
                return Optional.of(new SimulationResult(oCB.process(bPU, p, env, bPU.getId(), pred), bPU.getId()));
            } catch (Exception e) {
				System.out.println("Could not create predicate for " + bPU.getId() + " with " + e);
                return Optional.<SimulationResult>empty();
                //throw new RuntimeException(e);
            }
		}).filter(Optional::isPresent).map(Optional::get);
	}
	public static void main(String[] args) throws Exception {

		// Loading of configuration file that contains sampling space
		// information and simulated annealing configuration
		URL configURL = BasicSimulator.class.getClassLoader().getResource("scenario/");
		URL dataURL = DemoEnvironmentProvider.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/data/");
		if (configURL== null && dataURL == null) {
			System.out.println("Can't go through because one of the directories is empty");
			return;
		}
        assert configURL != null;
        String folderName = configURL.getPath();

		String fileName = "building_parameters_project_expthese_3.json";
		//SimpluParameters p = new SimpluParametersJSON(new File(folderName + fileName));

		// Load default environment (data are in resource directory)
        assert dataURL != null;
        //Environnement env = LoaderSHP.loadNoDTM(new File(dataURL.getPath()));
		Stream<SimulationResult> results = simulate(folderName + fileName, dataURL.getPath());

		results.forEach(result -> {
			// Writting the output
			String pathCourant = "result_" + result.id + ".shp";
			SaveGeneratedObjects.saveShapefile(pathCourant, result.configuration, result.id, 0);

			MergeCuboid recal = new MergeCuboid();

			/* IFeatureCollection<IFeature> fus = */recal.mergeFromShapefile(pathCourant, 0.0);
			// System.out.println("Surface" + recal.getSurface());
			// System.out.println("Energie" + cc.getEnergy());
			// System.out.println("ratio" + cc.getEnergy()/recal.getSurface());

			SDPCalc sdpcalc = new SDPCalc();

			double sdp = sdpcalc.process(pathCourant);

			System.out.println(result.id + "," + result.configuration.size() + "," + recal.getSurface() + "," + result.configuration.getEnergy() + "," + sdp);
		});
		System.out.println("that's all folks");
	}
}
