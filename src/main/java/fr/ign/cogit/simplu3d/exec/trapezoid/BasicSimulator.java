package fr.ign.cogit.simplu3d.exec.trapezoid;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.experiments.iauidf.predicate.PredicateIAUIDF;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.importer.RoadImporter;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundarySide;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry.ParallelTrapezoid2;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.optimizer.OptimisedParallelTrapezoidFinalDirectRejection;
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
	
	// Initialisation des attributs différents du schéma de base
	// et le fichier de paramètre commun à toutes les simulations
	public static void init() throws Exception {
		RoadImporter.ATT_NOM_RUE = "NOM_VOIE_G";
		RoadImporter.ATT_LARGEUR = "LARGEUR";
		RoadImporter.ATT_TYPE = "NATURE";

		LoaderSHP.NOM_FICHIER_PARCELLE = "parcelle.shp";

		CadastralParcelLoader.TYPE_ANNOTATION = 2;
		CadastralParcelLoader.ATT_HAS_TO_BE_SIMULATED = "simul";

		PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = SpecificCadastralBoundarySide.LEFT;
	}

	/**
	 * @param args
	 */

	// [building_footprint_rectangle_cli_main
	public static void main(String[] args) throws Exception {
		
		init();
		

		// Chargement du fichier de configuration
		String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
		String fileName = "building_parameters_project_trapezoid_4.xml";

		Parameters p = Parameters.unmarshall(new File(folderName + fileName));

		IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();
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

		// Chargement de l'environnement
		Environnement env = LoaderSHP.loadNoDTM(new File("/home/mickael/data/mbrasebin/donnees/simPLU3D/testTrapezoid"));
		
		for(BasicPropertyUnit bPU : env.getBpU().getElements()){


	
		SamplePredicate<ParallelTrapezoid2, GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> pred = new SamplePredicate<>(
				bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati, maximalCES);

		// Création du Sampler (qui va générer les propositions de solutions)
		OptimisedParallelTrapezoidFinalDirectRejection oCB = new OptimisedParallelTrapezoidFinalDirectRejection();

		// Instanciation du sampler avec l'unité foncière et les valeurs
		// ci-dessus

		IFeatureCollection<SpecificCadastralBoundary> lSB = bPU.getCadastralParcel().get(0)
				.getSpecificCadastralBoundaryByType(SpecificCadastralBoundaryType.ROAD);

		IGeometry[] limits = new IGeometry[lSB.size()];
		int count = 0;
		for (SpecificCadastralBoundary sc : lSB) {

			limits[count] = sc.getGeom();

			count++;
		}

		
		if(count == 0){
			continue;
		}
		
		
		
		// Lancement de l'optimisation avec unité foncière, paramètres,
		// environnement, id et prédicat
		GraphConfiguration<ParallelTrapezoid2> cc = oCB.process(bPU, p, env, bPU.getId(), pred, limits,
				(IGeometry) bPU.generateGeom().get(0));

		// IGeometry[] limits, IGeometry polygon

		// On prépare la sortie pour récupérer la liste des entités


		for (GraphVertex<ParallelTrapezoid2> v : cc.getGraph().vertexSet()) {

			IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());
			// On ajoute des attributs aux entités (dimension des objets)
			AttributeManager.addAttribute(feat, "Longueur", Math.max(v.getValue().length, v.getValue().width),
					"Double");
			AttributeManager.addAttribute(feat, "Largeur", Math.min(v.getValue().length, v.getValue().width), "Double");
			AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height, "Double");
			AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation, "Double");

			iFeatC.add(feat);

		}
	
		}
			
			

		// On écrit en sortie le shapefile
		// ATTENTIONT : il faut mettre à jour le nom de fichier en sorie
		ShapefileWriter.write(iFeatC, p.get("result").toString() + "out.shp");

		System.out.println("That's all folks");

	}

}
