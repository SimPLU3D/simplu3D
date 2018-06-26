package fr.ign.cogit.simplu3d.experiments.herault;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.experiments.herault.predicate.PredicateHerault;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.cogit.simplu3d.util.SimpluParametersJSON;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;

public class Simulator {

	public static void main(String[] args) throws Exception {
		// Chemin vers le dossier où se trouvent les données
		String folder = "D:/donnees/3AU/";
	
		// Chemin vers le fichier où les résultats seront stockés
		// String folderOut = "D://donnees//4AUa//out2//out2.shp";

		int nbMaxBox = 2;

		// On charge le fichier de configuration (cf regarder à l'intérieur)
		String fileName = "parameters.xml";
		SimpluParameters p = new SimpluParametersJSON(new File(folder + fileName));

		// On charge l'environnement géographique
		Environnement env = LoaderSHP.loadNoDTM(new File(folder));

		// Nombre de parcelles
		System.out.println(env.getBpU().size());

		// Nombre de zones interdites
		System.out.println("Number of forbidden zones : " + env.getPrescriptions().size());

		
		System.out.println(folder + "zone_urba.shp");
		// On lit le shapeFile avec la zone
		IFeatureCollection<IFeature> featCollPLU = ShapefileReader.read(folder + "zone_urba.shp");

		
	

		for (BasicPropertyUnit bPU : env.getBpU()) {
	                // Writting the output
	                IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();
		    
		    
		    
		    String folderOut = "D:/donnees/3AU/out/" + "out" + bPU.getCadastralParcels().get(0).getCode() + ".shp";


			// String sectionparcelle = (String)
			// bPU.getCadastralParcels().get(0).getAttribute("SECTION");

			// On vérifie que l'on doit bien simuler la parcelle
			// Attribut "simul" dans le shapefile des parcelles à 1
			if (!bPU.getCadastralParcels().get(0).hasToBeSimulated()) {
				continue;
			}

			// Numéro de la parcelle que l'on simule
			System.out.println("Simulation de la parcelle : " + bPU.getCadastralParcels().get(0).getCode());

			// Instantiation of the sampler
			OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

			PredicateHerault<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new PredicateHerault<>(
					bPU, env.getPrescriptions(), featCollPLU.get(0), nbMaxBox);

			System.out.println(pred.toString());

			GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, bPU.getId(), pred);

			// For all generated boxes
			for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {

				// Output feature with generated geometry
				IFeature feat = new DefaultFeature(v.getValue().getFootprint());

				// We write some attributes
				AttributeManager.addAttribute(feat, "Longueur", Math.max(v.getValue().length, v.getValue().width),
						"Double");
				AttributeManager.addAttribute(feat, "Largeur", Math.min(v.getValue().length, v.getValue().width),
						"Double");
				AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height, "Double");
				AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation, "Double");
				AttributeManager.addAttribute(feat, "IdParcel", bPU.getCadastralParcels().get(0).getCode(), "String");
				iFeatC.add(feat);

			}

		             // A shapefile is written as output
	                // WARNING : 'out' parameter from configuration file have to be change
	                ShapefileWriter.write(iFeatC, folderOut);

		}


	}

}
