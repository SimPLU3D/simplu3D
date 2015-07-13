package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple;

import java.util.List;

import org.apache.commons.math.util.MathUtils;

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
public class ParallelCuboid extends Cuboid {

  public ParallelCuboid(double centerx, double centery, double length, double width, double height, double orientation) {
    super(centerx, centery, length, width, height, orientation);
  }
  @Override
  public Object[] getArray() {
    return new Object[] { this.centerx, this.centery, this.length/*, this.width*/, this.height/*, this.orientation*/ };
  }

  @Override
  public int size() {
    return 4;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    double[] array = { this.centerx, this.centery, this.length/*, this.width*/, this.orientation/*, this.height*/ };
    for (double e : array)
      hashCode = 31 * hashCode + hashCode(e);
    return hashCode;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ParallelCuboid)) {
      return false;
    }
    ParallelCuboid r = (ParallelCuboid) o;
    return MathUtils.equals(this.centerx, r.centerx) && MathUtils.equals(this.centery, r.centery)
        && MathUtils.equals(this.width, r.width) && MathUtils.equals(this.length, r.length)
        && MathUtils.equals(this.orientation, r.orientation) && MathUtils.equals(this.height, r.height);
  }

  public String toString() {
    return "ParallelCuboid : " + " Centre " + this.centerx + "; " + this.centery + " hauteur " + this.height + " largeur "
        + this.width + " longueur " + this.width + " orientation " + this.orientation;

  }
  @Override
  public double[] toArray() {
    return new double[] { this.centerx, this.centery, this.length/*, this.width*/, this.height/*, this.orientation*/ };
  }

  @Override
  public void set(List<Double> list) {
    this.centerx = list.get(0);
    this.centery = list.get(1);
    this.length = list.get(2);
//    this.width = list.get(3);
    this.height = list.get(3);
//    this.orientation = list.get(5);
    this.isNew = true;
  }
}
