package fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses;

import java.awt.Color;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion3DObject;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;

public class LoadReservoirDEau {

  public static VectorLayer load(IPopulation<IFeature> featCol, DTM mnt) {
    int nbElem = featCol.size();

    for (int i = 0; i < nbElem; i++) {
      // On récupère les informations relatives à chaque éléments
      IFeature feat = featCol.get(i);
      IGeometry geom = feat.getGeom();

      Box3D b = new Box3D(geom);

      double hRel = b.getURDP().getZ() - b.getLLDP().getZ();
      
      Object o = feat.getAttribute("hauteur");
      
      if(o == null){
        o = feat.getAttribute("HAUTEUR");
      }

      double heigth = 0;
      
      if(o != null){
        heigth = Double.parseDouble(o.toString());
      }
  

      geom = Extrusion3DObject.conversionFromGeom(geom, -1 * (heigth - hRel));
      feat.setGeom(geom);

    }

    return new VectorLayer(featCol, "Resrvoir_Eau", Color.cyan);

  }
}
