package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation;

import fr.ign.rjmcmc.kernel.Transform;

public class RotateCuboid2 implements Transform {

  private double amplitudeRotate;

  public RotateCuboid2(double amp) {
    amplitudeRotate = amp;
  }

  @Override
  public double apply(double[] in, double[] out) {
    double x = in[0];
    double y = in[1];
    double length = in[2];
    double width = in[3];
    double height = in[4];
    double orientation = in[5];
    double dor = in[6];
    out[0] = x;
    out[1] = y;
    out[2] = length;
    out[3] = width;
    out[4] = height;
    double newAngle = orientation + (0.5 - dor) * amplitudeRotate;
    double modulo = newAngle % (Math.PI);
    if (modulo < 0) {
      modulo = Math.PI + modulo;
    }
    out[5] = modulo;
    out[6] = 1 - dor;
    return 1;
  }

  @Override
  public double getInverseAbsJacobian(double[] d) {
    return 1;
  }

  @Override
  public double getAbsJacobian(double[] d) {
    return 1;
  }

  @Override
  public double inverse(double[] in, double[] out) {
    return this.apply(in, out);
  }

  @Override
  public int dimension() {
    return 7;
  }
}
