package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;

import java.util.Arrays;

/**
 *
 * This software is released under the licence CeCILL
 * <p>
 * see LICENSE.TXT
 * <p>
 * see <a href="http://www.cecill.info/">...</a>
 * <p>
 *
 *
 * copyright IGN
 *
 * @author Brasebin Mickaël
 *
 * @version 1.0
 **/
public abstract class AbstractSimpleBuilding extends Building implements ISimPLU3DPrimitive {
	public double centerx;
	public double centery;
	public double length;
	public double width;
	public double orientation = 0;
	public double height;

	public AbstractSimpleBuilding() {
		super();
	}

	public abstract Polygon toGeometry();

	public abstract boolean prospectJTS(Geometry geom, double slope, double hIni);

	public abstract Geometry generated3DGeom();

	public abstract void setCoordinates(double[] val1);

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		double[] array1 = this.toArray();
		double[] array2 = this.toArray();
		return Arrays.equals(array1, array2);
	}

	public double getCenterx() {
		return centerx;
	}

	public double getCentery() {
		return centery;
	}

	public double getLength() {
		return length;
	}

	public double getWidth() {
		return width;
	}

	public double getOrientation() {
		return orientation;
	}

	public double getHeight() {
		return height;
	}

}
