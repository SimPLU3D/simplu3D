package fr.ign.cogit.simplu3d.experiments.thesis.predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.util.CuboidGroupCreation;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;
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
public class UB14PredicateFull<O extends Cuboid, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>> implements
    ConfigurationModificationPredicate<C,M> {
  IMultiCurve<IOrientableCurve> curveVoirie;
  IMultiCurve<IOrientableCurve> curveLatBot;

  IGeometry bufferRoad;
  IGeometry bufferLimLat;

  IGeometry buffer13, buffer20, buffer20more;

  int numberMaxOfBoxesInGroup;

  private double threshold = 10;

  BasicPropertyUnit bPU;

  // C'est ternaire 0 = non, 1 = OUI, 2 = OSEF
  private int CASE_BOARD_ROAD, CASE_BOARD_LIM;
  
  IGeometry forbiddenGeom = null;
  
  public UB14PredicateFull(BasicPropertyUnit bPU, double threshold,
          int CASE_BOARD_ROAD, int CASE_BOARD_LIM,IGeometry forbiddengeom) {
      this(bPU,threshold,CASE_BOARD_ROAD,CASE_BOARD_LIM);
      this.forbiddenGeom = forbiddengeom;
      
  }


  
  
  public UB14PredicateFull(BasicPropertyUnit bPU, double threshold,
      int CASE_BOARD_ROAD, int CASE_BOARD_LIM) {
    
    
  

    this.CASE_BOARD_LIM = CASE_BOARD_LIM;
    this.CASE_BOARD_ROAD = CASE_BOARD_ROAD;

    this.bPU = bPU;

    this.threshold = threshold;

    List<IOrientableCurve> lCurveVoirie = new ArrayList<>();

    List<IOrientableCurve> lCurveLatBot = new ArrayList<>();

    for (CadastralParcel cP : bPU.getCadastralParcels()) {
      // for (SubParcel sB : cP.getSubParcel()) {

      for (ParcelBoundary sCB : cP.getBoundaries()) {

        if (sCB.getType() == ParcelBoundaryType.ROAD) {

          IGeometry geom = sCB.getGeom();

          if (geom instanceof IOrientableCurve) {

            lCurveVoirie.add((IOrientableCurve) geom);

          } else {
            System.out
                .println("Classe UB14PredicateFull : quelque chose n'est pas un ICurve");
          }

        } else if (sCB.getType() != ParcelBoundaryType.INTRA) {
          IGeometry geom = sCB.getGeom();

          if (geom instanceof IOrientableCurve) {

            lCurveLatBot.add((IOrientableCurve) geom);

          } else {
            System.out
                .println("Classe UB14PredicateFull : quelque chose n'est pas un ICurve");
          }

        }

        // }

      }

    }

    // System.out.println("NB voirie : " + lCurveVoirie.size());

    // System.out.println("NB other : " + lCurveLatBot.size());

    System.out.println(lCurveVoirie.size());

    curveVoirie = new GM_MultiCurve<>(lCurveVoirie);
    bufferRoad = curveVoirie.buffer(0.75);
    curveLatBot = new GM_MultiCurve<>(lCurveLatBot);
    bufferLimLat = curveLatBot.buffer(0.75);

    buffer13 = curveVoirie.buffer(13);
    buffer20 = curveVoirie.buffer(20).difference(buffer13);

    buffer20more = FromGeomToSurface.convertMSGeom(bPU.getGeom().difference(
        curveVoirie.buffer(20)));

  }



  @Override
  public boolean check(C c, M m) {

    List<O> lO = m.getBirth();
    
    for(O ab : lO){
        if(forbiddenGeom != null){
            if(ab.getFootprint().intersects(forbiddenGeom)){
                return false;
            }
        }
    }

    O batDeath = null;

    if (!m.getDeath().isEmpty()) {

      batDeath = m.getDeath().get(0);

    }

    // On ne cherche à implanter qu'un seul bâtiment

    List<O> lBatIni = new ArrayList<>();

    Iterator<O> iTBat = c.iterator();

    while (iTBat.hasNext()) {

      O batTemp = iTBat.next();

      if (batTemp == batDeath) {
        continue;
      }

      lBatIni.add(batTemp);

    }

    for (O ab : lO) {

      lBatIni.add(ab);

    }

    if (lBatIni.size() == 0) {
      return true;
    }
    
   // if(lBatIni.size() > 2){
    //    return false;
    //}
    
    CuboidGroupCreation<AbstractSimpleBuilding> cGR = new CuboidGroupCreation<AbstractSimpleBuilding>();

    List<List<AbstractSimpleBuilding>> groupes = cGR.createGroup(lBatIni, 0.5);

    if (groupes.size() > 1) {
      return false;
    }

    if (!respectBuildArea(lBatIni)) {
      return false;
    }

    // On a qu'un seul groupe

    // Distance de 1,5 m à la rue ou alors longer la rue

    boolean checked = false;

    if (CASE_BOARD_ROAD == 0) {
      // On ne borde pas la route, distance de 1.5m
      checked = this.distanceToRoadRespected(lBatIni, 1.5);
      if (!checked) {
        return false;
      }

    } else if (CASE_BOARD_ROAD == 1) {
      // On borde la route
      checked = this.boardRoad(lBatIni, threshold);

      if (!checked) {

        return false;

      }
    } else if (CASE_BOARD_ROAD == 2) {
      // On s'en fiche, on teste si on borde puis on teste si on recule
      checked = this.distanceToRoadRespected(lBatIni, 1.5);
      if (!checked) {
        checked = this.boardRoad(lBatIni, threshold);
        if (!checked) {

          return false;

        }
      }

    }
    
    enoughLength = false;

    // System.out.println("Je passe là");

    // On s'implante en mode prospect
    if (CASE_BOARD_LIM == 0) {

      boolean co = checkProspectForBuilding(lBatIni);

      return co;

    } else if (CASE_BOARD_LIM == 1) {

      // On s'implante contre les limites

      List<O> lOut = boardLimLAt(lBatIni, threshold);


      if (lOut.isEmpty()) {
        return false;
      }
      
      if(! enoughLength){
        return false;
      }

      boolean checkHeight = checkHeight(lOut, threshold);

      return checkHeight;

    }

    // On s'implante en prospect si c'est bon sinon on regarde si on s'implante
    // en limite
    boolean co = checkProspectForBuilding(lBatIni);
    if (co) {
      return true;
    }
    List<O> lOut = boardLimLAt(lBatIni, threshold);



    if (lOut.isEmpty()) {
      return false;
    }
    
    if(! enoughLength){
      return false;
    }


    boolean checkHeight = checkHeight(lOut, threshold);

    return checkHeight;

  }

  private boolean checkProspectForBuilding(List<O> lO) {

    for (O ab : lO) {

      boolean checked = ab.prospect(bufferLimLat, 0.5, 0);

      if (!checked) {
        return false;
      }

      if (ab.getFootprint().intersects(buffer13)) {

        IGeometry decoup = ab.getFootprint().intersection(buffer13);

        double dist = decoup.distance(curveLatBot);

        if (dist < 1.9) {
          return false;
        }

      }

      if (ab.getFootprint().intersects(buffer20)) {

        IGeometry decoup = ab.getFootprint().intersection(buffer20);

        double dist = decoup.distance(curveLatBot);

        if (dist < 3) {
          return false;
        }

      }

      if (ab.getFootprint().intersects(buffer20more)) {

        IGeometry decoup = ab.getFootprint().intersection(buffer20more);

        double dist = decoup.distance(curveLatBot);

        if (dist < 6) {
          return false;
        }

      }
    }

    return true;

  }

  private boolean checkHeight(List<O> lO, double threshold) {

    bouclebat: for (O ab : lO) {

     // List<ILineString> ls = createLineStringsFromPol(ab.getFootprint());

      if (ab.height() <= 3.5) {
        continue bouclebat;
      }

      for (IDirectPosition dp : ab.getFootprint().coord()) {

        if (curveVoirie.distance(new GM_Point(dp)) < 13) {
          continue;
        }

        return false;

      }

    }

    return true;

  }

  private boolean distanceToRoadRespected(List<O> lO, double dist) {

    for (O ab : lO) {

      if (ab.getFootprint().distance(this.curveVoirie) < 1.5) {
        return false;
      }

    }

    return true;

  }
  
  
  private boolean enoughLength = false;

  private List<O> boardLimLAt(List<O> lO, double threshold) {

    List<O> lOut = new ArrayList<>();

    bouclebat: for (O ab : lO) {

      List<ILineString> ls = createLineStringsFromPol(ab.getFootprint());

      for (ILineString l : ls) {
        
        
        
        if(bufferLimLat.intersects(l)){
      //  if (this.bufferLimLat.contains(new GM_Point(l.coord().get(0))) && this.bufferLimLat.contains(new GM_Point(l.coord().get(1)))) {
          
          if(! lOut.contains(ab)){
            lOut.add(ab);
          }
          
          
          if(!enoughLength && l.intersection(bufferLimLat).length() > threshold){
            
         //   System.out.println(l.length());
            
            
            
            enoughLength = true;
          }
          
 
          continue bouclebat;
        }

      }

    }

    return lOut;

  }

  private boolean boardRoad(List<O> lO, double threshold) {

    for (O ab : lO) {

      List<ILineString> ls = createLineStringsFromPol(ab.getFootprint());

      for (ILineString l : ls) {

        if (l.length() > threshold && this.bufferRoad.contains(new GM_Point(l.coord().get(0))) && this.bufferRoad.contains(new GM_Point(l.coord().get(1)))) {
          return true;
        }

      }

    }

    return false;

  }

  private List<ILineString> createLineStringsFromPol(IGeometry geom) {
    List<ILineString> ls = new ArrayList<>();

    IDirectPositionList dpl1 = new DirectPositionList();
    dpl1.add(geom.coord().get(0));
    dpl1.add(geom.coord().get(1));

    IDirectPositionList dpl2 = new DirectPositionList();
    dpl2.add(geom.coord().get(1));
    dpl2.add(geom.coord().get(2));

    IDirectPositionList dpl3 = new DirectPositionList();
    dpl3.add(geom.coord().get(2));
    dpl3.add(geom.coord().get(3));

    IDirectPositionList dpl4 = new DirectPositionList();
    dpl4.add(geom.coord().get(3));
    dpl4.add(geom.coord().get(4));

    ls.add(new GM_LineString(dpl1));
    ls.add(new GM_LineString(dpl2));
    ls.add(new GM_LineString(dpl3));
    ls.add(new GM_LineString(dpl4));

    return ls;
  }

  protected double compareGroup(List<O> l1, List<O> l2) {

    double min = Double.POSITIVE_INFINITY;

    for (O o1 : l1) {
      for (O o2 : l2) {

        min = Math.min(o1.getFootprint().distance(o2.getFootprint()), min);

      }
    }
    // System.out.println(min);
    return min;

  }

  private boolean respectBuildArea(List<O> lBatIni) {

    if (lBatIni.isEmpty()) {
      return true;
    }

    int nbElem = lBatIni.size();

    IGeometry geom = lBatIni.get(0).getFootprint();

    for (int i = 1; i < nbElem; i++) {

      geom = geom.union(lBatIni.get(i).getFootprint());

    }

    double airePAr = this.bPU.getCadastralParcels().get(0).getArea();

    return ((geom.area() / airePAr) <= 0.8);
  }

}