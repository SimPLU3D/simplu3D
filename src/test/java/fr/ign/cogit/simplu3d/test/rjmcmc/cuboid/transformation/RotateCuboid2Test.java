package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.util.Assert;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.RotateCuboid;

public class RotateCuboid2Test {
  RotateCuboid t;

  @Before
  public void setUp() throws Exception {
    t = new RotateCuboid(5 * Math.PI / 180);
  }

  @Test
  public void testApply() {
    double[] in = new double[] { 0, 0, 5, 1, 20, 0, 1 };
    double[] out = new double[t.dimension()];
    t.apply(in, out);
    String outString = "";
    for (int i = 0; i < t.dimension(); i++) {
      outString += out[i] + " ";
    }
    System.out.println("out = " + outString);
    Cuboid cuboidIn = new Cuboid(in[0], in[1], in[2], in[3], in[4], in[5]);
    Cuboid cuboidOut = new Cuboid(out[0], out[1], out[2], out[3], out[4], out[5]);
    System.out.println("In = " + cuboidIn.toGeometry());
    System.out.println("Out = " + cuboidOut.toGeometry());
  }

  @Test
  public void testInverse() {
    double[] in = new double[] { 0, 0, 5, 1, 20, 0, 1 };
    double[] out = new double[t.dimension()];
    t.apply(in, out);
    double[] outInv = new double[t.dimension()];
    t.inverse(out, outInv);
    String outInvString = "";
    for (int i = 0; i < t.dimension(); i++) {
      outInvString += outInv[i] + " ";
    }
    System.out.println("outInv = " + outInvString);
    Cuboid cuboidOutInv = new Cuboid(outInv[0], outInv[1], outInv[2], outInv[3], outInv[4], outInv[5]);
    System.out.println("Inv = " + cuboidOutInv.toGeometry());
  }

  @Test
  public void testDimension() {
    Assert.equals(7, t.dimension());
  }

}
