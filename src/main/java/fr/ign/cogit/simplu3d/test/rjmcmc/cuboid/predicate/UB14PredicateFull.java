package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;
import fr.ign.rjmcmc.configuration.Modification;

public class UB14PredicateFull<O extends Cuboid2> implements
    ConfigurationModificationPredicate<O> {

  IMultiCurve<IOrientableCurve> curveVoirie;
  IMultiCurve<IOrientableCurve> curveLatBot;

  IGeometry bufferRoad;
  IGeometry bufferLimLat;

  IGeometry buffer13, buffer20, buffer20more;

  int numberMaxOfBoxesInGroup;

  private double threshold = 10;

  // C'est ternaire 0 = non, 1 = OUI, 2 = OSEF
  private int CASE_BOARD_ROAD, CASE_BOARD_LIM;

  public UB14PredicateFull(BasicPropertyUnit bPU, int numberMaxOfBoxesInGroup,
      double threshold, int CASE_BOARD_ROAD, int CASE_BOARD_LIM) {

    this.CASE_BOARD_LIM = CASE_BOARD_LIM;
    this.CASE_BOARD_ROAD = CASE_BOARD_ROAD;

    this.threshold = threshold;
    this.numberMaxOfBoxesInGroup = numberMaxOfBoxesInGroup;

    List<IOrientableCurve> lCurveVoirie = new ArrayList<>();

    List<IOrientableCurve> lCurveLatBot = new ArrayList<>();

    for (CadastralParcel cP : bPU.getCadastralParcel()) {
      // for (SubParcel sB : cP.getSubParcel()) {
      for (SpecificCadastralBoundary sCB : cP.getBoundary()) {

        if (sCB.getType() != SpecificCadastralBoundary.ROAD) {
          IGeometry geom = sCB.getGeom();

          if (geom instanceof IOrientableCurve) {

            lCurveVoirie.add((IOrientableCurve) geom);

          } else {
            System.out
                .println("Classe UB14PredicateFull : quelque chose n'est pas un ICurve");
          }

          // }

        } else if (sCB.getType() != SpecificCadastralBoundary.INTRA) {
          IGeometry geom = sCB.getGeom();

          if (geom instanceof IOrientableCurve) {

            lCurveLatBot.add((IOrientableCurve) geom);

          } else {
            System.out
                .println("Classe UB14PredicateFull : quelque chose n'est pas un ICurve");
          }

        }

      }

    }

    curveVoirie = new GM_MultiCurve<>(lCurveVoirie);
    bufferRoad = curveVoirie.buffer(0.5);
    curveLatBot = new GM_MultiCurve<>(lCurveLatBot);
    bufferLimLat = curveLatBot.buffer(0.5);

    buffer13 = curveVoirie.buffer(13);
    buffer20 = curveVoirie.buffer(20).difference(buffer13);
    buffer20more = bPU.getGeom().difference(buffer20);

  }

  private List<List<O>> createGroupe(List<O> lBatIn) {

    List<List<O>> listGroup = new ArrayList<>();

    while (!lBatIn.isEmpty()) {

      O batIni = lBatIn.remove(0);

      List<O> currentGroup = new ArrayList<>();
      currentGroup.add(batIni);

      int nbElem = lBatIn.size();

      bouclei: for (int i = 0; i < nbElem; i++) {

        for (O batTemp : currentGroup) {

          if (lBatIn.get(i).getFootprint().distance(batTemp.getFootprint()) < 0.5) {

            currentGroup.add(lBatIn.get(i));
            lBatIn.remove(i);
            i = -1;
            nbElem--;
            continue bouclei;

          }
        }

      }

      listGroup.add(currentGroup);
    }

    return listGroup;
  }

  @Override
  public boolean check(Configuration<O> c, Modification<O, Configuration<O>> m) {

    List<O> lO = m.getBirth();

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

    List<List<O>> groupes = createGroupe(lBatIni);

    if (groupes.size() == 0) {
      return true;
    }

    if (groupes.size() > 1) {
      return false;
    }

    // On a qu'un seul groupe

    // Distance de 1,5 m à la rue ou alors longer la rue

    boolean checked = false;

    if (CASE_BOARD_ROAD != 1) {

      checked = this.distanceToRoadRespected(lBatIni, 1.5);

      if (CASE_BOARD_ROAD == 0 && !checked) {
        return false;
      }

    }

    if (!checked && CASE_BOARD_ROAD != 0) {

      boolean checked2 = this.boardRoad(lBatIni, threshold);

      if (!checked2) {

        return false;

      }

    }

    // 2 implantation : soit l'implantation borde les limites séparatives soit
    // ce n'est pas le cas
    boolean bordeLimiteLateral = boardLimLAt(lBatIni, threshold);

    if (!bordeLimiteLateral && CASE_BOARD_LIM == 1) {
      return false;
    }

    if (bordeLimiteLateral && CASE_BOARD_LIM == 0) {

      boolean checkHeight = checkHeight(lBatIni, threshold);

      return checkHeight;
    }

    // Cas de l'implantation avec prospect

    boolean co = checkProspectForBuilding(lBatIni);

    return co;

  }

  private boolean checkProspectForBuilding(List<O> lO) {

    for (O ab : lO) {

      boolean checked = ab.prospect(bufferLimLat, 0.5, 0);

      if (!checked) {
        return false;
      }

      if (ab.getFootprint().intersects(buffer13)) {

        IGeometry decoup = ab.footprint.intersection(buffer13);

        double dist = decoup.distance(curveLatBot);

        if (dist < 1.9) {
          return false;
        }

      }

      if (ab.getFootprint().intersects(buffer20)) {

        IGeometry decoup = ab.footprint.intersection(buffer20);

        double dist = decoup.distance(curveLatBot);

        if (dist < 3) {
          return false;
        }

      }

      if (ab.getFootprint().intersects(buffer20more)) {

        IGeometry decoup = ab.footprint.intersection(buffer20);

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

      List<ILineString> ls = createLineStringsFromPol(ab.getFootprint());

      for (ILineString l : ls) {

        // On se place à une distance de plus de 13 m
        if (l.distance(curveVoirie) < 13) {
          continue;
        }

        if (ab.height() <= 3.5) {
          continue bouclebat;
        }

        if (l.length() > threshold && this.bufferLimLat.contains(l)) {
          return false;
        }

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

  private boolean boardLimLAt(List<O> lO, double threshold) {
    for (O ab : lO) {

      List<ILineString> ls = createLineStringsFromPol(ab.getFootprint());

      for (ILineString l : ls) {

        if (l.length() > threshold && this.bufferLimLat.contains(l)) {
          return true;
        }

      }

    }

    return false;

  }

  private boolean boardRoad(List<O> lO, double threshold) {

    for (O ab : lO) {

      List<ILineString> ls = createLineStringsFromPol(ab.getFootprint());

      for (ILineString l : ls) {

        if (l.length() > threshold && this.bufferRoad.contains(l)) {
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
    dpl1.add(geom.coord().get(1));
    dpl1.add(geom.coord().get(2));

    IDirectPositionList dpl3 = new DirectPositionList();
    dpl1.add(geom.coord().get(2));
    dpl1.add(geom.coord().get(3));

    IDirectPositionList dpl4 = new DirectPositionList();
    dpl1.add(geom.coord().get(3));
    dpl1.add(geom.coord().get(4));

    ls.add(new GM_LineString(dpl1));
    ls.add(new GM_LineString(dpl2));
    ls.add(new GM_LineString(dpl3));
    ls.add(new GM_LineString(dpl4));

    return ls;
  }

  private double compareGroup(List<O> l1, List<O> l2) {

    double min = Double.POSITIVE_INFINITY;

    for (O o1 : l1) {
      for (O o2 : l2) {

        min = Math.min(o1.getFootprint().distance(o2.getFootprint()), min);

      }
    }
    // System.out.println(min);
    return min;

  }

}
