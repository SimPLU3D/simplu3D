package fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses;

import java.awt.Color;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;

public class LoadSurfaceEau {

  public static VectorLayer load(IPopulation<IFeature> featCol, DTM mnt)
      throws Exception {
    return new VectorLayer(mnt.mapFeatureCollection(featCol, true),
        "surface_eau.shp", Color.cyan);
  }
}
