package fr.ign.cogit.simplu3d.rjmcmc.cuboid.visitor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import fr.ign.cogit.simplu3d.model.application.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.configuration.ModelInstanceGraphConfigurationModificationPredicate;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.configuration.ModelInstanceGraphConfigurationPredicate;
import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;

public class CSVvisitor<O extends AbstractSimpleBuilding> implements Visitor<O> {
  private int dump;
  private int save;
  private int iter;
  private BufferedWriter writer;
  private String textSeparator = ";";


  public CSVvisitor(String fileName) {

    Path path = Paths.get(fileName);
    try {
      writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
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
  public void begin(Configuration<O> config, Sampler<O> sampler, Temperature t) {

    String s = "Iteration" + textSeparator + "NBCube" + textSeparator + "ENERGIE" + textSeparator
        + "ENERGIE_MOY" + textSeparator + "NB_FALSE";





    try {
      writer.append(s);
      writer.newLine();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  @Override
  public void end(Configuration<O> config, Sampler<O> sampler, Temperature t) {
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

  @Override
  public void visit(Configuration<O> config, Sampler<O> sampler, Temperature t) {

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

  public void doWrite(Configuration<O> config, Sampler<O> sampler) {

    StringBuffer sB = new StringBuffer();

    sB.append(iter);
    sB.append(textSeparator);
    sB.append(config.size());
    sB.append(textSeparator);

    sB.append(config.getEnergy());
    sB.append(textSeparator);

    sB.append(energyMoy);
    sB.append(textSeparator);




    try {
      writer.append(sB.toString());
      writer.newLine();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
