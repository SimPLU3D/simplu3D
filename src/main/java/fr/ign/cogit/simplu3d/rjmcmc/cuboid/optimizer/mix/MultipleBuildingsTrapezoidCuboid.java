package fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.mix;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.util.SimpluParameters;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToLineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.experiments.enau.transformation.RotateCuboid;
import fr.ign.cogit.simplu3d.experiments.iauidf.predicate.PredicateIAUIDF;
import fr.ign.cogit.simplu3d.experiments.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.experiments.iauidf.tool.BandProduction;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.builder.mix.ParallelCuboidAbstractSimpleBuildingBuilder;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.builder.mix.ParallelRightTrapezoidAbstractSimpleBuildingBuilder2;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.builder.mix.SimpleCuboidAbstractSimpleBuildingBuilder;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.builder.mix.SimpleCuboidAbstractSimpleBuildingBuilder2;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.ParallelCuboid2;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.SimpleCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.SimpleCuboid2;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.sampler.MixAbstractBuildingSampler;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.ChangeHeight;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.ChangeLength;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.ChangeWidth;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.MoveCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.ParallelPolygonTransform;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.TransformToSurface;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.optimizer.DefaultSimPLU3DOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.generic.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.PrepareVisitors;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry.ParallelTrapezoid2;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.transform.ParallelTrapezoidTransform;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.transformation.MoveParallelRightTrapezoid;
import fr.ign.mpp.DirectRejectionSampler;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.UniformTypeView;
import fr.ign.random.Random;
import fr.ign.rjmcmc.acceptance.Acceptance;
import fr.ign.rjmcmc.acceptance.MetropolisAcceptance;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;
import fr.ign.rjmcmc.distribution.PoissonDistribution;
import fr.ign.rjmcmc.energy.BinaryEnergy;
import fr.ign.rjmcmc.energy.ConstantEnergy;
import fr.ign.rjmcmc.energy.MinusUnaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesBinaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesUnaryEnergy;
import fr.ign.rjmcmc.energy.UnaryEnergy;
import fr.ign.rjmcmc.kernel.ChangeValue;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.kernel.NullView;
import fr.ign.rjmcmc.kernel.Transform;
import fr.ign.rjmcmc.kernel.Variate;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.SimulatedAnnealing;
import fr.ign.simulatedannealing.endtest.EndTest;
import fr.ign.simulatedannealing.schedule.Schedule;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;
import fr.ign.simulatedannealing.visitor.CompositeVisitor;

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
public class MultipleBuildingsTrapezoidCuboid extends DefaultSimPLU3DOptimizer<AbstractSimpleBuilding> {

	public static boolean ALLOW_INTERSECTING_AbstractSimpleBuilding = false;

	public GraphConfiguration<AbstractSimpleBuilding> process(
			BasicPropertyUnit bpu, SimpluParameters p, Environnement env,
			PredicateIAUIDF<AbstractSimpleBuilding, GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> pred,
			Regulation r1, Regulation r2, BandProduction bP) throws Exception {

		// Géométrie de l'unité foncière sur laquelle porte la génération (on se
		// permet de faire un petit buffer)
		IGeometry geom = bpu.getPol2D().buffer(1);

		// Définition de la fonction d'optimisation (on optimise en décroissant)
		// relative au volume
		GraphConfiguration<AbstractSimpleBuilding> conf = null;

		try {
			conf = create_configuration(p, geom, bpu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		RandomGenerator random = new MersenneTwister(42);
		
		
		// Création de l'échantilloneur
		Sampler<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> samp = create_sampler(
				random, p, bpu, pred, r1, r2, bP);
		if (samp == null) {
			return null;
		}

		// Température
		Schedule<SimpleTemperature> sch = create_schedule(p);

		EndTest end = create_end_test(p);

		PrepareVisitors<AbstractSimpleBuilding> pv = new PrepareVisitors<>(env);
		CompositeVisitor<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> mVisitor = pv
				.prepare(p, bpu.getId());
		/*
		 * < This is the way to launch the optimization process. Here, the magic
		 * happen... >
		 */
		SimulatedAnnealing.optimize(Random.random(), conf, samp, sch, end, mVisitor);
		return conf;
	}

	public GraphConfiguration<AbstractSimpleBuilding> create_configuration(SimpluParameters p, IGeometry geom,
			BasicPropertyUnit bpu) throws Exception {
		return this.create_configuration(p, AdapterFactory.toGeometry(new GeometryFactory(), geom), bpu);
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
	public GraphConfiguration<AbstractSimpleBuilding> create_configuration(SimpluParameters p, Geometry geom,
			BasicPropertyUnit bpu) {

		if (ALLOW_INTERSECTING_AbstractSimpleBuilding) {
			return create_configuration_intersection(p, geom, bpu);
		}

		return create_configuration_no_inter(p, geom, bpu);

	}

	private GraphConfiguration<AbstractSimpleBuilding> create_configuration_intersection(SimpluParameters p, Geometry geom,
			BasicPropertyUnit bpu) {
		// Énergie constante : à la création d'un nouvel objet
		ConstantEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding> energyCreation = new ConstantEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding>(
				p.getDouble("energy"));
		// Énergie constante : pondération de l'intersection
		ConstantEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding> ponderationVolume = new ConstantEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding>(
				p.getDouble("ponderation_volume"));
		// Énergie unaire : aire dans la parcelle
		UnaryEnergy<AbstractSimpleBuilding> energyVolume = new VolumeUnaryEnergy<AbstractSimpleBuilding>();
		// Multiplication de l'énergie d'intersection et de l'aire
		UnaryEnergy<AbstractSimpleBuilding> energyVolumePondere = new MultipliesUnaryEnergy<AbstractSimpleBuilding>(
				ponderationVolume, energyVolume);

		// On retire de l'énergie de création, l'énergie de l'aire
		UnaryEnergy<AbstractSimpleBuilding> u3 = new MinusUnaryEnergy<AbstractSimpleBuilding>(energyCreation,
				energyVolumePondere);

		// Énergie binaire : intersection entre deux rectangles
		ConstantEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding> c3 = new ConstantEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding>(
				p.getDouble("ponderation_volume_inter"));
		BinaryEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding> b1 = new IntersectionVolumeBinaryEnergy<AbstractSimpleBuilding>();
		BinaryEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding> binaryEnergy = new MultipliesBinaryEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding>(
				c3, b1);
		// empty initial configuration*/
		GraphConfiguration<AbstractSimpleBuilding> conf = new GraphConfiguration<>(u3, binaryEnergy);
		return conf;
	}

	private GraphConfiguration<AbstractSimpleBuilding> create_configuration_no_inter(SimpluParameters p, Geometry geom,
			BasicPropertyUnit bpu) {
		// Énergie constante : à la création d'un nouvel objet
		double energyCrea = p.getDouble("energy");
		ConstantEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding> energyCreation = new ConstantEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding>(
				energyCrea);
		// Énergie constante : pondération de l'intersection
		ConstantEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding> ponderationVolume = new ConstantEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding>(
				p.getDouble("ponderation_volume"));
		// Énergie unaire : aire dans la parcelle
		UnaryEnergy<AbstractSimpleBuilding> energyVolume = new VolumeUnaryEnergy<AbstractSimpleBuilding>();
		// Multiplication de l'énergie d'intersection et de l'aire
		UnaryEnergy<AbstractSimpleBuilding> energyVolumePondere = new MultipliesUnaryEnergy<AbstractSimpleBuilding>(
				ponderationVolume, energyVolume);
		// On retire de l'énergie de création, l'énergie de l'aire
		UnaryEnergy<AbstractSimpleBuilding> u3 = new MinusUnaryEnergy<AbstractSimpleBuilding>(energyCreation,
				energyVolumePondere);
		// empty initial configuration*/
		GraphConfiguration<AbstractSimpleBuilding> conf = new GraphConfiguration<>(u3,
				new ConstantEnergy<AbstractSimpleBuilding, AbstractSimpleBuilding>(0));
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
	public static Sampler<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> create_sampler(
			RandomGenerator rng, SimpluParameters p, BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> pred,
			Regulation r1, Regulation r2, BandProduction bP) throws Exception {
		////////////////////////
		// On prépare les paramètres de tirage aléatoire des boîtes
		////////////////////////

		// On récupère les intervalles dans lesquels on va tirer aléatoirement
		// les carac des boîtes
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

		// Est-ce que l'implémentation dans la première bande se fait
		// parallèlement à la voirie ?
		// On regarde si c'est possible avec la présence d'une limite donnant
		// sur la voirie sinon c'est non
		boolean band1Parallel = !(bP.getLineRoad() == null || bP.getLineRoad().isEmpty());

		// On prépare le vecteur dans lequel on va tirer aléatoirement les
		// boîtes dans la première bande
		double[] v = new double[] { env.minX(), env.minY(), minlen, minwid, minheight, 0. };
		double[] v2 = new double[] { env.minX(), env.minY(), minlen, minwid, minheight, 0. };
		
		// On regarde si la contrainte de hauteur ne permet pas de réduire
		// l'intervallle des hauteurs
		if (r1 != null && r1.getArt_10_m() != 99) {
			maxheight = Math.min(maxheight, r1.getArt_10_m());
		}

		double[] d = new double[] { env.maxX(), env.maxY(), maxlen, maxwid, maxheight, Math.PI };

		if (r2 != null && r2.getArt_10_m() != 99) {
			maxheight2 = Math.min(maxheight2, r2.getArt_10_m());
		}

		// On répète la même chose pour la seconde
		double[] d2 = new double[] { env.maxX(), env.maxY(), maxlen, maxwid, maxheight2, Math.PI };

		for (int i = 0; i < d.length; i++) {
			d[i] = d[i] - v[i];
		}

		for (int i = 0; i < d.length; i++) {
			d2[i] = d2[i] - v[i];
		}

		////////////////////////
		// On prépare les zones dans lesquelles les boîtes seront tirées
		////////////////////////

		// On récupère la bande numéro 1
		IGeometry geomBand1 = r1.getGeomBande();
		IGeometry geomBand2 = null;

		////////////////////////
		// On prépare les transforme
		////////////////////////

		// On calcule la transforme 1 => il n'est pas initialisé s'il n'y a pas
		// de bande 1
		Transform transformBand1 = null;
		ObjectBuilder<AbstractSimpleBuilding> builderBand1 = null;
		Class<? extends AbstractSimpleBuilding> c1 = null;
		if (geomBand1 != null && !geomBand1.isEmpty()) {
			if (band1Parallel) {

				// S'il n'y a qu'une seule bande de constructibilité
				// On peut demander à construire des bâtiments dans la bande
				// derrière
				// le bâtiment aligné à la voirie
				// On va séparer en 2 en fonction de la largeur max du bâtiment
				if (r2 == null) {

					geomBand2 = geomBand1.difference(bP.getLineRoad().buffer(maxwid));

					// Si la bande est toute petite alors, on ne met rien
					if (geomBand2.area() < 5) {
						geomBand2 = null;
					}

				}
				v2[2] = v2[2]/2;
				d2[2] = d2[2]/2 + v2[2];
				d2[5] = 1;
				// The center is included in a band equals to half of max
				// allowed width according to alignment line

				geomBand1 = geomBand1.intersection(bP.getLineRoad().buffer(d2[2] * 2));
				geomBand1 = geomBand1.difference(bP.getLineRoad().buffer(v2[2]));

				builderBand1 = new ParallelRightTrapezoidAbstractSimpleBuildingBuilder2(bP.getLineRoad().toArray(),
						geomBand1);
				transformBand1 = new ParallelTrapezoidTransform(d2, v, geomBand1);
				c1 = ParallelTrapezoid2.class;
			} else {

				geomBand1 = geomBand1.buffer(-minwid / 2);

				transformBand1 = new TransformToSurface(d2, v, geomBand1);
				builderBand1 = new SimpleCuboidAbstractSimpleBuildingBuilder();
				c1 = SimpleCuboid.class;
			}
		}

		ObjectBuilder<AbstractSimpleBuilding> builderBand2 = null;

		boolean band2parallel = false;
		Class<? extends AbstractSimpleBuilding> c2 = null;

		// On calcule la transforme 2 => il n'est pas initialisé s'il n'y a pas
		// de bande 2
		Transform transformBand2 = null;

		// On récupère la seconde bande

		if (r2 != null) {
			geomBand2 = r2.getGeomBande();
		}

		// On calcule la transforme 2 et le builder 2
		if (r2 != null && geomBand2 != null && !geomBand2.isEmpty()) {

			if (r2 != null && r2.getArt_71() == 2) {

				band2parallel = true;

				List<ParcelBoundary> featC = bpU.getCadastralParcels().get(0)
						.getBoundariesBySide(PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71);
				IMultiCurve<IOrientableCurve> ims = new GM_MultiCurve<>();
				for (ParcelBoundary s : featC) {
					ims.addAll(FromGeomToLineString.convert(s.getGeom()));
				}

				// On se colle partout si on peut pas déterminer de côté.
				if (ims.isEmpty()) {

					featC = bpU.getCadastralParcels().get(0).getBoundaries();

					for (ParcelBoundary s : featC) {
						ims.addAll(FromGeomToLineString.convert(s.getGeom()));
					}

				}

				// The center is included in a band equals to half of max
				// allowed width according to alignment line
				geomBand2 = geomBand2.intersection(ims.buffer(maxwid / 2));

				builderBand2 = new ParallelCuboidAbstractSimpleBuildingBuilder(ims.toArray(), 2);
				transformBand2 = new ParallelPolygonTransform(d, v, geomBand2);
				c2 = ParallelCuboid2.class;

			} else {

				geomBand2 = geomBand2.buffer(-minwid / 2);

				builderBand2 = new SimpleCuboidAbstractSimpleBuildingBuilder2();
				transformBand2 = new TransformToSurface(d, v, geomBand2);
				c2 = SimpleCuboid2.class;
			}
		}

		// Cas où il n'y a qu'une seule bande, mais qu'on implante des bâtiments
		// derrière
		if (r2 == null && geomBand2 != null) {
			builderBand2 = new SimpleCuboidAbstractSimpleBuildingBuilder();
			transformBand2 = new TransformToSurface(d, v, geomBand2);
			c2 = SimpleCuboid.class;
		}

		////////////////////////
		// Préparation des noyaux de modification
		////////////////////////

		// Probabilité de s'implenter en bande 1 ou 2 (si c'est inférieur c'est
		// 2 et supérieur c'est 1)
		double p_simple = 0.5;

		Variate variate = new Variate(rng);
		// Probabilité de naissance-morts modifications
		List<Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>>> kernels = new ArrayList<>(
				3);
		NullView<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> nullView = new NullView<>();

		// Noyau pour la bande 1
		if (transformBand1 != null) {
			List<Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>>> lKernelsBand1 = new ArrayList<>();
			lKernelsBand1 = getBande1Kernels(variate, nullView, p, transformBand1, builderBand1, band1Parallel);
			kernels.addAll(lKernelsBand1);
		} else {
			p_simple = 1; // pas de transform on ne sera jamais dans la bande 1
		}

		// Noyaux pour la bande 1
		if (r2 != null && transformBand2 != null) {
			List<Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>>> lKernelsBand2 = new ArrayList<>();
			lKernelsBand2 = getBande2Kernels(variate, nullView, p, transformBand2, builderBand2, band2parallel);
			kernels.addAll(lKernelsBand2);
		} else if (r2 == null && transformBand2 != null) { // Cas une seule
															// bande et on bâtie
															// derrière

			List<Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>>> lKernelsBand2 = new ArrayList<>();
			lKernelsBand2 = getBande1Kernels(variate, nullView, p, transformBand2, builderBand2, false);
			kernels.addAll(lKernelsBand2);

		} else {
			p_simple = 0; // pas de transform on ne sera jamais dans la bande 2
		}


		// Si on ne peut pas construire dans la deuxième bande ni dans la
		// première ça sert à rien de continue
		if (kernels.isEmpty()) {
			return null;
		}

		// When direct sampling (solomon, etc.), what is the prob to choose a
		// simple AbstractSimpleBuilding

		// AbstractSimpleBuildingSampler objectSampler = new
		// AbstractSimpleBuildingSampler(rng, p_simple,
		// transformSimple, transformParallel);
		MixAbstractBuildingSampler objectSampler = new MixAbstractBuildingSampler(rng, p_simple, transformBand1,
				transformBand2, builderBand1, builderBand2, c1, c2);

		// poisson distribution
		PoissonDistribution distribution = new PoissonDistribution(rng, p.getDouble("poisson"));
		DirectSampler<AbstractSimpleBuilding, GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> ds = new DirectRejectionSampler<>(
				distribution, objectSampler, pred);

		Acceptance<SimpleTemperature> acceptance = new MetropolisAcceptance<>();
		Sampler<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> s = new GreenSamplerBlockTemperature<>(
				rng, ds, acceptance, kernels);
		return s;
	}

	private static List<Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>>> getBande1Kernels(
			Variate variate,
			NullView<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> nullView,
			SimpluParameters p, Transform transform, ObjectBuilder<AbstractSimpleBuilding> pbuilder, boolean parallel) {
		List<Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>>> kernels = new ArrayList<>();
		// Kernel de naissance
		// TODO Use a KernelProposalRatio to propose only birth when size is 0
		UniformTypeView<AbstractSimpleBuilding, GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> pView = null;

		if (parallel) {
			pView = new UniformTypeView<>(ParallelTrapezoid2.class, pbuilder);

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> kernel2 = new Kernel<>(
					nullView, pView, variate, variate, transform, p.getDouble("pbirthdeath"), p.getDouble("pbirth"),
					"Parallel");
			kernels.add(kernel2);

			double amplitudeMove = p.getDouble("amplitudeMove");

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> simpleMovekernel = new Kernel<>(
					pView, pView, variate, variate, new MoveParallelRightTrapezoid(amplitudeMove), 0.2,
					1.0, "SimpleMove");
			kernels.add(simpleMovekernel);

			double amplitudeMaxDim = p.getDouble("amplitudeMaxDim");

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> changeLenghtKernel = new Kernel<>(
					pView, pView, variate, variate, new ChangeValue(amplitudeMaxDim, 6, 2), 0.2, 1.0, "ChgLength");
			kernels.add(changeLenghtKernel);

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> changeWidthKernel = new Kernel<>(
					pView, pView, variate, variate, new ChangeValue(amplitudeMaxDim, 6, 3), 0.2, 1.0, "ChgWidth");
			kernels.add(changeWidthKernel);

			if ((p.getDouble("maxheight") - p.getDouble("minheight")) > 0.2) {
				double amplitudeHeight = p.getDouble("amplitudeHeight");

				Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> changeHeight = new Kernel<>(
						pView, pView, variate, variate, new ChangeValue(amplitudeHeight, 6, 4), 0.2, 1.0, "ChgWidth");
				kernels.add(changeHeight);

			}

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> changeOrient = new Kernel<>(
					pView, pView, variate, variate, new ChangeValue(0.1, 6, 5), 0.2, 1.0, "Rotate");
			kernels.add(changeOrient);

		} else {
			pView = new UniformTypeView<>(SimpleCuboid.class, pbuilder);

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> kernel1 = new Kernel<>(
					nullView, pView, variate, variate, transform, p.getDouble("pbirthdeath"), p.getDouble("pbirth"),
					"Simple");
			kernels.add(kernel1);

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> simpleMovekernel = new Kernel<>(
					pView, pView, variate, variate, new MoveCuboid(p.getDouble("amplitudeMove")), 0.2, 1.0,
					"SimpleMove");
			kernels.add(simpleMovekernel);

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> simpleRotatekernel = new Kernel<>(
					pView, pView, variate, variate, new RotateCuboid(p.getDouble("amplitudeRotate") * Math.PI / 180),
					0.2, 1.0, "RotateS");
			kernels.add(simpleRotatekernel);

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> simpleWidthkernel = new Kernel<>(
					pView, pView, variate, variate, new ChangeWidth(p.getDouble("amplitudeMaxDim")), 0.2, 1.0,
					"ChgWidthS");
			kernels.add(simpleWidthkernel);

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> simpleLength = new Kernel<>(
					pView, pView, variate, variate, new ChangeLength(p.getDouble("amplitudeMaxDim")), 0.2, 1.0,
					"ChgLengthS");
			kernels.add(simpleLength);

			if ((p.getDouble("maxheight") - p.getDouble("minheight")) > 0.2) {

				double amplitudeHeight = p.getDouble("amplitudeHeight");
				Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> simpleHeight = new Kernel<>(
						pView, pView, variate, variate, new ChangeHeight(amplitudeHeight), 0.2, 1.0, "ChgHeightS");
				kernels.add(simpleHeight);
			}
		}

		return kernels;

	}

	private static List<Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>>> getBande2Kernels(
			Variate variate,
			NullView<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> nullView,
			SimpluParameters p, Transform transformSimple, ObjectBuilder<AbstractSimpleBuilding> sbuilder, boolean parallel) {

		List<Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>>> kernels = new ArrayList<>();

		if (parallel) {

			UniformTypeView<AbstractSimpleBuilding, GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> pView = new UniformTypeView<>(
					ParallelCuboid2.class, sbuilder);
			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> kernel2 = new Kernel<>(
					nullView, pView, variate, variate, transformSimple, p.getDouble("pbirthdeath"),
					p.getDouble("pbirth"), "Parallel2");
			kernels.add(kernel2);

			if ((p.getDouble("maxheight") - p.getDouble("minheight")) > 0.2) {
				double amplitudeHeight = p.getDouble("amplitudeHeight");
				Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> parallelHeight = new Kernel<>(
						pView, pView, variate, variate, new ChangeValue(amplitudeHeight, 5, 3), 0.2, 1.0,
						"ChgHeightP2");
				kernels.add(parallelHeight);
			}

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> parallelMovekernel = new Kernel<>(
					pView, pView, variate, variate, new MoveCuboid(p.getDouble("amplitudeMove")), 0.2, 1.0,
					"SimpleMoveP2");
			kernels.add(parallelMovekernel);

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> parallelLength = new Kernel<>(
					pView, pView, variate, variate, new ChangeValue(p.getDouble("amplitudeMaxDim"), 5, 2), 0.2, 1.0,
					"ChgLengthP2");
			kernels.add(parallelLength);

		} else {
			UniformTypeView<AbstractSimpleBuilding, GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> sView = new UniformTypeView<>(
					SimpleCuboid2.class, sbuilder);

			// we also need one birthdeath kernel per object subtype
			// TODO Use a KernelProposalRatio to propose only birth when size is
			// 0
			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> kernel1 = new Kernel<>(
					nullView, sView, variate, variate, transformSimple, p.getDouble("pbirthdeath"),
					p.getDouble("pbirth"), "Simple");
			kernels.add(kernel1);

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> simpleMovekernel = new Kernel<>(
					sView, sView, variate, variate, new MoveCuboid(p.getDouble("amplitudeMove")), 0.2, 1.0,
					"SimpleMove");
			kernels.add(simpleMovekernel);

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> simpleRotatekernel = new Kernel<>(
					sView, sView, variate, variate, new RotateCuboid(p.getDouble("amplitudeRotate") * Math.PI / 180),
					0.2, 1.0, "RotateS");
			kernels.add(simpleRotatekernel);

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> simpleWidthkernel = new Kernel<>(
					sView, sView, variate, variate, new ChangeWidth(p.getDouble("amplitudeMaxDim")), 0.2, 1.0,
					"ChgWidthS");
			kernels.add(simpleWidthkernel);

			Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> simpleLength = new Kernel<>(
					sView, sView, variate, variate, new ChangeLength(p.getDouble("amplitudeMaxDim")), 0.2, 1.0,
					"ChgLengthS");
			kernels.add(simpleLength);

			if ((p.getDouble("maxheight") - p.getDouble("minheight")) > 0.2) {

				double amplitudeHeight = p.getDouble("amplitudeHeight");
				Kernel<GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> simpleHeight = new Kernel<>(
						sView, sView, variate, variate, new ChangeHeight(amplitudeHeight), 0.2, 1.0, "ChgHeightS");
				kernels.add(simpleHeight);
			}

		}

		return kernels;
	}

}
