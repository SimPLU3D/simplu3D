package fr.ign.cogit.appli.xdogs.theseMickael.io.imports.loadBDTopo.bdTopoClasses;

import java.awt.Color;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;

public class LoadLigneElectrique {

  public static VectorLayer load(IPopulation<IFeature> featCol, DTM mnt) {

    return new VectorLayer(featCol, "Ligne électrique", Color.black);
  }

}
