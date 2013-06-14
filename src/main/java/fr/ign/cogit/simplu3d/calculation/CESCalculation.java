package fr.ign.cogit.simplu3d.calculation;

import fr.ign.cogit.simplu3d.calculation.COSCalculation.METHOD;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application._AbstractBuilding;

public class CESCalculation {

  public static double assess(SubParcel p, METHOD m) {

    double area = p.getLod2MultiSurface().area();
    double aireBatie = 0;
   
    
    for(_AbstractBuilding b:p.getBuildingsParts()){
      aireBatie = aireBatie + b.getFootprint().area();
    }
    
    return aireBatie/area;
    
  }
}
