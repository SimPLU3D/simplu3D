package fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid;

import java.util.List;

import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.PrepareVisitors;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.random.Random;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.ParallelTempering;
import fr.ign.simulatedannealing.endtest.EndTest;
import fr.ign.simulatedannealing.schedule.GeometricSchedule;
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
 * seehttp://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class OptimisedBuildingsCuboidFinalDirectRejectionParallelTempering extends BasicCuboidOptimizer<Cuboid> {

	private int numberOfReplicas = 1;

	private double tempMax = 1;

	public OptimisedBuildingsCuboidFinalDirectRejectionParallelTempering(int nbReplicas, double tempMax) {
		numberOfReplicas = nbReplicas;
		this.tempMax = tempMax;
	}

	public void process(BasicPropertyUnit bpu, SimpluParameters p, Environnement env, int id,
						ConfigurationModificationPredicate<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred) {
		// Géométrie de l'unité foncière sur laquelle porte la génération
		IGeometry geom = bpu.generateGeom().buffer(1);

		// Définition de la fonction d'optimisation (on optimise en décroissant)
		// relative au volume

		// Création de l'échantilloneur
		@SuppressWarnings("unchecked")
		Schedule<SimpleTemperature>[] samp = new Schedule[this.numberOfReplicas];
		// Température

		int loadExistingConfig = p.getInteger("load_existing_config");

		@SuppressWarnings("unchecked")
		GraphConfiguration<Cuboid>[] tabConfig = new GraphConfiguration[this.numberOfReplicas];

		@SuppressWarnings("unchecked")
		Visitor<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>[] tabVisitor = new Visitor[this.numberOfReplicas];

		Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> sampler = create_sampler(Random.random(), p,
				bpu, pred);

		for (int i = 0; i < this.numberOfReplicas; i++) {

			GraphConfiguration<Cuboid> conf = null;
			try {
				conf = create_configuration(p, AdapterFactory.toGeometry(new GeometryFactory(), geom), bpu);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (loadExistingConfig == 1) {
				String configPath = p.get("config_shape_file").toString();
				List<Cuboid> lCuboid = LoaderCuboid.loadFromShapeFile(configPath);
				BirthDeathModification<Cuboid> m = conf.newModification();
				for (Cuboid c : lCuboid) {
					m.insertBirth(c);
				}

				conf.deltaEnergy(m);
				// conf.apply(m);
				m.apply(conf);
				System.out.println("First update OK");
			}

			tabConfig[i] = conf;
			samp[i] = new GeometricSchedule<SimpleTemperature>(
					new SimpleTemperature(i * Math.log(this.tempMax) / (this.numberOfReplicas - 1)), 1);

			PrepareVisitors<Cuboid> pv = new PrepareVisitors<>(env);
			CompositeVisitor<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> mVisitor = pv.prepare(p, id);
			countV = pv.getCountV();
			tabVisitor[i] = mVisitor;

		}

		/*
		 * < This is the way to launch the optimization process. Here, the magic
		 * happen... >
		 */

		EndTest end = create_end_test(p);

		// Partie spécial Parrallel tempering

		ParallelTempering.optimize(Random.random(), tabConfig, sampler, samp, end, tabVisitor);

	}

}
