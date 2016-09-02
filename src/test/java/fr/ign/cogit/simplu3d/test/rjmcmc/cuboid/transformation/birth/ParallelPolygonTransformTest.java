package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.birth;

import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.util.conversion.WktGeOxygene;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth.ParallelPolygonTransform;
import fr.ign.random.Random;

public class ParallelPolygonTransformTest {
  ParallelPolygonTransform t;

  @Before
  public void setUp() throws Exception {
    // IPolygon polygon = (IPolygon) WktGeOxygene
    // .makeGeOxygene("POLYGON (( 5 0, 5 -10, 15 -10, 15 -5, 10 -5, 10 0, 5 0 ))");
    IPolygon polygon = (IPolygon) WktGeOxygene
        .makeGeOxygene("POLYGON (( 24.46264278127928 -13.32945321889124, 16.83388492956696 -2.4038452403082764, 21.54914732032382 4.036513147066944, 30.596317435922344 9.7484976692033, 35.119902493721604 1.4680368854351595, 23.657597982857375 1.1996886192945253, 32.66643263186438 -8.729197227908939, 41.17690621518163 -1.7138068416609316, 39.98850675084454 -16.47296147939581, 20.092399589846092 -22.60663613403888, 13.307022003147196 -13.942820684355546, 24.46264278127928 -13.32945321889124 ))");
    // ILineString limits = (ILineString)
    // WktGeOxygene.makeGeOxygene("LINESTRING ( 5 -10, 15 -10 )");
    //ILineString limit1 = (ILineString) WktGeOxygene
    //   .makeGeOxygene("LINESTRING ( 41.17690621518163 -1.7138068416609316, 39.98850675084454 -16.47296147939581, 20.092399589846092 -22.60663613403888 )");
   // ILineString limit2 = (ILineString) WktGeOxygene
    //     .makeGeOxygene("LINESTRING ( 30.596317435922344 9.7484976692033, 21.54914732032382 4.036513147066944, 16.83388492956696 -2.4038452403082764 )");
    double[] d = new double[] { 0, 0, 1, 3, 3, 0 };
    double[] v = new double[] { 0, 0, 5, 0, 30, 0 };

    t = new ParallelPolygonTransform(d, v, polygon);
  }

  @Test
  public void testApply() {
    // int dimension = 6;
    for (int index = 0; index < 100; index++) {
      double[] lvalIn = new double[4];
      RandomGenerator generator = Random.random();
      for (int i = 0; i < 4; i++) {
        lvalIn[i] = generator.nextDouble();
      }
      double[] lvalOut = new double[4];

//      Vector<Double> lvarOut = new Vector<>();
      t.apply(true, lvalIn, lvalOut);

      // String outString = "";
      // for (int i = 0; i < dimension; i++) {
      // outString += lvalOut.get(i) + " ";
      // }
      // System.out.println("out = " + outString);
//      Cuboid cuboidOut = new ParallelCuboid(lvalOut[0], lvalOut.get(1), lvalOut.get(2), lvalOut.get(3), lvalOut.get(4),
//          lvalOut.get(5));
//
//      System.out.println(cuboidOut.toGeometry());
    }
    // lvalIn = new Vector<Double>();
    // lvarIn = new Vector<Double>();
    // lvarIn.add(generator.nextDouble());
    // lvarIn.add(generator.nextDouble());
    // lvarIn.add(generator.nextDouble());
    // lvarIn.add(generator.nextDouble());
    //
    // lvalOut = new Vector<>();
    // lvalOut.setSize(6);
    //
    // lvarOut = new Vector<>();
    // t.apply(true, lvalIn, lvarIn, lvalOut, lvarOut);
    //
    // outString = "";
    // for (int i = 0; i < dimension; i++) {
    // outString += lvalOut.get(i) + " ";
    // }
    // // System.out.println("out = " + outString);
    // cuboidOut = new Cuboid(lvalOut.get(0), lvalOut.get(1), lvalOut.get(2),
    // lvalOut.get(3), lvalOut.get(4),
    // lvalOut.get(5));
    // System.out.println(cuboidOut.toGeometry());
    // lvalIn.clear();
    // lvarIn.clear();
    // lvarIn.add(generator.nextDouble());
    // lvarIn.add(generator.nextDouble());
    // lvarIn.add(generator.nextDouble());
    // lvarIn.add(generator.nextDouble());
    //
    // lvalOut.clear();
    // lvalOut.setSize(6);
    // lvarOut.clear();
    // t.apply(true, lvalIn, lvarIn, lvalOut, lvarOut);
    // outString = "";
    // for (int i = 0; i < dimension; i++) {
    // outString += lvalOut.get(i) + " ";
    // }
    // // System.out.println("out = " + outString);
    // cuboidOut = new Cuboid(lvalOut.get(0), lvalOut.get(1), lvalOut.get(2),
    // lvalOut.get(3), lvalOut.get(4),
    // lvalOut.get(5));
    // System.out.println(cuboidOut.toGeometry());
    // GeometryFactory f = new GeometryFactory();
    // for (int i = 0; i < 1; i++) {
    // lvalIn = new Vector<Double>();
    // lvarIn = new Vector<Double>();
    // lvarIn.add(generator.nextDouble());
    // lvarIn.add(generator.nextDouble());
    // lvarIn.add(generator.nextDouble());
    // lvarIn.add(generator.nextDouble());
    //
    // lvalOut = new Vector<>();
    // lvalOut.setSize(6);
    //
    // lvarOut = new Vector<>();
    // t.apply(true, lvalIn, lvarIn, lvalOut, lvarOut);
    // // System.out.println(f.createPoint(new Coordinate(lvalOut.get(0),
    // // lvalOut.get(1))));
    // }
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
