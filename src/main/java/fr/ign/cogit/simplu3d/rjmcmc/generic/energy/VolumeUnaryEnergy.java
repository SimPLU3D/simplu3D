package fr.ign.cogit.simplu3d.rjmcmc.generic.energy;

import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.rjmcmc.energy.UnaryEnergy;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see http://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class VolumeUnaryEnergy<T extends ISimPLU3DPrimitive> implements UnaryEnergy<T> {

	@Override
	public double getValue(T t) {

		return t.getVolume();

	}

}
