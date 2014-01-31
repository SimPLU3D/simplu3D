package fr.ign.cogit.appli.xdogs.theseMickael.io.imports.loadBDTopo.bdTopoClasses;

import java.awt.Color;

import fr.ign.cogit.appli.xdogs.theseMickael.io.imports.loadBDTopo.transformation.TransformBuilding;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.representation.sample.ObjectCartoon;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;

public class LoadBatiRemarquable {

  private static Color ALEZAN = new Color(167, 103, 38);
  private static Color AMETHYSTE = new Color(136, 77, 167);
  private static Color BEIGEASSE = new Color(175, 167, 123);
  private static Color CELADON = new Color(131, 166, 151);
  private static Color BLEU_ROI = new Color(49, 140, 231);
  private static Color BORDEAUX = new Color(109, 7, 26);

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
      

      if (nature.equalsIgnoreCase("Gare")) {

        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiRemarquable.ALEZAN));

      } else if (nature.equalsIgnoreCase("BÃ¢timent religieux divers")) {

        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiRemarquable.AMETHYSTE));

      } else if (nature.equalsIgnoreCase("Eglise")) {

        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiRemarquable.AMETHYSTE));

      } else if (nature.equalsIgnoreCase("BÃ¢timent sportif")) {

        feat.setRepresentation(new ObjectCartoon(feat, Color.orange));

      } else if (nature.equalsIgnoreCase("Fort, blockhaus, casemate")) {

        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiRemarquable.BEIGEASSE));

      } else if (nature.equalsIgnoreCase("Mairie")) {

        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiRemarquable.CELADON));

      } else if (nature.equalsIgnoreCase("Tribune")) {
        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiRemarquable.BLEU_ROI));

      } else if (nature.equalsIgnoreCase("Tour, donjon, moulin")) {
        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiRemarquable.BORDEAUX));

      } else if (nature.equalsIgnoreCase("Chapelle")) {

        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiRemarquable.AMETHYSTE));

      } else if (nature.equalsIgnoreCase("ChÃ¢teau")) {

        feat.setRepresentation(new ObjectCartoon(feat,
            LoadBatiRemarquable.BEIGEASSE));

      } else {
        System.out.println("Nature inconnue : " + nature);

        feat.setRepresentation(new ObjectCartoon(feat, Color.orange));

      }

    }

    return new VectorLayer(featCol, "Bati_remarquable");

  }
}
