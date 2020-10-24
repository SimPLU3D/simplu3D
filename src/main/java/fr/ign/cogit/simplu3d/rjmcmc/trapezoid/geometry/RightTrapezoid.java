package fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.geometry.Primitive;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * seehttp://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class RightTrapezoid extends AbstractSimpleBuilding implements ISimPLU3DPrimitive {
	public double length1, length2, length3;

	/*
	 * La hauteur length 1 s'applique partout sauf sur la 4ieme cooronndées où
	 * c'est length2 qui s'applique
	 */
	public RightTrapezoid(double centerx, double centery, double length1, double length2, double length3, double width,
			double height, double orientation) {
		super();
		this.setNew(true);
		this.centerx = centerx;
		this.centery = centery;
		this.length1 = length1;
		this.length2 = length2;
		this.length3 = length3;
		this.width = width;
		this.height = height;
		this.orientation = orientation;
		this.length = length1;
	}

	@Override
	public Object[] getArray() {
		return new Object[] { this.centerx, this.centery, this.length1, this.length2, this.length3, this.width,
				this.height, this.orientation };
	}

	@Override
	public void set(List<Double> list) {
		this.centerx = list.get(0);
		this.centery = list.get(1);
		this.length1 = list.get(2);
		this.length2 = list.get(3);
		this.length3 = list.get(4);
		this.width = list.get(5);
		this.height = list.get(6);
		this.orientation = list.get(7);
		this.setNew(true);

	}

	@Override
	public int size() {
		return 8;
	}

	@Override
	public double[] toArray() {
		return new double[] { this.centerx, this.centery, this.length1, this.length2, this.length3, this.width,
				this.height, this.orientation };
	}
	
	
	
	
	
	@Override
	public void setCoordinates(double[] val1) {
		
		this.centerx = val1[0] ; 
		this.centery= val1[1] ;  
		this.length1= val1[2] ;  
		this.length2= val1[3] ;  
		this.length3= val1[4] ;  
		this.width= val1[5] ; 
		this.height= val1[6];  
		this.orientation = val1[7]; 
	}

	
	
	
	

	private static GeometryFactory geomFact = new GeometryFactory();
	Polygon geomJTS = null;

	@Override
	public Polygon toGeometry() {

		if (geomJTS == null) {

			Coordinate[] pts = new Coordinate[5];
			double cosOrient = Math.cos(orientation);
			double sinOrient = Math.sin(orientation);
			double a = cosOrient * length1;
			double b = sinOrient * width;
			double c = sinOrient * length1;
			double d = cosOrient * width;
			pts[0] = new Coordinate(this.centerx - a + b, this.centery - c - d, height);
			pts[1] = new Coordinate(this.centerx + a + b, this.centery + c - d, height);
			pts[2] = new Coordinate(this.centerx + a - b, this.centery + c + d, height);
			pts[3] = new Coordinate(this.centerx - a - b, this.centery - c + d, height);

			Vecteur v2 = new Vecteur(pts[3].x - pts[2].x, pts[3].y - pts[2].y);
			v2.normalise();
			v2 = v2.multConstante(length1 + length2);
			IDirectPosition dp3 = v2.translate(new DirectPosition(pts[2].x, pts[2].y));

			Vecteur v = new Vecteur(pts[0].x - pts[1].x, pts[0].y - pts[1].y);
			v.normalise();
			v = v.multConstante(length1 + length3);
			IDirectPosition dp0 = v.translate(new DirectPosition(pts[1].x, pts[1].y));

			pts[0] = new Coordinate(dp0.getX(), dp0.getY());
			pts[3] = new Coordinate(dp3.getX(), dp3.getY());
			pts[4] = new Coordinate(pts[0]);

			LinearRing ring = geomFact.createLinearRing(pts);
			Polygon poly = geomFact.createPolygon(ring, null);
			this.geomJTS = poly;
		}
		return this.geomJTS;
	}

	@Override
	public double intersectionArea(Primitive arg0) {

		return this.toGeometry().intersection(arg0.toGeometry()).getArea();
	}

	public boolean prospect(IGeometry geom, double slope, double hIni) {
		try {
			return prospectJTS(AdapterFactory.toGeometry(geomFact, geom), slope, hIni);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return false;
		/*
		 * double h = -1; double distance = this.getFootprint().distance(geom);
		 * 
		 * h = ((Cuboid) this).height;
		 * 
		 * return distance * slope + hIni > h;
		 */
	}

	public boolean prospectJTS(Geometry geom, double slope, double hIni) {
		double h = -1;
		double distance = this.toGeometry().distance(geom);

		h = this.height;

		return distance * slope + hIni > h;
	}

	double zMin = Double.NaN;

	public double getZmin() {
		if (Double.isNaN(zMin)) {
			Environnement env = Environnement.getInstance();
			if (env != null && env.getTerrain() != null) {
				zMin = env.getTerrain().castCoordinate(this.centerx, this.centery).z;
			} else {
				logger.warn("No terrain Cuboid ZMin set to 0");
				zMin = 0;
			}

		}
		// TODO Auto-generated method stub
		return zMin;
	}

	@Override
	public IGeometry generated3DGeom() {
		return generate(this, this.getZmin());
	}

	public IGeometry getGeom() {

		if (geom == null) {
			try {
				geom = JtsGeOxygene.makeGeOxygeneGeom(this.toGeometry());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return geom;
	}

	public static IMultiSurface<IOrientableSurface> generate(RightTrapezoid c, double zMin) {

		IDirectPositionList dpl = c.getGeom().coord();

		IDirectPosition dp1 = dpl.get(0);
		dp1.setZ(zMin + c.height);
		IDirectPosition dp2 = dpl.get(1);
		dp2.setZ(zMin + c.height);
		IDirectPosition dp3 = dpl.get(2);
		dp3.setZ(zMin + c.height);
		IDirectPosition dp4 = dpl.get(3);
		dp4.setZ(zMin + c.height);

		return createCube(dp1, dp2, dp3, dp4, zMin);
	}

	private static IMultiSurface<IOrientableSurface> createCube(IDirectPosition p1, IDirectPosition p2,
			IDirectPosition p3, IDirectPosition p4, double zmin) {

		// Polygone p1,p2,p3,p4 représente la face supérieure dans cet ordre

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
		for (IDirectPositionList dpl : lDpl) {

			lOS.add(new GM_Polygon(new GM_LineString(dpl)));

		}

		return new GM_MultiSurface<>(lOS);

	}

	private double area = -1;

	@Override
	public double getArea() {
		if (area == -1) {
			area = this.toGeometry().getArea();
		}
		return area;
	}

	@Override
	public double getHeight() {

		return height;
	}

	private double volume = Double.NaN;

	@Override
	public double getVolume() {

		if (Double.isNaN(volume)) {
			volume = this.getArea() * this.getHeight();
		}

		return volume;
	}


}
