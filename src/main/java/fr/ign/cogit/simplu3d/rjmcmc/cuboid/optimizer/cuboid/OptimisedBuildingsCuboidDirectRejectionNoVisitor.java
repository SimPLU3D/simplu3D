package fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.util.SimpluParameters;
import org.apache.commons.math3.random.RandomGenerator;

import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.SimulatedAnnealing;
import fr.ign.simulatedannealing.endtest.EndTest;
import fr.ign.simulatedannealing.schedule.Schedule;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;
import fr.ign.simulatedannealing.visitor.CompositeVisitor;
import fr.ign.simulatedannealing.visitor.Visitor;

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
 *          Classe d'optimisation sans visiteur pour calcul distribués
 * 
 * 
 */
public class OptimisedBuildingsCuboidDirectRejectionNoVisitor extends BasicCuboidOptimizer<Cuboid> {

	public GraphConfiguration<Cuboid> process(RandomGenerator rng, BasicPropertyUnit bpu, SimpluParameters p,
			Environnement env,
			ConfigurationModificationPredicate<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred) {
		// Géométrie de l'unité foncière sur laquelle porte la génération
		IGeometry geom = bpu.generateGeom().buffer(1);

		// Définition de la fonction d'optimisation (on optimise en décroissant)
		// relative au volume
		GraphConfiguration<Cuboid> conf = null;
		try {
			conf = create_configuration(p, AdapterFactory.toGeometry(new GeometryFactory(), geom), bpu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Création de l'échantilloneur
		Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> samp = create_sampler(rng, p, bpu, pred);
		// Température
		Schedule<SimpleTemperature> sch = create_schedule(p);

		EndTest end = create_end_test(p);

		List<Visitor<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> list = new ArrayList<>();
		CompositeVisitor<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> mVisitor = new CompositeVisitor<>(
				list);

		SimulatedAnnealing.optimize(rng, conf, samp, sch, end, mVisitor);
		return conf;
	}

}
