package fr.ign.cogit.simplu3d.exec.influParam;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.experiments.thesis.predicate.UXL3Predicate;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.cogit.simplu3d.util.SimpluParametersJSON;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;

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
 * Classe pour étudier les variations du coefficient de température
 * 
 *
 */
public class InfluCoeffTemp {

	// [building_footprint_rectangle_cli_main
	public static void main(String[] args) throws Exception {

		String folderName = "./src/main/resources/scenario/";

		String fileName = "building_parameters_project_expthese_1.xml";

		SimpluParameters p = new SimpluParametersJSON(new File(folderName + fileName));

		int nbIt = 1;
		int nbInter = 5;

		double bMin = 0.99699993;
		double bMax = 0.9999999;

		int count = 0;

		List<Double> ld = new ArrayList<>();

		for (int i = 0; i < nbInter; i++) {

			ld.add(i * (bMax - bMin) / nbInter + bMin);

		}

		ld.add(bMax);

		Object[] valCoeff = ld.toArray();

		for (int i = 0; i < valCoeff.length; i++) {

			// writer.append(valCoeff[i] + ";");

			for (int j = 0; j < nbIt; j++) {
				Environnement env = LoaderSHP.load(new File(p.getString("folder")));

				OptimisedBuildingsCuboidFinalDirectRejection ocb = new OptimisedBuildingsCuboidFinalDirectRejection();
				UXL3Predicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new UXL3Predicate<>(
						env.getBpU().get(1));

				// OCLBuildingsCuboidFinal ocb = new OCLBuildingsCuboidFinal();
				ocb.setCoeffDec((double) valCoeff[i]);

				double timeMs = System.currentTimeMillis();

				GraphConfiguration<Cuboid> cc = ocb.process(
						env.getBpU().get(1), p, env, 1, pred);

				IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();

				for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {

			

					IFeature feat = new DefaultFeature(v.getValue()
							.generated3DGeom());

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

				System.out.println(valCoeff[i] + "," + ocb.getCount() + ","
						+ (System.currentTimeMillis() - timeMs) + ","
						+ cc.getEnergy());

				count++;

				System.out.println("État itération : " + count + "  / "
						+ (valCoeff.length * nbIt));

			}

		}

	}

}
