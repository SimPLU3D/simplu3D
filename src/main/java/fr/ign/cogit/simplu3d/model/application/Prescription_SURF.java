package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;

public class Prescription_SURF extends Prescription{
  
  
  public Prescription_SURF(int type, IOrientableSurface geom) {
    super(type);
    this.geom = geom;
  }

  private IOrientableSurface geom;

  public IOrientableSurface geom() {
    return geom;
  }
  
  
  
  
}
