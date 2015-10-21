package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple;

import org.apache.commons.math3.util.MathUtils;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;

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
 **/
public class SimpleCuboid extends Cuboid {
  public SimpleCuboid(double centerx, double centery, double length, double width, double height, double orientation) {
    super(centerx, centery, length, width, height, orientation);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SimpleCuboid)) {
      return false;
    }
    SimpleCuboid r = (SimpleCuboid) o;
    return MathUtils.equals(this.centerx, r.centerx) && MathUtils.equals(this.centery, r.centery)
        && MathUtils.equals(this.width, r.width) && MathUtils.equals(this.length, r.length)
        && MathUtils.equals(this.orientation, r.orientation) && MathUtils.equals(this.height, r.height);
  }

  public String toString() {
    return "SimpleCuboid : " + " Centre " + this.centerx + "; " + this.centery + " hauteur " + this.height
        + " largeur " + this.width + " longueur " + this.width + " orientation " + this.orientation;

  }
}
