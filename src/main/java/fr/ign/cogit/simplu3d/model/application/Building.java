package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.feature.IFeature;

public class Building extends AbstractBuilding {

  @Override
  public AbstractBuilding clone() {

    Building b = new Building();
    IFeature dF;
    try {
      dF = this.cloneGeom();
      b.setGeom(dF.getGeom());
    } catch (CloneNotSupportedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return b;

  }

}
