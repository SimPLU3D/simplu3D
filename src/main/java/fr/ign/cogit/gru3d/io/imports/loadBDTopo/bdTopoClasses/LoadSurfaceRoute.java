package fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses;

import java.awt.Color;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object2d;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;

public class LoadSurfaceRoute {

  public static VectorLayer load(IPopulation<IFeature> featCol, DTM mnt) {
    int nbElem = featCol.size();

    for (int i = 0; i < nbElem; i++) {
      // On récupère les informations relatives à chaque éléments
      IFeature feat = featCol.get(i);
      IGeometry geom = feat.getGeom();

      // On génère la géométrie
      try {
        geom = mnt.mapGeom(geom, 0, true, true);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      feat.setGeom(geom);
      
      
      Object  o = feat.getAttribute("nature");
      
      if( o == null){
        o =  feat.getAttribute("NATURE");
      }

      String nature = ""; 
      
      if(o != null){
        nature = o.toString();
      }
      


      if (nature.equalsIgnoreCase("Parking")) {

        feat.setRepresentation(new Object2d(feat, Color.gray));

      } else {

        System.out.println("Nature inconnue" + nature);
        continue;

      }

    }

    VectorLayer vl = new VectorLayer(featCol, "Route surfacique");
    featCol.clear();
    return vl;

  }

}
