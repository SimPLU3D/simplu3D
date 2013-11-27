package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.birth;

import org.junit.Before;
import org.junit.Test;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.util.conversion.WktGeOxygene;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.random.Random;

public class UniformBirthInGeomTest {
  UniformBirthInGeom<Cuboid2> birth;

  @Before
  public void setUp() throws Exception {
    ObjectBuilder<Cuboid2> builder = new ObjectBuilder<Cuboid2>() {
      @Override
      public Cuboid2 build(double[] coordinates) {
        return new Cuboid2(coordinates[0], coordinates[1], coordinates[2], coordinates[3],
            coordinates[4], coordinates[5]);
      }

      @Override
      public int size() {
        return 6;
      }

      @Override
      public void setCoordinates(Cuboid2 t, double[] coordinates) {
        coordinates[0] = t.centerx;
        coordinates[1] = t.centery;
        coordinates[2] = t.length;
        coordinates[3] = t.width;
        coordinates[4] = t.height;
        coordinates[5] = t.orientation;
      }
    };

    IPolygon polygon = (IPolygon) WktGeOxygene
        .makeGeOxygene("POLYGON (( 5 0, 5 -10, 15 -10, 15 -5, 10 -5, 10 0, 5 0 ))");
    IEnvelope env = polygon.envelope();
    double mindim = 4;
    double maxdim = 6;
    double minheight = 3;
    double maxheight = 5;
    // Sampler de naissance
    birth = new UniformBirthInGeom<Cuboid2>(new Cuboid2(env.minX(), env.minY(), mindim, mindim,
        minheight, 0), new Cuboid2(env.maxX(), env.maxY(), maxdim, maxdim, maxheight, Math.PI),
        builder, polygon);
  }

  @Test
  public void testSample() {
    double p = birth.sample(Random.random());
    System.out.println("p=" + p);
    System.out.println("object=" + birth.getObject().toGeometry());
    for (int i = 0; i < 10000; i++) {
      birth.sample(Random.random());
      System.out.println(birth.getObject().toGeometry());
    }
  }

  @Test
  public void testPdf() {
    double p = birth.sample(Random.random());
    System.out.println("p=" + p);
    System.out.println("pdf=" + birth.pdf(birth.getObject()));
  }

}
