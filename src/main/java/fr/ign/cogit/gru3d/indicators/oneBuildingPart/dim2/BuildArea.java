package fr.ign.cogit.gru3d.indicators.oneBuildingPart.dim2;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.contextIndicator.ContextIndicator;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class BuildArea extends ContextIndicator {

  private static String ATT_NAME = "Build_AREA";

  public BuildArea(IFeature feat, IFeatureCollection<IFeature> context) {
    super(feat, context);

    double totB = 0;

    for (IFeature featC : context) {

      try {
        Building b = new Building(featC);
        Area2D a2D = new Area2D(b);

        totB = totB + a2D.getValue();
      } catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
      } finally {

        totB = totB + featC.getGeom().area() / 2;

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
