package fr.ign.cogit.simplu3d.test.sdpcalc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.util.merge.SDPCalc;

public class SDPCalcTest {

  @Test
  public void testOneBuilding() {
    Cuboid a = new Cuboid(0, 0, 4, 8, 6, 0);
    SDPCalc sd = new SDPCalc();
    List<Cuboid> cubes = new ArrayList<>();
    cubes.add(a);
    Double d = sd.process(cubes);
    assertEquals("sdp must be 4 * 8 * 2  = 64 ", 64.0, d, 0.001);
  }

  @Test
  public void testListIntersectingBuildings() {
    Cuboid a = new Cuboid(0, 0, 4, 8, 6, 0);
    Cuboid b = new Cuboid(2, 0, 4, 8, 12, 0);
    SDPCalc sd = new SDPCalc();
    List<Cuboid> cubes = new ArrayList<>();
    cubes.add(a);
    cubes.add(b);
    // System.out.println(b.generated3DGeom());
    Double d = sd.process(cubes);
    System.out.println(d);
    assertEquals("sdp must be 32 + 128 = 160", 160.0, d, 0.001);
  }

  @Test
  public void testTwoGroupsofBuildings() {
    // group 1 sdp = 160
    Cuboid a1 = new Cuboid(0, 0, 4, 8, 6, 0);
    Cuboid b1 = new Cuboid(2, 0, 4, 8, 12, 0);

    // group 2 sdp = 249
    Cuboid a2 = new Cuboid(20, 0, 10, 4, 15, 0);
    Cuboid b2 = new Cuboid(18, 0, 6, 6, 10, 0);
    Cuboid c2 = new Cuboid(22, -2, 4, 2, 12, 0);

    SDPCalc sd = new SDPCalc();
    List<Cuboid> cubes = new ArrayList<>();
    cubes.add(a1);
    cubes.add(b1);
    cubes.add(a2);
    cubes.add(b2);
    cubes.add(c2);

    Double d = sd.process(cubes);
    System.out.println(d);
    assertEquals("sdp must be 32 + 128 = 160 + 249", 160.0 + 249.0, d, 0.001);
  }

}
