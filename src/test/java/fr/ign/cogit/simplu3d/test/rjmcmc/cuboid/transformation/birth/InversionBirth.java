package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.birth;

import fr.ign.cogit.simplu3d.distribution.EquiSurfaceDistribution;
import fr.ign.cogit.simplu3d.io.feature.FeatureType;
import fr.ign.cogit.simplu3d.model.AbstractUrbanFeature;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.io.ShapefileReader;
import fr.ign.common.io.ShapefileWriter;
import org.geotools.feature.DefaultFeatureCollection;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.triangulate.polygon.ConstrainedDelaunayTriangulator;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 *        This software is released under the licence CeCILL
 * <p>
 *        see LICENSE.TXT
 * <p>
 *        see <a href="http://www.cecill.info/">...</a>
 * <p>
 *
 *
 * copyright IGN
 *
 * @author Brasebin Mickaël
 *
 * @version 1.0
 **/
public class InversionBirth {
	/**
	 * Class to test if the birth operation is reversible
	 * @param args ont used
	 * @throws Exception exception in sampler instanciation
	 */
  public static void main(String[] args) throws Exception {

    int nbSample = 10000;

    URL url = InversionBirth.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/data/parcelle.shp");

    String fileOut = "out.shp";

    DefaultFeatureCollection feat = ShapefileReader.read(url);

    if (feat.isEmpty()) return;
    List<Polygon> lOS = FromGeomToSurface.convertGeom((Geometry) feat.iterator().next().getDefaultGeometry());
    Polygon pol = lOS.getFirst();
    ConstrainedDelaunayTriangulator triangulation = new ConstrainedDelaunayTriangulator(pol);
    Geometry result = triangulation.getResult();

    List<Polygon> triangles = new ArrayList<>();
    for (int i = 0; i < result.getNumGeometries(); i++) {
      if (pol.contains(result.getGeometryN(i)))
        triangles.add((Polygon) result.getGeometryN(i));
    }
    MultiPolygon iMS = pol.getFactory().createMultiPolygon(triangles.toArray(new Polygon[0]));
    EquiSurfaceDistribution eq = new EquiSurfaceDistribution(iMS);
    Collection<AbstractUrbanFeature> featCollOut = new ArrayList<>();
    for (int i = 0; i < nbSample; i++) {
      double rand1 = Math.random();
      double rand2 = Math.random();
      Coordinate dpCal = eq.sample(rand1, rand2);
      AbstractUrbanFeature feature = new AbstractUrbanFeature();
      feature.setGeometry(pol.getFactory().createPoint(dpCal));
      featCollOut.add(feature);
      Coordinate dpInv = eq.inversample(dpCal.getX(), dpCal.getY());
      if((Math.abs( dpInv.getX() - eq.getCorrectedRand()) > 0.0001) || (0.0001 < Math.abs(dpInv.getY() - rand2))){
        System.out.println("Error X : " + dpInv.getX() + "        " + eq.getCorrectedRand());
        System.out.println("Error Y : " + dpInv.getY() + "        " + rand2);
      }
    }
    System.out.println(featCollOut.size());
    FeatureType ft = new FeatureType("Birth");
    ft.setGeometryType("Point");
    ShapefileWriter.write(featCollOut, ft, feat.getSchema().getCoordinateReferenceSystem(), fileOut);
  }
}
