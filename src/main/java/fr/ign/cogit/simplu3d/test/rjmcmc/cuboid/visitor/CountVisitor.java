package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.Formatter;

import fr.ign.mpp.configuration.Configuration;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;

public class CountVisitor<O, C extends Configuration<O>, T extends Temperature, S extends Sampler<O, C, T>>
    implements Visitor<O, C, T, S> {


  private int count = 0;
  
  public CountVisitor() {

  }

  @Override
  public void init(int d, int save) {

  }

  @Override
  public void begin(C config, S sampler, T t) {
  }

  @Override
  public void end(C config, S sampler, T t) {

  }

  @Override
  public void visit(C config, S sampler, T t) {
    
      count ++;
    }
  
  
  public int getCount(){
    return count;
  }
  
  
}
