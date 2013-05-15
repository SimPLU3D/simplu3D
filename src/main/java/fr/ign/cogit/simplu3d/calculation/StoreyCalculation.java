package fr.ign.cogit.simplu3d.calculation;

import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.simplu3d.model.application.Batiment;

public class StoreyCalculation {

  public static double HAUTEUR_ETAGE = 3;

  public static boolean CONSIDER_ROOF = false;

  public static boolean USE_STOREYS_HEIGH_ATT = false;
  
  
  
  
    
  
  
  
  
  

  public static int process(Batiment batiment) {

    Box3D b = new Box3D(batiment.getGeom());

    double zMin = b.getLLDP().getZ();

    double zMax = 0;

    if (CONSIDER_ROOF) {
      zMax = b.getURDP().getZ();

    } else {

      Box3D b2 = new Box3D(batiment.getToit().getGeom());

      zMax = b2.getLLDP().getZ();

    }

    double hauteurEtage = 0;

    if (USE_STOREYS_HEIGH_ATT) {
      hauteurEtage = batiment.getStoreyHeightsAboveGround();
    }

    if (hauteurEtage < 0.1) {
      hauteurEtage = HAUTEUR_ETAGE;
    }

    int nbEtage = (int) ((zMax - zMin) / hauteurEtage);

    return Math.max(nbEtage, 1);
  }
}
