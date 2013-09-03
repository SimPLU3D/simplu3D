package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboidSnap;

import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.CuboidSnap;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.ParameterCuboidSNAP;
import fr.ign.rjmcmc.energy.UnaryEnergy;

public class VolumeUnaryEnergy<T> implements UnaryEnergy<T> {

  @Override
  public double getValue(T t) {
    if (!(t instanceof CuboidSnap)) {
      return 0;
    }
    CuboidSnap c = (CuboidSnap) t;

    return c.width * c.length * c.height * ParameterCuboidSNAP.SNAPX *  ParameterCuboidSNAP.SNAPY ;

  }

}
