package fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation;

import java.util.Vector;

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
  public double apply(boolean direct, Vector<Double> val0, Vector<Double> var0,
      Vector<Double> val1, Vector<Double> var1) {
    double dor = var0.get(0);
    double newAngle = val0.get(5) + (0.5 - dor) * amplitudeRotate;
    double modulo = newAngle % (Math.PI);
    if (modulo < 0) {
      modulo = Math.PI + modulo;
    }
    val1.set(0, val0.get(0));
    val1.set(1, val0.get(1));
    val1.set(2, val0.get(2));
    val1.set(3, val0.get(3));
    val1.set(4, val0.get(4));
    val1.set(5, modulo);

    var1.set(0, 1 - dor);
    return 1;
  }

  @Override
  public double getAbsJacobian(boolean direct) {
    return 1;
  }

  @Override
  public int dimension(int n0, int n1) {
    return 7;
  }
}
