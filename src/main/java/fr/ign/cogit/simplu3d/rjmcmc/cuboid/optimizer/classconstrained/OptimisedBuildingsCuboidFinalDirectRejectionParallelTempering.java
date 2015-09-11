package fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained;

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
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.energy.cuboid2.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.energy.cuboid2.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.energy.cuboid2.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid2;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.ChangeHeight;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.ChangeLength;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.ChangeWidth;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.MoveCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.RotateCuboid;
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
import fr.ign.simulatedannealing.ParallelTempering;
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
public class OptimisedBuildingsCuboidFinalDirectRejectionParallelTempering {

	private double minLengthBox = Double.NaN;
	private double maxLengthBox = Double.NaN;
	private double minWidthBox = Double.NaN;
	private double maxWidthBox = Double.NaN;

	private int numberOfReplicas = 1;

	private double tempMax = 1;

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

	public OptimisedBuildingsCuboidFinalDirectRejectionParallelTempering(
			int nbReplicas, double tempMax) {
		numberOfReplicas = nbReplicas;
		this.tempMax = tempMax;
	}
	
	private IGeometry samplingSurface = null;
	
	public void setSamplingSurface(IGeometry geom){
		samplingSurface = geom;
	}

	public void process(
			BasicPropertyUnit bpu,
			Parameters p,
			Environnement env,
			int id,
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

		Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> sampler = create_sampler(
				Random.random(), p, bpu, pred);

		for (int i = 0; i < this.numberOfReplicas; i++) {

			GraphConfiguration<Cuboid> conf = null;
			try {
				conf = create_configuration(p,
						AdapterFactory.toGeometry(new GeometryFactory(), geom),
						bpu);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (loadExistingConfig == 1) {
				String configPath = p.get("config_shape_file").toString();
				List<Cuboid> lCuboid = LoaderCuboid2
						.loadFromShapeFile(configPath);
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
					new SimpleTemperature(i * Math.log(this.tempMax)
							/ (this.numberOfReplicas - 1)), 1);

			List<Visitor<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> list = new ArrayList<>();
			if (i == 0) {

				if (p.getBoolean("outputstreamvisitor")) {
					Visitor<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> visitor = new OutputStreamVisitor<>(
							System.out);
					list.add(visitor);
				}

				if (p.getBoolean("shapefilewriter")) {
					ShapefileVisitorCuboid<Cuboid,GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> shpVisitor = new ShapefileVisitorCuboid<>(
							p.get("result").toString() + "result");
					list.add(shpVisitor);
				}

				if (p.getBoolean("visitorviewer")) {
					ViewerVisitor<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> visitorViewer = new ViewerVisitor<>(
							"" + id, p);
					list.add(visitorViewer);
				}

				if (p.getBoolean("statsvisitor")) {
					StatsVisitor<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> statsViewer = new StatsVisitor<>(
							"Énergie");
					list.add(statsViewer);
				}

				if (p.getBoolean("filmvisitor")) {
					IDirectPosition dpCentre = new DirectPosition(
							p.getDouble("filmvisitorx"),
							p.getDouble("filmvisitory"),
							p.getDouble("filmvisitorz"));

					Vecteur viewTo = new Vecteur(
							p.getDouble("filmvisitorvectx"),
							p.getDouble("filmvisitorvecty"),
							p.getDouble("filmvisitorvectz")

					);

					Color c = new Color(p.getInteger("filmvisitorr"),
							p.getInteger("filmvisitorg"),

							p.getInteger("filmvisitorb"));

					FilmVisitor<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> visitorViewerFilmVisitor = new FilmVisitor<>(
							dpCentre, viewTo, p.getString("result"), c, p);
					list.add(visitorViewerFilmVisitor);
				}

				if (p.getBoolean("csvvisitorend")) {
					String fileName = p.get("result").toString()
							+ p.get("csvfilenamend");
					CSVendStats<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> statsViewer = new CSVendStats<>(
							fileName);
					list.add(statsViewer);
				}
				if (p.getBoolean("csvvisitor")) {
					String fileName = p.get("result").toString()
							+ p.get("csvfilename");
					CSVvisitor<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> statsViewer = new CSVvisitor<>(
							fileName);
					list.add(statsViewer);
				}
			}

			countV = new CountVisitor<>();
			list.add(countV);
			CompositeVisitor<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> mVisitor = new CompositeVisitor<>(
					list);
			init_visitor(p, mVisitor);

			tabVisitor[i] = mVisitor;

		}

		/*
		 * < This is the way to launch the optimization process. Here, the magic
		 * happen... >
		 */

		EndTest end = create_end_test(p);

		// Partie spécial Parrallel tempering

		ParallelTempering.optimize(Random.random(), tabConfig, sampler, samp,
				end, tabVisitor);

	}

	// Initialisation des visiteurs
	// nbdump => affichage dans la console
	// nbsave => sauvegarde en shapefile
	static void init_visitor(
			Parameters p,
			Visitor<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> v) {
		v.init(p.getInteger("nbdump"), p.getInteger("nbsave"));
	}

	CountVisitor<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> countV = null;

	public int getCount() {
		return countV.getCount();
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
	public static GraphConfiguration<Cuboid> create_configuration(Parameters p,
			Geometry geom, BasicPropertyUnit bpu) {
		// Énergie constante : à la création d'un nouvel objet
		ConstantEnergy<Cuboid, Cuboid> energyCreation = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("energy"));
		// Énergie constante : pondération de l'intersection
		ConstantEnergy<Cuboid, Cuboid> ponderationVolume = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("ponderation_volume"));
		// Énergie unaire : aire dans la parcelle
		UnaryEnergy<Cuboid> energyVolume = new VolumeUnaryEnergy<Cuboid>();
		// Multiplication de l'énergie d'intersection et de l'aire
		UnaryEnergy<Cuboid> energyVolumePondere = new MultipliesUnaryEnergy<Cuboid>(
				ponderationVolume, energyVolume);

		// On retire de l'énergie de création, l'énergie de l'aire
		UnaryEnergy<Cuboid> u3 = new MinusUnaryEnergy<Cuboid>(energyCreation,
				energyVolumePondere);

		// Énergie constante : pondération de la différence
		ConstantEnergy<Cuboid, Cuboid> ponderationDifference = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("ponderation_difference_ext"));
		// On ajoute l'énergie de différence : la zone en dehors de la parcelle
		UnaryEnergy<Cuboid> u4 = new DifferenceVolumeUnaryEnergy<Cuboid>(geom);
		UnaryEnergy<Cuboid> u5 = new MultipliesUnaryEnergy<Cuboid>(
				ponderationDifference, u4);
		UnaryEnergy<Cuboid> unaryEnergy = new PlusUnaryEnergy<Cuboid>(u3, u5);

		// Énergie binaire : intersection entre deux rectangles
		ConstantEnergy<Cuboid, Cuboid> c3 = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("ponderation_volume_inter"));
		BinaryEnergy<Cuboid, Cuboid> b1 = new IntersectionVolumeBinaryEnergy<Cuboid>();
		BinaryEnergy<Cuboid, Cuboid> binaryEnergy = new MultipliesBinaryEnergy<Cuboid, Cuboid>(
				c3, b1);
		// empty initial configuration*/
		GraphConfiguration<Cuboid> conf = new GraphConfiguration<>(unaryEnergy,
				binaryEnergy);
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
	Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> create_sampler(
			RandomGenerator rnd,
			Parameters p,
			BasicPropertyUnit bpU,
			ConfigurationModificationPredicate<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred) {
		// Un vecteur ?????
		double minlen = Double.isNaN(this.minLengthBox) ? p.getDouble("minlen")
				: this.minLengthBox;
		double maxlen = Double.isNaN(this.maxLengthBox) ? p.getDouble("maxlen")
				: this.maxLengthBox;

		
		double minwid = Double.isNaN(this.minWidthBox) ? p.getDouble("minwid")
				: this.minWidthBox;
		double maxwid = Double.isNaN(this.maxWidthBox) ? p.getDouble("maxwid")
				: this.maxWidthBox;


		double minheight = p.getDouble("minheight");
		double maxheight = p.getDouble("maxheight");
		// A priori on redéfini le constructeur de l'objet
		// A priori on redéfini le constructeur de l'objet
    ObjectBuilder<Cuboid> builder = new ObjectBuilder<Cuboid>() {

      @Override
      public int size() {
        return 6;
      }

      @Override
      public Cuboid build(double[] val1) {

        return new Cuboid(val1[0], val1[1], val1[2],
            val1[3], val1[4], val1[5]);
      }

      @Override
      public void setCoordinates(Cuboid t, double[] val1) {
        val1[0] = t.centerx;
        val1[1] = t.centery;
        val1[2] = t.length;
        val1[3] = t.width;
        val1[4] = t.height;
        val1[5] = t.orientation;
      }
    };

		IEnvelope env = bpU.getGeom().envelope();
		// Sampler de naissance
		// UniformBirthInGeom<Cuboid2> birth = new
		// UniformBirthInGeom<Cuboid2>(new
		// Cuboid2(env.minX(),
		// env.minY(), mindim, mindim, minheight, 0), new Cuboid2(env.maxX(),
		// env.maxY(), maxdim,
		// maxdim, maxheight, Math.PI), builder, bpU.getpol2D());
		
		
		
		if(samplingSurface == null){
			samplingSurface = bpU.getpol2D();
		}
		
		UniformBirth<Cuboid> birth = new UniformBirth<Cuboid>(rnd, new Cuboid(
				env.minX(), env.minY(), minlen, minwid, minheight, 0),
				new Cuboid(env.maxX(), env.maxY(), maxlen, maxwid, maxheight,
						Math.PI), builder, TransformToSurface.class,
						samplingSurface);

		// Distribution de poisson
		PoissonDistribution distribution = new PoissonDistribution(rnd,
				p.getDouble("poisson"));

		DirectSampler<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> ds = new DirectRejectionSampler<>(
				distribution, birth, pred);

		// Probabilité de naissance-morts modifications
		List<Kernel<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> kernels = new ArrayList<>(3);
		KernelFactory<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> factory = new KernelFactory<>();
    //TODO Use a KernelProposalRatio to propose only birth when size is 0
		kernels.add(factory.make_uniform_birth_death_kernel(rnd, builder, birth, p.getDouble("pbirth"), 1.0, "BirthDeath"));
		double amplitudeMove = p.getDouble("amplitudeMove");
		kernels.add(factory.make_uniform_modification_kernel(rnd, builder, new MoveCuboid(amplitudeMove), 0.2, "Move"));
		double amplitudeRotate = p.getDouble("amplitudeRotate") * Math.PI / 180;
		kernels.add(factory.make_uniform_modification_kernel(rnd, builder, new RotateCuboid(amplitudeRotate), 0.2, "Rotate"));
		double amplitudeMaxDim = p.getDouble("amplitudeMaxDim");
		kernels.add(factory.make_uniform_modification_kernel(rnd, builder, new ChangeWidth(amplitudeMaxDim), 0.2, "ChgWidth"));
		kernels.add(factory.make_uniform_modification_kernel(rnd, builder, new ChangeLength(amplitudeMaxDim), 0.2, "ChgLength"));
		double amplitudeHeight = p.getDouble("amplitudeHeight");
		kernels.add(factory.make_uniform_modification_kernel(rnd, builder, new ChangeHeight(amplitudeHeight), 0.2, "ChgHeight"));

		Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> s = new GreenSamplerBlockTemperature<>(
				ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);
		return s;
	}

	private static EndTest create_end_test(Parameters p) {
		return new MaxIterationEndTest(p.getInteger("nbiter"));
	}

}
