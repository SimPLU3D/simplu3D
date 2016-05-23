package fr.ign.cogit.simplu3d.experiments.enau.geometry;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.geometry.transform.PolygonTransform;
import fr.ign.rjmcmc.kernel.Transform;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * @copyright IGN
 * 
 * @version 1.0
 **/
public class ParallelDeformedCuboidTransform implements Transform {
	/**
	 * Logger.
	 */
	static Logger LOGGER = Logger
			.getLogger(ParallelDeformedCuboidTransform.class.getName());

	private double absJacobian[];
	private PolygonTransform polygonTransform;
	// private MultiLineString limits;
	private GeometryFactory factory = new GeometryFactory();

	private double deltaLength;
	private double deltaHeight1;
	private double deltaHeight2;
	private double deltaHeight3;
	private double deltaHeight4;
	private double rangeLength;
	private double rangeHeight1;
	private double rangeHeight2;
	private double rangeHeight3;
	private double rangeHeight4;

	public ParallelDeformedCuboidTransform(double[] d, double[] v,
			IGeometry polygon, IGeometry[] limits) throws Exception {
		this.rangeLength = d[2];
		this.rangeHeight1 = d[4];
		this.rangeHeight2 = d[5];
		this.rangeHeight3 = d[6];
		this.rangeHeight4 = d[7];
		this.deltaLength = v[2];
		this.deltaHeight1 = v[4];
		this.deltaHeight2 = v[5];
		this.deltaHeight3 = v[6];
		this.deltaHeight4 = v[7];
		double determinant = rangeLength * rangeHeight1 * rangeHeight2
				* rangeHeight3 * rangeHeight4;
		// LineString[] lineStrings = new LineString[limits.length];
		// for (int i = 0; i < limits.length; i++) {
		// lineStrings[i] = (LineString) AdapterFactory.toGeometry(factory,
		// limits[i]);
		// }
		// this.limits = factory.createMultiLineString(lineStrings);
		Geometry pp = AdapterFactory.toGeometry(factory, polygon);
		this.polygonTransform = new PolygonTransform(pp, 0.1);
		this.absJacobian = new double[2];
		this.absJacobian[0] = Math.abs(determinant)
				* this.polygonTransform.getAbsJacobian(true);
		this.absJacobian[1] = Math.abs(1 / determinant)
				* this.polygonTransform.getAbsJacobian(false);
	}

	@Override
	public double apply(boolean direct, double[] val0, double[] val1) {
		double pt = this.polygonTransform.apply(direct, val0, val1);
		if (direct) {
			// Coordinate p = new Coordinate(val1.get(0), val1.get(1));
			// DistanceOp op = new DistanceOp(this.limits,
			// factory.createPoint(p));
			// Coordinate projected = op.nearestPoints()[0];
			// double distance = op.distance();
			// double orientation = Angle.angle(p, projected);
			val1[2] = val0[2] * rangeLength + deltaLength;
			// val1.set(3, distance * 2);
			val1[4] = val0[4] * rangeHeight1 + deltaHeight1;
			val1[5] = val0[5] * rangeHeight2 + deltaHeight2;
			val1[6] = val0[6] * rangeHeight3 + deltaHeight3;
			val1[7] = val0[7] * rangeHeight4 + deltaHeight4;
			// val1.set(5, orientation + Math.PI / 2);
			return pt * this.absJacobian[0];
		} else {
			val1[2] = (val0[2] - deltaLength) / rangeLength;
			val1[4] = (val0[4] - deltaHeight1) / rangeHeight1;
			val1[5] = (val0[5] - deltaHeight2) / rangeHeight2;
			val1[6] = (val0[6] - deltaHeight3) / rangeHeight3;
			val1[7] = (val0[7] - deltaHeight4) / rangeHeight4;
			// var1.set(4, 0.0);
			// var1.set(5, 0.0);
			return pt * this.absJacobian[1];
		}
	}

	// @Override
	public double getAbsJacobian(boolean direct) {
		return this.absJacobian[direct ? 0 : 1];
	}

	@Override
	public int dimension() {
		return 9;
	}
}
