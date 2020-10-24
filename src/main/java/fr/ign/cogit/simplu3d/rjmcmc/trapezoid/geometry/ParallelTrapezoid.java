package fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see  http://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class ParallelTrapezoid extends RightTrapezoid {

	public ParallelTrapezoid(double centerx, double centery, double length1, double length2,  double length3, double width,
			double height, double orientation) {
		super(centerx, centery, length1, length2, length3, width, height, orientation);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object[] getArray() {
		return new Object[] { this.centerx, this.centery, this.length1,
				 /*this.length2, this.length3*/  this.width, this.height, this.orientation };
	}

	@Override
	public void set(List<Double> list) {
		this.centerx = list.get(0);
		this.centery = list.get(1);
		this.length1 = list.get(2);
		// this.length2 = list.get(3);
		// this.length3 = list.get(4);
		this.width = list.get(3);
		this.height = list.get(4);
		this.orientation = list.get(5);
		this.setNew(true);

	}

	@Override
	public int size() {
		return 6;
	}

	@Override
	public double[] toArray() {
		return new double[] { this.centerx, this.centery, this.length1,
				/* this.length2, this.length3*/ this.width, this.height, this.orientation };
	}

	private static GeometryFactory geomFact = new GeometryFactory();
	
	@Override
	public Polygon toGeometry() {

		if (geomJTS == null) {


			Coordinate[] pts = new Coordinate[5];
			double cosOrient = Math.cos(orientation);
			double sinOrient = Math.sin(orientation);
			double a = cosOrient * length ;
			double b = sinOrient * width ;
			double c = sinOrient * length;
			double d = cosOrient * width;
			pts[0] = new Coordinate(this.centerx - a + b, this.centery - c - d,
					height);
			pts[1] = new Coordinate(this.centerx + a + b, this.centery + c - d,
					height);
			pts[2] = new Coordinate(this.centerx + a - b, this.centery + c + d,
					height);
			pts[3] = new Coordinate(this.centerx - a - b, this.centery - c + d,
					height);

			
			
			Vecteur v2 = new Vecteur(pts[3].x - pts[2].x,pts[3].y - pts[2].y);
			v2.normalise();
			v2 = v2.multConstante(length + length2);
			IDirectPosition dp3 = v2.translate(new DirectPosition(pts[2].x, pts[2].y));
			
			
			Vecteur v = new Vecteur(pts[0].x - pts[1].x,pts[0].y - pts[1].y);
			v.normalise();
			v = v.multConstante(length + length3);
			IDirectPosition dp0 = v.translate(new DirectPosition(pts[1].x, pts[1].y));
			
			
			System.out.println("length + length2" + (length + length2));
			System.out.println("length + length3" + (length + length3));
			
			pts[0] = new Coordinate(dp0.getX(), dp0.getY());
			pts[3] = new Coordinate(dp3.getX(), dp3.getY());
			pts[4] = new Coordinate(pts[0]);
			
			LinearRing ring = geomFact.createLinearRing(pts);
			Polygon poly = geomFact.createPolygon(ring, null);
			this.geomJTS = poly;
		}
		return this.geomJTS;
	}

}
