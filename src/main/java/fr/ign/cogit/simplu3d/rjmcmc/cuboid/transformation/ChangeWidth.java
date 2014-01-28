package fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation;

import fr.ign.rjmcmc.kernel.Transform;

public class ChangeWidth implements Transform {

  private double amplitude;

  public ChangeWidth(double amplitude) {
    this.amplitude = amplitude;
  }

  @Override
  public double apply(double[] in, double[] out) {
    double x = in[0];
    double y = in[1];
    double length = in[2];
    double width = in[3];
    double height = in[4];
    double orientation = in[5];
    double dl = in[6];
    out[0] = x;
    out[1] = y;
    out[2] = length;
    out[3] = width + (0.5 - dl) * amplitude;
    out[4] = height;
    out[5] = orientation;
    out[6] = 1 - dl;
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
