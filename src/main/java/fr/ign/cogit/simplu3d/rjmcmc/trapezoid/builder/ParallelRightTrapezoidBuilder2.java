package fr.ign.cogit.simplu3d.rjmcmc.trapezoid.builder;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.operation.distance.DistanceOp;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Angle;
import fr.ign.cogit.geoxygene.contrib.geometrie.Operateurs;
import fr.ign.cogit.geoxygene.convert.FromGeomToLineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry.ParallelTrapezoid2;
import fr.ign.mpp.kernel.ObjectBuilder;

public class ParallelRightTrapezoidBuilder2 implements ObjectBuilder<ParallelTrapezoid2> {

	GeometryFactory factory;
	MultiLineString limits;
	IMultiCurve<ILineString> limitsGeox;
	double factMult = 200;

	public ParallelRightTrapezoidBuilder2(IGeometry[] limits, IGeometry polygon) {
		super();
		factory = new GeometryFactory();
		LineString[] lineStrings = new LineString[limits.length];
		for (int i = 0; i < limits.length; i++) {
			try {
				lineStrings[i] = (LineString) AdapterFactory.toGeometry(factory, limits[i]);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		this.limits = factory.createMultiLineString(lineStrings);
		IEnvelope geom = polygon.getEnvelope();

		limitsGeox = new GM_MultiCurve<>();
		for (int i = 0; i < limits.length; i++) {

			List<IOrientableCurve> ils = FromGeomToLineString.convert(limits[i]);

			for (IOrientableCurve c : ils) {
				limitsGeox.add((ILineString) c);
			}

		}

		factMult = geom.getLowerCorner().distance(geom.getUpperCorner());

	}

	@Override
	public ParallelTrapezoid2 build(double[] val1) {

		IDirectPosition dpOrientation = Operateurs.pointEnAbscisseCurviligne(limitsGeox, val1[5] * limitsGeox.length());

		IDirectPosition centre = new DirectPosition(val1[0], val1[1]);

		Angle angleOr = new Angle(dpOrientation, centre);
		// angleOr.ajoute(new Angle(Math.PI/2));
		// angleOr.ajoute(new Angle(Math.PI/2));
		// angleOr.ajoute(new Angle(Math.PI/2));

		double orientation = angleOr.getValeur();

		double a = Math.cos(orientation) * val1[3] / 2;
		double b = Math.sin(orientation) * val1[3]/ 2;
		/*
		 * 
		 * RightTrapezoid rt = new RightTrapezoid(val1[0], val1[1], val1[2], 0,
		 * 0, val1[3], val1[4], val1[5]);
		 * 
		 * TestParallelRightTrapezoidSampler.trapezoidAfter.add(rt.getGeom());
		 */

		Coordinate p1 = new Coordinate(val1[0] + b, val1[1] - a);
		Coordinate p2 = new Coordinate(val1[0] - b, val1[1] + a);

		Coordinate p3 = new Coordinate(p1.x - a * factMult, p1.y - b * factMult);
		Coordinate p4 = new Coordinate(p2.x - a * factMult, p2.y - b * factMult);

		Coordinate[] coords1 = new Coordinate[2];
		Coordinate[] coords2 = new Coordinate[2];

		coords1[0] = p1;
		coords1[1] = p3;

		coords2[0] = p2;
		coords2[1] = p4;

		Geometry g1 = new GeometryFactory().createLineString(coords1);
		Geometry g2 = new GeometryFactory().createLineString(coords2);

		Geometry geom1 = g1.intersection(limits);
		Geometry geom2 = g2.intersection(limits);

		/*
		 * try {
		 * TestParallelRightTrapezoidSampler2.orientationLine.add(JtsGeOxygene.
		 * makeGeOxygeneGeom(g1));
		 * TestParallelRightTrapezoidSampler2.orientationLine.add(JtsGeOxygene.
		 * makeGeOxygeneGeom(g2)); } catch (Exception e) { e.printStackTrace();
		 * 
		 * }
		 */

		double length2 = Double.POSITIVE_INFINITY;

		if (geom1 != null && !geom1.isEmpty()) {

			Coordinate[] cTemp = geom1.getCoordinates();

			for (int i = 0; i < cTemp.length; i++) {
				length2 = Math.min(length2,
						DistanceOp.distance(factory.createPoint(p1), factory.createPoint(cTemp[i])));
			}

		}
		double length3 = Double.POSITIVE_INFINITY;

		if (geom2 != null && !geom2.isEmpty()) {

			Coordinate[] cTemp = geom2.getCoordinates();

			for (int i = 0; i < cTemp.length; i++) {
				length3 = Math.min(length3,
						DistanceOp.distance(factory.createPoint(p2), factory.createPoint(cTemp[i])));
			}

		}

		if (length2 == Double.POSITIVE_INFINITY || length3 == Double.POSITIVE_INFINITY) {
			length2 = 0;
			length3 = 0;
			return new ParallelTrapezoid2(val1[0], val1[1],0, length3, length2, 0,0,0);
		}

		ParallelTrapezoid2 t2 = new ParallelTrapezoid2(val1[0], val1[1], val1[2], length3, length2, val1[3], val1[4],
				val1[5]);
		t2.setOrientation(orientation);
		return t2;
	}

	@Override
	public void setCoordinates(ParallelTrapezoid2 t, double[] val1) {
		val1[0] = t.centerx;
		val1[1] = t.centery;
		val1[2] = t.length1;
		// val1[3] = t.length2;
		// val1[3] = t.length3;
		val1[3] = t.width;
		val1[4] = t.height;
		val1[5] = t.abscisse;

	}

	@Override
	public int size() {
		return 6;
	}

}
