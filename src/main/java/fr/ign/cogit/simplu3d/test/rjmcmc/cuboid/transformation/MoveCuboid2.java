package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation;

import fr.ign.rjmcmc.kernel.Transform;

public class MoveCuboid2  implements Transform {

  private double amplitudeMove;
  
  public MoveCuboid2(double amplitudeMove){
    this.amplitudeMove = amplitudeMove;
  }


  @Override
  public void apply(double[] in, double[] out) {
    double x = in[0];
    double y = in[1];
    double length = in[2];
    double width = in[3];
    double height = in[4];
    double orientation = in[5];
    double dx = in[6];
    double dy = in[7];

    // res = Rectangle_2(c+v+u, n+v,r);
    out[0] = x + ( 0.5 - dx) * amplitudeMove;
    out[1] = y + (0.5 - dy) * amplitudeMove;
    out[2] = length;
    out[3] = width;
    out[4] = height;
    out[5] = orientation;
    out[6] = 1-dx;
    out[7] = 1-dy;
  }

  @Override
  public double getAbsJacobian() {
    // TODO Auto-generated method stub
    return 1;
  }

  @Override
  public double getAbsJacobian(double[] d) {
    // TODO Auto-generated method stub
    return 1;
  }

  @Override
  public void inverse(double[] in, double[] out) {
    this.apply(in, out);
  }
  
  @Override
  public int dimension() {
    // TODO Auto-generated method stub
    return 8;
  }

}
