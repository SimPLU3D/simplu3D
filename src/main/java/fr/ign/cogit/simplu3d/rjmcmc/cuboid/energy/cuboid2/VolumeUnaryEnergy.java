package fr.ign.cogit.simplu3d.rjmcmc.cuboid.energy.cuboid2;

import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.simplu3d.enau.geometry.DeformedCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
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
public class VolumeUnaryEnergy<T extends AbstractSimpleBuilding > implements UnaryEnergy<T> {

	@Override
	public double getValue(T t) {

		if (t instanceof Cuboid) {
			Cuboid c = (Cuboid) t;

			double volume = c.width * c.length * c.height;

			return volume;
		}

		if (t instanceof DeformedCuboid) {
			DeformedCuboid c = (DeformedCuboid) t;

			
			
			double volume = Math.abs(Util.volumeUnderSurface(c.getTriangle()));
			volume = volume - c.getTriangle().get(0).area() * c.getZmin()
					- c.getTriangle().get(1).area() * c.getZmin();
			
		//	double volume = c.height() * c.getRectangle2D().getArea();

			return volume;
		}

		System.out.println("Probleme : volume unary energy");
		return 0;

	}

}
