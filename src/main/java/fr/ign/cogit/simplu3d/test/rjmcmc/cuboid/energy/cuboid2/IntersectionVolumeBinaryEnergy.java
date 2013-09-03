package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2;

import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.geometry.Primitive;
import fr.ign.rjmcmc.energy.BinaryEnergy;

public class IntersectionVolumeBinaryEnergy<T extends Primitive> implements
    BinaryEnergy<T, T> {
  public IntersectionVolumeBinaryEnergy() {
  }

  @Override
  public double getValue(T t, T u) {

    if (!(t instanceof Cuboid2) || !(u instanceof Cuboid2)) {
      System.out.println("Problem");
      return 0;
    }

    Cuboid2 c1 = (Cuboid2) t;
    Cuboid2 c2 = (Cuboid2) u;

    if (Cuboid2.do_intersect(c1, c2)) {
      
      
      double area = Cuboid2.intersection_area(c1, c2);

      return area * Math.min(c1.height, c2.height);

    }

    return 0;
  }



}
