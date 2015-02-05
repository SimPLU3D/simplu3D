package fr.ign.cogit.simplu3d.rjmcmc.cuboid.visitor;

import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.configuration.Modification;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;

public class CountVisitor<C extends Configuration<C, M>, M extends Modification<C, M>> implements Visitor<C, M> {

  private int count = 0;

  public CountVisitor() {

  }

  @Override
  public void init(int d, int save) {

  }

  @Override
  public void begin(C config, Sampler<C,M> sampler, Temperature t) {
  }

  @Override
  public void end(C config, Sampler<C,M> sampler, Temperature t) {

  }

  @Override
  public void visit(C config, Sampler<C,M> sampler, Temperature t) {
    count++;
  }

  public int getCount() {
    return count;
  }

}
