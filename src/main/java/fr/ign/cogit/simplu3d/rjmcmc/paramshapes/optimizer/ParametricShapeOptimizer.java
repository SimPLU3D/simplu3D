package fr.ign.cogit.simplu3d.rjmcmc.paramshapes.optimizer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.random.RandomGenerator;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.MoveCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.TransformToSurface;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.cogit.simplu3d.rjmcmc.generic.optimizer.DefaultSimPLU3DOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.generic.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.PrepareVisitors;
import fr.ign.cogit.simplu3d.rjmcmc.paramshapes.builder.ParametetricBuildingBuilder;
import fr.ign.cogit.simplu3d.rjmcmc.paramshapes.factory.footprint.IWallFactory;
import fr.ign.cogit.simplu3d.rjmcmc.paramshapes.factory.roof.IRoofFactory;
import fr.ign.cogit.simplu3d.rjmcmc.paramshapes.impl.ParametricBuilding;
import fr.ign.mpp.DirectRejectionSampler;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.kernel.KernelFactory;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.UniformBirth;
import fr.ign.parameters.Parameters;
import fr.ign.random.Random;
import fr.ign.rjmcmc.acceptance.MetropolisAcceptance;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;
import fr.ign.rjmcmc.distribution.PoissonDistribution;
import fr.ign.rjmcmc.energy.BinaryEnergy;
import fr.ign.rjmcmc.energy.ConstantEnergy;
import fr.ign.rjmcmc.energy.MinusUnaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesBinaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesUnaryEnergy;
import fr.ign.rjmcmc.energy.PlusUnaryEnergy;
import fr.ign.rjmcmc.energy.UnaryEnergy;
import fr.ign.rjmcmc.kernel.ChangeValue;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.SimulatedAnnealing;
import fr.ign.simulatedannealing.endtest.EndTest;
import fr.ign.simulatedannealing.schedule.Schedule;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;
import fr.ign.simulatedannealing.visitor.CompositeVisitor;
import fr.ign.simulatedannealing.visitor.Visitor;

public class ParametricShapeOptimizer<C extends ParametricBuilding>
		extends DefaultSimPLU3DOptimizer<ISimPLU3DPrimitive> {

	/**
	 * Sampler
	 * 
	 * @param p
	 *            les paramètres chargés depuis le fichier xml
	 * @param r
	 *            l'enveloppe dans laquelle on génère les positions
	 * @return
	 */
	public Sampler<GraphConfiguration<ParametricBuilding>, BirthDeathModification<ParametricBuilding>> create_sampler(
			RandomGenerator rng, Parameters p, BasicPropertyUnit bpU, IRoofFactory roofFactory,
			IWallFactory wallFactory,
			ConfigurationModificationPredicate<GraphConfiguration<ParametricBuilding>, BirthDeathModification<ParametricBuilding>> pred) {

		ObjectBuilder<ParametricBuilding> builder = new ParametetricBuildingBuilder(wallFactory, roofFactory);

		if (samplingSurface == null) {
			samplingSurface = bpU.getpol2D();
		}

		double[] minCoord = (double[]) ArrayUtils.addAll(wallFactory.getMinParameters(),
				roofFactory.getMinParameters());
		double[] maxCoord = (double[]) ArrayUtils.addAll(wallFactory.getMaxParameters(),
				roofFactory.getMaxParameters());

		UniformBirth<ParametricBuilding> birth = new UniformBirth<ParametricBuilding>(rng,
				new ParametricBuilding(wallFactory,roofFactory,  minCoord),
				new ParametricBuilding(wallFactory,roofFactory,  maxCoord), builder, TransformToSurface.class,
				samplingSurface);

		// Distribution de poisson
		PoissonDistribution distribution = new PoissonDistribution(rng, p.getDouble("poisson"));

		DirectSampler<ParametricBuilding, GraphConfiguration<ParametricBuilding>, BirthDeathModification<ParametricBuilding>> ds = new DirectRejectionSampler<>(
				distribution, birth, pred);

		// Probabilité de naissance-morts modifications
		List<Kernel<GraphConfiguration<ParametricBuilding>, BirthDeathModification<ParametricBuilding>>> kernels = new ArrayList<>(
				3);
		KernelFactory<ParametricBuilding, GraphConfiguration<ParametricBuilding>, BirthDeathModification<ParametricBuilding>> factory = new KernelFactory<>();

		kernels.add(
				factory.make_uniform_birth_death_kernel(rng, builder, birth, p.getDouble("pbirth"), 1.0, "BirthDeath"));
		
		double amplitudeMove = p.getDouble("amplitudeMove");
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new MoveCuboid(amplitudeMove), 0.2, "Move"));

		for (int i = 0; i < wallFactory.getDimension(); i++) {

			double amplitude = p.getDouble("w" + i + "a"); // amplite name is
															// r+id+a for roof
			kernels.add(factory.make_uniform_modification_kernel(rng, builder,
					new ChangeValue(amplitude, i, builder.size()), 0.2, "ChangeW" + i));

		}

		for (int i = 0; i < roofFactory.getDimension(); i++) {

			double amplitude = p.getDouble("r" + i + "a"); // amplite name is
															// r+id+a for roof
			kernels.add(factory.make_uniform_modification_kernel(rng, builder,
					new ChangeValue(amplitude, wallFactory.getDimension() - 1 + i, builder.size()), 0.2,
					"ChangeR" + i));

		}

		Sampler<GraphConfiguration<ParametricBuilding>, BirthDeathModification<ParametricBuilding>> s = new GreenSamplerBlockTemperature<>(
				rng, ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);
		return s;
	}

	// Création de la configuration
	/**
	 * @param p
	 *            paramètres importés depuis le fichier XML
	 * @param bpu
	 *            l'unité foncière considérée
	 * @return la configuration chargée, c'est à dire la formulation énergétique
	 *         prise en compte
	 */
	public GraphConfiguration<ParametricBuilding> create_configuration(Parameters p, Geometry geom,
			BasicPropertyUnit bpu) {
		// Énergie constante : à la création d'un nouvel objet
		ConstantEnergy<ParametricBuilding, ParametricBuilding> energyCreation = new ConstantEnergy<ParametricBuilding, ParametricBuilding>(
				p.getDouble("energy"));
		// Énergie constante : pondération de l'intersection
		ConstantEnergy<ParametricBuilding, ParametricBuilding> ponderationVolume = new ConstantEnergy<ParametricBuilding, ParametricBuilding>(
				p.getDouble("ponderation_volume"));
		// Énergie unaire : aire dans la parcelle
		UnaryEnergy<ParametricBuilding> energyVolume = new VolumeUnaryEnergy<ParametricBuilding>();
		// Multiplication de l'énergie d'intersection et de l'aire
		UnaryEnergy<ParametricBuilding> energyVolumePondere = new MultipliesUnaryEnergy<ParametricBuilding>(
				ponderationVolume, energyVolume);

		// On retire de l'énergie de création, l'énergie de l'aire
		UnaryEnergy<ParametricBuilding> u3 = new MinusUnaryEnergy<ParametricBuilding>(energyCreation,
				energyVolumePondere);

		// Énergie constante : pondération de la différence
		ConstantEnergy<ParametricBuilding, ParametricBuilding> ponderationDifference = new ConstantEnergy<ParametricBuilding, ParametricBuilding>(
				p.getDouble("ponderation_difference_ext"));
		// On ajoute l'énergie de différence : la zone en dehors de la parcelle
		UnaryEnergy<ParametricBuilding> u4 = new DifferenceVolumeUnaryEnergy<ParametricBuilding>(geom);
		UnaryEnergy<ParametricBuilding> u5 = new MultipliesUnaryEnergy<ParametricBuilding>(ponderationDifference, u4);
		UnaryEnergy<ParametricBuilding> unaryEnergy = new PlusUnaryEnergy<ParametricBuilding>(u3, u5);

		// Énergie binaire : intersection entre deux rectangles
		ConstantEnergy<ParametricBuilding, ParametricBuilding> c3 = new ConstantEnergy<ParametricBuilding, ParametricBuilding>(
				p.getDouble("ponderation_volume_inter"));
		BinaryEnergy<ParametricBuilding, ParametricBuilding> b1 = new IntersectionVolumeBinaryEnergy<ParametricBuilding>();
		BinaryEnergy<ParametricBuilding, ParametricBuilding> binaryEnergy = new MultipliesBinaryEnergy<ParametricBuilding, ParametricBuilding>(
				c3, b1);
		// empty initial configuration*/
		GraphConfiguration<ParametricBuilding> conf = new GraphConfiguration<>(unaryEnergy, binaryEnergy);
		return conf;
	}
	
	public GraphConfiguration<ParametricBuilding> process(BasicPropertyUnit bpu, Parameters p, Environnement env, int id,IRoofFactory roofFactory,
			IWallFactory wallFactory,
			ConfigurationModificationPredicate<GraphConfiguration<ParametricBuilding>, BirthDeathModification<ParametricBuilding>> pred) {
	return this.process(bpu, p, env, id, pred, roofFactory, wallFactory, new ArrayList<Visitor<GraphConfiguration<ParametricBuilding>, BirthDeathModification<ParametricBuilding>>>());
}
	
	

	public GraphConfiguration<ParametricBuilding> process(BasicPropertyUnit bpu, Parameters p, Environnement env, int id,
			ConfigurationModificationPredicate<GraphConfiguration<ParametricBuilding>, BirthDeathModification<ParametricBuilding>> pred, IRoofFactory roofFactory,
			IWallFactory wallFactory,List<Visitor<GraphConfiguration<ParametricBuilding>, BirthDeathModification<ParametricBuilding>>> lSupplementaryVisitors) {
		// Géométrie de l'unité foncière sur laquelle porte la génération
		IGeometry geom = bpu.generateGeom().buffer(1);

		// Définition de la fonction d'optimisation (on optimise en décroissant)
		// relative au volume
		GraphConfiguration<ParametricBuilding> conf = null;

		try {
			conf = create_configuration(p, AdapterFactory.toGeometry(new GeometryFactory(), geom), bpu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Création de l'échantilloneur
		Sampler<GraphConfiguration<ParametricBuilding>, BirthDeathModification<ParametricBuilding>> samp = create_sampler(Random.random(), p,
				bpu, null, null, pred);
		// Température
		Schedule<SimpleTemperature> sch = create_schedule(p);


		// EndTest<Cuboid2, Configuration<Cuboid2>, SimpleTemperature,
		// Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>> end =
		// create_end_test(p);

		EndTest end = create_end_test(p);

		PrepareVisitors<ParametricBuilding> pv = new PrepareVisitors<>(env,lSupplementaryVisitors);
		CompositeVisitor<GraphConfiguration<ParametricBuilding>, BirthDeathModification<ParametricBuilding>> mVisitor = pv.prepare(p, id);

		/*
		 * < This is the way to launch the optimization process. Here, the magic
		 * happen... >
		 */
		SimulatedAnnealing.optimize(Random.random(), conf, samp, sch, end, mVisitor);
		return conf;
	}

}
