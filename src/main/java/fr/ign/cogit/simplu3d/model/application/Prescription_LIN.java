package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.ICurve;

/**
 * 
 * @author MBrasebin
 *
 */
public class Prescription_LIN extends Prescription {
  



  public Prescription_LIN(int type, ICurve geom) {
    super(type);

    this.geom = geom;
  }
  
  

}
