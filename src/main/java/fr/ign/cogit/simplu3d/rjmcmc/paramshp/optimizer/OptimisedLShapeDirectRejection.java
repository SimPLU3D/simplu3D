package fr.ign.cogit.simplu3d.rjmcmc.paramshp.optimizer;

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
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.TransformToSurface;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.optimizer.DefaultSimPLU3DOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.generic.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.PrepareVisitors;
import fr.ign.cogit.simplu3d.rjmcmc.paramshp.builder.LBuildingWithRoofBuilder;
import fr.ign.cogit.simplu3d.rjmcmc.paramshp.geometry.impl.LBuildingWithRoof;
import fr.ign.cogit.simplu3d.rjmcmc.paramshp.transform.MoveLShapeBuilding;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.mpp.DirectRejectionSampler;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.kernel.KernelFactory;
import fr.ign.mpp.kernel.UniformBirth;
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

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see http://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class OptimisedLShapeDirectRejection extends DefaultSimPLU3DOptimizer<LBuildingWithRoof> {

	public OptimisedLShapeDirectRejection() {
	}

	public GraphConfiguration<LBuildingWithRoof> process(BasicPropertyUnit bpu, SimpluParameters p,
			Environnement env, int id,
			ConfigurationModificationPredicate<GraphConfiguration<LBuildingWithRoof>, BirthDeathModification<LBuildingWithRoof>> pred,
			IGeometry polygon)
	{
		return process(Random.random(), bpu, p, env, id, pred, polygon);
	}
	public GraphConfiguration<LBuildingWithRoof> process(RandomGenerator rG, BasicPropertyUnit bpu, SimpluParameters p,
			Environnement env, int id,
			ConfigurationModificationPredicate<GraphConfiguration<LBuildingWithRoof>, BirthDeathModification<LBuildingWithRoof>> pred,
			IGeometry polygon) {
		// Géométrie de l'unité foncière sur laquelle porte la génération
		IGeometry geom = bpu.generateGeom().buffer(1);

		// Configuration que l'on optimise
		GraphConfiguration<LBuildingWithRoof> conf = null;

		try {
			// On créer la configuration (notamment la fonction énergétique)
			conf = create_configuration(p, geom, bpu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Création de l'échantilloneur
		Sampler<GraphConfiguration<LBuildingWithRoof>, BirthDeathModification<LBuildingWithRoof>> samp = create_sampler(
				rG, p, bpu, pred, polygon);
		// Température
		Schedule<SimpleTemperature> sch = create_schedule(p);

		EndTest end = create_end_test(p);
		PrepareVisitors<LBuildingWithRoof> pv = new PrepareVisitors<>(env);

		SimulatedAnnealing.optimize(rG, conf, samp, sch, end, pv.prepare(p, bpu.getId()));
		return conf;
	}

	public GraphConfiguration<LBuildingWithRoof> create_configuration(SimpluParameters p, IGeometry geom,
			BasicPropertyUnit bpu) throws Exception {

		return this.create_configuration(p, AdapterFactory.toGeometry(new GeometryFactory(), geom), bpu);

	}

	public GraphConfiguration<LBuildingWithRoof> create_configuration(SimpluParameters p, Geometry geom,
			BasicPropertyUnit bpu) {
		// Énergie constante : à la création d'un nouvel objet

		double energyCrea = Double.isNaN(this.energyCreation) ? p.getDouble("energy") : this.energyCreation;

		ConstantEnergy<LBuildingWithRoof, LBuildingWithRoof> energyCreation = new ConstantEnergy<LBuildingWithRoof, LBuildingWithRoof>(
				energyCrea);

		// Énergie constante : pondération de l'intersection
		ConstantEnergy<LBuildingWithRoof, LBuildingWithRoof> ponderationVolume = new ConstantEnergy<LBuildingWithRoof, LBuildingWithRoof>(
				p.getDouble("ponderation_volume"));
		// Énergie unaire : aire dans la parcelle
		UnaryEnergy<LBuildingWithRoof> energyVolume = new VolumeUnaryEnergy<LBuildingWithRoof>();
		// Multiplication de l'énergie d'intersection et de l'aire
		UnaryEnergy<LBuildingWithRoof> energyVolumePondere = new MultipliesUnaryEnergy<LBuildingWithRoof>(
				ponderationVolume, energyVolume);

		// On retire de l'énergie de création, l'énergie de l'aire
		UnaryEnergy<LBuildingWithRoof> u3 = new MinusUnaryEnergy<LBuildingWithRoof>(energyCreation,
				energyVolumePondere);

		double pondDiffExt = p.getDouble("ponderation_difference_ext");

		UnaryEnergy<LBuildingWithRoof> unaryEnergy = null;

		if (pondDiffExt != 0) {
			// Énergie constante : pondération de la différence
			ConstantEnergy<LBuildingWithRoof, LBuildingWithRoof> ponderationDifference = new ConstantEnergy<LBuildingWithRoof, LBuildingWithRoof>(
					pondDiffExt);
			// On ajoute l'énergie de différence : la zone en dehors de la parcelle
			UnaryEnergy<LBuildingWithRoof> u4 = new DifferenceVolumeUnaryEnergy<LBuildingWithRoof>(geom);
			UnaryEnergy<LBuildingWithRoof> u5 = new MultipliesUnaryEnergy<LBuildingWithRoof>(ponderationDifference, u4);
			unaryEnergy = new PlusUnaryEnergy<LBuildingWithRoof>(u3, u5);
		} else {
			unaryEnergy = u3;
		}

		double pondInterVolume = p.getDouble("ponderation_volume_inter");
		BinaryEnergy<LBuildingWithRoof, LBuildingWithRoof> binaryEnergy = null;
		if (pondInterVolume != 0) {

			// Énergie binaire : intersection entre deux rectangles
			ConstantEnergy<LBuildingWithRoof, LBuildingWithRoof> c3 = new ConstantEnergy<LBuildingWithRoof, LBuildingWithRoof>(
					p.getDouble("ponderation_volume_inter"));
			BinaryEnergy<LBuildingWithRoof, LBuildingWithRoof> b1 = new IntersectionVolumeBinaryEnergy<LBuildingWithRoof>();
			binaryEnergy = new MultipliesBinaryEnergy<LBuildingWithRoof, LBuildingWithRoof>(c3, b1);
		} else {
			binaryEnergy = new ConstantEnergy<LBuildingWithRoof, LBuildingWithRoof>(0);
		}

		// empty initial configuration*/
		GraphConfiguration<LBuildingWithRoof> conf = new GraphConfiguration<>(unaryEnergy, binaryEnergy);
		return conf;
	}

	

	/**
	 * 
	 * @param rng     a random generator
	 * @param p       a json parameter files
	 * @param bpU     a basic property unit
	 * @param pred    a predicate to check the rules
	 * @param polygon a polygon that contains al the cuboid
	 * @return a sampler for the optimization process
	 */
	public Sampler<GraphConfiguration<LBuildingWithRoof>, BirthDeathModification<LBuildingWithRoof>> create_sampler(
			RandomGenerator rng, SimpluParameters p, BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<LBuildingWithRoof>, BirthDeathModification<LBuildingWithRoof>> pred,
			IGeometry polygon) {

		// On créé les bornes min et max pour le sampler (10 paramètres dans le
		// cas du LBuildingWithRoof)
		IEnvelope env = polygon.envelope();

		double xmin = env.getLowerCorner().getX();
		double xmax = env.getUpperCorner().getX();

		double ymin = env.getLowerCorner().getY();
		double ymax = env.getUpperCorner().getY();

		double l1min = p.getDouble("l1min");
		double l1max = p.getDouble("l1max");

		double l2min = p.getDouble("l2min");
		double l2max = p.getDouble("l2max");

		double h1min = p.getDouble("h1min");
		double h1max = p.getDouble("h1max");

		double h2min = p.getDouble("h2min");
		double h2max = p.getDouble("h2max");

		double heightToTopMin = p.getDouble("heightToTopMin");
		double heightToTopgMax = p.getDouble("heightToTopgMax");

		double orientationMin = 0;
		double orientationMax =  Math.PI;

		double heightgutterMin = p.getDouble("heightgutterMin");
		;
		double heightguterrMax = p.getDouble("heightgutterMax");
		;

		double shiftMin = 0;
		double shiftMax = 1;

		// A priori on redéfini le constructeur de l'objet
		// A priori on redéfini le constructeur de l'objet
		LBuildingWithRoofBuilder builder = new LBuildingWithRoofBuilder();

		// On initialise la surface sur laquelle on fait la simulation
		if (samplingSurface == null) {
			samplingSurface = bpU.getPol2D();
		}

		// On initialise l'espace sur lequel on va calculer les objets
		// (normalement tu as juste à changer le nom des classes)
		UniformBirth<LBuildingWithRoof> birth = new UniformBirth<LBuildingWithRoof>(rng,
				new LBuildingWithRoof(xmin, ymin, l1min, l2min, h1min, h2min, heightToTopMin, orientationMin,
						heightgutterMin, shiftMin),
				new LBuildingWithRoof(xmax, ymax, l1max, l2max, h1max, h2max, heightToTopgMax, orientationMax,
						heightguterrMax, shiftMax),
				builder, TransformToSurface.class, (IGeometry) polygon);

		// La distribution de poisson qui drive le nombre total d'objets
		PoissonDistribution distribution = new PoissonDistribution(rng, p.getDouble("poisson"));

		// Le sampler qui détermine comment on tire aléatoirement un objet dans
		// l'espace défini
		DirectSampler<LBuildingWithRoof, GraphConfiguration<LBuildingWithRoof>, BirthDeathModification<LBuildingWithRoof>> ds = new DirectRejectionSampler<>(
				distribution, birth, pred);

		// Probabilité de naissance-morts modifications
		List<Kernel<GraphConfiguration<LBuildingWithRoof>, BirthDeathModification<LBuildingWithRoof>>> kernels = new ArrayList<>(
				3);
		KernelFactory<LBuildingWithRoof, GraphConfiguration<LBuildingWithRoof>, BirthDeathModification<LBuildingWithRoof>> factory = new KernelFactory<>();

		// On liste les kernels, pour le premier, tu devrais probablement le
		// définir toi ....
		kernels.add(
				factory.make_uniform_birth_death_kernel(rng, builder, birth, p.getDouble("pbirth"), 1.0, "BirthDeath"));
		double amplitudeMove = p.getDouble("amplitudeMove");
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new MoveLShapeBuilding(amplitudeMove), 0.2,
				"Move"));

		// Pour les autres, le ChangeValue peut être utiliser (attention, le
		// deuxième arguement est la taille de ton builder +1)
		// car il utilise un tableau pour stocker les paramètres et le +1 est
		// pour stocker de manière temporaire le tirage aléatoire
		double amplitudeMaxDim = p.getDouble("amplitudeMaxDim");
		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeValue(amplitudeMaxDim, builder.size() + 1, 2), 0.2, "h1Change"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeValue(amplitudeMaxDim, builder.size() + 1, 3), 0.2, "h2Change"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeValue(amplitudeMaxDim, builder.size() + 1, 4), 0.2, "l1Change"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeValue(amplitudeMaxDim, builder.size() + 1, 5), 0.2, "l2Change"));

		double amplitudeHeight = p.getDouble("amplitudeHeight");

		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeValue(amplitudeHeight, builder.size() + 1, 6), 0.2, "heightChange"));

		double amplitudeRotate = p.getDouble("amplitudeRotate") * Math.PI / 180;
		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeValue(amplitudeRotate, builder.size() + 1, 7), 0.2, "Rotate"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeValue(amplitudeHeight, builder.size() + 1, 8), 0.2, "changeHeightGutter"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeValue(0.1, builder.size() + 1, 9),
				0.2, "changeShift"));

		// On instancie le sampler avec tous les objets.
		Sampler<GraphConfiguration<LBuildingWithRoof>, BirthDeathModification<LBuildingWithRoof>> s = new GreenSamplerBlockTemperature<>(
				rng, ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);
		return s;
	}

}
