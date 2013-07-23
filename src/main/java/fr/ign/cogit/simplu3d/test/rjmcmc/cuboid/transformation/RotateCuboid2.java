package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation;

import fr.ign.rjmcmc.kernel.Transform;

public class RotateCuboid2 implements Transform {
  
  @Override
  public void apply(double[] in, double[] out) {
    double x = in[0];
    double y = in[1];
    double length = in[2];
    double width = in[3];
    double orientation = in[4];
    double height = in[5];
    double dor = in[6];


    
    // res = Rectangle_2(c+v+u, n+v,r);
    out[0] = x;
    out[1] = y;
    out[2] = length;
    out[3] = width ;
    out[4] = orientation + dor * Math.PI;
    out[5] = height;
    out[6] = -dor;

  }

  @Override
  public double getAbsJacobian() {

    return 1;
  }

  @Override
  public double getAbsJacobian(double[] d) {

    return 1;
  }

  @Override
  public void inverse(double[] in, double[] out) {
    this.apply(in, out);
  }
  
  @Override
  public int dimension() {

    return 7;
  }


}
