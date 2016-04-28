package fr.ign.cogit.simplu3d.rjmcmc.trapezoid.builder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.operation.distance.DistanceOp;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry.ParallelTrapezoid;
import fr.ign.mpp.kernel.ObjectBuilder;

public class ParallelRightTrapezoidBuilder implements ObjectBuilder<ParallelTrapezoid> {

	GeometryFactory factory;
	MultiLineString limits;
	double factMult = 10;

	public ParallelRightTrapezoidBuilder(IGeometry[] limits) {
		super();
		factory = new GeometryFactory();
		LineString[] lineStrings = new LineString[limits.length];
		for (int i = 0; i < limits.length; i++) {
			try {
				lineStrings[i] = (LineString) AdapterFactory.toGeometry(factory, limits[i]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.limits = factory.createMultiLineString(lineStrings);
		Geometry geom = this.limits.getEnvelope();
		Coordinate coMin = geom.getCoordinates()[0];
		Coordinate coMax = geom.getCoordinates()[3];

		factMult = DistanceOp.distance(factory.createPoint(coMin), factory.createPoint(coMax));

	}

	@Override
	public ParallelTrapezoid build(double[] val1) {
		double a = Math.cos(val1[5]) * val1[3];
		double b = Math.sin(val1[5]) * val1[3];
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
		 * TestParallelRightTrapezoidSampler.orientationLine.add(JtsGeOxygene.
		 * makeGeOxygeneGeom(g1));
		 * TestParallelRightTrapezoidSampler.orientationLine.add(JtsGeOxygene.
		 * makeGeOxygeneGeom(g2)); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
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

		return new ParallelTrapezoid(val1[0], val1[1], val1[2], length3, length2, val1[3], val1[4], val1[5]);
	}

	@Override
	public void setCoordinates(ParallelTrapezoid t, double[] val1) {
		val1[0] = t.centerx;
		val1[1] = t.centery;
		val1[2] = t.length1;
		// val1[3] = t.length2;
		// val1[3] = t.length3;
		val1[3] = t.width;
		val1[4] = t.height;
		val1[5] = t.orientation;

	}

	@Override
	public int size() {
		return 6;
	}

}
