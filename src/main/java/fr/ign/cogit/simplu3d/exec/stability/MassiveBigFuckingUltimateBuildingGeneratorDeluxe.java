package fr.ign.cogit.simplu3d.exec.stability;

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
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.convert.GenerateSolidFromCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.predicate.UXL3Predicate;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;
import fr.ign.rjmcmc.configuration.Configuration;

public class MassiveBigFuckingUltimateBuildingGeneratorDeluxe {
	
	

	/**
	 * @param args
	 */

	// [building_footprint_rectangle_cli_main
	public static void main(String[] args) throws Exception {

		String folderName = "./src/main/resources/scenario/";

		String fileName = "building_parameters_project_expthese_1_maison.xml";

		Parameters p = initialize_parameters(folderName + fileName);

		int count = 0;


		int nbIt = 100;
		int nbIni = 100;

		BufferedWriter bf = createBufferWriter(p.get("result")
				+ "inflboucle.csv");
		bf.write("EnergyCreation,Iteration,Energy,Box");
		bf.newLine();
		bf.flush();

		

		for (int j = nbIni; j < nbIni + nbIt; j++) {



				// writer.append(valCoeff[i] + ";");

				Environnement env = LoaderSHP.load(p.getString("folder"));

				OptimisedBuildingsCuboidFinalDirectRejection ocb = new OptimisedBuildingsCuboidFinalDirectRejection();
				UXL3Predicate<Cuboid> pred = new UXL3Predicate<>(env.getBpU()
						.get(1));

				double timeMs = System.currentTimeMillis();

				Configuration<Cuboid> cc = ocb.process(env.getBpU().get(1), p,
						env, 1, pred);

				IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();

				for (GraphConfiguration<Cuboid>.GraphVertex v : ((GraphConfiguration<Cuboid>) cc)
						.getGraph().vertexSet()) {

					IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
					iMS.addAll(GenerateSolidFromCuboid.generate(v.getValue())
							.getFacesList());

					IFeature feat = new DefaultFeature(iMS);

					AttributeManager.addAttribute(feat, "Longueur",
							Math.max(v.getValue().length, v.getValue().width),
							"Double");
					AttributeManager.addAttribute(feat, "Largeur",
							Math.min(v.getValue().length, v.getValue().width),
							"Double");
					AttributeManager.addAttribute(feat, "Hauteur",
							v.getValue().height, "Double");
					AttributeManager.addAttribute(feat, "Rotation",
							v.getValue().orientation, "Double");

					iFeatC.add(feat);

				}

				ShapefileWriter.write(iFeatC,
						p.get("result").toString() + "shp_" + j + "_ene" + cc.getEnergy() + ".shp");

				bf.write( j + "," + cc.getEnergy() + ","
						+ cc.size());
				bf.newLine();
				bf.flush();

				count++;

				System.out.println(+ ocb.getCount() + ","
						+ (System.currentTimeMillis() - timeMs) + ","
						+ cc.getEnergy() + "État itération : " + count + "  / "
						+ ( nbIt));

			

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
