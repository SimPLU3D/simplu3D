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
package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.model.BuildingPart;

/**
 * 
 * An implementation of BuildPart Dedicated to simulation
 * 
 * @author Brasebin Mickaël
 *
 */
public class FastBuildingPart extends BuildingPart {

	private double z;

	public FastBuildingPart(IPolygon poly, double z) {
		super();
		this.setFootprint(poly);
		this.setNew(true);

		IPolygon polyClone = (IPolygon) poly.clone();

		IDirectPositionList dpl = polyClone.exteriorCoord();

		dpl.inverseOrdre();

		for (IDirectPosition dp : dpl) {
			dp.setZ(z);
		}

		this.setGeom(polyClone);

		this.z = z;

	}

	public boolean prospect(IGeometry geom, double slope, double hIni) {

		double distance = this.getFootprint().distance(this.getGeom());

		double zMin = Double.POSITIVE_INFINITY;

		for (IDirectPosition dp : geom.coord()) {

			zMin = Math.min(dp.getZ(), zMin);

		}

		return distance * slope + hIni > (z - zMin);

	}

}
