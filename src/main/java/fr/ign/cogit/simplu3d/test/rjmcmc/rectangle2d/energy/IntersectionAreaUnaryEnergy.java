package fr.ign.cogit.simplu3d.test.rjmcmc.rectangle2d.energy;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.geometry.Rectangle2D;
import fr.ign.rjmcmc.energy.UnaryEnergy;

public class IntersectionAreaUnaryEnergy<T> implements UnaryEnergy<T> {
  Geometry bpu;
  public IntersectionAreaUnaryEnergy(Geometry p) {
    this.bpu = p;
  }

  @Override
  public double getValue(T t) {
    if (!(t instanceof Rectangle2D)) {
      return 0;
    }
    return ((Rectangle2D) t).toGeometry().getArea();
//    try {
//      Geometry intersection = ((Rectangle2D) t).toGeometry().intersection(this.bpu);
//      return intersection.getArea();
//    } catch (Exception e) {
//      System.out.println("G = " + ((Rectangle2D) t).toGeometry());
//      System.out.println("BPU = " + bpu);
//
//    }
//    return 0;
  }

}
