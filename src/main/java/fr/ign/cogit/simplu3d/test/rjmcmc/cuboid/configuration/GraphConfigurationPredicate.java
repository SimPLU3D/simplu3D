package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.configuration;

import org.apache.log4j.Logger;

import fr.ign.cogit.simplu3d.checker.VeryFastRuleChecker;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.configuration.ConfigurationPredicate;

/**
 * Predicate on the current configuration.
 * @author JPerret
 * @param <O>
 */
public class GraphConfigurationPredicate<O extends AbstractBuilding> implements
    ConfigurationPredicate<O> {
  /**
   * Logger.
   */
  static Logger LOGGER = Logger.getLogger(GraphConfigurationPredicate.class.getName());
  /**
   * The rule checker.
   */
  VeryFastRuleChecker vFR;

  public VeryFastRuleChecker getRuleChecker() {
    return vFR;
  }

  public GraphConfigurationPredicate(BasicPropertyUnit bPU) {
    this.vFR = new VeryFastRuleChecker(bPU);
  }

  @Override
  public boolean check(Configuration<O> c) {
    GraphConfigurationWithPredicate<O> conf = (GraphConfigurationWithPredicate<O>) c;
    return this.vFR.check(conf.getCurrent());
  }
}
