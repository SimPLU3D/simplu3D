package fr.ign.cogit.simplu3d.enau;

import java.io.File;

import fr.ign.cogit.simplu3d.enau.geometry.DeformedCuboid;
import fr.ign.cogit.simplu3d.enau.optimizer.DeformedOptimizer;
import fr.ign.cogit.simplu3d.importer.applicationClasses.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.io.save.SaveGeneratedObjects;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.util.AssignZ;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;

public class ExecDeformedCuboid {

	public static void main(String[] args) throws Exception {

		// Chargement du fichier de configuration
		String folderName = "C:/Users/mbrasebin/Desktop/Alia/";

		String fileName = "building_parameters_project_expthese_3.xml";

		CadastralParcelLoader.ATT_ID_PARC = "id_parcell";
		AssignZ.DEFAULT_Z = 0;

		// Chargement de l'environnement
		Environnement env = LoaderSHP.loadNoDTM(folderName);
		Parameters p = Parameters.unmarshall(new File(folderName + fileName));


		// On trouve la parcelle qui a l'identifiant numéro 4
		BasicPropertyUnit bPU = null;
		for (BasicPropertyUnit bPUTemp : env.getBpU()) {
			// Identiant numéro 4
			if (bPUTemp.getId() == 3) {
				bPU = bPUTemp;
				break;
			}

		}

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
		double maximalCES = 0.5;

		// Création du Sampler (qui va générer les propositions de solutions)
		DeformedOptimizer oCB = new DeformedOptimizer();
 
		PredicateTunis<DeformedCuboid, GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> pred = new PredicateTunis<>(
				distReculVoirie, slope, hIni, hMax, distReculLimi,
				slopeProspectLimit, maximalCES, bPU);

		// Lancement de l'optimisation avec unité foncière, paramètres,
		// environnement, id et prédicat
		GraphConfiguration<DeformedCuboid> cc = oCB.process(bPU, p, env, 1,
				pred, distReculVoirie, slope, hIni, hMax, distReculLimi,
				slopeProspectLimit, maximalCES,DeformedOptimizer.DROITE);

		SaveGeneratedObjects.saveShapefile(folderName + "out.shp", cc,
				bPU.getId(), 0);

		System.out.println("That's all folks");

	}

}
