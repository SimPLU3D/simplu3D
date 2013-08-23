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

public class Cuboid2 extends Building implements Primitive, SimpleObject {
  public double centerx;
  public double centery;
  public double length;
  public double width;
  private double orientation = 0;
  public double height;

  public boolean isNew = true;

  protected Cuboid2(){
    
  }
  
  public Cuboid2(double centerx, double centery, double length, double width,
    double height) {
    super();
    this.centerx = centerx;
    this.centery = centery;
    this.length = length;
    this.width = width;
    this.height = height;

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
      /*
      double cosOrient = Math.cos(orientation);
      double sinOrient = Math.sin(orientation);

      
      double a = cosOrient * length / 2;
      double b = sinOrient * width / 2;
      
      
      double c = sinOrient * length / 2;
      double d = cosOrient * width / 2;
      

      pts[0] = new Coordinate(this.centerx   - a + b,
          this.centery  - c - d, height);

      pts[1] = new Coordinate(this.centerx  + a + b,
          this.centery +   c - d, height);

      pts[2] = new Coordinate(this.centerx +   a - b,
          this.centery +  c + d, height);

      pts[3] = new Coordinate(this.centerx  - a - b,
          this.centery   - c + d, height);*/
          
      
      double hLength = length/2;
      double hWidth = width/2;

      pts[0] = new Coordinate(this.centerx   - hLength,
          this.centery  -hWidth );

      pts[1] = new Coordinate(this.centerx  + hLength,
          this.centery - hWidth, height);

      pts[2] = new Coordinate(this.centerx +   hLength,
          this.centery + hWidth);

      pts[3] = new Coordinate(this.centerx  - hLength,
          this.centery  + hWidth, height);

      pts[4] = new Coordinate(pts[0]);
      LinearRing ring = geomFact.createLinearRing(pts);
      Polygon poly = geomFact.createPolygon(ring, null);
      this.geom = poly;
    }
    return this.geom;
  }

  @Override
  public Object[] toArray() {
    return new Object[] { this.centerx, this.centery, this.length, this.width, this.height };
  }

  @Override
  public int size() {
    return 5;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    double[] array = { this.centerx, this.centery, this.length, this.width,
        this.orientation, this.height };
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
    if (!(o instanceof Cuboid2)) {
      return false;
    }
    Cuboid2 r = (Cuboid2) o;
    return this.centerx == r.centerx && this.centery == r.centery
        && this.width == r.width && this.length == r.length
        && this.orientation == r.orientation && this.height == r.height;
  }

  public String toString() {
    return "Cuboid : " +" Centre " + this.centerx +"; " + this.centery + "  hauteur "+ this.height+ " largeur  " + this.width + "   longueur  " + this.width ;

  }

  private Rectangle2D rectangle = null;

  public Rectangle2D getRectangle2D() {

    if (rectangle == null) {
      rectangle = new Rectangle2D(this.centerx, this.centery,
          Math.cos(orientation) * length / 2 , Math.sin(orientation) * length / 2 , width    / length);
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
      zMin = env.getTerrain().castCoordinate(this.centerx, this.centery).z;

    }
    // TODO Auto-generated method stub
    return zMin;
  }

  public static boolean do_intersect(Cuboid2 a, Cuboid2 b) {

    return Rectangle2D.do_intersect(a.getRectangle2D(), b.getRectangle2D());
  }

  public static double intersection_area(Cuboid2 a, Cuboid2 b) {

    return Rectangle2D
        .intersection_area(a.getRectangle2D(), b.getRectangle2D());
  }

}
