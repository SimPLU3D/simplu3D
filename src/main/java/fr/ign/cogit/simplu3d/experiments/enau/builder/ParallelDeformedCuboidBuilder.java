package fr.ign.cogit.simplu3d.experiments.enau.builder;

import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.operation.distance.DistanceOp;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.experiments.enau.geometry.DeformedCuboid;
import fr.ign.cogit.simplu3d.experiments.enau.geometry.ParallelDeformedCuboid;
import fr.ign.mpp.kernel.ObjectBuilder;

public class ParallelDeformedCuboidBuilder implements ObjectBuilder<DeformedCuboid> {
	GeometryFactory factory;
	MultiLineString limits;

	public ParallelDeformedCuboidBuilder(IGeometry[] limits) throws Exception {
		factory = new GeometryFactory();
		LineString[] lineStrings = new LineString[limits.length];
		for (int i = 0; i < limits.length; i++) {
			lineStrings[i] = (LineString) AdapterFactory.toGeometry(factory, limits[i]);
		}
		this.limits = factory.createMultiLineString(lineStrings);

	}

	@Override
	public DeformedCuboid build(double[] coordinates) {
		Coordinate p = new Coordinate(coordinates[0], coordinates[1]);
		DistanceOp op = new DistanceOp(this.limits, factory.createPoint(p));
		Coordinate projected = op.nearestPoints()[0];
		double distance = op.distance();
		double orientation = Angle.angle(p, projected);

		ParallelDeformedCuboid result = new ParallelDeformedCuboid(coordinates[0], coordinates[1], coordinates[2],
				distance * 2, coordinates[4], coordinates[5], coordinates[6], coordinates[7],
				orientation + Math.PI / 2);

		return result;
	}

	@Override
	public int size() {
		return 9;
	}

	@Override
	public void setCoordinates(DeformedCuboid t, double[] val1) {
		val1[0] = t.centerx;
		val1[1] = t.centery;
		val1[2] = t.length;
		val1[3] = t.width;
		val1[4] = t.height1;
		val1[5] = t.height2;
		val1[6] = t.height3;
		val1[7] = t.height4;
		val1[8] = t.orientation;

	}
}
