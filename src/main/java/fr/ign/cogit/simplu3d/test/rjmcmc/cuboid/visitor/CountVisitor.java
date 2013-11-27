package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor;

import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;

public class CountVisitor<O extends SimpleObject> implements Visitor<O> {

  private int count = 0;

  public CountVisitor() {

  }

  @Override
  public void init(int d, int save) {

  }

  @Override
  public void begin(Configuration<O> config, Sampler<O> sampler, Temperature t) {
  }

  @Override
  public void end(Configuration<O> config, Sampler<O> sampler, Temperature t) {

  }

  @Override
  public void visit(Configuration<O> config, Sampler<O> sampler, Temperature t) {

    count++;
  }

  public int getCount() {
    return count;
  }

}
