package fr.ign.cogit.simplu3d.rjmcmc.paramshp.optimizer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.TransformToSurface;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.optimizer.DefaultSimPLU3DOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.generic.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.PrepareVisitors;
import fr.ign.cogit.simplu3d.rjmcmc.paramshp.builder.CuboidRoofedBuilder;
import fr.ign.cogit.simplu3d.rjmcmc.paramshp.geometry.impl.CuboidRoofed;
import fr.ign.cogit.simplu3d.rjmcmc.paramshp.transform.MoveRCuboid;
import fr.ign.mpp.DirectRejectionSampler;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.kernel.KernelFactory;
import fr.ign.mpp.kernel.UniformBirth;
import fr.ign.parameters.Parameters;
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
import fr.ign.rjmcmc.kernel.ChangeValue;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.SimulatedAnnealing;
import fr.ign.simulatedannealing.endtest.EndTest;
import fr.ign.simulatedannealing.schedule.Schedule;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;

public class OptimisedRCuboidDirectRejection
    extends DefaultSimPLU3DOptimizer<CuboidRoofed> {
  public GraphConfiguration<CuboidRoofed> process(RandomGenerator rG,
      BasicPropertyUnit bpu, Parameters p, Environnement env, int id,
      ConfigurationModificationPredicate<GraphConfiguration<CuboidRoofed>, BirthDeathModification<CuboidRoofed>> pred,
      IGeometry polygon) {
    // Géométrie de l'unité foncière sur laquelle porte la génération
    IGeometry geom = bpu.generateGeom().buffer(1);

    // Configuration que l'on optimise
    GraphConfiguration<CuboidRoofed> conf = null;

    try {
      // On créer la configuration (notamment la fonction énergétique)
      conf = create_configuration(p, geom, bpu);
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Création de l'échantilloneur
    Sampler<GraphConfiguration<CuboidRoofed>, BirthDeathModification<CuboidRoofed>> samp = create_sampler(
        rG, p, bpu, pred, polygon);
    // Température
    Schedule<SimpleTemperature> sch = create_schedule(p);

    EndTest end = create_end_test(p);
    PrepareVisitors<CuboidRoofed> pv = new PrepareVisitors<>(env);

    SimulatedAnnealing.optimize(rG, conf, samp, sch, end,
        pv.prepare(p, bpu.getId()));
    return conf;
  }

  public GraphConfiguration<CuboidRoofed> create_configuration(Parameters p,
      IGeometry geom, BasicPropertyUnit bpu) throws Exception {

    return this.create_configuration(p,
        AdapterFactory.toGeometry(new GeometryFactory(), geom), bpu);

  }

  public GraphConfiguration<CuboidRoofed> create_configuration(Parameters p,
      Geometry geom, BasicPropertyUnit bpu) {
    // Énergie constante : à la création d'un nouvel objet

    double energyCrea = Double.isNaN(this.energyCreation)
        ? p.getDouble("energy") : this.energyCreation;

    ConstantEnergy<CuboidRoofed, CuboidRoofed> energyCreation = new ConstantEnergy<CuboidRoofed, CuboidRoofed>(
        energyCrea);

    // Énergie constante : pondération de l'intersection
    ConstantEnergy<CuboidRoofed, CuboidRoofed> ponderationVolume = new ConstantEnergy<CuboidRoofed, CuboidRoofed>(
        p.getDouble("ponderation_volume"));
    // Énergie unaire : aire dans la parcelle
    UnaryEnergy<CuboidRoofed> energyVolume = new VolumeUnaryEnergy<CuboidRoofed>();
    // Multiplication de l'énergie d'intersection et de l'aire
    UnaryEnergy<CuboidRoofed> energyVolumePondere = new MultipliesUnaryEnergy<CuboidRoofed>(
        ponderationVolume, energyVolume);

    // On retire de l'énergie de création, l'énergie de l'aire
    UnaryEnergy<CuboidRoofed> u3 = new MinusUnaryEnergy<CuboidRoofed>(
        energyCreation, energyVolumePondere);

    // Énergie constante : pondération de la différence
    ConstantEnergy<CuboidRoofed, CuboidRoofed> ponderationDifference = new ConstantEnergy<CuboidRoofed, CuboidRoofed>(
        p.getDouble("ponderation_difference_ext"));
    // On ajoute l'énergie de différence : la zone en dehors de la parcelle
    UnaryEnergy<CuboidRoofed> u4 = new DifferenceVolumeUnaryEnergy<CuboidRoofed>(
        geom);
    UnaryEnergy<CuboidRoofed> u5 = new MultipliesUnaryEnergy<CuboidRoofed>(
        ponderationDifference, u4);
    UnaryEnergy<CuboidRoofed> unaryEnergy = new PlusUnaryEnergy<CuboidRoofed>(
        u3, u5);

    // Énergie binaire : intersection entre deux rectangles
    ConstantEnergy<CuboidRoofed, CuboidRoofed> c3 = new ConstantEnergy<CuboidRoofed, CuboidRoofed>(
        p.getDouble("ponderation_volume_inter"));
    BinaryEnergy<CuboidRoofed, CuboidRoofed> b1 = new IntersectionVolumeBinaryEnergy<CuboidRoofed>();
    BinaryEnergy<CuboidRoofed, CuboidRoofed> binaryEnergy = new MultipliesBinaryEnergy<CuboidRoofed, CuboidRoofed>(
        c3, b1);
    // empty initial configuration*/
    GraphConfiguration<CuboidRoofed> conf = new GraphConfiguration<>(
        unaryEnergy, binaryEnergy);
    return conf;
  }

  /**
   * Sampler
   * 
   * @param p les paramètres chargés depuis le fichier xmlg
   * @param r l'enveloppe dans laquelle on génère les positions
   * @return
   */
  public Sampler<GraphConfiguration<CuboidRoofed>, BirthDeathModification<CuboidRoofed>> create_sampler(
      RandomGenerator rng, Parameters p, BasicPropertyUnit bpU,
      ConfigurationModificationPredicate<GraphConfiguration<CuboidRoofed>, BirthDeathModification<CuboidRoofed>> pred,
      IGeometry polygon) {

    // On créé les bornes min et max pour le sampler (8 paramètres dans le cas
    // du CuboidRoofed)
    IEnvelope env = polygon.envelope();

    double xmin = env.getLowerCorner().getX();
    double xmax = env.getUpperCorner().getX();

    double ymin = env.getLowerCorner().getY();
    double ymax = env.getUpperCorner().getY();

    double lmin = p.getDouble("minlen");
    double lmax = p.getDouble("maxlen");

    double wmin = p.getDouble("minwid");
    double wmax = p.getDouble("maxwid");

    double hgmin = p.getDouble("minheightG");
    double hgmax = p.getDouble("maxheightG");

    double htmin = p.getDouble("minheightT");
    double htmax = p.getDouble("maxheightT");

    double orientationMin = 0;
    double orientationMax = Math.PI;

    double deltaMin = 0;
    double deltaMax = 1;

    // A priori on redéfini le constructeur de l'objet
    // A priori on redéfini le constructeur de l'objet
    CuboidRoofedBuilder builder = new CuboidRoofedBuilder();

    // On initialise la surface sur laquelle on fait la simulation
    if (samplingSurface == null) {
      samplingSurface = bpU.getpol2D();
    }

    // On initialise l'espace sur lequel on va calculer les objets (normalement
    // tu as juste à changer le nom des classes)
    UniformBirth<CuboidRoofed> birth = new UniformBirth<CuboidRoofed>(rng,
        new CuboidRoofed(xmin, ymin, lmin, wmin, hgmin, orientationMin, htmin,
            deltaMin),
        new CuboidRoofed(xmax, ymax, lmax, wmax, hgmax, orientationMax, htmax,
            deltaMax),
        builder, TransformToSurface.class, /* (IGeometry) */ polygon);

    // La distribution de poisson qui drive le nombre total d'objets
    PoissonDistribution distribution = new PoissonDistribution(rng,
        p.getDouble("poisson"));

    // Le sampler qui détermine comment on tire aléatoirement un objet dans
    // l'espace défini
    DirectSampler<CuboidRoofed, GraphConfiguration<CuboidRoofed>, BirthDeathModification<CuboidRoofed>> ds = new DirectRejectionSampler<>(
        distribution, birth, pred);

    // Probabilité de naissance-morts modifications
    List<Kernel<GraphConfiguration<CuboidRoofed>, BirthDeathModification<CuboidRoofed>>> kernels = new ArrayList<>(
        3);
    KernelFactory<CuboidRoofed, GraphConfiguration<CuboidRoofed>, BirthDeathModification<CuboidRoofed>> factory = new KernelFactory<>();

    // On liste les kernels, pour le premier, tu devrais probablement le définir
    // toi ....
    kernels.add(factory.make_uniform_birth_death_kernel(rng, builder, birth,
        p.getDouble("pbirth"), 1.0, "BirthDeath"));
    double amplitudeMove = p.getDouble("amplitudeMove");
    kernels.add(factory.make_uniform_modification_kernel(rng, builder,
        new MoveRCuboid(amplitudeMove), 0.2, "Move"));

    // Pour les autres, le ChangeValue peut être utiliser (attention, le
    // deuxième arguement est la taille de ton builder +1)
    // car il utilise un tableau pour stocker les paramètres et le +1 est pour
    // stocker de manière temporaire le tirage aléatoire
    double amplitudeMaxDim = p.getDouble("amplitudeMaxDim");
    kernels.add(factory.make_uniform_modification_kernel(rng, builder,
        new ChangeValue(amplitudeMaxDim, builder.size() + 1, 2), 0.2,
        "h1Change"));

    kernels.add(factory.make_uniform_modification_kernel(rng, builder,
        new ChangeValue(amplitudeMaxDim, builder.size() + 1, 3), 0.2,
        "h2Change"));

    kernels.add(factory.make_uniform_modification_kernel(rng, builder,
        new ChangeValue(amplitudeMaxDim, builder.size() + 1, 4), 0.2,
        "l1Change"));

    kernels.add(factory.make_uniform_modification_kernel(rng, builder,
        new ChangeValue(amplitudeMaxDim, builder.size() + 1, 5), 0.2,
        "l2Change"));

    double amplitudeHeight = p.getDouble("amplitudeHeight");

    kernels.add(factory.make_uniform_modification_kernel(rng, builder,
        new ChangeValue(amplitudeHeight, builder.size() + 1, 6), 0.2,
        "heightChange"));

    double amplitudeRotate = p.getDouble("amplitudeRotate") * Math.PI / 180;
    kernels.add(factory.make_uniform_modification_kernel(rng, builder,
        new ChangeValue(amplitudeRotate, builder.size() + 1, 7), 0.2,
        "Rotate"));

    kernels.add(factory.make_uniform_modification_kernel(rng, builder,
        new ChangeValue(amplitudeHeight, builder.size() + 1, 8), 0.2,
        "changeHeightGutter"));

    // kernels.add(factory.make_uniform_modification_kernel(rng, builder,
    // new ChangeValue(amplitudeHeight, builder.size() + 1, 9), 0.2,
    // "changeShift"));

    // On instancie le sampler avec tous les objets.
    Sampler<GraphConfiguration<CuboidRoofed>, BirthDeathModification<CuboidRoofed>> s = new GreenSamplerBlockTemperature<>(
        rng, ds, new MetropolisAcceptance<SimpleTemperature>(), kernels);
    return s;
  }

}
