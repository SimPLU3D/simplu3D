package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.loader.LoaderCuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.sampler.GreenSamplerBlockTemperature;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.ChangeHeight;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.ChangeLength;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.ChangeWidth;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.MoveCuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.RotateCuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.birth.TransformToSurface;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor.CSVendStats;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor.CSVvisitor;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor.CountVisitor;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor.FilmVisitor;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor.ShapefileVisitorCuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor.StatsV⁮isitor;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor.ViewerVisitor;
import fr.ign.mpp.DirectRejectionSampler;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.UniformBirth;
import fr.ign.parameters.Parameters;
import fr.ign.random.Random;
import fr.ign.rjmcmc.acceptance.MetropolisAcceptance;
import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;
import fr.ign.rjmcmc.configuration.Modification;
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
import fr.ign.simulatedannealing.ParallelTempering;
import fr.ign.simulatedannealing.endtest.EndTest;
import fr.ign.simulatedannealing.endtest.MaxIterationEndTest;
import fr.ign.simulatedannealing.schedule.GeometricSchedule;
import fr.ign.simulatedannealing.schedule.Schedule;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;
import fr.ign.simulatedannealing.visitor.CompositeVisitor;
import fr.ign.simulatedannealing.visitor.OutputStreamVisitor;
import fr.ign.simulatedannealing.visitor.Visitor;

/**
 * 
 * @author MBrasebin
 * 
 */
public class OptimisedBuildingsCuboidFinalDirectRejectionParallelTempering {

  private double minDimBox = Double.NaN;
  private double maxDimBox = Double.NaN;

  private int numberOfReplicas = 1;

  private double tempMax = 1;

  public void setMinDimBox(double minDimBox) {
    this.minDimBox = minDimBox;
  }

  public void setMaxDimBox(double maxDimBox) {
    this.maxDimBox = maxDimBox;
  }

  public OptimisedBuildingsCuboidFinalDirectRejectionParallelTempering(
      int nbReplicas, double tempMax) {
    numberOfReplicas = nbReplicas;
    this.tempMax = tempMax;
  }


  public void process(BasicPropertyUnit bpu, Parameters p, Environnement env,
      int id, ConfigurationModificationPredicate<Cuboid2> pred) {
    // Géométrie de l'unité foncière sur laquelle porte la génération
    IGeometry geom = bpu.generateGeom().buffer(1);

    // Définition de la fonction d'optimisation (on optimise en décroissant)
    // relative au volume

    // Création de l'échantilloneur
    Schedule<SimpleTemperature>[] samp = new Schedule[this.numberOfReplicas];
    // Température

    int loadExistingConfig = Integer.parseInt(p.get("load_existing_config"));

    Configuration<Cuboid2>[] tabConfig = new Configuration[this.numberOfReplicas];

    Visitor<Cuboid2>[] tabVisitor = new Visitor[this.numberOfReplicas];

    Sampler<Cuboid2> sampler = create_sampler(p, bpu, pred);

    for (int i = 0; i < this.numberOfReplicas; i++) {

      Configuration<Cuboid2> conf = null;
      try {
        conf = create_configuration(p,
            AdapterFactory.toGeometry(new GeometryFactory(), geom), bpu);
      } catch (Exception e) {
        e.printStackTrace();
      }

      if (loadExistingConfig == 1) {
        String configPath = p.get("config_shape_file").toString();
        List<Cuboid2> lCuboid = LoaderCuboid2.loadFromShapeFile(configPath);
        Modification<Cuboid2, Configuration<Cuboid2>> m = new Modification<>();
        for (Cuboid2 c : lCuboid) {
          m.insertBirth(c);
        }

        conf.deltaEnergy(m);
        conf.apply(m);

        System.out.println("First update OK");
      }

      tabConfig[i] = conf;
      samp[i] = new GeometricSchedule<SimpleTemperature>(new SimpleTemperature(
          i * Math.log(this.tempMax) / (this.numberOfReplicas - 1)), 1);

      List<Visitor<Cuboid2>> list = new ArrayList<Visitor<Cuboid2>>();
      if (i == 0) {

        if (Boolean.parseBoolean(p.get("outputstreamvisitor"))) {
          Visitor<Cuboid2> visitor = new OutputStreamVisitor<Cuboid2>(
              System.out);
          list.add(visitor);
        }

        if (Boolean.parseBoolean(p.get("shapefilewriter"))) {
          Visitor<Cuboid2> shpVisitor = new ShapefileVisitorCuboid2<Cuboid2>(p
              .get("result").toString() + "result");
          list.add(shpVisitor);
        }

        if (Boolean.parseBoolean(p.get("visitorviewer"))) {
          ViewerVisitor<Cuboid2> visitorViewer = new ViewerVisitor<Cuboid2>(""
              + id, p);
          list.add(visitorViewer);
        }

        if (Boolean.parseBoolean(p.get("filmvisitor"))) {
          IDirectPosition dpCentre = new DirectPosition(Double.parseDouble(p
              .get("filmvisitorx")), Double.parseDouble(p.get("filmvisitory")),
              Double.parseDouble(p.get("filmvisitorz")));
          Vecteur viewTo = new Vecteur(Double.parseDouble(p
              .get("filmvisitorvectx")), Double.parseDouble(p
              .get("filmvisitorvecty")), Double.parseDouble(p
              .get("filmvisitorvectz")));
          Color c = new Color(Integer.parseInt(p.get("filmvisitorr")),
              Integer.parseInt(p.get("filmvisitorg")), Integer.parseInt(p
                  .get("filmvisitorb")));
          FilmVisitor<Cuboid2> visitorViewerFilmVisitor = new FilmVisitor<Cuboid2>(
              dpCentre, viewTo, p.get("result"), c);
          list.add(visitorViewerFilmVisitor);
        }
        if (Boolean.parseBoolean(p.get("statsvisitor"))) {
          StatsV⁮isitor<Cuboid2> statsViewer = new StatsV⁮isitor<Cuboid2>(
              "Énergie");
          list.add(statsViewer);
        }
        if (Boolean.parseBoolean(p.get("csvvisitorend"))) {
          String fileName = p.get("result").toString() + p.get("csvfilenamend");
          CSVendStats<Cuboid2> statsViewer = new CSVendStats<Cuboid2>(fileName);
          list.add(statsViewer);
        }
        if (Boolean.parseBoolean(p.get("csvvisitor"))) {
          String fileName = p.get("result").toString() + p.get("csvfilename");
          CSVvisitor<Cuboid2> statsViewer = new CSVvisitor<Cuboid2>(fileName);
          list.add(statsViewer);
        }
      }

      countV = new CountVisitor<>();
      list.add(countV);
      CompositeVisitor<Cuboid2> mVisitor = new CompositeVisitor<Cuboid2>(list);
      init_visitor(p, mVisitor);

      tabVisitor[i] = mVisitor;

    }

    /*
     * < This is the way to launch the optimization process. Here, the magic
     * happen... >
     */

    EndTest<Cuboid2> end = create_end_test(p);

    // Partie spécial Parrallel tempering

    ParallelTempering.optimize(Random.random(), tabConfig, sampler, samp, end,
        tabVisitor);

  }

  // Initialisation des visiteurs
  // nbdump => affichage dans la console
  // nbsave => sauvegarde en shapefile
  static void init_visitor(Parameters p, Visitor<?> v) {
    v.init(Integer.parseInt(p.get("nbdump")), Integer.parseInt(p.get("nbsave")));
  }

  CountVisitor<Cuboid2> countV = null;

  public int getCount() {
    return countV.getCount();
  }

  // Création de la configuration
  /**
   * @param p paramètres importés depuis le fichier XML
   * @param bpu l'unité foncière considérée
   * @return la configuration chargée, c'est à dire la formulation énergétique
   *         prise en compte
   */
  public static Configuration<Cuboid2> create_configuration(Parameters p,
      Geometry geom, BasicPropertyUnit bpu) {
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
    UnaryEnergy<Cuboid2> u4 = new DifferenceVolumeUnaryEnergy<Cuboid2>(geom);
    UnaryEnergy<Cuboid2> u5 = new MultipliesUnaryEnergy<Cuboid2>(
        ponderationDifference, u4);
    UnaryEnergy<Cuboid2> unaryEnergy = new PlusUnaryEnergy<Cuboid2>(u3, u5);

    // Énergie binaire : intersection entre deux rectangles
    ConstantEnergy<Cuboid2, Cuboid2> c3 = new ConstantEnergy<Cuboid2, Cuboid2>(
        Double.parseDouble(p.get("ponderation_volume_inter")));
    BinaryEnergy<Cuboid2, Cuboid2> b1 = new IntersectionVolumeBinaryEnergy<Cuboid2>();
    BinaryEnergy<Cuboid2, Cuboid2> binaryEnergy = new MultipliesBinaryEnergy<Cuboid2, Cuboid2>(
        c3, b1);
    // empty initial configuration*/
    Configuration<Cuboid2> conf = new GraphConfiguration<>(unaryEnergy,
        binaryEnergy);
    return conf;
  }

  /**
   * Sampler
   * @param p les paramètres chargés depuis le fichier xml
   * @param r l'enveloppe dans laquelle on génère les positions
   * @return
   */
  Sampler<Cuboid2> create_sampler(Parameters p, BasicPropertyUnit bpU,
      ConfigurationModificationPredicate<Cuboid2> pred) {
    // Un vecteur ?????
    double mindim = Double.isNaN(this.minDimBox) ? Double.parseDouble(p
        .get("mindim")) : this.minDimBox;
    double maxdim = Double.isNaN(this.maxDimBox) ? Double.parseDouble(p
        .get("maxdim")) : this.maxDimBox;

    double minheight = Double.parseDouble(p.get("minheight"));
    double maxheight = Double.parseDouble(p.get("maxheight"));
    // A priori on redéfini le constructeur de l'objet
    ObjectBuilder<Cuboid2> builder = new ObjectBuilder<Cuboid2>() {
      @Override
      public Cuboid2 build(double[] coordinates) {
        return new Cuboid2(coordinates[0], coordinates[1], coordinates[2],
            coordinates[3], coordinates[4], coordinates[5]);
      }

      @Override
      public int size() {
        return 6;
      }

      @Override
      public void setCoordinates(Cuboid2 t, double[] coordinates) {
        coordinates[0] = t.centerx;
        coordinates[1] = t.centery;
        coordinates[2] = t.length;
        coordinates[3] = t.width;
        coordinates[4] = t.height;
        coordinates[5] = t.orientation;
      }
    };

    IEnvelope env = bpU.getGeom().envelope();
    // Sampler de naissance
    // UniformBirthInGeom<Cuboid2> birth = new UniformBirthInGeom<Cuboid2>(new
    // Cuboid2(env.minX(),
    // env.minY(), mindim, mindim, minheight, 0), new Cuboid2(env.maxX(),
    // env.maxY(), maxdim,
    // maxdim, maxheight, Math.PI), builder, bpU.getpol2D());
    UniformBirth<Cuboid2> birth = new UniformBirth<Cuboid2>(new Cuboid2(
        env.minX(), env.minY(), mindim, mindim, minheight, 0), new Cuboid2(
        env.maxX(), env.maxY(), maxdim, maxdim, maxheight, Math.PI), builder,
        TransformToSurface.class, bpU.getpol2D());

    // Distribution de poisson
    PoissonDistribution distribution = new PoissonDistribution(
        Double.parseDouble(p.get("poisson")));

    DirectSampler<Cuboid2> ds = new DirectRejectionSampler<Cuboid2>(
        distribution, birth, pred);

    // Probabilité de naissance-morts modifications
    List<Kernel<Cuboid2>> kernels = new ArrayList<Kernel<Cuboid2>>(3);

    kernels.add(Kernel.make_uniform_birth_death_kernel(builder, birth,
        Double.parseDouble(p.get("pbirth")),
        Double.parseDouble(p.get("pdeath"))));
    double amplitudeMove = Double.parseDouble(p.get("amplitudeMove"));
    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new MoveCuboid2(amplitudeMove), 0.2, "Move"));
    double amplitudeRotate = Double.parseDouble(p.get("amplitudeRotate"))
        * Math.PI / 180;
    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new RotateCuboid2(amplitudeRotate), 0.2, "Rotate"));
    double amplitudeMaxDim = Double.parseDouble(p.get("amplitudeMaxDim"));
    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new ChangeWidth(amplitudeMaxDim), 0.2, "ChgWidth"));
    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new ChangeLength(amplitudeMaxDim), 0.2, "ChgLength"));
    double amplitudeHeight = Double.parseDouble(p.get("amplitudeHeight"));
    kernels.add(Kernel.make_uniform_modification_kernel(builder,
        new ChangeHeight(amplitudeHeight), 0.2, "ChgHeight"));

    Sampler<Cuboid2> s = new GreenSamplerBlockTemperature<Cuboid2>(ds,
        new MetropolisAcceptance<SimpleTemperature>(), kernels);
    return s;
  }

  private static EndTest<Cuboid2> create_end_test(Parameters p) {
    return new MaxIterationEndTest<Cuboid2>(Integer.parseInt(p.get("nbiter")));
  }

}
