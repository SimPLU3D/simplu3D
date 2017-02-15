package fr.ign.cogit.simplu3d.rjmcmc.paramshp.builder;

import fr.ign.cogit.simplu3d.rjmcmc.paramshp.geometry.impl.CuboidRoofed;
import fr.ign.mpp.kernel.ObjectBuilder;

public class CuboidRoofedBuilder implements ObjectBuilder<CuboidRoofed> {

  @Override
  public CuboidRoofed build(double[] val) {
    return new CuboidRoofed(val[0], val[1], val[2], val[3], val[4], val[5],
        val[6], val[7]);
  }

  @Override
  public void setCoordinates(CuboidRoofed t, double[] val) {
    t.setCoordinates(val);
  }

  @Override
  public int size() {
    return 8;
  }

}
