package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.SubParcel;

public class CESCalculation {

  public static double assess(SubParcel p) {

    double area = p.getLod2MultiSurface().area();
    double aireBatie = 0;
   
    
    for(AbstractBuilding b:p.getBuildingsParts()){
      aireBatie = aireBatie + b.getFootprint().area();
    }
    
    return aireBatie/area;
    
  }
}
