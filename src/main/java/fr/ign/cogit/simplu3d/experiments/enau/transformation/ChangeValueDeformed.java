package fr.ign.cogit.simplu3d.experiments.enau.transformation;

import fr.ign.rjmcmc.kernel.Transform;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class ChangeValueDeformed implements Transform {

  private double amplitude;
  private int dimension;
  private int index;

  public ChangeValueDeformed(double amplitude, int dimension, int index) {
    this.amplitude = amplitude;
    this.dimension = dimension;
    this.index = index;
  }

  @Override
  public double apply(boolean direct, double[] val0, double[] val1) {
    for (int i = 0; i < this.dimension - 1; i++) {
      val1[i] = val0[i];
    }
    double dh = val0[this.dimension - 1];
    val1[index] += (0.5 - dh) * amplitude;
    val1[this.dimension - 1] = 1 - dh;
    return 1;
  }

  // @Override
  public double getAbsJacobian(boolean direct) {
    return 1;
  }

  @Override
  public int dimension() {
    return this.dimension;
  }
}
