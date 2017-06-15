package fr.ign.cogit.simplu3d.test.rjmcmc.paramshapes;

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
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.TransformToSurface;
import fr.ign.cogit.simplu3d.rjmcmc.generic.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.paramshp.builder.CuboidRoofedBuilder;
import fr.ign.cogit.simplu3d.rjmcmc.paramshp.geometry.impl.CuboidRoofed;
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

public class TestCuboidRoofed {

  public static void main(String[] args) throws ParseException {
    RandomGenerator rng = Random.random();
    PoissonDistribution distribution = new PoissonDistribution(rng, 20.0);

    IPolygon polygon = (IPolygon) WktGeOxygene.makeGeOxygene(
        "POLYGON (( 24.46264278127928 -13.32945321889124, 16.83388492956696 -2.4038452403082764, 21.54914732032382 4.036513147066944, 30.596317435922344 9.7484976692033, 35.119902493721604 1.4680368854351595, 23.657597982857375 1.1996886192945253, 32.66643263186438 -8.729197227908939, 41.17690621518163 -1.7138068416609316, 39.98850675084454 -16.47296147939581, 20.092399589846092 -22.60663613403888, 13.307022003147196 -13.942820684355546, 24.46264278127928 -13.32945321889124 ))");

    IEnvelope env = polygon.envelope();

    double xmin = env.getLowerCorner().getX();
    double xmax = env.getUpperCorner().getX();

    double ymin = env.getLowerCorner().getY();
    double ymax = env.getUpperCorner().getY();

    double lmin = 10;
    double lmax = 25;

    double wmin = 4;
    double wmax = 15;

    double hgmin = 1;
    double hgmax = 15;

    double htmin = 1;
    double htmax = 3;

    double orientationMin = 0;
    double orientationMax = Math.PI;

    double deltaMin = 0;
    double deltaMax = 2;

    int nbSample = 10;

    CuboidRoofedBuilder builder = new CuboidRoofedBuilder();

    UniformBirth<CuboidRoofed> birth = new UniformBirth<CuboidRoofed>(rng,
        new CuboidRoofed(xmin, ymin, lmin, wmin, hgmin, orientationMin, htmin,
            deltaMin),
        new CuboidRoofed(xmax, ymax, lmax, wmax, hgmax, orientationMax, htmax,
            deltaMax),
        builder, TransformToSurface.class, (IGeometry) polygon);

    List<Kernel<GraphConfiguration<CuboidRoofed>, BirthDeathModification<CuboidRoofed>>> kernels = new ArrayList<>(
        3);
    KernelFactory<CuboidRoofed, GraphConfiguration<CuboidRoofed>, BirthDeathModification<CuboidRoofed>> factory = new KernelFactory<>();
    // TODO Use a KernelProposalRatio to propose only birth when size is 0

    kernels.add(factory.make_uniform_birth_death_kernel(rng, builder, birth,
        1.0, 1.0, "BirthDeath"));

    DirectSampler<CuboidRoofed, GraphConfiguration<CuboidRoofed>, BirthDeathModification<CuboidRoofed>> ds = new DirectSampler<>(
        distribution, birth);

    IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<>();

    Sampler<GraphConfiguration<CuboidRoofed>, BirthDeathModification<CuboidRoofed>> s = new GreenSamplerBlockTemperature<>(
        rng, ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);

    for (int i = 0; i < nbSample; i++) {
      GraphConfiguration<CuboidRoofed> gC = new GraphConfiguration<>(
          new ConstantEnergy<CuboidRoofed, CuboidRoofed>(5.0),
          new ConstantEnergy<CuboidRoofed, CuboidRoofed>(0.0));
      s.sample(rng, gC, new SimpleTemperature(1000));

      featColl.addAll(exportColl(gC));
    }

    System.out.println(featColl.size());
    ShapefileWriter.write(featColl, "test_tra.shp");

  }

  public static IFeatureCollection<IFeature> exportColl(
      GraphConfiguration<CuboidRoofed> gC) {

    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
    for (GraphVertex<CuboidRoofed> v : gC.getGraph().vertexSet()) {

      IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());
      // On ajoute des attributs aux entités (dimension des objets)
      AttributeManager.addAttribute(feat, "x", v.getValue().centerx, "Double");
      AttributeManager.addAttribute(feat, "y", v.getValue().centery, "Double");

      featC.add(feat);

    }

    return featC;
  }

}
