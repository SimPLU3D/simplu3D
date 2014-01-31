package fr.ign.cogit.appli.xdogs.theseMickael.io.imports.loadBDTopo.bdTopoClasses;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;

public class LoadPylone {

  public static VectorLayer load(IPopulation<IFeature> featCol, DTM mnt) {
    int nbElem = featCol.size();

    for (int i = 0; i < nbElem; i++) {
      // On récupère les informations relatives à chaque éléments
      IFeature feat = featCol.get(i);
      IGeometry geom = feat.getGeom();

      IDirectPositionList dpl = geom.coord();

      List<GM_LineString> lS = new ArrayList<GM_LineString>();

      int nbP = dpl.size();

      for (int j = 0; j < nbP; j++) {

        IDirectPosition dp = dpl.get(j);
        IDirectPosition dp2 = mnt.cast(dp);

        DirectPositionList dplTemp = new DirectPositionList();
        dplTemp.add(dp);
        dplTemp.add(dp2);

        lS.add(new GM_LineString(dplTemp));

      }

      if (lS.size() == 1) {
        geom = lS.get(0);

      } else {

        geom = new GM_MultiCurve<GM_LineString>(lS);
      }

      feat.setGeom(geom);

    }

    return new VectorLayer(featCol, "Pylone", Color.black);
  }
}
