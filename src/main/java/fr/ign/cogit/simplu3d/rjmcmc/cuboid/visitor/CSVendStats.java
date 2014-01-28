package fr.ign.cogit.simplu3d.rjmcmc.cuboid.visitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;

public class CSVendStats<O extends SimpleObject> implements Visitor<O> {
//  private int dump;
//  private int save;
  private int iter;
  private BufferedWriter writer;
  private String textSeparator = ";";

  public CSVendStats(String fileName) {

    try {

      File f = new File(fileName);

      if (!f.exists()) {
        f.createNewFile();
      }

      Path path = Paths.get(fileName);

      writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void init(int dump, int s) {
    this.iter = 0;
//    this.dump = dump;
//    this.save = s;
  }

  @Override
  public void begin(Configuration<O> config, Sampler<O> sampler, Temperature t) {

  }

  @Override
  public void end(Configuration<O> config, Sampler<O> sampler, Temperature t) {
    try {
      doWrite(config, sampler);
      writer.flush();
      writer.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  String formatInt = "%1$-10d";

  double energyMoy = 0;

  private long currentTime;

  @Override
  public void visit(Configuration<O> config, Sampler<O> sampler, Temperature t) {

    if (iter == 0) {
      currentTime = System.currentTimeMillis();
    }

    ++iter;

  }

  public void doWrite(Configuration<O> config, Sampler<O> sampler) {

    StringBuffer sB = new StringBuffer();

    sB.append(iter);
    sB.append(textSeparator);
    sB.append((System.currentTimeMillis() - currentTime) / 1000);
    sB.append(textSeparator);

    sB.append(config.size());
    sB.append(textSeparator);

    sB.append(config.getEnergy());
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
