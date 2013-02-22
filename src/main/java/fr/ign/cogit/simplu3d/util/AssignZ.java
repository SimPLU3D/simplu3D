package fr.ign.cogit.simplu3d.util;

import fr.ign.cogit.simplu3d.model.application.Bordure;
import fr.ign.cogit.simplu3d.model.application.Parcelle;
import fr.ign.cogit.simplu3d.model.application.SousParcelle;
import fr.ign.cogit.simplu3d.model.application.Voirie;
import fr.ign.cogit.simplu3d.model.application.Zone;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;

public class AssignZ {

  public static void toParcelle(IFeatureCollection<Parcelle> parcelles,
      DTM dtm, boolean sursampled) throws Exception {

    for (Parcelle p : parcelles) {

      IGeometry geom = dtm.mapGeom(p.getGeom(), 0, true, sursampled);
      p.setGeom(geom);

    }

  }

  public static void toSousParcelle(IFeatureCollection<SousParcelle> parcelles,
      DTM dtm, boolean sursampled) throws Exception {

    for (SousParcelle p : parcelles) {

      IGeometry geom = dtm.mapGeom(p.getGeom(), 0, true, sursampled);
      p.setGeom(geom);

      for (Bordure b : p.getBordures()) {

        IGeometry geomB = dtm.mapGeom(b.getGeom(), 0, true, sursampled);
        b.setGeom(geomB);

      }

    }

  }
  
  public static void toVoirie(IFeatureCollection<Voirie> voiries,
      DTM dtm, boolean sursampled) throws Exception {

    for (Voirie z : voiries) {

      IGeometry geom = dtm.mapGeom(z.getGeom(), 0, true, sursampled);
      z.setGeom(geom);

    }

  }

  
  public static void toZone(IFeatureCollection<Zone> zones,
      DTM dtm, boolean sursampled) throws Exception {

    for (Zone z : zones) {

      IGeometry geom = dtm.mapGeom(z.getGeom(), 0, true, sursampled);
      z.setGeom(geom);

    }

  }
}
