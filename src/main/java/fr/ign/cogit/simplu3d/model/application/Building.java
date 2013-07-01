package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;

public class Building extends AbstractBuilding {

  protected Building(){
    
  }
  public Building(IGeometry geom) {
    super(geom);

  }

  @Override
  public AbstractBuilding clone() {

    Building b = new Building((IGeometry) this.getGeom().clone());

    return b;

  }

}
