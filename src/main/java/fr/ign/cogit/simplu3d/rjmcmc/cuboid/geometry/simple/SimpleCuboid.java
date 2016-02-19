package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple;

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
    return (this.centerx== r.centerx) && (this.centery== r.centery)
        && (this.width== r.width) && (this.length== r.length)
        && (this.orientation== r.orientation) && (this.height== r.height);
  }

  public String toString() {
    return "SimpleCuboid : " + " Centre " + this.centerx + "; " + this.centery + " hauteur " + this.height
        + " largeur " + this.width + " longueur " + this.width + " orientation " + this.orientation;

  }
}
