package fr.ign.cogit.simplu3d.calculation;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.equation.PlanEquation;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.sig3d.calculation.CutBuilding;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;

public class SHONCalculation {

  // METHODE
  public static double COEF_PLANCHER_PAR_ETAGE = 0.8;

  // COMPLEXE
  public static double HABITABLE_HAUTEUR = 1.8;

  public static enum METHOD {
    SIMPLE, FLOOR_CUT
  }

  public static double assess(SubParcel p, METHOD m) {

    double aireBatie = 0;

    switch (m) {

      case SIMPLE:
        aireBatie = SHONCalculation.assessSimpleAireBati(p);
      case FLOOR_CUT:
        aireBatie = SHONCalculation.assessAireBatieFromCut(p);
    }

    return aireBatie;
  }

  public static double assessSimpleAireBati(SubParcel p) {

    double aireBatie = 0;

    for (AbstractBuilding b : p.getBuildingsParts()) {

      aireBatie = aireBatie + assessSimpleSHON(b);
    }

    return aireBatie;

  }

  public static double assessAireBatieFromCut(SubParcel p) {

    double aireBatie = 0;

    for (AbstractBuilding b : p.getBuildingsParts()) {

      aireBatie = aireBatie + assessCUTSHON(b);
    }

    return aireBatie;

  }

  public static double assessSimpleSHON(AbstractBuilding bati) {

    double aireBatie = 0;

    RoofSurface t = bati.getToit();

    if (t == null) {

      System.out.println("ERROR COS : rajouter cas ou pas de toit");

      return 0;
    }

    int nbEtage = bati.getStoreysAboveGround();

    aireBatie = bati.getFootprint().area() * nbEtage * COEF_PLANCHER_PAR_ETAGE;

    return aireBatie;
  }

  public static List<IOrientableSurface> DEBUG = new ArrayList<IOrientableSurface>();

  public static double assessCUTSHON(AbstractBuilding bati) {

    double hauteurEtage = 0;

    if (StoreyCalculation.USE_STOREYS_HEIGH_ATT) {
      hauteurEtage = bati.getStoreyHeightsAboveGround();
    }

    if (hauteurEtage < 0.1) {
      hauteurEtage = StoreyCalculation.HAUTEUR_ETAGE;
    }

    List<IOrientableSurface> lOS = new ArrayList<IOrientableSurface>();
    lOS.addAll(bati.getLod2MultiSurface());

    Box3D b = new Box3D(bati.getLod2MultiSurface());

    double zMin = b.getLLDP().getZ();
    double zMax = b.getURDP().getZ();

    int nbFloor = StoreyCalculation.process(bati);

    if (nbFloor == 0) {
      nbFloor = 1;
    }

    double zActu = zMin;
    double areaSHON = 0;

    for (int i = 0; i < nbFloor; i++) {

      PlanEquation eq = new PlanEquation(0, 0, -1, zActu + HABITABLE_HAUTEUR);
      List<IOrientableSurface> lG = CutBuilding.cutWithPPPolygon(lOS, eq);

      if (lG != null && lG.size() > 0) {

        DEBUG.addAll(lG);

      }

      double areaTemp = calculAreaFromCut(lG);

      if (areaTemp == -1) {
        return -1;
      }

      zActu = zActu + hauteurEtage;

      areaSHON = areaSHON + areaTemp;

    }

    if (areaSHON == 0 && nbFloor == 0 && (zMax - zMax) > HABITABLE_HAUTEUR) {
      PlanEquation eq = new PlanEquation(0, 0, -1, (zMax + zMax) / 2);

      List<IOrientableSurface> lG = CutBuilding.cutWithPPPolygon(lOS, eq);

      double areaTemp = calculAreaFromCut(lG);

      areaSHON = areaTemp;

    }

    return areaSHON * COEF_PLANCHER_PAR_ETAGE;

  }

  /**
   * 
   * @param lG
   * @return
   */
  private static double calculAreaFromCut(List<IOrientableSurface> lG) {
    double areaTemp = 0;

    if (lG == null) {
      return -1;
    }

    int nbGeom = lG.size();
    for (int j = 0; j < nbGeom; j++) {

      IGeometry gT = lG.get(j);

      if (gT instanceof IPolygon) {
        if (gT.isValid()) {
          areaTemp = areaTemp + gT.area();
        } /*
           * else { return -1;
           * 
           * }
           */

      } /*
         * else { areaTemp = -1; return areaTemp; }
         */

    }

    return areaTemp;
  }
}
