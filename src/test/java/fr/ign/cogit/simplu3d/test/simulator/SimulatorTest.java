package fr.ign.cogit.simplu3d.test.simulator;

import java.io.File;

import org.junit.Test;

import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.predicate.UXL3Predicate;
import fr.ign.cogit.simplu3d.test.io.load.application.LoaderSimpluSHPTest;
import fr.ign.parameters.Parameters;

public class SimulatorTest {

  @Test
  public void testImport() throws Exception {

    String folderName = SimulatorTest.class.getClassLoader()
        .getResource("scenario/").getPath();

    String fileName = "scenariotest.xml";

    Parameters p = Parameters.unmarshall(new File(folderName + fileName));

    Environnement env = LoaderSimpluSHPTest.getENVTest();

    BasicPropertyUnit bPU = env.getBpU().get(1);

    OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

    UXL3Predicate<Cuboid> pred = new UXL3Predicate<>(env.getBpU().get(1));

    oCB.process(bPU, p, env, 1, pred);
  }

}
