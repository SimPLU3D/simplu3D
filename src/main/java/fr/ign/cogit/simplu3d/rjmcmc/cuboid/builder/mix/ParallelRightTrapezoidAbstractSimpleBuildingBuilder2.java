package fr.ign.cogit.simplu3d.rjmcmc.cuboid.builder.mix;

import java.util.List;

import fr.ign.common.util.Operateurs;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.distance.DistanceOp;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry.ParallelTrapezoid2;
import fr.ign.mpp.kernel.ObjectBuilder;

public class ParallelRightTrapezoidAbstractSimpleBuildingBuilder2 implements ObjectBuilder<AbstractSimpleBuilding> {

	GeometryFactory factory;
	MultiLineString limits;
	double factMult = 200;

	public ParallelRightTrapezoidAbstractSimpleBuildingBuilder2(Geometry[] limits, Geometry polygon) {
		super();
		factory = new GeometryFactory();
		LineString[] lineStrings = new LineString[limits.length];
		for (int i = 0; i < limits.length; i++) {
			try {
				lineStrings[i] = (LineString) limits[i];
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		this.limits = factory.createMultiLineString(lineStrings);
		Envelope geom = polygon.getEnvelopeInternal();
		Coordinate lowerLeft = new Coordinate(geom.getMinX(), geom.getMinY());
		Coordinate upperRight = new Coordinate(geom.getMaxX(), geom.getMaxY());
		factMult = lowerLeft.distance(upperRight);
	}

	@Override
	public AbstractSimpleBuilding build(double[] val1) {

		Coordinate dpOrientation = Operateurs.pointEnAbscisseCurviligne(limits, val1[5] * limits.getLength());

		Coordinate centre = new Coordinate(val1[0], val1[1]);
		double orientation = Operateurs.getAngle(dpOrientation, centre);

		double a = Math.cos(orientation) * val1[3] / 2;
		double b = Math.sin(orientation) * val1[3] / 2;
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
			return new ParallelTrapezoid2(val1[0], val1[1], 0, length3, length2, 0, 0, 0);
		}


		ParallelTrapezoid2 t2 = new ParallelTrapezoid2(val1[0], val1[1], val1[2], length3, length2, val1[3], val1[4],
				val1[5]);
		t2.setOrientation(orientation);
		return t2;
	}

	@Override
	public void setCoordinates(AbstractSimpleBuilding t, double[] val1) {
			t.setCoordinates(val1);
	}

	@Override
	public int size() {
		return 6;
	}

}
