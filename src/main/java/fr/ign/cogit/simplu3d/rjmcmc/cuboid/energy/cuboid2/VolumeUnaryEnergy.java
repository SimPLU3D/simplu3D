package fr.ign.cogit.simplu3d.rjmcmc.cuboid.energy.cuboid2;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.rjmcmc.energy.UnaryEnergy;


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
public class VolumeUnaryEnergy<T> implements UnaryEnergy<T> {

  @Override
  public double getValue(T t) {


    if (!(t instanceof Cuboid)) {
      System.out.println("Probleme : volume unary energy");
      return 0;
    }
    Cuboid c = (Cuboid) t;

    
    double volume= c.width * c.length * c.height;

    return volume;

  }

}
