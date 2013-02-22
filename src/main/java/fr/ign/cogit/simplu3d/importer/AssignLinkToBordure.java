package fr.ign.cogit.simplu3d.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.model.application.Bordure;
import fr.ign.cogit.simplu3d.model.application.SousParcelle;
import fr.ign.cogit.simplu3d.model.application.Voirie;

public class AssignLinkToBordure {

  public static void process(IFeatureCollection<SousParcelle> sousParcelles,
      IFeatureCollection<Voirie> voiries) {

    if (!sousParcelles.hasSpatialIndex()) {
      sousParcelles.initSpatialIndex(Tiling.class, false);
    }
    if (!voiries.hasSpatialIndex()) {
      voiries.initSpatialIndex(Tiling.class, false);
    }

    for (SousParcelle sP : sousParcelles) {

      IFeatureCollection<Bordure> bordures = sP.getBordures();

      for (Bordure b : bordures) {

        // 2 cas : c'est une bordure avec voirie
        if (b.getTypeDroit() == Bordure.VOIE) {
          b.setFeatAdj(retrieveVoirie(b, voiries));
          continue;
        }

        

        // Sinon c'est un lien avec une autre sous Parceller
        SousParcelle sPOut = retrieveSousParcelle(b,sP,sousParcelles);
        if(sP == null){
        
          System.out.println("La sousParcelle est nulle Oo");
        
        }else{
          
          List<SousParcelle> slp = new ArrayList<SousParcelle>();
          slp.add(sPOut);
          
          b.setFeatAdj(slp);
          
        }

      }

    }

  }

  private static SousParcelle retrieveSousParcelle(Bordure b, SousParcelle sousParcelleIni,
      IFeatureCollection<SousParcelle> parcelles) {

    Collection<SousParcelle> sP = parcelles.select(b.getGeom(), 0);

    if (sP.size() < 2) {

      sP = parcelles.select(b.getGeom(), 0.3);
    }

    if (sP.size() < 2) {
      System.out.println("Error in sousParcelle selection");
    }

    Iterator<SousParcelle> itP = sP.iterator();

    if (sP.size() == 2) {

      SousParcelle spC = itP.next();

      if (spC == sousParcelleIni) {
        return itP.next();
      }

      return spC;

    }

    while (itP.hasNext()) {
      SousParcelle sousParcelle = itP.next();

      if (sousParcelle == sousParcelleIni) {
        continue;
      }

      IFeatureCollection<Bordure> lB = sousParcelle.getBordures();

      for (Bordure b2 : lB) {

        IGeometry geom = b.getGeom().intersection(b2.getGeom());

        if (geom.dimension() == 2) {
          return sousParcelle;
        }

      }

    }

    return null;

  }

  private static List<Voirie> retrieveVoirie(Bordure b,
      IFeatureCollection<Voirie> voiries) {

    IGeometry geomB = b.getGeom();

    List<Voirie> lV = new ArrayList<Voirie>();

    IGeometry buffer = geomB.buffer(10);

    Collection<Voirie> collVoirie = voiries.select(buffer);

    if (collVoirie.size() == 0) {
      return lV;
    }

    if (collVoirie.size() == 1) {
      lV.add(collVoirie.iterator().next());
      return lV;
    }

    IDirectPositionList dpl = geomB.coord();

    Vecteur v = new Vecteur(dpl.get(0), dpl.get(dpl.size() - 1));

    v.normalise();

    Iterator<Voirie> it = collVoirie.iterator();

    while (it.hasNext()) {

      Voirie vCandidate = it.next();
      IGeometry geomB2 = b.getGeom();
      IDirectPositionList dpl2 = geomB2.coord();

      Vecteur v2 = new Vecteur(dpl2.get(0), dpl2.get(dpl2.size() - 1));
      v2.normalise();

      double cos = Math.abs(v.prodScalaire(v2));

      if (cos > Math.cos(Math.PI / 5)) {
        lV.add(vCandidate);

      }

    }

    return lV;

  }

}
