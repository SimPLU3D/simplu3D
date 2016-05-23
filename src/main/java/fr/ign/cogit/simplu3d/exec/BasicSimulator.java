package fr.ign.cogit.simplu3d.exec;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.demo.nonStructDatabase.shp.LoadDefaultEnvironment;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.predicate.SamplePredicate;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 * 
 *          Simulateur standard
 * 
 * 
 */
public class BasicSimulator {

	/**
	 * @param args
	 */

	// [building_footprint_rectangle_cli_main
	public static void main(String[] args) throws Exception {

		// Chargement du fichier de configuration
		String folderName = BasicSimulator.class.getClassLoader()
				.getResource("scenario/").getPath();
		String fileName = "building_parameters_project_expthese_3.xml";
		Parameters p = Parameters.unmarshall(new File(folderName + fileName));

		// Chargement de l'environnement
		Environnement env = LoadDefaultEnvironment.getENVDEF();

		// Récupération de l'unité foncière surlaquelle bâtir
		// J'ai mis 8 car c'est la plus grosse et on sera sur d'avoir des
		// bâtiments
		BasicPropertyUnit bPU = env.getBpU().get(8);

		// Création du Sampler (qui va générer les propositions de solutions)
		OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

		// Valeurs de règles à saisir
		// Recul par rapport à la voirie
		double distReculVoirie = 0.0;
		// Recul par rapport au fond de la parcelle
		double distReculFond = 2;
		// Recul par rapport aux bordures latérales
		double distReculLat = 4;
		// Distance entre 2 boîtes d'une même parcelle
		double distanceInterBati = 5;
		// CES maximal (2 ça ne sert à rien)
		double maximalCES = 2;

		// Instanciation du sampler avec l'unité foncière et les valeurs
		// ci-dessus
		SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(
				bPU, distReculVoirie, distReculFond, distReculLat,
				distanceInterBati, maximalCES);
		
		
	

		// Lancement de l'optimisation avec unité foncière, paramètres,
		// environnement, id et prédicat
		GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);

		// On prépare la sortie pour récupérer la liste des entités
		IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();

		for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {

			IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());
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
		ShapefileWriter.write(iFeatC, p.get("result").toString() + "out.shp");

		System.out.println("That's all folks");

	}

}
