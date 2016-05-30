/**
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 *        
 *        
 * @author MBrasebin
 *
 */
package fr.ign.cogit.simplu3d.experiments.simplu3dapi.object;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;

public class ParametricCuboid {

	public double centerx;
	public double centery;
	public double length;
	public double width;
	public double orientation = 0;
	public double height;

	public ParametricCuboid() {

	}
	
	public ParametricCuboid(Cuboid c){
		this(c.centerx, c.centery,c.length, c.width, c.orientation, c.height);
	}

	public ParametricCuboid(double centerx, double centery, double length, double width, double orientation,
			double height) {
		super();
		this.centerx = centerx;
		this.centery = centery;
		this.length = length;
		this.width = width;
		this.orientation = orientation;
		this.height = height;
	}

	public double getCenterx() {
		return centerx;
	}

	public void setCenterx(double centerx) {
		this.centerx = centerx;
	}

	public double getCentery() {
		return centery;
	}

	public void setCentery(double centery) {
		this.centery = centery;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getOrientation() {
		return orientation;
	}

	public void setOrientation(double orientation) {
		this.orientation = orientation;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public IGeometry getGeometry() {

		IDirectPositionList dpl = new DirectPositionList();

		double cosOrient = Math.cos(orientation);
		double sinOrient = Math.sin(orientation);
		double a = cosOrient * length / 2;
		double b = sinOrient * width / 2;
		double c = sinOrient * length / 2;
		double d = cosOrient * width / 2;

		dpl.add(new DirectPosition(this.centerx - a + b, this.centery - c - d, height));
		dpl.add(new DirectPosition(this.centerx + a + b, this.centery + c - d, height));
		dpl.add(new DirectPosition(this.centerx + a - b, this.centery + c + d, height));
		dpl.add(new DirectPosition(this.centerx - a - b, this.centery - c + d, height));
		dpl.add(dpl.get(0));

		ILineString iLs = new GM_LineString(dpl);

		return new GM_Polygon(iLs);
	}

	public IGeometry getGeom3D() {
		return getGeom3D(0);
	}

	public IGeometry getGeom3D(double zmin) {

		IDirectPositionList dpl = this.getGeometry().coord();

		IDirectPosition p1 = dpl.get(0);
		p1.setZ(zmin + this.height);
		IDirectPosition p2 = dpl.get(1);
		p2.setZ(zmin + this.height);
		IDirectPosition p3 = dpl.get(2);
		p3.setZ(zmin + this.height);
		IDirectPosition p4 = dpl.get(3);
		p4.setZ(zmin + this.height);

		List<IDirectPositionList> lDpl = new ArrayList<IDirectPositionList>();

		IDirectPositionList dpl1 = new DirectPositionList();
		dpl1.add(p1);
		dpl1.add(p2);
		dpl1.add(p3);
		dpl1.add(p4);
		dpl1.add(p1);
		lDpl.add(dpl1);

		IDirectPosition p1bas = new DirectPosition(p1.getX(), p1.getY(), zmin);
		IDirectPosition p2bas = new DirectPosition(p2.getX(), p2.getY(), zmin);
		IDirectPosition p3bas = new DirectPosition(p3.getX(), p3.getY(), zmin);
		IDirectPosition p4bas = new DirectPosition(p4.getX(), p4.getY(), zmin);

		IDirectPositionList dpl2 = new DirectPositionList();
		dpl2.add(p2);
		dpl2.add(p1);
		dpl2.add(p1bas);
		dpl2.add(p2bas);
		dpl2.add(p2);
		lDpl.add(dpl2);

		IDirectPositionList dpl3 = new DirectPositionList();
		dpl3.add(p3);
		dpl3.add(p2);
		dpl3.add(p2bas);
		dpl3.add(p3bas);
		dpl3.add(p3);
		lDpl.add(dpl3);

		IDirectPositionList dpl4 = new DirectPositionList();
		dpl4.add(p4);
		dpl4.add(p3);
		dpl4.add(p3bas);
		dpl4.add(p4bas);
		dpl4.add(p4);
		lDpl.add(dpl4);

		IDirectPositionList dpl5 = new DirectPositionList();
		dpl5.add(p1);
		dpl5.add(p4);
		dpl5.add(p4bas);
		dpl5.add(p1bas);
		dpl5.add(p1);
		lDpl.add(dpl5);

		IDirectPositionList dpl6 = new DirectPositionList();
		dpl6.add(p1bas);
		dpl6.add(p4bas);
		dpl6.add(p3bas);
		dpl6.add(p2bas);
		dpl6.add(p1bas);
		lDpl.add(dpl6);

		List<IOrientableSurface> lOS = new ArrayList<>();
		for (IDirectPositionList dplTemp : lDpl) {

			lOS.add(new GM_Polygon(new GM_LineString(dplTemp)));

		}

		return new GM_MultiSurface<>(lOS);

	}

}
