package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid;

import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.geometry.Primitive;
import fr.ign.rjmcmc.energy.BinaryEnergy;

public class IntersectionAreaBinaryEnergy<T extends Primitive> implements BinaryEnergy<T, T> {
  public IntersectionAreaBinaryEnergy() {
  }

  @Override
  public double getValue(T t, T u) {
    Cuboid a = (Cuboid) t;
    Cuboid b = (Cuboid) u;
    if (Cuboid.do_intersect(a, b)) {
      return Cuboid.intersection_area(a, b);
    }
    return 0;
    // return t.intersectionArea(u);
  }

}
