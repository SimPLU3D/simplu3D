package fr.ign.cogit.simplu3d.rjmcmc.cuboid.configuration;

import java.util.List;

import org.apache.log4j.Logger;

import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import fr.ign.cogit.simplu3d.checker.VeryFastRuleChecker;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;
import fr.ign.rjmcmc.configuration.Modification;

/**
 * A modification predicate used in a DirectRejectionSampler. When a modification is proposed, it
 * will first try to apply in it and check the predicate, then cancel the modification.
 * @author JPerret
 * @param <O>
 */
public class ModelInstanceGraphConfigurationModificationPredicate<O extends AbstractBuilding>
    implements ConfigurationModificationPredicate<O> {
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

  public ModelInstanceGraphConfigurationModificationPredicate(BasicPropertyUnit bPU) {
    this.vFR = new VeryFastRuleChecker(bPU);
  }

  @Override
  public boolean check(Configuration<O> c, Modification<O, Configuration<O>> m) {
    return this.check(c, m, true);
  }

  public boolean check(Configuration<O> c, Modification<O, Configuration<O>> m, boolean cancelUpdate) {
    ModelInstanceGraphConfiguration<O> conf = (ModelInstanceGraphConfiguration<O>) c;
    List<IModelInstanceObject> list = conf.update(m);
    boolean result = this.vFR.check(list);
    if (cancelUpdate) {
      conf.cancelUpdate(m);
    }
    return result;
  }
}
