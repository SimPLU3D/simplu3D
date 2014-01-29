package fr.ign.cogit.simplu3d.indicator;

import java.util.List;

import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.SubParcel;

public class COSCalculation {

  public static enum METHOD {
    SIMPLE, FLOOR_CUT
  }

  public static double assess(SubParcel p, METHOD m) {
    double area = p.getLod2MultiSurface().area();
    return assess(p.getBuildingsParts().getElements(), m, area);
  }

  public static double assess(BasicPropertyUnit bPu, METHOD m) {

    double area = bPu.getpol2D().area();

    return assess(bPu.getBuildings(), m, area);
  }

  public static double assess(List<? extends AbstractBuilding> lBuildings,
      METHOD m, double area) {

    double aireBatie = 0;

    switch (m) {

      case SIMPLE:
        aireBatie = SHONCalculation.assessSimpleAireBati(lBuildings);
        break;
      case FLOOR_CUT:
        aireBatie = SHONCalculation.assessAireBatieFromCut(lBuildings);
        break;
    }

    return aireBatie / area;
  }

}
