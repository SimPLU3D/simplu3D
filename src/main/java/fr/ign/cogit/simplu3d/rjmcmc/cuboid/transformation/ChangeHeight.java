package fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation;

import java.util.Vector;

import fr.ign.rjmcmc.kernel.Transform;

public class ChangeHeight implements Transform {

  private double amplitude;

  public ChangeHeight(double amplitude) {
    this.amplitude = amplitude;
  }


  @Override
  public double apply(boolean direct, Vector<Double> val0, Vector<Double> var0,
      Vector<Double> val1, Vector<Double> var1) {

    double dh = var0.get(0);

    val1.set(0, val0.get(0));
    val1.set(1, val0.get(1));
    val1.set(2, val0.get(2));
    val1.set(3, val0.get(3));
    val1.set(4, val0.get(4)+   (0.5 - dh) * amplitude);
    val1.set(5, val0.get(5));

    var1.set(0, 1 - dh);

    return 1;
  }

  @Override
  public double getAbsJacobian(boolean direct) {
    // TODO Auto-generated method stub
    return 1;
  }

  @Override
  public int dimension(int n0, int n1) {
    // TODO Auto-generated method stub
    return 7;
  }
}
