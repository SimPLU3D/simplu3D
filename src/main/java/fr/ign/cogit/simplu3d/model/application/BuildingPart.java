package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;

public class BuildingPart extends AbstractBuilding {

  public BuildingPart(IGeometry geom) {
    super(geom);
  }

  public BuildingPart() {
    // TODO Auto-generated constructor stub
  }

  public SubParcel sP;

  public SubParcel getsP() {
    return sP;
  }

  public void setsP(SubParcel sP) {
    this.sP = sP;
  }

  @Override
  public AbstractBuilding clone() {

    BuildingPart b = new BuildingPart((IGeometry) this.getGeom().clone());
    b.isNew = this.isNew;

    return b;

  }

}
