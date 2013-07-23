package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2;

import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.Cuboid2;
import fr.ign.rjmcmc.energy.UnaryEnergy;

public class VolumeUnaryEnergy<T> implements UnaryEnergy<T> {

  @Override
  public double getValue(T t) {
    if (!(t instanceof Cuboid2)) {
      return 0;
    }
    Cuboid2 c = (Cuboid2) t;

    return c.width * c.length * c.height;

  }

}
