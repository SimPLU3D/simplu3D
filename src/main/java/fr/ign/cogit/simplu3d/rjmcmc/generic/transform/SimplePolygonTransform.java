package fr.ign.cogit.simplu3d.rjmcmc.generic.transform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.geometry.transform.PolygonTransform;

public class SimplePolygonTransform extends PolygonTransform {
	MultiPolygon polygon;

	double xmin, ymin;
	double width, height;

	double totalArea;

	private boolean isValid = true;

	public SimplePolygonTransform(Geometry p) {
		super();
		if (Polygon.class.isInstance(p)) {
			this.polygon = p.getFactory().createMultiPolygon(new Polygon[] { (Polygon) p });
		} else {
			if (MultiPolygon.class.isInstance(p)) {
				this.polygon = (MultiPolygon) p;
			} else {
				throw new IllegalArgumentException(
						"Argument should be of type Polygon or MultiPolygon but was " + p.getClass());
			}
		}

		totalArea = polygon.getArea();

		if (polygon.isEmpty() || polygon.getArea() == 0) {
			isValid = false;
			return;
		}

		Geometry enveloppe = polygon.getEnvelope();
		if (enveloppe.isEmpty() || enveloppe.getDimension() != 2) {
			isValid = false;
			return;
		}

		
		
		Coordinate minPoint = enveloppe.getCoordinates()[0];
		Coordinate maxPoint = enveloppe.getCoordinates()[2];

		xmin = minPoint.x;
		ymin = minPoint.y;

		width = maxPoint.x - xmin;
		height = maxPoint.y - ymin;

		isValid = true;

	}

	public boolean isValid() {
		return isValid;
	}

	@Override
	public double apply(boolean direct, double[] val0, double[] val1) {
		//
		if (direct) {
			double s = val0[0];
			double t = val0[1];

			boolean contains = false;

			double x = 0;
			double y = 0;

			for (int i = 0; i < 100000; i++) {

				x = xmin + s * width;
				y = ymin + t * height;

				Geometry point = polygon.getFactory().createPoint(new Coordinate(x, y));
				contains = polygon.contains(point);
				if (contains) {
					break;
				}

				s = Math.random();
				t = Math.random();

			}

			val0[0] = s;
			val0[1] = t;

			val1[0] = x;
			val1[1] = y;

			if (!contains) {
				return 0;
			}

			return 1. / totalArea;
		}

		double s = val0[0];
		double t = val0[1];

		Point point = polygon.getFactory().createPoint(new Coordinate(s, t));

		boolean contains = polygon.contains(point);

		if (!contains) {
			return 0;
		}

		val1[0] = (s - xmin) / width;
		val1[1] = (t - ymin) / height;
		return totalArea;
	}

	// @Override
	public double getAbsJacobian(boolean direct) {
		return (direct) ? 1 / totalArea : totalArea;
	}

	@Override
	public int dimension() {
		return 2;
	}

}
