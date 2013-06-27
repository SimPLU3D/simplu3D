package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.feature.IFeature;

public class BuildingPart extends AbstractBuilding {

  public SubParcel sP;

  public SubParcel getsP() {
    return sP;
  }

  public void setsP(SubParcel sP) {
    this.sP = sP;
  }

  @Override
  public AbstractBuilding clone() {

    BuildingPart b = new BuildingPart();
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
