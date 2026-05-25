package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.equation.ApproximatedPlanEquation;
import fr.ign.common.io.ShapefileReader;
import fr.ign.common.util.OrientedBoundingBox;
import fr.ign.common.util.Vecteur;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.feature.DefaultFeatureCollection;
import org.locationtech.jts.geom.*;

/**
 *
 *        This software is released under the licence CeCILL
 *        see LICENSE.TXT
 *        see <a href="http://www.cecill.info/">...</a>
 * copyright IGN
 *
 * @author Brasebin Mickaël
 *
 * @version 1.0
 **/
public class LoaderCuboid {

  public static List<Cuboid> loadFromShapeFile(String shapeFile) throws MalformedURLException {
    return loadFromCollection(ShapefileReader.read(new File(shapeFile).toURI().toURL()));
  }

  public static List<Cuboid> loadFromCollection(DefaultFeatureCollection featColl) {
    List<Cuboid> lCub = new ArrayList<>();
    for (SimpleFeature feat : featColl) {
      lCub.add(transformFeature(feat));
    }
    return lCub;
  }
  public static Cuboid transformFeature(SimpleFeature feat) {
    MultiPolygon iMS = FromGeomToSurface.convertMSGeom((Geometry) feat.getDefaultGeometry());
    OrientedBoundingBox oBB = new OrientedBoundingBox(iMS);
    Polygon poly = oBB.getPoly();
    ApproximatedPlanEquation ap = new ApproximatedPlanEquation(poly.getCoordinates());
    if (ap.getNormale().getZ() < 0) {
      poly = poly.reverse();
    }
    CoordinateList dpl = new CoordinateList(poly.getCoordinates());
    double l = dpl.get(0).distance(dpl.get(1));
    double w = dpl.get(2).distance(dpl.get(1));
    double centreX = oBB.getCentre().getX();
    double centreY = oBB.getCentre().getY();
    Vecteur v = new Vecteur(
        (dpl.get(1).getX() + dpl.get(2).getX()) / 2 - centreX,
        (dpl.get(1).getY() + dpl.get(2).getY()) / 2 - centreY);
    return new Cuboid(centreX, centreY, l, w, oBB.getMaxZ() - oBB.getMinZ(), v.getDirection());
  }
}
