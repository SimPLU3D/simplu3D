package fr.ign.cogit.simplu3d.rjmcmc.generic.energy;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.rjmcmc.energy.UnaryEnergy;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class DifferenceVolumeUnaryEnergy<T extends ISimPLU3DPrimitive> implements UnaryEnergy<T> {
	Geometry bpu;

	public DifferenceVolumeUnaryEnergy(Geometry p) {
		this.bpu = p;
	}

	@Override
	public double getValue(T t) {

		Geometry difference = t.toGeometry().buffer(0.01).difference(this.bpu.buffer(2));
		double height = t.getHeight();
		return difference.getArea() * height;

	}

}
