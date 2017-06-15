package fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.mix;

import java.util.ArrayList;
import java.util.List;

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
import fr.ign.cogit.simplu3d.experiments.iauidf.predicate.PredicateIAUIDF;
import fr.ign.cogit.simplu3d.experiments.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.experiments.iauidf.tool.BandProduction;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.builder.ParallelCuboidBuilder;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.builder.SimpleCuboidBuilder;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.builder.SimpleCuboidBuilder2;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.ParallelCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.ParallelCuboid2;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.SimpleCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.SimpleCuboid2;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.BasicCuboidOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.sampler.MixCuboidSampler;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.ChangeHeight;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.ChangeLength;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.ChangeWidth;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.MoveCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.RotateCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.ParallelPolygonTransform;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.TransformToSurface;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.parallelCuboid.MoveParallelCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.PrepareVisitors;
import fr.ign.mpp.DirectRejectionSampler;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.UniformTypeView;
import fr.ign.parameters.Parameters;
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
public class MultipleBuildingsCuboid extends BasicCuboidOptimizer<Cuboid> {

	public static boolean ALLOW_INTERSECTING_CUBOID = false;

	private boolean isValid = true;

	public boolean isValid() {
		return isValid;
	}

	public GraphConfiguration<Cuboid> process(BasicPropertyUnit bpu, Parameters p, Environnement env,
			PredicateIAUIDF<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred, Regulation r1,
			Regulation r2, BandProduction bP) throws Exception {
		// Géométrie de l'unité foncière sur laquelle porte la génération (on se permet de faire un petit buffer)
		IGeometry geom = bpu.getPol2D().buffer(1);
		// Définition de la fonction d'optimisation (on optimise en décroissant) relative au volume
		GraphConfiguration<Cuboid> conf = null;
		try {
			conf = create_configuration(p, geom, bpu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Création de l'échantilloneur
		Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> samp = create_sampler(Random.random(), p, bpu, pred, r1, r2, bP);
		if (samp == null) {
			return null;
		}
		// Température
		Schedule<SimpleTemperature> sch = create_schedule(p);
		EndTest end = create_end_test(p);
		System.out.println("*************** " + end.getClass() + " ********************");
		PrepareVisitors<Cuboid> pv = new PrepareVisitors<>(env);
		CompositeVisitor<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> mVisitor = pv.prepare(p, bpu.getId());
		/*
		 * < This is the way to launch the optimization process. Here, the magic happens... >
		 */
		SimulatedAnnealing.optimize(Random.random(), conf, samp, sch, end, mVisitor);
		return conf;
	}

	public GraphConfiguration<Cuboid> create_configuration(Parameters p, IGeometry geom, BasicPropertyUnit bpu) throws Exception {
		return this.create_configuration(p, AdapterFactory.toGeometry(new GeometryFactory(), geom), bpu);
	}

	/**
	 * Création de la configuration.
	 * @param p
	 *            paramètres importés depuis le fichier XML
	 * @param bpu
	 *            l'unité foncière considérée
	 * @return la configuration chargée, c'est à dire la formulation énergétique
	 *         prise en compte
	 */
	@Override
	public GraphConfiguration<Cuboid> create_configuration(Parameters p, Geometry geom, BasicPropertyUnit bpu) {
		if (ALLOW_INTERSECTING_CUBOID) {
			return create_configuration_intersection(p, geom, bpu);
		}
		return create_configuration_no_inter(p, geom, bpu);
	}

	private GraphConfiguration<Cuboid> create_configuration_intersection(Parameters p, Geometry geom, BasicPropertyUnit bpu) {
		// Énergie constante : à la création d'un nouvel objet
		ConstantEnergy<Cuboid, Cuboid> energyCreation = new ConstantEnergy<Cuboid, Cuboid>(p.getDouble("energy"));
		// Énergie constante : pondération de l'intersection
		ConstantEnergy<Cuboid, Cuboid> ponderationVolume = new ConstantEnergy<Cuboid, Cuboid>(p.getDouble("ponderation_volume"));
		// Énergie unaire : aire dans la parcelle
		UnaryEnergy<Cuboid> energyVolume = new VolumeUnaryEnergy<Cuboid>();
		// Multiplication de l'énergie d'intersection et de l'aire
		UnaryEnergy<Cuboid> energyVolumePondere = new MultipliesUnaryEnergy<Cuboid>(ponderationVolume, energyVolume);
		// On retire de l'énergie de création, l'énergie de l'aire
		UnaryEnergy<Cuboid> u3 = new MinusUnaryEnergy<Cuboid>(energyCreation, energyVolumePondere);
		// Énergie binaire : intersection entre deux rectangles
		ConstantEnergy<Cuboid, Cuboid> c3 = new ConstantEnergy<Cuboid, Cuboid>(p.getDouble("ponderation_volume_inter"));
		BinaryEnergy<Cuboid, Cuboid> b1 = new IntersectionVolumeBinaryEnergy<Cuboid>();
		BinaryEnergy<Cuboid, Cuboid> binaryEnergy = new MultipliesBinaryEnergy<Cuboid, Cuboid>(c3, b1);
		// empty initial configuration*/
		return new GraphConfiguration<>(u3, binaryEnergy);
	}

	private GraphConfiguration<Cuboid> create_configuration_no_inter(Parameters p, Geometry geom,
			BasicPropertyUnit bpu) {
		// Énergie constante : à la création d'un nouvel objet
		double energyCrea = p.getDouble("energy");
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
		// empty initial configuration*/
		GraphConfiguration<Cuboid> conf = new GraphConfiguration<>(u3, new ConstantEnergy<Cuboid, Cuboid>(0));
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
	 * @throws Exception
	 */
	public Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> create_sampler(RandomGenerator rng,
			Parameters p, BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred,
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
		System.out.println("minwid " + minwid + " -- maxwid " + maxwid);
		double minheight = p.getDouble("minheight");
		double maxheight = p.getDouble("maxheight");
		double maxheight2 = p.getDouble("maxheight");
		IEnvelope env = bpU.getGeom().envelope();
		// in multi object situations, we need an object builder for each subtype and a sampler for the supertype (end of file)
		
		// Est-ce que l'implémentation dans la première bande se fait parallèlement à la voirie ?
		// On regarde si c'est possible avec la présence d'une limite donnant sur la voirie sinon c'est non
		boolean band1Parallel = !(bP.getLineRoad() == null || bP.getLineRoad().isEmpty());
		// On prépare le vecteur dans lequel on va tirer aléatoirement les boîtes dans la première bande
		double[] v = new double[] { env.minX(), env.minY(), minlen, minwid, minheight, 0. };
		// On regarde si la contrainte de hauteur ne permet pas de réduire l'intervallle des hauteurs
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
		// On calcule la transforme 1 => il n'est pas initialisé s'il n'y a pas de bande 1
		Transform transformBand1 = null;
		ObjectBuilder<Cuboid> builderBand1 = null;
		Class<?> c1 = null;
		if (geomBand1 != null && !geomBand1.isEmpty()) {
			if (band1Parallel) {
				geomBand1 = geomBand1.buffer(-minwid / 2);
				if(! geomBand1.isValid()){
					//Hack to get ValidGeometries
					geomBand1 = geomBand1.buffer(0.1);
				}
				// S'il n'y a qu'une seule bande de constructibilité
				// On peut demander à construire des bâtiments dans la bande derrière le bâtiment aligné à la voirie
				// On va séparer en 2 en fonction de la largeur max du bâtiment
				if (r2 == null) {
					geomBand2 = geomBand1.difference(bP.getLineRoad().buffer(maxwid));
					// Si la bande est toute petite alors, on ne met rien
					if (geomBand2.area() < 5) {
						geomBand2 = null;
					}
				}
				// The center is included in a band equals to half of max allowed width according to alignment line
				geomBand1 = geomBand1.intersection(bP.getLineRoad().buffer(maxwid / 2));
				if (geomBand1 == null || geomBand1.isEmpty()) {
					geomBand1 = null;
				} else {
					builderBand1 = new ParallelCuboidBuilder(bP.getLineRoad().toArray(), 1);
					ParallelPolygonTransform transformBandTemp = new ParallelPolygonTransform(d2, v, geomBand1);
					c1 = ParallelCuboid.class;
					if (transformBandTemp.isValid()) {
						transformBand1 = transformBandTemp;
					} else {
						this.isValid = false;
						System.out.println("Not valid : " + bpU.getCadastralParcels().get(0).getCode());
						return null;
					}
				}
			} else {
				geomBand1 = geomBand1.buffer(-minwid / 2);
				if(! geomBand1.isValid()){
					geomBand1 = geomBand1.buffer(0.1);
				}
				if (geomBand1 == null || geomBand1.isEmpty()) {
					geomBand1 = null;
				} else {
					TransformToSurface tempTransform = new TransformToSurface(d2, v, geomBand1);
					if (!tempTransform.isValid()) {
						this.isValid = false;
						System.out.println("Not valid : " + bpU.getCadastralParcels().get(0).getCode());
						return null;
					}
					transformBand1 = tempTransform;
					builderBand1 = new SimpleCuboidBuilder();
					c1 = SimpleCuboid.class;
				}
			}
		}
		ObjectBuilder<Cuboid> builderBand2 = null;
		boolean band2parallel = false;
		Class<?> c2 = null;
		// On calcule la transforme 2 => il n'est pas initialisé s'il n'y a pas de bande 2
		Transform transformBand2 = null;
		// On récupère la seconde bande
		if (r2 != null) {
			geomBand2 = r2.getGeomBande();
		}
		// On calcule la transforme 2 et le builder 2
		if (r2 != null && geomBand2 != null && !geomBand2.isEmpty()) {
			if (r2 != null && r2.getArt_71() == 2) {
				band2parallel = true;
				List<ParcelBoundary> featC = bpU.getCadastralParcels().get(0).getBoundariesBySide(PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71);
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
				// The center is included in a band equals to half of max allowed width according to alignment line
				geomBand2 = geomBand2.intersection(ims.buffer(maxwid / 2));
				if (geomBand2 == null || geomBand2.isEmpty()) {
					geomBand2 = null;
				} else {
					builderBand2 = new ParallelCuboidBuilder(ims.toArray(), 2);
					ParallelPolygonTransform transformBandTemp = new ParallelPolygonTransform(d, v, geomBand2);
					if (transformBandTemp.isValid()) {
						transformBand2 = transformBandTemp;
					} else {
						this.isValid = false;
						System.out.println("Not valid : " + bpU.getCadastralParcels().get(0).getCode());
						return null;
					}
					c2 = ParallelCuboid2.class;
				}
			} else {
				geomBand2 = geomBand2.buffer(-minwid / 2);
				if(!geomBand2.isValid()){
					geomBand2 = geomBand2.buffer(0.1);
				}
				if (geomBand2 == null || geomBand2.isEmpty()) {
					geomBand2 = null;
				} else if(geomBand2 != null && ! geomBand2.isEmpty()){
					builderBand2 = new SimpleCuboidBuilder2();
					TransformToSurface tempTransform = new TransformToSurface(d2, v, geomBand2);
					if (!tempTransform.isValid()) {
						this.isValid = false;
						System.out.println("Not valid : " + bpU.getCadastralParcels().get(0).getCode());
						return null;
					}
					transformBand2 = tempTransform;
					c2 = SimpleCuboid2.class;
				}
			}
		}
		// Cas où il n'y a qu'une seule bande, mais qu'on implante des bâtiments derrière
		if (r2 == null && geomBand2 != null) {
			builderBand2 = new SimpleCuboidBuilder();
			TransformToSurface tempTransform =  new TransformToSurface(d, v, geomBand2);
			if (!tempTransform.isValid()) {
				this.isValid = false;
				System.out.println("Not valid : " + bpU.getCadastralParcels().get(0).getCode());
				return null;
			}
			transformBand2 = tempTransform;
			c2 = SimpleCuboid.class;
		}
		////////////////////////
		// Préparation des noyaux de modification
		////////////////////////
		// Probabilité de s'implenter en bande 1 ou 2 (si c'est inférieur c'est 2 et supérieur c'est 1)
		double p_simple = 0.5;
		Variate variate = new Variate(rng);
		// Probabilité de naissance-morts modifications
		List<Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> kernels = new ArrayList<>(3);
		NullView<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> nullView = new NullView<>();
		// Noyau pour la bande 1
		if (transformBand1 != null) {
			List<Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> lKernelsBand1 = new ArrayList<>();
			lKernelsBand1 = getBande1Kernels(variate, nullView, p, transformBand1, builderBand1, band1Parallel);
			kernels.addAll(lKernelsBand1);
		} else {
			p_simple = 1; // pas de transform on ne sera jamais dans la bande 1
		}
		// Noyaux pour la bande 1
		if (r2 != null && transformBand2 != null) {
			List<Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> lKernelsBand2 = new ArrayList<>();
			lKernelsBand2 = getBande2Kernels(variate, nullView, p, transformBand2, builderBand2, band2parallel);
			kernels.addAll(lKernelsBand2);
		} else if (r2 == null && transformBand2 != null) {
			// Cas une seule bande et on bâtie derrière
			List<Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> lKernelsBand2 = new ArrayList<>();
			lKernelsBand2 = getBande1Kernels(variate, nullView, p, transformBand2, builderBand2, false);
			kernels.addAll(lKernelsBand2);
		} else {
			p_simple = 0; // pas de transform on ne sera jamais dans la bande 2
		}
		// Si on ne peut pas construire dans la deuxième bande ni dans la première ça sert à rien de continue
		if (kernels.isEmpty()) {
			return null;
		}
		// When direct sampling (solomon, etc.), what is the prob to choose a simple cuboid
		// CuboidSampler objectSampler = new CuboidSampler(rng, p_simple, transformSimple, transformParallel);
		MixCuboidSampler objectSampler = new MixCuboidSampler(rng, p_simple, transformBand1, transformBand2, builderBand1, builderBand2, c1, c2);
		// poisson distribution
		PoissonDistribution distribution = new PoissonDistribution(rng, p.getDouble("poisson"));
		DirectSampler<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> ds = new DirectRejectionSampler<>(
				distribution, objectSampler, pred);
		Acceptance<SimpleTemperature> acceptance = new MetropolisAcceptance<>();
		return new GreenSamplerBlockTemperature<>(rng, ds, acceptance, kernels);
	}

	private static List<Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> getBande1Kernels(
			Variate variate, NullView<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> nullView,
			Parameters p, Transform transform, ObjectBuilder<Cuboid> pbuilder, boolean parallel) {
		List<Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> kernels = new ArrayList<>();
		// Kernel de naissance
		// TODO Use a KernelProposalRatio to propose only birth when size is 0
		UniformTypeView<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pView = null;
		if (parallel) {
			pView = new UniformTypeView<>(ParallelCuboid.class, pbuilder);
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> kernel2 = new Kernel<>(nullView, pView,
					variate, variate, transform, p.getDouble("pbirthdeath"), p.getDouble("pbirth"), "Parallel");
			kernels.add(kernel2);
			if ((p.getDouble("maxheight") - p.getDouble("minheight")) > 0.2) {
				double amplitudeHeight = p.getDouble("amplitudeHeight");
				Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> parallelHeight = new Kernel<>(pView,
						pView, variate, variate, new ChangeValue(amplitudeHeight, 5, 3), 0.2, 1.0, "ChgHeightP");
				kernels.add(parallelHeight);
			}
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> parallelMovekernel = new Kernel<>(pView,
					pView, variate, variate, new MoveParallelCuboid(p.getDouble("amplitudeMove")), 0.2, 1.0,
					"SimpleMoveP");
			kernels.add(parallelMovekernel);
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> parallelLength = new Kernel<>(pView,
					pView, variate, variate, new ChangeValue(p.getDouble("amplitudeMaxDim"), 5, 2), 0.2, 1.0,
					"ChgLengthP");
			kernels.add(parallelLength);
		} else {
			pView = new UniformTypeView<>(SimpleCuboid.class, pbuilder);
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> kernel1 = new Kernel<>(nullView, pView,
					variate, variate, transform, p.getDouble("pbirthdeath"), p.getDouble("pbirth"), "Simple");
			kernels.add(kernel1);
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> simpleMovekernel = new Kernel<>(pView,
					pView, variate, variate, new MoveCuboid(p.getDouble("amplitudeMove")), 0.2, 1.0, "SimpleMove");
			kernels.add(simpleMovekernel);
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> simpleRotatekernel = new Kernel<>(pView,
					pView, variate, variate, new RotateCuboid(p.getDouble("amplitudeRotate") * Math.PI / 180), 0.2, 1.0,
					"RotateS");
			kernels.add(simpleRotatekernel);
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> simpleWidthkernel = new Kernel<>(pView,
					pView, variate, variate, new ChangeWidth(p.getDouble("amplitudeMaxDim")), 0.2, 1.0, "ChgWidthS");
			kernels.add(simpleWidthkernel);
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> simpleLength = new Kernel<>(pView, pView,
					variate, variate, new ChangeLength(p.getDouble("amplitudeMaxDim")), 0.2, 1.0, "ChgLengthS");
			kernels.add(simpleLength);
			if ((p.getDouble("maxheight") - p.getDouble("minheight")) > 0.2) {
				double amplitudeHeight = p.getDouble("amplitudeHeight");
				Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> simpleHeight = new Kernel<>(pView,
						pView, variate, variate, new ChangeHeight(amplitudeHeight), 0.2, 1.0, "ChgHeightS");
				kernels.add(simpleHeight);
			}
		}
		return kernels;
	}

	private static List<Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> getBande2Kernels(
			Variate variate, NullView<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> nullView,
			Parameters p, Transform transformSimple, ObjectBuilder<Cuboid> sbuilder, boolean parallel) {
		List<Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> kernels = new ArrayList<>();
		if (parallel) {
			UniformTypeView<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pView = new UniformTypeView<>(
					ParallelCuboid2.class, sbuilder);
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> kernel2 = new Kernel<>(nullView, pView,
					variate, variate, transformSimple, p.getDouble("pbirthdeath"), p.getDouble("pbirth"), "Parallel2");
			kernels.add(kernel2);
			if ((p.getDouble("maxheight") - p.getDouble("minheight")) > 0.2) {
				double amplitudeHeight = p.getDouble("amplitudeHeight");
				Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> parallelHeight = new Kernel<>(pView,
						pView, variate, variate, new ChangeValue(amplitudeHeight, 5, 3), 0.2, 1.0, "ChgHeightP2");
				kernels.add(parallelHeight);
			}
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> parallelMovekernel = new Kernel<>(pView,
					pView, variate, variate, new MoveParallelCuboid(p.getDouble("amplitudeMove")), 0.2, 1.0,
					"SimpleMoveP2");
			kernels.add(parallelMovekernel);
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> parallelLength = new Kernel<>(pView,
					pView, variate, variate, new ChangeValue(p.getDouble("amplitudeMaxDim"), 5, 2), 0.2, 1.0,
					"ChgLengthP2");
			kernels.add(parallelLength);
		} else {
			UniformTypeView<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> sView = new UniformTypeView<>(
					SimpleCuboid2.class, sbuilder);
			// we also need one birthdeath kernel per object subtype
			// TODO Use a KernelProposalRatio to propose only birth when size is 0
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> kernel1 = new Kernel<>(nullView, sView,
					variate, variate, transformSimple, p.getDouble("pbirthdeath"), p.getDouble("pbirth"), "Simple");
			kernels.add(kernel1);

			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> simpleMovekernel = new Kernel<>(sView,
					sView, variate, variate, new MoveCuboid(p.getDouble("amplitudeMove")), 0.2, 1.0, "SimpleMove");
			kernels.add(simpleMovekernel);

			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> simpleRotatekernel = new Kernel<>(sView,
					sView, variate, variate, new RotateCuboid(p.getDouble("amplitudeRotate") * Math.PI / 180), 0.2, 1.0,
					"RotateS");
			kernels.add(simpleRotatekernel);
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> simpleWidthkernel = new Kernel<>(sView,
					sView, variate, variate, new ChangeWidth(p.getDouble("amplitudeMaxDim")), 0.2, 1.0, "ChgWidthS");
			kernels.add(simpleWidthkernel);
			Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> simpleLength = new Kernel<>(sView, sView,
					variate, variate, new ChangeLength(p.getDouble("amplitudeMaxDim")), 0.2, 1.0, "ChgLengthS");
			kernels.add(simpleLength);
			if ((p.getDouble("maxheight") - p.getDouble("minheight")) > 0.2) {
				double amplitudeHeight = p.getDouble("amplitudeHeight");
				Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> simpleHeight = new Kernel<>(sView,
						sView, variate, variate, new ChangeHeight(amplitudeHeight), 0.2, 1.0, "ChgHeightS");
				kernels.add(simpleHeight);
			}
		}
		return kernels;
	}
}
