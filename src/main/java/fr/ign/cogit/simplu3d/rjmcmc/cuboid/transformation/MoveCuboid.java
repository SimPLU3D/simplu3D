package fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation;

import java.util.Vector;

import fr.ign.rjmcmc.kernel.Transform;

public class MoveCuboid implements Transform {

  private double amplitudeMove;

  public MoveCuboid(double amplitudeMove) {
    this.amplitudeMove = amplitudeMove;
  }

  @Override
  public double apply(boolean direct, Vector<Double> val0, Vector<Double> var0,
      Vector<Double> val1, Vector<Double> var1) {

    double dx = var0.get(0);
    double dy = var0.get(1);
    val1.set(0, val0.get(0) + (0.5 - dx) * amplitudeMove);
    val1.set(1, val0.get(1) + (0.5 - dy) * amplitudeMove);
    val1.set(2, val0.get(2));
    val1.set(3, val0.get(3));
    val1.set(4, val0.get(4));
    val1.set(5, val0.get(5));

    var1.set(0, 1 - dx);
    var1.set(1, 1 - dy);
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
    return 8;
  }

}
