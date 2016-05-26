package fr.ign.cogit.simplu3d.exec.influParam;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.experiments.thesis.predicate.UXL3PredicateGroup;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
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
 *
 * Classe pour étudier l'influence des variations du nombre de boîtes
 * 
 *
 */
public class InfluNumberOfBoxesInGroup {
	// [building_footprint_rectangle_cli_main
	public static void main(String[] args) throws Exception {

		String folderName = "./src/main/resources/scenario/";

		String fileName = "building_parameters_project_expthese_1.xml";

		Parameters p = Parameters.unmarshall(new File(folderName + fileName));

		int count = 0;

		List<Integer> ld = new ArrayList<>();

		// ld.add(2);
		// ld.add(3);
		// ld.add(4);
		// ld.add(5);

		ld.add(6);
		ld.add(7);
		ld.add(8);
		ld.add(9);

		// ld.add(10);
		// ld.add(20);

		int nbIt = 1;

		BufferedWriter bf = createBufferWriter(p.get("result")
				+ "influenceNumberOfBoxesInGroup.csv");
		bf.write("NombreMaxParGroupe,Iteration,Energy,Box");
		bf.newLine();
		bf.flush();

		for (int i = 0; i < ld.size(); i++) {

			// writer.append(valCoeff[i] + ";");

			for (int j = 0; j < nbIt; j++) {

				Environnement env = LoaderSHP.load(new File(p.getString("folder")));

				OptimisedBuildingsCuboidFinalDirectRejection ocb = new OptimisedBuildingsCuboidFinalDirectRejection();

				UXL3PredicateGroup<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new UXL3PredicateGroup<>(
						env.getBpU().get(1), (int) ld.get(i));

				ocb.process(env.getBpU().get(1), p, env, 1, pred);

				double timeMs = System.currentTimeMillis();

				GraphConfiguration<Cuboid> cc = ocb.process(
						env.getBpU().get(1), p, env, 1, pred);

				IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();

				for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {



					IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());

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
						p.get("result").toString() + "shp_" + ld.get(i) + "_ "
								+ j + "_ene" + cc.getEnergy() + ".shp");

				bf.write(ld.get(i) + "," + j + "," + cc.getEnergy() + ","
						+ cc.size());
				bf.newLine();
				bf.flush();

				count++;

				System.out.println(ld.get(i) + "," + ocb.getCount() + ","
						+ (System.currentTimeMillis() - timeMs) + ","
						+ cc.getEnergy() + "État itération : " + count + "  / "
						+ (ld.size() * nbIt));

			}

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

			e.printStackTrace();
		}

		return writer;
	}

}
