package fr.ign.cogit.simplu3d.calculation;

import fr.ign.cogit.simplu3d.calculation.COSCalculation.METHOD;
import fr.ign.cogit.simplu3d.model.application.Batiment;
import fr.ign.cogit.simplu3d.model.application.SubParcel;

public class CESCalculation {

  public static double assess(SubParcel p, METHOD m) {

    double area = p.getLod2MultiSurface().area();
    double aireBatie = 0;
   
    
    for(Batiment b:p.getBuilding()){
      aireBatie = aireBatie + b.getEmprise().getLod2MultiSurface().area();
    }
    
    return aireBatie/area;
    
  }
}
