package fr.ign.cogit.simplu3d.experiments.enau;

import java.io.File;

import fr.ign.cogit.simplu3d.experiments.enau.optimizer.OBCFDRConstraintOptimisation;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.io.shapefile.SaveGeneratedObjects;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.util.AssignZ;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.cogit.simplu3d.util.SimpluParametersJSON;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;

public class Exec {

	public static void main(String[] args) throws Exception {

		// Chargement du fichier de configuration
		String folderName = "C:/Users/mbrasebin/Desktop/Alia/";

		String fileName = "building_parameters_project_expthese_3.xml";

		//CadastralParcelLoader.ATT_ID_PARC = "id_parcell";
		AssignZ.DEFAULT_Z = 0;

		SimpluParameters p = new SimpluParametersJSON(new File(folderName + fileName));

		// Chargement de l'environnement
		Environnement env = LoaderSHP.loadNoDTM(new File(folderName));

		// On trouve la parcelle qui a l'identifiant numéro 4
		BasicPropertyUnit bPU = null;
		for (BasicPropertyUnit bPUTemp : env.getBpU()) {
			// Identiant numéro 4
			if (bPUTemp.getId() == 3) {
				bPU = bPUTemp;
				break;
			}

		}

		// Création du Sampler (qui va générer les propositions de solutions)
		OBCFDRConstraintOptimisation oCB = new OBCFDRConstraintOptimisation();

		// Valeurs de règles à saisir
		// C1
		double distReculVoirie = 2;

		// C2
		double slope = 1;
		double hIni = 45;

		// C3
		double hMax = 17;
		p.set("maxheight", hMax);

		// C4
		double distReculLimi = 5.4;
		double slopeProspectLimit = 2;

		// C7
		double maximalCES = 0.3; // 0.5;

		PredicateTunis<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new PredicateTunis<>(
				distReculVoirie, slope, hIni, hMax, distReculLimi,
				slopeProspectLimit, maximalCES, bPU);

		// Lancement de l'optimisation avec unité foncière, paramètres,
		// environnement, id et prédicat
		GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred,
				distReculVoirie, slope, hIni, hMax, distReculLimi,
				slopeProspectLimit, maximalCES);

		SaveGeneratedObjects.saveShapefile(folderName + "out.shp", cc,
				bPU.getId(), 0);

		System.out.println("That's all folks");

	}

}
