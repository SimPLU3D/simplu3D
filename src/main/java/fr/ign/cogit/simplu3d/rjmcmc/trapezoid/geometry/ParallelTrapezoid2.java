package fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;

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
public class ParallelTrapezoid2 extends RightTrapezoid {

	public double abscisse;

	public ParallelTrapezoid2(double centerx, double centery, double length1, double length2, double length3,
			double width, double height, double abscisse) {
		super(centerx, centery, length1, length2, length3, width, height, Double.NaN);
		this.abscisse = abscisse;

	}

	@Override
	public Object[] getArray() {
		return new Object[] { this.centerx, this.centery, this.length1,
				/* this.length2, this.length3 */ this.width, this.height, this.abscisse };
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
		this.abscisse = list.get(5);
		this.generated = true;

	}

	@Override
	public void setCoordinates(double[] val1) {
		val1[0] = this.centerx;
		val1[1] = this.centery;
		val1[2] = this.length1;
		// this.length2 = val1[3];
		// this.length3 = val1[4];
		val1[3] = this.width;
		val1[4] = this.height;
		val1[5] = this.abscisse;
	}

	@Override
	public int size() {
		return 6;
	}

	@Override
	public double[] toArray() {
		return new double[] { this.centerx, this.centery, this.length1,
				/* this.length2, this.length3 */ this.width, this.height, this.abscisse };
	}

	private static GeometryFactory geomFact = new GeometryFactory();

	public double getOrientation() {
		return orientation;
	}

	public void setOrientation(double orientation) {
		this.orientation = orientation;
	}

	@Override
	public IOrientableSurface getFootprint() {

		return FromGeomToSurface.convertGeom(this.getGeom()).get(0);

	}

	@Override
	public Polygon toGeometry() {

		if (geomJTS == null) {

			Coordinate[] pts = new Coordinate[5];
			double cosOrient = Math.cos(this.getOrientation());
			double sinOrient = Math.sin(this.getOrientation());
			double a = cosOrient * (length);
			double b = sinOrient * (width/2);
			double c = sinOrient * (length);
			double d = cosOrient * (width /2);
			pts[0] = new Coordinate(this.centerx - a + b, this.centery - c - d, height);
			pts[1] = new Coordinate(this.centerx + a + b, this.centery + c - d, height);
			pts[2] = new Coordinate(this.centerx + a - b, this.centery + c + d, height);
			pts[3] = new Coordinate(this.centerx - a - b, this.centery - c + d, height);

			Vecteur v2 = new Vecteur(pts[3].x - pts[2].x, pts[3].y - pts[2].y);
			v2.normalise();
			v2 = v2.multConstante(length + length2);
			IDirectPosition dp3 = v2.translate(new DirectPosition(pts[2].x, pts[2].y));

			Vecteur v = new Vecteur(pts[0].x - pts[1].x, pts[0].y - pts[1].y);
			v.normalise();
			v = v.multConstante(length + length3);
			IDirectPosition dp0 = v.translate(new DirectPosition(pts[1].x, pts[1].y));

			// System.out.println("length + length2" + (length + length2));
			// System.out.println("length + length3" + (length + length3));

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
