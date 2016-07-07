package fr.ign.cogit.simplu3d.exec.trapezoid;

import java.io.File;

import org.apache.commons.math3.random.RandomGenerator;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;

import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.experiments.iauidf.predicate.PredicateIAUIDF;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundarySide;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundaryType;
import fr.ign.cogit.simplu3d.reader.RoadReader;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.paralellcuboid.ParallelCuboidOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.cogit.simplu3d.rjmcmc.generic.optimizer.DefaultSimPLU3DOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.generic.predicate.SamplePredicate;
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
public class BasicSimulatorTrapezoid {

	// Initialisation des attributs différents du schéma de base
	// et le fichier de paramètre commun à toutes les simulations
	public static void init() throws Exception {
		RoadReader.ATT_NOM_RUE = "NOM_VOIE_G";
		RoadReader.ATT_LARGEUR = "LARGEUR";
		RoadReader.ATT_TYPE = "NATURE";

		//LoaderSHP.NOM_FICHIER_PARCELLE = "parcelle.shp";

		CadastralParcelLoader.TYPE_ANNOTATION = 2;
		CadastralParcelLoader.ATT_HAS_TO_BE_SIMULATED = "simul";

		PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = SpecificCadastralBoundarySide.LEFT;
	}

	/**
	 * @param args
	 */

	// [building_footprint_rectangle_cli_main
	public static void main(String[] args) throws Exception {

		boolean triangle_or_cuboid = false;

		init();

		// Chargement du fichier de configuration
		String folderName = BasicSimulatorTrapezoid.class.getClassLoader().getResource("scenario/").getPath();
		String fileName = "building_parameters_project_trapezoid_4.xml";

		Parameters p = Parameters.unmarshall(new File(folderName + fileName));

		IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();
		// Valeurs de règles à saisir
		// Recul par rapport à la voirie
		double distReculVoirie = 0.0;
		// Recul par rapport au fond de la parcelle
		double distReculFond = 2;
		// Recul par rapport aux bordures latérales
		double distReculLat = 2;
		// Distance entre 2 boîtes d'une même parcelle
		double distanceInterBati = 5;
		// CES maximal (2 ça ne sert à rien)
		double maximalCES = 2;

		// Chargement de l'environnement
		Environnement env = LoaderSHP
				.load(new File("/home/mickael/data/mbrasebin/donnees/simPLU3D/testTrapezoid"));

		for (BasicPropertyUnit bPU : env.getBpU().getElements()) {

			DefaultSimPLU3DOptimizer<? extends ISimPLU3DPrimitive> optimizer = null;

			GraphConfiguration<? extends ISimPLU3DPrimitive> cc = null;

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

			if (count == 0) {
				continue;
			}

			RandomGenerator rng = Random.random();
			if (triangle_or_cuboid) {

				SamplePredicate<ParallelTrapezoid2, GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> pred = null;

				pred = new SamplePredicate<>(bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati,
						maximalCES);
				optimizer = new OptimisedParallelTrapezoidFinalDirectRejection();
				cc = ((OptimisedParallelTrapezoidFinalDirectRejection) optimizer).process(rng, bPU, p, env, bPU.getId(),
						pred, limits, (IGeometry) bPU.generateGeom().get(0));
			} else {
				
				
				SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = null;

				
				optimizer = new ParallelCuboidOptimizer();
				
				
				pred = new SamplePredicate<>(bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati,
						maximalCES);
				
				
				cc = ((ParallelCuboidOptimizer)optimizer).process(rng,bPU, p, env,  bPU.getId(),
						pred, limits, (IGeometry) bPU.generateGeom().get(0));
				
				
				
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
			
			break;

		}

		// On écrit en sortie le shapefile
		// ATTENTIONT : il faut mettre à jour le nom de fichier en sorie
		ShapefileWriter.write(iFeatC, p.get("result").toString() + "out.shp");

		System.out.println("That's all folks");

	}

}
