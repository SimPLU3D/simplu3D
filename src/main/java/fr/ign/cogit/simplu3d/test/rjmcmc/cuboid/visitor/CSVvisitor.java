package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.SimpleGreenSampler;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.birth.UniformBirthInGeom;
import fr.ign.mpp.configuration.Configuration;
import fr.ign.mpp.configuration.Modification;
import fr.ign.rjmcmc.distribution.UniformDistribution;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;

public class CSVvisitor<O, C extends Configuration<O>, T extends Temperature, S extends Sampler<O, C, T>>
    implements Visitor<O, C, T, S> {
  private int dump;
  private int save;
  private int iter;
  private BufferedWriter writer;
  private String textSeparator = ";";

  public CSVvisitor(String fileName) {

    Path path = Paths.get(fileName);

    try {
      writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
          StandardOpenOption.CREATE);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void init(int dump, int s) {
    this.iter = 0;
    this.dump = dump;
    this.save = s;
  }

  @Override
  public void begin(C config, S sampler, T t) {

    String s = "Iteration" + textSeparator + "NBCube" + textSeparator
        + "ENERGIE" + textSeparator + "ENERGIE_MOY" + textSeparator + "NB_EVAL"
        + textSeparator + "NB_FALSE";

    SimpleGreenSampler<Cuboid2, Configuration<Cuboid2>, UniformDistribution, SimpleTemperature, UniformBirthInGeom<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>> sGS = (SimpleGreenSampler<Cuboid2, Configuration<Cuboid2>, UniformDistribution, SimpleTemperature, UniformBirthInGeom<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>>) sampler;

    List<List<Integer>> llInt = sGS.getvFR().getlFalseArray();

    double max = 0;

    for (List<Integer> lInt : llInt) {
      max = Math.max(lInt.size(), max);
    }

    for (int i = 0; i < max; i++) {
      s = s + textSeparator + "FALSE_" + i;
    }

    try {
      writer.append(s);
      writer.newLine();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  @SuppressWarnings({ "unchecked" })
  @Override
  public void end(C config, S sampler, T t) {
    try {
      writer.flush();
      writer.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  String formatInt = "%1$-10d";

  double energyMoy = 0;

  @SuppressWarnings({ "unchecked" })
  @Override
  public void visit(C config, S sampler, T t) {

    ++iter;

    energyMoy = (iter - 1) * energyMoy / iter + config.getEnergy() / iter;

    if ((dump > 0) && (iter % dump == 0)) {
      doWrite(config, sampler);
    }

    if ((dump > 0) && (iter % save == 0)) {
      try {
        writer.flush();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

  }

  public void doWrite(C config, S sampler) {

    StringBuffer sB = new StringBuffer();

    sB.append(iter);
    sB.append(textSeparator);
    sB.append(config.size());
    sB.append(textSeparator);

    sB.append(config.getEnergy());
    sB.append(textSeparator);

    sB.append(energyMoy);
    sB.append(textSeparator);

    SimpleGreenSampler<Cuboid2, Configuration<Cuboid2>, UniformDistribution, SimpleTemperature, UniformBirthInGeom<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>> sGS = (SimpleGreenSampler<Cuboid2, Configuration<Cuboid2>, UniformDistribution, SimpleTemperature, UniformBirthInGeom<Cuboid2, Configuration<Cuboid2>, Modification<Cuboid2, Configuration<Cuboid2>>>>) sampler;

    sB.append(sGS.getvFR().evalCount);
    sB.append(textSeparator);

    sB.append(sGS.getvFR().evalFalse);
    sB.append(textSeparator);

    for (Integer i : sGS.getvFR().getlFalseArray().get(0)) {
      sB.append(i);
      sB.append(textSeparator);

    }

    try {
      writer.append(sB.toString());
      writer.newLine();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
