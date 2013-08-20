package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation;

import fr.ign.rjmcmc.kernel.Transform;



public class ChangeWidth implements Transform {
  
  private double amplitude;
  
  public ChangeWidth(double amplitude){
    this.amplitude = amplitude;

  }
  
  
  @Override
  public void apply(double[] in, double[] out) {
    double x = in[0];
    double y = in[1];
    double length = in[2];
    double width = in[3];
    double height = in[4];
    double dl = in[5];


    // res = Rectangle_2(c+v+u, n+v,r);
    out[0] = x;
    out[1] = y;
    out[2] = length;
    out[3] = width + ( 0.5 - dl ) * amplitude;
    out[4] = height;
    out[5] = 1-dl;

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
    return 6;
  }

}
