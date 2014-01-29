package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.simplu3d.model.application.SubParcel;

public class COSCalculation {

  public static enum METHOD {
    SIMPLE, FLOOR_CUT
  }

  public static double assess(SubParcel p, METHOD m) {

    double area = p.getLod2MultiSurface().area();
    double aireBatie = 0;

    switch (m) {

      case SIMPLE:
        aireBatie = SHONCalculation.assessSimpleAireBati(p);
        break;
      case FLOOR_CUT:
        aireBatie = SHONCalculation.assessAireBatieFromCut(p);
        break;
    }

    return aireBatie / area;
  }

}
