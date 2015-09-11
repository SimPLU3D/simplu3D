package fr.ign.cogit.simplu3d.rjmcmc.cuboid.energy.cuboid2;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.simplu3d.enau.geometry.DeformedCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.geometry.Rectangle2D;
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
public class DifferenceVolumeUnaryEnergy<T> implements UnaryEnergy<T> {
	Geometry bpu;

	public DifferenceVolumeUnaryEnergy(Geometry p) {
		this.bpu = p;
	}

	@Override
	public double getValue(T t) {

		try {
			if (t instanceof Cuboid) {
				Geometry difference = ((Cuboid) t).toGeometry().difference(
						this.bpu);

				return difference.getArea() * ((Cuboid) t).height; // Math.exp(difference.getArea()
																	// *
																	// ((Cuboid)t).height
																	// ) ;

			}

			if (t instanceof DeformedCuboid) {
				Geometry difference = ((DeformedCuboid) t).toGeometry()
						.difference(this.bpu);

				return difference.getArea()
						* Math.max(Math.max(((DeformedCuboid) t).height1,
								((DeformedCuboid) t).height2), Math.max(
								((DeformedCuboid) t).height3,
								((DeformedCuboid) t).height4)); // Math.exp(difference.getArea()
																// *
																// ((Cuboid)t).height
																// ) ;

			}

		} catch (Exception e) {
			System.out.println("G = " + ((Rectangle2D) t).toGeometry());
			System.out.println("BPU = " + bpu);
		}
		return 0;
	}

}
