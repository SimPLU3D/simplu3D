package fr.ign.cogit.simplu3d.exec;

import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.delaunay.TriangulationJTS;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.distribution.EquiSurfaceDistribution;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.sig3d.topology.TriangulationLoader;

public class DistributionOnSurface {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    int nbSample = 10000;

    String folder = "E:/mbrasebin/Donnees/Strasbourg/DistribTest/";

    String file = folder + "parcelle.shp";
    String fileOut = folder + "out.shp";

    IFeatureCollection<IFeature> feat = ShapefileReader.read(file);

    List<IOrientableSurface> lOS = FromGeomToSurface.convertGeom(feat.get(1)
        .getGeom());

    TriangulationJTS triangulation = TriangulationLoader
        .generate((IPolygon) lOS.get(0));

    triangulation.triangule();

    IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
    
    

    for (Face f : triangulation.getPopFaces()) {

      if (lOS.get(0).contains(f.getGeom())) {
        iMS.add(f.getGeometrie());
        
        break;
      }

    }

    EquiSurfaceDistribution eq = new EquiSurfaceDistribution(iMS);

    IFeatureCollection<IFeature> featCollOut = new FT_FeatureCollection<>();

    for (int i = 0; i < nbSample; i++) {

      featCollOut.add(new DefaultFeature(new GM_Point(eq.sample())));

    }

    System.out.println(featCollOut.size());

    ShapefileWriter.write(featCollOut, fileOut);

  }

}
