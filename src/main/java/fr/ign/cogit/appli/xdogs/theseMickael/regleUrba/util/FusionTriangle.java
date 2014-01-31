package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.util;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.sig3d.equation.ApproximatedPlanEquation;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;

/**
 * Classe permettant de générer des solides à partir de triangle pour mieux
 * présenter les résultats issus d'opérateurs booléens
 * 
 * @author MBrasebin
 */
public class FusionTriangle {

  private final static double PRECISION = 0.05;

  public static List<IOrientableSurface> fusionnePolygonne(
      List<IOrientableSurface> lOS) {

    int nbSurfaces = lOS.size();

    List<Vecteur> lNormales = new ArrayList<Vecteur>();
    List<List<IOrientableSurface>> lGroupesSurface = new ArrayList<List<IOrientableSurface>>();

    int nbNormales = 0;
    // On tri les faces par normales
    bouclesurface: for (int i = 0; i < nbSurfaces; i++) {

      IOrientableSurface surf = lOS.get(i);

      ApproximatedPlanEquation eq = new ApproximatedPlanEquation(surf);
      Vecteur v = eq.getNormale();

      // On regarde si un groupe existe déjà
      for (int j = 0; j < nbNormales; j++) {

        Vecteur vTemp = lNormales.get(j);
        double pv = vTemp.prodVectoriel(v).norme();

        double proS = vTemp.prodScalaire(v);

        if (Double.isNaN(proS)) {
          continue bouclesurface;
        }
        // On trouve le groupe, on l'ajoute
        if (pv < 0.2 && vTemp.prodScalaire(v) > -0.1) {
          lGroupesSurface.get(j).add(surf);
          continue bouclesurface;
        }
      }

      // On crée un nouveau groupe
      List<IOrientableSurface> groupeNouveau = new ArrayList<IOrientableSurface>();
      groupeNouveau.add(surf);
      lGroupesSurface.add(groupeNouveau);
      nbNormales++;
      lNormales.add(v);

    }

    List<IOrientableSurface> polygonnesOk = new ArrayList<IOrientableSurface>(
        nbNormales);

    // Nous avons les groupes, nous allons traiter chacun d'entre eux

    for (int i = 0; i < nbNormales; i++) {

      List<IOrientableSurface> groupe = lGroupesSurface.get(i);

      int nbElem = groupe.size();

      List<IOrientableCurve> lCurveAtraiter = new ArrayList<IOrientableCurve>();

      for (int j = 0; j < nbElem; j++) {

        IOrientableSurface surfActu = groupe.get(j);
        lCurveAtraiter.addAll(surfActu.boundary().getExterior().getGenerator());

      }

      List<IOrientableCurve> lCurveAGarder = new ArrayList<IOrientableCurve>();

      lCurveAtraiter = FusionTriangle.prepareLCurve(lCurveAtraiter);

      int nbAgarder = 0;

      for (int j = 0; j < lCurveAtraiter.size(); j++) {

        IOrientableCurve cActu = lCurveAtraiter.get(j);

        boolean exist = false;

        for (int l = j + 1; l < lCurveAtraiter.size(); l++) {

          IOrientableCurve cPreparee = lCurveAtraiter.get(l);

          if (FusionTriangle.sameCurve(cPreparee, cActu)) {

            exist = true;
            lCurveAtraiter.remove(l);
            l--;

          }

        }

        if (!exist) {

          nbAgarder++;
          lCurveAGarder.add(cActu);
        }
      }

      if (lCurveAGarder.size() != 0) {

        polygonnesOk.addAll(FusionTriangle.attacheLignes(lCurveAGarder));
      }

    }

    return polygonnesOk;

  }

  private static List<IOrientableCurve> prepareLCurve(List<IOrientableCurve> lC) {
    int nbCurv = lC.size();
    List<IOrientableCurve> lCurvF = new ArrayList<IOrientableCurve>();
    for (int i = 0; i < nbCurv; i++) {
      lCurvF.addAll(FusionTriangle.prepareCurve(lC.get(i)));

    }

    return lCurvF;

  }

  private static List<IOrientableCurve> prepareCurve(IOrientableCurve c) {
    IDirectPositionList dpl = c.coord();
    List<IOrientableCurve> lC = new ArrayList<IOrientableCurve>();
    int nbP = dpl.size();

    for (int i = 0; i < nbP - 1; i++) {

      DirectPositionList dplTemp = new DirectPositionList();
      dplTemp.add(dpl.get(i));
      dplTemp.add(dpl.get(i + 1));

      lC.add(new GM_LineString(dplTemp));
    }

    return lC;
  }

  private static List<IPolygon> attacheLignes(List<IOrientableCurve> lC) {

    List<IPolygon> lPolygones = new ArrayList<IPolygon>();
    DirectPositionList lPolyEnCours = new DirectPositionList();

    IOrientableCurve curveActu = lC.remove(0);
    lPolyEnCours.addAll(curveActu.coord());

    IDirectPosition dpIni = curveActu.coord().get(0);
    IDirectPosition dpFin = curveActu.coord().get(curveActu.coord().size() - 1);

    for (int i = 0; i < lC.size(); i++) {
      IOrientableCurve curveTemp = lC.get(i);

      IDirectPosition dpIniTemp = curveTemp.coord().get(0);
      IDirectPosition dpFinTemp = curveTemp.coord().get(
          curveTemp.coord().size() - 1);

      if (dpIni.equals(dpIniTemp, FusionTriangle.PRECISION)) {

        lC.remove(i);
        i = -1;

        DirectPositionList dpTemp = (DirectPositionList) curveTemp.coord()
            .clone();
        dpTemp.remove(0);
        dpTemp.inverseOrdre();

        dpTemp.addAll(lPolyEnCours);

        lPolyEnCours = dpTemp;

        dpIni = dpTemp.get(0);

      } else

      if (dpIni.equals(dpFinTemp, FusionTriangle.PRECISION)) {

        lC.remove(i);
        i = -1;

        DirectPositionList dpTemp = (DirectPositionList) curveTemp.coord()
            .clone();
        dpTemp.remove(dpTemp.size() - 1);

        dpTemp.addAll(lPolyEnCours);

        lPolyEnCours = dpTemp;

        dpIni = dpTemp.get(0);

      } else

      if (dpFin.equals(dpIniTemp, FusionTriangle.PRECISION)) {

        lC.remove(i);
        i = -1;

        DirectPositionList dpTemp = (DirectPositionList) curveTemp.coord()
            .clone();
        dpTemp.remove(0);

        lPolyEnCours.addAll(dpTemp);

        dpFin = dpTemp.get(dpTemp.size() - 1);

      } else

      if (dpFin.equals(dpFinTemp, FusionTriangle.PRECISION)) {

        lC.remove(i);
        i = -1;

        DirectPositionList dpTemp = (DirectPositionList) curveTemp.coord()
            .clone();
        dpTemp.remove(0);
        dpTemp.inverseOrdre();

        lPolyEnCours.addAll(dpTemp);

        dpFin = dpTemp.get(dpTemp.size() - 1);
      }

      if (dpIni == dpFin) {
        break;
      }

    }

    if (lPolyEnCours.size() == 0) {

      return lPolygones;
    }

    if (dpIni != dpFin) {

      lPolyEnCours.add(lPolyEnCours.get(0));

    }

    lPolygones.add(new GM_Polygon(new GM_LineString(lPolyEnCours)));

    if (lC.size() != 0) {

      lPolygones.addAll(FusionTriangle.attacheLignes(lC));
    }

    return lPolygones;

  }

  private static boolean sameCurve(IOrientableCurve c1, IOrientableCurve c2) {

    IDirectPositionList dpl1 = c1.coord();
    IDirectPositionList dpl2 = c2.coord();

    if (dpl1.get(0).equals(dpl2.get(0), FusionTriangle.PRECISION)
        && dpl1.get(1).equals(dpl2.get(1), FusionTriangle.PRECISION)) {
      return true;
    }

    if (dpl1.get(1).equals(dpl2.get(0), FusionTriangle.PRECISION)
        && dpl1.get(0).equals(dpl2.get(1), FusionTriangle.PRECISION)) {
      return true;
    }

    return false;

    /*
     * int nbP1 = dpl1.size(); int nbP2 = dpl2.size(); if (nbP1 != nbP2) {
     * System.out.println("Pas même nombre de points"); return false; } boolean
     * diffTrouve = false; for (int i = 0; i < nbP1; i++) { if (!
     * dpl1.get(i).equals(dpl2.get(i), PRECISION)) { diffTrouve = true; break; }
     * } if (!diffTrouve) { return true; } for (int i = 0; i < nbP1; i++) { if
     * (!dpl1.get(i).equals(dpl2.get(nbP1 - 1 - i), PRECISION)) { diffTrouve =
     * true; break; } } if (!diffTrouve) { return true; } return false;
     */

  }

}
