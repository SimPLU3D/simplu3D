package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.util.Assert;

import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;

public class RotateCuboid2Test {
  RotateCuboid2 t;

  @Before
  public void setUp() throws Exception {
    t = new RotateCuboid2(5 * Math.PI / 180);
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
    Cuboid2 cuboidIn = new Cuboid2(in[0], in[1], in[2], in[3], in[4], in[5]);
    Cuboid2 cuboidOut = new Cuboid2(out[0], out[1], out[2], out[3], out[4], out[5]);
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
    Cuboid2 cuboidOutInv = new Cuboid2(outInv[0], outInv[1], outInv[2], outInv[3], outInv[4], outInv[5]);
    System.out.println("Inv = " + cuboidOutInv.toGeometry());
  }

  @Test
  public void testDimension() {
    Assert.equals(7, t.dimension());
  }

}
