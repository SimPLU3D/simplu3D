package fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.rjmcmc.generic.transform.SimplePolygonTransform;
import fr.ign.geometry.transform.PolygonTransform;
import fr.ign.rjmcmc.kernel.Transform;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see http://www.cecill.info/
 * 
 * copyright IGN
 * 
 * @version 1.0
 **/
public class ParallelCuboidTransform implements Transform {
  /**
   * Logger.
   */
  static Logger LOGGER = Logger.getLogger(ParallelCuboidTransform.class.getName());

  private double absJacobian[];
  private PolygonTransform polygonTransform;
//  private MultiLineString limits;
  private GeometryFactory factory = new GeometryFactory();

  private double deltaLength;
  private double deltaHeight;
  private double rangeLength;
  private double rangeHeight;
  
  private boolean isValid = false;

  /**
   * 
   * @return Indicate if the transform is valid (i.e: that the triangulation in the PolygonTransform is ok)
   */
  public boolean isValid(){  
	  return isValid;
  }
  
  public ParallelCuboidTransform(double[] d, double[] v, IGeometry polygon)
      throws Exception {
    this.rangeLength = d[2];
    this.rangeHeight = d[3];
    this.deltaLength = v[2];
    this.deltaHeight = v[3];
    double determinant = rangeLength * rangeHeight;
//    LineString[] lineStrings = new LineString[limits.length];
//    for (int i = 0; i < limits.length; i++) {
//      lineStrings[i] = (LineString) AdapterFactory.toGeometry(factory, limits[i]);
//    }
//    this.limits = factory.createMultiLineString(lineStrings);
    Geometry pp = AdapterFactory.toGeometry(factory, polygon);
    Iterator<Double> testedSnapping = Arrays.asList(0.1,0.001,0.0).iterator();
    
    while(testedSnapping.hasNext() && ! isValid) {
        this.polygonTransform = new PolygonTransform(pp, testedSnapping.next());
        isValid = this.polygonTransform.isValid();
    }
    
	if (!isValid) {
		this.polygonTransform = new SimplePolygonTransform(pp);
		this.isValid = polygonTransform.isValid();

	}
	
    if(isValid) {
        this.absJacobian = new double[2];
        this.absJacobian[0] = Math.abs(determinant) * this.polygonTransform.getAbsJacobian(true);
        this.absJacobian[1] = Math.abs(1 / determinant) * this.polygonTransform.getAbsJacobian(false);
    }
  }

  @Override
  public double apply(boolean direct, double[] val0, double[] val1) {
    double pt = this.polygonTransform.apply(direct, val0, val1);
    if (direct) {
//      Coordinate p = new Coordinate(val1.get(0), val1.get(1));
//      DistanceOp op = new DistanceOp(this.limits, factory.createPoint(p));
//      Coordinate projected = op.nearestPoints()[0];
//      double distance = op.distance();
//      double orientation = Angle.angle(p, projected);
      val1[2] = val0[2] * rangeLength + deltaLength;
//      val1.set(3, distance * 2);
      val1[3] = val0[3] * rangeHeight + deltaHeight;
//      val1.set(5, orientation + Math.PI / 2);
      return pt * this.absJacobian[0];
    } else {
      val1[2] = (val0[2] - deltaLength) / rangeLength;
      val1[3] = (val0[3] - deltaHeight) / rangeHeight;
//      var1.set(4, 0.0);
//      var1.set(5, 0.0);
      return pt * this.absJacobian[1];
    }
  }

//  @Override
  public double getAbsJacobian(boolean direct) {
    return this.absJacobian[direct ? 0 : 1];
  }

  @Override
  public int dimension() {
    return 4;
  }
}
