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
public class RotateCuboid implements Transform {

  private double amplitudeRotate;

  public RotateCuboid(double amp) {
    amplitudeRotate = amp;
  }

  @Override
  public double apply(boolean direct, double[] val0, double[] val1) {

    double dor = val0[9];
    double newAngle = val0[8] + (0.5 - dor) * amplitudeRotate;
    double modulo = newAngle % (Math.PI);
    if (modulo < 0) {
      modulo = Math.PI + modulo;
    }
    val1[0] = val0[0];
    val1[1] = val0[1];
    val1[2] = val0[2];
    val1[3] = val0[3];
    val1[4] = val0[4];
    val1[5] = val0[5];
    val1[6] = val0[6];
    val1[7] = val0[7];
    val1[9] = modulo;

    val1[9] = 1 - dor;
    return 1;
  }

//  @Override
  public double getAbsJacobian(boolean direct) {
    return 1;
  }

  @Override
  public int dimension() {
    return 10;
  }
}
