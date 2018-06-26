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
package fr.ign.cogit.simplu3d.experiments.simplu3dapi;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.experiments.simplu3dapi.object.ParametricCuboid;
import fr.ign.cogit.simplu3d.experiments.simplu3dapi.predicate.RennesSamplePredicate;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;

/**
 * 
 * Optimizer utilisé dans le cadre du démonstrateur SIMPLU. Il se repose sur un modèle simplifiés des règles (Rules)
 * 
 * @author MBrasebin
 *
 */
public class OptimizerRennes {

	public static List<ParametricCuboid> simulate(Environnement env, BasicPropertyUnit bpToSimulate , ZoneRegulation rules, SimpluParameters p) throws Exception {

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
