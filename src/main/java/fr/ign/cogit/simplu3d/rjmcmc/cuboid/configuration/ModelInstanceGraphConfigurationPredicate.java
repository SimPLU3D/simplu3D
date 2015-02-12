package fr.ign.cogit.simplu3d.rjmcmc.cuboid.configuration;

import org.apache.log4j.Logger;

import fr.ign.cogit.simplu3d.checker.VeryFastRuleChecker;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.configuration.ConfigurationPredicate;

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
 * @author JPerret
 * @version 1.0
 *
 * A predicate on the current graph configuration. It needs to check the entire configuration
 * buildings since it is used in a RejectionSampler that allows to sample from unchecked
 * configurations (no guarantee on the previously generated buildings).
 * @param <O>
 */
public class ModelInstanceGraphConfigurationPredicate<O extends AbstractSimpleBuilding> implements
    ConfigurationPredicate<ModelInstanceGraphConfiguration<O>> {
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
  public boolean check(ModelInstanceGraphConfiguration<O> conf) {
    return this.vFR.check(conf.getBuildings());
  }
}
