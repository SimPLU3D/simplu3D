package fr.ign.cogit.simplu3d.rjmcmc.trapezoid.optimizer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.generic.endTest.StabilityEndTest;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.optimizer.DefaultSimPLU3DOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.generic.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.generic.transformation.ChangeValue;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.CSVendStats;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.CSVvisitor;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.CountVisitor;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.FilmVisitor;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.ShapefileVisitor;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.StatsVisitor;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.ViewerVisitor;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.builder.ParallelRightTrapezoidBuilder2;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry.ParallelTrapezoid2;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.transform.ParallelTrapezoidTransform;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.transform.birth.parallelTrapezoid.MoveParallelRightTrapezoid;
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
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.SimulatedAnnealing;
import fr.ign.simulatedannealing.endtest.EndTest;
import fr.ign.simulatedannealing.endtest.MaxIterationEndTest;
import fr.ign.simulatedannealing.schedule.GeometricSchedule;
import fr.ign.simulatedannealing.schedule.Schedule;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;
import fr.ign.simulatedannealing.visitor.CompositeVisitor;
import fr.ign.simulatedannealing.visitor.OutputStreamVisitor;
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
 **/
public class OptimisedParallelTrapezoidFinalDirectRejection extends DefaultSimPLU3DOptimizer<Cuboid> {

	public OptimisedParallelTrapezoidFinalDirectRejection() {
	}

	public GraphConfiguration<ParallelTrapezoid2> process(BasicPropertyUnit bpu, Parameters p, Environnement env,
			int id,
			ConfigurationModificationPredicate<GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> pred,
			IGeometry[] limits, IGeometry polygon) {
		// Géométrie de l'unité foncière sur laquelle porte la génération
		IGeometry geom = bpu.generateGeom().buffer(1);

		// Définition de la fonction d'optimisation (on optimise en décroissant)
		// relative au volume
		GraphConfiguration<ParallelTrapezoid2> conf = null;

		try {
			conf = create_configuration(p, geom, bpu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Création de l'échantilloneur
		Sampler<GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> samp = create_sampler(
				Random.random(), p, bpu, pred, limits, polygon);
		// Température
		Schedule<SimpleTemperature> sch = create_schedule(p);

		EndTest end = null;
		if (p.getBoolean(("isAbsoluteNumber"))) {
			end = create_end_test(p);
		} else {
			end = create_end_test_stability(p);
		}

		List<Visitor<GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>>> list = new ArrayList<>();
		if (p.getBoolean("outputstreamvisitor")) {
			Visitor<GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> visitor = new OutputStreamVisitor<>(
					System.out);
			list.add(visitor);
		}
		if (p.getBoolean("shapefilewriter")) {
			ShapefileVisitor<ParallelTrapezoid2, GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> shpVisitor = new ShapefileVisitor<>(
					p.get("result").toString() + "result");
			list.add(shpVisitor);
		}
		if (p.getBoolean("visitorviewer")) {
			ViewerVisitor<ParallelTrapezoid2, GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> visitorViewer = new ViewerVisitor<>(
					"" + id, p);
			list.add(visitorViewer);
		}

		if (p.getBoolean("statsvisitor")) {
			StatsVisitor<ParallelTrapezoid2, GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> statsViewer = new StatsVisitor<>(
					"Énergie");
			list.add(statsViewer);
		}

		if (p.getBoolean("filmvisitor")) {
			IDirectPosition dpCentre = new DirectPosition(p.getDouble("filmvisitorx"), p.getDouble("filmvisitory"),
					p.getDouble("filmvisitorz"));
			Vecteur viewTo = new Vecteur(p.getDouble("filmvisitorvectx"), p.getDouble("filmvisitorvecty"),
					p.getDouble("filmvisitorvectz"));
			Color c = new Color(p.getInteger("filmvisitorr"), p.getInteger("filmvisitorg"),
					p.getInteger("filmvisitorb"));
			FilmVisitor<ParallelTrapezoid2, GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> visitorViewerFilmVisitor = new FilmVisitor<>(
					dpCentre, viewTo, p.getString("result"), c, p);
			list.add(visitorViewerFilmVisitor);
		}

		if (p.getBoolean("csvvisitorend")) {
			String fileName = p.get("result").toString() + p.get("csvfilenamend");
			CSVendStats<ParallelTrapezoid2, GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> statsViewer = new CSVendStats<>(
					fileName);
			list.add(statsViewer);
		}
		if (p.getBoolean("csvvisitor")) {
			String fileName = p.get("result").toString() + p.get("csvfilename");
			CSVvisitor<ParallelTrapezoid2, GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> statsViewer = new CSVvisitor<>(
					fileName);
			list.add(statsViewer);
		}

		CompositeVisitor<GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> mVisitor = new CompositeVisitor<>(
				list);
		init_visitor(p, mVisitor);
		/*
		 * < This is the way to launch the optimization process. Here, the magic
		 * happen... >
		 */
		SimulatedAnnealing.optimize(Random.random(), conf, samp, sch, end, mVisitor);
		return conf;
	}

	// Initialisation des visiteurs
	// nbdump => affichage dans la console
	// nbsave => sauvegarde en shapefile
	static void init_visitor(Parameters p,
			Visitor<GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> v) {
		v.init(p.getInteger("nbdump"), p.getInteger("nbsave"));
	}

	CountVisitor<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> countV = null;

	public GraphConfiguration<ParallelTrapezoid2> create_configuration(Parameters p, IGeometry geom,
			BasicPropertyUnit bpu) throws Exception {

		return this.create_configuration(p, AdapterFactory.toGeometry(new GeometryFactory(), geom), bpu);

	}

	public GraphConfiguration<ParallelTrapezoid2> create_configuration(Parameters p, Geometry geom,
			BasicPropertyUnit bpu) {
		// Énergie constante : à la création d'un nouvel objet

		double energyCrea = Double.isNaN(this.energyCreation) ? p.getDouble("energy") : this.energyCreation;

		ConstantEnergy<ParallelTrapezoid2, ParallelTrapezoid2> energyCreation = new ConstantEnergy<ParallelTrapezoid2, ParallelTrapezoid2>(
				energyCrea);

		// Énergie constante : pondération de l'intersection
		ConstantEnergy<ParallelTrapezoid2, ParallelTrapezoid2> ponderationVolume = new ConstantEnergy<ParallelTrapezoid2, ParallelTrapezoid2>(
				p.getDouble("ponderation_volume"));
		// Énergie unaire : aire dans la parcelle
		UnaryEnergy<ParallelTrapezoid2> energyVolume = new VolumeUnaryEnergy<ParallelTrapezoid2>();
		// Multiplication de l'énergie d'intersection et de l'aire
		UnaryEnergy<ParallelTrapezoid2> energyVolumePondere = new MultipliesUnaryEnergy<ParallelTrapezoid2>(
				ponderationVolume, energyVolume);

		// On retire de l'énergie de création, l'énergie de l'aire
		UnaryEnergy<ParallelTrapezoid2> u3 = new MinusUnaryEnergy<ParallelTrapezoid2>(energyCreation,
				energyVolumePondere);

		// Énergie constante : pondération de la différence
		ConstantEnergy<ParallelTrapezoid2, ParallelTrapezoid2> ponderationDifference = new ConstantEnergy<ParallelTrapezoid2, ParallelTrapezoid2>(
				p.getDouble("ponderation_difference_ext"));
		// On ajoute l'énergie de différence : la zone en dehors de la parcelle
		UnaryEnergy<ParallelTrapezoid2> u4 = new DifferenceVolumeUnaryEnergy<ParallelTrapezoid2>(geom);
		UnaryEnergy<ParallelTrapezoid2> u5 = new MultipliesUnaryEnergy<ParallelTrapezoid2>(ponderationDifference, u4);
		UnaryEnergy<ParallelTrapezoid2> unaryEnergy = new PlusUnaryEnergy<ParallelTrapezoid2>(u3, u5);

		// Énergie binaire : intersection entre deux rectangles
		ConstantEnergy<ParallelTrapezoid2, ParallelTrapezoid2> c3 = new ConstantEnergy<ParallelTrapezoid2, ParallelTrapezoid2>(
				p.getDouble("ponderation_volume_inter"));
		BinaryEnergy<ParallelTrapezoid2, ParallelTrapezoid2> b1 = new IntersectionVolumeBinaryEnergy<ParallelTrapezoid2>();
		BinaryEnergy<ParallelTrapezoid2, ParallelTrapezoid2> binaryEnergy = new MultipliesBinaryEnergy<ParallelTrapezoid2, ParallelTrapezoid2>(
				c3, b1);
		// empty initial configuration*/
		GraphConfiguration<ParallelTrapezoid2> conf = new GraphConfiguration<>(unaryEnergy, binaryEnergy);
		return conf;
	}

	/**
	 * Sampler
	 * 
	 * @param p
	 *            les paramètres chargés depuis le fichier xml
	 * @param r
	 *            l'enveloppe dans laquelle on génère les positions
	 * @return
	 */
	public Sampler<GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> create_sampler(
			RandomGenerator rng, Parameters p, BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> pred,
			IGeometry[] limits, IGeometry polygon) {
		// Un vecteur ?????
		double minlen1 = p.getDouble("minlen1");
		double maxlen1 = p.getDouble("maxlen1");

		double minlen2 = p.getDouble("minlen2");
		double maxlen2 = p.getDouble("maxlen2");

		double minlen3 = p.getDouble("minlen3");
		double maxlen3 = p.getDouble("maxlen3");

		double minwid = Double.isNaN(this.minWidthBox) ? p.getDouble("minwid") : this.minWidthBox;
		double maxwid = Double.isNaN(this.maxWidthBox) ? p.getDouble("maxwid") : this.maxWidthBox;

		double minheight = p.getDouble("minheight");
		double maxheight = p.getDouble("maxheight");
		// A priori on redéfini le constructeur de l'objet
		// A priori on redéfini le constructeur de l'objet
		ObjectBuilder<ParallelTrapezoid2> builder = new ParallelRightTrapezoidBuilder2(limits, polygon);

		IEnvelope env = bpU.getGeom().envelope();
		// Sampler de naissance
		// UniformBirthInGeom<Cuboidpred2> birth = new
		// UniformBirthInGeom<Cuboid2>(new
		// Cuboid2(env.minX(),
		// env.minY(), mindim, mindim, minheight, 0), new Cuboid2(env.maxX(),
		// env.maxY(), maxdim,
		// maxdim, maxheight, Math.PI), builder, bpU.getpol2D());

		if (samplingSurface == null) {
			samplingSurface = bpU.getpol2D();
		}

		UniformBirth<ParallelTrapezoid2> birth = new UniformBirth<ParallelTrapezoid2>(rng,
				new ParallelTrapezoid2(env.minX(), env.minY(), minlen1, minlen2, minlen3, minwid, minheight, 0),
				new ParallelTrapezoid2(env.maxX(), env.maxY(), maxlen1, maxlen2, maxlen3, maxwid, maxheight, 1),
				new ParallelRightTrapezoidBuilder2(limits, polygon), ParallelTrapezoidTransform.class, polygon);

		PoissonDistribution distribution = new PoissonDistribution(rng, p.getDouble("poisson"));

		DirectSampler<ParallelTrapezoid2, GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> ds = new DirectRejectionSampler<>(
				distribution, birth, pred);

		// Probabilité de naissance-morts modifications
		List<Kernel<GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>>> kernels = new ArrayList<>(
				3);
		KernelFactory<ParallelTrapezoid2, GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> factory = new KernelFactory<>();
		// TODO Use a KernelProposalRatio to propose only birth when size is 0
		kernels.add(
				factory.make_uniform_birth_death_kernel(rng, builder, birth, p.getDouble("pbirth"), 1.0, "BirthDeath"));
		double amplitudeMove = p.getDouble("amplitudeMove");
		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new MoveParallelRightTrapezoid(amplitudeMove), 0.2, "Move"));

		double amplitudeMaxDim = p.getDouble("amplitudeMaxDim");
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeValue(amplitudeMaxDim, 6, 2), 0.2,
				"ChgLength"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeValue(amplitudeMaxDim, 6, 3), 0.2,
				"ChgWidth"));

		double amplitudeHeight = p.getDouble("amplitudeHeight");
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeValue(amplitudeHeight, 6, 4), 0.2,
				"ChgHeight"));

		double amplitudeRotate = p.getDouble("amplitudeRotate") * Math.PI / 180;
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeValue(amplitudeRotate, 6, 5), 0.2,
				"Rotate"));

		Sampler<GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> s = new GreenSamplerBlockTemperature<>(
				rng, ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);
		return s;
	}

	private static EndTest create_end_test(Parameters p) {
		return new MaxIterationEndTest(p.getInteger("nbiter"));
	}

	private EndTest create_end_test_stability(Parameters p) {
		double loc_deltaconf;
		if (Double.isNaN(this.deltaConf)) {
			loc_deltaconf = p.getDouble("delta");
		} else {
			loc_deltaconf = this.deltaConf;
		}
		return new StabilityEndTest<ParallelTrapezoid2>(p.getInteger("nbiter"), loc_deltaconf);
	}

	private Schedule<SimpleTemperature> create_schedule(Parameters p) {
		double coefDef = 0;
		if (Double.isNaN(this.coeffDec)) {
			coefDef = p.getDouble("deccoef");
		} else {
			coefDef = this.coeffDec;
		}
		return new GeometricSchedule<SimpleTemperature>(new SimpleTemperature(p.getDouble("temp")), coefDef);
	}
}
