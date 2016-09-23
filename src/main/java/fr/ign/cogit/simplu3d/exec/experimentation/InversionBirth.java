package fr.ign.cogit.simplu3d.exec.experimentation;


import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.delaunay.TriangulationJTS;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.distribution.EquiSurfaceDistribution;
import fr.ign.cogit.geoxygene.sig3d.distribution.EquiSurfaceDistributionJTS;
import fr.ign.cogit.geoxygene.sig3d.topology.TriangulationLoader;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
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
public class InversionBirth {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    int nbSample = 100000;

    String folder = "E:/mbrasebin/Donnees/Strasbourg/DistribTest/";

    String file = folder + "parcelle.shp";
    String fileOut = folder + "out.shp";

    IFeatureCollection<IFeature> feat = ShapefileReader.read(file);

    if (feat == null || feat.isEmpty()) return;
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
    IFeatureCollection<IFeature> featCollOut = new FT_FeatureCollection<>();
    
    
    long t = System.currentTimeMillis();
    
    ////Eq1

    EquiSurfaceDistribution eq = new EquiSurfaceDistribution(iMS);



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
    
    
    System.out.println("Méthode 1 : " + nbSample + " sampling et inverse en " + (System.currentTimeMillis() - t));
    

    ////Eq1
    
    t = System.currentTimeMillis();

    EquiSurfaceDistributionJTS eq2 = new EquiSurfaceDistributionJTS(iMS);



    for (int i = 0; i < nbSample; i++) {

      double rand1 = Math.random();
      double rand2 = Math.random();

      IDirectPosition dpCal = eq2.sample(rand1, rand2);

      featCollOut.add(new DefaultFeature(new GM_Point(dpCal)));
      
      
      IDirectPosition dpInv = eq2.inversample(dpCal.getX(), dpCal.getY());
      
      if((Math.abs( dpInv.getX() - eq2.getCorrectedRand()) > 0.0001) || (0.0001 < Math.abs(dpInv.getY() - rand2))){
          

        
        System.out.println("Error X : " + dpInv.getX() + "        " + eq2.getCorrectedRand());
        System.out.println("Error Y : " + dpInv.getY() + "        " + rand2);
      }
      
      

    }
    
    
    System.out.println("Méthode 2  : " + nbSample + " sampling et inverse en " + (System.currentTimeMillis() - t));
    
    
    
    
    
    
    

    System.out.println(featCollOut.size());

    ShapefileWriter.write(featCollOut, fileOut);

  }

}
