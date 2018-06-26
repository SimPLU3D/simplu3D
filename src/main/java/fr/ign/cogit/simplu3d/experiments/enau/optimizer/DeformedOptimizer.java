package fr.ign.cogit.simplu3d.experiments.enau.optimizer;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.util.SimpluParameters;
import org.apache.commons.math3.random.RandomGenerator;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.convert.FromGeomToLineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.experiments.enau.builder.DeformedCuboidBuilder;
import fr.ign.cogit.simplu3d.experiments.enau.builder.ParallelDeformedCuboidBuilder;
import fr.ign.cogit.simplu3d.experiments.enau.energy.AlignementEnergy;
import fr.ign.cogit.simplu3d.experiments.enau.energy.HauteurEnergy;
import fr.ign.cogit.simplu3d.experiments.enau.energy.ProspectEnergy;
import fr.ign.cogit.simplu3d.experiments.enau.energy.ProspectEnergy2;
import fr.ign.cogit.simplu3d.experiments.enau.energy.ServitudeVue;
import fr.ign.cogit.simplu3d.experiments.enau.geometry.DeformedCuboid;
import fr.ign.cogit.simplu3d.experiments.enau.geometry.ParallelDeformedCuboidTransform;
import fr.ign.cogit.simplu3d.experiments.enau.sampler.ParalledDeformedSampler;
import fr.ign.cogit.simplu3d.experiments.enau.transformation.ChangeHeight1Deformed;
import fr.ign.cogit.simplu3d.experiments.enau.transformation.ChangeHeight2Deformed;
import fr.ign.cogit.simplu3d.experiments.enau.transformation.ChangeHeight3Deformed;
import fr.ign.cogit.simplu3d.experiments.enau.transformation.ChangeHeight4Deformed;
import fr.ign.cogit.simplu3d.experiments.enau.transformation.ChangeValueDeformed;
import fr.ign.cogit.simplu3d.experiments.enau.transformation.MoveCuboidDeformed;
import fr.ign.cogit.simplu3d.experiments.enau.transformation.MoveParallelDeformedCuboid;
import fr.ign.cogit.simplu3d.experiments.enau.transformation.RotateCuboid;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.TransformToSurface;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.optimizer.DefaultSimPLU3DOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.generic.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.PrepareVisitors;
import fr.ign.mpp.DirectRejectionSampler;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.kernel.KernelFactory;
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
import fr.ign.rjmcmc.kernel.ChangeValue;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.kernel.NullView;
import fr.ign.rjmcmc.kernel.Variate;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.SimulatedAnnealing;
import fr.ign.simulatedannealing.endtest.EndTest;
import fr.ign.simulatedannealing.schedule.Schedule;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;

public class DeformedOptimizer extends DefaultSimPLU3DOptimizer<Cuboid> {

	public GraphConfiguration<DeformedCuboid> process(
			BasicPropertyUnit bpu, SimpluParameters p, Environnement env, int id,
			ConfigurationModificationPredicate<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> pred,
			double distReculVoirie, double slope, double hIni, double hMax, double distReculLimi, double slopeProspect,
			double maximalCES, int pos) throws Exception {

		// Géométrie de l'unité foncière sur laquelle porte la génération
		IGeometry geom = bpu.generateGeom().buffer(1);

		// Définition de la fonction d'optimisation (on optimise en décroissant)
		// relative au volume
		GraphConfiguration<DeformedCuboid> conf = null;

		try {
			conf = create_configuration(p, geom, bpu, distReculVoirie, slope, hIni, hMax, distReculLimi, slopeProspect,
					maximalCES);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Création de l'échantilloneur
		Sampler<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> samp = create_sampler(
				Random.random(), p, bpu, pred, pos);
		// Température
		Schedule<SimpleTemperature> sch = create_schedule(p);

		EndTest end = this.create_end_test(p);

		PrepareVisitors<DeformedCuboid> pv = new PrepareVisitors<>(env);

		/*
		 * < This is the way to launch the optimization process. Here, the magic
		 * happen... >
		 */
		SimulatedAnnealing.optimize(Random.random(), conf, samp, sch, end, pv.prepare(p, bpu.getId()));
		return conf;
	}

	public GraphConfiguration<DeformedCuboid> create_configuration(SimpluParameters p, IGeometry geom, BasicPropertyUnit bpu,
			double distReculVoirie, double slope, double hIni, double hMax, double distReculLimi, double slopeProspect,
			double maximalCES) throws Exception {

		return this.create_configuration(p, AdapterFactory.toGeometry(new GeometryFactory(), geom), bpu,
				distReculVoirie, slope, hIni, hMax, distReculLimi, slopeProspect, maximalCES);

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
	public GraphConfiguration<DeformedCuboid> create_configuration(SimpluParameters p, Geometry geom, BasicPropertyUnit bpu,
			double distReculVoirie, double slope, double hIni, double hMax, double distReculLimi, double slopeProspect,
			double maximalCES) {
		// Énergie constante : à la création d'un nouvel objet

		double energyCrea = Double.isNaN(this.energyCreation) ? p.getDouble("energy") : this.energyCreation;

		ConstantEnergy<DeformedCuboid, DeformedCuboid> energyCreation = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				energyCrea);

		// Énergie constante : pondération de l'intersection
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationVolume = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_volume"));
		// Énergie unaire : aire dans la parcelle
		UnaryEnergy<DeformedCuboid> energyVolume = new VolumeUnaryEnergy<DeformedCuboid>();
		// Multiplication de l'énergie d'intersection et de l'aire
		UnaryEnergy<DeformedCuboid> energyVolumePondere = new MultipliesUnaryEnergy<DeformedCuboid>(ponderationVolume,
				energyVolume);

		// On retire de l'énergie de création, l'énergie de l'aire
		UnaryEnergy<DeformedCuboid> u3 = new MinusUnaryEnergy<DeformedCuboid>(energyCreation, energyVolumePondere);

		// //C1 : distance à 2 m par rapport à la voirie
		UnaryEnergy<DeformedCuboid> u6 = new AlignementEnergy<DeformedCuboid>(distReculVoirie, bpu);
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationC1 = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_c1"));
		UnaryEnergy<DeformedCuboid> u6Pondere = new MultipliesUnaryEnergy<DeformedCuboid>(u6, ponderationC1);
		UnaryEnergy<DeformedCuboid> u7 = new MinusUnaryEnergy<DeformedCuboid>(u3, u6Pondere);

		// //C2
		UnaryEnergy<DeformedCuboid> u8 = new ProspectEnergy<DeformedCuboid>(slopeProspect, hIni, bpu);
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationC2 = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_c2"));
		UnaryEnergy<DeformedCuboid> u8Pondere = new MultipliesUnaryEnergy<DeformedCuboid>(u8, ponderationC2);
		UnaryEnergy<DeformedCuboid> u9 = new MinusUnaryEnergy<DeformedCuboid>(u7, u8Pondere);

		// /C3
		UnaryEnergy<DeformedCuboid> u10 = new HauteurEnergy<DeformedCuboid>(hMax);
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationC3 = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_c3"));
		UnaryEnergy<DeformedCuboid> u10pondere = new MultipliesUnaryEnergy<DeformedCuboid>(u10, ponderationC3);
		UnaryEnergy<DeformedCuboid> u11 = new MinusUnaryEnergy<DeformedCuboid>(u9, u10pondere);

		// C4
		UnaryEnergy<DeformedCuboid> u12 = new ProspectEnergy2<DeformedCuboid>(slopeProspect, bpu);
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationC4 = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_c4"));
		UnaryEnergy<DeformedCuboid> u12pondere = new MultipliesUnaryEnergy<DeformedCuboid>(u12, ponderationC4);
		UnaryEnergy<DeformedCuboid> u13 = new MinusUnaryEnergy<DeformedCuboid>(u11, u12pondere);

		// C5
		UnaryEnergy<DeformedCuboid> u14 = new ServitudeVue<DeformedCuboid>(distReculLimi, bpu);
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationC5 = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_c5"));
		UnaryEnergy<DeformedCuboid> u14pondere = new MultipliesUnaryEnergy<DeformedCuboid>(u14, ponderationC5);
		UnaryEnergy<DeformedCuboid> u15 = new MinusUnaryEnergy<DeformedCuboid>(u13, u14pondere);

		// Énergie constante : pondération de la différence
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationDifference = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_difference_ext"));
		// On ajoute l'énergie de différence : la zone en dehors de la parcelle
		UnaryEnergy<DeformedCuboid> u4 = new DifferenceVolumeUnaryEnergy<DeformedCuboid>(geom);
		UnaryEnergy<DeformedCuboid> u5 = new MultipliesUnaryEnergy<DeformedCuboid>(ponderationDifference, u4);
		UnaryEnergy<DeformedCuboid> unaryEnergy = new PlusUnaryEnergy<DeformedCuboid>(u15, u5);

		// Énergie binaire : intersection entre deux rectangles
		ConstantEnergy<DeformedCuboid, DeformedCuboid> c3 = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_volume_inter"));
		BinaryEnergy<DeformedCuboid, DeformedCuboid> b1 = new IntersectionVolumeBinaryEnergy<DeformedCuboid>();
		BinaryEnergy<DeformedCuboid, DeformedCuboid> binaryEnergy = new MultipliesBinaryEnergy<DeformedCuboid, DeformedCuboid>(
				c3, b1);

		// empty initial configuration*/
		GraphConfiguration<DeformedCuboid> conf = new GraphConfiguration<>(unaryEnergy, binaryEnergy);
		return conf;
	}

	/**
	 * Sampler
	 * 
	 * @param p
	 *            les paramètres chargés depuis le fichier xml
	 *            l'enveloppe dans laquelle on génère les positions
	 * @return
	 * @throws Exception
	 */
	public Sampler<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> create_sampler(
			RandomGenerator rng, SimpluParameters p, BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> pred,
			int pos) throws Exception {

		if (pos == NO_POS) {
			return undeterminedSampler(rng, p, bpU, pred);
		}

		IMultiCurve<IOrientableCurve> ims = new GM_MultiCurve<>();

		for (ParcelBoundary scb : bpU.getCadastralParcels().get(0).getBoundaries()) {
			switch (pos) {

			case DROITE:

				if (scb.getSide() == ParcelBoundarySide.RIGHT) {
					ims.addAll(FromGeomToLineString.convert(scb.getGeom()));
				}

				break;

			case GAUCHE:

				if (scb.getSide() == ParcelBoundarySide.LEFT) {
					ims.addAll(FromGeomToLineString.convert(scb.getGeom()));
				}
				break;
			case FOND:

				if (scb.getType() == ParcelBoundaryType.BOT) {
					ims.addAll(FromGeomToLineString.convert(scb.getGeom()));
				}
				break;

			case DEVANT:
				if (scb.getType() == ParcelBoundaryType.ROAD) {
					ims.addAll(FromGeomToLineString.convert(scb.getGeom()));
				}
				break;
			}

		}

		IMultiCurve<IOrientableCurve> iMSShifted = shiftRoad(bpU, 7.3, ims);

		return determinedPos(rng, p, bpU, pred, iMSShifted);

	}

	private static IMultiCurve<IOrientableCurve> shiftRoad(BasicPropertyUnit bPU, double valShiftB,
			IMultiCurve<IOrientableCurve> iMS) {

		IDirectPosition centroidParcel = bPU.getPol2D().centroid();
		IMultiCurve<IOrientableCurve> iMSOut = new GM_MultiCurve<>();
		for (IOrientableCurve oC : iMS) {

			if (oC.isEmpty()) {
				continue;
			}

			IDirectPosition centroidGeom = oC.coord().get(0);
			Vecteur v = new Vecteur(centroidParcel, centroidGeom);

			Vecteur v2 = new Vecteur(oC.coord().get(0), oC.coord().get(oC.coord().size() - 1));
			v2.setZ(0);
			v2.normalise();

			Vecteur vOut = v2.prodVectoriel(new Vecteur(0, 0, 1));

			IGeometry geom = ((IGeometry) oC.clone());

			if (v.prodScalaire(vOut) < 0) {
				vOut = vOut.multConstante(-1);
			}

			IGeometry geom2 = geom.translate(valShiftB * vOut.getX(), valShiftB * vOut.getY(), 0);

			if (!geom2.intersects(bPU.getGeom())) {
				geom2 = geom.translate(-valShiftB * vOut.getX(), -valShiftB * vOut.getY(), 0);
			}

			iMSOut.addAll(FromGeomToLineString.convert(geom2));

		}

		return iMSOut;

	}

	public final static int NO_POS = 0;
	public final static int DROITE = 1;
	public final static int GAUCHE = 2;
	public final static int FOND = 3;
	public final static int DEVANT = 4;

	public Sampler<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> determinedPos(
			RandomGenerator rng, SimpluParameters p, BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> pred,
			IMultiCurve<IOrientableCurve> ims) throws Exception {

		// Un vecteur ?????
		double minlen = Double.isNaN(this.minLengthBox) ? p.getDouble("minlen") : this.minLengthBox;
		double maxlen = Double.isNaN(this.maxLengthBox) ? p.getDouble("maxlen") : this.maxLengthBox;

		double minwid = p.getDouble("minwid");
		double maxwid = p.getDouble("maxwid");

		double minheight = p.getDouble("minheight");
		double maxheight = p.getDouble("maxheight");

		// A priori on redéfini le constructeur de l'objet
		// A priori on redéfini le constructeur de l'objet

		IEnvelope env = bpU.getGeom().envelope();
		// Sampler de naissance
		// UniformBirthInGeom<DeformedCuboid2> birth = new
		// UniformBirthInGeom<DeformedCuboid2>(new
		// DeformedCuboid2(env.minX(),
		// env.minY(), mindim, mindim, minheight, 0), new
		// DeformedCuboid2(env.maxX(),
		// env.maxY(), maxdim,
		// maxdim, maxheight, Math.PI), builder, bpU.getpol2D());

		if (samplingSurface == null) {
			samplingSurface = bpU.getPol2D();
		}

		ObjectBuilder<DeformedCuboid> builder = new ParallelDeformedCuboidBuilder(ims.toArray());

		double[] d = new double[] { env.maxX(), env.maxY(), maxlen, maxwid, maxheight, maxheight, maxheight, maxheight,
				Math.PI };
		double[] v = new double[] { env.minX(), env.minY(), minlen, minwid, minheight, minheight, minheight, minheight,
				0. };

		for (int i = 0; i < d.length; i++) {
			d[i] = d[i] - v[i];
		}

		ParallelDeformedCuboidTransform transformParallel = new ParallelDeformedCuboidTransform(d, v, bpU.getPol2D(),
				ims.toArray());

		// Distribution de poisson
		PoissonDistribution distribution = new PoissonDistribution(rng, p.getDouble("poisson"));

		ParalledDeformedSampler birth = new ParalledDeformedSampler(rng, transformParallel, builder);

		DirectSampler<DeformedCuboid, GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> ds = new DirectRejectionSampler<>(
				distribution, birth, pred);

		// Probabilité de naissance-morts modifications
		List<Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>>> kernels = new ArrayList<>(
				3);

		Variate variate = new Variate(rng);
		NullView<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> nullView = new NullView<>();

		UniformTypeView<DeformedCuboid, GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> pView = new UniformTypeView<>(
				DeformedCuboid.class, builder);

		Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> kernel = new Kernel<>(
				nullView, pView, variate, variate, transformParallel, p.getDouble("pbirth"), p.getDouble("pdeath"),
				"Parallel");

		Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> parallelMovekernel = new Kernel<>(
				pView, pView, variate, variate, new MoveParallelDeformedCuboid(p.getDouble("amplitudeMove")), 0.2, 1.0,
				"SimpleMoveP");
		kernels.add(parallelMovekernel);

		double amplitudeDim = p.getDouble("amplitudeMaxDim");

		Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> parallelHeight = new Kernel<>(
				pView, pView, variate, variate, new ChangeValue(amplitudeDim, 10, 3), 0.2, 1.0, "ChangeLength");
		kernels.add(parallelHeight);

		double amplitudeHeight = p.getDouble("amplitudeHeight");
		Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> parallelHeight1 = new Kernel<>(
				pView, pView, variate, variate, new ChangeHeight1Deformed(amplitudeHeight), 0.2, 1.0, "ChangeHeight1");
		kernels.add(parallelHeight1);

		Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> parallelHeight2 = new Kernel<>(
				pView, pView, variate, variate, new ChangeHeight2Deformed(amplitudeHeight), 0.2, 1.0, "ChangeHeight2");
		kernels.add(parallelHeight2);

		Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> parallelHeight3 = new Kernel<>(
				pView, pView, variate, variate, new ChangeHeight3Deformed(amplitudeHeight), 0.2, 1.0, "ChangeHeight3");
		kernels.add(parallelHeight3);

		Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> parallelHeight4 = new Kernel<>(
				pView, pView, variate, variate, new ChangeHeight4Deformed(amplitudeHeight), 0.2, 1.0, "ChangeHeight4");
		kernels.add(parallelHeight4);

		kernels.add(kernel);

		Sampler<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> s = new GreenSamplerBlockTemperature<>(
				rng, ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);
		return s;
	}

	public Sampler<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> undeterminedSampler(
			RandomGenerator rng, SimpluParameters p, BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> pred) {

		// Un vecteur ?????
		double minlen = Double.isNaN(this.minLengthBox) ? p.getDouble("minlen") : this.minLengthBox;
		double maxlen = Double.isNaN(this.maxLengthBox) ? p.getDouble("maxlen") : this.maxLengthBox;

		double minheight = p.getDouble("minheight");
		double maxheight = p.getDouble("maxheight");

		DeformedCuboidBuilder builder = new DeformedCuboidBuilder();

		IEnvelope env = bpU.getGeom().envelope();
		// Sampler de naissance
		// UniformBirthInGeom<DeformedCuboid2> birth = new
		// UniformBirthInGeom<DeformedCuboid2>(new
		// DeformedCuboid2(env.minX(),
		// env.minY(), mindim, mindim, minheight, 0), new
		// DeformedCuboid2(env.maxX(),
		// env.maxY(), maxdim,
		// maxdim, maxheight, Math.PI), builder, bpU.getpol2D());

		if (samplingSurface == null) {
			samplingSurface = bpU.getPol2D();
		}

		UniformBirth<DeformedCuboid> birth = new UniformBirth<DeformedCuboid>(rng,
				new DeformedCuboid(env.minX(), env.minY(), minlen, minlen, minheight, minheight, minheight, minheight,
						0),
				new DeformedCuboid(env.maxX(), env.maxY(), maxlen, maxlen, maxheight, maxheight, maxheight, maxheight,
						Math.PI),
				builder, TransformToSurface.class, samplingSurface);

		// Distribution de poisson
		PoissonDistribution distribution = new PoissonDistribution(rng, p.getDouble("poisson"));

		DirectSampler<DeformedCuboid, GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> ds = new DirectRejectionSampler<>(
				distribution, birth, pred);

		// Probabilité de naissance-morts modifications
		List<Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>>> kernels = new ArrayList<>(
				3);
		KernelFactory<DeformedCuboid, GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> factory = new KernelFactory<>();
		// TODO Use a KernelProposalRatio to propose only birth when size is 0
		kernels.add(
				factory.make_uniform_birth_death_kernel(rng, builder, birth, p.getDouble("pbirth"), 0.5, "BirthDeath"));

		double amplitudeMove = p.getDouble("amplitudeMove");
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new MoveCuboidDeformed(amplitudeMove), 0.2,
				"Move"));

		double amplitudeMaxDim = p.getDouble("amplitudeMaxDim");
		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeValueDeformed(amplitudeMaxDim, 10, 2), 0.2, "ChgWidth"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeValueDeformed(amplitudeMaxDim, 10, 3), 0.2, "ChgLength"));

		double amplitudeHeight = p.getDouble("amplitudeHeight");

		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeHeight1Deformed(amplitudeHeight),
				0.2, "ChgHeight1"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeHeight2Deformed(amplitudeHeight),
				0.2, "ChgHeight2"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeHeight3Deformed(amplitudeHeight),
				0.2, "ChgHeight3"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeHeight4Deformed(amplitudeHeight),
				0.2, "ChgHeight4"));

		double amplitudeRotate = p.getDouble("amplitudeRotate") * Math.PI / 180;
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new RotateCuboid(amplitudeRotate), 0.2,
				"Rotate"));

		Sampler<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> s = new GreenSamplerBlockTemperature<>(
				rng, ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);
		return s;
	}

}
