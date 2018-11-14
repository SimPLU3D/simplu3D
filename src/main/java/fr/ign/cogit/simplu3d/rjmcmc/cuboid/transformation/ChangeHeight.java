package fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation;

import fr.ign.rjmcmc.kernel.Transform;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see http://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class ChangeHeight implements Transform {

  private double amplitude;

  public ChangeHeight(double amplitude) {
    this.amplitude = amplitude;
  }

  @Override
  public double apply(boolean direct, double[] val0, double[] val1) {

    double dh = val0[6];

    val1[0] = val0[0];
    val1[1] = val0[1];
    val1[2] = val0[2];
    val1[3] = val0[3];
    val1[4] = val0[4] + (0.5 - dh) * amplitude;
    val1[5] = val0[5];

    val1[6] = 1 - dh;

    return 1;
  }

  // @Override
  public double getAbsJacobian(boolean direct) {
    return 1;
  }

  @Override
  public int dimension() {
    return 7;
  }
}
