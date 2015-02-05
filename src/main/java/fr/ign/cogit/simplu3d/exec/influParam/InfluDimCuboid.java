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
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.convert.GenerateSolidFromCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.predicate.UXL3Predicate;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

/**
 * Classe pour étudier l'influence des variations des dimensions des boîtes
 * 
 * @author MBrasebin
 *
 */
public class InfluDimCuboid {

	// [building_footprint_rectangle_cli_main
	public static void main(String[] args) throws Exception {

		String folderName = "./src/main/resources/scenario/";

		String fileName = "building_parameters_project_expthese_1_maison.xml";

		Parameters p = Parameters.unmarshall(new File(folderName + fileName));

		int nbValMin = 4;
		int nbValMax = 4;

		double[] valsMinDimBox = new double[nbValMin];
		double[] valsMaxDimBox = new double[nbValMax];

		valsMinDimBox[0] = 1;
		valsMinDimBox[1] = 5;
		valsMinDimBox[2] = 10;
		valsMinDimBox[3] = 20;

		valsMaxDimBox[0] = 30;
		valsMaxDimBox[1] = 50;
		valsMaxDimBox[2] = 75;
		valsMaxDimBox[3] = 100;

		int nbIt = 10;

		int count = 0;

		BufferedWriter bf = createBufferWriter(p.get("result")
				+ "influDimCuboid.csv");
		bf.write("DimCuboidMin, DimCuboidMax, Iteration, Energy,Box");
		bf.newLine();
		bf.flush();

		for (int indexMin = 0; indexMin < valsMinDimBox.length; indexMin++) {

			for (int indexMax = 0; indexMax < valsMinDimBox.length; indexMax++) {

				// writer.append(valCoeff[i] + ";");

				for (int j = 10; j < 20; j++) {
					Environnement env = LoaderSHP.load(p.getString("folder"));

					OptimisedBuildingsCuboidFinalDirectRejection ocb = new OptimisedBuildingsCuboidFinalDirectRejection();
					UXL3Predicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new UXL3Predicate<>(
							env.getBpU().get(1));

					// OCLBuildingsCuboidFinal ocb = new
					// OCLBuildingsCuboidFinal();
					ocb.setMinDimBox(valsMinDimBox[indexMin]);
					ocb.setMaxDimBox(valsMaxDimBox[indexMax]);

					double timeMs = System.currentTimeMillis();

					GraphConfiguration<Cuboid> cc = ocb.process(env.getBpU()
							.get(1), p, env, 1, pred);

					IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();

					for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {

						IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
						iMS.addAll(GenerateSolidFromCuboid.generate(
								v.getValue()).getFacesList());

						IFeature feat = new DefaultFeature(iMS);

						AttributeManager.addAttribute(feat, "Longueur", Math
								.max(v.getValue().length, v.getValue().width),
								"Double");
						AttributeManager.addAttribute(feat, "Largeur", Math
								.min(v.getValue().length, v.getValue().width),
								"Double");
						AttributeManager.addAttribute(feat, "Hauteur",
								v.getValue().height, "Double");
						AttributeManager.addAttribute(feat, "Rotation",
								v.getValue().orientation, "Double");

						iFeatC.add(feat);

					}

					ShapefileWriter.write(iFeatC, p.get("result").toString()
							+ "shp_mindim" + valsMinDimBox[indexMin]
							+ "_maxDim_ " + valsMaxDimBox[indexMax] + "_ene"
							+ cc.getEnergy() + ".shp");

					System.out.println("mindim" + valsMinDimBox[indexMin]
							+ "_maxDim_ " + valsMaxDimBox[indexMax] + "_ene"
							+ cc.getEnergy() + "," + ocb.getCount() + ","
							+ (System.currentTimeMillis() - timeMs) + ","
							+ cc.getEnergy());

					count++;

					System.out.println("État itération : " + count + "  / "
							+ (indexMin * indexMax * nbIt));

					bf.write(valsMinDimBox[indexMin] + ","
							+ valsMaxDimBox[indexMax] + "," + j + ","
							+ cc.getEnergy() + "," + cc.size());
					bf.newLine();
					bf.flush();

				}

			}
		}

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

}
