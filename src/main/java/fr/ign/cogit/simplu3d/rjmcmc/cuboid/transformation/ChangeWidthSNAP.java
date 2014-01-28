package fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.ParameterCuboidSNAP;
import fr.ign.rjmcmc.kernel.Transform;

public class ChangeWidthSNAP implements Transform {

//  private double mindim, maxdim;

  public ChangeWidthSNAP(double mindim, double maxdim) {
//    this.mindim = mindim;
//    this.maxdim = maxdim;
  }

  @Override
  public double apply(double[] in, double[] out) {
    double x = in[0];
    double y = in[1];
    double length = in[2];
    double width = in[3];
    double orientation = in[4];
    double height = in[5];
    double dl = in[6];

    // res = Rectangle_2(c+v+u, n+v,r);
    out[0] = x;
    out[1] = y;
    out[2] = length;
    out[3] = width + Math.signum(0.5 - dl) * ParameterCuboidSNAP.SNAPY;
    out[4] = orientation;
    out[5] = height;
    out[6] = 1 - dl;
    return 1;
  }

  @Override
  public double getInverseAbsJacobian(double[] d) {
    // FIXME CHECK THAT
    return 1;
  }

  @Override
  public double getAbsJacobian(double[] d) {
    // TODO Auto-generated method stub
    return 1;
  }

  @Override
  public double inverse(double[] in, double[] out) {
    this.apply(in, out);
    return 1;
  }

  @Override
  public int dimension() {
    // TODO Auto-generated method stub
    return 7;
  }

}
