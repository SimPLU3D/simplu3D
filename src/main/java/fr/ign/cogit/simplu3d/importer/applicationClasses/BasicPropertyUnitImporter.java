package fr.ign.cogit.simplu3d.importer.applicationClasses;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;

public class BasicPropertyUnitImporter {

  public static IFeatureCollection<BasicPropertyUnit> importBPU(
      IFeatureCollection<CadastralParcel> cP) {

    IFeatureCollection<BasicPropertyUnit> bPU = new FT_FeatureCollection<BasicPropertyUnit>();

    for (CadastralParcel c : cP) {
      BasicPropertyUnit bP = new BasicPropertyUnit();
      bP.getCadastralParcel().add(c);
      c.setbPU(bP);
      bPU.add(bP);

      IMultiSurface<IOrientableSurface> geom = new GM_MultiSurface<>();
      geom.addAll(FromGeomToSurface.convertGeom(c.getGeom()));

      bP.setGeom(geom);
    }

    return bPU;
  }

}
