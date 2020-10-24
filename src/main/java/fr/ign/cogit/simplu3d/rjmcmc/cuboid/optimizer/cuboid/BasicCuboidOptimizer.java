package fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;
import org.locationtech.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.builder.CuboidBuilder;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.ChangeHeight;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.ChangeLength;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.ChangeWidth;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.MoveCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.RotateCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.TransformToSurface;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.cogit.simplu3d.rjmcmc.generic.optimizer.DefaultSimPLU3DOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.generic.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.mpp.DirectRejectionSampler;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.kernel.KernelFactory;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.UniformBirth;
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
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;

public class BasicCuboidOptimizer<C extends Cuboid> extends DefaultSimPLU3DOptimizer<ISimPLU3DPrimitive> {

	public Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> create_sampler(RandomGenerator rng,
			SimpluParameters p, BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred) {
		return create_sampler(rng, p, bpU, pred, bpU.getGeom());
	}


	/**
	 * Creation of the sampler
	 * @param rng  a random generator
	 * @param p    the parameters loaded from the json file
	 * @param bpU  the basic property unit on which the simulation will be proceeded
	 * @param pred a predicate that will check the respect of the rules
	 * @param geom a geometry that will contains all the cuboid
	 * @return a sampler that will be used during the simulation process
	 */
	public Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> create_sampler(RandomGenerator rng,
			SimpluParameters p, BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred,
			IGeometry geom) {
		
		
		//Step 1 : Creation of the object that will control the birth and death of cuboid
		
		//Getting minimal and maximal dimension from the parameter file
		double minlen = Double.isNaN(this.minLengthBox) ? p.getDouble("minlen") : this.minLengthBox;
		double maxlen = Double.isNaN(this.maxLengthBox) ? p.getDouble("maxlen") : this.maxLengthBox;

		double minwid = Double.isNaN(this.minWidthBox) ? p.getDouble("minwid") : this.minWidthBox;
		double maxwid = Double.isNaN(this.maxWidthBox) ? p.getDouble("maxwid") : this.maxWidthBox;

		double minheight = p.getDouble("minheight");
		double maxheight = p.getDouble("maxheight");

		
		//Builder class of the object
		ObjectBuilder<Cuboid> builder = new CuboidBuilder();

		//The geometry in which the sampler will be instanciated
		if (geom != null) {
			samplingSurface = geom;
		}

		if (samplingSurface == null) {
			samplingSurface = bpU.getGeom();
		}
		IEnvelope env = samplingSurface.getEnvelope();
		
		//Instanciation of the object dedicated for the creation of new cuboid during the process
		//Passing the building, the class (TransformToSurface) that will make 
		// the transformation between random numbers and coordinates inside the samplingSurface
		UniformBirth<Cuboid> birth = new UniformBirth<Cuboid>(rng,
				new Cuboid(env.minX(), env.minY(), minlen, minwid, minheight, 0),
				new Cuboid(env.maxX(), env.maxY(), maxlen, maxwid, maxheight, Math.PI), builder,
				TransformToSurface.class, samplingSurface);
		

		
		//Step 2  : Listing the modification kernel

		//List of kernel for modification during the process
		List<Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> kernels = new ArrayList<>();
		
		//A factory to create proper kernels
		KernelFactory<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> factory = new KernelFactory<>();
		
		//Adding the birth/death kernel
		kernels.add(
				factory.make_uniform_birth_death_kernel(rng, builder, birth, p.getDouble("pbirth"), 1.0, "BirthDeath"));
		//Adding the other modification kernel
		double amplitudeMove = p.getDouble("amplitudeMove");
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new MoveCuboid(amplitudeMove), 0.2, "Move"));
		double amplitudeRotate = p.getDouble("amplitudeRotate") * Math.PI / 180;
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new RotateCuboid(amplitudeRotate), 0.2,
				"Rotate"));
		double amplitudeMaxDim = p.getDouble("amplitudeMaxDim");
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeWidth(amplitudeMaxDim), 0.2,
				"ChgWidth"));
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeLength(amplitudeMaxDim), 0.2,
				"ChgLength"));
		double amplitudeHeight = p.getDouble("amplitudeHeight");
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeHeight(amplitudeHeight), 0.2,
				"ChgHeight"));
		
		//Step 3  : Creation of the sampler for the brith/death of cuboid
		
		// This distribution create a biais to make the system tends around a certain number of boxes
		PoissonDistribution distribution = new PoissonDistribution(rng, p.getDouble("poisson"));
		//Creation of the sampler with the modification in itself
		DirectSampler<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> ds = new DirectRejectionSampler<>(
				distribution, birth, pred);
		
		//Step 4  : Creation of the GreenSampler that will be used during the optimization process 
		//It notably control the acception ratio and that the created objects and that the proposed configurations area generated
		//According to the uniformbirth
		Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> s = new GreenSamplerBlockTemperature<>(rng,
				ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);
		return s;
	}

	
	/**
	 * Creation of a cuboid configuration
	 * @param p    parameters from the json file
	 * @param bpu  the basic property unit on which the optimization will be
	 *             proceeded
	 * @param geom the geometry that contains the cuboids
	 * @return a new configuration that embeds the calculation of the optimization
	 *         function
	 */

	public GraphConfiguration<Cuboid> create_configuration(SimpluParameters p, Geometry geom, BasicPropertyUnit bpu) {
		// Énergie constante : à la création d'un nouvel objet
		ConstantEnergy<Cuboid, Cuboid> energyCreation = new ConstantEnergy<Cuboid, Cuboid>(p.getDouble("energy"));
		// Énergie constante : pondération de l'intersection
		ConstantEnergy<Cuboid, Cuboid> ponderationVolume = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("ponderation_volume"));
		// Énergie unaire : aire dans la parcelle
		UnaryEnergy<Cuboid> energyVolume = new VolumeUnaryEnergy<Cuboid>();
		// Multiplication de l'énergie d'intersection et de l'aire
		UnaryEnergy<Cuboid> energyVolumePondere = new MultipliesUnaryEnergy<Cuboid>(ponderationVolume, energyVolume);

		// On retire de l'énergie de création, l'énergie de l'aire
		UnaryEnergy<Cuboid> u3 = new MinusUnaryEnergy<Cuboid>(energyCreation, energyVolumePondere);

		double ponderationExt = p.getDouble("ponderation_difference_ext");

		UnaryEnergy<Cuboid> unaryEnergy;

		if (ponderationExt != 0) {
			// Énergie constante : pondération de la différence
			ConstantEnergy<Cuboid, Cuboid> ponderationDifference = new ConstantEnergy<Cuboid, Cuboid>(
					p.getDouble("ponderation_difference_ext"));
			// On ajoute l'énergie de différence : la zone en dehors de la parcelle
			UnaryEnergy<Cuboid> u4 = new DifferenceVolumeUnaryEnergy<Cuboid>(geom);
			UnaryEnergy<Cuboid> u5 = new MultipliesUnaryEnergy<Cuboid>(ponderationDifference, u4);
			unaryEnergy = new PlusUnaryEnergy<Cuboid>(u3, u5);
		} else {
			unaryEnergy = u3;
		}

		// Énergie binaire : intersection entre deux rectangles
		ConstantEnergy<Cuboid, Cuboid> c3 = new ConstantEnergy<Cuboid, Cuboid>(p.getDouble("ponderation_volume_inter"));
		BinaryEnergy<Cuboid, Cuboid> b1 = new IntersectionVolumeBinaryEnergy<Cuboid>();
		BinaryEnergy<Cuboid, Cuboid> binaryEnergy = new MultipliesBinaryEnergy<Cuboid, Cuboid>(c3, b1);
		// empty initial configuration*/
		GraphConfiguration<Cuboid> conf = new GraphConfiguration<>(unaryEnergy, binaryEnergy);
		return conf;
	}

}
