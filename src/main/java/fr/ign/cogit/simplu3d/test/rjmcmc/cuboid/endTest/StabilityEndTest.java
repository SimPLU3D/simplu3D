package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.endTest;

import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.endtest.EndTest;
import fr.ign.simulatedannealing.temperature.Temperature;

public class StabilityEndTest<O extends SimpleObject> implements EndTest<O> {
  int iterations;
  double lastEnergy;
  int iterationCount;

  public StabilityEndTest(int n, double delta) {
    this.iterations = n;
    iterationCount = 0;
    lastEnergy = Double.POSITIVE_INFINITY;
  }

  @Override
  public boolean evaluate(Configuration<O> config, Sampler<O> sampler, Temperature t) {

    double currentEnergy = config.getEnergy();

    if (currentEnergy != 0 && Math.abs(currentEnergy - lastEnergy) > 40) {
      lastEnergy = currentEnergy;
      iterationCount = 0;
      return false;
    }

    iterationCount++;

    return iterationCount > iterations;

  }

  @Override
  public void stop() {
    this.iterationCount = 0;
  }

  @Override
  public String toString() {
    return "" + this.iterations;
  }
}
