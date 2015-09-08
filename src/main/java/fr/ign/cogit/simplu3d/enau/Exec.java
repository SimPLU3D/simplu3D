package fr.ign.cogit.simplu3d.enau;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.convert.GenerateSolidFromCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.util.AssignZ;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

public class Exec {

	public static void main(String[] args) throws Exception {

		// Chargement du fichier de configuration
		String folderName = "C:/Users/mbrasebin/Desktop/Alia/";

		String fileName = "building_parameters_project_expthese_3.xml";
		
		CadastralParcelLoader.ATT_ID_PARC = "id_parcell";
		AssignZ.DEFAULT_Z = 0;
		
		
		Parameters p = Parameters.unmarshall(new File(folderName + fileName));

		// Chargement de l'environnement
		Environnement env = LoaderSHP.loadNoDTM(folderName);
		
		

		BasicPropertyUnit bPU = null;
		for(BasicPropertyUnit bPUTemp : env.getBpU()){
				
			if(bPUTemp.getId() == 4){
				bPU = bPUTemp;
				break;
			}
			
		}

		// Création du Sampler (qui va générer les propositions de solutions)
		OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

		// Valeurs de règles à saisir
		// C1
		double distReculVoirie = 2;

		// C2
		double slope = 1;
		double hIni = 45;

		// C3
		double hMax = 17;

		// C4
		double distReculLimi = 5.4;
		double slopeProspectLimit = 2;

		// C7
		double maximalCES = 0.5;

		p.set("maxheight", hMax);

		PredicateTunis<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new PredicateTunis<>(
				distReculVoirie, slope, hIni, hMax, distReculLimi,
				slopeProspectLimit, maximalCES, bPU);

		// Lancement de l'optimisation avec unité foncière, paramètres,
		// environnement, id et prédicat
		GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);

		// On prépare la sortie pour récupérer la liste des entités
		IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();

		for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {

			IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
			iMS.addAll(GenerateSolidFromCuboid.generate(v.getValue())
					.getFacesList());

			IFeature feat = new DefaultFeature(iMS);
			// On ajoute des attributs aux entités (dimension des objets)
			AttributeManager
					.addAttribute(feat, "Longueur",
							Math.max(v.getValue().length, v.getValue().width),
							"Double");
			AttributeManager
					.addAttribute(feat, "Largeur",
							Math.min(v.getValue().length, v.getValue().width),
							"Double");
			AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height,
					"Double");
			AttributeManager.addAttribute(feat, "Rotation",
					v.getValue().orientation, "Double");

			iFeatC.add(feat);

		}

		// On écrit en sortie le shapefile
		// ATTENTIONT : il faut mettre à jour le nom de fichier en sorie
		ShapefileWriter.write(iFeatC, folderName + "out.shp");

		System.out.println("That's all folks");

	}

}
