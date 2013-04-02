package fr.ign.cogit.simplu3d.calculation;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.sig3d.semantic.MNTAire;
import fr.ign.cogit.simplu3d.model.application.SousParcelle;

public class AverageSlope {

  public static double averageSlope(SousParcelle p, MNTAire dtm) {

    int count = 0;
    double penteCumul = 0;

    for (IOrientableSurface o : p.getLod2MultiSurface()) {

    }

    return penteCumul / count;
  }

}
