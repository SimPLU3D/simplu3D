package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.geometry.Rectangle2D;
import fr.ign.rjmcmc.energy.UnaryEnergy;

public class DifferenceVolumeUnaryEnergy<T> implements UnaryEnergy<T> {
  Geometry bpu;
  public DifferenceVolumeUnaryEnergy(Geometry p) {
    this.bpu = p;
  }

  @Override
  public double getValue(T t) {

    try {
      if (t instanceof Cuboid2) {
        Geometry difference = ((Cuboid2) t).toGeometry()
            .difference(this.bpu);


        return difference.getArea() * ((Cuboid2) t).height; // Math.exp(difference.getArea() * ((Cuboid)t).height ) ;

      }

    } catch (Exception e) {
      System.out.println("G = " + ((Rectangle2D) t).toGeometry());
      System.out.println("BPU = " + bpu);
    }
    return 0;
  }

}
