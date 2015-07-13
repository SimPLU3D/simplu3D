package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.util.Assert;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.RotateCuboid;

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
public class RotateCuboid2Test {
  RotateCuboid t;

  @Before
  public void setUp() throws Exception {
    t = new RotateCuboid(5 * Math.PI / 180);
  }

  @Test
  public void testApply() {
    int dimension = t.dimension() - 1;
    // double[] in = new double[] { 0, 0, 5, 1, 20, 0, 1 };
    double[] in = new double[] { 0.0, 5.0, 1.0, 20.0, 0.0, 0.0, 1.0 };
    double[] out = new double[7];
    t.apply(true, in, out);
    String outString = "";
    for (int i = 0; i < dimension; i++) {
      outString += out[i] + " ";
    }
    outString += out[dimension];
    System.out.println("out = " + outString);
    Cuboid cuboidIn = new Cuboid(in[0], in[1],
        in[2], in[3], in[4], in[5]);
    Cuboid cuboidOut = new Cuboid(out[0], out[1],
        out[2], out[3], out[4], out[5]);
    System.out.println("In = " + cuboidIn.toGeometry());
    System.out.println("Out = " + cuboidOut.toGeometry());
    // Assert.equals(cuboidIn.toGeometry().toString(), cuboidOut.toGeometry()
    // .toString());
  }

  @Test
  public void testInverse() {

    int dimension = t.dimension() - 1;
    // double[] in = new double[] { 0, 0, 5, 1, 20, 0, 1 };
    double[] lvalIn = new double[] { 0.0, 5.0, 1.0, 20.0, 0.0, 0.0, 1.0 };

    // double[] out = new double[dimension];
    double[] lvalOut = new double[7];
    t.apply(true, lvalIn, lvalOut);

    double[] lvalOutInv = new double[7];

    t.apply(false, lvalOut, lvalOutInv);

    String inString = "";
    for (int i = 0; i < dimension; i++) {
      inString += lvalIn[i] + " ";
    }
    inString += lvalIn[dimension];

    System.out.println("in = " + inString);

    String outString = "";
    for (int i = 0; i < dimension; i++) {
      outString += lvalOutInv[i] + " ";
    }
    outString += lvalOutInv[dimension];

    System.out.println("outInv = " + outString);
    Cuboid cuboidOutInv = new Cuboid(lvalOutInv[0], lvalOutInv[1],
        lvalOutInv[2], lvalOutInv[3], lvalOutInv[4],
        lvalOutInv[5]);
    System.out.println("Inv = " + cuboidOutInv.toGeometry());
    Cuboid cuboidIn = new Cuboid(lvalIn[0], lvalIn[1],
        lvalIn[2], lvalIn[3], lvalIn[4], lvalIn[5]);
    Assert.equals(cuboidOutInv.toGeometry().toString(), cuboidIn
        .toGeometry().toString());
  }

  @Test
  public void testDimension() {
    int dimension = t.dimension();
    Assert.equals(7, dimension);
  }
}
