package fr.ign.cogit.simplu3d.rjmcmc.generic.visitor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import fr.ign.mpp.configuration.ListConfiguration;
import fr.ign.rjmcmc.configuration.Modification;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;
/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see http://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class CSVvisitor<O extends SimpleObject, C extends ListConfiguration<O, C, M>, M extends Modification<C, M>> implements Visitor<C,M> {
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
  public void begin(C config, Sampler<C,M> sampler, Temperature t) {

    String s = "Iteration" + textSeparator + "NBCube" + textSeparator
        + "ENERGIE" + textSeparator + "ENERGIE_MOY" + textSeparator
        + "NB_FALSE";

    try {
      writer.append(s);
      writer.newLine();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  @Override
  public void end(C config, Sampler<C,M> sampler, Temperature t) {
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
  public void visit(C config, Sampler<C,M> sampler, Temperature t) {

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

  public void doWrite(C config, Sampler<C,M> sampler) {

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
