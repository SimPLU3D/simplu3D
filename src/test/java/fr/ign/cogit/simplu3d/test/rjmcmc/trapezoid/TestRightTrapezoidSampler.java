package fr.ign.cogit.simplu3d.test.rjmcmc.trapezoid;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Envelope;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.TransformToSurface;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.builder.SimpleRightTrapezoidBuilder;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry.RightTrapezoid;
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

public class TestRightTrapezoidSampler {

	public static void main(String[] args) {
		RandomGenerator rng = Random.random();
		PoissonDistribution distribution = new PoissonDistribution(rng, 20.0);

		double xmin = 0;
		double xmax = 100;

		double ymin = 0;
		double ymax = 200;

		double length1min = 1;
		double length1max = 20;

		double length2min = 1;
		double length2max = 15;
		
		
		double length3min = 1;
		double length3max = 15;

		double widhtmin = 2;
		double widhtmax = 20;

		double heightMin = 3;
		double heightMax = 10;

		double orientationMin = 0;
		double orientationMax = Math.PI;
		
		int nbSample = 5;

		UniformBirth<RightTrapezoid> birth = new UniformBirth<RightTrapezoid>(rng,
				new RightTrapezoid(xmin, ymin, length1min, length2min, length3min, widhtmin, heightMin, orientationMin),
				new RightTrapezoid(xmax, ymax, length1max, length2max, length3max, widhtmax, heightMax, orientationMax),
				new SimpleRightTrapezoidBuilder(), TransformToSurface.class, (new GM_Envelope(xmin, xmax, ymin, ymax)).getGeom());
		
		

		List<Kernel<GraphConfiguration<RightTrapezoid>, BirthDeathModification<RightTrapezoid>>> kernels = new ArrayList<>(
				3);
		KernelFactory<RightTrapezoid, GraphConfiguration<RightTrapezoid>, BirthDeathModification<RightTrapezoid>> factory = new KernelFactory<>();
		// TODO Use a KernelProposalRatio to propose only birth when size is 0
		kernels.add(factory.make_uniform_birth_death_kernel(rng, new SimpleRightTrapezoidBuilder(), birth, 1.0, 1.0,
				"BirthDeath"));


		DirectSampler<RightTrapezoid, GraphConfiguration<RightTrapezoid>, BirthDeathModification<RightTrapezoid>> ds = new DirectSampler<>(
				distribution, birth);
		
	IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<>();

		Sampler<GraphConfiguration<RightTrapezoid>, BirthDeathModification<RightTrapezoid>> s = new GreenSamplerBlockTemperature<>(
				rng, ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);
		
		
		for(int i=0; i < nbSample ; i++){
			GraphConfiguration<RightTrapezoid> gC = new GraphConfiguration<>(new ConstantEnergy<RightTrapezoid,RightTrapezoid>(5.0), new ConstantEnergy<RightTrapezoid,RightTrapezoid>(0.0));
			s.sample(rng	, gC, new SimpleTemperature(1000));

		
			featColl.addAll(exportColl(gC));
		}
		
		
	System.out.println(featColl.size());
		ShapefileWriter.write(featColl,"test_tra.shp");

	}
	
	public static  IFeatureCollection<IFeature> exportColl(GraphConfiguration<RightTrapezoid> gC ){
		
		
	IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
		for (GraphVertex<RightTrapezoid> v : gC.getGraph().vertexSet()) {

			IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());
			// On ajoute des attributs aux entités (dimension des objets)
			AttributeManager.addAttribute(feat, "x",v.getValue().centerx,
					"Double");
			AttributeManager.addAttribute(feat, "y",v.getValue().centery,
					"Double");
			AttributeManager.addAttribute(feat, "lenght1",v.getValue().length1,
					"Double");
			AttributeManager.addAttribute(feat, "lenght2",v.getValue().length2,
					"Double");
			AttributeManager.addAttribute(feat, "lenght3",v.getValue().length3,
					"Double");
			AttributeManager.addAttribute(feat, "width", v.getValue().width, "Double");
			AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height, "Double");
			AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation, "Double");
			
			System.out.println("Orientaiton : " +  v.getValue().orientation);
			
			featC.add(feat);
	
		}
		
		return featC;
	}

}
