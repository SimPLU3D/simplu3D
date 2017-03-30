package fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.paralellcuboid;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.builder.ParallelCuboidBuilder;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.ParallelCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.ParallelCuboidTransform;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.cogit.simplu3d.rjmcmc.generic.optimizer.DefaultSimPLU3DOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.generic.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.PrepareVisitors;
import fr.ign.mpp.DirectRejectionSampler;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.UniformBirth;
import fr.ign.mpp.kernel.UniformTypeView;
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
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.kernel.NullView;
import fr.ign.rjmcmc.kernel.Transform;
import fr.ign.rjmcmc.kernel.Variate;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.SimulatedAnnealing;
import fr.ign.simulatedannealing.endtest.EndTest;
import fr.ign.simulatedannealing.schedule.Schedule;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;

//@TODO : does not work, I have to correct it
public class ParallelCuboidOptimizer extends DefaultSimPLU3DOptimizer<ISimPLU3DPrimitive> {

	public GraphConfiguration<Cuboid> process(RandomGenerator rng, BasicPropertyUnit bpu, Parameters p,
			Environnement env, int id,
			ConfigurationModificationPredicate<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred,
			IGeometry[] limits, IGeometry polygon) throws Exception {
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
		Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> samp = create_sampler(Random.random(), p,
				bpu, pred, polygon, limits);
		// Température
		Schedule<SimpleTemperature> sch = create_schedule(p);

		EndTest end = create_end_test(p);

		PrepareVisitors<Cuboid> prep = new PrepareVisitors<>(env);
		SimulatedAnnealing.optimize(rng, conf, samp, sch, end, prep.prepare(p, id));
		return conf;
	}

	public GraphConfiguration<Cuboid> create_configuration(Parameters p, IGeometry geom, BasicPropertyUnit bpu)
			throws Exception {

		return this.create_configuration(p, AdapterFactory.toGeometry(new GeometryFactory(), geom), bpu);

	}

	public GraphConfiguration<Cuboid> create_configuration(Parameters p, Geometry geom, BasicPropertyUnit bpu) {
		// Énergie constante : à la création d'un nouvel objet

		double energyCrea = Double.isNaN(this.energyCreation) ? p.getDouble("energy") : this.energyCreation;

		ConstantEnergy<Cuboid, Cuboid> energyCreation = new ConstantEnergy<Cuboid, Cuboid>(energyCrea);

		// Énergie constante : pondération de l'intersection
		ConstantEnergy<Cuboid, Cuboid> ponderationVolume = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("ponderation_volume"));
		// Énergie unaire : aire dans la parcelle
		UnaryEnergy<Cuboid> energyVolume = new VolumeUnaryEnergy<Cuboid>();
		// Multiplication de l'énergie d'intersection et de l'aire
		UnaryEnergy<Cuboid> energyVolumePondere = new MultipliesUnaryEnergy<Cuboid>(ponderationVolume, energyVolume);

		// On retire de l'énergie de création, l'énergie de l'aire
		UnaryEnergy<Cuboid> u3 = new MinusUnaryEnergy<Cuboid>(energyCreation, energyVolumePondere);

		// Énergie constante : pondération de la différence
		ConstantEnergy<Cuboid, Cuboid> ponderationDifference = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("ponderation_difference_ext"));
		// On ajoute l'énergie de différence : la zone en dehors de la parcelle
		UnaryEnergy<Cuboid> u4 = new DifferenceVolumeUnaryEnergy<Cuboid>(geom);
		UnaryEnergy<Cuboid> u5 = new MultipliesUnaryEnergy<Cuboid>(ponderationDifference, u4);
		UnaryEnergy<Cuboid> unaryEnergy = new PlusUnaryEnergy<Cuboid>(u3, u5);

		// Énergie binaire : intersection entre deux rectangles
		ConstantEnergy<Cuboid, Cuboid> c3 = new ConstantEnergy<Cuboid, Cuboid>(p.getDouble("ponderation_volume_inter"));
		BinaryEnergy<Cuboid, Cuboid> b1 = new IntersectionVolumeBinaryEnergy<Cuboid>();
		BinaryEnergy<Cuboid, Cuboid> binaryEnergy = new MultipliesBinaryEnergy<Cuboid, Cuboid>(c3, b1);
		// empty initial configuration*/
		GraphConfiguration<Cuboid> conf = new GraphConfiguration<>(unaryEnergy, binaryEnergy);
		return conf;
	}

	public static Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> create_sampler(
			RandomGenerator rng, Parameters p, BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred,
			IGeometry pol, IGeometry[] lines) throws Exception {

		double minlen = p.getDouble("minlen");
		double maxlen = p.getDouble("maxlen");

		double minwid = p.getDouble("minwid");
		double maxwid = p.getDouble("maxwid");

		double minheight = p.getDouble("minheight");
		double maxheight = p.getDouble("maxheight");
		double maxheight2 = p.getDouble("maxheight");
		IEnvelope env = bpU.getGeom().envelope();
		// in multi object situations, we need an object builder for each
		// subtype and a sampler for the supertype (end of file)

		double[] v = new double[] { env.minX(), env.minY(), minlen, minwid, minheight, 0. };

		double[] d = new double[] { env.maxX(), env.maxY(), maxlen, maxwid, maxheight, Math.PI };

		double[] d2 = new double[] { env.maxX(), env.maxY(), maxlen, maxwid, maxheight2, Math.PI };

		for (int i = 0; i < d.length; i++) {
			d[i] = d[i] - v[i];
		}

		for (int i = 0; i < d.length; i++) {
			d2[i] = d2[i] - v[i];
		}

		IGeometry samplingSurface = null;
		if (samplingSurface == null) {
			samplingSurface = bpU.getPol2D();
		}

		Transform transform = new ParallelCuboidTransform(d2, v, samplingSurface);

		ObjectBuilder<Cuboid> builder = new ParallelCuboidBuilder(lines, 1);

		UniformBirth<Cuboid> birth = new UniformBirth<Cuboid>(rng,
				new ParallelCuboid(env.minX(), env.minY(), minlen, minwid, minheight, 0),
				new ParallelCuboid(env.maxX(), env.maxY(), maxlen, maxwid, maxheight, Math.PI), builder,
				ParallelCuboidTransform.class, samplingSurface);

		PoissonDistribution distribution = new PoissonDistribution(rng, p.getDouble("poisson"));

		Variate variate = new Variate(rng);
		List<Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> kernels = new ArrayList<>(3);
		UniformTypeView<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pView = new UniformTypeView<>(
				ParallelCuboid.class, builder);
		Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> kernel2 = new Kernel<>(new NullView<>(),
				pView, variate, variate, transform, 1.0, 1.0, "Parallel");
		kernels.add(kernel2);

		/*
		 * double amplitudeHeight = p.getDouble("amplitudeHeight");
		 * Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>
		 * parallelHeight = new Kernel<>(pView, pView, variate, variate, new
		 * ChangeValue(amplitudeHeight, 5, 3), 0.2, 1.0, "ChgHeightP");
		 * kernels.add(parallelHeight);
		 * 
		 * Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>
		 * parallelMovekernel = new Kernel<>(pView, pView, variate, variate, new
		 * MoveParallelCuboid(p.getDouble("amplitudeMove")), 0.2, 1.0,
		 * "SimpleMoveP"); kernels.add(parallelMovekernel);
		 * 
		 * Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>
		 * parallelLength = new Kernel<>(pView, pView, variate, variate, new
		 * ChangeValue(p.getDouble("amplitudeMaxDim"), 5, 2), 0.2, 1.0,
		 * "ChgLengthP"); kernels.add(parallelLength);
		 */

		DirectSampler<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> ds = new DirectRejectionSampler<>(
				distribution, birth, pred);

		Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> s = new GreenSamplerBlockTemperature<>(rng,
				ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);

		return s;
	}

}
