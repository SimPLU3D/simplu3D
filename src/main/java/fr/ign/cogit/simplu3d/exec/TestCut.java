package fr.ign.cogit.simplu3d.exec;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.geoxygene.sig3d.equation.PlanEquation;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Triangle;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.sig3d.COGITLauncher3D;
import fr.ign.cogit.sig3d.calculation.CutBuilding;
import fr.ign.cogit.sig3d.calculation.OrientedBoundingBox;

public class TestCut {

  /**
   * @param args
   */
  public static void main(String[] args) {
    String folder = "E:/mbrasebin/Donnees/Strasbourg/TestCut/";
    String fileCut = folder + "testcut2.shp";
    String fileToCut = folder + "bati.shp";

    IFeatureCollection<IFeature> featCollCut = ShapefileReader.read(fileCut);
    IFeatureCollection<IFeature> featCollBat = ShapefileReader.read(fileToCut);

    IFeature featToCut = featCollCut.get(0);

    IPolygon polyt = (IPolygon) FromGeomToSurface.convertGeom(
        featToCut.getGeom()).get(0);

    IEnvelope e = featToCut.getGeom().getEnvelope();
    
    List<IGeometry> lGeom = new ArrayList<>();

    for (IFeature feat : featCollBat) {

      OrientedBoundingBox oBB = new OrientedBoundingBox(feat.getGeom());

      if (!oBB.getPoly().intersects(e.getGeom())) {
        continue;
      }

      List<ITriangle> lT = FromPolygonToTriangle
          .convertAndTriangle(FromGeomToSurface.convertGeom(feat.getGeom()));

      List<ITriangle> verticalT = new ArrayList<>();
      List<ITriangle> horizontalT = new ArrayList<>();

      for (ITriangle t : lT) {

        if (isVertical(t)) {
          verticalT.add(t);
        } else {
          horizontalT.add(t);
        }

      }



      for (ITriangle t : horizontalT) {
        IGeometry geomTemp = handleHorizontal(t, polyt);

        if (geomTemp != null) {
          lGeom.add(geomTemp);
        }

      }

      List<ILineString> lSExtCut = new ArrayList<>();

      int nbSom = polyt.getExterior().coord().size();

      for (int i = 0; i < nbSom-1; i++) {

        IDirectPositionList dpl = new DirectPositionList();
        dpl.add(polyt.getExterior().coord().get(i));
        dpl.add(polyt.getExterior().coord().get(i + 1));

        lSExtCut.add(new GM_LineString(dpl));

      }

      for (ITriangle t : verticalT) {

        double distance1 = t.getCorners(0).getDirect()
            .distance2D(t.getCorners(1).getDirect());
        
        double distance2 = t.getCorners(2).getDirect()
            .distance2D(t.getCorners(1).getDirect());
        
        double distance3 = t.getCorners(2).getDirect()
            .distance2D(t.getCorners(0).getDirect());

        ILineString ls = null;

        if (distance1 > distance2 && distance1 > distance3) {

          IDirectPositionList dpl = new DirectPositionList();
          dpl.add(t.getCorners(0).getDirect());
          dpl.add(t.getCorners(1).getDirect());
          ls = new GM_LineString(dpl);
        }

        if (distance2 >= distance1 && distance2 > distance3) {
          IDirectPositionList dpl = new DirectPositionList();
          dpl.add(t.getCorners(2).getDirect());
          dpl.add(t.getCorners(1).getDirect());
          ls = new GM_LineString(dpl);
        }

        if (distance3 >= distance1 && distance3 >= distance2) {
          IDirectPositionList dpl = new DirectPositionList();
          dpl.add(t.getCorners(2).getDirect());
          dpl.add(t.getCorners(0).getDirect());
          ls = new GM_LineString(dpl);

        }
        
        if(ls == null){
          System.out.println("ls null");
        }
        
     
        
        if(polyt.contains(ls)){
          lGeom.add(t);
          continue;
        }

        // on a un segment 2D représentatif
        
        

        for (ILineString lsTemp : lSExtCut) {
          if (!lsTemp.intersects(ls)) {
            continue;
          }

          IDirectPosition dp1 = lsTemp.coord().get(0);
          dp1.setZ(0);

          IDirectPosition dp2 = lsTemp.coord().get(1);
          dp2.setZ(0);

          IDirectPosition dp3 = (IDirectPosition) dp1.clone();
          dp3.setZ(1);

          PlanEquation ep = new PlanEquation(dp1, dp2, dp3);

          List<IGeometry> lOS = CutBuilding.cut(t, ep);

          for (IGeometry os : lOS) {
            if (os != null && !os.isEmpty()) {
              
              IDirectPositionList dpl = os.coord();
              
              dpl.add(t.getCorners(0).getDirect());
              dpl.add(t.getCorners(1).getDirect());
              dpl.add(t.getCorners(2).getDirect());
              
              
              
              
              
              
              int nbPos= dpl.size();
              
              for(int i=0;i<nbPos;i++)
              {
                IPoint p = new GM_Point(dpl.get(i));
                
                if(! polyt.buffer(0.2).contains(p)){
                  
                  dpl.remove(i);
                  i--;
                  nbPos--;
                }
                
                
              }
              
              nbPos= dpl.size();
              
              if(nbPos < 2 || nbPos > 4){
                System.out.println("What");
                continue;
              }
              
              
              if(nbPos == 3){
                
                lGeom.add(new GM_Triangle(dpl.get(0), dpl.get(1), dpl.get(2)));
                continue;
              }
              
              
              if(nbPos ==4){
                
                Vecteur v1 = new Vecteur(dpl.get(0), dpl.get(1));
                v1.normalise();
                Vecteur v2 = new Vecteur(dpl.get(1), dpl.get(2));
                v2.normalise();
                
           //     if(v1.prodVectoriel(v2).norme() < 0.1){
                  
                  lGeom.add(new GM_Triangle(dpl.get(3), dpl.get(1), dpl.get(2)));
                  lGeom.add(new GM_Triangle(dpl.get(3), dpl.get(0), dpl.get(2)));
                  
             //   }else{
                  lGeom.add(new GM_Triangle(dpl.get(0), dpl.get(1), dpl.get(2)));
                  lGeom.add(new GM_Triangle(dpl.get(3), dpl.get(2), dpl.get(0)));
               // }
                
                
                continue;
                
              }
              
              
              
              
              //lGeom.add(os);
              
              
              
              
            }
          }

        }

      }

    }
    
    System.out.println(lGeom.size());
    
    
    for(IGeometry geom: lGeom) {
      System.out.println(geom);
    }
    
    COGITLauncher3D mW = new COGITLauncher3D();
    
    
    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
    
    for(IGeometry geom: lGeom){
      featC.add(new DefaultFeature(geom));
    }
    
    
    VectorLayer vL = new VectorLayer(featC,"Intersection", Color.blue);
    
    
    mW.getInterfaceMap3D().getCurrent3DMap().addLayer(vL);
    
    
    

  }

  private static IGeometry handleHorizontal(ITriangle t, IPolygon poly) {

    IGeometry geom = t.intersection(poly);

    if (geom.isEmpty()) {
      return null;
    }

    PlanEquation eQ = new PlanEquation(t);

    for (IDirectPosition dp : geom.coord()) {

      dp.setZ(eQ.getZ(dp));

    }

    return geom;

  }

  private static boolean isVertical(ITriangle t) {

    PlanEquation pE = new PlanEquation(t);

    Vecteur v = pE.getNormale().getNormalised();

    return Math.abs(v.getZ()) < 0.1;

  }
}
