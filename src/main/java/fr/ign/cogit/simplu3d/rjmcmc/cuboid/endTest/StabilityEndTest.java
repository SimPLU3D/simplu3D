package fr.ign.cogit.simplu3d.rjmcmc.cuboid.endTest;

import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.configuration.Modification;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.endtest.EndTest;
import fr.ign.simulatedannealing.temperature.Temperature;
/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/ 
public class StabilityEndTest<O extends SimpleObject> implements EndTest {
  int iterations;
  double lastEnergy;
  int iterationCount;

  public StabilityEndTest(int n, double delta) {
    this.iterations = n;
    iterationCount = 0;
    lastEnergy = Double.POSITIVE_INFINITY;
  }

  @Override
  public <C extends Configuration<C, M>, M extends Modification<C, M>> boolean evaluate(
  		C config, Sampler<C, M> sampler, Temperature t) {
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
