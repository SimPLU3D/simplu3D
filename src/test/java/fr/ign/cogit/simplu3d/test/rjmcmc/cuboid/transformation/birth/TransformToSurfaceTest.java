package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.birth;

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
    double[] in = new double[] { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 };
    double[] out = new double[t.dimension()];
    t.apply(in, out);
    String outString = "";
    for (int i = 0; i < t.dimension(); i++) {
      outString += out[i] + " ";
    }
    System.out.println("out = " + outString);
    Cuboid cuboidOut = new Cuboid(out[0], out[1], out[2], out[3], out[4],
        out[5]);
    System.out.println("Out = " + cuboidOut.toGeometry());
    in = new double[] { 0, 0, 0.5, 0.5, 0.5, 0.5, 0.5 };
    t.apply(in, out);
    outString = "";
    for (int i = 0; i < t.dimension(); i++) {
      outString += out[i] + " ";
    }
    System.out.println("out = " + outString);
    cuboidOut = new Cuboid(out[0], out[1], out[2], out[3], out[4], out[5]);
    System.out.println("Out = " + cuboidOut.toGeometry());
    in = new double[] { 1, 1, 0.5, 0.5, 0.5, 0.5, 0.5 };
    t.apply(in, out);
    outString = "";
    for (int i = 0; i < t.dimension(); i++) {
      outString += out[i] + " ";
    }
    System.out.println("out = " + outString);
    cuboidOut = new Cuboid(out[0], out[1], out[2], out[3], out[4], out[5]);
    System.out.println("Out = " + cuboidOut.toGeometry());

    RandomGenerator generator = Random.random();
    GeometryFactory f = new GeometryFactory();
    for (int i = 0; i < 10000; i++) {
      in = new double[] { generator.nextDouble(), generator.nextDouble(), 0.5,
          0.5, 0.5, 0.5, 0.5 };
      t.apply(in, out);
      System.out.println(f.createPoint(new Coordinate(out[0], out[1])));
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
