package fr.ign.cogit.simplu3d.util;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.simplu3d.model.application.Alignement;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;
import fr.ign.cogit.simplu3d.model.application.Road;


/**
 * Classe pour affecter un z à différents types d'objets
 * 
 * @author MBrasebin
 */
public class AssignZ {

  public static void toParcelle(IFeatureCollection<CadastralParcel> parcelles,
      DTM dtm, boolean sursampled) throws Exception {

    for (CadastralParcel p : parcelles) {

      IGeometry geom = dtm.mapGeom(p.getGeom(), 0, true, sursampled);
      p.setGeom(geom);

      for (SpecificCadastralBoundary b : p.getBoundary()) {

        IGeometry geomB = dtm.mapGeom(b.getGeom(), 0, true, sursampled);
        b.setGeom(geomB);

      }

    }

  }

  public static void toSousParcelle(IFeatureCollection<SubParcel> parcelles,
      DTM dtm, boolean sursampled) throws Exception {

    for (SubParcel p : parcelles) {

      IGeometry geom = dtm.mapGeom(p.getGeom(), 0, true, sursampled);
      p.setGeom(geom);

    }

  }

  public static void toVoirie(IFeatureCollection<Road> voiries, DTM dtm,
      boolean sursampled) throws Exception {

    for (Road z : voiries) {

      IGeometry geom = dtm.mapGeom(z.getGeom(), 0, true, sursampled);
      z.setGeom(geom);

    }

  }

  public static void toZone(IFeatureCollection<UrbaZone> zones, DTM dtm,
      boolean sursampled) throws Exception {

    for (UrbaZone z : zones) {

      IGeometry geom = z.getGeom();
      
      for(IDirectPosition dp: geom.coord()){
        dp.setZ(0);
      }

    }

  }

  public static void toAlignement(
      IFeatureCollection<Alignement> alignementColl, DTM dtm, boolean sursampled)
      throws Exception {

    for (Alignement a : alignementColl) {

      IGeometry geom = dtm.mapGeom(a.getGeom(), 0, true, sursampled);
      a.setGeom(geom);
    }

  }
}
