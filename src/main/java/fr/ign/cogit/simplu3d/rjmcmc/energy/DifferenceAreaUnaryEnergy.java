package fr.ign.cogit.simplu3d.rjmcmc.energy;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.geometry.Rectangle2D;
import fr.ign.rjmcmc.energy.UnaryEnergy;

public class DifferenceAreaUnaryEnergy<T> implements UnaryEnergy<T> {
  Geometry bpu;
  public DifferenceAreaUnaryEnergy(Geometry p) {
    this.bpu = p;
  }

  @Override
  public double getValue(T t) {
    if (!(t instanceof Rectangle2D)) {
      return 0;
    }
    try {
      Geometry difference = ((Rectangle2D) t).toGeometry().difference(this.bpu);
      return difference.getArea();
    } catch (Exception e) {
//      System.out.println("G = " + ((Rectangle2D) t).toGeometry());
//      System.out.println("BPU = " + bpu);
    }
    return 0;
  }

}
