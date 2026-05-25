/**
 * 
 * This software is released under the licence CeCILL
 * <p>
 * see LICENSE.TXT
 * <p>
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
package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl;

import fr.ign.cogit.simplu3d.model.BuildingPart;
import org.apache.commons.lang3.ArrayUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

/**
 * 
 * An implementation of BuildPart Dedicated to simulation
 * 
 * @author Brasebin Mickaël
 *
 */
public class FastBuildingPart extends BuildingPart {

	private final double z;

	public FastBuildingPart(Polygon poly, double z) {
		super();
		this.setFootprint(poly);
		this.setNew(true);
		Polygon polyClone = (Polygon) poly.copy();
		Coordinate[] dpl = polyClone.getExteriorRing().getCoordinates();
		ArrayUtils.reverse(dpl);
		for (Coordinate dp : dpl) {
			dp.setZ(z);
		}
		this.setGeometry(polyClone);
		this.z = z;
	}

	public boolean prospect(Geometry geom, double slope, double hIni) {
		double distance = this.getFootprint().distance(this.getGeometry());
		double zMin = Double.POSITIVE_INFINITY;
		for (Coordinate dp : geom.getCoordinates()) {
			zMin = Math.min(dp.getZ(), zMin);
		}
		return distance * slope + hIni > (z - zMin);
	}
}
