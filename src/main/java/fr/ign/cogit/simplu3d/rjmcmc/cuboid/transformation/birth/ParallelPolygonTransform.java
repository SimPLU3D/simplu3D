package fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.operation.distance.DistanceOp;

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
public class ParallelPolygonTransform implements Transform {
  /**
   * Logger.
   */
  static Logger LOGGER = Logger.getLogger(ParallelPolygonTransform.class.getName());

  private double absJacobian[];
  private PolygonTransform polygonTransform;
  private MultiLineString limits;
  private GeometryFactory factory = new GeometryFactory();

  private double deltaLength;
  private double deltaHeight;
  private double rangeLength;
  private double rangeHeight;

  public ParallelPolygonTransform(Vector<Double> d, Vector<Double> v, IGeometry polygon, IGeometry[] limits)
      throws Exception {
    this.rangeLength = d.get(2);
    this.rangeHeight = d.get(4);
    this.deltaLength = v.get(2);
    this.deltaHeight = v.get(4);
    double determinant = rangeLength * rangeHeight;
    LineString[] lineStrings = new LineString[limits.length];
    for (int i = 0; i < limits.length; i++) {
      lineStrings[i] = (LineString) AdapterFactory.toGeometry(factory, limits[i]);
    }
    this.limits = factory.createMultiLineString(lineStrings);
    Geometry pp = AdapterFactory.toGeometry(factory, polygon);
    this.polygonTransform = new PolygonTransform(pp.intersection(this.limits.buffer(d.get(3) / 2 + v.get(3))), 0.1);
    this.absJacobian = new double[2];
    this.absJacobian[0] = Math.abs(determinant) * this.polygonTransform.getAbsJacobian(true);
    this.absJacobian[1] = Math.abs(1 / determinant) * this.polygonTransform.getAbsJacobian(false);
  }

  @Override
  public double apply(boolean direct, Vector<Double> val0, Vector<Double> var0, Vector<Double> val1, Vector<Double> var1) {
    double pt = this.polygonTransform.apply(direct, val0, var0, val1, var1);
    if (direct) {
      Coordinate p = new Coordinate(val1.get(0), val1.get(1));
      DistanceOp op = new DistanceOp(this.limits, factory.createPoint(p));
      Coordinate projected = op.nearestPoints()[0];
      double distance = op.distance();
      double orientation = Angle.angle(p, projected);
      val1.set(2, var0.get(2) * rangeLength + deltaLength);
      val1.set(3, distance * 2);
      val1.set(4, var0.get(3) * rangeHeight + deltaHeight);
      val1.set(5, orientation + Math.PI / 2);
      return pt * this.absJacobian[0];
    } else {
      var1.set(2, (val0.get(2) - deltaLength) / rangeLength);
      var1.set(3, (val0.get(4) - deltaHeight) / rangeHeight);
      var1.set(4, 0.0);
      var1.set(5, 0.0);
      return pt * this.absJacobian[1];
    }
  }

  @Override
  public double getAbsJacobian(boolean direct) {
    return this.absJacobian[direct ? 0 : 1];
  }

  @Override
  public int dimension(int n0, int n1) {
    return 6; //4;
  }
}
