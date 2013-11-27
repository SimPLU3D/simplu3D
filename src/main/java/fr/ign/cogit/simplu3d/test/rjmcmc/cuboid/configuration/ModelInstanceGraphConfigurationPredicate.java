package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.configuration;

import org.apache.log4j.Logger;

import fr.ign.cogit.simplu3d.checker.VeryFastRuleChecker;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.configuration.ConfigurationPredicate;

/**
 * A predicate on the current graph configuration. It needs to check the entire configuration
 * buildings since it is used in a RejectionSampler that allows to sample from unchecked
 * configurations (no guarantee on the previously generated buildings).
 * @author JPerret
 * @param <O>
 */
public class ModelInstanceGraphConfigurationPredicate<O extends AbstractBuilding> implements
    ConfigurationPredicate<O> {
  /**
   * Logger.
   */
  static Logger LOGGER = Logger.getLogger(ModelInstanceGraphConfiguration.class.getName());
  /**
   * The rule checker.
   */
  VeryFastRuleChecker vFR;

  public VeryFastRuleChecker getRuleChecker() {
    return vFR;
  }

  public ModelInstanceGraphConfigurationPredicate(BasicPropertyUnit bPU) {
    this.vFR = new VeryFastRuleChecker(bPU);
  }

  @Override
  public boolean check(Configuration<O> c) {
    ModelInstanceGraphConfiguration<O> conf = (ModelInstanceGraphConfiguration<O>) c;
    return this.vFR.check(conf.getBuildings());
  }
}
