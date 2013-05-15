package fr.ign.cogit.simplu3d.calculation;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.sig3d.equation.ApproximatedPlanEquation;
import fr.ign.cogit.sig3d.semantic.MNTAire;
import fr.ign.cogit.simplu3d.model.application.SousParcelle;

public class AverageSlope {

  public static double averageSlope(SousParcelle p, MNTAire dtm)
      throws Exception {

    Vecteur vTot = new Vecteur(0, 0, 0);

    for (IOrientableSurface o : p.getLod2MultiSurface()) {

      double area3DTemp = dtm.calcul3DArea(o);

      ApproximatedPlanEquation ep = new ApproximatedPlanEquation(o);

      Vecteur v = ep.getNormale();

      v.normalise();

      if (v.getZ() < 0) {
        v.multConstante(-area3DTemp);
      } else {
        v.multConstante(area3DTemp);
      }

      vTot = vTot.ajoute(v);

    }

    double z = vTot.getZ();

    vTot.setZ(0);

    double norme = vTot.norme();

    if (norme == 0) {
      return 0;
    }

    return Math.PI / 2 - Math.atan(z / norme);

  }

}
