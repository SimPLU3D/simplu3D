package fr.ign.cogit.simplu3d.test.simulator;

import java.io.File;

import org.junit.Test;

import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.generic.predicate.SamplePredicate;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;
/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/ 
public class SimulatorTest {

  @Test
  public void testImport() throws Exception {

    String folderName = SimulatorTest.class.getClassLoader()
        .getResource("scenario/").getPath();

    String fileName = "scenariotest.xml";

    Parameters p = Parameters.unmarshall(new File(folderName + fileName));

    Environnement env = DemoEnvironmentProvider.getDefaultEnvironment();

    if (env.getBpU().isEmpty()) return;
    BasicPropertyUnit bPU = env.getBpU().get(1);

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


    oCB.process(bPU, p, env, 1, pred);
  }

}
