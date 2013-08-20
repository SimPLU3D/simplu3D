package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboidSnap;

import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.CuboidSnap;
import fr.ign.rjmcmc.energy.BinaryEnergy;

public class DifferenceVolumeBinaryEnergy<T, U> implements BinaryEnergy<T, U> {

  @Override
  public double getValue(T t, U u) {

    if (!(t instanceof CuboidSnap) || !(u instanceof CuboidSnap)) {
      System.out.println("Problem");
      return 0;
    }

    CuboidSnap c1 = (CuboidSnap) t;
    CuboidSnap c2 = (CuboidSnap) u;

    if (CuboidSnap.do_intersect(c1, c2)) {
      
      
      double area = CuboidSnap.intersection_area(c1, c2);

      return area * Math.min(c1.height, c2.height);

    }

    return 0;
  }

}
