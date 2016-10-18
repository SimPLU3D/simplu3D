package fr.ign.cogit.simplu3d.experiments.openmole.diversity;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.algo.JtsAlgorithms;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.util.convert.ExportAsFeatureCollection;
import fr.ign.mpp.configuration.GraphConfiguration;

public class ParcelCoverageRatio {
  IFeatureCollection<IFeature> featColl;
  double area;

  public ParcelCoverageRatio(GraphConfiguration<Cuboid> c, BasicPropertyUnit bPU) {
    ExportAsFeatureCollection exporter = new ExportAsFeatureCollection(c);
    IFeatureCollection<IFeature> collection = exporter.getFeatureCollection();
    this.featColl = collection;
    this.area = bPU.getGeom().area();
  }
  
  
  public ParcelCoverageRatio(IFeatureCollection<IFeature> featColl, double area) {
    this.featColl = featColl;
    this.area = area;
  }

  @SuppressWarnings("unchecked")
  public double getCoverageRatio() {


    List<GM_MultiSurface<IPolygon>> list = new ArrayList<>();
    for (IFeature f : featColl) {
      list.add((GM_MultiSurface<IPolygon>) f.getGeom());
    }
    // if there is no object, don't bother
    if (list.isEmpty()) {
      return 0.0;
    }
    IGeometry union = JtsAlgorithms.union(list);
    // if the union is empty, stop now
    if (union == null || union.isEmpty()) {
      return 0.0;
    }
    try {
      // force the union to a 2D geometry (the input geometries are 3D)
      Geometry g = AdapterFactory.toGeometry(new GeometryFactory(), union);
      g.apply(new CoordinateFilter() {
        @Override
        public void filter(Coordinate coord) {
          coord.setCoordinate(new Coordinate(coord.x, coord.y));
        }
      });
      g = g.buffer(0);
      double buildingArea = g.getArea();
      return buildingArea / area;
    } catch (Exception e) {
      e.printStackTrace();
      return 0.0;
    }
  }
}
