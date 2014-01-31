package fr.ign.cogit.appli.xdogs.theseMickael.indicators.oneBuildingPart.dim3;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.contextIndicator.ContextIndicator;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class VolumeArea extends ContextIndicator {

  private static String ATT_NAME = "VOL_AREA";

  public VolumeArea(IFeature feat, IFeatureCollection<IFeature> context) {
    super(feat, context);

    double totB = 0;

    for (IFeature featC : context) {

      try {
        Building b = new Building(featC);
        Volume vol = new Volume(b);

        totB = totB + vol.getValue();
      } catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
      } finally {

        Box3D b = new Box3D(featC.getGeom());

        double h = b.getURDP().getZ() - b.getLLDP().getZ();

        double area = featC.getGeom().area() / 2;

        totB = totB + area * h;

      }

    }

    if (totB == 0) {

      AttributeManager.addAttribute(feat, ATT_NAME, Double.POSITIVE_INFINITY,
          "Double");

    }

    AttributeManager.addAttribute(feat, ATT_NAME, totB, "Double");

  }

  public static void modifyATTNAme(String attName) {
    ATT_NAME = attName;
  }

}
