package fr.ign.cogit.simplu3d.calculation;

import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application._AbstractBuilding;

public class HauteurCalculation {

  public enum POINT_HAUT_TYPE {
    PLUS_HAUT_EGOUT, PLUS_HAUT_FAITAGE, PLANCHER_PLUS_ELEVE
  };


 
  public static double calculate(_AbstractBuilding b, int type_pb,
      POINT_HAUT_TYPE type_ph) {

    double zBas = calculateZBas(b, type_pb);

    double zHaut = calculateZHaut(b, type_ph);

    return zHaut - zBas;

  }

  public static double calculateZHaut(_AbstractBuilding b, POINT_HAUT_TYPE type_ph) {

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

  public static double calculateZBas(_AbstractBuilding b, Integer type_pb) {
    System.out.println(type_pb);
    return 1.0;
    
    /*
    double zBas = -1;

    if (type_pb == PointBasType.EMPRISE_PUBLIQUE) {
      zBas = calculateZBasEP(b);
    } 
    
    if (type_pb == PointBasType.PLUS_BAS__AbstractBuilding) {
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
  public static double calculateZHautPPE(_AbstractBuilding b) {

    double hauteurParEtage = b.getStoreyHeightsAboveGround();

    if (hauteurParEtage <= 0 || !StoreyCalculation.USE_STOREYS_HEIGH_ATT) {
      hauteurParEtage = StoreyCalculation.HAUTEUR_ETAGE;
    }

    int nbEtage = StoreyCalculation.process(b);

    double hauteur = hauteurParEtage * nbEtage;

    Box3D box = new Box3D(b.getGeom());

    return hauteur + box.getLLDP().getZ();
  }

  public static double calculateZHautPHE(_AbstractBuilding b) {

    IGeometry g = b.getToit().setGutter();

    Box3D box = new Box3D(g);

    return box.getURDP().getZ();
  }

  public static double calculateZHautPHF(_AbstractBuilding b) {
    Box3D box = new Box3D(b.getGeom());
    return box.getURDP().getZ();
  }

  // //////////////////DIFFERENTS TYPES DE ZBAS

  private static double calculateZBasPHT(_AbstractBuilding b) {

    List<SubParcel> spList = b.getSousParcelles();

    double zMax = Double.NEGATIVE_INFINITY;

    for (SubParcel sp : spList) {

      Box3D box = new Box3D(sp.getGeom());

      zMax = Math.max(zMax, box.getLLDP().getZ());

    }

    return zMax;
  }

  private static double calculateZBasPBT(_AbstractBuilding b) {

    List<SubParcel> spList = b.getSousParcelles();

    double zMin = Double.POSITIVE_INFINITY;

    for (SubParcel sp : spList) {

      Box3D box = new Box3D(sp.getGeom());

      zMin = Math.min(zMin, box.getLLDP().getZ());

    }

    return zMin;
  }

  private static double calculateZBasPBB(_AbstractBuilding b) {
    Box3D box = new Box3D(b.getGeom());
    return box.getLLDP().getZ();
  }

  private static double calculateZBasEP(_AbstractBuilding b) {
    BasicPropertyUnit  spList = b.getbPU();

    double zMin = Double.POSITIVE_INFINITY;

    for (CadastralParcel sp : spList.cadastralParcel) {

      IFeatureCollection<SpecificCadastralBoundary> bordures = sp.getSpecificCadastralBoundary();

      for (SpecificCadastralBoundary bord : bordures) {
        if (bord.getType() == SpecificCadastralBoundary.ROAD
            || bord.getType() == SpecificCadastralBoundary.PUB) {

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
