package fr.ign.cogit.simplu3d.experiments.enau.transformation;

import fr.ign.rjmcmc.kernel.Transform;
/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class MoveCuboidDeformed implements Transform {

  private double amplitudeMove;

  public MoveCuboidDeformed(double amplitudeMove) {
    this.amplitudeMove = amplitudeMove;
  }

  @Override
  public double apply(boolean direct, double[] val0, double[] val1) {

    double dx = val0[9];
    double dy = val0[10];
    val1[0] = val0[0] + (0.5 - dx) * amplitudeMove;
    val1[1] = val0[1] + (0.5 - dy) * amplitudeMove;
    val1[2] = val0[2];
    val1[3] = val0[3];
    val1[4] = val0[4];
    val1[5] = val0[5];
    val1[6] = val0[6];
    val1[7] = val0[7];
    val1[8] = val0[8];

    val1[9] = 1 - dx;
    val1[10] = 1 - dy;
    return 1;
  }

//  @Override
  public double getAbsJacobian(boolean direct) {
    return 1;
  }

  @Override
  public int dimension() {
    return 11;
  }
}
