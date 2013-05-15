package fr.ign.cogit.simplu3d.calculation;

import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.simplu3d.calculation.util.PointBasType;
import fr.ign.cogit.simplu3d.model.application.Batiment;
import fr.ign.cogit.simplu3d.model.application.Bordure;
import fr.ign.cogit.simplu3d.model.application.SousParcelle;

public class HauteurCalculation {

  public enum POINT_HAUT_TYPE {
    PLUS_HAUT_EGOUT, PLUS_HAUT_FAITAGE, PLANCHER_PLUS_ELEVE
  };


 
  public static double calculate(Batiment b, int type_pb,
      POINT_HAUT_TYPE type_ph) {

    double zBas = calculateZBas(b, type_pb);

    double zHaut = calculateZHaut(b, type_ph);

    return zHaut - zBas;

  }

  public static double calculateZHaut(Batiment b, POINT_HAUT_TYPE type_ph) {

    double zHaut = Double.NaN;

    switch (type_ph) {
      case PLANCHER_PLUS_ELEVE:
        zHaut = calculateZHautPPE(b);
        break;
      case PLUS_HAUT_EGOUT:
        zHaut = calculateZHautPHE(b);
        break;
      case PLUS_HAUT_FAITAGE:
        zHaut = calculateZHautPHF(b);
        break;

    }

    return zHaut;
  }

  public static double calculateZBas(Batiment b, Integer type_pb) {
    System.out.println(type_pb);
    return 1.0;
    
    /*
    double zBas = -1;

    if (type_pb == PointBasType.EMPRISE_PUBLIQUE) {
      zBas = calculateZBasEP(b);
    } 
    
    if (type_pb == PointBasType.PLUS_BAS_BATIMENT) {
      zBas = calculateZBasPBB(b);
    }
    
    if (type_pb == PointBasType.PLUS_BAS_TERRAIN) {
      zBas = calculateZBasPBT(b);
    }
    
    if (type_pb == PointBasType.PLUS_HAUT_TERRAIN) {
      zBas = calculateZBasPHT(b);
    }

    System.out.println(zBas);
    
    return zBas;*/
  }

  // //////////////////DIFFERENTS TYPES DE ZHAUT
  // // IL s'agit d'un Z et pas d'un H bien sur
  public static double calculateZHautPPE(Batiment b) {

    double hauteurParEtage = b.getStoreyHeightsAboveGround();

    if (hauteurParEtage <= 0 || !StoreyCalculation.USE_STOREYS_HEIGH_ATT) {
      hauteurParEtage = StoreyCalculation.HAUTEUR_ETAGE;
    }

    int nbEtage = StoreyCalculation.process(b);

    double hauteur = hauteurParEtage * nbEtage;

    Box3D box = new Box3D(b.getGeom());

    return hauteur + box.getLLDP().getZ();
  }

  public static double calculateZHautPHE(Batiment b) {

    IGeometry g = b.getToit().getGouttiere();

    Box3D box = new Box3D(g);

    return box.getURDP().getZ();
  }

  public static double calculateZHautPHF(Batiment b) {
    Box3D box = new Box3D(b.getGeom());
    return box.getURDP().getZ();
  }

  // //////////////////DIFFERENTS TYPES DE ZBAS

  private static double calculateZBasPHT(Batiment b) {

    List<SousParcelle> spList = b.getSousParcelles();

    double zMax = Double.NEGATIVE_INFINITY;

    for (SousParcelle sp : spList) {

      Box3D box = new Box3D(sp.getGeom());

      zMax = Math.max(zMax, box.getLLDP().getZ());

    }

    return zMax;
  }

  private static double calculateZBasPBT(Batiment b) {

    List<SousParcelle> spList = b.getSousParcelles();

    double zMin = Double.POSITIVE_INFINITY;

    for (SousParcelle sp : spList) {

      Box3D box = new Box3D(sp.getGeom());

      zMin = Math.min(zMin, box.getLLDP().getZ());

    }

    return zMin;
  }

  private static double calculateZBasPBB(Batiment b) {
    Box3D box = new Box3D(b.getGeom());
    return box.getLLDP().getZ();
  }

  private static double calculateZBasEP(Batiment b) {
    List<SousParcelle> spList = b.getSousParcelles();

    double zMin = Double.POSITIVE_INFINITY;

    for (SousParcelle sp : spList) {

      IFeatureCollection<Bordure> bordures = sp.getBordures();

      for (Bordure bord : bordures) {
        if (bord.getTypeDroit() == Bordure.VOIE
            || bord.getTypeDroit() == Bordure.EMPRISEPUBLIQUE) {

          Box3D box = new Box3D(bord.getGeom());

          zMin = Math.min(zMin, box.getLLDP().getZ());

        }

      }
    }

    if (zMin == Double.POSITIVE_INFINITY) {
      zMin = calculateZBasPBB(b);
    }

    return zMin;
  }

}
