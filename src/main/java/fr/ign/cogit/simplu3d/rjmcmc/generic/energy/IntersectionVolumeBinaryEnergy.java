package fr.ign.cogit.simplu3d.rjmcmc.generic.energy;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.rjmcmc.energy.BinaryEnergy;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class IntersectionVolumeBinaryEnergy<T extends ISimPLU3DPrimitive>
    implements BinaryEnergy<T, T> {
  public IntersectionVolumeBinaryEnergy() {
  }

  @Override
  public double getValue(T t, T u) {

    double areaInter;
    if (t instanceof Cuboid && u instanceof Cuboid) {
      areaInter = t.intersectionArea(u);
    } else {
      areaInter = t.intersectionArea(u);
    }

    return areaInter * Math.min(t.getHeight(), u.getHeight());

  }

}
