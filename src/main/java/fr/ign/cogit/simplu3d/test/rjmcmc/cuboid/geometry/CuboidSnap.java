package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.geometry.Primitive;
import fr.ign.geometry.Rectangle2D;
import fr.ign.rjmcmc.kernel.SimpleObject;

public class CuboidSnap extends Building implements Primitive, SimpleObject {
  public int centerx;
  public int centery;
  public int length;
  public int width;
  public double height;
  public double heightGut;

  public boolean isNew = true;

  public CuboidSnap(double centerx, double centery, double length,
      double width, double height, double heightGut) {
    super();
    this.centerx = (int) centerx;
    this.centery = (int) centery;
    this.length = (int) length;
    this.width = (int) width;
    this.height = height;
    this.heightGut= heightGut;
  }

  Polygon geom = null;

  @Override
  public double intersectionArea(Primitive p) {
    return this.toGeometry().intersection(p.toGeometry()).getArea();
  }

  IPolygon polyGeox = null;

  @Override
  public IOrientableSurface getFootprint() {

    if (polyGeox == null) {

      try {
        polyGeox = (IPolygon) JtsGeOxygene.makeGeOxygeneGeom(this.toGeometry());
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }

    return polyGeox;

  }

  @Override
  public Polygon toGeometry() {
    if (geom == null) {
      GeometryFactory geomFact = new GeometryFactory();
      Coordinate[] pts = new Coordinate[5];

      double xTemp = ParameterCuboidSNAP.X0 + this.centerx
          * ParameterCuboidSNAP.SNAPX;

      double yTemp = ParameterCuboidSNAP.Y0 + this.centery
          * ParameterCuboidSNAP.SNAPY;

      double lengthTemp = length * ParameterCuboidSNAP.SNAPX /2;
      double widthTemp = width * ParameterCuboidSNAP.SNAPY /2;

      pts[0] = new Coordinate(xTemp - lengthTemp, yTemp - widthTemp, height);

      pts[1] = new Coordinate(xTemp + lengthTemp, yTemp - widthTemp, height);

      pts[2] = new Coordinate(xTemp + lengthTemp, yTemp + widthTemp, height);

      pts[3] = new Coordinate(xTemp - lengthTemp, yTemp + widthTemp, height);

      pts[4] = new Coordinate(pts[0]);
      LinearRing ring = geomFact.createLinearRing(pts);
      Polygon poly = geomFact.createPolygon(ring, null);
      this.geom = poly;
    }
    return this.geom;
  }

  @Override
  public Object[] toArray() {
    return new Object[] { (double) this.centerx, (double) this.centery,
        (double) this.length, (double) this.width, (double) this.height , this.heightGut };
  }

  @Override
  public int size() {
    return 6;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    double[] array = { this.centerx, this.centery, this.length, this.width,
        this.height };
    for (double e : array)
      hashCode = 31 * hashCode + hashCode(e);
    return hashCode;
  }

  public int hashCode(double value) {
    long bits = Double.doubleToLongBits(value);

    return (int) (bits ^ (bits >>> 32));
  }

  public double height(int a, int b) {
    return height;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CuboidSnap)) {
      return false;
    }
    CuboidSnap r = (CuboidSnap) o;
    return this.centerx == r.centerx && this.centery == r.centery
        && this.width == r.width && this.length == r.length
        && this.height == r.height;
  }

  public String toString() {
    return "Cuboid : " + " Centre "
        + (ParameterCuboidSNAP.X0 + this.centerx * ParameterCuboidSNAP.SNAPX)
        + "; "
        + (ParameterCuboidSNAP.Y0 + this.centery * ParameterCuboidSNAP.SNAPY)
        + "  hauteur " + this.height + "  longueur  " + this.length
        * ParameterCuboidSNAP.SNAPX + " largeur  " + this.width
        * ParameterCuboidSNAP.SNAPY

    ;

  }

  private Rectangle2D rectangle = null;

  private Rectangle2D getRectangle2D() {

    if (rectangle == null) {
      rectangle = new Rectangle2D(ParameterCuboidSNAP.X0 + this.centerx
          * ParameterCuboidSNAP.SNAPX, ParameterCuboidSNAP.Y0 + this.centery
          * ParameterCuboidSNAP.SNAPY, length * ParameterCuboidSNAP.SNAPX, 0,
          width / length);
    }
    return rectangle;
  }

  enum Sign {
    NEGATIVE, POSITIVE, ZERO;
  }

  public static Sign sign(double t) {
    return (t < 0) ? Sign.NEGATIVE : ((t > 0) ? Sign.POSITIVE : Sign.ZERO);
  }

  double zMin = Double.NaN;

  public double getZmin() {
    if (Double.isNaN(zMin)) {

      Environnement env = Environnement.getInstance();
      zMin = env.getTerrain().castCoordinate(
          ParameterCuboidSNAP.X0 + this.centerx * ParameterCuboidSNAP.SNAPX,
          ParameterCuboidSNAP.Y0 + this.centery * ParameterCuboidSNAP.SNAPY).z;

    }
    // TODO Auto-generated method stub
    return zMin;
  }

  public static boolean do_intersect(CuboidSnap a, CuboidSnap b) {

    return Rectangle2D.do_intersect(a.getRectangle2D(), b.getRectangle2D());
  }

  public static double intersection_area(CuboidSnap a, CuboidSnap b) {

    return Rectangle2D
        .intersection_area(a.getRectangle2D(), b.getRectangle2D());
  }
}
