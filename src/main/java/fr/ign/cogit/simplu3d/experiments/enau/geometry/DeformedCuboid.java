package fr.ign.cogit.simplu3d.experiments.enau.geometry;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Triangle;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.geometry.IntersectionArea;
import fr.ign.geometry.Primitive;
import fr.ign.geometry.Rectangle2D;

public class DeformedCuboid extends AbstractSimpleBuilding implements ISimPLU3DPrimitive {

	public double height1, height2, height3, height4;

	public DeformedCuboid(double centerx, double centery, double length,
			double width, double height1, double height2,
			double height3, double height4, double orientation) {
		super();
		this.centerx = centerx;
		this.centery = centery;
		this.length = length;
		this.width = width;
		this.orientation = orientation;
		this.height1 = height1;
		this.height2 = height2;
		this.height3 = height3;
		this.height4 = height4;
	}

	Polygon geomJTS = null;

	@Override
	public double intersectionArea(Primitive p) {
		return this.toGeometry().intersection(p.toGeometry()).getArea();
	}

	IPolygon polyGeox = null;

	@Override
	public IOrientableSurface getFootprint() {
		if (polyGeox == null) {
			try {
				polyGeox = (IPolygon) JtsGeOxygene.makeGeOxygeneGeom(this
						.toGeometry());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return polyGeox;
	}

	private static GeometryFactory geomFact = new GeometryFactory();

	@Override
	public Polygon toGeometry() {
		if (geomJTS == null) {

			Coordinate[] pts = new Coordinate[5];
			double cosOrient = Math.cos(orientation);
			double sinOrient = Math.sin(orientation);
			double a = cosOrient * length / 2;
			double b = sinOrient * width / 2;
			double c = sinOrient * length / 2;
			double d = cosOrient * width / 2;
			pts[0] = new Coordinate(this.centerx - a + b, this.centery - c - d,
					height1);
			pts[1] = new Coordinate(this.centerx + a + b, this.centery + c - d,
					height2);
			pts[2] = new Coordinate(this.centerx + a - b, this.centery + c + d,
					height3);
			pts[3] = new Coordinate(this.centerx - a - b, this.centery - c + d,
					height4);
			pts[4] = new Coordinate(pts[0]);

			LinearRing ring = geomFact.createLinearRing(pts);
			Polygon poly = geomFact.createPolygon(ring, null);
			this.geomJTS = poly;
		}
		return this.geomJTS;
	}

	@Override
	public Object[] getArray() {
		return new Object[] { this.centerx, this.centery, this.length,
				this.width, this.height1, this.height2, this.height3,
				this.height4, this.orientation };
	}

	@Override
	public int size() {
		return 9;
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		double[] array = { this.centerx, this.centery, this.length, this.width,
				this.height1, this.height2, this.height3, this.height4,
				this.orientation };
		for (double e : array)
			hashCode = 31 * hashCode + hashCode(e);
		return hashCode;
	}

	public int hashCode(double value) {
		long bits = Double.doubleToLongBits(value);
		return (int) (bits ^ (bits >>> 32));
	}

	public double height(int a, int b) {
		return Math.max(Math.max(height1, height2), Math.max(height3, height4));
	}

	public double getArea() {
		return this.length * this.width;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Cuboid)) {
			return false;
		}
		DeformedCuboid r = (DeformedCuboid) o;
		return  (this.centerx == r.centerx)
				&& (this.centery== r.centery)
				&& (this.width== r.width)
				&& (this.length== r.length)
				&& (this.orientation== r.orientation)
				&& (this.height1== r.height1)
				&& (this.height2== r.height2)
				&& (this.height3== r.height3)
				&& (this.height4== r.height4);
	}

	public String toString() {
		return "Cuboid : " + " Centre " + this.centerx + "; " + this.centery
				+ " hauteur1 " + this.height1

				+ " hauteur2 " + this.height2 + " hauteur3 " + this.height3
				+ " hauteur4 " + this.height4

				+ " largeur " + this.width + " longueur " + this.width
				+ " orientation " + this.orientation;

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
			Environnement env = Environnement.getInstance();
			if (env != null && env.getTerrain() != null) {
				zMin = env.getTerrain().castCoordinate(this.centerx,
						this.centery).z;
			} else {
				logger.warn("No terrain Cuboid ZMin set to 0");
				zMin = 0;
			}

		}
		// TODO Auto-generated method stub
		return zMin;
	}

	public static boolean do_intersect(DeformedCuboid a, DeformedCuboid b) {
		return IntersectionArea.do_intersect(a.getRectangle2D(),
				b.getRectangle2D());
	}

	public static double intersection_area(DeformedCuboid a, DeformedCuboid b) {
		return IntersectionArea.intersection_area(a.getRectangle2D(),
				b.getRectangle2D());
	}

	private Rectangle2D rectangle = null;

	public Rectangle2D getRectangle2D() {

		if (rectangle == null) {
			rectangle = new Rectangle2D(this.centerx, this.centery,
					Math.cos(orientation) * length / 2, Math.sin(orientation)
							* length / 2, width / length);
		}
		return rectangle;
	}

	public double height() {
		return this.height(0, 0);
	}

	public boolean prospect(IGeometry geom, double slope, double hIni) {
		try {
			return prospectJTS(AdapterFactory.toGeometry(geomFact, geom),
					slope, hIni);
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

		Polygon pol = this.toGeometry();

		Coordinate[] coord = pol.getCoordinates();

		double distance1 = geom.distance(geomFact.createPoint(coord[0]));

		if (!(distance1 * slope + hIni > this.height1)) {
			return false;
		}

		double distance2 = geom.distance(geomFact.createPoint(coord[1]));

		if (!(distance2 * slope + hIni > this.height2)) {
			return false;
		}

		double distance3 = geom.distance(geomFact.createPoint(coord[2]));

		if (!(distance3 * slope + hIni > this.height3)) {
			return false;
		}

		double distance4 = geom.distance(geomFact.createPoint(coord[3]));

		return distance4 * slope + hIni > this.height4;
	}

	@Override
	public double[] toArray() {
		return new double[] { this.centerx, this.centery, this.length,
				this.width, this.height1, this.height2, this.height3,
				this.height4, this.orientation };
	}

	@Override
	public void set(List<Double> list) {
		this.centerx = list.get(0);
		this.centery = list.get(1);
		this.length = list.get(2);
		this.width = list.get(3);
		this.height1 = list.get(4);
		this.height2 = list.get(5);
		this.height3 = list.get(6);
		this.height4 = list.get(7);
		this.orientation = list.get(8);
		this.setGenerated(true);
	}
	
	
	
	@Override
	public void setCoordinates(double[] val1) {
		this.centerx = val1[0];
		this.centery = val1[1];
		this.length = val1[2];
		this.width = val1[3];
		this.height1 = val1[4];
		this.height2 = val1[5];
		this.height3 = val1[6];
		this.height4 = val1[7];
		this.orientation = val1[8];
		this.setGenerated(true);
		
	}

	public IGeometry generated3DGeom() {
		return generate(this, this.getZmin());
	}

	public static IMultiSurface<IOrientableSurface> generate(DeformedCuboid c,
			double zMin) {

		IDirectPositionList dpl = c.getFootprint().coord();

		IDirectPosition dp1 = dpl.get(0);
		dp1.setZ(zMin + c.height1);
		IDirectPosition dp2 = dpl.get(1);
		dp2.setZ(zMin + c.height2);
		IDirectPosition dp3 = dpl.get(2);
		dp3.setZ(zMin + c.height3);
		IDirectPosition dp4 = dpl.get(3);
		dp4.setZ(zMin + c.height4);

		return createCube(dp1, dp2, dp3, dp4, zMin);
	}

	private static IMultiSurface<IOrientableSurface> createCube(
			IDirectPosition p1, IDirectPosition p2, IDirectPosition p3,
			IDirectPosition p4, double zmin) {

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

	private List<ITriangle> triangles = null;

	public List<ITriangle> getTriangle() {

		if (triangles == null) {

			IDirectPositionList dpl = this.getFootprint().coord();

			IDirectPosition dp1 = dpl.get(0);
			dp1.setZ(getZmin() + this.height1);
			IDirectPosition dp2 = dpl.get(1);
			dp2.setZ(getZmin() + this.height2);
			IDirectPosition dp3 = dpl.get(2);
			dp3.setZ(getZmin() + this.height3);
			IDirectPosition dp4 = dpl.get(3);
			dp4.setZ(getZmin() + this.height4);

			ITriangle t1 = new GM_Triangle(dp1, dp2, dp3);
			ITriangle t2 = new GM_Triangle(dp1, dp3, dp4);

			triangles = new ArrayList<>();
			triangles.add(t1);
			triangles.add(t2);

		}

		return triangles;

	}
	
	
	private double height= Double.NaN;

	@Override
	public double getHeight() {
		
		if(Double.isNaN(height)){
			height =  Math.max(Math.max(((DeformedCuboid) this).height1,
					((DeformedCuboid) this).height2), Math.max(
							((DeformedCuboid)this).height3,
							((DeformedCuboid) this).height4));
		}
	return height;
	}
	
	private double volume = Double.NaN;
	
	public double getVolume(){
		
		if(Double.isNaN(volume)){

			
			
			double volume = Math.abs(Util.volumeUnderSurface(this.getTriangle()));
			volume = volume -  this.getTriangle().get(0).area() * this.getZmin()
					-  this.getTriangle().get(1).area() *  this.getZmin();
			
		
		}

		return volume;
	}


}
