package fr.ign.cogit.simplu3d.rjmcmc.paramshp.geometry.impl;

import java.util.List;

public abstract class AbstractParallelCuboidRoofed extends CuboidRoofed {

  public AbstractParallelCuboidRoofed(double centerx, double centery,
      double length, double width, double height, double orientation,
      double heightT, double deltaFromSide) {
    super(centerx, centery, length, width, height, orientation, heightT,
        deltaFromSide);
  }

  @Override
  public Object[] getArray() {
    return new Object[] { this.centerx, this.centery,
        this.length/* , this.width */, this.height/* , this.orientation */,
        this.getHeightT(), this.getDeltaFromSide() };
  }

  @Override
  public int size() {
    return 6;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    double[] array = { this.centerx, this.centery,
        this.length/* , this.width */, this.orientation/* , this.height */,
        this.getHeightT(), this.getDeltaFromSide() };
    for (double e : array)
      hashCode = 31 * hashCode + hashCode(e);
    return hashCode;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof AbstractParallelCuboidRoofed)) {
      return false;
    }
    AbstractParallelCuboidRoofed r = (AbstractParallelCuboidRoofed) o;
    // return r.equals(o);
    return (this.centerx == r.centerx) && (this.centery == r.centery)
    /* && (this.width == r.width) */ && (this.length == r.length)
        && (this.orientation == r.orientation) && (this.height == r.height)
        && this.getHeightT() == r.getHeightT()
        && this.getDeltaFromSide() == r.getDeltaFromSide();
  }

  public String toString() {
    return "ParallelCuboidRoofed : " + " Centre " + this.centerx + "; "
        + this.centery + " hauteur " + this.height + " largeur " + this.width
        + " longueur " + this.width + " orientation " + this.orientation;

  }

  @Override
  public double[] toArray() {
    return new double[] { this.centerx, this.centery,
        this.length/* , this.width */, this.height/* , this.orientation */ ,
        this.getHeightT(), this.getDeltaFromSide() };
  }

  @Override
  public void set(List<Double> list) {
    this.centerx = list.get(0);
    this.centery = list.get(1);
    this.length = list.get(2);
    // this.width = list.get(3);
    this.height = list.get(3);
    this.setHeightT(list.get(4));
    this.setDeltaFromSide(list.get(4));
    // this.orientation = list.get(5);
	this.setNew(true);
  }

}
