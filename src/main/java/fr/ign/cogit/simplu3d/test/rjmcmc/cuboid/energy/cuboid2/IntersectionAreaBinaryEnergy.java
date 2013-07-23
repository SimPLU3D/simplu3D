package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2;

import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.Cuboid2;
import fr.ign.geometry.Primitive;
import fr.ign.rjmcmc.energy.BinaryEnergy;

public class IntersectionAreaBinaryEnergy<T extends Primitive> implements
    BinaryEnergy<T, T> {
  public IntersectionAreaBinaryEnergy() {
  }

  @Override
  public double getValue(T t, T u) {
    Cuboid2 a = (Cuboid2) t;
    Cuboid2 b = (Cuboid2) u;

    if (Cuboid2.do_intersect(a, b)) {
      return 999999; //Cuboid2.intersection_area(a, b);
    }
    return 0;
    // return t.intersectionArea(u);
  }

}
