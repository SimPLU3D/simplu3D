package fr.ign.cogit.simplu3d.rjmcmc.cuboid.energy.cuboid2;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.geometry.Primitive;
import fr.ign.rjmcmc.energy.BinaryEnergy;
/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class IntersectionVolumeBinaryEnergy<T extends Primitive> implements
    BinaryEnergy<T, T> {
  public IntersectionVolumeBinaryEnergy() {
  }

  @Override
  public double getValue(T t, T u) {

    if (!(t instanceof Cuboid) || !(u instanceof Cuboid)) {
      System.out.println("Problem");
      return 0;
    }

    Cuboid c1 = (Cuboid) t;
    Cuboid c2 = (Cuboid) u;

    if (Cuboid.do_intersect(c1, c2)) {
      
      
      double area = Cuboid.intersection_area(c1, c2);

      return area * Math.min(c1.height, c2.height);

    }

    return 0;
  }



}
