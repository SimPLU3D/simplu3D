package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;

public class Prescription_PCT extends Prescription {
  


  public Prescription_PCT(int type, IPoint geom) {
    super(type);
    this.geom = geom;
  }
  
   

}
