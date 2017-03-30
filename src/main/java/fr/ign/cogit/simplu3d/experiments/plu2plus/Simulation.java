package fr.ign.cogit.simplu3d.experiments.plu2plus;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.checker.model.CompositeChecker;
import fr.ign.cogit.simplu3d.experiments.plu2plus.checker.CheckerGenerator;
import fr.ign.cogit.simplu3d.experiments.plu2plus.context.SimulationcheckerContext;
import fr.ign.cogit.simplu3d.experiments.plu2plus.predicate.CheckerPredicate;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

public class Simulation {

	public static void main(String[] args) throws Exception {
		String folderName = "/home/mickael/data/mbrasebin/donnees/PLU2PLUS/Projet/";
		String paramFile = folderName + "parameters_meylan.xml";

		String pathEmprise = folderName + "temp_data/emprise_proj.shp";

		IFeature featForbiddenZone = ShapefileReader.read(pathEmprise).get(0);

		Parameters p = Parameters.unmarshall(new File(paramFile));

		Environnement env = LoaderSHP.loadNoDTM(new File(folderName));

		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

		for (BasicPropertyUnit bPU : env.getBpU()) {
			
	

			if (featForbiddenZone.getGeom().intersects(bPU.getPol2D())) {

				featC.addAll(simulateBPU(env, p, bPU));
			}

		}

		ShapefileWriter.write(featC, folderName + "out/result.shp");

	}

	public static IFeatureCollection<IFeature> simulateBPU(Environnement env, Parameters p, BasicPropertyUnit bPU) {

		// Instantiation of the sampler
		OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

		SimulationcheckerContext context = new SimulationcheckerContext();
		context.setStopOnFailure(true);

		// Initialisation of the Checker
		CompositeChecker checker = CheckerGenerator.generate(bPU);

		CheckerPredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new CheckerPredicate<>(
				bPU, checker, context);
		// Run of the optimisation on a parcel with the predicate
		GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);

		// Witting the output
		IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();
		// For all generated boxes
		for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {

			// Output feature with generated geometry
			IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());

			// We write some attributes
			AttributeManager.addAttribute(feat, "Longueur", Math.max(v.getValue().length, v.getValue().width),
					"Double");
			AttributeManager.addAttribute(feat, "Largeur", Math.min(v.getValue().length, v.getValue().width), "Double");
			AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height, "Double");
			AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation, "Double");
			AttributeManager.addAttribute(feat, "IdParcel", bPU.getCadastralParcels().get(0).getCode(), "String");

			iFeatC.add(feat);

		}

		return iFeatC;

	}

}
