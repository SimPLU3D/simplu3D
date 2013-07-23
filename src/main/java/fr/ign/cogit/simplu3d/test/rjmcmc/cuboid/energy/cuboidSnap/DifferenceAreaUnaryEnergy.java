package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboidSnap;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.CuboidSnap;
import fr.ign.geometry.Rectangle2D;
import fr.ign.rjmcmc.energy.UnaryEnergy;

public class DifferenceAreaUnaryEnergy<T> implements UnaryEnergy<T> {
  Geometry bpu;

  public DifferenceAreaUnaryEnergy(Geometry p) {
    this.bpu = p;
  }

  @Override
  public double getValue(T t) {

    try {
      if (t instanceof CuboidSnap) {
        Geometry difference = ((CuboidSnap) t).toGeometry()
            .difference(this.bpu);


        return difference.getArea() * ((CuboidSnap) t).height; // Math.exp(difference.getArea() * ((Cuboid)t).height ) ;

      }

    } catch (Exception e) {
      System.out.println("G = " + ((Rectangle2D) t).toGeometry());
      System.out.println("BPU = " + bpu);
    }
    return 0;
  }

}
