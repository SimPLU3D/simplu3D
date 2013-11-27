package fr.ign.cogit.simplu3d.test.rjmcmc.rectangle2d;

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
import fr.ign.cogit.simplu3d.test.rjmcmc.rectangle2d.energy.DifferenceAreaUnaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.rectangle2d.energy.IntersectionAreaUnaryEnergy;
import fr.ign.geometry.Rectangle2D;
import fr.ign.geometry.Vector2D;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.energy.IntersectionAreaBinaryEnergy;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.UniformBirth;
import fr.ign.parameters.Parameters;
import fr.ign.random.Random;
import fr.ign.rjmcmc.acceptance.MetropolisAcceptance;
import fr.ign.rjmcmc.configuration.Configuration;
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
import fr.ign.simulatedannealing.visitor.ShapefileVisitor;
import fr.ign.simulatedannealing.visitor.Visitor;

public class TestBuildingsRectangle<O> {

  // [building_footprint_rectangle_init_visitor
  static void init_visitor(Parameters p, Visitor<?> v) {
    v.init(Integer.parseInt(p.get("nbdump")), Integer.parseInt(p.get("nbsave")));
  }

  // ]

  public static Configuration<Rectangle2D> create_configuration(Parameters p, Geometry bpu) {
    ConstantEnergy<Rectangle2D, Rectangle2D> c1 = new ConstantEnergy<Rectangle2D, Rectangle2D>(
        Double.parseDouble(p.get("energy")));
    ConstantEnergy<Rectangle2D, Rectangle2D> c2 = new ConstantEnergy<Rectangle2D, Rectangle2D>(
        Double.parseDouble(p.get("ponderation_intersection")));
    ConstantEnergy<Rectangle2D, Rectangle2D> c4 = new ConstantEnergy<Rectangle2D, Rectangle2D>(
        Double.parseDouble(p.get("ponderation_difference")));
    UnaryEnergy<Rectangle2D> u1 = new IntersectionAreaUnaryEnergy<Rectangle2D>(bpu);
    UnaryEnergy<Rectangle2D> u2 = new MultipliesUnaryEnergy<Rectangle2D>(c2, u1);
    UnaryEnergy<Rectangle2D> u3 = new MinusUnaryEnergy<Rectangle2D>(c1, u2);
    UnaryEnergy<Rectangle2D> u4 = new DifferenceAreaUnaryEnergy<Rectangle2D>(bpu);
    UnaryEnergy<Rectangle2D> u5 = new MultipliesUnaryEnergy<Rectangle2D>(c4, u4);
    UnaryEnergy<Rectangle2D> u6 = new PlusUnaryEnergy<Rectangle2D>(u3, u5);
    ConstantEnergy<Rectangle2D, Rectangle2D> c3 = new ConstantEnergy<Rectangle2D, Rectangle2D>(
        Double.parseDouble(p.get("ponderation_surface")));
    BinaryEnergy<Rectangle2D, Rectangle2D> b1 = new IntersectionAreaBinaryEnergy<Rectangle2D>();
    BinaryEnergy<Rectangle2D, Rectangle2D> b2 = new MultipliesBinaryEnergy<Rectangle2D, Rectangle2D>(
        c3, b1);
    // empty initial configuration
    return new GraphConfiguration<Rectangle2D>(u6, b2);
  }

  // ]

  // [building_footprint_rectangle_create_sampler
  static Sampler<Rectangle2D> create_sampler(Parameters p, IEnvelope r) {
    Vector2D v = new Vector2D(Double.parseDouble(p.get("maxsize")), Double.parseDouble(p
        .get("maxsize")));
    double minratio = Double.parseDouble(p.get("minratio"));
    double maxratio = Double.parseDouble(p.get("maxratio"));
    ObjectBuilder<Rectangle2D> builder = new ObjectBuilder<Rectangle2D>() {
      @Override
      public Rectangle2D build(double[] coordinates) {
        return new Rectangle2D(coordinates[0], coordinates[1], coordinates[2], coordinates[3],
            coordinates[4]);
      }

      @Override
      public int size() {
        return 5;
      }

      @Override
      public void setCoordinates(Rectangle2D t, double[] coordinates) {
        coordinates[0] = t.centerx;
        coordinates[1] = t.centery;
        coordinates[2] = t.normalx;
        coordinates[3] = t.normaly;
        coordinates[4] = t.ratio;
      }
    };
    Vector2D n = v.negate();
    UniformBirth<Rectangle2D> birth = new UniformBirth<Rectangle2D>(new Rectangle2D(r.minX(),
        r.minY(), n.x(), n.y(), minratio), new Rectangle2D(r.maxX(), r.maxY(), v.x(), v.y(),
        maxratio), builder);

    PoissonDistribution distribution = new PoissonDistribution(Double.parseDouble(p.get("poisson")));

    DirectSampler<Rectangle2D> ds = new DirectSampler<Rectangle2D>(distribution, birth);

    List<Kernel<Rectangle2D>> kernels = new ArrayList<Kernel<Rectangle2D>>(3);
    kernels.add(Kernel.make_uniform_birth_death_kernel(builder, birth,
        Double.parseDouble(p.get("pbirth")), Double.parseDouble(p.get("pdeath"))));
    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new RectangleScaledEdgeTransform(), 0.4, "ScaledEdge"));
    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new RectangleCornerTranslationTransform(0), 0.1, "CornTrans0"));
    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new RectangleCornerTranslationTransform(1), 0.1, "CornTrans1"));
    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new RectangleCornerTranslationTransform(2), 0.1, "CornTrans2"));
    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new RectangleCornerTranslationTransform(3), 0.1, "CornTrans3"));

    Sampler<Rectangle2D> s = new TestSampler<Rectangle2D>(ds,
        new MetropolisAcceptance<SimpleTemperature>(), kernels);
    return s;
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
    Configuration<Rectangle2D> conf = create_configuration(p,
        AdapterFactory.toGeometry(new GeometryFactory(), geom.buffer(0)));
    Sampler<Rectangle2D> samp = create_sampler(p, geom.getEnvelope());
    Schedule<SimpleTemperature> sch = create_schedule(p);
    EndTest<Rectangle2D> end = create_end_test(p);
    Visitor<Rectangle2D> visitor = new OutputStreamVisitor<Rectangle2D>(System.out);
    Visitor<Rectangle2D> shpVisitor = new ShapefileVisitor<Rectangle2D>("result");
    List<Visitor<Rectangle2D>> list = new ArrayList<Visitor<Rectangle2D>>();
    list.add(visitor);
    list.add(shpVisitor);
    CompositeVisitor<Rectangle2D> mVisitor = new CompositeVisitor<Rectangle2D>(list);
    init_visitor(p, mVisitor);
    /*
     * < This is the way to launch the optimization process. Here, the magic
     * happen... >
     */
    SimulatedAnnealing.optimize(Random.random(), conf, samp, sch, end, mVisitor);
    return;
  }

  private static EndTest<Rectangle2D> create_end_test(Parameters p) {
    return new MaxIterationEndTest<Rectangle2D>(Integer.parseInt(p.get("nbiter")));
  }

  private static Schedule<SimpleTemperature> create_schedule(Parameters p) {
    return new GeometricSchedule<SimpleTemperature>(new SimpleTemperature(Double.parseDouble(p
        .get("temp"))), Double.parseDouble(p.get("deccoef")));
  }

  private static Parameters initialize_parameters() {
    return Parameters.unmarshall("./src/main/resources/building_parameters.xml");
  }
  // ]
}
