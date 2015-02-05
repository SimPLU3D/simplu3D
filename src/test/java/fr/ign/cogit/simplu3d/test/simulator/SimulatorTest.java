package fr.ign.cogit.simplu3d.test.simulator;

import java.io.File;

import org.junit.Test;

import fr.ign.cogit.simplu3d.exe.LoadDefaultEnvironment;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.predicate.UXL3Predicate;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;

public class SimulatorTest {

  @Test
  public void testImport() throws Exception {

    String folderName = SimulatorTest.class.getClassLoader()
        .getResource("scenario/").getPath();

    String fileName = "scenariotest.xml";

    Parameters p = Parameters.unmarshall(new File(folderName + fileName));

    Environnement env = LoadDefaultEnvironment.getENVDEF();

    BasicPropertyUnit bPU = env.getBpU().get(1);

    OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

    UXL3Predicate<Cuboid,GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new UXL3Predicate<>(env.getBpU().get(1));

    oCB.process(bPU, p, env, 1, pred);
  }

}
