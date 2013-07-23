package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.checker.FastRuleChecker;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid.IntersectionAreaBinaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.Cuboid;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor.ViewerVisitor;
import fr.ign.cogit.simplu3d.test.rjmcmc.rectangle2d.energy.DifferenceAreaUnaryEnergy;
import fr.ign.geometry.Vector2D;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.Configuration;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.Modification;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.UniformBirth;
import fr.ign.parameters.Parameters;
import fr.ign.rjmcmc.acceptance.MetropolisAcceptance;
import fr.ign.rjmcmc.distribution.PoissonDistribution;
import fr.ign.rjmcmc.energy.BinaryEnergy;
import fr.ign.rjmcmc.energy.ConstantEnergy;
import fr.ign.rjmcmc.energy.MinusUnaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesBinaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesUnaryEnergy;
import fr.ign.rjmcmc.energy.PlusUnaryEnergy;
import fr.ign.rjmcmc.energy.UnaryEnergy;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.kernel.RectangleCornerTranslationTransform;
import fr.ign.rjmcmc.kernel.RectangleScaledEdgeTransform;
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

public class OCLBuildings<O, C extends Configuration<O>, S extends Sampler<O, C, SimpleTemperature>, V extends Visitor<O, C, SimpleTemperature, S>> {

  // Initialisation des visiteurs
  // nbdump => affichage dans la console
  // nbsave => sauvegarde en shapefile
  static void init_visitor(Parameters p, Visitor<?, ?, ?, ?> v) {
    v.init(Integer.parseInt(p.get("nbdump")), Integer.parseInt(p.get("nbsave")));
  }

  // ]

  // [building_footprint_rectangle_cli_main
  public static void main(String[] args) throws Exception {
    /*
     * < Retrieve the singleton instance of the parameters object... initialize
     * the parameters object with the default values provided... parse the
     * command line to eventually change the values >
     */
    Parameters p = initialize_parameters();
    Environnement env = LoaderSHP.load(p.get("folder"));
    BasicPropertyUnit bpu = env.getBpU().get(Integer.parseInt(p.get("bpu")));
    IGeometry geom = bpu.generateGeom().buffer(1);
    
    

    
    /*
     * < Before launching the optimization process, we create all the required
     * stuffs: a configuration, a sampler, a schedule scheme and an end test >
     */
    Configuration<Cuboid> conf = create_configuration(p,
        AdapterFactory.toGeometry(new GeometryFactory(), geom));
    Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature> samp = create_sampler(
        p, bpu);
    Schedule<SimpleTemperature> sch = create_schedule(p);
    EndTest<Cuboid, Configuration<Cuboid>, SimpleTemperature, Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature>> end = create_end_test(p);

    Visitor<Cuboid, Configuration<Cuboid>, SimpleTemperature, Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature>> visitor = new OutputStreamVisitor<Cuboid, Configuration<Cuboid>, SimpleTemperature, Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature>>(
        System.out);
    // Visitor<Cuboid, Configuration<Cuboid>, SimpleTemperature, Sampler<Cuboid,
    // Configuration<Cuboid>, SimpleTemperature>> shpVisitor = new
    // ShapefileVisitor<Cuboid, Configuration<Cuboid>, SimpleTemperature,
    // Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature>>(
    // "result");

    ViewerVisitor<Cuboid, Configuration<Cuboid>, SimpleTemperature, Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature>> visitorViewer = new ViewerVisitor<Cuboid, Configuration<Cuboid>, SimpleTemperature, Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature>>();

    List<Visitor<Cuboid, Configuration<Cuboid>, SimpleTemperature, Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature>>> list = new ArrayList<Visitor<Cuboid, Configuration<Cuboid>, SimpleTemperature, Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature>>>();
    list.add(visitor);
    list.add(visitorViewer);
    // list.add(shpVisitor);

    CompositeVisitor<Cuboid, Configuration<Cuboid>, SimpleTemperature, Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature>> mVisitor = new CompositeVisitor<Cuboid, Configuration<Cuboid>, SimpleTemperature, Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature>>(
        list);
    init_visitor(p, mVisitor);
    /*
     * < This is the way to launch the optimization process. Here, the magic
     * happen... >
     */
    SimulatedAnnealing.optimize(conf, samp, sch, end, mVisitor);
    return;
  }

  // Création de la configuration
  /**
   * 
   * @param p paramètres importés depuis le fichier XML
   * @param bpu l'unité foncière considérée
   * @return la configuration chargée, c'est à dire la formulation énergétique
   *         prise en compte
   */
  public static Configuration<Cuboid> create_configuration(Parameters p,
      Geometry bpu) {

    // Énergie constante : à la création d'un nouvel objet
    ConstantEnergy<Cuboid, Cuboid> energyCreation = new ConstantEnergy<Cuboid, Cuboid>(
        Double.parseDouble(p.get("energy")));
    // Énergie constante : pondération de l'intersection
    ConstantEnergy<Cuboid, Cuboid> ponderationVolume = new ConstantEnergy<Cuboid, Cuboid>(
        Double.parseDouble(p.get("ponderation_volume")));
    // Énergie constante : pondération de la différence
    ConstantEnergy<Cuboid, Cuboid> ponderationDifference = new ConstantEnergy<Cuboid, Cuboid>(
        Double.parseDouble(p.get("ponderation_difference")));
    // Énergie unaire : aire dans la parcelle
    UnaryEnergy<Cuboid> energyVolume = new VolumeUnaryEnergy<Cuboid>();
    // Multiplication de l'énergie d'intersection et de l'aire
    UnaryEnergy<Cuboid> energyVolumePondere = new MultipliesUnaryEnergy<Cuboid>(ponderationVolume, energyVolume);
    
    
    
    // On retire de l'énergie de création, l'énergie de l'aire
    UnaryEnergy<Cuboid> u3 = new MinusUnaryEnergy<Cuboid>(energyCreation, energyVolumePondere);
    // On ajoute l'énergie de différence : la zone en dehors de la parcelle
    UnaryEnergy<Cuboid> u4 = new DifferenceAreaUnaryEnergy<Cuboid>(bpu);
    UnaryEnergy<Cuboid> u5 = new MultipliesUnaryEnergy<Cuboid>(ponderationDifference, u4);
    UnaryEnergy<Cuboid> u6 = new PlusUnaryEnergy<Cuboid>(u3, u5);
    
    
    // Énergie binaire : intersection entre deux rectangles
    ConstantEnergy<Cuboid, Cuboid> c3 = new ConstantEnergy<Cuboid, Cuboid>(
        Double.parseDouble(p.get("ponderation_surface")));
    BinaryEnergy<Cuboid, Cuboid> b1 = new IntersectionAreaBinaryEnergy<Cuboid>();
    BinaryEnergy<Cuboid, Cuboid> b2 = new MultipliesBinaryEnergy<Cuboid, Cuboid>(
        c3, b1);
    // empty initial configuration*/
    return new GraphConfiguration<Cuboid>(u6, b2);
  }

  // ]

  /**
   * Sampler
   * @param p les paramètres chargés depuis le fichier xml
   * @param r l'enveloppe dans laquelle on génère les positions
   * @return
   */
  static Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature> create_sampler(
      Parameters p,  BasicPropertyUnit bpU) {
    
    IEnvelope r = bpU.generateGeom().envelope();
    
    // Un vecteur ?????
    Vector2D v = new Vector2D(Double.parseDouble(p.get("maxsize")),
        Double.parseDouble(p.get("maxsize")));
    double minratio = Double.parseDouble(p.get("minratio"));
    double maxratio = Double.parseDouble(p.get("maxratio"));

    double minheight = Double.parseDouble(p.get("minheight"));
    double maxheight = Double.parseDouble(p.get("maxheight"));
    // A priori on redéfini le constructeur de l'objet
    ObjectBuilder<Cuboid> builder = new ObjectBuilder<Cuboid>() {
      @Override
      public Cuboid build(double[] coordinates) {
        return new Cuboid(coordinates[0], coordinates[1], coordinates[2],
            coordinates[3], coordinates[4], coordinates[5]);
      }

      @Override
      public int size() {
        return 6;
      }

      @Override
      public void setCoordinates(Cuboid t, double[] coordinates) {
        coordinates[0] = t.centerx;
        coordinates[1] = t.centery;
        coordinates[2] = t.normalx;
        coordinates[3] = t.normaly;
        coordinates[4] = t.ratio;
        coordinates[5] = t.height;
      }
    };
    Vector2D n = v.negate();

    // Sampler de naissance
    UniformBirth<Cuboid, Configuration<Cuboid>, Modification<Cuboid, Configuration<Cuboid>>> birth = new UniformBirth<Cuboid, Configuration<Cuboid>, Modification<Cuboid, Configuration<Cuboid>>>(
        new Cuboid(r.minX(), r.minY(), n.x(), n.y(), minratio, minheight),
        new Cuboid(r.maxX(), r.maxY(), v.x(), v.y(), maxratio, maxheight),
        builder);

    // Distribution de poisson
    PoissonDistribution distribution = new PoissonDistribution(
        Double.parseDouble(p.get("poisson")));

    DirectSampler<Cuboid, Configuration<Cuboid>, PoissonDistribution, UniformBirth<Cuboid, Configuration<Cuboid>, Modification<Cuboid, Configuration<Cuboid>>>> ds = new DirectSampler<Cuboid, Configuration<Cuboid>, PoissonDistribution, UniformBirth<Cuboid, Configuration<Cuboid>, Modification<Cuboid, Configuration<Cuboid>>>>(
        distribution, birth);

    // Probabilité de naissance-morts modifications
    List<Kernel<Cuboid, Configuration<Cuboid>, Modification<Cuboid, Configuration<Cuboid>>>> kernels = new ArrayList<Kernel<Cuboid, Configuration<Cuboid>, Modification<Cuboid, Configuration<Cuboid>>>>(
        3);
    kernels.add(Kernel.make_uniform_birth_death_kernel(builder, birth,
        Double.parseDouble(p.get("pbirth")),
        Double.parseDouble(p.get("pdeath"))));
    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new RectangleScaledEdgeTransform(), 0.4));
    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new RectangleCornerTranslationTransform(), 0.4));

    Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature> s = new SimpleGreenSampler<Cuboid, Configuration<Cuboid>, PoissonDistribution, SimpleTemperature, UniformBirth<Cuboid, Configuration<Cuboid>, Modification<Cuboid, Configuration<Cuboid>>>>(
        ds, new MetropolisAcceptance<SimpleTemperature>(), kernels, bpU);
    return s;
  }

  private static EndTest<Cuboid, Configuration<Cuboid>, SimpleTemperature, Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature>> create_end_test(
      Parameters p) {
    return new MaxIterationEndTest<Cuboid, Configuration<Cuboid>, SimpleTemperature, Sampler<Cuboid, Configuration<Cuboid>, SimpleTemperature>>(
        Integer.parseInt(p.get("nbiter")));
  }

  private static Schedule<SimpleTemperature> create_schedule(Parameters p) {
    return new GeometricSchedule<SimpleTemperature>(new SimpleTemperature(
        Double.parseDouble(p.get("temp"))),
        Double.parseDouble(p.get("deccoef")));
  }

  private static Parameters initialize_parameters() {
    return Parameters
        .unmarshall("./src/main/resources/building_parameters.xml");
  }
  // ]
}
