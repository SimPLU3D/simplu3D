package fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses;

import java.awt.Color;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.representation.sample.ObjectCartoon;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.transformation.TransformBuilding;

public class LoadBatiIndifferencie {

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

      feat.setRepresentation(new ObjectCartoon(feat, Color.orange));

    }

    
    VectorLayer vl = new VectorLayer(featCol, "Bati_indif");
    featCol.clear();
    
    return vl;
  }
}
