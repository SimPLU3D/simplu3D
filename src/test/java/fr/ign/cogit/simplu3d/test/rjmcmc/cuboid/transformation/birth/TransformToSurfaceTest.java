package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.birth;

import java.util.Vector;

import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.util.conversion.WktGeOxygene;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.TransformToSurface;
import fr.ign.random.Random;

public class TransformToSurfaceTest {
  TransformToSurface t;

  @Before
  public void setUp() throws Exception {
    IPolygon polygon = (IPolygon) WktGeOxygene
        .makeGeOxygene("POLYGON (( 5 0, 5 -10, 15 -10, 15 -5, 10 -5, 10 0, 5 0 ))");
    double[] d = new double[] { 0, 0, 1, 1, 3, 0 };
    double[] v = new double[] { 10, 10, 5, 5, 3, Math.PI };
    t = new TransformToSurface(d, v, polygon);
  }

  @Test
  public void testApply() {

    int dimension = t.dimension(0, 0);

    /*
     * double[] in = new double[] { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 };
     * double[] out = new double[dimension]; t.apply(in, out);
     */

    Vector<Double> lvalIn = new Vector<Double>();

    lvalIn.add(0.5);
    lvalIn.add(0.5);
    lvalIn.add(0.5);
    lvalIn.add(0.5);
    lvalIn.add(0.5);

    Vector<Double> lvarIn = new Vector<Double>();
    lvarIn.add(0.5);
    lvarIn.add(0.5);

    Vector<Double> lvalOut = new Vector<>();
    Vector<Double> lvarOut = new Vector<>();

    t.apply(true, lvalIn, lvarIn, lvalOut, lvarOut);

    String outString = "";

    for (int i = 0; i < dimension; i++) {
      outString += lvalOut.get(i) + " ";
    }

    outString += lvarOut.get(0) + " ";
    outString += lvarOut.get(1) + " ";

    System.out.println("out = " + outString);

    Cuboid cuboidOut = new Cuboid(lvalOut.get(0), lvalOut.get(1),
        lvalOut.get(2), lvalOut.get(3), lvalOut.get(4), lvalOut.get(5));

    System.out.println("Out = " + cuboidOut.toGeometry());
    /*
     * in = new double[] { 0, 0, 0.5, 0.5, 0.5, 0.5, 0.5 }; t.apply(in, out);
     */
    lvalIn = new Vector<Double>();

    lvalIn.add(0.0);
    lvalIn.add(0.0);
    lvalIn.add(0.5);
    lvalIn.add(0.5);
    lvalIn.add(0.5);

    lvarIn = new Vector<Double>();
    lvarIn.add(0.5);
    lvarIn.add(0.5);

    lvalOut.clear();
    lvarOut.clear();

    outString = "";

    for (int i = 0; i < dimension; i++) {
      outString += lvalOut.get(i) + " ";
    }

    outString += lvarOut.get(0) + " ";
    outString += lvarOut.get(1) + " ";

    System.out.println("out = " + outString);
    cuboidOut = new Cuboid(lvalOut.get(0), lvalOut.get(1), lvalOut.get(2),
        lvalOut.get(3), lvalOut.get(4), lvalOut.get(5));
    System.out.println("Out = " + cuboidOut.toGeometry());

    lvalIn = new Vector<Double>();

    // in = new double[] { 1, 1, 0.5, 0.5, 0.5, 0.5, 0.5 };

    lvalIn.add(1.0);
    lvalIn.add(1.0);
    lvalIn.add(0.5);
    lvalIn.add(0.5);
    lvalIn.add(0.5);

    lvarIn = new Vector<Double>();
    lvarIn.add(0.5);
    lvarIn.add(0.5);

    lvalOut.clear();
    lvarOut.clear();

    t.apply(true, lvalIn, lvarIn, lvalOut, lvarOut);

    // t.apply(in, out);
    outString = "";
    outString = "";

    for (int i = 0; i < dimension; i++) {
      outString += lvalOut.get(i) + " ";
    }

    outString += lvarOut.get(0) + " ";
    outString += lvarOut.get(1) + " ";

    System.out.println("out = " + outString);
    cuboidOut = new Cuboid(lvalOut.get(0), lvalOut.get(1), lvalOut.get(2),
        lvalOut.get(3), lvalOut.get(4), lvalOut.get(5));
    System.out.println("Out = " + cuboidOut.toGeometry());

    RandomGenerator generator = Random.random();
    GeometryFactory f = new GeometryFactory();
    for (int i = 0; i < 10000; i++) {

      // in = new double[] { generator.nextDouble(), generator.nextDouble(),
      // 0.5,
      // 0.5, 0.5, 0.5, 0.5 };
      lvalIn = new Vector<Double>();

      lvalIn.add(generator.nextDouble());
      lvalIn.add(generator.nextDouble());
      lvalIn.add(0.5);
      lvalIn.add(0.5);
      lvalIn.add(0.5);

      lvarIn = new Vector<Double>();
      lvarIn.add(0.5);
      lvarIn.add(0.5);

      lvalOut.clear();
      lvarOut.clear();

      t.apply(true, lvalIn, lvarIn, lvalOut, lvarOut);

      System.out.println(f.createPoint(new Coordinate(lvalOut.get(0), lvalOut
          .get(1))));

    }
  }

  @Test
  public void testInverse() {
    // fail("Not yet implemented"); // TODO
  }

  @Test
  public void testDimension() {
    // fail("Not yet implemented"); // TODO
  }

}
