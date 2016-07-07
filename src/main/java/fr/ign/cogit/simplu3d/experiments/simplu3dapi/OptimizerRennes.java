package fr.ign.cogit.simplu3d.experiments.simplu3dapi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.Rules;
import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.experiments.simplu3dapi.object.ParametricCuboid;
import fr.ign.cogit.simplu3d.experiments.simplu3dapi.predicate.RennesSamplePredicate;
import fr.ign.cogit.simplu3d.importer.AssignBuildingPartToSubParcel;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.reader.RoadReader;
import fr.ign.cogit.simplu3d.reader.UrbaZoneReader;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.util.AssignZ;
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
 *          Optimizer for the experiment on Rennes-Metropole
 * 
 * 
 * 
 */
public class OptimizerRennes {

	public static void main(String[] args) throws Exception {

		////// On initialise les paramètres statiques

		//On met tout à zéro car on n'utilise pas le MNT
		AssignZ.DEFAULT_Z = 0;
		
		
		// Rerouting towards the new files
//		LoaderSHP.NOM_FICHIER_PLU = "DOC_URBA.shp";
//		LoaderSHP.NOM_FICHIER_ZONAGE = "zones_UB2.shp";
//		LoaderSHP.NOM_FICHIER_PARCELLE = "parcelles_UB2.shp";
//		LoaderSHP.NOM_FICHIER_TERRAIN = "MNT_UB2_L93.asc";
//		LoaderSHP.NOM_FICHIER_VOIRIE = "Voirie_UB2.shp";
//		// LoaderSHP.NOM_FICHIER_BATIMENTS = "Bati_UB2_3D.shp";
//		LoaderSHP.NOM_FICHIER_BATIMENTS = "Bati_UB2_3D_V2.shp";
//
//		LoaderSHP.NOM_FICHIER_PRESC_LINEAIRE = "no_file.shp";

		// Corrections on attributes
		RoadReader.ATT_LARGEUR = "LARGEUR";
		RoadReader.ATT_NOM_RUE = "NOM_VOIE_G";
		RoadReader.ATT_TYPE = "NATURE";

		CadastralParcelLoader.ATT_ID_PARC = "NUMERO";
		CadastralParcelLoader.TYPE_ANNOTATION = 1;

		UrbaZoneReader.ATT_TYPE_ZONE = "TYPE";

		AssignBuildingPartToSubParcel.RATIO_MIN = 0.8;
		AssignBuildingPartToSubParcel.ASSIGN_METHOD = 0;

		// On charge l'environnement
		Environnement env = LoaderSHP.loadNoDTM(new File(
				"/home/mickael/data/mbrasebin/workspace/simPLU3D/simplu3d-api/src/test/resources/data/demo-01"));
		
		//On récupère le fichier de paramétrage de la simulatoin
		String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
		String fileName = "building_parameters_project_expthese_rennes.xml";
		
		//Les règles
		Rules r =  new Rules("UB2,6,0.5,500,16,0.4,16.5,0,0,4,1,1,11,1,3,6,8.5,1,3.5,7");
		
		
		// We load the parameters file for the simulation
		Parameters p = Parameters.unmarshall(new File(folderName+fileName) );
		
		List<ParametricCuboid> sim = simulate(env, env.getBpU().get(0), r,  p);

		System.out.println("Nombre d'objets  : "+sim.size());
	}

	public static List<ParametricCuboid> simulate(Environnement env, BasicPropertyUnit bpToSimulate , Rules rules, Parameters p) throws Exception {




		if (bpToSimulate == null) {
			return null;
		}

		// Instantiation of the sampler
		OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

		RennesSamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new RennesSamplePredicate<>(
				bpToSimulate, rules);

		// Run of the optimisation on a parcel with the predicate
		GraphConfiguration<Cuboid> cc = oCB.process(bpToSimulate, p, env, 1, pred);

		List<ParametricCuboid> lC = new ArrayList<>();

		for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {
			
			lC.add(new ParametricCuboid(v.getValue()));
		}

		return lC;
	}

}
