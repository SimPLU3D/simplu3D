package fr.ign.cogit.simplu3d.test.rjmcmc.trapezoid;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ParseException;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.geoxygene.util.conversion.WktGeOxygene;
import fr.ign.cogit.simplu3d.rjmcmc.generic.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.builder.ParallelRightTrapezoidBuilder;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry.ParallelTrapezoid;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.transform.ParallelTrapezoidTransform;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.mpp.kernel.KernelFactory;
import fr.ign.mpp.kernel.UniformBirth;
import fr.ign.random.Random;
import fr.ign.rjmcmc.acceptance.MetropolisAcceptance;
import fr.ign.rjmcmc.distribution.PoissonDistribution;
import fr.ign.rjmcmc.energy.ConstantEnergy;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;

public class TestParallelRightTrapezoidSampler {

	// public static List<IGeometry> trapezoidAfter = new ArrayList<>();
	// public static List<IGeometry> orientationLine = new ArrayList<>();

	public static void main(String[] args) throws ParseException {
		RandomGenerator rng = Random.random();
		PoissonDistribution distribution = new PoissonDistribution(rng, 20.0);

		IPolygon polygon = (IPolygon) WktGeOxygene.makeGeOxygene(
				"POLYGON (( 24.46264278127928 -13.32945321889124, 16.83388492956696 -2.4038452403082764, 21.54914732032382 4.036513147066944, 30.596317435922344 9.7484976692033, 35.119902493721604 1.4680368854351595, 23.657597982857375 1.1996886192945253, 32.66643263186438 -8.729197227908939, 41.17690621518163 -1.7138068416609316, 39.98850675084454 -16.47296147939581, 20.092399589846092 -22.60663613403888, 13.307022003147196 -13.942820684355546, 24.46264278127928 -13.32945321889124 ))");
		IGeometry[] limits = new IGeometry[1];
		limits[0] = polygon.exteriorLineString();

		IEnvelope env = polygon.envelope();

		double xmin = env.getLowerCorner().getX();
		double xmax = env.getUpperCorner().getX();

		double ymin = env.getLowerCorner().getY();
		double ymax = env.getUpperCorner().getY();

		double length1min = 1;
		double length1max = 2;

		double length2min = 1;
		double length2max = 15;

		double length3min = 1;
		double length3max = 15;

		double widhtmin = 2;
		double widhtmax = 3;

		double heightMin = 3;
		double heightMax = 10;

		double orientationMin = 0;
		double orientationMax = 2 * Math.PI;

		int nbSample = 50;

		UniformBirth<ParallelTrapezoid> birth = new UniformBirth<ParallelTrapezoid>(rng,
				new ParallelTrapezoid(xmin, ymin, length1min, length2min, length3min, widhtmin, heightMin,
						orientationMin),
				new ParallelTrapezoid(xmax, ymax, length1max, length2max, length3max, widhtmax, heightMax,
						orientationMax),
				new ParallelRightTrapezoidBuilder(limits), ParallelTrapezoidTransform.class, polygon.buffer(-widhtmax));

		List<Kernel<GraphConfiguration<ParallelTrapezoid>, BirthDeathModification<ParallelTrapezoid>>> kernels = new ArrayList<>(
				3);
		KernelFactory<ParallelTrapezoid, GraphConfiguration<ParallelTrapezoid>, BirthDeathModification<ParallelTrapezoid>> factory = new KernelFactory<>();
		// TODO Use a KernelProposalRatio to propose only birth when size is 0
		kernels.add(factory.make_uniform_birth_death_kernel(rng, new ParallelRightTrapezoidBuilder(limits), birth, 1.0,
				1.0, "BirthDeath"));

		DirectSampler<ParallelTrapezoid, GraphConfiguration<ParallelTrapezoid>, BirthDeathModification<ParallelTrapezoid>> ds = new DirectSampler<>(
				distribution, birth);

		IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<>();

		Sampler<GraphConfiguration<ParallelTrapezoid>, BirthDeathModification<ParallelTrapezoid>> s = new GreenSamplerBlockTemperature<>(
				rng, ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);

		for (int i = 0; i < nbSample; i++) {
			GraphConfiguration<ParallelTrapezoid> gC = new GraphConfiguration<>(
					new ConstantEnergy<ParallelTrapezoid, ParallelTrapezoid>(5.0),
					new ConstantEnergy<ParallelTrapezoid, ParallelTrapezoid>(0.0));
			s.sample(rng, gC, new SimpleTemperature(1000));

			featColl.addAll(exportColl(gC));
		}

		System.out.println(featColl.size());
		ShapefileWriter.write(featColl, "test_tra.shp");

		featColl.clear();
		featColl.add(new DefaultFeature(limits[0]));

		ShapefileWriter.write(featColl, "test_tra_line.shp");

	}

	public static IFeatureCollection<IFeature> exportColl(GraphConfiguration<ParallelTrapezoid> gC) {

		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
		for (GraphVertex<ParallelTrapezoid> v : gC.getGraph().vertexSet()) {

			IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());
			// On ajoute des attributs aux entités (dimension des objets)
			AttributeManager.addAttribute(feat, "x", v.getValue().centerx, "Double");
			AttributeManager.addAttribute(feat, "y", v.getValue().centery, "Double");
			AttributeManager.addAttribute(feat, "lenght1", v.getValue().length1, "Double");
			AttributeManager.addAttribute(feat, "lenght2", v.getValue().length2, "Double");
			AttributeManager.addAttribute(feat, "lenght3", v.getValue().length3, "Double");
			AttributeManager.addAttribute(feat, "width", v.getValue().width, "Double");
			AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height, "Double");
			AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation, "Double");

			featC.add(feat);

		}

		return featC;
	}

}
