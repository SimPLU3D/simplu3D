package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.util.Date;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;

public class ZonesImporter {

  public final static String NOM_ATT_NAME_ZONE = "TYPEZONE";
  public final static String NOM_ATT_TEXT_ZONE = "TEXT";
  public final static String NOM_VALIDITY_DATE = "DATE";

  public static IFeatureCollection<UrbaZone> importUrbaZone(
      IFeatureCollection<IFeature> zoneColl) {

    IFeatureCollection<UrbaZone> zones = new FT_FeatureCollection<UrbaZone>();
    for (IFeature feat : zoneColl) {

      UrbaZone z = new UrbaZone(FromGeomToSurface.convertMSGeom(feat.getGeom())
          .get(0));

      Object o = feat.getAttribute(NOM_ATT_NAME_ZONE);

      if (o != null) {
        z.setName(o.toString());
      }

      o = feat.getAttribute(NOM_ATT_TEXT_ZONE);

      if (o != null) {
        z.setText(o.toString());
      }

      o = feat.getAttribute(NOM_VALIDITY_DATE);

      if (o != null) {

        z.setDate(new Date(o.toString()));
      }
      
      z.setGeom(feat.getGeom());

      zones.add(z);

    }

    return zones;

  }

}
