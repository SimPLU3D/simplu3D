package fr.ign.cogit.simplu3d.calculation;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.simplu3d.model.application.Toit;
import fr.ign.cogit.simplu3d.util.AngleFromSurface;

public class RoofAngle {

  public static double angleMin(Toit t) {

    double angleMin = Double.POSITIVE_INFINITY;
    for (IOrientableSurface o : t.getLod2MultiSurface()) {

      angleMin = Math.min(angleMin, AngleFromSurface.calculate(o));
    }

    return angleMin;

  }

  public static double angleMax(Toit t) {

    double angleMax = Double.POSITIVE_INFINITY;
    for (IOrientableSurface o : t.getLod2MultiSurface()) {

      angleMax = Math.max(angleMax, AngleFromSurface.calculate(o));
    }

    return angleMax;

  }

}
