package fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses;

import java.awt.Color;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.representation.sample.ObjectCartoon;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.transformation.TransformBuilding;

public class LoadBatiIndustriel {

  private static Color Pistache = new Color(190, 245, 116);
  private static Color RoseVif = new Color(255, 0, 127);
  private static Color VERTDEAU = new Color(176, 242, 182);
  private static Color Marine = new Color(3, 34, 76);

  public static VectorLayer load(IPopulation<IFeature> featCol, DTM mnt) {
    int nbElem = featCol.size();

    for (int i = 0; i < nbElem; i++) {

      // On récupère les informations relatives à chaque éléments
      IFeature feat = featCol.get(i);
      IGeometry geom = feat.getGeom();

      try {
        feat.setGeom(TransformBuilding.createBDTopoBuilding(geom, mnt));

      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }


      String nature = "";
      Object p = feat.getAttribute("nature");
      
      
      if(p == null){
        p = feat.getAttribute("NATURE");
      }
      
      if(p != null){
        nature = p.toString();
      }
      
      
      if (nature.equalsIgnoreCase("BÃ¢timent agricole")) {

        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiIndustriel.Pistache));

      } else if (nature.equalsIgnoreCase("BÃ¢timent industriel")) {

        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiIndustriel.RoseVif));

      } else if (nature.equalsIgnoreCase("BÃ¢timent commercial")) {

        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiIndustriel.Marine));

      } else if (nature.equalsIgnoreCase("Silo")) {

        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiIndustriel.Pistache));

      } else if (nature.equalsIgnoreCase("Serre")) {

        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiIndustriel.VERTDEAU, Color.white, 1, 0.5));

      } else {
        //Nature inconnue

        feat.setRepresentation(new ObjectCartoon(feat, Color.orange));

      }

    }

    return new VectorLayer(featCol, "Bati_indus");

  }
}
