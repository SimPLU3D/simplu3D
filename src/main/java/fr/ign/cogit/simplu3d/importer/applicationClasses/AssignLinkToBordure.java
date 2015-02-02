package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.util.Collection;
import java.util.Iterator;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;

public class AssignLinkToBordure {

  public static void process(IFeatureCollection<CadastralParcel> cadastralParcels,
      IFeatureCollection<Road> voiries) {

    if (!cadastralParcels.hasSpatialIndex()) {
      cadastralParcels.initSpatialIndex(Tiling.class, false);
    }
    if (!voiries.hasSpatialIndex()) {
      voiries.initSpatialIndex(Tiling.class, false);
    }

    for (CadastralParcel sP : cadastralParcels) {

      IFeatureCollection<SpecificCadastralBoundary> bordures = sP
          .getSpecificCadastralBoundary();

      for (SpecificCadastralBoundary b : bordures) {

        // 2 cas : c'est une bordure avec voirie
        if (b.getType() == SpecificCadastralBoundary.ROAD) {

          b.setFeatAdj(retrieveVoirie(b, voiries));
          continue;
        }

        // Sinon c'est un lien avec une autre sous Parceller
        CadastralParcel sPOut = retrieveSousParcelle(b, sP, cadastralParcels);
        if (sP == null) {

          System.out.println("La sousParcelle est nulle Oo");

        } else {

          b.setFeatAdj(sPOut);

        }

      }

    }

  }

  private static CadastralParcel retrieveSousParcelle(
      SpecificCadastralBoundary b, CadastralParcel sousParcelleIni,
      IFeatureCollection<CadastralParcel> parcelles) {

    Collection<CadastralParcel> sP = parcelles.select(b.getGeom(), 0);

    if (sP.size() < 2) {

      sP = parcelles.select(b.getGeom(), 0.3);
    }

    if (sP.size() < 2) {
      System.out.println("Error in sousParcelle selection");
    }

    Iterator<CadastralParcel> itP = sP.iterator();

    if (sP.size() == 2) {

      CadastralParcel spC = itP.next();

      if (spC == sousParcelleIni) {
        return itP.next();
      }

      return spC;

    }

    while (itP.hasNext()) {
      CadastralParcel sousParcelle = itP.next();

      if (sousParcelle == sousParcelleIni) {
        continue;
      }

      IFeatureCollection<SpecificCadastralBoundary> lB = sousParcelle
          .getSpecificCadastralBoundary();

      for (SpecificCadastralBoundary b2 : lB) {

        IGeometry geom = b.getGeom().intersection(b2.getGeom());

        if (geom.dimension() == 2) {
          return sousParcelle;
        }

      }

    }

    return null;

  }

  private static Road retrieveVoirie(SpecificCadastralBoundary b,
      IFeatureCollection<Road> voiries) {

    IGeometry geomB = b.getGeom();

    IGeometry buffer = geomB.buffer(10);

    Collection<Road> collVoirie = voiries.select(buffer);

    if (collVoirie.size() == 0) {
      return null;
    }

    if (collVoirie.size() == 1) {

      return collVoirie.iterator().next();
    }

    IDirectPositionList dpl = geomB.coord();

    Vecteur v = new Vecteur(dpl.get(0), dpl.get(dpl.size() - 1));

    v.normalise();

    Iterator<Road> it = collVoirie.iterator();

    Road bestR = null;
    double bestScore = Double.NEGATIVE_INFINITY;

    while (it.hasNext()) {

      Road vCandidate = it.next();
      IGeometry geomB2 = b.getGeom();
      IDirectPositionList dpl2 = geomB2.coord();

      Vecteur v2 = new Vecteur(dpl2.get(0), dpl2.get(dpl2.size() - 1));
      v2.normalise();

      double cos = Math.abs(v.prodScalaire(v2));

      if (cos > Math.cos(Math.PI / 5)) {

        // à voir .....
        if (cos > bestScore) {
          bestR = vCandidate;
          bestScore = cos;

        }

      }

    }

    return bestR;

  }

}
