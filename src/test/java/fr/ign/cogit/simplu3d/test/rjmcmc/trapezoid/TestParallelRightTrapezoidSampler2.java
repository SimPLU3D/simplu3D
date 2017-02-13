package fr.ign.cogit.simplu3d.test.rjmcmc.trapezoid;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ParseException;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.geoxygene.util.conversion.WktGeOxygene;
import fr.ign.cogit.simplu3d.rjmcmc.generic.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.builder.ParallelRightTrapezoidBuilder2;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry.ParallelTrapezoid2;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry.RightTrapezoid;
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
import fr.ign.rjmcmc.kernel.ChangeValue;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;

public class TestParallelRightTrapezoidSampler2 {

	// public static List<IGeometry> trapezoidAfter = new ArrayList<>();
	public static List<IGeometry> orientationLine = new ArrayList<>();

	public static void main(String[] args) throws ParseException {
		RandomGenerator rng = Random.random();
		PoissonDistribution distribution = new PoissonDistribution(rng, 20.0);

		IPolygon polygon = (IPolygon) WktGeOxygene.makeGeOxygene(
				"POLYGON (( 24.46264278127928 -13.32945321889124, 16.83388492956696 -2.4038452403082764, 21.54914732032382 4.036513147066944, 30.596317435922344 9.7484976692033, 35.119902493721604 1.4680368854351595, 23.657597982857375 1.1996886192945253, 32.66643263186438 -8.729197227908939, 41.17690621518163 -1.7138068416609316, 39.98850675084454 -16.47296147939581, 20.092399589846092 -22.60663613403888, 13.307022003147196 -13.942820684355546, 24.46264278127928 -13.32945321889124 ))");

		ILineString lS = (ILineString) WktGeOxygene.makeGeOxygene(
				"LINESTRING ( 24.46264278127928 -13.32945321889124, 16.83388492956696 -2.4038452403082764, 21.54914732032382 4.036513147066944)");

		IGeometry[] limits = new IGeometry[1];
		limits[0] = lS;

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

		double widhtmin = 0.5;
		double widhtmax = 1;

		double heightMin = 3;
		double heightMax = 10;

		double orientationMin = 0;
		double orientationMax = 1;

		int nbSample = 50;
		
		ParallelRightTrapezoidBuilder2 builder = new ParallelRightTrapezoidBuilder2(limits, polygon);

		UniformBirth<ParallelTrapezoid2> birth = new UniformBirth<ParallelTrapezoid2>(rng,
				new ParallelTrapezoid2(xmin, ymin, length1min, length2min, length3min, widhtmin, heightMin,
						orientationMin),
				new ParallelTrapezoid2(xmax, ymax, length1max, length2max, length3max, widhtmax, heightMax,
						orientationMax),
				builder, ParallelTrapezoidTransform.class,
				polygon.buffer(-widhtmax));

		List<Kernel<GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>>> kernels = new ArrayList<>(
				3);
		KernelFactory<ParallelTrapezoid2, GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> factory = new KernelFactory<>();
		// TODO Use a KernelProposalRatio to propose only birth when size is 0
		
		
		kernels.add(factory.make_uniform_birth_death_kernel(rng, new ParallelRightTrapezoidBuilder2(limits, polygon),
				birth, 1.0, 1.0, "BirthDeath"));
		
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeValue(0.5, 6, 2), 0.2,
				"ChgLength"));
		
		
		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeValue(0.5, 6, 3), 0.2,
				"ChgWidth"));


		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeValue(0.5, 6, 4), 0.2,
				"ChgHeight"));

		kernels.add(factory.make_uniform_modification_kernel(rng, builder, new ChangeValue(0.5, 6, 5), 0.2,
				"ChgAbscisse"));

		
		

		DirectSampler<ParallelTrapezoid2, GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> ds = new DirectSampler<>(
				distribution, birth);

		IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<>();

		Sampler<GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> s = new GreenSamplerBlockTemperature<>(
				rng, ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);

		for (int i = 0; i < nbSample; i++) {
			GraphConfiguration<ParallelTrapezoid2> gC = new GraphConfiguration<>(
					new ConstantEnergy<ParallelTrapezoid2, ParallelTrapezoid2>(5.0),
					new ConstantEnergy<ParallelTrapezoid2, ParallelTrapezoid2>(0.0));
			s.sample(rng, gC, new SimpleTemperature(1000));

			featColl.addAll(exportColl(gC));
		}

		System.out.println(featColl.size());
		ShapefileWriter.write(featColl, "test_tra.shp");

		featColl.clear();
		featColl.add(new DefaultFeature(limits[0]));

		ShapefileWriter.write(featColl, "test_tra_line.shp");

		IFeatureCollection<IFeature> featCollOut = new FT_FeatureCollection<>();

		for (IGeometry iC : orientationLine) {
			featCollOut.add(new DefaultFeature(iC));
		}

		ShapefileWriter.write(featCollOut, "test_tra_orientation.shp");

	}

	public static IFeatureCollection<IFeature> exportColl(GraphConfiguration<? extends RightTrapezoid> gC) {

		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
		for (GraphVertex<? extends RightTrapezoid> v : gC.getGraph().vertexSet()) {

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
