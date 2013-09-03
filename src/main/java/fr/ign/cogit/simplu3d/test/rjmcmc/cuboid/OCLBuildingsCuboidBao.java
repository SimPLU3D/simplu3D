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
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboidSnap.DifferenceVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboidSnap.DifferenceVolumeExtUnaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboidSnap.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.CuboidSnap;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.ParameterCuboidSNAP;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.ChangeHeight;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.ChangeLengthSNAP;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.ChangeWidthSNAP;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.MoveCuboidSnap;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor.ShapefileVisitorCuboidSnap;
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
import fr.ign.simulatedannealing.visitor.ShapefileVisitor;
import fr.ign.simulatedannealing.visitor.Visitor;

public class OCLBuildingsCuboidBao<O, C extends Configuration<O>, S extends Sampler<O, C, SimpleTemperature>, V extends Visitor<O, C, SimpleTemperature, S>> {

  // [building_footprint_rectangle_cli_main
  public static void main(String[] args) throws Exception {

    ParameterCuboidSNAP.SNAPPING_FACTOR = 0.03;
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
    Configuration<CuboidSnap> conf = create_configuration(p,
        AdapterFactory.toGeometry(new GeometryFactory(), geom));
    Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature> samp = create_sampler(
        p, bpu);
    Schedule<SimpleTemperature> sch = create_schedule(p);
    EndTest<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>> end = create_end_test(p);

    Visitor<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>> visitor = new OutputStreamVisitor<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>>(
        System.out);
   // Visitor<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>> shpVisitor = new ShapefileVisitorCuboidSnap<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>>(
    //    "result");

    ViewerVisitor<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>> visitorViewer = new ViewerVisitor<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>>(0+"");

    StatsV⁮isitor<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>> statsViewer = new StatsV⁮isitor<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>>(
        "Énergie");

    List<Visitor<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>>> list = new ArrayList<Visitor<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>>>();
    list.add(visitor);
    list.add(visitorViewer);
    list.add(statsViewer);
    //list.add(shpVisitor);

    CompositeVisitor<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>> mVisitor = new CompositeVisitor<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>>(
        list);
    init_visitor(p, mVisitor);
    /*
     * < This is the way to launch the optimization process. Here, the magic
     * happen... >
     */
    SimulatedAnnealing.optimize(conf, samp, sch, end, mVisitor);
  }

  // Initialisation des visiteurs
  // nbdump => affichage dans la console
  // nbsave => sauvegarde en shapefile
  static void init_visitor(Parameters p, Visitor<?, ?, ?, ?> v) {
    v.init(Integer.parseInt(p.get("nbdump")), Integer.parseInt(p.get("nbsave")));
  }

  // ]

  // Création de la configuration
  /**
   * 
   * @param p paramètres importés depuis le fichier XML
   * @param bpu l'unité foncière considérée
   * @return la configuration chargée, c'est à dire la formulation énergétique
   *         prise en compte
   */
  public static Configuration<CuboidSnap> create_configuration(Parameters p,
      Geometry bpu) {

    // Énergie constante : à la création d'un nouvel objet
    ConstantEnergy<CuboidSnap, CuboidSnap> energyCreation = new ConstantEnergy<CuboidSnap, CuboidSnap>(
        Double.parseDouble(p.get("energy")));
    // Énergie constante : pondération de l'intersection
    ConstantEnergy<CuboidSnap, CuboidSnap> ponderationVolume = new ConstantEnergy<CuboidSnap, CuboidSnap>(
        Double.parseDouble(p.get("ponderation_volume")));
    // Énergie constante : pondération de la différence

    // Énergie unaire : aire dans la parcelle
    VolumeUnaryEnergy<CuboidSnap> energyVolume = new VolumeUnaryEnergy<CuboidSnap>();
    // Multiplication de l'énergie d'intersection et de l'aire
    UnaryEnergy<CuboidSnap> energyVolumePondere = new MultipliesUnaryEnergy<CuboidSnap>(
        ponderationVolume, energyVolume);

    // On retire de l'énergie de création, l'énergie de l'aire
    UnaryEnergy<CuboidSnap> u3 = new MinusUnaryEnergy<CuboidSnap>(
        energyCreation, energyVolumePondere);

    UnaryEnergy<CuboidSnap> uDiff = new DifferenceVolumeExtUnaryEnergy<CuboidSnap>(
        bpu);
    ConstantEnergy<CuboidSnap, CuboidSnap> ponderationDifference = new ConstantEnergy<CuboidSnap, CuboidSnap>(
        Double.parseDouble(p.get("ponderation_difference_ext")));

    UnaryEnergy<CuboidSnap> energydiffPondere = new MultipliesUnaryEnergy<CuboidSnap>(
        uDiff, ponderationDifference);

    UnaryEnergy<CuboidSnap> uFin = new PlusUnaryEnergy<CuboidSnap>(u3,
        energydiffPondere);

    // Énergie binaire : intersection entre deux rectangles
    ConstantEnergy<CuboidSnap, CuboidSnap> c3 = new ConstantEnergy<CuboidSnap, CuboidSnap>(
        Double.parseDouble(p.get("ponderation_volume_inter")));
    BinaryEnergy<CuboidSnap, CuboidSnap> b1 = new DifferenceVolumeBinaryEnergy<CuboidSnap, CuboidSnap>();
    BinaryEnergy<CuboidSnap, CuboidSnap> b2 = new MultipliesBinaryEnergy<CuboidSnap, CuboidSnap>(
        c3, b1);
    // empty initial configuration*/
    return new GraphConfiguration<CuboidSnap>(uFin, b2);
  }

  // ]

  /**
   * Sampler
   * @param p les paramètres chargés depuis le fichier xml
   * @param r l'enveloppe dans laquelle on génère les positions
   * @return
   */
  static Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature> create_sampler(
      Parameters p, BasicPropertyUnit bpU) {

    IEnvelope r = bpU.generateGeom().envelope();

    double deltaXParcel = r.maxX() - r.minX();
    double deltaYParcel = r.maxY() - r.minY();

    ParameterCuboidSNAP.SNAPX = deltaXParcel
        * ParameterCuboidSNAP.SNAPPING_FACTOR;
    ParameterCuboidSNAP.SNAPY = deltaYParcel
        * ParameterCuboidSNAP.SNAPPING_FACTOR;

    ParameterCuboidSNAP.X0 = r.minX();
    ParameterCuboidSNAP.Y0 = r.minY();

    // Un vecteur ?????

    double mindim = Double.parseDouble(p.get("mindim"));
    double maxdim = Double.parseDouble(p.get("maxdim"));

    double minheight = Double.parseDouble(p.get("minheight"));
    double maxheight = Double.parseDouble(p.get("maxheight"));

    // A priori on redéfini le constructeur de l'objet
    ObjectBuilder<CuboidSnap> builder = new ObjectBuilder<CuboidSnap>() {
      @Override
      public CuboidSnap build(double[] coordinates) {
        return new CuboidSnap(coordinates[0], coordinates[1], coordinates[2],
            coordinates[3], coordinates[4]);
      }

      @Override
      public int size() {
        return 5;
      }

      @Override
      public void setCoordinates(CuboidSnap t, double[] coordinates) {
        coordinates[0] = t.centerx;
        coordinates[1] = t.centery;
        coordinates[2] = t.length;
        coordinates[3] = t.width;
        coordinates[4] = t.height;

      }
    };

    // Sampler de naissance
    UniformBirth<CuboidSnap, Configuration<CuboidSnap>, Modification<CuboidSnap, Configuration<CuboidSnap>>> birth = new UniformBirth<CuboidSnap, Configuration<CuboidSnap>, Modification<CuboidSnap, Configuration<CuboidSnap>>>(
        new CuboidSnap(0.0, 0.0, mindim / ParameterCuboidSNAP.SNAPX, mindim
            / ParameterCuboidSNAP.SNAPY, minheight), new CuboidSnap(
            1 + (int) (deltaXParcel / ParameterCuboidSNAP.SNAPX),
            1 + (int) (deltaYParcel / ParameterCuboidSNAP.SNAPY), maxdim
                / ParameterCuboidSNAP.SNAPX,
            maxdim / ParameterCuboidSNAP.SNAPX, maxheight), builder);

    // Distribution de poisson
    PoissonDistribution distribution = new PoissonDistribution(
        Double.parseDouble(p.get("poisson")));

    DirectSampler<CuboidSnap, Configuration<CuboidSnap>, PoissonDistribution, UniformBirth<CuboidSnap, Configuration<CuboidSnap>, Modification<CuboidSnap, Configuration<CuboidSnap>>>> ds = new DirectSampler<CuboidSnap, Configuration<CuboidSnap>, PoissonDistribution, UniformBirth<CuboidSnap, Configuration<CuboidSnap>, Modification<CuboidSnap, Configuration<CuboidSnap>>>>(
        distribution, birth);

    // Probabilité de naissance-morts modifications
    List<Kernel<CuboidSnap, Configuration<CuboidSnap>, Modification<CuboidSnap, Configuration<CuboidSnap>>>> kernels = new ArrayList<Kernel<CuboidSnap, Configuration<CuboidSnap>, Modification<CuboidSnap, Configuration<CuboidSnap>>>>(
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
     * kernels.add(Kernel.make_uniform_modification_kernel(builder, new
     * MoveCuboidSnap(), 0.2));
     * 
     * kernels.add(Kernel.make_uniform_modification_kernel(builder, new
     * ChangeWidth(), 0.2));
     * 
     * kernels.add(Kernel.make_uniform_modification_kernel(builder, new
     * ChangeHeight(), 0.2));
     * 
     * 
     * 
     * kernels.add(Kernel.make_uniform_modification_kernel(builder, new
     * MoveCuboidSnap(), 0.2));
     * 
     * kernels.add(Kernel.make_uniform_modification_kernel(builder, new
     * ChangeWidth(), 0.2));
     */

    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new ChangeWidthSNAP(mindim, maxdim), 0.2));

    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new ChangeLengthSNAP(mindim, maxdim), 0.2));

    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new ChangeHeight(2), 0.2));

    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new MoveCuboidSnap(), 0.2));

    Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature> s = new SimpleGreenSampler<CuboidSnap, Configuration<CuboidSnap>, PoissonDistribution, SimpleTemperature, UniformBirth<CuboidSnap, Configuration<CuboidSnap>, Modification<CuboidSnap, Configuration<CuboidSnap>>>>(
        ds, new MetropolisAcceptance<SimpleTemperature>(), kernels, bpU);
    return s;
  }

  private static EndTest<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>> create_end_test(
      Parameters p) {
    return new MaxIterationEndTest<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature, Sampler<CuboidSnap, Configuration<CuboidSnap>, SimpleTemperature>>(
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
