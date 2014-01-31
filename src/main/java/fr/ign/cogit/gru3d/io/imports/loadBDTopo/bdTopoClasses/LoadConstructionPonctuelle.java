package fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses;

import java.awt.Color;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion3DObject;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object1d;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;

public class LoadConstructionPonctuelle {

  public static VectorLayer load(IPopulation<IFeature> featCol, DTM mnt) {
    int nbElem = featCol.size();

    for (int i = 0; i < nbElem; i++) {
      // On récupère les informations relatives à chaque éléments
      IFeature feat = featCol.get(i);
      IGeometry geom = feat.getGeom();

      Box3D b = new Box3D(geom);

      IDirectPosition dpTemp = mnt.cast(b.getCenter());

      double heigth = b.getLLDP().getZ() - dpTemp.getZ();

      geom = Extrusion3DObject.conversionFromGeom(geom, -1 * (heigth));
      feat.setGeom(geom);

      
      Object o = feat.getAttribute("nature");
      
      if(o == null){
        o = feat.getAttribute("nature");
      }
      String nature  = "";
      if(o!=null){
         nature = o.toString();
      }
      
     

      if (nature.equalsIgnoreCase("IndiffÃ©renciÃ©")) {

        feat.setRepresentation(new Object1d(feat, Color.orange));

      } else if (nature.equalsIgnoreCase("Antenne")) {

        feat.setRepresentation(new Object1d(feat, Color.gray));

      } else if (nature.equalsIgnoreCase("CheminÃ©e")) {

        feat.setRepresentation(new Object1d(feat, Color.red));

      } else {
        feat.setRepresentation(new Object1d(feat, Color.orange));

      }

    }

    return new VectorLayer(featCol, "Construction ponctuelle");

  }
}
