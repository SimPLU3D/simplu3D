package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;

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

	public abstract IGeometry generated3DGeom();

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

		return array1.equals(array2);
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
