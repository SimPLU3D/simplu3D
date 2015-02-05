package fr.ign.cogit.simplu3d.exec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.math3.random.RandomGenerator;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.configuration.ModelInstanceGraphConfiguration;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.configuration.ModelInstanceGraphConfigurationPredicate;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.configuration.ModelInstanceModification;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.TransformToSurface;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.kernel.KernelFactory;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.UniformBirth;
import fr.ign.parameters.Parameters;
import fr.ign.random.Random;
import fr.ign.rjmcmc.acceptance.MetropolisAcceptance;
import fr.ign.rjmcmc.distribution.UniformDistribution;
import fr.ign.rjmcmc.energy.ConstantEnergy;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.kernel.KernelFunctor;
import fr.ign.rjmcmc.kernel.RandomApply;
import fr.ign.rjmcmc.sampler.GreenSampler;
import fr.ign.rjmcmc.sampler.RejectionSampler;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;

/**
 * Classe permettant de générer et d'exporter un ensemble de boîtes suivant la
 * distribution paramétrée
 * 
 * 
 * @author MBrasebin
 * 
 */
public class DistributionGeneration {

	public static IFeatureCollection<IFeature> featCD = new FT_FeatureCollection<>();

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

		/*
		 * < Retrieve the singleton instance of the parameters object...
		 * initialize the parameters object with the default values provided...
		 * parse the command line to eventually change the values >
		 */
		Parameters p = Parameters
				.unmarshall(new File(
						"./src/main/resources/scenario/building_parameters_project_expthese_1.xml"));
		Environnement env = LoaderSHP.load(p.getString("folder"));
		BasicPropertyUnit bpu = env.getBpU().get(1);

		ModelInstanceGraphConfigurationPredicate<Cuboid> pred = new ModelInstanceGraphConfigurationPredicate<Cuboid>(
				bpu);
		ModelInstanceGraphConfiguration<Cuboid> config = new ModelInstanceGraphConfiguration<>(
				bpu, pred.getRuleChecker().getlModeInstance().get(0),
				new ConstantEnergy<Cuboid, Cuboid>(0),
				new ConstantEnergy<Cuboid, Cuboid>(0));

		Sampler<ModelInstanceGraphConfiguration<Cuboid>, ModelInstanceModification<Cuboid>> samp = create_sampler(
				p, bpu, pred);

		// int count = 0;

		int testC = 5000;

		// int countTrue = 0;

		for (int i = 0; i < testC; i++) {

			// count++;

			ModelInstanceModification<Cuboid> modif = config.newModification();

			KernelFunctor<ModelInstanceGraphConfiguration<Cuboid>, ModelInstanceModification<Cuboid>> kf = new KernelFunctor<>(
					Random.random(), config, modif);

			RandomApply.randomApply(0, samp.getKernels(), kf);

			boolean test = true; // samp.checkNonUpdateConfiguration(kf);

			List<Cuboid> lAB = (List<Cuboid>) kf.getModif().getBirth();

			if (test) {

				// countTrue++;

				if (lAB.size() != 1) {
					System.out.println("Probabilité supérieure à 1 ?");
					return;
				}

				Cuboid c1 = lAB.get(0);

				IFeature f = new DefaultFeature();
				f.setGeom(new GM_Point(new DirectPosition(c1.centerx,
						c1.centery)));

				AttributeManager.addAttribute(f, "l", c1.length, "Double");
				AttributeManager.addAttribute(f, "w", c1.width, "Double");
				AttributeManager.addAttribute(f, "h", c1.height, "Double");
				AttributeManager.addAttribute(f, "o", c1.orientation, "Double");

				featC.add(f);
			}

		}

		ShapefileWriter.write(featC, "H:/Distrib/dist.shp");

		ShapefileWriter.write(featCD, "H:/Distrib/distdeb.shp");

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
	static Sampler<ModelInstanceGraphConfiguration<Cuboid>, ModelInstanceModification<Cuboid>> create_sampler(
			Parameters p, BasicPropertyUnit bpU,
			ModelInstanceGraphConfigurationPredicate<Cuboid> pred) {

		RandomGenerator rng = Random.random();
		// Un vecteur ?????

		double mindim = p.getDouble("mindim");
		double maxdim = p.getDouble("maxdim");

		double minheight = p.getDouble("minheight");
		double maxheight = p.getDouble("maxheight");

		// A priori on redéfini le constructeur de l'objet
		ObjectBuilder<Cuboid> builder = new ObjectBuilder<Cuboid>() {

			@Override
			public int size() {
				return 6;
			}

			@Override
			public Cuboid build(Vector<Double> val1) {

				return new Cuboid(val1.get(0), val1.get(1), val1.get(2),
						val1.get(3), val1.get(4), val1.get(5));
			}

			@Override
			public void setCoordinates(Cuboid t, List<Double> val1) {
				val1.set(0, t.centerx);
				val1.set(1, t.centery);
				val1.set(2, t.length);
				val1.set(3, t.width);
				val1.set(4, t.height);
				val1.set(5, t.orientation);
			}
		};

		// Sampler de naissance
		// UniformBirthInGeom<Cuboid2> birth = new
		// UniformBirthInGeom<Cuboid2>(new
		// Cuboid2(0, 0, mindim,
		// mindim, minheight, 0), new Cuboid2(1, 1, maxdim, maxdim, maxheight,
		// Math.PI), builder,
		// bpU.getpol2D());
		UniformBirth<Cuboid> birth = new UniformBirth<Cuboid>(rng, new Cuboid(
				0, 0, mindim, mindim, minheight, 0), new Cuboid(1, 1, maxdim,
				maxdim, maxheight, Math.PI), builder, TransformToSurface.class,
				bpU.getpol2D());

		DirectSampler<Cuboid, ModelInstanceGraphConfiguration<Cuboid>, ModelInstanceModification<Cuboid>> ds = new DirectSampler<>(
				new UniformDistribution(rng, 0, 1), birth);

		// Probabilité de naissance-morts modifications
		List<Kernel<ModelInstanceGraphConfiguration<Cuboid>, ModelInstanceModification<Cuboid>>> kernels = new ArrayList<>(
				3);
		KernelFactory<Cuboid, ModelInstanceGraphConfiguration<Cuboid>, ModelInstanceModification<Cuboid>> factory = new KernelFactory<>();
		kernels.add(factory.make_uniform_birth_death_kernel(rng, builder,
				birth, 1, 1));

		Sampler<ModelInstanceGraphConfiguration<Cuboid>, ModelInstanceModification<Cuboid>> s = new GreenSampler<>(
				rng, ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);
		Sampler<ModelInstanceGraphConfiguration<Cuboid>, ModelInstanceModification<Cuboid>> rs = new RejectionSampler<>(
				s, pred);
		return rs;
	}

	// public static <T extends SimpleObject> Kernel<T>
	// make_uniform_birth_death_kernel_with_geom(
	// ObjectBuilder<T> builder, UniformBirthInGeom<T> b, double pbirth, double
	// pdeath) {
	// return new Kernel<T>(new NullView<T>(), new UniformView<T>(builder),
	// b.getVariate(),
	// new Variate<T>(0), b.getTransform(), pbirth, pdeath);
	// }

}
