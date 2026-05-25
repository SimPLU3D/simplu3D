package fr.ign.cogit.simplu3d.test.rjmcmc.trapezoid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ign.cogit.simplu3d.io.feature.FeatureType;
import fr.ign.cogit.simplu3d.model.AbstractUrbanFeature;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import fr.ign.common.io.ShapefileWriter;
import org.apache.commons.math3.random.RandomGenerator;

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
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class TestParallelRightTrapezoidSampler2 {

	// public static List<IGeometry> trapezoidAfter = new ArrayList<>();
	public static List<Geometry> orientationLine = new ArrayList<>();

	public static void main(String[] args) throws ParseException {
		RandomGenerator rng = Random.random();
		PoissonDistribution distribution = new PoissonDistribution(rng, 20.0);

		WKTReader reader = new WKTReader();
		Polygon polygon = (Polygon) reader.read(
				"POLYGON (( 24.46264278127928 -13.32945321889124, 16.83388492956696 -2.4038452403082764, 21.54914732032382 4.036513147066944, 30.596317435922344 9.7484976692033, 35.119902493721604 1.4680368854351595, 23.657597982857375 1.1996886192945253, 32.66643263186438 -8.729197227908939, 41.17690621518163 -1.7138068416609316, 39.98850675084454 -16.47296147939581, 20.092399589846092 -22.60663613403888, 13.307022003147196 -13.942820684355546, 24.46264278127928 -13.32945321889124 ))"
		);
		LineString lS = (LineString) reader.read(
				"LINESTRING ( 24.46264278127928 -13.32945321889124, 16.83388492956696 -2.4038452403082764, 21.54914732032382 4.036513147066944)"
		);
		Geometry[] limits = new Geometry[1];
		limits[0] = lS;
		Envelope env = polygon.getEnvelopeInternal();
		double xmin = env.getMinX();
		double xmax = env.getMaxX();
		double ymin = env.getMinY();
		double ymax = env.getMaxY();
		double length1min = 1;
		double length1max = 15;
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
		UniformBirth<ParallelTrapezoid2> birth = new UniformBirth<>(rng,
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

		Collection<UrbanFeature> featColl = new ArrayList<>();
		Sampler<GraphConfiguration<ParallelTrapezoid2>, BirthDeathModification<ParallelTrapezoid2>> s = new GreenSamplerBlockTemperature<>(
				rng, ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);

		for (int i = 0; i < nbSample; i++) {
			GraphConfiguration<ParallelTrapezoid2> gC = new GraphConfiguration<>(
					new ConstantEnergy<ParallelTrapezoid2, ParallelTrapezoid2>(5.0),
                    new ConstantEnergy<>(0.0));
			s.sample(rng, gC, new SimpleTemperature(1000));
			featColl.addAll(exportColl(gC));
		}
		FeatureType featureType = new FeatureType("ParallelTrapezoid");
		featureType.setGeometryType("MultiPolygon");
		featureType.addAttribute("x","Double");
		featureType.addAttribute("y","Double");
		featureType.addAttribute("length1", "Double");
		featureType.addAttribute("length2", "Double");
		featureType.addAttribute("length3", "Double");
		featureType.addAttribute("width", "Double");
		featureType.addAttribute("Hauteur", "Double");
		featureType.addAttribute("Rotation", "Double");
		System.out.println(featColl.size());
		ShapefileWriter.write(featColl, featureType, null, "test_tra.shp");
		featColl.clear();
		UrbanFeature limit = new AbstractUrbanFeature();
		limit.setGeometry(limits[0]);
		featColl.add(limit);
		FeatureType featureTypeLimit = new FeatureType("Limit");
		featureTypeLimit.setGeometryType("LineString");
		ShapefileWriter.write(featColl, featureTypeLimit, null, "test_tra_line.shp");
		Collection<UrbanFeature> featCollOut = new ArrayList<>();
		for (Geometry iC : orientationLine) {
			UrbanFeature feat = new AbstractUrbanFeature();
			feat.setGeometry(iC);
			featCollOut.add(feat);
		}
		FeatureType featureTypeOrientation = new FeatureType("Orientation");
		featureTypeOrientation.setGeometryType("MultiLineString");
		ShapefileWriter.write(featCollOut, featureTypeOrientation, null, "test_tra_orientation.shp");
	}

	public static Collection<UrbanFeature> exportColl(GraphConfiguration<? extends RightTrapezoid> gC) {
		Collection<UrbanFeature> featC = new ArrayList<>();
		for (GraphVertex<? extends RightTrapezoid> v : gC.getGraph().vertexSet()) {
			UrbanFeature feat = new AbstractUrbanFeature();
			feat.setGeometry(v.getValue().generated3DGeom());
			// On ajoute des attributs aux entités (dimension des objets)
			feat.setAttribute("x", v.getValue().centerx);
			feat.setAttribute("y", v.getValue().centery);
			feat.setAttribute("length1", v.getValue().length1);
			feat.setAttribute("length2", v.getValue().length2);
			feat.setAttribute("length3", v.getValue().length3);
			feat.setAttribute("width", v.getValue().width);
			feat.setAttribute("Hauteur", v.getValue().height);
			feat.setAttribute("Rotation", v.getValue().orientation);
			featC.add(feat);
		}
		return featC;
	}

}
