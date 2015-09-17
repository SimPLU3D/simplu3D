package fr.ign.cogit.simplu3d.enau.optimizer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;

import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.operation.distance.DistanceOp;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToLineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.enau.energy.AlignementEnergy;
import fr.ign.cogit.simplu3d.enau.energy.HauteurEnergy;
import fr.ign.cogit.simplu3d.enau.energy.ProspectEnergy;
import fr.ign.cogit.simplu3d.enau.energy.ProspectEnergy2;
import fr.ign.cogit.simplu3d.enau.energy.ServitudeVue;
import fr.ign.cogit.simplu3d.enau.geometry.DeformedCuboid;
import fr.ign.cogit.simplu3d.enau.geometry.ParallelDeformedCuboid;
import fr.ign.cogit.simplu3d.enau.geometry.ParallelDeformedCuboidTransform;
import fr.ign.cogit.simplu3d.enau.transformation.ChangeHeight1Deformed;
import fr.ign.cogit.simplu3d.enau.transformation.ChangeHeight2Deformed;
import fr.ign.cogit.simplu3d.enau.transformation.ChangeHeight3Deformed;
import fr.ign.cogit.simplu3d.enau.transformation.ChangeHeight4Deformed;
import fr.ign.cogit.simplu3d.enau.transformation.ChangeValueDeformed;
import fr.ign.cogit.simplu3d.enau.transformation.MoveCuboidDeformed;
import fr.ign.cogit.simplu3d.enau.transformation.MoveParallelDeformedCuboid;
import fr.ign.cogit.simplu3d.enau.transformation.RotateCuboid;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.endTest.StabilityEndTest;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.energy.cuboid2.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.energy.cuboid2.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.energy.cuboid2.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.ParallelCuboid2;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.SimpleCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.ChangeValue;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.MoveParallelCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.TransformToSurface;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.visitor.CSVendStats;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.visitor.CSVvisitor;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.visitor.CountVisitor;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.visitor.FilmVisitor;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.visitor.ShapefileVisitorCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.visitor.StatsVisitor;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.visitor.ViewerVisitor;
import fr.ign.mpp.DirectRejectionSampler;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.kernel.KernelFactory;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.ObjectSampler;
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
import fr.ign.simulatedannealing.endtest.MaxIterationEndTest;
import fr.ign.simulatedannealing.schedule.GeometricSchedule;
import fr.ign.simulatedannealing.schedule.Schedule;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;
import fr.ign.simulatedannealing.visitor.CompositeVisitor;
import fr.ign.simulatedannealing.visitor.OutputStreamVisitor;
import fr.ign.simulatedannealing.visitor.Visitor;

public class DeformedOptimizer {

	private double coeffDec = Double.NaN;
	private double deltaConf = Double.NaN;

	private double minLengthBox = Double.NaN;
	private double maxLengthBox = Double.NaN;
	private double minWidthBox = Double.NaN;
	private double maxWidthBox = Double.NaN;

	private double energyCreation = Double.NaN;
	private IGeometry samplingSurface = null;

	public void setSamplingSurface(IGeometry geom) {
		samplingSurface = geom;
	}

	public void setEnergyCreation(double energyCreation) {
		this.energyCreation = energyCreation;
	}

	public void setMinLengthBox(double minLengthBox) {
		this.minLengthBox = minLengthBox;
	}

	public void setMaxLengthBox(double maxLengthBox) {
		this.maxLengthBox = maxLengthBox;
	}

	public void setMinWidthBox(double minWidthBox) {
		this.minWidthBox = minWidthBox;
	}

	public void setMaxWidthBox(double maxWidthBox) {
		this.maxWidthBox = maxWidthBox;
	}

	public void setCoeffDec(double coeffDec) {
		this.coeffDec = coeffDec;
	}

	public void setDeltaConf(double deltaConf) {
		this.deltaConf = deltaConf;
	}

	public GraphConfiguration<DeformedCuboid> process(
			BasicPropertyUnit bpu,
			Parameters p,
			Environnement env,
			int id,
			ConfigurationModificationPredicate<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> pred,
			double distReculVoirie, double slope, double hIni, double hMax,
			double distReculLimi, double slopeProspect, double maximalCES, int pos) throws Exception {

		// Géométrie de l'unité foncière sur laquelle porte la génération
		IGeometry geom = bpu.generateGeom().buffer(1);

		// Définition de la fonction d'optimisation (on optimise en décroissant)
		// relative au volume
		GraphConfiguration<DeformedCuboid> conf = null;

		try {
			conf = create_configuration(p, geom, bpu, distReculVoirie, slope,
					hIni, hMax, distReculLimi, slopeProspect, maximalCES);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Création de l'échantilloneur
		Sampler<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> samp = create_sampler(
				Random.random(), p, bpu, pred, pos);
		// Température
		Schedule<SimpleTemperature> sch = create_schedule(p);

		EndTest end = null;
		if (p.getBoolean(("isAbsoluteNumber"))) {
			end = create_end_test(p);
		} else {
			end = create_end_test_stability(p);
		}

		List<Visitor<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>>> list = new ArrayList<>();
		if (p.getBoolean("outputstreamvisitor")) {
			Visitor<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> visitor = new OutputStreamVisitor<>(
					System.out);
			list.add(visitor);
		}
		if (p.getBoolean("shapefilewriter")) {
			ShapefileVisitorCuboid<DeformedCuboid, GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> shpVisitor = new ShapefileVisitorCuboid<>(
					p.get("result").toString() + "result");

			list.add(shpVisitor);
		}
		if (p.getBoolean("visitorviewer")) {
			ViewerVisitor<DeformedCuboid, GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> visitorViewer = new ViewerVisitor<>(
					"" + id, p);
			list.add(visitorViewer);
		}

		if (p.getBoolean("statsvisitor")) {
			StatsVisitor<DeformedCuboid, GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> statsViewer = new StatsVisitor<>(
					"Énergie");
			list.add(statsViewer);
		}

		if (p.getBoolean("filmvisitor")) {
			IDirectPosition dpCentre = new DirectPosition(
					p.getDouble("filmvisitorx"), p.getDouble("filmvisitory"),
					p.getDouble("filmvisitorz"));
			Vecteur viewTo = new Vecteur(p.getDouble("filmvisitorvectx"),
					p.getDouble("filmvisitorvecty"),
					p.getDouble("filmvisitorvectz"));
			Color c = new Color(p.getInteger("filmvisitorr"),
					p.getInteger("filmvisitorg"), p.getInteger("filmvisitorb"));
			FilmVisitor<DeformedCuboid, GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> visitorViewerFilmVisitor = new FilmVisitor<>(
					dpCentre, viewTo, p.getString("result"), c, p);
			list.add(visitorViewerFilmVisitor);
		}

		if (p.getBoolean("csvvisitorend")) {
			String fileName = p.get("result").toString()
					+ p.get("csvfilenamend");
			CSVendStats<DeformedCuboid, GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> statsViewer = new CSVendStats<>(
					fileName);
			list.add(statsViewer);
		}
		if (p.getBoolean("csvvisitor")) {
			String fileName = p.get("result").toString() + p.get("csvfilename");
			CSVvisitor<DeformedCuboid, GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> statsViewer = new CSVvisitor<>(
					fileName);
			list.add(statsViewer);
		}
		countV = new CountVisitor<>();
		list.add(countV);
		CompositeVisitor<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> mVisitor = new CompositeVisitor<>(
				list);
		init_visitor(p, mVisitor);
		/*
		 * < This is the way to launch the optimization process. Here, the magic
		 * happen... >
		 */
		SimulatedAnnealing.optimize(Random.random(), conf, samp, sch, end,
				mVisitor);
		return conf;
	}

	// Initialisation des visiteurs
	// nbdump => affichage dans la console
	// nbsave => sauvegarde en shapefile
	static void init_visitor(
			Parameters p,
			Visitor<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> v) {
		v.init(p.getInteger("nbdump"), p.getInteger("nbsave"));
	}

	CountVisitor<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> countV = null;

	public int getCount() {
		return countV.getCount();
	}

	public GraphConfiguration<DeformedCuboid> create_configuration(
			Parameters p, IGeometry geom, BasicPropertyUnit bpu,
			double distReculVoirie, double slope, double hIni, double hMax,
			double distReculLimi, double slopeProspect, double maximalCES)
			throws Exception {

		return this.create_configuration(p,
				AdapterFactory.toGeometry(new GeometryFactory(), geom), bpu,
				distReculVoirie, slope, hIni, hMax, distReculLimi,
				slopeProspect, maximalCES);

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
	public GraphConfiguration<DeformedCuboid> create_configuration(
			Parameters p, Geometry geom, BasicPropertyUnit bpu,
			double distReculVoirie, double slope, double hIni, double hMax,
			double distReculLimi, double slopeProspect, double maximalCES) {
		// Énergie constante : à la création d'un nouvel objet

		double energyCrea = Double.isNaN(this.energyCreation) ? p
				.getDouble("energy") : this.energyCreation;

		ConstantEnergy<DeformedCuboid, DeformedCuboid> energyCreation = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				energyCrea);

		// Énergie constante : pondération de l'intersection
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationVolume = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_volume"));
		// Énergie unaire : aire dans la parcelle
		UnaryEnergy<DeformedCuboid> energyVolume = new VolumeUnaryEnergy<DeformedCuboid>();
		// Multiplication de l'énergie d'intersection et de l'aire
		UnaryEnergy<DeformedCuboid> energyVolumePondere = new MultipliesUnaryEnergy<DeformedCuboid>(
				ponderationVolume, energyVolume);

		// On retire de l'énergie de création, l'énergie de l'aire
		UnaryEnergy<DeformedCuboid> u3 = new MinusUnaryEnergy<DeformedCuboid>(
				energyCreation, energyVolumePondere);

		// //C1 : distance à 2 m par rapport à la voirie
		UnaryEnergy<DeformedCuboid> u6 = new AlignementEnergy<DeformedCuboid>(
				distReculVoirie, bpu);
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationC1 = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_c1"));
		UnaryEnergy<DeformedCuboid> u6Pondere = new MultipliesUnaryEnergy<DeformedCuboid>(
				u6, ponderationC1);
		UnaryEnergy<DeformedCuboid> u7 = new MinusUnaryEnergy<DeformedCuboid>(
				u3, u6Pondere);

		// //C2
		UnaryEnergy<DeformedCuboid> u8 = new ProspectEnergy<DeformedCuboid>(
				slopeProspect, hIni, bpu);
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationC2 = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_c2"));
		UnaryEnergy<DeformedCuboid> u8Pondere = new MultipliesUnaryEnergy<DeformedCuboid>(
				u8, ponderationC2);
		UnaryEnergy<DeformedCuboid> u9 = new MinusUnaryEnergy<DeformedCuboid>(
				u7, u8Pondere);

		// /C3
		UnaryEnergy<DeformedCuboid> u10 = new HauteurEnergy<DeformedCuboid>(
				hMax);
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationC3 = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_c3"));
		UnaryEnergy<DeformedCuboid> u10pondere = new MultipliesUnaryEnergy<DeformedCuboid>(
				u10, ponderationC3);
		UnaryEnergy<DeformedCuboid> u11 = new MinusUnaryEnergy<DeformedCuboid>(
				u9, u10pondere);

		// C4
		UnaryEnergy<DeformedCuboid> u12 = new ProspectEnergy2<DeformedCuboid>(
				slopeProspect, bpu);
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationC4 = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_c4"));
		UnaryEnergy<DeformedCuboid> u12pondere = new MultipliesUnaryEnergy<DeformedCuboid>(
				u12, ponderationC4);
		UnaryEnergy<DeformedCuboid> u13 = new MinusUnaryEnergy<DeformedCuboid>(
				u11, u12pondere);

		// C5
		UnaryEnergy<DeformedCuboid> u14 = new ServitudeVue<DeformedCuboid>(
				distReculLimi, bpu);
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationC5 = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_c5"));
		UnaryEnergy<DeformedCuboid> u14pondere = new MultipliesUnaryEnergy<DeformedCuboid>(
				u14, ponderationC5);
		UnaryEnergy<DeformedCuboid> u15 = new MinusUnaryEnergy<DeformedCuboid>(
				u13, u14pondere);

		// Énergie constante : pondération de la différence
		ConstantEnergy<DeformedCuboid, DeformedCuboid> ponderationDifference = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_difference_ext"));
		// On ajoute l'énergie de différence : la zone en dehors de la parcelle
		UnaryEnergy<DeformedCuboid> u4 = new DifferenceVolumeUnaryEnergy<DeformedCuboid>(
				geom);
		UnaryEnergy<DeformedCuboid> u5 = new MultipliesUnaryEnergy<DeformedCuboid>(
				ponderationDifference, u4);
		UnaryEnergy<DeformedCuboid> unaryEnergy = new PlusUnaryEnergy<DeformedCuboid>(
				u15, u5);

		// Énergie binaire : intersection entre deux rectangles
		ConstantEnergy<DeformedCuboid, DeformedCuboid> c3 = new ConstantEnergy<DeformedCuboid, DeformedCuboid>(
				p.getDouble("ponderation_volume_inter"));
		BinaryEnergy<DeformedCuboid, DeformedCuboid> b1 = new IntersectionVolumeBinaryEnergy<DeformedCuboid>();
		BinaryEnergy<DeformedCuboid, DeformedCuboid> binaryEnergy = new MultipliesBinaryEnergy<DeformedCuboid, DeformedCuboid>(
				c3, b1);

		// empty initial configuration*/
		GraphConfiguration<DeformedCuboid> conf = new GraphConfiguration<>(
				unaryEnergy, binaryEnergy);
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
	public Sampler<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> create_sampler(
			RandomGenerator rng,
			Parameters p,
			BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> pred,
			int pos) throws Exception {

		if (pos == NO_POS) {
			return undeterminedSampler(rng, p, bpU, pred);
		}

		IMultiCurve<IOrientableCurve> ims = new GM_MultiCurve<>();

		for (SpecificCadastralBoundary scb : bpU.getCadastralParcel().get(0)
				.getSpecificCadastralBoundary()) {
			switch (pos) {

			case DROITE:

				if (scb.getSide() == SpecificCadastralBoundary.RIGHT_SIDE) {
					ims.addAll(FromGeomToLineString.convert(scb.getGeom()));
				}

				break;

			case GAUCHE:

				if (scb.getSide() == SpecificCadastralBoundary.LEFT_SIDE) {
					ims.addAll(FromGeomToLineString.convert(scb.getGeom()));
				}
				break;
			case FOND:

				if (scb.getType() == SpecificCadastralBoundary.BOT) {
					ims.addAll(FromGeomToLineString.convert(scb.getGeom()));
				}
				break;

			case DEVANT:
				if (scb.getType() == SpecificCadastralBoundary.ROAD) {
					ims.addAll(FromGeomToLineString.convert(scb.getGeom()));
				}
				break;
			}

		}

		IMultiCurve<IOrientableCurve> iMSShifted =  shiftRoad(bpU, 7.3,ims);
		
		
		
		return determinedPos(rng, p, bpU, pred,iMSShifted);
		
	}
	
	private static IMultiCurve<IOrientableCurve> shiftRoad(BasicPropertyUnit bPU,
			double valShiftB, IMultiCurve<IOrientableCurve> iMS) {


		IDirectPosition centroidParcel = bPU.getpol2D().centroid();
		IMultiCurve<IOrientableCurve> iMSOut = new GM_MultiCurve<>();
		for (IOrientableCurve oC : iMS) {

			if (oC.isEmpty()) {
				continue;
			}

			IDirectPosition centroidGeom = oC.coord().get(0);
			Vecteur v = new Vecteur(centroidParcel, centroidGeom);

			Vecteur v2 = new Vecteur(oC.coord().get(0), oC.coord().get(
					oC.coord().size() - 1));
			v2.setZ(0);
			v2.normalise();

			Vecteur vOut = v2.prodVectoriel(new Vecteur(0, 0, 1));

			IGeometry geom = ((IGeometry) oC.clone());

			if (v.prodScalaire(vOut) < 0) {
				vOut = vOut.multConstante(-1);
			}

			IGeometry geom2 = geom.translate(valShiftB * vOut.getX(), valShiftB
					* vOut.getY(), 0);

			if (!geom2.intersects(bPU.getGeom())) {
				geom2 = geom.translate(-valShiftB * vOut.getX(), -valShiftB
						* vOut.getY(), 0);
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

	public static class ParallelBuilder implements
			ObjectBuilder<DeformedCuboid> {
		GeometryFactory factory;
		MultiLineString limits;

		public ParallelBuilder(IGeometry[] limits) throws Exception {
			factory = new GeometryFactory();
			LineString[] lineStrings = new LineString[limits.length];
			for (int i = 0; i < limits.length; i++) {
				lineStrings[i] = (LineString) AdapterFactory.toGeometry(
						factory, limits[i]);
			}
			this.limits = factory.createMultiLineString(lineStrings);

		}

		@Override
		public DeformedCuboid build(double[] coordinates) {
			Coordinate p = new Coordinate(coordinates[0], coordinates[1]);
			DistanceOp op = new DistanceOp(this.limits, factory.createPoint(p));
			Coordinate projected = op.nearestPoints()[0];
			double distance = op.distance();
			double orientation = Angle.angle(p, projected);

			ParallelDeformedCuboid result = new ParallelDeformedCuboid(
					coordinates[0], coordinates[1],  coordinates[2], distance * 2, 
					 coordinates[4], coordinates[5],
					coordinates[6], coordinates[7], orientation + Math.PI / 2);

			return result;
		}

		@Override
		public int size() {
			return 9;
		}

		@Override
		public void setCoordinates(DeformedCuboid t, double[] val1) {
			val1[0] = t.centerx;
			val1[1] = t.centery;
			val1[2] = t.length;
			val1[3] = t.width;
			val1[4] = t.height1;
			val1[5] = t.height2;
			val1[6] = t.height3;
			val1[7] = t.height4;
			val1[8] = t.orientation;

		}
	};

	public Sampler<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> determinedPos(
			RandomGenerator rng,
			Parameters p,
			BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> pred, IMultiCurve<IOrientableCurve> ims)
			throws Exception {

		// Un vecteur ?????
		double minlen = Double.isNaN(this.minLengthBox) ? p.getDouble("minlen")
				: this.minLengthBox;
		double maxlen = Double.isNaN(this.maxLengthBox) ? p.getDouble("maxlen")
				: this.maxLengthBox;

		
		double minwid =p.getDouble("minwid");
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
			samplingSurface = bpU.getpol2D();
		}

		ObjectBuilder<DeformedCuboid>  builder = new ParallelBuilder(ims.toArray());

		double[] d = new double[] { env.maxX(), env.maxY(), maxlen, maxwid,
				maxheight, maxheight, maxheight, maxheight, Math.PI };
		double[] v = new double[] { env.minX(), env.minY(), minlen, minwid,
				minheight, minheight, minheight, minheight, 0. };

		for (int i = 0; i < d.length; i++) {
			d[i] = d[i] - v[i];
		}
		

		ParallelDeformedCuboidTransform transformParallel = new ParallelDeformedCuboidTransform(
				d, v, bpU.getpol2D(), ims.toArray());


		// Distribution de poisson
		PoissonDistribution distribution = new PoissonDistribution(rng,
				p.getDouble("poisson"));
		
		ParalledDeformedSampler birth = new ParalledDeformedSampler(rng,transformParallel,builder);

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
				nullView, pView, variate, variate, transformParallel,
				p.getDouble("pbirth"), p.getDouble("pdeath"), "Parallel");
		
		
			Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> parallelMovekernel = new Kernel<>(
					pView, pView, variate, variate, new MoveParallelDeformedCuboid(
							p.getDouble("amplitudeMove")), 0.2, 1.0, "SimpleMoveP");
			kernels.add(parallelMovekernel);
		
		
		double amplitudeDim = p.getDouble("amplitudeMaxDim");

		
		Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> parallelHeight = new Kernel<>(
				pView, pView, variate, variate, new ChangeValue(
						amplitudeDim, 10,3), 0.2, 1.0, "ChangeLength");
		kernels.add(parallelHeight);
		
		
		
	
		



		double amplitudeHeight = p.getDouble("amplitudeHeight");
		Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> parallelHeight1 = new Kernel<>(
				pView, pView, variate, variate, new ChangeValue(
						amplitudeHeight, 10, 4), 0.2, 1.0, "ChangeHeight1");
		kernels.add(parallelHeight1);
		
		Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> parallelHeight2 = new Kernel<>(
				pView, pView, variate, variate, new ChangeValue(
						amplitudeHeight, 10, 5), 0.2, 1.0, "ChangeHeight2");
		kernels.add(parallelHeight2);
		
		Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> parallelHeight3 = new Kernel<>(
				pView, pView, variate, variate, new ChangeValue(
						amplitudeHeight, 10, 6), 0.2, 1.0, "ChangeHeight3");
		kernels.add(parallelHeight3);
		
		Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> parallelHeight4 = new Kernel<>(
				pView, pView, variate, variate, new ChangeValue(
						amplitudeHeight, 10, 7), 0.2, 1.0, "ChangeHeight4");
		kernels.add(parallelHeight4);
		
		

		kernels.add(kernel);

		Sampler<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> s = new GreenSamplerBlockTemperature<>(
				ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);
		return s;
	}

	public Sampler<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> undeterminedSampler(
			RandomGenerator rng,
			Parameters p,
			BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> pred) {

		// Un vecteur ?????
		double minlen = Double.isNaN(this.minLengthBox) ? p.getDouble("minlen")
				: this.minLengthBox;
		double maxlen = Double.isNaN(this.maxLengthBox) ? p.getDouble("maxlen")
				: this.maxLengthBox;

		double minheight = p.getDouble("minheight");
		double maxheight = p.getDouble("maxheight");
		// A priori on redéfini le constructeur de l'objet
		// A priori on redéfini le constructeur de l'objet
		ObjectBuilder<DeformedCuboid> builder = new ObjectBuilder<DeformedCuboid>() {

			@Override
			public int size() {
				return 9;
			}

			@Override
			public DeformedCuboid build(double[] val1) {

				return new DeformedCuboid(val1[0], val1[1], val1[2], val1[3],
						val1[4], val1[5], val1[6], val1[7], val1[8]);
			}

			@Override
			public void setCoordinates(DeformedCuboid t, double[] val1) {
				val1[0] = t.centerx;
				val1[1] = t.centery;
				val1[2] = t.length;
				val1[3] = t.width;
				val1[4] = t.height1;
				val1[5] = t.height2;
				val1[6] = t.height3;
				val1[7] = t.height4;
				val1[8] = t.orientation;
			}
		};

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
			samplingSurface = bpU.getpol2D();
		}

		UniformBirth<DeformedCuboid> birth = new UniformBirth<DeformedCuboid>(

		rng, new DeformedCuboid(env.minX(), env.minY(), minlen, minlen,
				minheight, minheight, minheight, minheight, 0),

		new DeformedCuboid(env.maxX(), env.maxY(), maxlen, maxlen, maxheight,
				maxheight, maxheight, maxheight, Math.PI), builder,
				TransformToSurface.class, samplingSurface);

		// Distribution de poisson
		PoissonDistribution distribution = new PoissonDistribution(rng,
				p.getDouble("poisson"));

		DirectSampler<DeformedCuboid, GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> ds = new DirectRejectionSampler<>(
				distribution, birth, pred);

		// Probabilité de naissance-morts modifications
		List<Kernel<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>>> kernels = new ArrayList<>(
				3);
		KernelFactory<DeformedCuboid, GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> factory = new KernelFactory<>();
		// TODO Use a KernelProposalRatio to propose only birth when size is 0
		kernels.add(factory.make_uniform_birth_death_kernel(rng, builder,
				birth, p.getDouble("pbirth"), 0.5, "BirthDeath"));

		double amplitudeMove = p.getDouble("amplitudeMove");
		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new MoveCuboidDeformed(amplitudeMove), 0.2, "Move"));

		double amplitudeMaxDim = p.getDouble("amplitudeMaxDim");
		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeValueDeformed(amplitudeMaxDim, 10, 2), 0.2,
				"ChgWidth"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeValueDeformed(amplitudeMaxDim, 10, 3), 0.2,
				"ChgLength"));

		double amplitudeHeight = p.getDouble("amplitudeHeight");

		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeHeight1Deformed(amplitudeHeight), 0.2, "ChgHeight1"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeHeight2Deformed(amplitudeHeight), 0.2, "ChgHeight2"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeHeight3Deformed(amplitudeHeight), 0.2, "ChgHeight3"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new ChangeHeight4Deformed(amplitudeHeight), 0.2, "ChgHeight4"));

		double amplitudeRotate = p.getDouble("amplitudeRotate") * Math.PI / 180;
		kernels.add(factory.make_uniform_modification_kernel(rng, builder,
				new RotateCuboid(amplitudeRotate), 0.2, "Rotate"));

		Sampler<GraphConfiguration<DeformedCuboid>, BirthDeathModification<DeformedCuboid>> s = new GreenSamplerBlockTemperature<>(
				ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);
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
		return new StabilityEndTest<DeformedCuboid>(p.getInteger("nbiter"),
				loc_deltaconf);
	}

	private Schedule<SimpleTemperature> create_schedule(Parameters p) {
		double coefDef = 0;
		if (Double.isNaN(this.coeffDec)) {
			coefDef = p.getDouble("deccoef");
		} else {
			coefDef = this.coeffDec;
		}
		return new GeometricSchedule<SimpleTemperature>(new SimpleTemperature(
				p.getDouble("temp")), coefDef);
	}
	
	
	public static class ParalledDeformedSampler implements ObjectSampler<DeformedCuboid> {
		RandomGenerator engine;
		DeformedCuboid object;
		Variate variate;
		Transform transformBand1;
		ObjectBuilder<? extends DeformedCuboid> builder1;


		public ParalledDeformedSampler(RandomGenerator e, 
				Transform transformBand1, 
				ObjectBuilder<? extends DeformedCuboid> builder) {
			this.engine = e;

			this.transformBand1 = transformBand1;
			this.variate = new Variate(e);
			this.builder1 = builder;

		}

		@Override
		public double sample(RandomGenerator e) {
			double[] val0;
			double[] val1;

			val0 = new double[builder1.size()];
			val1 = new double[builder1.size()];
			double phi = this.variate.compute(val0, 0);
			double jacob = this.transformBand1.apply(true, val0, val1);
			this.object = builder1.build(val1);
			return phi / jacob;
		}

		@Override
		public double pdf(DeformedCuboid t) {

			double[] val1 = new double[builder1.size()];
			((ObjectBuilder<DeformedCuboid>) builder1).setCoordinates(t, val1);
			double[] val0 = new double[builder1.size()];
			double J10 = this.transformBand1.apply(false, val1, val0);
			if (J10 == 0) {
				return 0;
			}
			double pdf = this.variate.pdf(val0, 0);
			return pdf * J10;
		}

		@Override
		public DeformedCuboid getObject() {
			return this.object;
		}
	}

}
