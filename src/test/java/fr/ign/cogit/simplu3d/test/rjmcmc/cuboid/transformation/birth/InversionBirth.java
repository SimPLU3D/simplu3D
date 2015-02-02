package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.birth;

import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.delaunay.TriangulationJTS;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.distribution.EquiSurfaceDistribution;
import fr.ign.cogit.geoxygene.sig3d.topology.TriangulationLoader;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;

public class InversionBirth {

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

     //   break;
      }

    }

    EquiSurfaceDistribution eq = new EquiSurfaceDistribution(iMS);

    IFeatureCollection<IFeature> featCollOut = new FT_FeatureCollection<>();

    for (int i = 0; i < nbSample; i++) {

      double rand1 = Math.random();
      double rand2 = Math.random();

      IDirectPosition dpCal = eq.sample(rand1, rand2);

      featCollOut.add(new DefaultFeature(new GM_Point(dpCal)));
      
      
      IDirectPosition dpInv = eq.inversample(dpCal.getX(), dpCal.getY());
      
      if((Math.abs( dpInv.getX() - eq.getCorrectedRand()) > 0.0001) || (0.0001 < Math.abs(dpInv.getY() - rand2))){
          

        
        System.out.println("Error X : " + dpInv.getX() + "        " + eq.getCorrectedRand());
        System.out.println("Error Y : " + dpInv.getY() + "        " + rand2);
      }
      
      

    }

    System.out.println(featCollOut.size());

    ShapefileWriter.write(featCollOut, fileOut);

  }

}
