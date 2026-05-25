package fr.ign.cogit.simplu3d.rjmcmc.paramshp.geometry.impl;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.*;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;

public class CuboidRoofed extends Cuboid {

  private static final GeometryFactory gf = new GeometryFactory();
  private double heightT;

  public double getHeightT() {
    return heightT;
  }

  public void setHeightT(double heightT) {
    this.heightT = heightT;
  }

  public double getDeltaFromSide() {
    return deltaFromSide;
  }

  public void setDeltaFromSide(double deltaFromSide) {
    this.deltaFromSide = deltaFromSide;
  }

  private double deltaFromSide;

  public CuboidRoofed(double centerx, double centery, double length,
      double width, double heightG, double orientation, double heightT,
      double deltaFromSide) {
    super(centerx, centery, length, width, heightG, orientation);
    this.heightT = heightT;
    this.deltaFromSide = deltaFromSide;
  }

  @Override
  public double height() {
    return heightT + height;
  }

  @Override
  public double getVolume() {
    double v = width * height * length;
    double Vsp = (deltaFromSide == 0) ? 0
        : 2 * (heightT * width * deltaFromSide / 3); // volume coté largeur
    double Vbp = 2 * (heightT * length * width * 0.5 / 3); // volume coté
                                                           // longueur
    v = v + (length * width * heightT) - Vsp - Vbp;
    return v;
  }

  @Override
  public double[] toArray() {
    return new double[] { this.centerx, this.centery, this.length, this.width,
        this.height, this.orientation, this.heightT, this.deltaFromSide };
  }

  @Override
  public int size() {
    return 8;
  }

  @Override
  public int hashCode() {
    int hashCode = super.hashCode();
    hashCode = (31 * hashCode + super.hashCode(heightT)) * 31
        + super.hashCode(deltaFromSide);
    return hashCode;
  }

  @Override
  public void set(List<Double> list) {
    super.set(list);
    this.heightT = list.get(6);
    this.deltaFromSide = list.get(7);
  }

  @Override
  public void setCoordinates(double[] val1) {
    super.setCoordinates(val1);
    val1[6] = this.heightT;
    val1[7] = this.deltaFromSide;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CuboidRoofed r)) {
      return false;
    }
    return (this.centerx == r.centerx) && (this.centery == r.centery)
            && (this.width == r.width) && (this.length == r.length)
            && (this.orientation == r.orientation) && (this.height == r.height)
            && (this.heightT == r.heightT)
            && (this.deltaFromSide == r.deltaFromSide);
  }

  public String toString() {
    return super.toString() + " hauteur Toit " + heightT + " delta empan "
        + deltaFromSide;
  }

  public Geometry generated3DGeom() {
    List<Coordinate[]> lDpl = super.createCubePointsList();
    addRoofCoords(lDpl);
    List<Polygon> lOS = new ArrayList<>();
    for (Coordinate[] dpl : lDpl) {
      lOS.add(gf.createPolygon(dpl));
    }
    return gf.createMultiPolygon(lOS.toArray(new Polygon[0]));
    // return generate(this, this.getZmin());
  }

  private void addRoofCoords(List<Coordinate[]> lDpl) {
    double zmin = getZmin();
    // pointsCube = new ArrayList<CoordinateList>();
    Coordinate[] dpl = this.getFootprint().getCoordinates();
    Coordinate p1 = dpl[0];
    p1.setZ(zmin + height);
    Coordinate p2 = dpl[1];
    p2.setZ(zmin + height);
    Coordinate p3 = dpl[2];
    p3.setZ(zmin + height);
    Coordinate p4 = dpl[3];
    p4.setZ(zmin + height);
    Coordinate e1 = new Coordinate(emp1.x, emp1.y, this.height());
    Coordinate e2 = new Coordinate(emp2.x, emp2.y, this.height());
    CoordinateList dpl7 = new CoordinateList();
    dpl7.add(p1);
    dpl7.add(e1);
    dpl7.add(p4);
    dpl7.add(p1);
    lDpl.add(dpl7.toCoordinateArray());
    CoordinateList dpl8 = new CoordinateList();
    dpl8.add(p2);
    dpl8.add(e2);
    dpl8.add(p3);
    dpl8.add(p2);
    lDpl.add(dpl8.toCoordinateArray());
    CoordinateList dpl9 = new CoordinateList();
    dpl9.add(p1);
    dpl9.add(e1);
    dpl9.add(e2);
    dpl9.add(p2);
    dpl9.add(p1);
    lDpl.add(dpl9.toCoordinateArray());
    CoordinateList dpl10 = new CoordinateList();
    dpl10.add(p3);
    dpl10.add(e2);
    dpl10.add(e1);
    dpl10.add(p4);
    dpl10.add(p3);
    lDpl.add(dpl10.toCoordinateArray());
  }

  // coordinates of the roof shifts from the side
  private Coordinate emp1 = null, emp2 = null;
  
  @Override
  public Polygon toGeometry() {
    Polygon geomJTS = super.toGeometry();
    if (emp1 == null) {
      double factor = deltaFromSide / length;
      Coordinate p0 = geomJTS.getCoordinates()[0];
      Coordinate p1 = geomJTS.getCoordinates()[1];
      Coordinate p2 = geomJTS.getCoordinates()[2];
      Coordinate v1 = new Coordinate(
              factor * (p1.x - p0.x),
              factor * (p1.y - p0.y)
      );
      Coordinate v2 = new Coordinate(0.5 * (p2.x - p1.x), 0.5 * (p2.y - p1.y));
      emp1 = new Coordinate(p0.x + v2.x + v1.x, p0.y + v2.y + v1.y);
      emp2 = new Coordinate(p1.x + v2.x - v1.x, p1.y + v2.y - v1.y);
    }
    return geomJTS;
  }
}
