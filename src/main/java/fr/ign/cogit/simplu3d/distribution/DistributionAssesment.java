package fr.ign.cogit.simplu3d.distribution;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.SimpleGreenSampler;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.Configuration;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.Modification;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.UniformBirth;
import fr.ign.parameters.Parameters;
import fr.ign.rjmcmc.acceptance.MetropolisAcceptance;
import fr.ign.rjmcmc.distribution.PoissonDistribution;
import fr.ign.rjmcmc.energy.ConstantEnergy;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.kernel.KernelFunctor;
import fr.ign.rjmcmc.kernel.RandomApply;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;

public class DistributionAssesment {

  /**
   * @param args
   * @throws CloneNotSupportedException
   * @throws IOException
   */
  public static void main(String[] args) throws CloneNotSupportedException,
      IOException {

    String fileOut = "C:/Users/mbrasebin/Desktop/Distrib/testproj1.csv";
    String seperator = ",";
    /*
     * < Retrieve the singleton instance of the parameters object... initialize
     * the parameters object with the default values provided... parse the
     * command line to eventually change the values >
     */
    Parameters p = initialize_parameters();
    Environnement env = LoaderSHP.load(p.get("folder"));
    BasicPropertyUnit bpu = env.getBpU().get(Integer.parseInt(p.get("bpu")));

    SimpleGreenSampler<Cuboid2, Configuration<Cuboid2>, PoissonDistribution, SimpleTemperature, UniformBirth<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>> samp = create_sampler(
        p, bpu);

    Configuration<Cuboid2> config = new GraphConfiguration<>(
        new ConstantEnergy<Cuboid2, Cuboid2>(0),
        new ConstantEnergy<Cuboid2, Cuboid2>(0));

    int count = 0;

    int testC = 1000000;

    Path path = Paths.get(fileOut);

    BufferedWriter writer = Files.newBufferedWriter(path,
        StandardCharsets.UTF_8, StandardOpenOption.CREATE);

    StringBuffer sB = new StringBuffer();
    sB.append("X");
    sB.append(seperator);
    sB.append("Y");
    sB.append(seperator);
    sB.append("L");
    sB.append(seperator);
    sB.append("W");
    sB.append(seperator);
    sB.append("H");
    sB.append(seperator);
    sB.append("O");

    
    writer.write(sB.toString());

    writer.newLine();
    
    int countTrue = 0;

    for (int i = 0; i < testC; i++) {
      
   
      count++;
      
      
      
      if (count % 5000 == 0) {
        System.out.println(count + "/" +  testC);
        writer.flush();
      }

      Modification<Cuboid2, Configuration<Cuboid2>> modif = new Modification<Cuboid2, Configuration<Cuboid2>>();

      KernelFunctor<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>> kf = new KernelFunctor<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>(
          config, modif);

      RandomApply.randomApply(0, samp.getKernels(), kf);

      boolean test = samp.checkNonUpdateConfiguration(kf);
      
      
      List<Cuboid2> lAB = (List<Cuboid2>) kf.getModif().getBirth();
      

      if (test) {
        
        countTrue++;
   


        if (lAB.size() != 1) {
          System.out.println("Probabilité supérieure à 1 ?");
        }

        Cuboid2 c1 = lAB.get(0);

        sB = new StringBuffer();
        sB.append(c1.centerx);
        sB.append(seperator);
        sB.append(c1.centery);
        sB.append(seperator);
        sB.append(c1.length);
        sB.append(seperator);
        sB.append(c1.width);
        sB.append(seperator);
        sB.append(c1.height);
        sB.append(seperator);
        sB.append(c1.orientation);


        writer.write(sB.toString());
        writer.newLine();
      }

    }

    System.out.println(countTrue);
    writer.flush();
    writer.close();

  }

  /**
   * Sampler
   * @param p les paramètres chargés depuis le fichier xml
   * @param r l'enveloppe dans laquelle on génère les positions
   * @return
   */
  static SimpleGreenSampler<Cuboid2, Configuration<Cuboid2>, PoissonDistribution, SimpleTemperature, UniformBirth<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>> create_sampler(
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

    // Sampler de naissance
    UniformBirth<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>> birth = new UniformBirth<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>(
        new Cuboid2(r.minX(), r.minY(), mindim, mindim, minheight, 0),
        new Cuboid2(r.maxX(), r.maxY(), maxdim, maxdim, maxheight, Math.PI),
        builder);

    // Distribution de poisson
    PoissonDistribution distribution = new PoissonDistribution(
        Double.parseDouble(p.get("poisson")));

    DirectSampler<Cuboid2, Configuration<Cuboid2>, PoissonDistribution, UniformBirth<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>> ds = new DirectSampler<Cuboid2, Configuration<Cuboid2>, PoissonDistribution, UniformBirth<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>>(
        distribution, birth);

    // Probabilité de naissance-morts modifications
    List<Kernel<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>> kernels = new ArrayList<Kernel<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>>(
        3);
    kernels.add(Kernel.make_uniform_birth_death_kernel(builder, birth, 1, 0));

    SimpleGreenSampler<Cuboid2, Configuration<Cuboid2>, PoissonDistribution, SimpleTemperature, UniformBirth<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>> s = new SimpleGreenSampler<Cuboid2, Configuration<Cuboid2>, PoissonDistribution, SimpleTemperature, UniformBirth<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>>(
        ds, new MetropolisAcceptance<SimpleTemperature>(), kernels, bpU);
    return s;
  }

  private static Parameters initialize_parameters() {
    return Parameters
        .unmarshall("./src/main/resources/building_parameters_project_1.xml");
  }
}
