package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation;

import java.util.Vector;

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

    int dimension = t.dimension(0, 0)-1;

    // double[] in = new double[] { 0, 0, 5, 1, 20, 0, 1 };
    Vector<Double> lvalIn = new Vector<Double>();

    lvalIn.add(0.0);
    lvalIn.add(5.0);
    lvalIn.add(1.0);
    lvalIn.add(20.0);
    lvalIn.add(0.0);

    Vector<Double> lvarIn = new Vector<Double>();
    lvarIn.add(1.0);

    // double[] out = new double[dimension];
    Vector<Double> lvalOut = new Vector<>();
    Vector<Double> lvarOut = new Vector<>();

    t.apply(true, lvalIn, lvarIn, lvalOut, lvarOut);
    String outString = "";
    for (int i = 0; i < dimension; i++) {
      outString += lvalOut.get(i) + " ";
    }
    outString += lvarOut.get(0);

    System.out.println("out = " + outString);
    Cuboid cuboidIn = new Cuboid(lvalIn.get(0), lvalIn.get(1), lvalIn.get(2),
        lvalIn.get(3), lvalIn.get(4), lvalIn.get(5));
    Cuboid cuboidOut = new Cuboid(lvalOut.get(0), lvalOut.get(1),
        lvalOut.get(2), lvalOut.get(3), lvalOut.get(4), lvalOut.get(5));
    System.out.println("In = " + cuboidIn.toGeometry());
    System.out.println("Out = " + cuboidOut.toGeometry());
  
    Assert.equals(cuboidIn.toGeometry().toString(), cuboidOut.toGeometry().toString());
  
  }

  @Test
  public void testInverse() {

    int dimension = t.dimension(0, 0)-1;
    // double[] in = new double[] { 0, 0, 5, 1, 20, 0, 1 };
    Vector<Double> lvalIn = new Vector<Double>();

    lvalIn.add(0.0);
    lvalIn.add(5.0);
    lvalIn.add(1.0);
    lvalIn.add(20.0);
    lvalIn.add(0.0);

    Vector<Double> lvarIn = new Vector<Double>();
    lvarIn.add(1.0);

    // double[] out = new double[dimension];
    Vector<Double> lvalOut = new Vector<>();
    Vector<Double> lvarOut = new Vector<>();

    t.apply(true, lvalIn, lvarIn, lvalOut, lvarOut);

    Vector<Double> lvalOutInv = new Vector<>();
    Vector<Double> lvarOutInv = new Vector<>();

    t.apply(true, lvalOut, lvarOut, lvalOutInv, lvarOutInv);

    String outString = "";
    for (int i = 0; i < dimension; i++) {
      outString += lvalOutInv.get(i) + " ";
    }
    outString += lvarOutInv.get(0);

    System.out.println("outInv = " + outString);
    Cuboid cuboidOutInv = new Cuboid(lvalOutInv.get(0), lvalOutInv.get(1),
        lvalOutInv.get(2), lvalOutInv.get(3), lvalOutInv.get(4),
        lvalOutInv.get(5));
    System.out.println("Inv = " + cuboidOutInv.toGeometry());
    Cuboid cuboidIn = new Cuboid(lvalIn.get(0), lvalIn.get(1), lvalIn.get(2),
        lvalIn.get(3), lvalIn.get(4), lvalIn.get(5));
    
    Assert.equals(cuboidOutInv.toGeometry().toString(), cuboidIn.toGeometry().toString());
    
  }

  @Test
  public void testDimension() {

    int dimension = t.dimension(0, 0);

    Assert.equals(7, dimension);
  }

}
