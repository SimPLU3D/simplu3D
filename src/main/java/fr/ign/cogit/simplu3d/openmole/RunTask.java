package fr.ign.cogit.simplu3d.openmole;

import java.io.File;
import java.io.FileInputStream;

import fr.ign.cogit.simplu3d.exec.experimentation.SamplePredicate;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.io.save.SaveGeneratedObjects;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained.OptimisedBuildingsCuboidDirectRejectionNoVisitor;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;

public class RunTask {

	public static void main(String[] args) throws Exception {

		String folder = "E:/mbrasebin/Donnees/Strasbourg/GTRU/ProjectT3/";
		String folderOut = "E:/temp2/";

		String parameterFile = "E:/mbrasebin/GeOxygene/simplu3d/simplu3D/src/main/resources/scenario/building_parameters_project_expthese_3.xml";

		int idBPU = 255;
		double distReculVoirie = 3;
		double distReculFond = 1;
		double distReculLat = 2;
		double maximalCES = 0.3;
		double hIniRoad = 5;
		double slopeRoad = 0.5;
		double hauteurMax = 17;
		int run = 0;

		run(folder, folderOut, parameterFile, idBPU, distReculVoirie,
				distReculFond, distReculLat, maximalCES, hIniRoad, slopeRoad,
				hauteurMax, run);
	}

	public static void run(String folder, String folderOut,
			String parameterFile, int idBPU, double distReculVoirie,
			double distReculFond, double distReculLat, double maximalCES,
			double hIniRoad, double slopeRoad, double hauteurMax, int run)
			throws Exception {

		// On charge l'environnement
		Environnement env = LoaderSHP.load(folder, new FileInputStream(folder
				+ LoaderSHP.NOM_FICHIER_TERRAIN));

		// On charge le fichier de parametre
		Parameters p = Parameters.unmarshall(new File(parameterFile));

		// On récupère la parcelle sur laquelle on effectue la simulation
		BasicPropertyUnit bPU = null;
		for (BasicPropertyUnit bPUTemp : env.getBpU()) {

			if (bPUTemp.getId() == idBPU) {
				bPU = bPUTemp;
				break;
			}

		}

		if (bPU == null) {
			System.out.println("C'est null" + idBPU);
			return;
		}

		// Chargement de l'optimiseur
		OptimisedBuildingsCuboidDirectRejectionNoVisitor oCB = new OptimisedBuildingsCuboidDirectRejectionNoVisitor();

		// Création du Sampler (qui va générer les propositions de solutions)
		// OptimisedBuildingsCuboidFinalDirectRejection oCB = new
		// OptimisedBuildingsCuboidFinalDirectRejection();

		// Chargement des règles à appliquer
		SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(
				bPU, distReculVoirie, distReculFond, distReculLat, maximalCES,
				hIniRoad, slopeRoad);

		p.set("maxheight", hauteurMax);

		// Exécution de l'optimisation
		GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);

		// Identifiant de la parcelle

		double energy = cc.getEnergy();

		String pathShapeFile = folderOut + "_" + idBPU + "_drv_"
				+ distReculVoirie + "_drf_" + distReculFond + "_drl_"
				+ distReculLat + "_ces_" + maximalCES + "_hini_" + hIniRoad
				+ "_sro_" + slopeRoad + "_hmax_" + hauteurMax + "_run_" + run
				+ "_en_" + energy + ".shp";

		SaveGeneratedObjects.saveShapefile(pathShapeFile, cc, idBPU, run);

	}

}
