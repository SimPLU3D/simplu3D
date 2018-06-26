package fr.ign.cogit.simplu3d.exec.paraShpe;

import java.io.File;
import java.util.List;

import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.cogit.simplu3d.util.SimpluParametersJSON;
import org.apache.commons.math3.random.RandomGenerator;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.paralellcuboid.ParallelCuboidOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.cogit.simplu3d.rjmcmc.generic.optimizer.DefaultSimPLU3DOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.generic.predicate.SamplePredicate;
import fr.ign.cogit.simplu3d.rjmcmc.paramshp.geometry.impl.CuboidRoofed;
import fr.ign.cogit.simplu3d.rjmcmc.paramshp.geometry.impl.LBuildingWithRoof;
import fr.ign.cogit.simplu3d.rjmcmc.paramshp.optimizer.OptimisedLShapeDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.paramshp.optimizer.OptimisedRCuboidDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry.ParallelTrapezoid2;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.optimizer.OptimisedParallelTrapezoidFinalDirectRejection;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;
import fr.ign.random.Random;

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
public class BasicParametricShapeSimulator {

	public static void init() throws Exception {

		CadastralParcelLoader.TYPE_ANNOTATION = 2;

	}

	public enum TYPE_OF_SIMUL {

		TRAPEZOID,

		CUBOID,

		LSHAPE,

		ROOFEDCUBOID;

	}

	/**
	 * @param args
	 */

	// [building_footprint_rectangle_cli_main
	public static void main(String[] args) throws Exception {
		// Type de forme à simuler
		TYPE_OF_SIMUL typeOfSimul = TYPE_OF_SIMUL.ROOFEDCUBOID;

		// Chargement du fichier de configuration
		String folderName = BasicParametricShapeSimulator.class.getClassLoader().getResource("scenario/").getPath();

		init();

		IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();
		// Valeurs de règles à saisir
		// Recul par rapport à la voirie
		double distReculVoirie = 0.0;
		// Recul par rapport au fond de la parcelle
		double distReculFond = 0;
		// Recul par rapport aux bordures latérales
		double distReculLat = 0;
		// Distance entre 2 boîtes d'une même parcelle
		double distanceInterBati = 5;
		// CES maximal (2 ça ne sert à rien)
		double maximalCES = 2;

		Environnement env = LoaderSHP.loadNoDTM(new File(
				DemoEnvironmentProvider.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/data/").getPath()));

		// Fichier de paramètre qui va dépendre du type d'objet simulé
		SimpluParameters p = null;

		for (BasicPropertyUnit bPU : env.getBpU().getElements()) {
			// L'optimizer
			DefaultSimPLU3DOptimizer<? extends ISimPLU3DPrimitive> optimizer = null;
			// La configuration qui va évoluer pendant la simulation
			GraphConfiguration<? extends ISimPLU3DPrimitive> cc = null;

			// Random generator utilisé pour la suite
			RandomGenerator rng = Random.random();

			// On fonction du type d'objet simulé
			switch (typeOfSimul) {
			case CUBOID:
				SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = null;

				String fileName = "building_parameters_project_trapezoid_4.xml";

				p = new SimpluParametersJSON(new File(folderName + fileName));

				optimizer = new ParallelCuboidOptimizer();

				pred = new SamplePredicate<>(bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati,
						maximalCES);

				cc = ((ParallelCuboidOptimizer) optimizer).process(rng, bPU, p, env, bPU.getId(), pred,
						calculLimit(bPU), bPU.getGeom());
				break;
			case LSHAPE:
				// On instancie le prédicat (vérification des règles,
				// normalement,
				// rien à faire)
				SamplePredicate<LBuildingWithRoof, GraphConfiguration<LBuildingWithRoof>, BirthDeathModification<LBuildingWithRoof>> pred3 = null;

				// On indique le fichier de configuration (à créer ou utiliser
				// un
				// existant)
				String fileName2 = "building_parameters_project_lshape.xml";
				// On charge le fichier de configuration
				p = new SimpluParametersJSON(new File(folderName + fileName2));
				// On instancie le prédicat (vérification des règles,
				// normalement,
				// rien à faire)
				pred3 = new SamplePredicate<>(bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati,
						maximalCES);

				// On génère l'optimizer et on le lance (à faire)
				optimizer = new OptimisedLShapeDirectRejection();
				cc = ((OptimisedLShapeDirectRejection) optimizer).process(rng, bPU, p, env, bPU.getId(), pred3,
						bPU.getGeom());

				break;
			case TRAPEZOID:

				String fileName3 = "building_parameters_project_trapezoid_4.xml";

				p = new SimpluParametersJSON(new File(folderName + fileName3));

				SamplePredicate<ParallelTrapezoid2, GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> pred2 = null;

				pred2 = new SamplePredicate<>(bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati,
						maximalCES);
				optimizer = new OptimisedParallelTrapezoidFinalDirectRejection();
				cc = ((OptimisedParallelTrapezoidFinalDirectRejection) optimizer).process(rng, bPU, p, env, bPU.getId(),
						pred2, calculLimit(bPU), bPU.getGeom());
				break;
			case ROOFEDCUBOID:
				// On instancie le prédicat (vérification des règles,
				// normalement,
				// rien à faire)
				SamplePredicate<CuboidRoofed, GraphConfiguration<CuboidRoofed>, BirthDeathModification<CuboidRoofed>> pred4 = null;

				// On indique le fichier de configuration (à créer ou utiliser
				// un
				// existant)
				String fileName4 = "building_parameters_project_rcuboid.xml";
				// On charge le fichier de configuration
				p = new SimpluParametersJSON(new File(folderName + fileName4));
				// On instancie le prédicat (vérification des règles,
				// normalement,
				// rien à faire)
				pred4 = new SamplePredicate<>(bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati,
						maximalCES);

				// On génère l'optimizer et on le lance (à faire)
				optimizer = new OptimisedRCuboidDirectRejection();
				cc = ((OptimisedRCuboidDirectRejection) optimizer).process(rng, bPU, p, env, bPU.getId(), pred4,
						bPU.getGeom());
				break;
			default:
				break;

			}

			// Lancement de l'optimisation avec unité foncière, paramètres,
			// environnement, id et prédicat

			// IGeometry[] limits, IGeometry polygon

			// On prépare la sortie pour récupérer la liste des entités

			for (GraphVertex<? extends ISimPLU3DPrimitive> v : cc.getGraph().vertexSet()) {

				IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());
				// On ajoute des attributs aux entités (dimension des objets)
				AttributeManager.addAttribute(feat, "Info", v.getValue().toString(), "Double");

				iFeatC.add(feat);

			}

		}

		// On écrit en sortie le shapefile
		// ATTENTIONT : il faut mettre à jour le nom de fichier en sorie
		ShapefileWriter.write(iFeatC, p.get("result").toString() + "out.shp");

		System.out.println("That's all folks");

	}

	private static IGeometry[] calculLimit(BasicPropertyUnit bPU) {
		// Instanciation du sampler avec l'unité foncière et les valeurs
		// ci-dessus
		// Pour les simulation nécessitants
		List<ParcelBoundary> lSB = bPU.getCadastralParcels().get(0).getBoundariesByType(ParcelBoundaryType.ROAD);

		IGeometry[] limits = new IGeometry[lSB.size()];
		int count = 0;
		for (ParcelBoundary sc : lSB) {

			limits[count] = sc.getGeom();

			count++;
		}

		return limits;
	}

}
