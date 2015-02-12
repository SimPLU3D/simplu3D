package fr.ign.cogit.simplu3d.rjmcmc.cuboid.configuration;

import org.apache.log4j.Logger;

import fr.ign.cogit.simplu3d.checker.VeryFastRuleChecker;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
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
 * Predicate on the current configuration.
 * 
 * @param <O>
 */
public class GraphConfigurationPredicate<O extends AbstractSimpleBuilding>
		implements ConfigurationPredicate<GraphConfigurationWithPredicate<O>> {
	/**
	 * Logger.
	 */
	static Logger LOGGER = Logger.getLogger(GraphConfigurationPredicate.class
			.getName());
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
	public boolean check(GraphConfigurationWithPredicate<O> c) {
		return this.vFR.check(c.getCurrent());
	}
}
