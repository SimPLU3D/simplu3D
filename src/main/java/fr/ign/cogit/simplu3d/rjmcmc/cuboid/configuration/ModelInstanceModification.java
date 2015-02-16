package fr.ign.cogit.simplu3d.rjmcmc.cuboid.configuration;

import java.util.ArrayList;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
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
public class ModelInstanceModification<T extends AbstractSimpleBuilding>
		extends
		AbstractBirthDeathModification<T, ModelInstanceGraphConfiguration<T>, ModelInstanceModification<T>> {
	/**
	 * Create a new empty configuration.
	 */
	public ModelInstanceModification() {
	}
}
