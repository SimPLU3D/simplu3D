package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.Cuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.ChangeHeight;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.ChangeLength;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.ChangeWidth;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.MoveCuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.RotateCuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor.StatsV⁮isitor;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor.ViewerVisitor;
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

public class OCLBuildingsCuboid2<O, C extends Configuration<O>, S extends Sampler<O, C, SimpleTemperature>, V extends Visitor<O, C, SimpleTemperature, S>> {

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
    Configuration<Cuboid2> conf = create_configuration(p,
        AdapterFactory.toGeometry(new GeometryFactory(), geom));
    Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature> samp = create_sampler(
        p, bpu);
    Schedule<SimpleTemperature> sch = create_schedule(p);
    EndTest<Cuboid2, Configuration<Cuboid2>, SimpleTemperature, Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>> end = create_end_test(p);

    Visitor<Cuboid2, Configuration<Cuboid2>, SimpleTemperature, Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>> visitor = new OutputStreamVisitor<Cuboid2, Configuration<Cuboid2>, SimpleTemperature, Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>>(
        System.out);
    // Visitor<Cuboid2, Configuration<Cuboid2>, SimpleTemperature,
    // Sampler<Cuboid2,
    // Configuration<Cuboid2>, SimpleTemperature>> shpVisitor = new
    // ShapefileVisitor<Cuboid2, Configuration<Cuboid2>, SimpleTemperature,
    // Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>>(
    // "result");

    ViewerVisitor<Cuboid2, Configuration<Cuboid2>, SimpleTemperature, Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>> visitorViewer = new ViewerVisitor<Cuboid2, Configuration<Cuboid2>, SimpleTemperature, Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>>();

    StatsV⁮isitor<Cuboid2, Configuration<Cuboid2>, SimpleTemperature, Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>> statsViewer = new StatsV⁮isitor<Cuboid2, Configuration<Cuboid2>, SimpleTemperature, Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>>(
        "Énergie");

    List<Visitor<Cuboid2, Configuration<Cuboid2>, SimpleTemperature, Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>>> list = new ArrayList<Visitor<Cuboid2, Configuration<Cuboid2>, SimpleTemperature, Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>>>();
    list.add(visitor);
    list.add(visitorViewer);
    list.add(statsViewer);
    // list.add(shpVisitor);

    CompositeVisitor<Cuboid2, Configuration<Cuboid2>, SimpleTemperature, Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>> mVisitor = new CompositeVisitor<Cuboid2, Configuration<Cuboid2>, SimpleTemperature, Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>>(
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
  public static Configuration<Cuboid2> create_configuration(Parameters p,
      Geometry bpu) {

    // Énergie constante : à la création d'un nouvel objet
    ConstantEnergy<Cuboid2, Cuboid2> energyCreation = new ConstantEnergy<Cuboid2, Cuboid2>(
        Double.parseDouble(p.get("energy")));

    // Énergie constante : pondération de l'intersection
    ConstantEnergy<Cuboid2, Cuboid2> ponderationVolume = new ConstantEnergy<Cuboid2, Cuboid2>(
        Double.parseDouble(p.get("ponderation_volume")));

    // Énergie unaire : aire dans la parcelle
    UnaryEnergy<Cuboid2> energyVolume = new VolumeUnaryEnergy<Cuboid2>();
    // Multiplication de l'énergie d'intersection et de l'aire
    UnaryEnergy<Cuboid2> energyVolumePondere = new MultipliesUnaryEnergy<Cuboid2>(
        ponderationVolume, energyVolume);

    // On retire de l'énergie de création, l'énergie de l'aire
    UnaryEnergy<Cuboid2> u3 = new MinusUnaryEnergy<Cuboid2>(energyCreation,
        energyVolumePondere);

    // Énergie constante : pondération de la différence
    ConstantEnergy<Cuboid2, Cuboid2> ponderationDifference = new ConstantEnergy<Cuboid2, Cuboid2>(
        Double.parseDouble(p.get("ponderation_difference_ext")));
    // On ajoute l'énergie de différence : la zone en dehors de la parcelle
    UnaryEnergy<Cuboid2> u4 = new DifferenceVolumeUnaryEnergy<Cuboid2>(bpu);
    UnaryEnergy<Cuboid2> u5 = new MultipliesUnaryEnergy<Cuboid2>(
        ponderationDifference, u4);
    UnaryEnergy<Cuboid2> u6 = new PlusUnaryEnergy<Cuboid2>(u3, u5);

    // Énergie binaire : intersection entre deux rectangles
    ConstantEnergy<Cuboid2, Cuboid2> c3 = new ConstantEnergy<Cuboid2, Cuboid2>(
        Double.parseDouble(p.get("ponderation_volume_inter")));
    BinaryEnergy<Cuboid2, Cuboid2> b1 = new IntersectionVolumeBinaryEnergy<Cuboid2>();
    BinaryEnergy<Cuboid2, Cuboid2> b2 = new MultipliesBinaryEnergy<Cuboid2, Cuboid2>(
        c3, b1);
    // empty initial configuration*/
    return new GraphConfiguration<Cuboid2>(u6, b2);
  }

  // ]

  /**
   * Sampler
   * @param p les paramètres chargés depuis le fichier xml
   * @param r l'enveloppe dans laquelle on génère les positions
   * @return
   */
  static Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature> create_sampler(
      Parameters p, BasicPropertyUnit bpU) {

    IEnvelope r = bpU.generateGeom().envelope();

    // Un vecteur ?????

    double mindim = Double.parseDouble(p.get("mindim"));
    double maxdim = Double.parseDouble(p.get("maxdim"));

    double minheight = Double.parseDouble(p.get("minheight"));
    double maxheight = Double.parseDouble(p.get("maxheight"));
    // A priori on redéfini le constructeur de l'objet
    ObjectBuilder<Cuboid2> builder = new ObjectBuilder<Cuboid2>() {
      @Override
      public Cuboid2 build(double[] coordinates) {
        return new Cuboid2(coordinates[0], coordinates[1], coordinates[2],
            coordinates[3], coordinates[4]);
      }

      @Override
      public int size() {
        return 5;
      }

      @Override
      public void setCoordinates(Cuboid2 t, double[] coordinates) {
        coordinates[0] = t.centerx;
        coordinates[1] = t.centery;
        coordinates[2] = t.length;
        coordinates[3] = t.width;
        coordinates[4] = t.height;
      }
    };

    // Sampler de naissance
    UniformBirth<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>> birth = new UniformBirth<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>(
        new Cuboid2(r.minX(), r.minY(), mindim, mindim, minheight),
        new Cuboid2(r.maxX(), r.maxY(), maxdim, maxdim, maxheight), builder);

    // Distribution de poisson
    PoissonDistribution distribution = new PoissonDistribution(
        Double.parseDouble(p.get("poisson")));

    DirectSampler<Cuboid2, Configuration<Cuboid2>, PoissonDistribution, UniformBirth<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>> ds = new DirectSampler<Cuboid2, Configuration<Cuboid2>, PoissonDistribution, UniformBirth<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>>(
        distribution, birth);

    // Probabilité de naissance-morts modifications
    List<Kernel<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>> kernels = new ArrayList<Kernel<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>>(
        3);
    kernels.add(Kernel.make_uniform_birth_death_kernel(builder, birth,
        Double.parseDouble(p.get("pbirth")),
        Double.parseDouble(p.get("pdeath"))));

    /*
     * kernels.add(Kernel.make_uniform_modification_kernel(builder, new
     * RectangleScaledEdgeTransform(), 0.4));
     * kernels.add(Kernel.make_uniform_modification_kernel(builder, new
     * RectangleCornerTranslationTransform(), 0.4));
     */
    
    
/*
    kernels.add(Kernel.make_uniform_modification_kernel(builder,        new MoveCuboid2(), 0.2));

    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new ChangeWidth(), 0.2));



    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new MoveCuboid2(), 0.2));

    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new ChangeWidth(), 0.2));
*/
    double amplitudeMax = 1;
    double amplitudeHeight = 1;
    double amplitudeMove = 1;

    /*
     * kernels.add(Kernel.make_uniform_modification_kernel(builder, new
     * RotateCuboid2(), 0.2));
     */

    /*
     * 
     * kernels.add(Kernel.make_uniform_modification_kernel(builder, new
     * ChangeWidth(amplitudeMax), 0.2));
     * 
     * kernels.add(Kernel.make_uniform_modification_kernel(builder, new
     * ChangeLength(amplitudeMax), 0.2));
     * 
     * 
     * 
     * 
     * kernels.add(Kernel.make_uniform_modification_kernel(builder, new
     * MoveCuboid2(amplitudeMove), 0.2));
     * 
     */
    
    kernels.add(Kernel.make_uniform_modification_kernel(builder, new
         ChangeWidth(amplitudeMax), 0.2));
         
        kernels.add(Kernel.make_uniform_modification_kernel(builder, new
        ChangeLength(amplitudeMax), 0.2));
    kernels.add(Kernel.make_uniform_modification_kernel(builder, new
         MoveCuboid2(amplitudeMove), 0.2));
    kernels.add(Kernel.make_uniform_modification_kernel(builder, new 
         ChangeHeight(amplitudeHeight), 0.2));

    Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature> s = new SimpleGreenSampler<Cuboid2, Configuration<Cuboid2>, PoissonDistribution, SimpleTemperature, UniformBirth<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>>(
        ds, new MetropolisAcceptance<SimpleTemperature>(), kernels, bpU);
    return s;
  }

  private static EndTest<Cuboid2, Configuration<Cuboid2>, SimpleTemperature, Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>> create_end_test(
      Parameters p) {
    return new MaxIterationEndTest<Cuboid2, Configuration<Cuboid2>, SimpleTemperature, Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>>(
        Integer.parseInt(p.get("nbiter")));
  }

  private static Schedule<SimpleTemperature> create_schedule(Parameters p) {
    return new GeometricSchedule<SimpleTemperature>(new SimpleTemperature(
        Double.parseDouble(p.get("temp"))),
        Double.parseDouble(p.get("deccoef")));
  }

  private static Parameters initialize_parameters() {
    return Parameters
        .unmarshall("./src/main/resources/building_parameters2.xml");
  }
  // ]
}
