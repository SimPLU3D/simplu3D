package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl;

import java.util.ArrayList;
import java.util.List;

import fr.ign.common.conversion.FromGeomToSurface;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.util.AssignZ;
import fr.ign.geometry.IntersectionArea;
import fr.ign.geometry.Primitive;
import fr.ign.geometry.Rectangle2D;

/**
 *
 * This software is released under the licence CeCILL
 * <p>
 * see LICENSE.TXT
 * <p>
 * see <a href="http://www.cecill.info/">...</a>
 * <p>
 * copyright IGN
 *
 * @author Brasebin Mickaël
 *
 **/
public class Cuboid extends AbstractSimpleBuilding {

	public Cuboid(double centerx, double centery, double length, double width, double height, double orientation) {
		super();
		this.setNew(true);
		this.centerx = centerx;
		this.centery = centery;
		this.length = length;
		this.width = width;
		this.height = height;
		this.orientation = orientation;
	}

	@Override
	public double intersectionArea(Primitive p) {
		if (p instanceof Cuboid) {
			return this.getRectangle2D().intersectionArea(((Cuboid) p).getRectangle2D());
		}
		return this.toGeometry().intersection(p.toGeometry()).getArea();
	}

	@Override
	public Polygon getFootprint() {
		if (this.geometry == null) {
			try {
				geometry = this.toGeometry();//FIXME Remove redundant geometry
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (Polygon) geometry;
	}

	private static GeometryFactory geomFact = new GeometryFactory();

	@Override
	public Polygon toGeometry() {
		if (this.geometry == null) {
			Coordinate[] pts = new Coordinate[5];
			double cosOrient = Math.cos(orientation);
			double sinOrient = Math.sin(orientation);
			double a = cosOrient * length / 2;
			double b = sinOrient * width / 2;
			double c = sinOrient * length / 2;
			double d = cosOrient * width / 2;
			pts[0] = new Coordinate(this.centerx - a + b, this.centery - c - d, height);
			pts[1] = new Coordinate(this.centerx + a + b, this.centery + c - d, height);
			pts[2] = new Coordinate(this.centerx + a - b, this.centery + c + d, height);
			pts[3] = new Coordinate(this.centerx - a - b, this.centery - c + d, height);
			pts[4] = new Coordinate(pts[0]);
			LinearRing ring = geomFact.createLinearRing(pts);
			Polygon poly = geomFact.createPolygon(ring, null);
			this.geometry = poly;
		}
		return (Polygon) this.geometry;
	}

	@Override
	public Object[] getArray() {
		return new Object[] { this.centerx, this.centery, this.length, this.width, this.height, this.orientation };
	}

	@Override
	public void setCoordinates(double[] val1) {
		val1[0] = this.centerx;
		val1[1] = this.centery;
		val1[2] = this.length;
		val1[3] = this.width;
		val1[4] = this.height;
		val1[5] = this.orientation;
	}

	@Override
	public int size() {
		return 6;
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		double[] array = { this.centerx, this.centery, this.length, this.width, this.orientation, this.height };
		for (double e : array)
			hashCode = 31 * hashCode + hashCode(e);
		return hashCode;
	}

	public int hashCode(double value) {
        return Double.hashCode(value);
	}

	public double height(int a, int b) {
		return height;
	}

	public double getArea() {
		return this.length * this.width;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Cuboid)) {
			return false;
		}
		Cuboid r = (Cuboid) o;
		return (this.centerx == r.centerx) && (this.centery == r.centery) && (this.width == r.width)
				&& (this.length == r.length) && (this.orientation == r.orientation) && (this.height == r.height);
	}

	public String toString() {
		return "Cuboid : " + " Centre " + this.centerx + "; " + this.centery + " hauteur " + this.height + " largeur "
				+ this.width + " longueur " + this.width + " orientation " + this.orientation;

	}

	private Rectangle2D rectangle = null;

	public Rectangle2D getRectangle2D() {

		if (rectangle == null) {
			rectangle = new Rectangle2D(this.centerx, this.centery, Math.cos(orientation) * length / 2,
					Math.sin(orientation) * length / 2, width / length);
		}
		return rectangle;
	}

	enum Sign {
		NEGATIVE, POSITIVE, ZERO;
	}

	public static Sign sign(double t) {
		return (t < 0) ? Sign.NEGATIVE : ((t > 0) ? Sign.POSITIVE : Sign.ZERO);
	}

	double zMin = Double.NaN;

	public double getZmin() {
		if (Double.isNaN(zMin)) {
			Environnement env = Environnement.createEnvironnement();
			if (env.getTerrain() != null) {
				zMin = env.getTerrain().castCoordinate(this.centerx, this.centery).z;
			} else {
				System.out.println("No terrain Cuboid ZMin set to AssignZ.DEFAULT_Z");
				//FIXME Use logger
				zMin = AssignZ.DEFAULT_Z;
			}

		}
		// TODO Auto-generated method stub
		return zMin;
	}

	public static boolean do_intersect(Cuboid a, Cuboid b) {
		return IntersectionArea.do_intersect(a.getRectangle2D(), b.getRectangle2D());
	}

	public static double intersection_area(Cuboid a, Cuboid b) {
		return IntersectionArea.intersection_area(a.getRectangle2D(), b.getRectangle2D());
	}

	public List<AbstractBuilding> bandEpsilon(Geometry geom, double distMin, double distMax) {
		List<AbstractBuilding> lPolygonOut = new ArrayList<>();
		double d = geom.distance(geometry);
		if (d > distMax) {
			return lPolygonOut;
		}
		if (d < distMin) {
			return lPolygonOut;
		}
		Geometry geomOut = Double.isNaN(distMax) ? geometry : geom.buffer(distMax).intersection(geometry);
		geomOut = geomOut.difference(geom.buffer(distMin));
		List<AbstractBuilding> fBP = getFastBuildingPart(distMin, distMax);
		if (fBP != null) {
			return fBP;
		}
		List<Polygon> lS;
		try {
			lS = FromGeomToSurface.convertGeom(geomOut);
			for (Polygon oS : lS) {
				if (oS == null || oS.isEmpty()) {
					continue;
				}
				lDMin.add(distMin);
				lDMax.add(distMax);
				lPolygonOut.add(new FastBuildingPart(oS, this.height + this.getZmin()));
				lFP.add(fBP);//FIXME check
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lFP.add(lPolygonOut);
		return lPolygonOut;
	}

	private final List<Double> lDMin = new ArrayList<>();
	private final List<Double> lDMax = new ArrayList<>();
	private final List<List<AbstractBuilding>> lFP = new ArrayList<>();

	private List<AbstractBuilding> getFastBuildingPart(double dMin, double dMax) {
		int nbLDmin = lDMin.size();
		for (int i = 0; i < nbLDmin; i++) {
			if (lDMin.get(i) == dMin && lDMax.get(i) == dMax) {
				return lFP.get(i);
			}
		}
		return null;
	}

	public List<AbstractBuilding> bandEpsilon(CadastralParcel cP, double distMin, double distMax) {
		try {
			return bandEpsilon(cP.getConsLine(), distMin, distMax);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public double height() {
		return height;
	}

	public boolean prospect(Geometry geom, double slope, double hIni) {
		try {
			return prospectJTS(geom, slope, hIni);
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

		h = ((Cuboid) this).height;

		// System.out.println("Height : " + height);
		// System.out.println("Distance : " + distance + " slope " + slope +
		// " hIni "+ hIni);

		return distance * slope + hIni > h;
	}

	@Override
	public double[] toArray() {
		return new double[] { this.centerx, this.centery, this.length, this.width, this.height, this.orientation };
	}

	@Override
	public void set(List<Double> list) {
		this.centerx = list.get(0);
		this.centery = list.get(1);
		this.length = list.get(2);
		this.width = list.get(3);
		this.height = list.get(4);
		this.orientation = list.get(5);
		this.setNew(true);
	}

	public Geometry generated3DGeom() {
		// return generate(this, this.getZmin());
		List<Coordinate[]> lDpl = createCubePointsList();
		List<Polygon> lOS = new ArrayList<>();
		for (Coordinate[] dpl : lDpl) {
			lOS.add(geomFact.createPolygon(dpl));
		}
		return geomFact.createMultiPolygon(lOS.toArray(new Polygon[0]));
	}


	private List<Coordinate[]> pointsCube = null;

	public List<Coordinate[]> createCubePointsList() {
		if (pointsCube == null) {
			double zmin = getZmin();
			pointsCube = new ArrayList<>();
			Coordinate[] dpl = this.getFootprint().getCoordinates();
			Coordinate p1 = dpl[0];
			p1.setZ(zMin + height);
			Coordinate p2 = dpl[1];
			p2.setZ(zMin + height);
			Coordinate p3 = dpl[2];
			p3.setZ(zMin + height);
			Coordinate p4 = dpl[3];
			p4.setZ(zMin + height);
			pointsCube.add(new Coordinate[]{p1, p2, p3, p4, p1});
			Coordinate p1bas = new Coordinate(p1.getX(), p1.getY(), zmin);
			Coordinate p2bas = new Coordinate(p2.getX(), p2.getY(), zmin);
			Coordinate p3bas = new Coordinate(p3.getX(), p3.getY(), zmin);
			Coordinate p4bas = new Coordinate(p4.getX(), p4.getY(), zmin);
			pointsCube.add(new Coordinate[]{p2, p1, p1bas, p2bas, p2});
			pointsCube.add(new Coordinate[]{p3, p2, p2bas, p3bas, p3});
			pointsCube.add(new Coordinate[]{p4, p3, p3bas, p4bas, p4});
			pointsCube.add(new Coordinate[]{p1, p4, p4bas, p1bas, p1});
			pointsCube.add(new Coordinate[]{p1bas, p4bas, p3bas, p2bas, p1bas});
		}
		return pointsCube;
	}

    private double volume = Double.NaN;

	@Override
	public double getVolume() {
		if (Double.isNaN(volume)) {
			volume = this.width * this.length * this.height;
		}
		return volume;
	}
	
	public double getExternalSurfaceWithoutFloor() {
		  return 2 * height * width + 2*height*length + width*length ;
	}
}
