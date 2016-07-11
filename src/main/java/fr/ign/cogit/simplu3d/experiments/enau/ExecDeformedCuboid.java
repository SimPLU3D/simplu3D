package fr.ign.cogit.simplu3d.experiments.enau;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.experiments.enau.geometry.DeformedCuboid;
import fr.ign.cogit.simplu3d.experiments.enau.optimizer.DeformedOptimizer;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.util.AssignZ;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

public class ExecDeformedCuboid {

	public static void main(String[] args) throws Exception {

		// Chargement du fichier de configuration
		String folderName = "C:/Users/mbrasebin/Desktop/Alia/";

		String fileName = "building_parameters_project_expthese_3.xml";

		//CadastralParcelLoader.ATT_ID_PARC = "id_parcell";
		AssignZ.DEFAULT_Z = 0;

		// Chargement de l'environnement
		Environnement env = LoaderSHP.loadNoDTM(new File(folderName));
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
		double maximalCES = 0.3;// 0.5;

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

		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
		
		for (GraphVertex<? extends AbstractSimpleBuilding> v : cc.getGraph().vertexSet()) {
			double longueur = Math.max(v.getValue().length, v.getValue().width);
			double largeur = Math.min(v.getValue().length, v.getValue().width);
			double hauteur = v.getValue().height(0, 0);
			double orientation = v.getValue().orientation;

			IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());
			AttributeManager
					.addAttribute(feat, "idparc", 3, "Integer");
			AttributeManager.addAttribute(feat, "largeur", largeur, "Double");
			AttributeManager.addAttribute(feat, "longueur", longueur, "Double");
			AttributeManager.addAttribute(feat, "hauteur", hauteur, "Double");
			AttributeManager
					.addAttribute(feat, "orient", orientation, "Double");
			AttributeManager.addAttribute(feat, "idRun", 0, "Integer");

			
			/*
			AttributeManager.addAttribute(feat, "idRun", 0, "Integer");
			AttributeManager.addAttribute(feat, "idRun", 0, "Integer");
			AttributeManager.addAttribute(feat, "idRun", 0, "Integer");
			AttributeManager.addAttribute(feat, "idRun", 0, "Integer");
			AttributeManager.addAttribute(feat, "idRun", 0, "Integer");*/
			
			
			featC.add(feat);

		}

		ShapefileWriter.write(featC, folderName + "out.shp");

		System.out.println("That's all folks");

	}

}
