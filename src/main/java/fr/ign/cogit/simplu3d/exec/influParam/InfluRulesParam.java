package fr.ign.cogit.simplu3d.exec.influParam;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.convert.GenerateSolidFromCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.predicate.UB16PredicateWithOtherBuildings;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;
import fr.ign.rjmcmc.configuration.Configuration;

public class InfluRulesParam {

  public static void main(String[] args) throws Exception {

    double valMinSlope = 2;

    double valMaxSlope = 2.5;
    double pasSlope = 0.5;

    double valMinHini = 0;
    double valMaxHini = 2;
    double pasHini = 2;

    double nbIteration = 10;

    String folderName = "./src/main/resources/scenario/";

    String fileName = "building_parameters_project_expthese_3.xml";

    Parameters p = initialize_parameters(folderName + fileName);

    BufferedWriter bf = createBufferWriter(p.get("result")
        + "influenceNumberOfBoxesInGroup.csv");
    bf.write("ValSlope; ValHini; Iteration; Energy");
    bf.newLine();
    bf.flush();

    Environnement env = LoaderSHP.load(p.getString("folder"));

    for (int iteration = 0; iteration < nbIteration; iteration++) {

      for (double currentValSlope = valMinSlope; currentValSlope < valMaxSlope; currentValSlope = currentValSlope
          + pasSlope) {
        for (double currentValHini = valMinHini; currentValHini < valMaxHini; currentValHini = currentValHini
            + pasHini) {

          IFeatureCollection<IFeature> collectionToSave = new FT_FeatureCollection<>();

          double energyTot = 0;

          System.out.println("Slope : " + currentValSlope + "   HIni   "
              + currentValHini);

          for (BasicPropertyUnit bPU : env.getBpU()) {

            OptimisedBuildingsCuboidFinalDirectRejection ocb = new OptimisedBuildingsCuboidFinalDirectRejection();

            // UB16PredicateWithParameters<Cuboid2> pred = new
            // UB16PredicateWithParameters<Cuboid2>(
            // bPU, currentValHini, currentValSlope);

            UB16PredicateWithOtherBuildings<Cuboid2> pred = new UB16PredicateWithOtherBuildings<Cuboid2>(
                bPU, currentValHini, currentValSlope);

            Configuration<Cuboid2> cc = ocb.process(bPU, p, env, 1, pred);

            energyTot = energyTot + cc.getEnergy();

            for (GraphConfiguration<Cuboid2>.GraphVertex v : ((GraphConfiguration<Cuboid2>) cc)
                .getGraph().vertexSet()) {

              IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
              iMS.addAll(GenerateSolidFromCuboid.generate(v.getValue())
                  .getFacesList());

              IFeature feat = new DefaultFeature(iMS);

              AttributeManager.addAttribute(feat, "Longueur",
                  Math.max(v.getValue().length, v.getValue().width), "Double");
              AttributeManager.addAttribute(feat, "Largeur",
                  Math.min(v.getValue().length, v.getValue().width), "Double");
              AttributeManager.addAttribute(feat, "Hauteur",
                  v.getValue().height, "Double");
              AttributeManager.addAttribute(feat, "Rotation",
                  v.getValue().orientation, "Double");

              AttributeManager.addAttribute(feat, "ID Bpu", bPU.getId(),
                  "Integer");

              collectionToSave.add(feat);

            }

          }

          ShapefileWriter.write(collectionToSave, p.get("result").toString()
              + "shp_" + currentValHini + "_ " + currentValSlope + "_"
              + iteration + "_ene" + energyTot + ".shp");

          bf.write(currentValSlope + ";" + currentValHini + ";" + iteration
              + ";" + energyTot);
          bf.newLine();
          bf.flush();

        }

      }

      // Nouvelle itération

    }

    bf.flush();
    bf.close();

  }

  private static BufferedWriter createBufferWriter(String fileName) {
    BufferedWriter writer = null;
    try {

      File f = new File(fileName);

      if (!f.exists()) {
        f.createNewFile();
      }

      Path path = Paths.get(fileName);

      writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
          StandardOpenOption.APPEND);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return writer;
  }

  private static Parameters initialize_parameters(String name) throws Exception {
    return Parameters.unmarshall(name);
  }

}
