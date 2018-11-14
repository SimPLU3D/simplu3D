package fr.ign.cogit.simplu3d.rjmcmc.cuboid.builder;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.mpp.kernel.ObjectBuilder;


/**
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see  http://www.cecill.info/
 *        
 *        
 * Basic class for Cuboid Sampler
 * 
 * @author MBrasebin
 *
 */
public class CuboidBuilder implements ObjectBuilder<Cuboid>{
	@Override
	public int size() {
		return 6;
	}

	@Override
	public Cuboid build(double[] val1) {

		return new Cuboid(val1[0], val1[1], val1[2], val1[3], val1[4], val1[5]);
	}

	@Override
	public void setCoordinates(Cuboid t, double[] val1) {
		val1[0] = t.centerx;
		val1[1] = t.centery;
		val1[2] = t.length;
		val1[3] = t.width;
		val1[4] = t.height;
		val1[5] = t.orientation;
	}
}
