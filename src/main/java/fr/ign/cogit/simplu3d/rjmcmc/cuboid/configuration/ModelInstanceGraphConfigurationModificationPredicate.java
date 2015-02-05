package fr.ign.cogit.simplu3d.rjmcmc.cuboid.configuration;

import java.util.List;

import org.apache.log4j.Logger;

import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import fr.ign.cogit.simplu3d.checker.VeryFastRuleChecker;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;

/**
 * A modification predicate used in a DirectRejectionSampler. When a modification is proposed, it
 * will first try to apply in it and check the predicate, then cancel the modification.
 * @author JPerret
 * @param <O>
 */
public class ModelInstanceGraphConfigurationModificationPredicate<O extends AbstractSimpleBuilding>
    implements ConfigurationModificationPredicate<ModelInstanceGraphConfiguration<O>, ModelInstanceModification<O>> {
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
  public boolean check(ModelInstanceGraphConfiguration<O> c, ModelInstanceModification<O> m) {
    return this.check(c, m, true);
  }

  public boolean check(ModelInstanceGraphConfiguration<O> c, ModelInstanceModification<O> m, boolean cancelUpdate) {
    List<IModelInstanceObject> list = c.update(m);
    boolean result = this.vFR.check(list);
    if (cancelUpdate) {
      c.cancelUpdate(m);
    }
    return result;
  }
}
