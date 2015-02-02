package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.util.MathUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.FastBuildingPart;
import fr.ign.geometry.Primitive;
import fr.ign.geometry.Rectangle2D;

public class Cuboid extends AbstractSimpleBuilding implements Primitive {
  public double centerx;
  public double centery;
  public double length;
  public double width;
  public double orientation = 0;
  public double height;

  public Cuboid(double centerx, double centery, double length, double width,
      double height, double orientation) {
    super();
    this.isNew = true;
    this.centerx = centerx;
    this.centery = centery;
    this.length = length;
    this.width = width;
    this.height = height;
    this.orientation = orientation;
  }

  Polygon geomJTS = null;

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
    if (geomJTS == null) {
      GeometryFactory geomFact = new GeometryFactory();
      Coordinate[] pts = new Coordinate[5];
      double cosOrient = Math.cos(orientation);
      double sinOrient = Math.sin(orientation);
      double a = cosOrient * length / 2;
      double b = sinOrient * width / 2;
      double c = sinOrient * length / 2;
      double d = cosOrient * width / 2;
      pts[0] = new Coordinate(this.centerx - a + b, this.centery - c - d,
          height);
      pts[1] = new Coordinate(this.centerx + a + b, this.centery + c - d,
          height);
      pts[2] = new Coordinate(this.centerx + a - b, this.centery + c + d,
          height);
      pts[3] = new Coordinate(this.centerx - a - b, this.centery - c + d,
          height);
      pts[4] = new Coordinate(pts[0]);
      /*
       * double hLength = length / 2; double hWidth = width / 2; pts[0] = new
       * Coordinate(this.centerx - hLength, this.centery - hWidth); pts[1] = new
       * Coordinate(this.centerx + hLength, this.centery - hWidth, height);
       * pts[2] = new Coordinate(this.centerx + hLength, this.centery + hWidth);
       * pts[3] = new Coordinate(this.centerx - hLength, this.centery + hWidth,
       * height); pts[4] = new Coordinate(pts[0]);
       */

      LinearRing ring = geomFact.createLinearRing(pts);
      Polygon poly = geomFact.createPolygon(ring, null);
      this.geomJTS = poly;
    }
    return this.geomJTS;
  }

  @Override
  public Object[] getArray() {
    return new Object[] { this.centerx, this.centery, this.length, this.width,
        this.height, this.orientation };
  }

  @Override
  public int size() {
    return 6;
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
    if (!(o instanceof Cuboid)) {
      return false;
    }
    Cuboid r = (Cuboid) o;
    return MathUtils.equals(this.centerx, r.centerx)
        && MathUtils.equals(this.centery, r.centery)
        && MathUtils.equals(this.width, r.width)
        && MathUtils.equals(this.length, r.length)
        && MathUtils.equals(this.orientation, r.orientation)
        && MathUtils.equals(this.height, r.height);
  }

  public String toString() {
    return "Cuboid : " + " Centre " + this.centerx + "; " + this.centery
        + " hauteur " + this.height + " largeur " + this.width + " longueur "
        + this.width + " orientation " + this.orientation;

  }

  private Rectangle2D rectangle = null;

  public Rectangle2D getRectangle2D() {

    if (rectangle == null) {
      rectangle = new Rectangle2D(this.centerx, this.centery,
          Math.cos(orientation) * length / 2, Math.sin(orientation) * length
              / 2, width / length);
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
      if (env != null && env.getTerrain() != null) {
        zMin = env.getTerrain().castCoordinate(this.centerx, this.centery).z;
      } else {
        logger.warn("No terrain Cuboid ZMin set to 0");
        zMin = 0;
      }

    }
    // TODO Auto-generated method stub
    return zMin;
  }

  public static boolean do_intersect(Cuboid a, Cuboid b) {
    return Rectangle2D.do_intersect(a.getRectangle2D(), b.getRectangle2D());
  }

  public static double intersection_area(Cuboid a, Cuboid b) {
    return Rectangle2D
        .intersection_area(a.getRectangle2D(), b.getRectangle2D());
  }

  public List<AbstractBuilding> bandEpsilon(Geometry geom, double distMin,
      double distMax) {
    List<AbstractBuilding> lPolygonOut = new ArrayList<>();
    double d = geom.distance(geomJTS);
    if (d > distMax) {
      return lPolygonOut;
    }
    if (d < distMin) {
      return lPolygonOut;
    }
    Geometry geomOut = Double.isNaN(distMax) ? geomJTS : geom.buffer(distMax)
        .intersection(geomJTS);
    geomOut = geomOut.difference(geom.buffer(distMin));
    List<AbstractBuilding> fBP = getFastBuildingPart(distMin, distMax);
    if (fBP != null) {
      return fBP;
    }
    List<IOrientableSurface> lS;
    try {
      lS = FromGeomToSurface.convertGeom(JtsGeOxygene
          .makeGeOxygeneGeom(geomOut));
      for (IOrientableSurface oS : lS) {
        if (oS == null || oS.isEmpty()) {
          continue;
        }
        lDMin.add(distMin);
        lDMax.add(distMax);
        lPolygonOut.add(new FastBuildingPart((IPolygon) oS, this.height
            + this.getZmin()));
        lFP.add(fBP);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    lFP.add(lPolygonOut);
    return lPolygonOut;
  }

  private List<Double> lDMin = new ArrayList<>();
  private List<Double> lDMax = new ArrayList<>();
  private List<List<AbstractBuilding>> lFP = new ArrayList<>();

  private List<AbstractBuilding> getFastBuildingPart(double dMin, double dMax) {
    int nbLDmin = lDMin.size();
    for (int i = 0; i < nbLDmin; i++) {
      if (lDMin.get(i) == dMin && lDMax.get(i) == dMax) {
        return lFP.get(i);
      }
    }
    return null;
  }

  public List<AbstractBuilding> bandEpsilon(CadastralParcel cP, double distMin,
      double distMax) {
    try {
      return bandEpsilon(JtsGeOxygene.makeJtsGeom(cP.getConsLine()), distMin,
          distMax);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  public List<AbstractBuilding> bandEpsilon(IGeometry geom, double distMin,
      double distMax) {
    try {
      return bandEpsilon(JtsGeOxygene.makeJtsGeom(geom), distMin, distMax);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  public double height() {
    return height;
  }

  public boolean prospect(IGeometry geom, double slope, double hIni) {
    double h = -1;
    double distance = this.getFootprint().distance(geom);

    h = ((Cuboid) this).height;

    return distance * slope + hIni > h;
  }

  @Override
  public double[] toArray() {
    return new double[] { this.centerx, this.centery, this.length, this.width,
        this.height, this.orientation };
  }

  @Override
  public void set(List<Double> list) {
    this.centerx = list.get(0);
    this.centery = list.get(1);
    this.length = list.get(2);
    this.width = list.get(3);
    this.height = list.get(4);
    this.orientation = list.get(5);
    this.isNew = true;
  }
}
