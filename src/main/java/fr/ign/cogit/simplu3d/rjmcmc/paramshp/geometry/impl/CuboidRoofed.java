package fr.ign.cogit.simplu3d.rjmcmc.paramshp.geometry.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;

public class CuboidRoofed extends Cuboid {

  private double heightT;
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
    double v = super.getVolume();
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
    if (!(o instanceof CuboidRoofed)) {
      return false;
    }
    CuboidRoofed r = (CuboidRoofed) o;
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

  public IGeometry generated3DGeom() {
    List<IDirectPositionList> lDpl = super.createCubePointsList();
    addRoofCoords(lDpl);
    List<IOrientableSurface> lOS = new ArrayList<>();
    for (IDirectPositionList dpl : lDpl) {
      lOS.add(new GM_Polygon(new GM_LineString(dpl)));
    }
    return new GM_MultiSurface<>(lOS);
    // return generate(this, this.getZmin());
  }

  private void addRoofCoords(List<IDirectPositionList> lDpl) {
    double zmin = getZmin();
    // pointsCube = new ArrayList<IDirectPositionList>();
    IDirectPositionList dpl = this.getFootprint().coord();
    IDirectPosition p1 = dpl.get(0);
    p1.setZ(zmin + height);
    IDirectPosition p2 = dpl.get(1);
    p2.setZ(zmin + height);
    IDirectPosition p3 = dpl.get(2);
    p3.setZ(zmin + height);
    IDirectPosition p4 = dpl.get(3);
    p4.setZ(zmin + height);

    IDirectPosition e1 = new DirectPosition(emp1.x, emp1.y, this.height());
    IDirectPosition e2 = new DirectPosition(emp2.x, emp2.y, this.height());
    IDirectPositionList dpl7 = new DirectPositionList();
    dpl7.add(p1);
    dpl7.add(e1);
    dpl7.add(p4);
    dpl7.add(p1);
    lDpl.add(dpl7);

    IDirectPositionList dpl8 = new DirectPositionList();
    dpl8.add(p2);
    dpl8.add(e2);
    dpl8.add(p3);
    dpl8.add(p2);
    lDpl.add(dpl8);

    IDirectPositionList dpl9 = new DirectPositionList();
    dpl9.add(p1);
    dpl9.add(e1);
    dpl9.add(e2);
    dpl9.add(p2);
    dpl9.add(p1);
    lDpl.add(dpl9);

    IDirectPositionList dpl10 = new DirectPositionList();
    dpl10.add(p3);
    dpl10.add(e2);
    dpl10.add(e1);
    dpl10.add(p4);
    dpl10.add(p3);
    lDpl.add(dpl10);
  }

  // public static IMultiSurface<IOrientableSurface> generate(CuboidRoofed c,
  // double zMin) {
  //
  // IDirectPositionList dpl = c.getFootprint().coord();
  //
  // IDirectPosition dp1 = dpl.get(0);
  // dp1.setZ(zMin + c.height);
  // IDirectPosition dp2 = dpl.get(1);
  // dp2.setZ(zMin + c.height);
  // IDirectPosition dp3 = dpl.get(2);
  // dp3.setZ(zMin + c.height);
  // IDirectPosition dp4 = dpl.get(3);
  // dp4.setZ(zMin + c.height);
  // IDirectPosition e1 = new DirectPosition(c.emp1.x, c.emp1.y, c.height());
  // IDirectPosition e2 = new DirectPosition(c.emp2.x, c.emp2.y, c.height());
  // return createCube(dp1, dp2, dp3, dp4, zMin, e1, e2);
  // }
  //
  // private static IMultiSurface<IOrientableSurface> createCube(
  // IDirectPosition p1, IDirectPosition p2, IDirectPosition p3,
  // IDirectPosition p4, double zmin, IDirectPosition e1, IDirectPosition e2) {
  //
  // // Polygone p1,p2,p3,p4 représente la face supérieure dans cet ordre
  //
  // List<IDirectPositionList> lDpl = new ArrayList<IDirectPositionList>();
  //
  // IDirectPositionList dpl1 = new DirectPositionList();
  // dpl1.add(p1);
  // dpl1.add(p2);
  // dpl1.add(p3);
  // dpl1.add(p4);
  // dpl1.add(p1);
  // lDpl.add(dpl1);
  //
  // IDirectPosition p1bas = new DirectPosition(p1.getX(), p1.getY(), zmin);
  // IDirectPosition p2bas = new DirectPosition(p2.getX(), p2.getY(), zmin);
  // IDirectPosition p3bas = new DirectPosition(p3.getX(), p3.getY(), zmin);
  // IDirectPosition p4bas = new DirectPosition(p4.getX(), p4.getY(), zmin);
  //
  // IDirectPositionList dpl2 = new DirectPositionList();
  // dpl2.add(p2);
  // dpl2.add(p1);
  // dpl2.add(p1bas);
  // dpl2.add(p2bas);
  // dpl2.add(p2);
  // lDpl.add(dpl2);
  //
  // IDirectPositionList dpl3 = new DirectPositionList();
  // dpl3.add(p3);
  // dpl3.add(p2);
  // dpl3.add(p2bas);
  // dpl3.add(p3bas);
  // dpl3.add(p3);
  // lDpl.add(dpl3);
  //
  // IDirectPositionList dpl4 = new DirectPositionList();
  // dpl4.add(p4);
  // dpl4.add(p3);
  // dpl4.add(p3bas);
  // dpl4.add(p4bas);
  // dpl4.add(p4);
  // lDpl.add(dpl4);
  //
  // IDirectPositionList dpl5 = new DirectPositionList();
  // dpl5.add(p1);
  // dpl5.add(p4);
  // dpl5.add(p4bas);
  // dpl5.add(p1bas);
  // dpl5.add(p1);
  // lDpl.add(dpl5);
  //
  // IDirectPositionList dpl6 = new DirectPositionList();
  // dpl6.add(p1bas);
  // dpl6.add(p4bas);
  // dpl6.add(p3bas);
  // dpl6.add(p2bas);
  // dpl6.add(p1bas);
  // lDpl.add(dpl6);
  //
  // IDirectPositionList dpl7 = new DirectPositionList();
  // dpl7.add(p1);
  // dpl7.add(e1);
  // dpl7.add(p4);
  // dpl7.add(p1);
  // lDpl.add(dpl7);
  //
  // IDirectPositionList dpl8 = new DirectPositionList();
  // dpl8.add(p2);
  // dpl8.add(e2);
  // dpl8.add(p3);
  // dpl8.add(p2);
  // lDpl.add(dpl8);
  //
  // IDirectPositionList dpl9 = new DirectPositionList();
  // dpl9.add(p1);
  // dpl9.add(e1);
  // dpl9.add(e2);
  // dpl9.add(p2);
  // dpl9.add(p1);
  // lDpl.add(dpl9);
  //
  // IDirectPositionList dpl10 = new DirectPositionList();
  // dpl10.add(p3);
  // dpl10.add(e2);
  // dpl10.add(e1);
  // dpl10.add(p4);
  // dpl10.add(p3);
  // lDpl.add(dpl10);
  //
  // List<IOrientableSurface> lOS = new ArrayList<>();
  // for (IDirectPositionList dpl : lDpl) {
  //
  // lOS.add(new GM_Polygon(new GM_LineString(dpl)));
  //
  // }
  //
  // return new GM_MultiSurface<>(lOS);
  //
  // }

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
      Coordinate v1 = new Coordinate(factor * (p1.x - p0.x),
          factor * (p1.y - p0.y));
      Coordinate v2 = new Coordinate(0.5 * (p2.x - p1.x), 0.5 * (p2.y - p1.y));
      emp1 = new Coordinate(p0.x + v2.x + v1.x, p0.y + v2.y + v1.y);
      emp2 = new Coordinate(p1.x + v2.x - v1.x, p1.y + v2.y - v1.y);
    }
    return geomJTS;
  }

  public static void main(String[] args) {
    MainWindow mW = new MainWindow();
    CuboidRoofed sb = new CuboidRoofed(0, 0, 20, 10, 20, 0.88, 3, 2);
    // System.out.println(sb.toGeometry());
    IFeature feat = new DefaultFeature(sb.generated3DGeom());
    IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<>();
    featColl.add(feat);
    mW.getInterfaceMap3D().getCurrent3DMap()
        .addLayer(new VectorLayer(featColl, "Sausage", Color.red));
    System.out.println(sb);
  }

}
