package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.util.Iterator;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;

public class BuildingImporter {

  /**
   * Aire minimale pour considérée un polygone comme attaché à une parcelle
   */
  public final static double RATIO_MIN = 0.8;

  public static IFeatureCollection<Building> importBuilding(
      IFeatureCollection<IFeature> featBati,
      IFeatureCollection<BasicPropertyUnit> collBPU) {

    IFeatureCollection<Building> batiments = new FT_FeatureCollection<Building>();

    for (IFeature batiFeat : featBati) {

      // On crée le bâtiment
      Building b = new Building(batiFeat.getGeom());
      batiments.add(b);

      IGeometry poly = b.getFootprint();

      // Etape3 : on associe le bâtiment à la sous parcelles
      IFeatureCollection<IFeature> featTemp = new FT_FeatureCollection<IFeature>();

      for (BasicPropertyUnit bPU : collBPU) {

        IMultiSurface<IOrientableSurface> iMSTemp = new GM_MultiSurface<IOrientableSurface>();

        for (CadastralParcel bP : bPU.getCadastralParcel()) {
          iMSTemp.addAll(FromGeomToSurface.convertGeom(bP.getGeom()));

        }
        featTemp.add(new DefaultFeature(iMSTemp));
      }

      if (!featTemp.hasSpatialIndex()) {

        featTemp.initSpatialIndex(Tiling.class, false);

      }

      Iterator<IFeature> itSP = featTemp.select(poly).iterator();

      double aireEmprise = poly.area();

      boolean isAttached = false;

      while (itSP.hasNext()) {

        IFeature sp = itSP.next();

        double area = (poly.intersection(sp.getGeom())).area();

   //     if (area / aireEmprise > RATIO_MIN) {

          int index = featTemp.getElements().indexOf(sp);
          collBPU.get(index).getBuildings().add(b);
          isAttached = true;
System.out.println("Desactiver le hack de la classe BuildingImporter");
       //   break;

   //     }

      }

      if (!isAttached) {
        System.out.println("Bâtiment hors unité foncière");
      }

    }

    return batiments;

  }

}
