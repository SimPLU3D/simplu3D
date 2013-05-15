package fr.ign.cogit.simplu3d.util;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.sig3d.equation.ApproximatedPlanEquation;

public class AngleFromSurface {

  public static double calculate(IOrientableSurface o) {
    ApproximatedPlanEquation ep = new ApproximatedPlanEquation(o);

    Vecteur v = ep.getNormale();

    v.normalise();

    double z = v.getZ();

    v.setZ(0);

    double norme = v.norme();

    double angleTemp = 0;
    if (norme != 0) {
      angleTemp = Math.PI / 2 - Math.atan(z / norme);
    }

    return angleTemp;
  }

}
