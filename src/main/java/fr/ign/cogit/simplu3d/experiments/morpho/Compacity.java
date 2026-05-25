package fr.ign.cogit.simplu3d.experiments.morpho;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.io.feature.AttribNames;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.generic.predicate.SamplePredicate;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.cogit.simplu3d.util.SimpluParametersJSON;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import org.junit.Assert;
import org.locationtech.jts.geom.MultiPolygon;

/**
 * computation of generated buildings compacity.
*/
public class Compacity {
  public static void main(String[] args) throws Exception {
    String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
    String fileName = "building_parameters_project_expthese_3.json";
    SimpluParameters p = new SimpluParametersJSON(new File(folderName + fileName));
    System.out.println(p);
    // Load default environment (data are in resource directory)
    AttribNames.setATT_BDP_NUMERO("ID_Parcell");
    Environnement env = LoaderSHP.loadNoDTM(
            DemoEnvironmentProvider.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/data/")
    );
    // list of parcels
    env.getBpU().findAll().forEach(bpu->System.out.println(bpu.getId()));
    BasicPropertyUnit bPU = env.getBpU().findAll().stream().filter(bpu->bpu.getCadastralParcels().stream().anyMatch(parcel->parcel.getCode().equals("8"))).findFirst().get();

    System.out.println(bPU.getGeometry());
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
    SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati, maximalCES);
    // Run of the optimisation on a parcel with the predicate
    GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, "1", pred);
    System.out.println("optim terminée");
    System.out.println("nb cuboid = " + cc.size());
    List<AbstractSimpleBuilding> featCollec = new ArrayList<>();
    for (GraphVertex<? extends AbstractSimpleBuilding> v : cc.getGraph().vertexSet()) {
      AbstractSimpleBuilding building = v.getValue();
      featCollec.add(building);
    }
    AbstractSimpleBuilding firstBuilding = featCollec.getFirst();
    // create building element (roof, walls, etc.)
    firstBuilding.createElements();
    System.out.println("Wall surface: " + firstBuilding.getWallSurfaces().size());
    System.out.println("Volume: " + firstBuilding.getVolume());
    MultiPolygon lod2 = firstBuilding.getLod2MultiSurface();
    System.out.println("nb surfaces: " + lod2.getNumGeometries());
    System.out.println("that's all folks");
    Assert.assertEquals(3418.02, firstBuilding.getVolume(), 0.01);
    System.out.println(firstBuilding.getGeometry());
    firstBuilding.getWallSurfaces().forEach(wallSurface -> System.out.println(wallSurface.getGeometry()));
  }
}
