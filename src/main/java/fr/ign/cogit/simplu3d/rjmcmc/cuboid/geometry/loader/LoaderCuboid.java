package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.contrib.geometrie.Angle;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.calculation.OrientedBoundingBox;
import fr.ign.cogit.geoxygene.sig3d.equation.ApproximatedPlanEquation;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class LoaderCuboid {

  public static List<Cuboid> loadFromShapeFile(String shapeFile) {
    
    return loadFromCollection(ShapefileReader.read(shapeFile));
  }

  public static List<Cuboid> loadFromCollection(
      IFeatureCollection<IFeature> featColl) {
    List<Cuboid> lCub = new ArrayList<>();

    for (IFeature feat : featColl) {
      lCub.add(transformFeature(feat));

    }

    return lCub;

  }

  public static Cuboid transformFeature(IFeature feat) {

    IMultiSurface<IOrientableSurface> iMS = FromGeomToSurface
        .convertMSGeom(feat.getGeom());

    OrientedBoundingBox oBB = new OrientedBoundingBox(iMS);
    
    IPolygon poly = oBB.getPoly();
    ApproximatedPlanEquation ap = new ApproximatedPlanEquation(poly);

    if (ap.getNormale().getZ() < 0) {

      poly = (IPolygon) poly.reverse();

    }

    IDirectPositionList dpl = poly.coord();

    double l = dpl.get(0).distance(dpl.get(1));
    double w = dpl.get(2).distance(dpl.get(1));

    double centreX = oBB.getCentre().getX();
    double centreY = oBB.getCentre().getY();

    Vecteur v = new Vecteur((dpl.get(1).getX() + dpl.get(2).getX()) / 2
        - centreX, (dpl.get(1).getY() + dpl.get(2).getY()) / 2 - centreY);

    Angle a = v.direction();

    return new Cuboid(centreX, centreY, l, w, oBB.getzMax() - oBB.getzMin(),
        a.getValeur());

  }

}
