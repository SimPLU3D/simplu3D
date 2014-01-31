package fr.ign.cogit.appli.xdogs.theseMickael.io.imports.loadBDTopo.bdTopoClasses;

import java.awt.Color;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion3DObject;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;

public class LoadZoneVegetation {

  public static VectorLayer load(IPopulation<IFeature> featCol, DTM mnt) {
    int nbElem = featCol.size();

    for (int i = 0; i < nbElem; i++) {
      // On récupère les informations relatives à chaque éléments
      IFeature feat = featCol.get(i);
      IGeometry geom = feat.getGeom();

      double heigth = LoadZoneVegetation.calculH(feat);

      // On génère la géométrie
      try {
        geom = mnt.mapGeom(geom, 0, true, true);

        geom = Extrusion3DObject.conversionFromGeom(geom, heigth);

      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      feat.setGeom(geom);

      /*
       * String nature = feat.getAttribute("nature").toString();
       * 
       * if(nature.equalsIgnoreCase("Haie")){
       * 
       * 
       * }else if(nature.equalsIgnoreCase("Bois")){
       * 
       * 
       * }else if(nature.equalsIgnoreCase("ForÃªt fermÃ©e de feuillus")){
       * 
       * }else if(nature.equalsIgnoreCase("Lande ligneuse")){
       * 
       * 
       * }else{ System.out.println("Nature inconnue : "+nature);
       * 
       * 
       * }
       */

    }

    return new VectorLayer(featCol, "Végétation", new Color(133, 193, 126));

  }

  public static double calculH(IFeature feat) {
    Object o = feat.getAttribute("nature");
    String nature = "";

    if (o != null) {
      nature = o.toString();

    } else {
      o = feat.getAttribute("NATURE");
      if (o != null) {
        nature = o.toString();
      }

    }

    if (nature.equalsIgnoreCase("Haie")) {

      return 3.0;
    } else if (nature.equalsIgnoreCase("Bois")) {

      return 10.0;

    } else if (nature.equalsIgnoreCase("ForÃªt fermÃ©e de feuillus")) {

      return 15.0;

    } else if (nature.equalsIgnoreCase("Lande ligneuse")) {

      return 4.5;

    }

    return 10.0;

  }

}
